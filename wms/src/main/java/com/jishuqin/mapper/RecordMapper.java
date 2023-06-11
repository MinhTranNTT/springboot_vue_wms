package com.jishuqin.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.jishuqin.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
@Mapper
public interface RecordMapper extends BaseMapper<Record> {
    IPage pageC(IPage<Record> page, @Param(Constants.WRAPPER) Wrapper wrapper);
}
