package com.rp.cloud.service.impl;

import com.rp.cloud.service.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RedisServiceImpl implements RedisService {

    private static  final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    @Autowired
    private StringRedisTemplate docCount;

    @Override
    public Boolean put(String userId, Integer count) {
        logger.info("Setting doc count for userId: " + userId);
        ValueOperations<String, String> map = this.docCount.opsForValue();
        if(this.docCount.hasKey(userId)) {
            logger.info("userId found");
            Integer existingCount = Integer.valueOf(map.get(userId));
            map.set(userId, String.valueOf(count + existingCount));
        }
        else {
            logger.info("UserId not found, adding a new entry");
            map.set(userId,String.valueOf(count));
        }
        return true;
    }

    @Override
    public Integer get(String userId) {
        logger.info("Fetching doc count for userId: " + userId);
        ValueOperations<String, String> map = this.docCount.opsForValue();
        if(this.docCount.hasKey(userId)) {
            logger.info("userId found");
            return Integer.valueOf(map.get(userId));
        }else {
            logger.info("UserId not found, sending 0 docCount");
            return 0;
        }
    }

    @Override
    public Boolean reset(String userId) {
        logger.info("Resetting doc count for userId: " + userId);
        ValueOperations<String, String> map = this.docCount.opsForValue();
        if(this.docCount.hasKey(userId)) {
            map.set(userId,"0");
            return true;
        }else {
            logger.info("UserId " +userId+ " not found");
            return false;
        }
    }
}
