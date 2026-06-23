package com.ruoyi.touki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateReceiptVO;

public interface EvaluateReceiptService extends IService<EvaluateReceipt> {
    void submitAnswer(EvaluateReceiptVO receiptVO);
}
