package com.ruoyi.touki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;

import java.util.List;

/**
 * @author Touki
 */
public interface EvaluateOrderService extends IService<EvaluateOrder> {
    List<EvaluateOrder> selectOrderList(EvaluateOrder evaluateOrder);

    int insertOrder(EvaluateOrderVO evaluateOrderVO);

    int deleteByIds(Long[] ids);

    EvaluateOrderVO selectById(Long orderId);

    int updateOrder(EvaluateOrderVO evaluateOrder);

    boolean publish(Long orderId);
}
