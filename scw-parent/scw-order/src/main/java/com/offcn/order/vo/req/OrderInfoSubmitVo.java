package com.offcn.order.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
@ApiModel("订单VO")
public class OrderInfoSubmitVo implements Serializable {

    //accessToken
    @ApiModelProperty("accessToken")
    private String accessToken;
    //订单所订购项目编号
    @ApiModelProperty("订单所订购项目编号")
    private Integer projectid;
    //用户所选择回报编号
    @ApiModelProperty("用户所选择回报编号")
    private Integer returnid;
    //用户选择回报数量
    @ApiModelProperty("用户选择回报数量")
    private Integer rtncount;
    //实物回报：收货地址
    @ApiModelProperty("实物回报：收货地址")
    private String address;
    //是否开发票 0 - 不开发票， 1 - 开发票
    @ApiModelProperty("否开发票 0 - 不开发票， 1 - 开发票")
    private String invoice;
    //发票抬头
    @ApiModelProperty("发票抬头")
    private String invoictitle;
    //订单说明
    @ApiModelProperty("订单说明")
    private String remark;

}
