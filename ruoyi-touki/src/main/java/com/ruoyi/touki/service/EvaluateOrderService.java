package com.ruoyi.touki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.domain.EvaluateOrderCode;
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

    List<EvaluateOrderCode> codeInfo(Long orderId);

    boolean verificationCode(String intermediateCode, String randomCode);

    EvaluateOrderVO selectOneByIntermediateCode(String intermediateCode);

    boolean publish(Long orderId, List<EvaluateOrderCode> orderCodes);
}
