package com.offcn.user.service;

import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;

import java.util.List;

public interface UserService {

    //注册方法
    public void registerUser(TMember member);

    //登录方法
    public TMember login(String username,String password);

    //根据用户id，获取用户信息
    public TMember findTmemberById(Integer id);

    //根据用户编号获取对应地址集合
    public List<TMemberAddress> addressList(Integer memberId);
}
