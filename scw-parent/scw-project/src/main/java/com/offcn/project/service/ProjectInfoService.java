package com.offcn.project.service;

import com.offcn.project.po.TProject;
import com.offcn.project.po.TProjectImages;
import com.offcn.project.po.TReturn;

import java.util.List;

public interface ProjectInfoService {

    //获取全部项目
    public List<TProject> getAllProjects();

    //获取指定项目的配图
    public List<TProjectImages> getProjectImages(int projectId);

    //根据项目编号，获取项目基本信息对象
    public TProject getProjectInfo(int projectId);

    //获取指定项目全部回报信息
    public List<TReturn> findProjectReturn(Integer projectId);

    //根据回报编号，读取对应回报信息
    public TReturn findTreturn(Integer returnId);
}
