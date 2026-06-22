package com.ruoyi.touki.domain.vo;

import com.ruoyi.touki.domain.MyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateItemOptionVO extends MyBaseEntity {
    private Long optionId;

    /**
     * 题目id
     */
    private Long itemId;

    /*
    评议单id
     */
    private Long orderId;

    /**
     * A/B/C/D
     */
    private String optionCode;

    /**
     * 选项内容
     */
    private String optionContent;

    /**
     * 排序
     */
    private Integer sortNum;
}
