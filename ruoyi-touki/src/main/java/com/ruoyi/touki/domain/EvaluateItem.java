package com.ruoyi.touki.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateItem extends BaseEntity {
    private Long itemId;
    private Long orderId;
    private Integer itemType;
}
