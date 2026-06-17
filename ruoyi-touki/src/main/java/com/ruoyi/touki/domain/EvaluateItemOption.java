package com.ruoyi.touki.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class EvaluateItemOption extends MyBaseEntity {
    
    private Long optionId;

    /**
     * 题目id
     */
    private Long itemId;

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
