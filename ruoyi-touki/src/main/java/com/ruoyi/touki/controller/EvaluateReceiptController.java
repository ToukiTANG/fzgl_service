package com.ruoyi.touki.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.EvaluateItemOption;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateItemVO;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
import com.ruoyi.touki.domain.vo.TestPaper;
import com.ruoyi.touki.service.EvaluateAnswerService;
import com.ruoyi.touki.service.EvaluateOrderService;
import com.ruoyi.touki.service.EvaluateReceiptService;
import com.ruoyi.touki.service.impl.EvaluateExportServiceImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("evaluate/receipt")
public class EvaluateReceiptController extends BaseController {
    private final EvaluateReceiptService receiptService;
    private final EvaluateExportServiceImpl exportService;
    private final EvaluateOrderService orderService;
    private final EvaluateAnswerService answerService;

    public EvaluateReceiptController(EvaluateReceiptService evaluateReceiptService, EvaluateExportServiceImpl exportService, EvaluateOrderService orderService,
                                     EvaluateAnswerService answerService) {
        this.receiptService = evaluateReceiptService;
        this.exportService = exportService;
        this.orderService = orderService;
        this.answerService = answerService;
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
        List<EvaluateReceipt> receipts = receiptService.selectReceiptList(receipt);
        Long orderId = receipts.get(0).getOrderId();
        EvaluateOrderVO orderVO = orderService.selectById(orderId);

        List<Long> receiptIds = receipts.stream().map(EvaluateReceipt::getReceiptId).collect(Collectors.toList());
        LambdaQueryWrapper<EvaluateAnswer> answerWrapper = new LambdaQueryWrapper<>();
        answerWrapper.in(EvaluateAnswer::getReceiptId, receiptIds);
        List<EvaluateAnswer> answers = answerService.list(answerWrapper);
        Map<Long, Map<String, String>> optionMap = buildOptionMap(orderVO.getItems());
        exportService.exportExcel(orderVO, receipts, answers, optionMap, response);
    }

    private Map<Long, Map<String, String>> buildOptionMap(List<EvaluateItemVO> items) {

        return items.stream().filter(item -> item.getOptions() != null).collect(Collectors.toMap(EvaluateItemVO::getItemId,
                item -> item.getOptions().stream().collect(Collectors.toMap(EvaluateItemOption::getOptionCode, EvaluateItemOption::getOptionContent))));
    }
}
