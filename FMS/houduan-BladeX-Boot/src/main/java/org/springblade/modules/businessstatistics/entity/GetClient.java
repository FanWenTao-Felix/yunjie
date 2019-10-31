package org.springblade.modules.businessstatistics.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "client对象", description = "client对象")
public class GetClient implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "客户名称")
	private String fullName;

	@ApiModelProperty(value = "所属业务员")
	private String assalesman;

	@ApiModelProperty(value = "总柜数")
	private String concabinet;

	@ApiModelProperty(value = "总重量")
	private String conweight;

	@ApiModelProperty(value = "总体积")
	private String convolume;

	@ApiModelProperty(value = "成交票数")
	private String allticket;




}
