package com.ruoyi.touki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.EvaluateItemOption;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.*;
import com.ruoyi.touki.mapper.EvaluateReceiptMapper;
import com.ruoyi.touki.service.EvaluateAnswerService;
import com.ruoyi.touki.service.EvaluateOrderService;
import com.ruoyi.touki.service.EvaluateReceiptService;
import com.ruoyi.touki.utils.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EvaluateReceiptServiceImpl extends ServiceImpl<EvaluateReceiptMapper, EvaluateReceipt> implements EvaluateReceiptService {
    private final EvaluateAnswerService answerService;
    private final EvaluateOrderService orderService;

    public EvaluateReceiptServiceImpl(EvaluateAnswerService answerService, EvaluateOrderService orderService) {
        this.answerService = answerService;
        this.orderService = orderService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void submitAnswer(EvaluateReceiptVO receiptVO) {
        EvaluateReceipt receipt = BeanUtil.copy(receiptVO, EvaluateReceipt.class);
        save(receipt);

        List<EvaluateAnswer> answers = receiptVO.getAnswers();
        answers.forEach(answer -> answer.setReceiptId(receipt.getReceiptId()));
        answerService.saveBatch(answers);
    }

    @Override
    public List<EvaluateReceipt> selectReceiptList(EvaluateReceipt receipt) {
        LambdaQueryWrapper<EvaluateReceipt> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(receipt.getReceiptId()), EvaluateReceipt::getReceiptId, receipt.getReceiptId());
        queryWrapper.eq(!ObjectUtils.isEmpty(receipt.getOrderId()), EvaluateReceipt::getOrderId, receipt.getOrderId());
        queryWrapper.eq(!ObjectUtils.isEmpty(receipt.getIntermediateCode()), EvaluateReceipt::getIntermediateCode, receipt.getIntermediateCode());
        queryWrapper.eq(!ObjectUtils.isEmpty(receipt.getRandomCode()), EvaluateReceipt::getRandomCode, receipt.getRandomCode());
        return list(queryWrapper);
    }

    @Override
    public TestPaper selectReceiptDetailById(Long receiptId) {
        EvaluateReceipt receipt = getById(receiptId);
        EvaluateReceiptVO receiptVO = BeanUtil.copy(receipt, EvaluateReceiptVO.class);

        LambdaQueryWrapper<EvaluateAnswer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.eq(EvaluateAnswer::getReceiptId, receiptId);
        List<EvaluateAnswer> list = answerService.list(answerWrapper);
        receiptVO.setAnswers(list);

        EvaluateOrderVO orderVO = orderService.selectById(receipt.getOrderId());

        TestPaper paper = new TestPaper();
        paper.setOrder(orderVO);
        paper.setReceipt(receiptVO);
        return paper;
    }

    @Override
    public List<EvaluateExport> export(EvaluateReceipt receipt) {
        // 这里一定只会有一个评议
        List<EvaluateReceipt> receipts = selectReceiptList(receipt);
        Long orderId = receipts.get(0).getOrderId();
        EvaluateOrderVO orderVO = orderService.selectById(orderId);

        List<Long> receiptIds = receipts.stream().map(EvaluateReceipt::getReceiptId).collect(Collectors.toList());
        LambdaQueryWrapper<EvaluateAnswer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.in(EvaluateAnswer::getReceiptId, receiptIds);
        List<EvaluateAnswer> answers = answerService.list(answerWrapper);

        List<EvaluateItemVO> itemVOS = orderVO.getItems();
        Map<Long, Map<String, String>> optionMap = orderVO.getItems().stream().collect(Collectors.toMap(EvaluateItemVO::getItemId,
                item -> item.getOptions().stream().collect(Collectors.toMap(EvaluateItemOption::getOptionCode, EvaluateItemOption::getOptionContent))));

        Map<Long, Map<Long, String>> answerMap = answers.stream()
                .collect(Collectors.groupingBy(EvaluateAnswer::getReceiptId, Collectors.toMap(EvaluateAnswer::getItemId, EvaluateAnswer::getContent)));


        return Collections.emptyList();
    }

}
