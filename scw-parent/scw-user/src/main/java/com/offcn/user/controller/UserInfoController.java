package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.resp.UserAddressVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
@Api(tags = "用户信息接口")
@RestController
@RequestMapping("user")
public class UserInfoController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //根据用户编号，获取地址集合
    @ApiOperation("根据用户编号，获取地址集合")
    @ApiImplicitParam(name = "accessToken",value = "accessToken",required = true)
    @RequestMapping("/info/address")
    public AppResponse<List<UserAddressVo>> findUserAddress(String accessToken){
        String memeberId = stringRedisTemplate.opsForValue().get(accessToken);
        //判断从redis读取memeberId为空
        if(StringUtils.isEmpty(memeberId)){
            AppResponse<List<UserAddressVo>> appResponse = AppResponse.fail(null);
            appResponse.setMsg("鉴权失败，无效的accessToken");
            return appResponse;
        }

        //调用用户服务，根据用户编号，获取地址集合
        List<TMemberAddress> memberAddressList = userService.addressList(Integer.parseInt(memeberId));

        //创建vo集合
        List<UserAddressVo> voList=new ArrayList<>();
        //遍历地址集合
        for (TMemberAddress memberAddress : memberAddressList) {
            UserAddressVo vo=new UserAddressVo();
            //复制地址对象属性值到vo
            BeanUtils.copyProperties(memberAddress,vo);
            //封装vo到 volist
            voList.add(vo);

        }

        return AppResponse.ok(voList);


    }
}
