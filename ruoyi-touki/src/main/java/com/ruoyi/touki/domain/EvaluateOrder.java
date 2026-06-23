package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

/**
 * @author Touki
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EvaluateOrder extends MyBaseEntity {
    @TableId(type = IdType.ASSIGN_ID)
    private Long orderId;
    /**
     * 中间码
     */
    private String intermediateCode;
    /**
     * 评议事项名称
     */
    private String evaluateName;
    /**
     * 被评议人姓名
     */
    private String evaluatedPersonName;
    /**
     * 被评议人部门
     */
    private String evaluatedPersonDepartment;
    /**
     * 评议截止日期
     */
    private LocalDate deadline;
    /**
     * 评议单状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
