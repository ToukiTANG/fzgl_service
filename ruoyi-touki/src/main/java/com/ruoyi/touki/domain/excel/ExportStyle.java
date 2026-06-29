package com.ruoyi.touki.domain.excel;

import lombok.Data;
import org.apache.poi.ss.usermodel.CellStyle;

@Data
public class ExportStyle {

    /**
     * 标题
     */
    private CellStyle titleStyle;

    /**
     * 表头
     */
    private CellStyle headStyle;

    /**
     * 正文
     */
    private CellStyle bodyStyle;

    /**
     * 日期样式
     */
    private CellStyle dateStyle;

}
