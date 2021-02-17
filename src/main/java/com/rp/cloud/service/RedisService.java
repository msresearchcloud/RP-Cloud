package com.rp.cloud.service;

public interface RedisService {
    Boolean put(String userId, Integer count);
    Integer get(String userId);
    Boolean reset(String userId);
}
