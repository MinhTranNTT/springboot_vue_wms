package com.jishuqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.entity.Goodstype;
import com.jishuqin.mapper.GoodstypeMapper;
import com.jishuqin.service.GoodstypeService;
import com.jishuqin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class GoodstypeServiceImpl implements GoodstypeService {

    private static final String tableName = "Goodstype";

    @Autowired
    private GoodstypeMapper goodstypeMapper;

    @Autowired
    private RedisUtil redisUtil;

    public static String toKey(String id){
        return tableName + id;
    }

    @Override
    public boolean save(Goodstype goodstype) {
        if (goodstypeMapper.insert(goodstype) > 0){
            redisUtil.setValue(toKey(goodstype.getId().toString()),goodstype);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(Goodstype goodstype) {
        if(goodstypeMapper.updateById(goodstype) > 0){
            redisUtil.setValue(toKey(goodstype.getId().toString()),goodstype);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeById(Integer id) {
        if(goodstypeMapper.deleteById(id) > 0){
            redisUtil.delete(toKey(id.toString()));
            return true;
        }
        return false;
    }

    @Override
    public IPage page(Page<Goodstype> page, LambdaQueryWrapper<Goodstype> lambdaQueryWrapper) {
        return goodstypeMapper.selectPage(page,lambdaQueryWrapper);
    }

    @Override
    public List list() {
        return goodstypeMapper.selectList(null);
    }

    @Override
    public Goodstype getOne(QueryWrapper<Goodstype> wrapper) {
        return goodstypeMapper.selectOne(wrapper);
    }

}
