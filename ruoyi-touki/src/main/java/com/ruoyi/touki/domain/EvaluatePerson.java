package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluatePerson extends MyBaseEntity {
    @TableId
    private Long personId;
    private Long orderId;
    private String name;
    private String department;
}
