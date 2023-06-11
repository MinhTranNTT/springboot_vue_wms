package com.jishuqin.mapper;

import com.jishuqin.entity.Goods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
