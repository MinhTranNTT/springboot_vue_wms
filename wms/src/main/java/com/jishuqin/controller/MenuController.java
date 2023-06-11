package com.jishuqin.controller;


import com.jishuqin.common.Result;
import com.jishuqin.entity.Menu;
import com.jishuqin.entity.User;
import com.jishuqin.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author jishuqin
 * @since 2022-12-18
 */
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/list")
    public Result list(@RequestParam String roleId) {
        List list  = menuService.getList(roleId);
        return Result.suc(list);
    }
}
