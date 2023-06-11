package com.jishuqin.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.common.QueryPageParam;
import com.jishuqin.common.Result;
import com.jishuqin.dto.LoginDto;
import com.jishuqin.entity.Menu;
import com.jishuqin.entity.User;
import com.jishuqin.service.MenuService;
import com.jishuqin.service.UserService;
import com.jishuqin.util.BCrypt;
import com.jishuqin.util.JwtUtil;
import com.jishuqin.util.RedisUtil;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-14
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RedisUtil redisUtil;

    @GetMapping("/list")
    public List<User> list() {
        return userService.list();
    }

    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no) {
        List list = userService.lambdaQuery().eq(User::getNo, no).list();
        return list.size() > 0 ? Result.suc(list) : Result.fail();
    }

    // 新增
    @PostMapping("/save")
    public Result save(@RequestBody User user) {
        // 对密码使用BCrypt进行加密
        String gensalt = BCrypt.gensalt();
        String password = BCrypt.hashpw(user.getPassword(), gensalt);
        user.setPassword(password);
        return userService.save(user) ? Result.suc() : Result.fail();
    }

    // 更新
    @PostMapping("/update")
    public Result update(@RequestBody User user){
        String gensalt = BCrypt.gensalt();
        String password = BCrypt.hashpw(user.getPassword(), gensalt);
        user.setPassword(password);
        return userService.updateById(user) ? Result.suc(user) : Result.fail();
    }

    // 登录
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        User user1 = userService.login(user.getNo(), user.getPassword());
        if(user1 == null){
            return Result.result(400,"账号不存在",0L,null);
        }
        if(!BCrypt.checkpw(user.getPassword(),user1.getPassword())){
            return Result.result(400,"密码错误",0L,null);
        }
        if(("N").equals(user1.getIsvalid())){
            return Result.result(400,"用户被禁用",0L,null);
        }
        LoginDto loginDto = new LoginDto(user1.getId(),user1.getNo(),user1.getName(),
                user1.getSex(),user1.getPhone(),user1.getRoleId());
        // 生成token
        String token = JwtUtil.createToken(user1.getName(),user1.getNo());
        loginDto.setToken(token);
        //将token存放在Redis中，注意：此处是把token作为键，把username作为值
        if (!redisUtil.hasKey(token))
            redisUtil.setValue(token,user1.getNo(),null);

        List menuList = menuService.getList(loginDto.getRoleId().toString());
        HashMap res = new HashMap();
        res.put("user", loginDto);
        res.put("menu",menuList);
        return Result.suc(res);
    }

    @PostMapping("/loginout")
    public Result loingout(@RequestBody LoginDto loginDto){
        String token = loginDto.getToken();
        if (redisUtil.hasKey(token))
            redisUtil.delete(token);
        return Result.suc();
    }

    // 修改
    @PostMapping("/mod")
    public boolean mod(@RequestBody User user) {
        return userService.updateById(user);
    }

    // 删除
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        return userService.removeById(id) ? Result.suc() : Result.fail();
    }

    // 查询（模糊、匹配）
    @PostMapping("/listP")
    public Result listP(@RequestBody User user) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(user.getName())) {
            lambdaQueryWrapper.like(User::getName, user.getName());
        }

        return Result.suc(userService.list(lambdaQueryWrapper));
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");

        Page<User> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(sex)) {
            lambdaQueryWrapper.eq(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(roleId)) {
            lambdaQueryWrapper.eq(User::getRoleId, roleId);
        }

        IPage result = userService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    @PostMapping("/changeUserValid")
    public Result changeUserValid(@RequestBody User user){
        User user1 = new User();
        user1.setId(user.getId());
        user1.setIsvalid(user.getIsvalid());
        return userService.updateById(user1) ? Result.suc(user1) : Result.fail();
    }

    @PostMapping("/checkPassword")
    public Result checkPassword(@RequestBody User user){
        User user1 = userService.getById(user.getId());
        if (user1 != null && BCrypt.checkpw(user.getPassword(),user1.getPassword())){
            return Result.suc();
        }
        return Result.fail();
    }

}
