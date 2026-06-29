package com.ruoyi.touki.domain.excel;

import com.ruoyi.touki.domain.EvaluateAnswer;
import com.ruoyi.touki.domain.EvaluateOrderCode;
import com.ruoyi.touki.domain.EvaluatePerson;
import com.ruoyi.touki.domain.EvaluateReceipt;
import com.ruoyi.touki.domain.vo.EvaluateItemVO;
import com.ruoyi.touki.domain.vo.EvaluateOrderVO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExportContext {

    /**
     * 评议单
     */
    private EvaluateOrderVO order;

    /**
     * 随机码
     */
    private List<EvaluateOrderCode> codes;

    /**
     * 回执
     */
    private List<EvaluateReceipt> receipts;

    /**
     * 答案
     */
    private List<EvaluateAnswer> answers;

    /**
     * personId -> Person
     */
    private Map<Long, EvaluatePerson> personMap;

    /**
     * itemId -> Item
     */
    private Map<Long, EvaluateItemVO> itemMap;

    /**
     * receiptId -> Receipt
     */
    private Map<Long, EvaluateReceipt> receiptMap;

    /**
     * randomCode -> Code
     */
    private Map<String, EvaluateOrderCode> codeMap;

    /**
     * itemId -> (optionCode -> optionContent)
     */
    private Map<Long, Map<String, String>> optionMap;

    /**
     * personId
     *      ↓
     * itemId
     *      ↓
     * answers
     */
    private Map<Long, Map<Long, List<EvaluateAnswer>>> personItemAnswerMap;

    /**
     * personId
     *      ↓
     * receiptId
     *      ↓
     * answers
     */
    private Map<Long, Map<Long, List<EvaluateAnswer>>> personReceiptAnswerMap;

}
