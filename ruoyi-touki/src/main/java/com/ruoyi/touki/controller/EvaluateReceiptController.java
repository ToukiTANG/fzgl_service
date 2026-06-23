package com.ruoyi.touki.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.TestPaper;
import com.ruoyi.touki.service.EvaluateReceiptService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("evaluate/receipt")
public class EvaluateReceiptController extends BaseController {
    private final EvaluateReceiptService receiptService;

    public EvaluateReceiptController(EvaluateReceiptService evaluateReceiptService) {
        this.receiptService = evaluateReceiptService;
    }

    @GetMapping("list")
    @PreAuthorize("@ss.hasPermi('evaluate:receipt:list')")
    public TableDataInfo list(EvaluateReceipt receipt) {
        receipt.setDeleted(false);
        startPage();
        List<EvaluateReceipt> receipts = receiptService.selectReceiptList(receipt);

        return getDataTable(receipts);
    }

    @GetMapping("detail")
    @PreAuthorize("@ss.hasPermi('evaluate:receipt:detail')")
    public AjaxResult answerDetail(@RequestParam Long receiptId) {
        TestPaper paper = receiptService.selectReceiptDetailById(receiptId);
        return success(paper);
    }
}
