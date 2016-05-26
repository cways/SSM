package com.cways.dao;

import com.cways.entity.Seckill;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * Created by Cways on 2016/5/19.
 * 配置spring和junit整合，junit启动时加载springioc容器
 * spring-test，junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring配置文件
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml"})
public class SeckillDaoTest {
//    注入dao实现类依赖
    @Resource
    private SeckillDao seckillDao;
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println(updateCount);
    }

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckillDao.queryById(id).getName());
        System.out.println(seckill.toString());
    }

    @Test
    public void queryAll() throws Exception {
        //List<Seckill> queryAll(int offset,int limit)
        /**
         * java 没有保存形参的纪律：queryAll(int offset,int limit)=>queryAll(arg0,arg1)
         */
        List<Seckill> seckillList = seckillDao.queryAll(0,5);
        for (Seckill sec:seckillList
             ) {
            System.out.println(sec.toString());
        }
    }

}