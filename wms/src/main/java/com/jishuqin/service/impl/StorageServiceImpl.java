package com.jishuqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.entity.Goods;
import com.jishuqin.entity.Goodstype;
import com.jishuqin.entity.Storage;
import com.jishuqin.mapper.GoodsMapper;
import com.jishuqin.mapper.StorageMapper;
import com.jishuqin.service.StorageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jishuqin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class StorageServiceImpl implements StorageService {

    @Autowired
    private StorageMapper storageMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static final String tableName = "Storage";

    public static String toKey(String id){
        return tableName + id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStorageById(Integer id) throws Exception {
        int storage = storageMapper.deleteById(id);
        if (storage > 0){
            redisUtil.delete(toKey(id.toString()));
        }
        QueryWrapper<Goods> wrapper = new QueryWrapper();
        wrapper.eq("storage",id);
        List<Goods> goodsList = goodsMapper.selectList(wrapper);
        for (Goods goods : goodsList) {
            redisUtil.delete(GoodsServiceImpl.toKey(goods.getId().toString()));
        }
        int goods = goodsMapper.delete(wrapper);
        return storage > 0 && goods > 0 ;
    }

    @Override
    public boolean save(Storage storage) {
        if (storageMapper.insert(storage) > 0){
            redisUtil.setValue(toKey(storage.getId().toString()),storage);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateById(Storage storage) {
        if (storageMapper.updateById(storage) > 0){
            redisUtil.setValue(toKey(storage.getId().toString()),storage);
            return true;
        }
        return false;
    }

    @Override
    public IPage page(Page<Storage> page, LambdaQueryWrapper<Storage> lambdaQueryWrapper) {
        return storageMapper.selectPage(page,lambdaQueryWrapper);
    }

    @Override
    public List list() {
        return storageMapper.selectList(null);
    }

    @Override
    public Storage getOne(QueryWrapper<Storage> wrapper) {
        return storageMapper.selectOne(wrapper);
    }
}
