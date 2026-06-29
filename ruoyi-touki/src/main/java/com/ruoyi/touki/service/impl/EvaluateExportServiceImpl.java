package com.ruoyi.touki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ruoyi.touki.domain.*;
import com.ruoyi.touki.domain.excel.ExportContext;
import com.ruoyi.touki.domain.excel.PoiExportWriter;
import com.ruoyi.touki.domain.vo.EvaluateItemVO;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
import com.ruoyi.touki.service.EvaluateAnswerService;
import com.ruoyi.touki.service.EvaluateOrderCodeService;
import com.ruoyi.touki.service.EvaluateOrderService;
import com.ruoyi.touki.service.EvaluateReceiptService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class EvaluateExportServiceImpl {
    private final EvaluateReceiptService receiptService;
    private final EvaluateOrderService orderService;
    private final EvaluateAnswerService answerService;
    private final EvaluateOrderCodeService orderCodeService;
    private final PoiExportWriter poiExportWriter;

    public EvaluateExportServiceImpl(EvaluateReceiptService receiptService, EvaluateOrderService orderService, EvaluateAnswerService answerService,
                                     EvaluateOrderCodeService orderCodeService, PoiExportWriter poiExportWriter) {
        this.receiptService = receiptService;
        this.orderService = orderService;
        this.answerService = answerService;
        this.orderCodeService = orderCodeService;
        this.poiExportWriter = poiExportWriter;
    }

    public void exportExcel(String intermediateCode, HttpServletResponse response) throws IOException {
        // 获取数据
        LambdaQueryWrapper<EvaluateReceipt> receiptWrapper = new LambdaQueryWrapper<>();
        receiptWrapper.eq(EvaluateReceipt::getIntermediateCode, intermediateCode);
        List<EvaluateReceipt> receipts = receiptService.list(receiptWrapper);
        Long orderId = receipts.get(0).getOrderId();
        LambdaQueryWrapper<EvaluateAnswer> answerWrapper = new LambdaQueryWrapper<>();
        List<Long> receiptIds = receipts.stream().map(EvaluateReceipt::getReceiptId).collect(Collectors.toList());
        answerWrapper.in(EvaluateAnswer::getReceiptId, receiptIds);
        List<EvaluateAnswer> answers = answerService.list(answerWrapper);
        EvaluateOrderVO orderVO = orderService.selectById(orderId);
        LambdaQueryWrapper<EvaluateOrderCode> orderCodeWrapper = new LambdaQueryWrapper<>();
        orderCodeWrapper.eq(EvaluateOrderCode::getOrderId, orderId);
        List<EvaluateOrderCode> codes = orderCodeService.list(orderCodeWrapper);

        ExportContext context = buildContext(orderVO, codes, receipts, answers);

        // 导出
        poiExportWriter.write(context, response);
    }

    private ExportContext buildContext(EvaluateOrderVO order, List<EvaluateOrderCode> codes, List<EvaluateReceipt> receipts, List<EvaluateAnswer> answers) {

        ExportContext context = new ExportContext();

        context.setOrder(order);

        context.setCodes(codes);

        context.setReceipts(receipts);

        context.setAnswers(answers);

        context.setPersonMap(buildPersonMap(order));

        context.setItemMap(buildItemMap(order));

        context.setReceiptMap(buildReceiptMap(receipts));

        context.setCodeMap(buildCodeMap(codes));

        context.setOptionMap(buildOptionMap(order));

        context.setPersonItemAnswerMap(buildPersonItemAnswerMap(answers));

        context.setPersonReceiptAnswerMap(buildPersonReceiptAnswerMap(answers));

        return context;
    }

    private Map<Long, EvaluatePerson> buildPersonMap(EvaluateOrderVO order) {

        return order.getPersons().stream().collect(Collectors.toMap(EvaluatePerson::getPersonId, Function.identity()));
    }

    private Map<Long, EvaluateItemVO> buildItemMap(EvaluateOrderVO order) {

        return order.getItems().stream().collect(Collectors.toMap(EvaluateItemVO::getItemId, Function.identity()));
    }

    private Map<Long, EvaluateReceipt> buildReceiptMap(List<EvaluateReceipt> receipts) {

        return receipts.stream().collect(Collectors.toMap(EvaluateReceipt::getReceiptId, Function.identity()));
    }

    private Map<String, EvaluateOrderCode> buildCodeMap(List<EvaluateOrderCode> codes) {

        return codes.stream().collect(Collectors.toMap(EvaluateOrderCode::getCode, Function.identity()));
    }

    private Map<Long, Map<String, String>> buildOptionMap(EvaluateOrderVO order) {

        Map<Long, Map<String, String>> optionMap = new HashMap<>();

        for (EvaluateItemVO item : order.getItems()) {

            if (item.getOptions() == null || item.getOptions().isEmpty()) {
                continue;
            }

            Map<String, String> map = item.getOptions().stream()
                    .collect(Collectors.toMap(EvaluateItemOption::getOptionCode, EvaluateItemOption::getOptionContent));

            optionMap.put(item.getItemId(), map);
        }

        return optionMap;
    }

    private Map<Long, Map<Long, List<EvaluateAnswer>>> buildPersonItemAnswerMap(List<EvaluateAnswer> answers) {

        return answers.stream().collect(Collectors.groupingBy(EvaluateAnswer::getPersonId, Collectors.groupingBy(EvaluateAnswer::getItemId)));
    }

    private Map<Long, Map<Long, List<EvaluateAnswer>>> buildPersonReceiptAnswerMap(List<EvaluateAnswer> answers) {

        return answers.stream().collect(Collectors.groupingBy(EvaluateAnswer::getPersonId, Collectors.groupingBy(EvaluateAnswer::getReceiptId)));
    }
}