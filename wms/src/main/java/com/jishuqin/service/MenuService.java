package com.jishuqin.service;

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.jishuqin.entity.Menu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
public interface MenuService{
    /**
     * 根据用户权限查询菜单
     * @param roleId
     * @return
     */
    List<Menu> getList(String roleId);
}
