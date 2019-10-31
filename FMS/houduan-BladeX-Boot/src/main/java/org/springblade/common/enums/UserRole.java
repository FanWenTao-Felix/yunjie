package org.springblade.common.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;


public enum UserRole implements IEnum {
	ADMIN("admin", "管理员",1181379697443864578l),
	ADMINISTRATOR("administrator", "超级管理员",1123598816738675201l),
	OPERATOR("operator", "操作员",1166897724877041665l),
	SALESMAN("salesman", "业务员",1166898055392391169l),
	FINANCE("finance","财务",1182141385357254657l),
	OTHER("other", "其他角色",0l);

	private String role;
	private String description;
	private Long templateRoleId;//管理租户的业务员和操作员的角色 以及admin   的id,作为模板数据

	UserRole(String role, String description,Long templateRoleId) {
		this.role = role;
		this.description = description;
		this.templateRoleId=templateRoleId;
	}

	@Override
	@JsonValue
	public Serializable getValue() {
		return this.role;
	}

	public String getDescription(){return this.description;}

	public Long getTemplateRoleId(){return this.templateRoleId;}
	public String getTemplateRoleIdToString(){return this.templateRoleId.toString();}

	@Override
	public String toString() {
		return getValue().toString();
	}
}
