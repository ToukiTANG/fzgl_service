package com.ruoyi.touki.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ruoyi.common.core.domain.BaseEntity;
import com.sun.jna.WString;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class EvaluateItem extends BaseEntity {
    @TableId
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
}
