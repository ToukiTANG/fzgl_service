package com.ruoyi.touki.service.impl;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.mapper.EvaluateOrderMapper;
import com.ruoyi.touki.service.EvaluateOrderService;
import com.ruoyi.touki.utils.IdGenerateUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Touki
 */
@Service
public class EvaluateOrderServiceImpl implements EvaluateOrderService {
    private final EvaluateOrderMapper evaluateOrderMapper;

    public EvaluateOrderServiceImpl(EvaluateOrderMapper evaluateOrderMapper) {
        this.evaluateOrderMapper = evaluateOrderMapper;
    }

    @Override
    public List<EvaluateOrder> selectList(EvaluateOrder evaluateOrder) {
        return evaluateOrderMapper.selectList(evaluateOrder);
    }

    @Override
    public int insertOrder(EvaluateOrder evaluateOrder) {
        evaluateOrder.setOrderId(IdGenerateUtil.nextId());
        evaluateOrder.setStatus(0);
        evaluateOrder.setDeleted(false);
        evaluateOrder.setCreateBy(SecurityUtils.getUsername());
        evaluateOrder.setCreateTime(new Date());
        evaluateOrder.setUpdateBy(SecurityUtils.getUsername());
        evaluateOrder.setUpdateTime(new Date());
        return evaluateOrderMapper.insertOrder(evaluateOrder);
    }

    @Override
    public int deleteByIds(Long[] ids) {
        return evaluateOrderMapper.deleteByIds(ids);
    }

    @Override
    public EvaluateOrder selectById(Long orderId) {
        return evaluateOrderMapper.selectById(orderId);
    }

    @Override
    public int updateOrder(EvaluateOrder evaluateOrder) {
        evaluateOrder.setUpdateTime(new Date());
        evaluateOrder.setUpdateBy(SecurityUtils.getUsername());
        return evaluateOrderMapper.updateOrder(evaluateOrder);
    }


}
