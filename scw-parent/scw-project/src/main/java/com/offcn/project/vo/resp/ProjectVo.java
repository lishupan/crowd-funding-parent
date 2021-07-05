package com.offcn.project.vo.resp;

import lombok.Data;

import java.io.Serializable;
@Data
public class ProjectVo implements Serializable {

    //项目创建方编号
    private Integer memberId;

    //项目编号
    private Integer id;

    //项目名称
    private String name;
    //项目介绍
    private String remark;

    //项目头图
    private String headerImage;
}
