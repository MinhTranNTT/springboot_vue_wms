package com.jishuqin.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.entity.Goods;

import java.util.List;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
public interface GoodsService {
    /**
     * 存储物品
     * @param goods
     * @return
     */
    boolean save(Goods goods);

    /**
     * 根据id修改物品
     * @param goods
     * @return
     */
    boolean updateById(Goods goods);

    /**
     * 根据id删除物品
     * @param id
     * @return
     */
    boolean removeById(Integer id);

    /**
     * 分页显示物品
     * @param page
     * @param lambdaQueryWrapper
     * @return
     */
    IPage page(Page<Goods> page, LambdaQueryWrapper<Goods> lambdaQueryWrapper);

    /**
     * 根据条件查询物品
     * @param wrapper
     * @return
     */
    Goods getOne(QueryWrapper<Goods> wrapper);

    /**
     * 根据Id查询物品
     * @param id
     * @return
     */
    Goods getById(Integer id);

    /**
     * 根据条件查询全表
     * @param wrapper
     * @return
     */
    List<Goods> getList(Wrapper<Goods> wrapper);
}
