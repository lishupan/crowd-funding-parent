package com.offcn.project.controller;

import com.offcn.common.response.AppResponse;
import com.offcn.project.enums.ProjectImageTypeEnume;
import com.offcn.project.po.TProject;
import com.offcn.project.po.TProjectImages;
import com.offcn.project.po.TReturn;
import com.offcn.project.service.ProjectInfoService;
import com.offcn.project.vo.resp.ProjectDetailVo;
import com.offcn.project.vo.resp.ProjectVo;
import com.offcn.utils.OssTemplate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "项目处理接口")
@RestController
@RequestMapping("project")
@Slf4j
public class ProjectInfoController {

    @Autowired
    private OssTemplate ossTemplate;

    @Autowired
    private ProjectInfoService projectInfoService;
    //上传处理方法
    @ApiOperation("上传处理方法")
    @PostMapping("upload")
    public AppResponse<Map<String,Object>> upload(@RequestParam("file") MultipartFile[] files){
        Map<String,Object> map=new HashMap<>();

        //创建一个集合，存储所有上传的文件url地址
        List list=new ArrayList();
        //判断上传文件数组，是否为空
        if(files!=null&&files.length>0){
            //遍历上传文件数组
            for (MultipartFile file : files) {
                //判断上传文件是否存在
                if(!file.isEmpty()){
                    //调用osstemplate，上传到oss
                    try {
                        String uploadUrl = ossTemplate.upload(file.getInputStream(), file.getOriginalFilename());
                        list.add(uploadUrl);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        //把上传成功全部的文件集合封装到map
        map.put("urls",list);

        log.debug("上传文件成功:{}",list);


        return AppResponse.ok(map);



    }

    //获取全部项目调用接口
    @ApiOperation("获取全部项目调用接口")
    @GetMapping("/all")
    public AppResponse<List<ProjectVo>> findAllProjects(){
        List<TProject> projectList = projectInfoService.getAllProjects();
        //创建项目vo集合
        List<ProjectVo> projectVoList=new ArrayList<>();
        //遍历项目集合
        for (TProject project : projectList) {
            //创建项目vo对象
            ProjectVo projectVo = new ProjectVo();
            //复制项目对象属性值到项目vo对象属性值
            BeanUtils.copyProperties(project,projectVo);

            //获取项目编号，对应的项目全部配图
            List<TProjectImages> projectImagesList = projectInfoService.getProjectImages(project.getId());
            //遍历全部项目配图集合
            for (TProjectImages projectImages : projectImagesList) {
                //判断项目配图的类型，是否是头图
                if(projectImages.getImgtype()== ProjectImageTypeEnume.HEADER.getCode()){
                    //设置图片访问路径属性到项目vo的配图属性值
                    projectVo.setHeaderImage(projectImages.getImgurl());
                    break;
                }
            }

            //添加项目vo对象到volist
            projectVoList.add(projectVo);


        }

        return AppResponse.ok(projectVoList);
    }

    //根据项目编号，获取项目详情
    @ApiOperation("根据项目编号，获取项目详情")
    @GetMapping("/details/info/{projectId}")
    public AppResponse<ProjectDetailVo> getProjectDetail(@PathVariable("projectId") Integer projectId){
        //1、根据项目编号，获取项目详细信息
        TProject project = projectInfoService.getProjectInfo(projectId);
        //2、创建项目详情vo
        ProjectDetailVo projectDetailVo = new ProjectDetailVo();
        //3、复制项目详细信息对象属性值到项目详情vo
        BeanUtils.copyProperties(project,projectDetailVo);

        //4、根据项目编号，读取项目全部配图
        List<TProjectImages> projectImagesList = projectInfoService.getProjectImages(projectId);
        //创建一个详情图地址集合
        List<String> detailsImagesList=new ArrayList<>();
        //遍历项目全部配图
        for (TProjectImages projectImages : projectImagesList) {
            //判断图片类型，如果是头图，设置头图属性值到项目详情vo
            if(projectImages.getImgtype()==ProjectImageTypeEnume.HEADER.getCode()){
                projectDetailVo.setHeaderImage(projectImages.getImgurl());
            }else {
                //加入详情图集合
                detailsImagesList.add(projectImages.getImgurl());
            }
        }
        //关联详情图集合 到项目详情vo
        projectDetailVo.setDetailsImage(detailsImagesList);

        //根据项目编号获取对应回报集合
        List<TReturn> projectReturnList = projectInfoService.findProjectReturn(projectId);
        //关联项目回报集合到项目详情vo
        projectDetailVo.setProjectReturns(projectReturnList);

        return AppResponse.ok(projectDetailVo);


    }

    //根据回报编号，获取回报详情信息
    @ApiOperation("根据回报编号，获取回报详情信息")
    @GetMapping("/returns/info/{returnId}")
    public AppResponse<TReturn> findTreturn(@PathVariable("returnId") Integer returnId){
        return AppResponse.ok(projectInfoService.findTreturn(returnId));
    }
}
