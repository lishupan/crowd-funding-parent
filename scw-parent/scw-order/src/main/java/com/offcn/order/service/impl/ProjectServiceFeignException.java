package com.offcn.order.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.resp.TReturn;
import org.springframework.stereotype.Component;

@Component
public class ProjectServiceFeignException implements ProjectServiceFeign {
    @Override
    public AppResponse<TReturn> findTreturnInfo(Integer returnId) {
        TReturn tReturn = new TReturn();
        tReturn.setContent("调用项目服务，获取回报详细失败");
        return AppResponse.fail(tReturn);
    }
}
