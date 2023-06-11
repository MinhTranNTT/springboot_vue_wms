package com.jishuqin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.entity.Goodstype;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
public interface GoodstypeService{

    /**
     * 存储物品类型
     * @param goodstype
     * @return
     */
    boolean save(Goodstype goodstype);

    /**
     * 根据id修改物品类型
     * @param goodstype
     * @return
     */
    boolean updateById(Goodstype goodstype);

    /**
     * 根据id删除物品类型
     * @param id
     * @return
     */
    boolean removeById(Integer id);

    /**
     * 分页显示物品类型
     * @param page
     * @param lambdaQueryWrapper
     * @return
     */
    IPage page(Page<Goodstype> page, LambdaQueryWrapper<Goodstype> lambdaQueryWrapper);

    /**
     * 查询全表
     * @return
     */
    List list();

    /**
     * 根据条件查询物品类型
     * @param wrapper
     * @return
     */
    Goodstype getOne(QueryWrapper<Goodstype> wrapper);

}
