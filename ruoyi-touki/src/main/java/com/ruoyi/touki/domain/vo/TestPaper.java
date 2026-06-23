package com.ruoyi.touki.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class TestPaper {
    private EvaluateOrderVO order;
    private EvaluateReceiptVO receipt;
}
