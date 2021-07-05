package com.offcn.order.service;

import com.offcn.common.response.AppResponse;
import com.offcn.order.config.FeignConfig;
import com.offcn.order.service.impl.ProjectServiceFeignException;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "SCW-PROJECT",fallback = ProjectServiceFeignException.class,configuration = FeignConfig.class)
public interface ProjectServiceFeign {

    //调用根据回报编号，获取回报详情
    @GetMapping("/project/returns/info/{returnId}")
    public AppResponse<TReturn> findTreturnInfo(@PathVariable("returnId") Integer returnId);
}
