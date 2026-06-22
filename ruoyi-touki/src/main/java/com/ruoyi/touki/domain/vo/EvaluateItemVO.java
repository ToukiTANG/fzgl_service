package com.ruoyi.touki.domain.vo;

import com.ruoyi.touki.domain.EvaluateItemOption;
import com.ruoyi.touki.domain.MyBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateItemVO extends MyBaseEntity {
    private Long itemId;
    /*
    评议单id
     */
    private Long orderId;
    /*
    题目内容
     */
    private String title;
    /*
    题目类型
     */
    private Integer itemType;
    /*
    是否必填
     */
    private Boolean required;
    /**
     * 排序
     */
    private Integer sortNum;

    List<EvaluateItemOption>  options;
}
