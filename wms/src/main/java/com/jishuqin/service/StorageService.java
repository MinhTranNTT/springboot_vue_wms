package com.jishuqin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.entity.Goodstype;
import com.jishuqin.entity.Storage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jishuqin.mapper.StorageMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
public interface StorageService{
    /**
     * 根据id删除仓库
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteStorageById(Integer id) throws Exception;

    /**
     * 保存仓库
     * @param storage
     * @return
     */
    boolean save(Storage storage);

    /**
     * 根据id修改仓库
     * @param storage
     * @return
     */
    boolean updateById(Storage storage);

    /**
     * 分页显示仓库
     * @param page
     * @param lambdaQueryWrapper
     * @return
     */
    IPage page(Page<Storage> page, LambdaQueryWrapper<Storage> lambdaQueryWrapper);

    List list();

    /**
     * 根据条件查询
     * @param wrapper
     * @return
     */
    Storage getOne(QueryWrapper<Storage> wrapper);
}
