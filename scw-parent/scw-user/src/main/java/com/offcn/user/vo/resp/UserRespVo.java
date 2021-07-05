package com.offcn.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@ApiModel("用户登录响应封装类")
@Data
public class UserRespVo implements Serializable {

    //登录成功后，服务器端生成token
    @ApiModelProperty("登录成功后，服务器端生成token")
    private String accessToken;

    private String loginacct; //存储手机号
    private String username;
    private String email;
    private String authstatus;
    private String usertype;
    private String realname;
    private String cardnum;
    private String accttype;
}
