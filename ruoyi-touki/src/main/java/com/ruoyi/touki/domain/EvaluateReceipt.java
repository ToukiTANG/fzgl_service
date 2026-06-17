package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateReceipt extends MyBaseEntity {
    @TableId
    private Long receiptId;
    /**
     * 评议单id
     */
    private Long orderId;
    /**
     * 回执单code
     */
    private String receiptCode;
}
