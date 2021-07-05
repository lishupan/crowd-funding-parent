package com.offcn.webui.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.service.MemberServiceFeign;
import com.offcn.webui.service.ProjectServiceFeign;
import com.offcn.webui.vo.resp.ProjectDetailVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import com.offcn.webui.vo.resp.UserAddressVo;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private MemberServiceFeign memberServiceFeign;

    //根据项目编号，获取项目详情信息
    @RequestMapping("projectInfo")
    public String getProjectInfo(Integer id, Model model, HttpSession session){

        //调用项目接口，获取对应项目详情
        AppResponse<ProjectDetailVo> appResponse = projectServiceFeign.detailsInfo(id);
        ProjectDetailVo projectDetailVo = appResponse.getData();
        //把获取到项目详情对象封装到数据模型
        model.addAttribute("DetailVo",projectDetailVo);
        //同时写入项目详情数据到session
        session.setAttribute("DetailVo",projectDetailVo);

        //跳转到视图
        return "project/project";
    }

    //跳转到回报确认页面
    @RequestMapping("/confirm/returns/{projectId}/{returnId}")
    public String returnInfo(@PathVariable("projectId") Integer projectId, @PathVariable("returnId") Integer returnId, Model model, HttpSession session){
        //从session获取项目信息
    ProjectDetailVo projectDetailVo= (ProjectDetailVo) session.getAttribute("DetailVo");

    //调用项目接口，获取指定回报id对应回报详情
        AppResponse<ReturnPayConfirmVo> appResponse = projectServiceFeign.returnInfo(returnId);
        ReturnPayConfirmVo returnPayConfirmVo = appResponse.getData();

        //设置项目编号
        returnPayConfirmVo.setProjectId(projectId);
        //设置项目名称
        returnPayConfirmVo.setProjectName(projectDetailVo.getName());
        //设置项目描述
        returnPayConfirmVo.setProjectRemark(projectDetailVo.getRemark());
        //设置发起人编号
        returnPayConfirmVo.setMemberId(projectDetailVo.getMemberid());
        //根据用户id，获取用户信息
        AppResponse<UserRespVo> userRespVoAppResponse = memberServiceFeign.userInfo(projectDetailVo.getMemberid()+"");
        UserRespVo userRespVo = userRespVoAppResponse.getData();

        //设置会员名称
        returnPayConfirmVo.setMemberName(userRespVo.getUsername());

        //把回报确认对象封装到数据模型
        model.addAttribute("returnConfirm",returnPayConfirmVo);
        //同时写入session
        session.setAttribute("returnConfirm",returnPayConfirmVo);

        //跳转视图
        return "project/pay-step-1";

    }

    //跳转到订单确认页
    @RequestMapping("confirm/order/{num}")
    public String confirmOrder(@PathVariable("num") Integer num,Model model,HttpSession session){
        //从session读取用户信息
     UserRespVo userRespVo= (UserRespVo) session.getAttribute("sessionMember");

     //判断用户是否登录
        if(userRespVo==null){
            //记录当前地址 ,要跳转到订单确认页地址
            session.setAttribute("preUrl","project/confirm/order/"+num);
            //跳转到登录页面
            return "redirect:/login.html";
        }

        //用户登录
        //获取accesstoken
        String accessToken = userRespVo.getAccessToken();
        //调用用户接口，获取对应地址集合
        AppResponse<List<UserAddressVo>> appResponse = memberServiceFeign.findUserAddressList(accessToken);

        List<UserAddressVo> addressVoList = appResponse.getData();
        //封装用户地址集合到数据模型
        model.addAttribute("addresses",addressVoList);

        //从session读取项目回报信息
        ReturnPayConfirmVo returnPayConfirmVo= (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        //设置支持数量
        returnPayConfirmVo.setNum(num);
        //设置支持总金额
        returnPayConfirmVo.setTotalPrice(new BigDecimal(num*returnPayConfirmVo.getSupportmoney()+returnPayConfirmVo.getFreight()));

        //重新保存项目回报信息到session
        session.setAttribute("returnConfirm",returnPayConfirmVo);

        //跳转到订单确认模板
        return "project/pay-step-2";
    }
}
