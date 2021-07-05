package com.offcn.webui.service;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.config.FeignConfig;
import com.offcn.webui.service.impl.OrderServiceFeignException;
import com.offcn.webui.vo.resp.OrderFormInfoSubmitVo;
import com.offcn.webui.vo.resp.TOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@FeignClient(value = "SCW-ORDER",configuration = FeignConfig.class,fallback = OrderServiceFeignException.class)
public interface OrderServiceFeign {

    //保存订单方法
    @PostMapping("/order/createOrder")
    public AppResponse<TOrder> saveOrder(@RequestBody OrderFormInfoSubmitVo vo);
}
