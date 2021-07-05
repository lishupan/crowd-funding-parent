package com.offcn.project.service.impl;

import com.alibaba.fastjson.JSON;
import com.offcn.project.constans.ProjectConstant;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.mapper.*;
import com.offcn.project.po.*;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.SimpleFormatter;

@Service
public class ProjectCreateServiceImpl implements ProjectCreateService {

   //注入redis操作工具类
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TProjectMapper projectMapper;

    @Autowired
    private TProjectImagesMapper projectImagesMapper;

    @Autowired
    private TProjectTagMapper projectTagMapper;

    @Autowired
    private TProjectTypeMapper projectTypeMapper;

    @Autowired
    private TReturnMapper returnMapper;



    @Override
    public String initCreateProject(Integer memberId) {
        //创建临时存储到redis 项目vo对象
        ProjectRedisStorageVo projectRedisStorageVo = new ProjectRedisStorageVo();
        //把项目创建者编号，关联设置到项目vo
        projectRedisStorageVo.setMemberid(memberId);

        //生成绝对不会重复随机token
        String projectToken = UUID.randomUUID().toString().replaceAll("-", "");

        //增加统一前缀
       // projectToken= ProjectConstant.TEMP_PROJECT_PREFIX+projectToken;

        //把项目vo转换为json字符串
        String jsonString = JSON.toJSONString(projectRedisStorageVo);

        //存储项目vojson字符串到redis
        stringRedisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken).set(jsonString);

        return projectToken;
    }

    @Override
    public void saveProjectInfo(ProjectStatusEnume projectStatusEnume, ProjectRedisStorageVo vo) {
        //创建项目基本信息对象
        TProject project = new TProject();
        //从redis项目对象，复制属性值到项目基本信息对象
        BeanUtils.copyProperties(vo,project);
        //设置项目基本信息对象 设置状态属性
        project.setStatus(projectStatusEnume.getCode()+"");
        //设置项目的创建时间属性
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        project.setCreatedate(dateFormat.format(new Date()));

        //设置支持者数量
        project.setSupporter(0);
        //设置完成度
        project.setCompletion(0);
        //设置已经筹集到金额
        project.setSupportmoney(0L);
        //关注着数量
        project.setFollower(0);
        //保存项目基本信息到数据库
        projectMapper.insert(project);

      //保存成功后，获取数据库生成项目id
        Integer projectId = project.getId();

        //二、保存项目头图
        String headerImageUrl = vo.getHeaderImage();
        //创建项目配图对象
        TProjectImages headProjectImages = new TProjectImages(null, projectId, headerImageUrl, (byte) 0);
        //保存头图信息到数据库
        projectImagesMapper.insert(headProjectImages);

        //三、保存项目详情图
        List<String> detailsImageList = vo.getDetailsImage();
        if(detailsImageList!=null&&detailsImageList.size()>0){
            for (String imgUrl : detailsImageList) {
                //创建项目配图对象，设置图片类型是详情图
                TProjectImages DetailProjectImages = new TProjectImages(null, projectId, imgUrl, (byte) 1);
            //保存详细图到数据库
                projectImagesMapper.insert(DetailProjectImages);
            }
        }
        //四、保存项目标签
        List<Integer> tagidList = vo.getTagids();
        if(tagidList!=null&&tagidList.size()>0){
            //遍历标签集合
            for (Integer tagid : tagidList) {
                //创建项目标签对象
                TProjectTag projectTag = new TProjectTag(null, projectId, tagid);
                //保存到数据库
                projectTagMapper.insert(projectTag);
            }
        }

        //五、保存项目分类
        List<Integer> typeidList = vo.getTypeids();
        if(typeidList!=null&&typeidList.size()>0){
            for (Integer typeId : typeidList) {
                //创建项目分类对象
                TProjectType projectType = new TProjectType(null, projectId, typeId);
                //保存项目分类到数据库
                projectTypeMapper.insert(projectType);
            }
        }

        //六、保存项目回报
        List<TReturn> returnList = vo.getProjectReturns();
        if(returnList!=null&&returnList.size()>0){
            for (TReturn tReturn : returnList) {
                //设置关联到项目编号
                tReturn.setProjectid(projectId);
                //保存项目回报到数据库
                returnMapper.insert(tReturn);

            }
        }

        //七、清除redis临时存储项目信息
        stringRedisTemplate.delete(ProjectConstant.TEMP_PROJECT_PREFIX+vo.getProjectToken());


    }
}
