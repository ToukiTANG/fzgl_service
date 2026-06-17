package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateRecord extends BaseEntity {
    @TableId
    private Long recordId;
    /**
     * 回执单id
     */
    private Long receiptId;
    /**
     * 题目id
     */
    private Long itemId;
    /**
     * 题目答案
     */
    private String content;
}
