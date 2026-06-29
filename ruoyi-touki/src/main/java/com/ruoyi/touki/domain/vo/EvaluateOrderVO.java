package com.ruoyi.touki.domain.vo;

import com.ruoyi.touki.domain.EvaluateOrderCode;
import com.ruoyi.touki.domain.EvaluatePerson;
import com.ruoyi.touki.domain.MyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateOrderVO extends MyBaseEntity {
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
     * 评议类型
     */
    private Integer type;
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
    /**
     * 评议内容
     */
    List<EvaluateItemVO> items;
    List<EvaluatePerson> persons;
    List<EvaluateOrderCode> orderCodes;
}
