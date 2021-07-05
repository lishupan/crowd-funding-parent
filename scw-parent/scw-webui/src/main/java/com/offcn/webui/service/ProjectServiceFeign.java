package com.offcn.webui.service;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.config.FeignConfig;
import com.offcn.webui.service.impl.ProjectServiceFeignException;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "SCW-PROJECT",fallback = ProjectServiceFeignException.class,configuration = FeignConfig.class)
public interface ProjectServiceFeign {

    @GetMapping("/project/all")
    public AppResponse<List<ProjectVo>> findAllProject();

    //调用获取项目详情
    @GetMapping("/project/details/info/{projectId}")
    public AppResponse<ProjectDetailVo> detailsInfo(@PathVariable("projectId") Integer projectId);

    //根据回报id，获取回报详情
    @GetMapping("/project/returns/info/{returnId}")
    public AppResponse<ReturnPayConfirmVo> returnInfo(@PathVariable("returnId") Integer returnId);
}
