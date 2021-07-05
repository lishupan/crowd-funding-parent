package com.offcn.webui.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.service.MemberServiceFeign;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.vo.resp.ProjectVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Controller
public class DispathcherController {

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping("/")
    public String toIndex(Model model){

        //首先从缓存读取全部项目数据
        List<ProjectVo> projectVoList= (List<ProjectVo>) redisTemplate.opsForValue().get("projectStr");

        //判断如果从缓存读取数据失败，继续调用项目服务，获取数据
        if(projectVoList==null) {
            //调用项目接口，获取全部项目
            AppResponse<List<ProjectVo>> appResponse = projectServiceFeign.findAllProject();
            projectVoList = appResponse.getData();
            //把读取到数据写入redis缓存
            redisTemplate.boundValueOps("projectStr").set(projectVoList, 5, TimeUnit.MINUTES);
        }
        //获取到全部项目信息封装到数据模型，返回给前端
        model.addAttribute("projectList",projectVoList);
        return "index";
    }

    /*@RequestMapping("/login.html")
    public String toLogin(){
        return "login";
    }*/

    //创建登录调用方法
    @RequestMapping("doLogin")
    public String doLogin(String loginacct, String password, HttpSession session){
        AppResponse<UserRespVo> appResponse = memberServiceFeign.login(loginacct, password);
        UserRespVo userRespVo = appResponse.getData();
        if(userRespVo!=null) {


            //登录成功，把获取到用户信息写入到session
            session.setAttribute("sessionMember",userRespVo);

            //从session获取历史跳转页面
        String preUrl= (String) session.getAttribute("preUrl");
        //如果历史跳转页面地址不为空，登录成功后就跳转到历史跳转页面
            if(!StringUtils.isEmpty(preUrl)){
                return "redirect:/"+preUrl;
            }

            //跳转到首页
            return "redirect:/";

        }else {
            //登录失败，跳转到登录页面
            return "redirect:/login.html";
        }


    }
}
