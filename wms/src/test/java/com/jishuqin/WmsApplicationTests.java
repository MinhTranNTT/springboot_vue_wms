package com.jishuqin;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jishuqin.entity.Goods;
import com.jishuqin.entity.Menu;
import com.jishuqin.entity.User;
import com.jishuqin.service.GoodsService;
import com.jishuqin.service.MenuService;
import com.jishuqin.service.UserService;
import com.jishuqin.util.BCrypt;
import com.jishuqin.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest
class WmsApplicationTests {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Test
    void testFindGoods() {
//        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
//        wrapper.eq("name","足球");
//        Goods one = goodsService.getOne(wrapper);
//        System.out.println(one);
    }

    @Test
    void testJwt(){
        // 加密
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6ImFkbWluIiwicm9sZSI6ImFkbWluIiwic3ViIjoiYWRtaW4tbG9naW4iLCJleHAiOjE2NzE3MTI0NzMsImp0aSI6IjNlMTYyNjI1LTJiZTEtNDY4NS04YWRkLTZlYTE4ZjA2YTE5MCJ9.8llUUiTfJ2NUH3aHO8zdcewlJmq8mcjV3d4Zd-NAnWs";
        System.out.println(token);
        System.out.println(JwtUtil.verifyToken(token));
        System.out.println(JwtUtil.getUsername(token));
        System.out.println(JwtUtil.getRole(token));

    }

    @Test
    void testChangeUserValid(){
        User user = new User();
        user.setId(6);
        user.setIsvalid("N");
        boolean b = userService.updateById(user);
    }

    @Test
    void testMenu(){
        List<Menu> list = menuService.getList("1");
        System.out.println(list);
    }

    @Test
    void testBCrypt(){
        String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        String password = BCrypt.hashpw("123456",gensalt); // 密码，盐
        System.out.println(password);
        User user = new User();
        user.setId(1);
        user.setPassword(password);
        userService.updateById(user);

        boolean checkpw = BCrypt.checkpw("123456", user.getPassword());
        System.out.println(checkpw);
    }

    @Test
    void testLogin(){
        String password = "123456";
        String no = "admin";
        User user = userService.login(no, password);
        boolean checkpw = BCrypt.checkpw(password, user.getPassword());
        System.out.print(checkpw);
    }

    @Test
    void testGetOne(){
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("goodsType",2);
        List<Goods> list = goodsService.getList(wrapper);
        System.out.println(list);
    }

    @Test
    void testCheckToken(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6Iui2hee6p-euoeeQhuWRmCIsInJvbGUiOiJhZG1pbiIsInN1YiI6ImFkbWluLWxvZ2luIiwiZXhwIjoxNjcyNDYwMDE3LCJqdGkiOiI1YzUwZDcxNy1lMzQyLTRiYTgtYTRhZC0wZTIzOWJjODk5NDQifQ.bp-39qmVaPwldgf-x2dhM4UK-kvGrY2SiQ1Nwic-acQ";
        if(JwtUtil.verifyToken(token)){
            String role = JwtUtil.getRole(token);
            QueryWrapper<User> wrapper = new QueryWrapper<>();
            wrapper.eq("no",role);
            User user = userService.getOne(wrapper);
            System.out.println(user);
        }
    }

    @Test
    void testCheckPassword(){
        User user = userService.getById(2);
        String password = "Qwe123456.+";
        System.out.println(user);
        boolean checkpw = BCrypt.checkpw(password, user.getPassword());
        System.out.println(checkpw);
    }

}
