package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateOrderCode extends MyBaseEntity{
    @TableId
    private Long relationId;
    private Long orderId;
    private String code;
    private Integer count;
}
