package com.offcn.order.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "订单保存接口")
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @ApiOperation("保存订单")
    @PostMapping("createOrder")
    public AppResponse<TOrder> saveOrder(@RequestBody OrderInfoSubmitVo vo){
        //获取accessTOKEN
        String accessToken = vo.getAccessToken();
        String memeberId = stringRedisTemplate.opsForValue().get(accessToken);
        //判断从redis读取memeberId失败
        if(StringUtils.isEmpty(memeberId)){
            AppResponse<TOrder> appResponse = AppResponse.fail(null);
            appResponse.setMsg("accesstoken验证失败");
            return  appResponse;
        }

        try {
            //调用订单服务，保存订单
            TOrder tOrder = orderService.saveOrder(vo);
            return AppResponse.ok(tOrder);
        } catch (Exception e) {
            e.printStackTrace();
            return AppResponse.fail(null);
        }
    }
}
