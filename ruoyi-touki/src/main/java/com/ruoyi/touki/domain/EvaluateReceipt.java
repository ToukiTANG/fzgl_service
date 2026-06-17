package com.ruoyi.touki.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateReceipt extends MyBaseEntity{
    private Long receiptId;
    private Long orderId;
    private String receiptCode;
}
