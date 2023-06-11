package com.jishuqin.service;

import com.jishuqin.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-14
 */
public interface UserService extends IService<User> {
    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);
}
