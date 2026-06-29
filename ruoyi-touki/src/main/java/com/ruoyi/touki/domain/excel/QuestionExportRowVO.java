package com.ruoyi.touki.domain.excel;

import lombok.Data;

import java.util.Date;
import java.util.LinkedHashMap;

@Data
public class QuestionExportRowVO {

    /**
     * 随机码
     */
    private String randomCode;

    /**
     * 提交时间
     */
    private Date submitTime;

    /**
     * 每题答案
     * <p>
     * key=itemId
     */
    private LinkedHashMap<Long, String> answerMap = new LinkedHashMap<>();

}
