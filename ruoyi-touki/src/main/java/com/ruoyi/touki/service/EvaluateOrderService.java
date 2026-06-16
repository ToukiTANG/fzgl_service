package com.ruoyi.touki.service;

import com.ruoyi.touki.domain.EvaluateOrder;

import java.util.List;

/**
 * @author Touki
 */
public interface EvaluateOrderService {
    List<EvaluateOrder> selectList(EvaluateOrder evaluateOrder);

    int insertOrder(EvaluateOrder evaluateOrder);

    int deleteByIds(Long[] ids);

    EvaluateOrder selectById(Long orderId);

    int updateOrder(EvaluateOrder evaluateOrder);
}
