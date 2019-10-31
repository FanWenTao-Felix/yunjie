package org.springblade.modules.financialManagement.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.modules.businessManagement.entity.Fee;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Alexander
 * @date 2019/9/23 11:27 AM
 * 应收应付实体
 */
@Data
@ApiModel(value = "ReceivablePayable对象", description = "应收应付")
public class ReceivablePayable implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 工作号
	 */
	@ApiModelProperty(value = "工作号")
	private String internalOrderNo;

	/**
	 * 主单号
	 */

	@ApiModelProperty(value = "主单号")
	private String mainOrderNo;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@JsonFormat(
		pattern = "yyyy-MM-dd HH:mm:ss"
	)
	@ApiModelProperty(value = "创建时间")
	private Date createTime;

	/**
	 * 委托人
	 */
	/*@JsonSerialize(
		using = ToStringSerializer.class
	)*/
	@ApiModelProperty(value = "委托人")
	private String client;


	/**
	 * 类型  0应收 1应付
	 */
	@ApiModelProperty(value = "类型")
	private Integer type;


	/**
	 * 业务类型 0 空运  1 海运
	 */
	private Integer businessType;


	/**
	 * 业务员
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "业务员")
	private Long salesman;

	/**
	 * 操作员
	 */
	@JsonSerialize(
		using = ToStringSerializer.class
	)
	@ApiModelProperty(value = "操作员")
	private Long operator;


	/**
	 * 应收Map
	 */
	private Map<CustomCurrency, Double> receiveMap;

	/**
	 * 应付Map
	 */
	private Map<CustomCurrency, Double> payMap;

	/**
	 * 利润Map
	 */
	private Map<CustomCurrency, Double> profitMap;

}
