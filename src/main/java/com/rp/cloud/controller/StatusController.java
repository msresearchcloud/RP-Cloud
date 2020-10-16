package com.rp.cloud.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("statusservice")
public class StatusController {

	@RequestMapping(value = {"/status"}, produces = {"application/json"},method = {RequestMethod.GET})
    @ResponseBody
    public String getStatus() {
        return "Success";
    }
}
