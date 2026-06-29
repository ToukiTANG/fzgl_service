package com.ruoyi.touki.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.TestPaper;
import com.ruoyi.touki.service.EvaluateReceiptService;
import com.ruoyi.touki.service.impl.EvaluateExportServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("evaluate/receipt")
public class EvaluateReceiptController extends BaseController {
    private final EvaluateReceiptService receiptService;
    private final EvaluateExportServiceImpl exportService;

    public EvaluateReceiptController(EvaluateReceiptService evaluateReceiptService, EvaluateExportServiceImpl exportService) {
        this.receiptService = evaluateReceiptService;
        this.exportService = exportService;
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

    @Log(title = "评议答案管理", businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('evaluate:receipt:export')")
    @PostMapping("/export")
    public void export(HttpServletResponse response, EvaluateReceipt receipt) throws Exception {
        String intermediateCode = receipt.getIntermediateCode();
        exportService.exportExcel(intermediateCode, response);
    }
}
