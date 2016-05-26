package com.cways.service.impl;

import com.cways.dao.SeckillDao;
import com.cways.dao.SuccessKilledDao;
import com.cways.dto.Exposer;
import com.cways.dto.SeckillExecution;
import com.cways.entity.Seckill;
import com.cways.entity.SuccessKilled;
import com.cways.exception.RepeatKillException;
import com.cways.exception.SeckillCloseException;
import com.cways.exception.SeckillException;
import com.cways.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.*;

/**
 * Created by Cways on 2016/5/19.
 */
//@Component \ @ Service
@Service
public class SeckillServiceImpl implements SeckillService {
    private org.slf4j.Logger logger = getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    private final String slat = "asdfefawsefawefasdfegewr*()&^&*^(*)&*^&*";

    public List<Seckill> getSeckillList() {

        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new Exposer(seckillId,false);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();

        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        String md5 = getMD5(seckillId); //TODO
        return new Exposer(true,md5,seckillId);
    }

    /**
     * 使用注解控制事务方法的优点：
     * 1、开发团队达到一致约定，明确标注事务方法的编程风格。
     * 2、保证事务方法的执行时间尽可能短，不要穿插其他网络操作RPC、HTTP请求
     * 3、 不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制。
     *
     * @param seckillId
     * @param userPhone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */

    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();
        try {
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                //没有更新到操作
                throw new SeckillCloseException("seckill is closed");
            } else {
                //记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    //重复秒杀
                    throw new RepeatKillException("seckill repeated");
                } else {
                    //秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId);
                    return new SeckillExecution(seckillId, 1, "秒杀成功", successKilled);
                }
            }
        }catch (SeckillCloseException sce){
            throw sce;
        }catch (RepeatKillException rke){
            throw rke;
        }catch(Exception e){
            logger.error(e.getMessage(),e);
            throw  new SeckillException("seckill inner error:"+e.getMessage());
        }
    }

    public String getMD5(long seckillId){
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        logger.info("getMD5=",md5);
        return md5;
    }
}
