package com.offcn.order.service.impl;

import com.offcn.common.response.AppResponse;
import com.offcn.order.enums.OrderStatusEnumes;
import com.offcn.order.mapper.TOrderMapper;
import com.offcn.order.po.TOrder;
import com.offcn.order.service.OrderService;
import com.offcn.order.service.ProjectServiceFeign;
import com.offcn.order.vo.req.OrderInfoSubmitVo;
import com.offcn.order.vo.resp.TReturn;
import com.offcn.utils.AppDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Autowired
    private TOrderMapper orderMapper;
    @Override
    public TOrder saveOrder(OrderInfoSubmitVo orderInfoSubmitVo) {

        //创建订单实体对象
        TOrder order = new TOrder();
        //从订单vo获取accessToken
        String accessToken = orderInfoSubmitVo.getAccessToken();

        //从redis读取token对应用户编号
        String memberId = stringRedisTemplate.opsForValue().get(accessToken);
        //把vo属性值复制到订单对象
        BeanUtils.copyProperties(orderInfoSubmitVo,order);
        //设置当前操作的用户编号到订单对象
        order.setMemberid(Integer.parseInt(memberId));

        //生成一个随机字符串作为订单编号
        String orderNum = UUID.randomUUID().toString().replaceAll("-", "");
        //设置订单编号到订单对象
        order.setOrdernum(orderNum);

        //设置订单的创建时间
        order.setCreatedate(AppDateUtils.getFormatTime());

        //根据订单vo传递的回报id，获取回报信息
        AppResponse<TReturn> appResponse = projectServiceFeign.findTreturnInfo(orderInfoSubmitVo.getReturnid());
        //获取响应的回报信息
        TReturn tReturn = appResponse.getData();

        //判断回报信息是否为空
        if(tReturn!=null){
            //计算应付金额 =单价*支持数量+运费
int money=tReturn.getSupportmoney()*orderInfoSubmitVo.getRtncount()+tReturn.getFreight();
    //设置应付金额到订单对象
            order.setMoney(money);
        }

        //设置订单的状态
        order.setStatus(OrderStatusEnumes.UNPAY.getCode()+"");

        //保存订单到数据库
        orderMapper.insertSelective(order);


        return order;
    }
}
