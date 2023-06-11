package com.jishuqin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jishuqin.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;

import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
public interface RecordService {
    IPage pageC(IPage<Record> page, Wrapper wrapper);

    /**
     * 保存记录
     * @param record
     * @return
     */
    boolean save(Record record);

    /**
     * 删除记录
     * @param list
     * @return
     */
    boolean deleteByList(List<Record> list);
}
