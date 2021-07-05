package com.offcn.order.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private ProjectServiceFeign projectServiceFeign;
    @GetMapping("demo1")
    public TReturn demo1(){
        AppResponse<TReturn> appresponse = projectServiceFeign.findTreturnInfo(12);
        return appresponse.getData();
    }
}
