package com.offcn.project.vo.req;

import com.offcn.project.po.TReturn;
import lombok.Data;

import java.util.List;
@Data
public class ProjectRedisStorageVo {

    //projectToken 项目编号
    private String projectToken;

    //项目创建者编号
    private Integer memberid;

    //项目名称
    private String name;

    //项目简介
    private String remark;

    //项目的筹集金额 单位是元
    private Long money;
    //筹资天数
    private Integer day;

    //项目包含的标签
    private List<Integer> tagids;

    //项目包含分类
    private List<Integer> typeids;

    //项目头图 一张
    private String headerImage;

    //项目的详情图 多张
    private List<String> detailsImage;

    //项目回报 多个回报
    private List<TReturn> projectReturns;


}
