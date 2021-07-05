package com.offcn.project.controller;

import com.alibaba.fastjson.JSON;
import com.offcn.common.response.AppResponse;
import com.offcn.project.constans.ProjectConstant;
import com.offcn.project.enums.ProjectStatusEnume;
import com.offcn.project.po.TReturn;
import com.offcn.project.service.ProjectCreateService;
import com.offcn.project.vo.req.BaseVo;
import com.offcn.project.vo.req.ProjectBaseInfoVo;
import com.offcn.project.vo.req.ProjectRedisStorageVo;
import com.offcn.project.vo.req.ProjectReturnVo;
import com.sun.org.apache.regexp.internal.RE;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Api(tags = "项目创建接口")
@RestController
@RequestMapping("project")
public class ProjectCreateController {

    @Autowired
    private ProjectCreateService projectCreateService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    //项目发起第1步-阅读同意协议
    @GetMapping("init")
    public AppResponse<String> init(BaseVo vo){
        //获取acessToken
        String accessToken = vo.getAccessToken();

        //校验accesstoken
        String memberIdStr = stringRedisTemplate.boundValueOps(accessToken).get();
        //判断从redis读取memberIDSTR失败
        if(StringUtils.isEmpty(memberIdStr)){
            return AppResponse.fail("acessToken无效");
        }

        //转换用户编号字符串为 InTEGER
        int memberId = Integer.parseInt(memberIdStr);
        //调用项目创建服务，初始化项目
        String projectToken = projectCreateService.initCreateProject(memberId);

        return AppResponse.ok(projectToken);

    }

    //项目创建第二步：录入项目详细信息
    @ApiOperation("项目创建第二步：录入项目详细信息")
    @PostMapping("savebaseInfo")
    public AppResponse<String> savebaseInfo(ProjectBaseInfoVo vo){

        //1、获取项目临时token
        String projectToken = vo.getProjectToken();
        //2、根据项目token从redis读取项目信息
        String jsonStr = stringRedisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken).get();

        //3、解析json字符串为 项目临时存储到redis的对象
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(jsonStr, ProjectRedisStorageVo.class);
        //4、复制从前端收集过啦项目基本信息属性值到 项目redis对象
        BeanUtils.copyProperties(vo,projectRedisStorageVo);
        //5、把项目redis对象，转换为字符串
        String jsonStringNew = JSON.toJSONString(projectRedisStorageVo);
        //更新保存到redis
        stringRedisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken).set(jsonStringNew);

        return AppResponse.ok(projectToken);
    }

    //项目创建第三步：录入项目回报信息
    @ApiOperation("项目创建第三步：录入项目回报信息")
    @PostMapping("saveReturnInfo")
    public AppResponse<Object> saveReturnInfo(@RequestBody List<ProjectReturnVo> pro){
        //提取第一个项目回报
        ProjectReturnVo projectReturnVo = pro.get(0);
        //从项目回报对象获取项目临时token
        String projectToken = projectReturnVo.getProjectToken();
        //根据项目临时token，去redis读取项目redis存储信息
        String jsonStr = stringRedisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken).get();

        //转换为项目redis存储对象
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(jsonStr, ProjectRedisStorageVo.class);
        //创建一个集合，存储项目回报信息
        List<TReturn> returnList=new ArrayList<>();
        //遍历项目回报集合
        for (ProjectReturnVo returnVo : pro) {
            TReturn tReturn = new TReturn();
            //复制 returnVo 属性值到项目回报对象
            BeanUtils.copyProperties(returnVo,tReturn);
            //把项目回报对象，存储到list
            returnList.add(tReturn);
        }
        //关联设置项目回报集合到redis项目对象
        projectRedisStorageVo.setProjectReturns(returnList);

        //转换项目redis对象为惊悚字符串
        String jsonStringNew = JSON.toJSONString(projectRedisStorageVo);
        //更新到redis
        stringRedisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX+projectToken).set(jsonStringNew);

        return AppResponse.ok("ok");

    }

    //项目创建第4步，保存项目信息到数据库
    @ApiOperation("项目创建第4步，保存项目信息到数据库")
    @PostMapping("submit")
    public AppResponse<Object> submit(String accessToken,String projectToken,String ops){
        //1、校验accesstoken
        //根据accessToken从redis读取用户id
        String memeberId = stringRedisTemplate.boundValueOps(accessToken).get();
        if(StringUtils.isEmpty(memeberId)){
            return AppResponse.fail("accesstoken校验失败");
        }

        //2、根据项目token从redis读取项目redis信息
        String jsonStr = stringRedisTemplate.boundValueOps(ProjectConstant.TEMP_PROJECT_PREFIX + projectToken).get();
        //解析成项目redis对象
        ProjectRedisStorageVo projectRedisStorageVo = JSON.parseObject(jsonStr, ProjectRedisStorageVo.class);

        //3、判断操作类型
        if(!StringUtils.isEmpty(ops)){

            //4、判断操作类型
            if(ops.equalsIgnoreCase("1")){
                //调用项目服务，保存项目信息
                try {
                    projectCreateService.saveProjectInfo(ProjectStatusEnume.SUBMIT_AUTH,projectRedisStorageVo);
                   return AppResponse.ok("ok");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if(ops.equalsIgnoreCase("0")){
                try {
                    projectCreateService.saveProjectInfo(ProjectStatusEnume.DRAFT,projectRedisStorageVo);
                    return AppResponse.ok("OK");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }else {
            return AppResponse.fail("OPS为空");
        }

        return AppResponse.fail("保存项目信息失败");
    }
}
