package com.ruoyi.touki.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = false)
public class EvaluatePaperExport {
    /*
    题目内容
     */
    @Excel(name = "评议项点标题")
    private String title;
    /*
    题目类型
     */
    @Excel(name = "评议项点类型",readConverterExp = "0=单选,1=多选,2=填空")
    private Integer itemType;
    /*
    是否必填
     */
    @Excel(name = "是否必填")
    private Boolean required;
    /**
     * A/B/C/D
     */
    @Excel(name = "选择题选项标签")
    private String optionCode;
    /**
     * 选项内容
     */
    @Excel(name = "选择题选项内容")
    private String optionContent;
    /**
     * 题目答案
     */
    @Excel(name = "评议选择或回答")
    private String content;
}
