package com.cways.service;

import com.cways.dto.Exposer;
import com.cways.dto.SeckillExecution;
import com.cways.entity.Seckill;
import com.cways.exception.RepeatKillException;
import com.cways.exception.SeckillCloseException;
import com.cways.exception.SeckillException;

import java.util.List;

/**
 * 业务接口定义：三个方面，方法定义粒度、参数、返回类型（return 类型一定要友好）
 * Created by Cways on 2016/5/19.
 */
public interface SeckillService {
    List<Seckill> getSeckillList();
    Seckill getById(long seckillId);

    /**
     * 秒杀开启时输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     */
    Exposer exportSeckillUrl(long seckillId);

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
}
