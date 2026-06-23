package com.ruoyi.touki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateReceiptVO;
import com.ruoyi.touki.mapper.EvaluateReceiptMapper;
import com.ruoyi.touki.service.EvaluateAnswerService;
import com.ruoyi.touki.service.EvaluateReceiptService;
import com.ruoyi.touki.utils.BeanUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EvaluateReceiptServiceImpl extends ServiceImpl<EvaluateReceiptMapper, EvaluateReceipt> implements EvaluateReceiptService {
    private final EvaluateAnswerService answerService;

    public EvaluateReceiptServiceImpl(EvaluateAnswerService answerService) {
        this.answerService = answerService;
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
}
