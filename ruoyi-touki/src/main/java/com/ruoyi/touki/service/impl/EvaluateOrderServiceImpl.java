package com.ruoyi.touki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.mapper.EvaluateOrderMapper;
import com.ruoyi.touki.service.EvaluateOrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author Touki
 */
@Service
public class EvaluateOrderServiceImpl extends ServiceImpl<EvaluateOrderMapper, EvaluateOrder> implements EvaluateOrderService {

    @Override
    public List<EvaluateOrder> selectOrderList(EvaluateOrder evaluateOrder) {
        LambdaQueryWrapper<EvaluateOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(evaluateOrder.getOrderId()), EvaluateOrder::getOrderId, evaluateOrder.getOrderId());
        queryWrapper.like(!ObjectUtils.isEmpty(evaluateOrder.getEvaluateName()), EvaluateOrder::getEvaluateName, evaluateOrder.getEvaluateName());
        queryWrapper.like(!ObjectUtils.isEmpty(evaluateOrder.getEvaluatedPersonName()), EvaluateOrder::getEvaluatedPersonName,
                evaluateOrder.getEvaluatedPersonName());
        queryWrapper.like(!ObjectUtils.isEmpty(evaluateOrder.getEvaluatedPersonDepartment()), EvaluateOrder::getEvaluatedPersonDepartment,
                evaluateOrder.getEvaluatedPersonDepartment());
        queryWrapper.eq(!ObjectUtils.isEmpty(evaluateOrder.getDeadline()), EvaluateOrder::getDeadline, evaluateOrder.getDeadline());
        queryWrapper.eq(!ObjectUtils.isEmpty(evaluateOrder.getStatus()), EvaluateOrder::getStatus, evaluateOrder.getStatus());
        queryWrapper.like(!ObjectUtils.isEmpty(evaluateOrder.getRemark()), EvaluateOrder::getRemark, evaluateOrder.getRemark());
        queryWrapper.eq(!ObjectUtils.isEmpty(evaluateOrder.getDeleted()), EvaluateOrder::getDeleted, evaluateOrder.getDeleted());
        return list(queryWrapper);
    }

    @Override
    public int insertOrder(EvaluateOrder evaluateOrder) {
        if (save(evaluateOrder)) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByIds(Long[] ids) {
        if (removeBatchByIds(Arrays.asList(ids))) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public EvaluateOrder selectById(Long orderId) {
        return getById(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrder(EvaluateOrder evaluateOrder) {
        if (saveOrUpdate(evaluateOrder)) {
            return 1;
        } else {
            return 0;
        }
    }
}
