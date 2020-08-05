package com.rp.clound.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("healthservice")
public class HealthCheckController {


    @RequestMapping(value = {"/health"}, produces = {"application/json"},
            method = {RequestMethod.GET})
    @ResponseBody
    public String healthCheck() {
        return "OK";
    }



}
