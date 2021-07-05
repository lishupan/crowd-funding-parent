package com.offcn.webui.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.webui.service.OrderServiceFeign;
import com.offcn.webui.vo.resp.OrderFormInfoSubmitVo;
import com.offcn.webui.vo.resp.ReturnPayConfirmVo;
import com.offcn.webui.vo.resp.TOrder;
import com.offcn.webui.vo.resp.UserRespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    //保存订单方法
    @RequestMapping("save")
    public String OrderSave(OrderFormInfoSubmitVo vo, HttpSession session){
        //从session获取用户信息
     UserRespVo userRespVo= (UserRespVo) session.getAttribute("sessionMember");
     //判断用户是否为空
        if(userRespVo==null){
            //跳转到登录页面
            return "redirect:/login.html";
        }

        //从用户信息获取accessToken
        String accessToken = userRespVo.getAccessToken();
        //设置accesstoken到vo
        vo.setAccessToken(accessToken);
        //从session获取项目回报确认信息
    ReturnPayConfirmVo returnPayConfirmVo= (ReturnPayConfirmVo) session.getAttribute("returnConfirm");
        //判断项目回报确认信息为空
        if(returnPayConfirmVo==null){
            //跳转到登录页面
            return "redirect:/login.html";
        }
        //设置项目编号
        vo.setProjectid(returnPayConfirmVo.getProjectId());
        //回报id
        vo.setReturnid(returnPayConfirmVo.getId());
        //支持的数量
        vo.setRtncount(returnPayConfirmVo.getNum());
        //调用订单服务，保存订单
        AppResponse<TOrder> appResponse = orderServiceFeign.saveOrder(vo);
        TOrder order = appResponse.getData();
        System.out.println("保存订单:"+order);

        //跳转到我的项目
        return "member/minecrowdfunding";
    }
}
