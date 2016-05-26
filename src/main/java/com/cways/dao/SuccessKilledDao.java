package com.cways.dao;

import com.cways.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Cways on 2016/5/18.
 */
public interface SuccessKilledDao {
    /**
     *
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     *
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(long seckillId);
}
