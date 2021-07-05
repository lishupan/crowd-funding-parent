package com.offcn.common.response;

import com.offcn.common.enums.ResponseCodeEnume;
import lombok.Data;

@Data
public class AppResponse<T> {

    //响应状态码 从响应状态枚举类获取
    private Integer code;

    //响应消息
    private String msg;

    //响应数据
    private T data;


    //快速响应成功
    public static<T> AppResponse<T> ok(T data){
        AppResponse<T> appResponse = new AppResponse<T>();
        //设置成功状态码
        appResponse.setCode(ResponseCodeEnume.SUCCESS.getCode());
        //设置成功响应消息
        appResponse.setMsg(ResponseCodeEnume.SUCCESS.getMsg());
        //设置响应数据
        appResponse.setData(data);
        return appResponse;
    }

    //快速响应失败
    public static<T> AppResponse<T> fail(T data){
        AppResponse<T> appResponse = new AppResponse<T>();
        //设置成功状态码
        appResponse.setCode(ResponseCodeEnume.FAIL.getCode());
        //设置成功响应消息
        appResponse.setMsg(ResponseCodeEnume.FAIL.getMsg());
        //设置响应数据
        appResponse.setData(data);
        return appResponse;
    }



}
