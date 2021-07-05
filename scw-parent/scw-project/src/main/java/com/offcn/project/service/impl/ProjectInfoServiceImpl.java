package com.offcn.project.service.impl;

import com.offcn.project.mapper.TProjectImagesMapper;
import com.offcn.project.mapper.TProjectMapper;
import com.offcn.project.mapper.TReturnMapper;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TReturnMapper returnMapper;

    @Override
    public List<TProject> getAllProjects() {
        List<TProject> projectList = projectMapper.selectByExample(null);

        return projectList;
    }

    @Override
    public List<TProjectImages> getProjectImages(int projectId) {
        //创建查询条件对象
        TProjectImagesExample example = new TProjectImagesExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return projectImagesMapper.selectByExample(example);
    }

    @Override
    public TProject getProjectInfo(int projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    @Override
    public List<TReturn> findProjectReturn(Integer projectId) {
        TReturnExample example = new TReturnExample();
        example.createCriteria().andProjectidEqualTo(projectId);
        return returnMapper.selectByExample(example);
    }

    @Override
    public TReturn findTreturn(Integer returnId) {
        return returnMapper.selectByPrimaryKey(returnId);
    }
}
