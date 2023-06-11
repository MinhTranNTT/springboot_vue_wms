package com.jishuqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.entity.Goods;
import com.jishuqin.entity.Goodstype;
import com.jishuqin.mapper.GoodsMapper;
import com.jishuqin.service.GoodsService;
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
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String tableName = "Goods";

    public static String toKey(String id){
        return tableName + id;
    }

    @Override
    public boolean save(Goods goods) {
        if( goodsMapper.insert(goods) > 0 ){
            redisUtil.setValue( toKey(goods.getId().toString()),goods);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(Goods goods) {
        if(goodsMapper.updateById(goods) > 0){
            redisUtil.setValue(toKey(goods.getId().toString()),goods);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeById(Integer id) {
        if(goodsMapper.deleteById(id) > 0){
            redisUtil.delete(toKey(id.toString()));
            return true;
        }
        return false;
    }

    @Override
    public IPage page(Page<Goods> page, LambdaQueryWrapper<Goods> lambdaQueryWrapper) {
        return goodsMapper.selectPage(page,lambdaQueryWrapper);
    }

    @Override
    public Goods getOne(QueryWrapper<Goods> wrapper) {
       return goodsMapper.selectOne(wrapper);
    }

    @Override
    public Goods getById(Integer id) {
        String key = toKey(id.toString());
        if (redisUtil.hasKey(key)){
            return (Goods) redisUtil.getValue(key);
        }
        return goodsMapper.selectById(id);
    }

    @Override
    public List<Goods> getList(Wrapper<Goods> wrapper){
        return goodsMapper.selectList(wrapper);
    }
}
