package com.offcn;

import com.offcn.common.response.AppResponse;
import com.offcn.order.OrderStarterApplication;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OrderStarterApplication.class})
public class TestProjectServiceFeign {

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Test
    public void test1(){
        AppResponse<TReturn> appResponse = projectServiceFeign.findTreturnInfo(12);
        TReturn tReturn = appResponse.getData();
        System.out.println(tReturn.getContent()+" "+tReturn.getSupportmoney());
    }
}
