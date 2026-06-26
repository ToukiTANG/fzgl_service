package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateAnswer extends MyBaseEntity {
    @TableId
    private Long answerId;
    /**
     * 回执单id
     */
    private Long receiptId;
    /**
     * 题目id
     */
    private Long itemId;
    /**
     * 被评议人id
     */
    private Long personId;
    /**
     * 题目答案
     */
    private String content;
}
