package com.ruoyi.touki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateReceiptVO;
import com.ruoyi.touki.domain.vo.TestPaper;

import java.util.List;

public interface EvaluateReceiptService extends IService<EvaluateReceipt> {
    void submitAnswer(EvaluateReceiptVO receiptVO);

    List<EvaluateReceipt> selectReceiptList(EvaluateReceipt receipt);

    TestPaper selectReceiptDetailById(Long receiptId);

}
