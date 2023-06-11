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
import com.jishuqin.entity.Storage;
import com.jishuqin.entity.Storage;
import com.jishuqin.service.GoodsService;
import com.jishuqin.service.StorageService;
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
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    @Autowired
    private GoodsService goodsService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Storage storage){
        return storageService.save(storage)? Result.suc():Result.fail();
    }
    //更新
    @PostMapping("/update")
    public Result update(@RequestBody Storage storage){
        return storageService.updateById(storage)?Result.suc():Result.fail();
    }
    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id){
        try{
           storageService.deleteStorageById(id);
           return Result.suc();
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Result.fail();
        }
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");

        Page<Storage> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper();
        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            lambdaQueryWrapper.like(Storage::getName, name);
        }

        IPage result = storageService.page(page, lambdaQueryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }

    @GetMapping("/list")
    public Result list(){
        List list = storageService.list();
        return Result.suc(list);
    }

    @GetMapping("/findByName")
    public Result findByName(@RequestParam String name){
        QueryWrapper<Storage> wrapper = new QueryWrapper();
        wrapper.eq("name",name);
        Storage storage = storageService.getOne(wrapper);
        return storage != null ? Result.suc(storage) :Result.fail();
    }
}
