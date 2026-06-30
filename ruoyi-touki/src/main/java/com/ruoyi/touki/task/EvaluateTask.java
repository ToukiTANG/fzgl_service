package com.ruoyi.touki.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ruoyi.touki.constant.EvaluateOrderStatus;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.service.EvaluateOrderService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component("evaluateTask")
public class EvaluateTask {
    private final EvaluateOrderService orderService;

    public EvaluateTask(EvaluateOrderService orderService) {
        this.orderService = orderService;
    }

    public void updateStatus() {
        LambdaUpdateWrapper<EvaluateOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(EvaluateOrder::getStatus, EvaluateOrderStatus.STATUS_PUBLISHED);
        updateWrapper.lt(EvaluateOrder::getDeadline, new Date());
        updateWrapper.set(EvaluateOrder::getStatus, EvaluateOrderStatus.STATUS_FINISHED);
        orderService.update(updateWrapper);
    }
}
