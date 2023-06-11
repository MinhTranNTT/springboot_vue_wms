package com.jishuqin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jishuqin.entity.Goods;
import com.jishuqin.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GoodsCase {
    @Autowired
    private GoodsService goodsService;

    @Test
    void findById(){
        goodsService.getById(2);
    }

    @Test
    void saveGoods(){
       Goods goods = new Goods();
       goods.setName("百事可乐");
       goods.setStorage(2);
       goods.setGoodstype(1);
       goods.setCount(2);
       goodsService.save(goods);
    }

    @Test
    void removeById(){
        goodsService.removeById(25);
    }

    @Test
    void updateById(){
        Goods goods = new Goods();
        goods.setId(25);
        goods.setName("可口可乐");
        goods.setStorage(2);
        goods.setGoodstype(1);
        goodsService.updateById(goods);
    }

    @Test
    void getById(){
        Goods byId = goodsService.getById(26);
        System.out.println(byId);
    }
}
