package com.ruoyi.touki.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateRecord extends BaseEntity {
    private Long recordId;
    private Long receiptId;
    private Long itemId;
    private String content;
}
