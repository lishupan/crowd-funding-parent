package com.offcn.webui.vo.resp;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ProjectDetailVo implements Serializable {
    private Integer memberid;// 项目创建者id
    private Integer id; //项目编号

    private String name;//项目名称

    private String remark;//项目简介

    private Long money;//筹资金额

    private Integer day;//筹资天数

    private String status;//筹资状态  0 - 即将开始， 1 - 众筹中， 2 - 众筹成功， 3 - 众筹失败

    private String deploydate;//发布日期
    private Long supportmoney = 0L;//支持金额
    private Integer supporter = 0;//支持者数量
    private Integer completion = 0;//完成度
    private Integer follower = 100;//关注者数量
    // 项目头部图片
    private String headerImage;
    // 项目详情图片
    private List<String> detailsImage;
    // 发起人信息：自我介绍，详细自我介绍，联系电话，客服电话
    // 项目回报
    private List<ProjectReturnVo> projectReturns;
}
