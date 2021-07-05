package com.offcn.project.service;

import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.vo.req.ProjectRedisStorageVo;

public interface ProjectCreateService {

    //第一步：指定用户同意项目许可
    public String initCreateProject(Integer memberId);

    //保存项目信息到数据库
    public void saveProjectInfo(ProjectStatusEnume projectStatusEnume, ProjectRedisStorageVo vo);
}
