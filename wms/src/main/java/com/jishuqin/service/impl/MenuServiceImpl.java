package com.jishuqin.service.impl;

import com.baomidou.mybatisplus.core.conditions.interfaces.Compare;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jishuqin.entity.Menu;
import com.jishuqin.mapper.MenuMapper;
import com.jishuqin.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jishuqin.util.RedisUtil;
import org.apache.ibatis.jdbc.Null;
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
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<Menu> getList(String roleId) {
        if(redisUtil.hasKey("Menu" + roleId)){
            return (List<Menu>) redisUtil.getValue("Menu" + roleId);
        }else {
            QueryWrapper<Menu> wrapper = new QueryWrapper<>();
            wrapper.like("menuRight",roleId);
            List<Menu> menus = menuMapper.selectList(wrapper);
            redisUtil.setValue("Menu" + roleId,menus, null);
            return menus;
        }
    }
}
