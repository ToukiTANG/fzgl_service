package com.ruoyi.touki.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluatePerson extends MyBaseEntity {
    private Long personId;
    private Long orderId;
    private String name;
    private String department;
}
