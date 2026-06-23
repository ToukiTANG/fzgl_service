package com.ruoyi.touki.domain.vo;

import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.MyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateReceiptVO extends MyBaseEntity {
    private Long receiptId;
    /**
     * 评议单id
     */
    private Long orderId;
    /**
     * 评议单中间码
     */
    private String intermediateCode;
    /**
     * 回执单code
     */
    private String randomCode;
    List<EvaluateAnswer> answers;
}
