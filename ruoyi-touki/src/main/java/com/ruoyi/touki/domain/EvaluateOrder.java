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
    private String evaluateName;
    private String evaluatedPersonName;
    private String evaluatedPersonDepartment;
    private LocalDate deadline;
    private Integer status;
    private String remark;
}
