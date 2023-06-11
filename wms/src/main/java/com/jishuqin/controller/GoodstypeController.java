package com.jishuqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.common.QueryPageParam;
import com.jishuqin.common.Result;
import com.jishuqin.entity.Goods;
import com.jishuqin.entity.Goodstype;
import com.jishuqin.service.GoodsService;
import com.jishuqin.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
@RequestMapping("/goodstype")
public class GoodstypeController {
    @Autowired
    private GoodstypeService goodstypeService;

    @Autowired
    private GoodsService goodsService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goodstype goodstype){
        return goodstypeService.save(goodstype)? Result.suc():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Goodstype goodstype){
        return goodstypeService.updateById(goodstype)?Result.suc():Result.fail();
    }
    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id){
        QueryWrapper<Goods> wrapper = new QueryWrapper<>();
        wrapper.eq("goodsType",id);
        List<Goods> list = goodsService.getList(wrapper);
        if (list.size() > 0){
            return Result.fail("删除失败，存在该分类的物品！");
        }
        return goodstypeService.removeById(id)?Result.suc():Result.fail("删除失败！");
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<Goodstype> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Goodstype::getName, name);
        }

        IPage result = goodstypeService.page(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }

    @GetMapping("/list")
    public Result list(){
        List list = goodstypeService.list();
        return Result.suc(list);
    }

    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        QueryWrapper<Goodstype> wrapper = new QueryWrapper();
        wrapper.eq("name",name);
        Goodstype goodstype = goodstypeService.getOne(wrapper);
        return goodstype != null ? Result.suc(goodstype) :Result.fail();
    }
}
