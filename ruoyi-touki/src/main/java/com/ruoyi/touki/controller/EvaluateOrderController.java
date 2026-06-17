package com.ruoyi.touki.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.service.EvaluateOrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Touki
 */
@RestController
@RequestMapping("evaluate/order")
public class EvaluateOrderController extends BaseController {
    private final EvaluateOrderService evaluateOrderService;

    public EvaluateOrderController(EvaluateOrderService evaluateOrderService) {
        this.evaluateOrderService = evaluateOrderService;
    }

    @GetMapping("list")
    @PreAuthorize("@ss.hasPermi('evaluate:order:list')")
    public TableDataInfo list(EvaluateOrder evaluateOrder) {
        evaluateOrder.setDeleted(false);
        startPage();
        List<EvaluateOrder> list = evaluateOrderService.selectOrderList(evaluateOrder);
        return getDataTable(list);
    }

    @PostMapping("add")
    @PreAuthorize("@ss.hasPermi('evaluate:order:add')")
    @Log(title = "评议单管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody EvaluateOrder evaluateOrder) {
        return toAjax(evaluateOrderService.insertOrder(evaluateOrder));
    }

    @PostMapping("remove")
    @PreAuthorize("@ss.hasPermi('evaluate:order:remove')")
    @Log(title = "评议单管理", businessType = BusinessType.DELETE)
    public AjaxResult remove(@RequestBody Long[] ids) {
        return toAjax(evaluateOrderService.deleteByIds(ids));
    }

    @GetMapping("getByOrderId")
    public AjaxResult getById(@RequestParam Long orderId) {
        return success(evaluateOrderService.selectById(orderId));
    }

    @PostMapping("update")
    @PreAuthorize("@ss.hasPermi('evaluate:order:update')")
    @Log(title = "评议单管理", businessType = BusinessType.UPDATE)
    public AjaxResult update(@RequestBody EvaluateOrder evaluateOrder) {
        if (evaluateOrderService.updateOrder(evaluateOrder) > 0) {
            return success();
        } else {
            return error("修改评议单'" + evaluateOrder.getOrderId() + "'失败，请联系管理员");
        }
    }

    @PostMapping("publish")
    @PreAuthorize("@ss.hasPermi('evaluate:order:update')")
    @Log(title = "评议单管理", businessType = BusinessType.PUBLISH)
    public AjaxResult publish(@RequestParam Long orderId) {
        if(evaluateOrderService.publish(orderId)){
            return success();
        }else{
            return error("发布评议单'" + orderId + "'失败，请联系管理员");
        }
    }
}
