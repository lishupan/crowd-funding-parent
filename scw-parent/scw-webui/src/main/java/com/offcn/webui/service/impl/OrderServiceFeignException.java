package com.offcn.webui.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.service.OrderServiceFeign;
import com.offcn.webui.vo.resp.OrderFormInfoSubmitVo;
import com.offcn.webui.vo.resp.TOrder;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceFeignException implements OrderServiceFeign {
    @Override
    public AppResponse<TOrder> saveOrder(OrderFormInfoSubmitVo vo) {
        AppResponse<TOrder> appResponse = AppResponse.fail(null);
        appResponse.setMsg("调用订单服务，保存订单失败");
        return null;
    }
}
