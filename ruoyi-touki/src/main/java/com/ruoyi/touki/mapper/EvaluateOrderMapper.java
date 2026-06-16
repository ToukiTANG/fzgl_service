package com.ruoyi.touki.mapper;

import com.ruoyi.touki.domain.EvaluateOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Touki
 */
@Mapper
public interface EvaluateOrderMapper {
    List<EvaluateOrder> selectList(EvaluateOrder evaluateOrder);

    int insertOrder(EvaluateOrder evaluateOrder);

    int deleteByIds(Long[] ids);

    EvaluateOrder selectById(Long orderId);

    int updateOrder(EvaluateOrder evaluateOrder);
}
