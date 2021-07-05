package com.offcn.user.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.user.component.SmsTemplate;
import com.offcn.user.po.TMember;
import com.offcn.user.service.UserService;
import com.offcn.user.vo.req.UserRegistVo;
import com.offcn.user.vo.resp.UserRespVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("user")
@Api(tags = "用户登录处理接口")
@Slf4j
public class UserLoginController {

    //注入短信发送工具类
    @Autowired
    private SmsTemplate smsTemplate;

    //注入redis
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private UserService userService;

    //发送验证码调用方法
    @ApiOperation("发送验证码调用方法")
    @ApiImplicitParam(name = "phoneNo",value = "接收手机号码",required = true)
    @PostMapping("sendCode")
    public AppResponse<Object> sendSmsCode(String phoneNo){
        //1、创建一个随机数 22s11-sww222-3edee-3ddw33
        String smscode = UUID.randomUUID().toString().substring(0, 4);
        //2、存储验证码到redis
        stringRedisTemplate.boundValueOps(phoneNo).set(smscode,5, TimeUnit.MINUTES);
        //3、发送验证码到用户手机
        String result = smsTemplate.smsSend(phoneNo, smscode);

        //4、根据响应内容判断是否发送短信验证码成功
        if(result!=null&&result.indexOf("00000")>=0){
            return AppResponse.ok("发送验证码成功");
        }else {
            return AppResponse.fail("发送验证码失败");
        }
    }

    //注册方法
    @ApiOperation("用户注册方法")
    @PostMapping("regist")
    public AppResponse<Object> regist(UserRegistVo vo){

        //1、获取注册的手机号
        String loginacct = vo.getLoginacct();
        //2、从redis读取对应手机号的验证码
        String smscodeRedis = stringRedisTemplate.boundValueOps(loginacct).get();

        //判断从redis成功读取到验证码
        if(!StringUtils.isEmpty(smscodeRedis)) {

            if (!vo.getCode().equalsIgnoreCase(smscodeRedis)) {
                return AppResponse.fail("验证码不正确，注册失败");
            } else {

                //创建一个Tmemeber
                TMember member = new TMember();
                //复制vo的属性值到po
                //member.setLoginacct(vo.getLoginacct());
               // member.setUserpswd(vo.getUserpswd());
                BeanUtils.copyProperties(vo,member);
                //验证码一致，继续完成注册
                userService.registerUser(member);
                log.debug("用户注册成功:{}",vo);
                //注册成功，清除redis的短信验证码
                stringRedisTemplate.delete(vo.getLoginacct());

                return AppResponse.ok("注册成功");

            }

        }else {
            return AppResponse.fail("读取验证码失败");
        }


    }


    //登录方法
    @ApiOperation("登录方法")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "username",value = "登录账号",required = true),
                    @ApiImplicitParam(name = "password",value = "密码",required = true)
            }
    )
    @PostMapping("login")
    public AppResponse<UserRespVo> login(String username, String password){
        //1、调用用户服务，尝试登录
        TMember member = userService.login(username, password);
        //2、判断登录对象如果为空
        if(member==null){
            AppResponse<UserRespVo> appResponse = AppResponse.fail(null);
            appResponse.setMsg("登录失败");
            return appResponse;
        }
        //3、登录成功
        //生成令牌 2222asssw2sse33sss22
        String token = UUID.randomUUID().toString().replaceAll("-", "");

        //4、创建登录成功响应对象
        UserRespVo userRespVo = new UserRespVo();
        //复制从service获取到用户实体类的属性值到vo
        BeanUtils.copyProperties(member,userRespVo);
        //5、设置token到vo
        userRespVo.setAccessToken(token);

        //6、把token存储到服务器redis  key 是 token value 是 用户id
        stringRedisTemplate.boundValueOps(token).set(member.getId()+"");

        return AppResponse.ok(userRespVo);

    }

    //根据用户id，获取用户信息
    @ApiOperation("根据用户id，获取用户信息")
    @ApiImplicitParam(name ="id",value ="用户id",required = true,type = "path")
    @GetMapping("/findUser/{id}")
    public AppResponse<UserRespVo> findUser(@PathVariable("id") Integer id){
        TMember tmember = userService.findTmemberById(id);
        UserRespVo vo=new UserRespVo();
        //复制属性值到vo
        BeanUtils.copyProperties(tmember,vo);
        return AppResponse.ok(vo);
    }
}
