package com.rp.cloud.controller;

import com.rp.cloud.service.RedisService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.util.annotation.NonNull;

@RestController
@RequestMapping("redisservice")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @RequestMapping(value = {"/put"}, produces = {"application/json"}, method = {RequestMethod.GET})
    @ResponseBody
    public Boolean put(@NonNull @RequestParam(name = "userId") String userId, @NonNull @RequestParam(name = "count") Integer count){
        return redisService.put(userId,count);
    }

    @RequestMapping(value = {"/get"}, produces = {"application/json"}, method = {RequestMethod.GET})
    @ResponseBody
    public Integer get(@NonNull @RequestParam(name = "userId") String userId){
        return redisService.get(userId);
    }

    @RequestMapping(value = {"/reset"}, produces = {"application/json"}, method = {RequestMethod.GET})
    @ResponseBody
    public Boolean reset(@NonNull @RequestParam(name = "userId") String userId){
        return redisService.reset(userId);
    }

}
