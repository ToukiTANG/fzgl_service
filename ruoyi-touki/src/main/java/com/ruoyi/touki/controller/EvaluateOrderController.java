package com.ruoyi.touki.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.domain.EvaluateOrderCode;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
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
        startPage();
        List<EvaluateOrder> list = evaluateOrderService.selectOrderList(evaluateOrder);
        return getDataTable(list);
    }

    @PostMapping("add")
    @PreAuthorize("@ss.hasPermi('evaluate:order:add')")
    @Log(title = "评议单管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@RequestBody EvaluateOrderVO evaluateOrderVO) {

        return toAjax(evaluateOrderService.insertOrder(evaluateOrderVO));
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
    public AjaxResult update(@RequestBody EvaluateOrderVO evaluateOrderVO) {
        if (evaluateOrderService.updateOrder(evaluateOrderVO) > 0) {
            return success();
        } else {
            return error("修改评议单'" + evaluateOrderVO.getOrderId() + "'失败，请联系管理员");
        }
    }

    @PostMapping("publish")
    @PreAuthorize("@ss.hasPermi('evaluate:order:update')")
    @Log(title = "评议单管理", businessType = BusinessType.PUBLISH)
    public AjaxResult publish(@RequestParam Long orderId, @RequestParam Integer codeNum, @RequestParam Integer codeCount) {
        if (evaluateOrderService.publish(orderId, codeNum, codeCount)) {
            return success();
        } else {
            return error("发布评议单'" + orderId + "'失败，请联系管理员");
        }
    }

    @GetMapping("codeInfo")
    @PreAuthorize("@ss.hasPermi('evaluate:order:codeInfo')")
    public AjaxResult codeInfo(@RequestParam Long orderId) {
        List<EvaluateOrderCode> orderCode = evaluateOrderService.codeInfo(orderId);
        return success(orderCode);
    }
}
