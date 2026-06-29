package com.ruoyi.touki.domain.excel;

import com.ruoyi.touki.constant.EvaluateOrderType;
import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.EvaluateOrderCode;
import com.ruoyi.touki.domain.EvaluatePerson;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateItemVO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class PoiExportWriter {

    public void write(ExportContext context, HttpServletResponse response) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        if (context.getOrder().getType().equals(EvaluateOrderType.SCORING)) {
            writeScoreSheet(workbook, context);
        } else {
            writeQuestionSheets(workbook, context);
        }

        writeResponse(workbook, response, context.getOrder().getEvaluateName());
    }

    private ExportStyle createStyles(Workbook workbook) {

        ExportStyle style = new ExportStyle();

        style.setTitleStyle(createTitleStyle(workbook));
        style.setHeadStyle(createHeadStyle(workbook));
        CellStyle bodyStyle = createBodyStyle(workbook);
        style.setBodyStyle(bodyStyle);
        CreationHelper creationHelper = workbook.getCreationHelper();

        CellStyle dateStyle = workbook.createCellStyle();
        dateStyle.cloneStyleFrom(bodyStyle);

        short format = creationHelper.createDataFormat().getFormat("yyyy-MM-dd HH:mm:ss");

        dateStyle.setDataFormat(format);

        style.setDateStyle(dateStyle);

        return style;
    }

    private CellStyle createTitleStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createHeadStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBold(true);

        style.setFont(font);

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private CellStyle createBodyStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        return style;
    }

    private void writeResponse(Workbook workbook, HttpServletResponse response, String fileName) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        response.setCharacterEncoding("utf-8");

        fileName = URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)).replace("+", "%20");

        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        workbook.write(response.getOutputStream());

        workbook.close();
    }

    /**
     * 评分导出
     */
    private void writeScoreSheet(Workbook workbook, ExportContext context) {

        Sheet sheet = workbook.createSheet("评分统计");

        ExportStyle style = createStyles(workbook);

        int rowIndex = 0;

        // 基础信息
        rowIndex = writeBaseInfo(sheet, rowIndex, context, style);

        // 随机码统计
        rowIndex = writeCodeStatistics(sheet, rowIndex, context, style);

        // 记录评分表开始位置
        int scoreHeadRow = rowIndex;

        // 评分统计
        rowIndex = writeScoreTable(sheet, rowIndex, context, style);

        // 冻结评分表头
        sheet.createFreezePane(1, scoreHeadRow + 1);

        // 自动列宽
        autoSize(sheet, context.getOrder().getItems().size() + 2);
    }

    private void autoSize(Sheet sheet, int columnCount) {

        for (int i = 0; i < columnCount; i++) {

            sheet.autoSizeColumn(i);

            int width = sheet.getColumnWidth(i);

            sheet.setColumnWidth(i, Math.min(width + 1024, 255 * 256));
        }
    }

    private int writeCodeStatistics(Sheet sheet, int rowIndex, ExportContext context, ExportStyle style) {

        Row head = sheet.createRow(rowIndex++);

        createCell(head, 0, "随机码", style.getHeadStyle());
        createCell(head, 1, "可用次数", style.getHeadStyle());
        createCell(head, 2, "收回份数", style.getHeadStyle());
        createCell(head, 3, "剩余次数", style.getHeadStyle());

        List<CodeStatisticVO> statistics = buildCodeStatistics(context);

        for (CodeStatisticVO vo : statistics) {

            Row row = sheet.createRow(rowIndex++);

            createCell(row, 0, vo.getRandomCode(), style.getBodyStyle());

            createCell(row, 1, vo.getCodeCount(), style.getBodyStyle());

            createCell(row, 2, vo.getReceiptCount(), style.getBodyStyle());

            createCell(row, 3, vo.getRemainCount(), style.getBodyStyle());

        }

        return rowIndex + 1;
    }

    private int writeScoreTable(Sheet sheet, int rowIndex, ExportContext context, ExportStyle style) {

        // 写表头
        Row headRow = sheet.createRow(rowIndex++);

        int column = 0;

        createCell(headRow, column++, "被评议人", style.getHeadStyle());

        // 动态题目
        for (EvaluateItemVO item : context.getOrder().getItems()) {

            createCell(headRow, column++, item.getTitle(), style.getHeadStyle());

        }

        createCell(headRow, column, "总平均分", style.getHeadStyle());

        // 查询统计结果
        List<ScoreExportRowVO> rows = buildScoreRows(context);

        // 写数据
        for (ScoreExportRowVO vo : rows) {

            Row row = sheet.createRow(rowIndex++);

            column = 0;

            createCell(row, column++, vo.getPersonName(), style.getBodyStyle());

            for (Double score : vo.getScoreMap().values()) {

                createCell(row, column++, score, style.getBodyStyle());

            }

            createCell(row, column, vo.getTotalAverage(), style.getBodyStyle());
        }

        return rowIndex;
    }

    private List<ScoreExportRowVO> buildScoreRows(ExportContext context) {

        List<ScoreExportRowVO> rows = new ArrayList<>();

        for (EvaluatePerson person : context.getOrder().getPersons()) {

            ScoreExportRowVO row = buildPersonScore(person, context);

            rows.add(row);

        }

        return rows;
    }

    private ScoreExportRowVO buildPersonScore(EvaluatePerson person, ExportContext context) {

        ScoreExportRowVO row = new ScoreExportRowVO();

        row.setPersonName(person.getName());

        Map<Long, List<EvaluateAnswer>> personAnswerMap = context.getPersonItemAnswerMap().getOrDefault(person.getPersonId(), Collections.emptyMap());

        double total = 0;

        int questionCount = 0;

        for (EvaluateItemVO item : context.getOrder().getItems()) {

            List<EvaluateAnswer> answers = personAnswerMap.getOrDefault(item.getItemId(), Collections.emptyList());

            double average = calcAverageScore(answers);

            row.getScoreMap().put(item.getTitle(), average);

            total += average;

            questionCount++;

        }

        if (questionCount == 0) {

            row.setTotalAverage(0d);

        } else {

            row.setTotalAverage(total / questionCount);

        }

        return row;
    }

    private double calcAverageScore(List<EvaluateAnswer> answers) {

        if (answers.isEmpty()) {
            return 0;
        }

        return answers.stream().map(EvaluateAnswer::getContent).filter(StringUtils::hasText).mapToDouble(Double::parseDouble).average().orElse(0);
    }

    private List<CodeStatisticVO> buildCodeStatistics(ExportContext context) {

        List<CodeStatisticVO> list = new ArrayList<>();

        Map<String, Long> receiptCountMap = context.getReceipts().stream()
                .collect(Collectors.groupingBy(EvaluateReceipt::getRandomCode, Collectors.counting()));

        for (EvaluateOrderCode code : context.getCodes()) {

            CodeStatisticVO vo = new CodeStatisticVO();

            vo.setRandomCode(code.getCode());

            vo.setCodeCount(code.getCount());

            long receiptCount = receiptCountMap.getOrDefault(code.getCode(), 0L);

            vo.setReceiptCount((int) receiptCount);

            vo.setRemainCount(code.getCount() - (int) receiptCount);

            list.add(vo);
        }

        return list;
    }

    private int writeBaseInfo(Sheet sheet, int rowIndex, ExportContext context, ExportStyle style) {

        rowIndex = createKeyValueRow(sheet, rowIndex, "评议名称", context.getOrder().getEvaluateName(), style);

        rowIndex = createKeyValueRow(sheet, rowIndex, "评议类型", context.getOrder().getType() == 0 ? "评分" : "问卷", style);

        rowIndex = createKeyValueRow(sheet, rowIndex, "截止日期", context.getOrder().getDeadline(), style);

        rowIndex = createKeyValueRow(sheet, rowIndex, "备注", context.getOrder().getRemark(), style);

        return rowIndex + 1;
    }

    private void createCell(Row row, int column, Object value, CellStyle style) {

        Cell cell = row.createCell(column);

        if (value == null) {

            cell.setCellValue("");

        } else if (value instanceof Double) {
            cell.setCellValue(BigDecimal.valueOf((Double) value).setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else if (value instanceof Number) {
            cell.setCellValue(((Number) value).doubleValue());
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else {

            cell.setCellValue(value.toString());

        }

        cell.setCellStyle(style);

    }

    private int createKeyValueRow(Sheet sheet, int rowIndex, String key, Object value, ExportStyle style) {

        Row row = sheet.createRow(rowIndex);

        createCell(row, 0, key, style.getHeadStyle());

        createCell(row, 1, value, style.getBodyStyle());

        return rowIndex + 1;
    }

    /**
     * 问卷导出
     */
    private void writeQuestionSheets(Workbook workbook, ExportContext context) {

        ExportStyle style = createStyles(workbook);

        for (EvaluatePerson person : context.getOrder().getPersons()) {

            Sheet sheet = workbook.createSheet(person.getName());

            int rowIndex = 0;

            // 基础信息
            rowIndex = writeBaseInfo(sheet, rowIndex, context, style);

            // 随机码统计
            rowIndex = writeCodeStatistics(sheet, rowIndex, context, style);

            // 问卷内容
            int tableStartRow = rowIndex;

            rowIndex = writeQuestionTable(sheet, rowIndex, person, context, style);

            // 冻结表头
            sheet.createFreezePane(2, tableStartRow + 1);

            // 自动列宽
            autoSize(sheet, context.getOrder().getItems().size() + 2);
        }
    }

    private int writeQuestionTable(Sheet sheet, int rowIndex, EvaluatePerson person, ExportContext context, ExportStyle style) {

        Row head = sheet.createRow(rowIndex++);

        int col = 0;

        createCell(head, col++, "随机码", style.getHeadStyle());
        createCell(head, col++, "提交时间", style.getHeadStyle());

        for (EvaluateItemVO item : context.getOrder().getItems()) {
            createCell(head, col++, item.getTitle(), style.getHeadStyle());
        }

        for (EvaluateReceipt receipt : context.getReceipts()) {
            List<EvaluateAnswer> answerList = context.getAnswers().stream()
                    .filter(a -> receipt.getReceiptId().equals(a.getReceiptId()) && person.getPersonId().equals(a.getPersonId())).collect(Collectors.toList());

            // 当前回执没有评议这个人
            if (answerList.isEmpty()) {
                continue;
            }
            Row row = sheet.createRow(rowIndex++);
            col = 0;

            createCell(row, col++, receipt.getRandomCode(), style.getBodyStyle());
            createCell(row, col++, receipt.getCreateTime(), style.getDateStyle());

            for (EvaluateItemVO item : context.getOrder().getItems()) {

                EvaluateAnswer answer = context.getAnswers().stream()
                        .filter(a -> receipt.getReceiptId().equals(a.getReceiptId()) && person.getPersonId().equals(a.getPersonId()) &&
                                item.getItemId().equals(a.getItemId())).findFirst().orElse(null);

                createCell(row, col++, answer == null ? "" : answer.getContent(), style.getBodyStyle());
            }
        }

        return rowIndex;
    }

}
