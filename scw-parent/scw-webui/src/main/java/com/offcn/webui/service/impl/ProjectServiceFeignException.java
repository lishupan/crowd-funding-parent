package com.offcn.webui.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class ProjectServiceFeignException implements ProjectServiceFeign {
    @Override
    public AppResponse<List<ProjectVo>> findAllProject() {
        AppResponse<List<ProjectVo>> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用项目微服务，获取全部项目接口失败");
        return appResponse;
    }

    @Override
    public AppResponse<ProjectDetailVo> detailsInfo(Integer projectId) {

        AppResponse<ProjectDetailVo> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用项目微服务，获取项目详情失败");
        return appResponse;
    }

    @Override
    public AppResponse<ReturnPayConfirmVo> returnInfo(Integer returnId) {
        AppResponse<ReturnPayConfirmVo> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用项目微服务，获取回报详情失败");
        return appResponse;
    }
}
