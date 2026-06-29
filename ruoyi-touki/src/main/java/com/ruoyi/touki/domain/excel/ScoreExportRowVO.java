package com.ruoyi.touki.domain.excel;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class ScoreExportRowVO {

    /**
     * 被评议人
     */
    private String personName;

    /**
     * 各题平均分
     *
     * key：题目标题
     */
    private Map<String, Double> scoreMap = new LinkedHashMap<>();

    /**
     * 总平均分
     */
    private Double totalAverage;

}
