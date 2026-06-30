package com.ruoyi.touki.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
import com.ruoyi.touki.domain.vo.EvaluateReceiptVO;
import com.ruoyi.touki.service.EvaluateOrderService;
import com.ruoyi.touki.service.EvaluateReceiptService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("evaluate/execution")
public class EvaluateExecutionController extends BaseController {

    private final EvaluateOrderService orderService;
    private final EvaluateReceiptService receiptService;


    public EvaluateExecutionController(EvaluateOrderService evaluateOrderService, EvaluateReceiptService evaluateReceiptService, EvaluateReceiptService receiptService) {
        this.orderService = evaluateOrderService;
        this.receiptService = receiptService;
    }

    @GetMapping("content")
    public AjaxResult content(@RequestParam String intermediateCode, @RequestParam String randomCode) {
        if (!orderService.verificationCode(intermediateCode, randomCode)) {
            return error("您的中间码与随机码不匹配，请核实。");
        }
        EvaluateOrderVO orderVO = orderService.selectOneByIntermediateCode(intermediateCode);
        if(orderVO == null){
            return error("该次评议已结束，请联系管理员。");
        }
        return AjaxResult.success(orderVO);
    }
    
    @PostMapping("submitAnswer")
    @Log(title = "提交评议答案", businessType = BusinessType.INSERT)
    public AjaxResult submitAnswer(@RequestBody EvaluateReceiptVO receiptVO) {
        receiptService.submitAnswer(receiptVO);
        return success();
    }
}
