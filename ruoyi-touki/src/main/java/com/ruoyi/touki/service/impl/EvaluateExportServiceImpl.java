package com.ruoyi.touki.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.EvaluateItemOption;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateItemVO;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EvaluateExportServiceImpl {
    public void exportExcel(EvaluateOrderVO order, List<EvaluateReceipt> receipts, List<EvaluateAnswer> answers, Map<Long, Map<String, String>> optionMap,
                            HttpServletResponse response) throws Exception {
        List<EvaluateItemVO> itemVOS = order.getItems();

        // =========================
        // 构建答案索引
        // receiptId -> itemId -> answer
        // =========================
        Map<Long, Map<Long, String>> answerMap = answers.stream().collect(
                Collectors.groupingBy(EvaluateAnswer::getReceiptId, Collectors.toMap(EvaluateAnswer::getItemId, EvaluateAnswer::getContent, (v1, v2) -> v1)));

        // =========================
        // 创建Excel
        // =========================
        Workbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet("评议结果");

        // =========================
        // 表头
        // =========================
        int rowNum = 0;

        sheet.createRow(rowNum++).createCell(0).setCellValue("评议事项：" + order.getEvaluateName());

        sheet.createRow(rowNum++).createCell(0).setCellValue("评议单ID：" + order.getOrderId());

        sheet.createRow(rowNum++).createCell(0).setCellValue("中间码：" + order.getIntermediateCode());

        sheet.createRow(rowNum++).createCell(0).setCellValue("截止日期：" + order.getDeadline());

        sheet.createRow(rowNum++).createCell(0).setCellValue("回执数量：" + receipts.size());
        
        sheet.createRow(rowNum++).createCell(0).setCellValue("导出时间：" + DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", new Date()));

        rowNum++;
        Row header = sheet.createRow(rowNum);

        int colIndex = 0;

        header.createCell(colIndex++).setCellValue("随机码");

        header.createCell(colIndex++).setCellValue("提交时间");

        for (EvaluateItemVO item : itemVOS) {
            header.createCell(colIndex++).setCellValue(buildHeader(item));
        }

        // =========================
        // 数据
        // =========================
        for (int rowIndex = 0; rowIndex < receipts.size(); rowIndex++) {

            EvaluateReceipt receipt = receipts.get(rowIndex);

            Row row = sheet.createRow(rowNum + rowIndex + 1);

            int col = 0;

            // 随机码
            row.createCell(col++).setCellValue(receipt.getRandomCode());

            // 提交时间
            row.createCell(col++).setCellValue(receipt.getCreateTime() == null ? "" : DateUtils.parseDateToStr("yyyy-MM-dd HH:mm:ss", receipt.getCreateTime()));

            Map<Long, String> receiptAnswerMap = answerMap.get(receipt.getReceiptId());

            // 按题目顺序写答案
            for (EvaluateItemVO item : itemVOS) {

                String answer = "";

                if (receiptAnswerMap != null) {
                    answer = receiptAnswerMap.getOrDefault(item.getItemId(), "");
                }

                String displayValue = convertAnswer(item, answer, optionMap);

                row.createCell(col++).setCellValue(displayValue);
            }
        }

        // =========================
        // 列宽
        // =========================
        for (int i = 0; i < itemVOS.size() + 2; i++) {
            sheet.setColumnWidth(i, 8000);
        }

        // =========================
        // 下载
        // =========================
        String fileName = URLEncoder.encode(order.getEvaluateName() + "_评议结果", String.valueOf(StandardCharsets.UTF_8));

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setCharacterEncoding("utf-8");

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        workbook.write(response.getOutputStream());

        workbook.close();
    }

    private String convertAnswer(EvaluateItemVO item, String answer, Map<Long, Map<String, String>> optionMap) {

        if (answer == null || answer.trim().isEmpty()) {
            return "";
        }

        // 填空题
        if (item.getItemType() == 2) {
            return answer;
        }

        Map<String, String> option = optionMap.get(item.getItemId());

        if (option == null) {
            return answer;
        }

        // 单选
        if (item.getItemType() == 0) {

            return answer + "." + option.getOrDefault(answer, "");
        }

        // 多选
        if (item.getItemType() == 1) {

            return Arrays.stream(answer.split(",")).map(code -> code + "." + option.getOrDefault(code, "")).collect(Collectors.joining("\n"));
        }

        return answer;
    }

    private String buildHeader(EvaluateItemVO item) {

        // 填空题不拼接选项
        if (item.getItemType() == 2) {
            return item.getTitle();
        }

        if (item.getOptions() == null || item.getOptions().isEmpty()) {
            return item.getTitle();
        }

        String options = item.getOptions().stream().sorted(Comparator.comparing(EvaluateItemOption::getSortNum))
                .map(option -> option.getOptionCode() + "." + option.getOptionContent()).collect(Collectors.joining(" "));

        return item.getTitle() + "（" + options + "）";
    }
}
