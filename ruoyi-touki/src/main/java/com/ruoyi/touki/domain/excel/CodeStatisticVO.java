package com.ruoyi.touki.domain.excel;

import lombok.Data;

@Data
public class CodeStatisticVO {

    /**
     * 随机码
     */
    private String randomCode;

    /**
     * 可用次数
     */
    private Integer codeCount;

    /**
     * 已收回份数
     */
    private Integer receiptCount;

    /**
     * 剩余次数
     */
    private Integer remainCount;

}
