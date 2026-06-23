package com.ruoyi.touki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.touki.constant.EvaluateStatus;
import com.ruoyi.touki.domain.EvaluateItem;
import com.ruoyi.touki.domain.EvaluateItemOption;
import com.ruoyi.touki.domain.EvaluateOrder;
import com.ruoyi.touki.domain.EvaluateOrderCode;
import com.ruoyi.touki.domain.vo.EvaluateItemVO;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
import com.ruoyi.touki.mapper.EvaluateOrderMapper;
import com.ruoyi.touki.service.EvaluateItemOptionService;
import com.ruoyi.touki.service.EvaluateItemService;
import com.ruoyi.touki.service.EvaluateOrderCodeService;
import com.ruoyi.touki.service.EvaluateOrderService;
import com.ruoyi.touki.utils.BeanUtil;
import com.ruoyi.touki.utils.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Service
public class EvaluateOrderServiceImpl extends ServiceImpl<EvaluateOrderMapper, EvaluateOrder> implements EvaluateOrderService {

    private final EvaluateItemService evaluateItemService;
    private final EvaluateItemOptionService evaluateItemOptionService;
    private final EvaluateOrderCodeService evaluateOrderCodeService;

    public EvaluateOrderServiceImpl(EvaluateItemService evaluateItemService, EvaluateItemOptionService evaluateItemOptionService,
                                    EvaluateOrderCodeService evaluateOrderCodeService) {
        this.evaluateItemService = evaluateItemService;
        this.evaluateItemOptionService = evaluateItemOptionService;
        this.evaluateOrderCodeService = evaluateOrderCodeService;
    }

    @Override
    public List<EvaluateOrder> selectOrderList(EvaluateOrder evaluateOrder) {
        LambdaQueryWrapper<EvaluateOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(evaluateOrder.getOrderId()), EvaluateOrder::getOrderId, evaluateOrder.getOrderId());
        queryWrapper.eq(!ObjectUtils.isEmpty(evaluateOrder.getIntermediateCode()), EvaluateOrder::getIntermediateCode, evaluateOrder.getIntermediateCode());
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
    @Transactional(rollbackFor = Exception.class)
    public int insertOrder(EvaluateOrderVO evaluateOrderVO) {

        EvaluateOrder evaluateOrder = new EvaluateOrder();
        BeanUtils.copyProperties(evaluateOrderVO, evaluateOrder);
        String randomCode = RandomUtil.randomUpperAndNumber(6);
        evaluateOrder.setIntermediateCode(randomCode);
        if (!save(evaluateOrder)) {
            return 0;
        }

        List<EvaluateItemVO> itemVOS = evaluateOrderVO.getItems();
        List<EvaluateItem> itemList = itemVOS.stream().map(itemVO -> {
            EvaluateItem evaluateItem = new EvaluateItem();
            BeanUtils.copyProperties(itemVO, evaluateItem);
            evaluateItem.setOrderId(evaluateOrder.getOrderId());
            return evaluateItem;
        }).collect(Collectors.toList());
        if (!evaluateItemService.saveBatch(itemList)) {
            return 0;
        }
        List<EvaluateItemOption> evaluateItemOptions = new ArrayList<>();
        for (int i = 0; i < itemVOS.size(); i++) {
            EvaluateItemVO evaluateItemVO = itemVOS.get(i);
            List<EvaluateItemOption> itemOptions = evaluateItemVO.getOptions();
            int finalI = i;
            itemOptions.forEach(itemOption -> {
                itemOption.setItemId(itemList.get(finalI).getItemId());
                itemOption.setOrderId(evaluateOrder.getOrderId());
            });
            evaluateItemOptions.addAll(itemOptions);
        }
        if (!evaluateItemOptionService.saveBatch(evaluateItemOptions)) {
            return 0;
        }
        return 1;
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
    public EvaluateOrderVO selectById(Long orderId) {
        EvaluateOrder order = getById(orderId);
        EvaluateOrderVO orderVO = new EvaluateOrderVO();
        BeanUtils.copyProperties(order, orderVO);

        LambdaQueryWrapper<EvaluateItem> itemLambdaQueryWrapper = new LambdaQueryWrapper<>();
        itemLambdaQueryWrapper.eq(EvaluateItem::getOrderId, orderId);
        itemLambdaQueryWrapper.eq(EvaluateItem::getDeleted, Boolean.FALSE);
        List<EvaluateItem> evaluateItems = evaluateItemService.list(itemLambdaQueryWrapper);
        List<EvaluateItemVO> itemVOS = BeanUtil.copyList(evaluateItems, EvaluateItemVO.class);

        LambdaQueryWrapper<EvaluateItemOption> optionQueryWrapper = new LambdaQueryWrapper<>();
        List<Long> itemIds = evaluateItems.stream().map(EvaluateItem::getItemId).collect(Collectors.toList());
        optionQueryWrapper.in(EvaluateItemOption::getItemId, itemIds);
        List<EvaluateItemOption> options = evaluateItemOptionService.list(optionQueryWrapper);

        if (!CollectionUtils.isEmpty(options)) {
            itemVOS.forEach(itemVO -> {
                List<EvaluateItemOption> collect = options.stream().filter(option -> option.getItemId().equals(itemVO.getItemId()))
                        .collect(Collectors.toList());
                itemVO.setOptions(collect);
            });
        }
        orderVO.setItems(itemVOS);
        return orderVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateOrder(EvaluateOrderVO evaluateOrderVO) {

        EvaluateOrder order = BeanUtil.copy(evaluateOrderVO, EvaluateOrder.class);
        saveOrUpdate(order);

        EvaluateOrderVO oldOrderVO = selectById(evaluateOrderVO.getOrderId());
        List<EvaluateItemVO> oldItemVOS = oldOrderVO.getItems();
        List<Long> oldItemIds = oldItemVOS.stream().map(EvaluateItemVO::getItemId).collect(Collectors.toList());

        // 处理item
        List<EvaluateItemVO> newItemVOS = evaluateOrderVO.getItems();
        List<Long> newItemIds = newItemVOS.stream().map(EvaluateItemVO::getItemId).filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> removeItemIds = oldItemIds.stream().filter(item -> !newItemIds.contains(item)).collect(Collectors.toList());
        evaluateItemService.removeBatchByIds(removeItemIds);

        List<EvaluateItem> saveAndUpdateItems = BeanUtil.copyList(newItemVOS, EvaluateItem.class);
        evaluateItemService.saveOrUpdateBatch(saveAndUpdateItems);
        for (int i = 0; i < saveAndUpdateItems.size(); i++) {
            EvaluateItemVO newItem = newItemVOS.get(i);
            int finalI = i;
            newItem.getOptions().forEach(newOption -> {
                newOption.setItemId(saveAndUpdateItems.get(finalI).getItemId());
                newOption.setOrderId(evaluateOrderVO.getOrderId());
            });
        }

        //处理option

        List<EvaluateItemOption> options = newItemVOS.stream().flatMap(itemVO -> itemVO.getOptions().stream()).collect(Collectors.toList());
        evaluateItemOptionService.saveOrUpdateBatch(options);

        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publish(Long orderId, Integer codeNum, Integer codeCount) {

        List<EvaluateOrderCode> orderCodes = new ArrayList<>();
        for (int i = 0; i < codeNum; i++) {
            String code = RandomUtil.randomUpperAndNumber(6);
            EvaluateOrderCode orderCode = new EvaluateOrderCode();
            orderCode.setCode(code);
            orderCode.setOrderId(orderId);
            orderCode.setCount(codeCount);
            orderCodes.add(orderCode);
        }
        evaluateOrderCodeService.saveBatch(orderCodes);

        LambdaUpdateWrapper<EvaluateOrder> updateWrapper = new LambdaUpdateWrapper<EvaluateOrder>().eq(EvaluateOrder::getOrderId, orderId);
        updateWrapper.set(EvaluateOrder::getStatus, EvaluateStatus.STATUS_PUBLISHED);
        return update(updateWrapper);
    }

    @Override
    public List<EvaluateOrderCode> codeInfo(Long orderId) {
        LambdaQueryWrapper<EvaluateOrderCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EvaluateOrderCode::getOrderId, orderId);
        return evaluateOrderCodeService.list(queryWrapper);
    }

    @Override
    public boolean verificationCode(String intermediateCode, String randomCode) {
        LambdaQueryWrapper<EvaluateOrder> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(EvaluateOrder::getIntermediateCode, intermediateCode);
        EvaluateOrder order = getOne(orderWrapper);
        if (order == null) {
            return false;
        }

        LambdaQueryWrapper<EvaluateOrderCode> orderCodeWrapper = new LambdaQueryWrapper<>();
        orderCodeWrapper.eq(EvaluateOrderCode::getCode, randomCode);
        orderCodeWrapper.eq(EvaluateOrderCode::getOrderId, order.getOrderId());
        long count = evaluateOrderCodeService.count(orderCodeWrapper);
        return count > 0;
    }

    @Override
    public EvaluateOrderVO selectOneByIntermediateCode(String intermediateCode) {
        LambdaQueryWrapper<EvaluateOrder> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(EvaluateOrder::getIntermediateCode, intermediateCode);
        EvaluateOrder order = getOne(orderWrapper);

        return selectById(order.getOrderId());
    }
}
