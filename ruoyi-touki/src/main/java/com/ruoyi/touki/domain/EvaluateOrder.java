package com.ruoyi.touki.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Touki
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class EvaluateOrder {
    private Long orderId;
    private String evaluatedPersonName;
    private String evaluatedPersonDepartment;
    private LocalDate deadline;
    private Integer status;
    private String remark;
    private Boolean deleted;
    private Date createTime;
    private String createBy;
    private Date updateTime;
    private String updateBy;
}
