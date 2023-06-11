package com.jishuqin.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jishuqin.common.QueryPageParam;
import com.jishuqin.common.Result;
import com.jishuqin.entity.Goods;
import com.jishuqin.entity.Record;
import com.jishuqin.service.GoodsService;
import com.jishuqin.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        HashMap param = query.getParam();
        String name = (String) param.get("name");
        String storage = (String) param.get("storage");
        String goodstype = (String) param.get("goodstype");
        String roleId = (String) param.get("roleId");
        String userId = (String) param.get("userId");

        Page<Record> page = new Page();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.apply(" a.goods = b.id and b.storage=c.id and b.goodsType=d.id ");

        if("2".equals(roleId)){
            queryWrapper.apply(" a.userId = " + userId);
        }

        if (StringUtils.isNotBlank(name) && !"null".equals(name)) {
            queryWrapper.like("b.name",name);
        }
        if (StringUtils.isNotBlank(storage) && !"null".equals(storage)) {
            queryWrapper.eq("c.id",storage);
        }
        if (StringUtils.isNotBlank(goodstype) && !"null".equals(goodstype)) {
            queryWrapper.eq("d.id",goodstype);
        }

        IPage result = recordService.pageC(page, queryWrapper);

        return Result.suc(result.getRecords(), result.getTotal());
    }

    //出入库
    @PostMapping("/save")
    public Result save(@RequestBody Record record){
        Goods goods = goodsService.getById(record.getGoods());
        int n = record.getCount();
        String action = record.getAction();
        // action"2"出库
        if ("2".equals(action)){
            if (goods.getCount() - n < 0){
                return Result.fail("库存不足，无法出库！");
            }
            n = -n;
            record.setCount(n);
        }
        int num = goods.getCount()+n;
        goods.setCount(num);
        goodsService.updateById(goods);

        return recordService.save(record) ? Result.suc() : Result.fail("操作失败！");
    }

    @PostMapping("/delete")
    public Result delete(@RequestBody List<Record> list){
        return  recordService.deleteByList(list) ? Result.suc() : Result.fail();
    }
}
