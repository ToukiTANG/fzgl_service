package com.ruoyi.touki.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class EvaluateExport {
    /**
     * 中间码
     */
    @Excel(name = "中间码")
    private String intermediateCode;
    /**
     * 回执单随机码
     */
    @Excel(name = "随机码")
    private String randomCode;
    /**
     * 评议事项名称
     */
    @Excel(name = "评议事项名称")
    private String evaluateName;
    /**
     * 被评议人姓名
     */
    @Excel(name = "被评议人姓名")
    private String evaluatedPersonName;
    /**
     * 被评议人部门
     */
    @Excel(name = "被评议人部门")
    private String evaluatedPersonDepartment;

    @Excel(name = "评议选择及回答")
    List<EvaluatePaperExport>  evaluatePaperExportList;
}
