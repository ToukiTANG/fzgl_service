package com.ruoyi.touki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.touki.domain.EvaluateOrder;

import java.util.List;

/**
 * @author Touki
 */
public interface EvaluateOrderService extends IService<EvaluateOrder> {
    List<EvaluateOrder> selectOrderList(EvaluateOrder evaluateOrder);

    int insertOrder(EvaluateOrder evaluateOrder);

    int deleteByIds(Long[] ids);

    EvaluateOrder selectById(Long orderId);

    int updateOrder(EvaluateOrder evaluateOrder);

    boolean publish(Long orderId);
}
