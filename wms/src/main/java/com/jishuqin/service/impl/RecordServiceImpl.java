package com.jishuqin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jishuqin.entity.Record;
import com.jishuqin.mapper.RecordMapper;
import com.jishuqin.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import com.baomidou.mybatisplus.core.conditions.Wrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
@Service
public class RecordServiceImpl implements RecordService {

    @Resource
    private RecordMapper recordMapper;

    @Override
    public IPage pageC(IPage<Record> page, Wrapper wrapper) {
        return recordMapper.pageC(page,wrapper);
    }

    @Override
    public boolean save(Record record) {
        return recordMapper.insert(record) > 0;
    }

    @Override
    public boolean deleteByList(List<Record> list) {
        List<Integer> ids = new ArrayList<>();
        for (Record record : list) {
            ids.add(record.getId());
        }
        try {
            return recordMapper.deleteBatchIds(ids) > 0;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
     }
}
