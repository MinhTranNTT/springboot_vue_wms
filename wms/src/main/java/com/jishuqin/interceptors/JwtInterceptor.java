package com.jishuqin.interceptors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jishuqin.entity.User;
import com.jishuqin.service.UserService;
import com.jishuqin.util.JwtUtil;
import com.jishuqin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader("Authorization");
        Map<String,Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json; charset=utf-8");
        if (redisUtil.hasKey(token) || JwtUtil.verifyToken(token)){
            String role = JwtUtil.getRole(token);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("no",role);
            User user = userService.getOne(wrapper);
            if (!"Y".equals(user.getIsvalid())){
                redisUtil.delete(token);
                response.sendError(403);
                return false;
            }
            return true;
        }else {
            response.sendError(401);
            return false;
        }
    }

}
