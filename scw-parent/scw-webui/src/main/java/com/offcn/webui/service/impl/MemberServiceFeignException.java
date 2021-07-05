package com.offcn.webui.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.service.MemberServiceFeign;
import com.offcn.webui.vo.resp.UserAddressVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberServiceFeignException implements MemberServiceFeign {
    @Override
    public AppResponse<UserRespVo> login(String username, String password) {

        AppResponse<UserRespVo> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用用户微服务的，登录接口失败");
        return appResponse;
    }

    @Override
    public AppResponse<UserRespVo> userInfo(String id) {
        AppResponse<UserRespVo> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用用户微服务的，用户详情接口失败");
        return appResponse;
    }

    @Override
    public AppResponse<List<UserAddressVo>> findUserAddressList(String accessToken) {
        AppResponse<List<UserAddressVo>> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用用户微服务的，用户地址接口失败");
        return appResponse;
    }
}
