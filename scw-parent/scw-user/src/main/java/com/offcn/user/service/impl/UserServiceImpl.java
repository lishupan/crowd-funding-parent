package com.offcn.user.service.impl;

import com.offcn.user.enums.UserExceptionEnum;
import com.offcn.user.exception.UserException;
import com.offcn.user.mapper.TMemberAddressMapper;
import com.offcn.user.mapper.TMemberMapper;
import com.offcn.user.po.TMember;
import com.offcn.user.po.TMemberAddress;
import com.offcn.user.po.TMemberAddressExample;
import com.offcn.user.po.TMemberExample;
import com.offcn.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TMemberMapper memberMapper;

    @Autowired
    private TMemberAddressMapper memberAddressMapper;
    @Override
    public void registerUser(TMember member) {
        //1、判断注册账号是否存在
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(member.getLoginacct());
        //查询指定账号数据
        long count = memberMapper.countByExample(example);
        //判断count大于0，账号已经存在
        if(count>0){
            throw new  UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }

        //继续完成注册，设置相关属性
        //创建加密器对象
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(member.getUserpswd());
        //设置加密后密码到memeber对象
        member.setUserpswd(encodePassword);
        //设置用户名=注册账号
        member.setUsername(member.getLoginacct());
        //设置实名认证状态 实名认证状态 0 - 未实名认证， 1 - 实名认证申请中， 2 - 已实名认证
        member.setAuthstatus("0");
        //设置用户类型: 0 - 个人， 1 - 企业
        member.setUsertype("0");
        //设置账号类型 账户类型: 0 - 企业， 1 - 个体， 2 - 个人， 3 - 政府
        member.setAccttype("2");
        memberMapper.insertSelective(member);
    }

    @Override
    public TMember login(String username, String password) {
        TMemberExample example = new TMemberExample();
        example.createCriteria().andLoginacctEqualTo(username);
        List<TMember> memberList = memberMapper.selectByExample(example);
        if(memberList!=null&&memberList.size()>0){
            //提取第一个用户
            TMember member = memberList.get(0);
            //创建密码加密器对象
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //调用密码加密器比对原始密码和明文密码是否相同
            boolean is = passwordEncoder.matches(password, member.getUserpswd());

           return is?member:null;

        }
        return null;
    }

    @Override
    public TMember findTmemberById(Integer id) {
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<TMemberAddress> addressList(Integer memberId) {

        TMemberAddressExample example = new TMemberAddressExample();
        example.createCriteria().andMemberidEqualTo(memberId);
        return memberAddressMapper.selectByExample(example);
    }
}
