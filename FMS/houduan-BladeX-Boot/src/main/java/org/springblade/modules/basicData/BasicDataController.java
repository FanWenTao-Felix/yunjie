package org.springblade.modules.basicData;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springblade.common.constant.CommonConstant;
import org.springblade.common.enums.UserRole;
import org.springblade.common.utils.CommonUtil;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.modules.businessManagement.entity.AirBusiness;
import org.springblade.modules.businessManagement.service.IAirBusinessService;
import org.springblade.modules.businessManagement.service.ITaskNameService;
import org.springblade.modules.clientManagement.entity.ClientData;
import org.springblade.modules.clientManagement.entity.CompanyType;
import org.springblade.modules.clientManagement.enums.ClientType;
import org.springblade.modules.clientManagement.enums.CompanyTypeEnum;
import org.springblade.modules.clientManagement.service.IClientDataService;
import org.springblade.modules.clientManagement.service.ICompanyTypeService;
import org.springblade.modules.clientManagement.service.ISupplierService;
import org.springblade.modules.dictionaries.entity.*;
import org.springblade.modules.dictionaries.entity.Currency;
import org.springblade.modules.dictionaries.enums.CustomCurrency;
import org.springblade.modules.dictionaries.enums.WorkRuleUsage;
import org.springblade.modules.dictionaries.service.*;
import org.springblade.modules.financialManagement.entity.BankAccount;
import org.springblade.modules.financialManagement.entity.ReceivablePayable;
import org.springblade.modules.financialManagement.service.IBankAccountService;
import org.springblade.modules.system.entity.Role;
import org.springblade.modules.system.entity.User;
import org.springblade.modules.system.service.IRoleService;
import org.springblade.modules.system.service.IUserService;
import org.springblade.modules.system.service.impl.RoleServiceImpl;
import org.springblade.modules.system.service.impl.UserServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Alexander
 * @date 2019/8/22 3:36 PM
 * 用于给前端 选择框用的基础数据
 */

@RestController
@AllArgsConstructor
@RequestMapping("/basicData")
@Api(value = "基础数据", tags = "基础数据接口")
public class BasicDataController extends BladeController {
	private ICompanyTypeService companyTypeService;
	private ICurrencyService currencyService;
	private IUserService userService;
	private IAirService airService;
	private IWorkNumRulesService workNumRulesService;
	private IClientDataService clientDataService;
	private ISupplierService supplierService;
	private ICosttypeService costtypeService;
	private IAirBusinessService airBusinessService;
	private IShippingPortService shippingPortService;
	private IPacktypeService packtypeService;
	private RoleServiceImpl roleService;
	private IBankAccountService bankAccountService;
	private ITaskNameService taskNameService;

	public static List<Map<String, Object>> labelAndValue(IService service, String labelField, String valueField) {
		return labelAndValue(service, new QueryWrapper(), labelField, valueField);
	}

	public static List<Map<String, Object>> labelAndValue(IService service, QueryWrapper wrapper, String labelField, String valueField) {
		List<Object> data = service.list(wrapper);
		List<Map<String, Object>> list = new ArrayList<>(data.size());
		for (int i = 0; i < data.size(); i++) {
			Object item = data.get(i);
			Map<String, Object> map = new HashMap<>(2);
			try {
				Field[] fields = getAllFields(item);
				Field l_field = null;
				Field v_field = null;
				for (int j = 0; j < fields.length; j++) {
					if (fields[j].getName().equals(labelField)) l_field = fields[j];
					if (fields[j].getName().equals(valueField)) v_field = fields[j];
				}
				l_field.setAccessible(true);
				v_field.setAccessible(true);
				map.put("label", l_field.get(item));
				map.put("value", v_field.get(item));
				if (v_field.get(item) instanceof Long) map.put("value", ((Long) v_field.get(item)).toString());
				list.add(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	private static Field[] getAllFields(Object object) {
		Class clazz = object.getClass();
		List<Field> fieldList = new ArrayList<>();
		while (clazz != null) {
			fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
			clazz = clazz.getSuperclass();
		}
		Field[] fields = new Field[fieldList.size()];
		fieldList.toArray(fields);
		return fields;
	}

	private static Field getField(Object object, String fieldName) {
		Field[] fields = getAllFields(object);
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(fieldName)) {
				fields[i].setAccessible(true);
				return fields[i];
			}
		}
		return null;
	}

	public static <T> List<Map<String, Object>> fuzzySearchData(IService service, T entity, String fieldName) {
		String columnName = fieldName.replaceAll("[A-Z]", "_$0").toLowerCase();
		QueryWrapper<T> qw = new QueryWrapper<>();
		try {
			Object fieldObject = getField(entity, fieldName).get(entity);
			String fieldValue = fieldObject == null ? "" : fieldObject.toString();
			qw.like(columnName, fieldValue);
			return labelAndValue(service, qw, fieldName, fieldName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取公司类型数据
	 */
	@GetMapping("/companyType")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取公司类型数据", notes = "")
	public Object getCompanyType() {
		List<Map<String, String>> list = new ArrayList<>();
		CompanyTypeEnum[] companyTypeEnums = CompanyTypeEnum.values();
		for (int i = 0; i < companyTypeEnums.length; i++) {
			Map<String, String> map = new HashMap<>();
			CompanyTypeEnum a = companyTypeEnums[i];
			map.put("label", a.getName());
			map.put("value", a.getValue());
			list.add(map);
		}
		return list;
	}

	/**
	 * 模糊查询获取货币数据
	 */
	@GetMapping("/currency")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取货币数据", notes = "")
	public Object getCurrency(Currency currency) {
		int length = CustomCurrency.values().length;
		CustomCurrency[] currencies = CustomCurrency.values();
		List<Map<String, String>> list = new ArrayList<>();
		for (int i = 0; i < length; i++) {
			CustomCurrency c = currencies[i];
			Map<String, String> map = new HashMap<>();
			map.put("label", c.getValue());
			map.put("value", c.getValue());
			list.add(map);
		}
		return list;
	}

	/**
	 * 获取用户数据
	 */
	@GetMapping("/user")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "获取用户数据", notes = "")
	public Object getUsers() {
		BladeUser user = getUser();
		QueryWrapper<User> qw = new QueryWrapper<>();
		if (!CommonUtil.isAdministrator(user, roleService)) qw.eq("tenant_id", user.getTenantId());
		return labelAndValue(userService, qw, "realName", "id");
	}

	/**
	 * 获取业务员数据
	 */
	@GetMapping("/salesman")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "获取业务员数据", notes = "")
	public Object getSalesman() {
		BladeUser user = getUser();
		QueryWrapper<User> qw = new QueryWrapper<>();
		Role tenant_salesman = CommonUtil.getTenantRole(UserRole.SALESMAN, user, roleService);
		qw.eq("tenant_id", user.getTenantId());
		UserRole userRole = CommonUtil.judgeUserRole(user, roleService);
		if (userRole == UserRole.OPERATOR) ;
		else if (userRole == UserRole.SALESMAN) qw.eq("id", user.getUserId());
		else if (userRole == UserRole.ADMIN) ;
		else qw.eq("id", 0);
		qw.eq("role_id", tenant_salesman.getId());
		return labelAndValue(userService, qw, "realName", "id");
	}

	/**
	 * 获取操作员数据
	 */
	@GetMapping("/operator")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "获取操作员数据", notes = "")
	public Object getOperator() {
		BladeUser user = getUser();
		Role tenant_op = CommonUtil.getTenantRole(UserRole.OPERATOR, user, roleService);
		QueryWrapper<User> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		UserRole userRole = CommonUtil.judgeUserRole(user, roleService);
		if (userRole == UserRole.ADMIN) ;
		else if (userRole == UserRole.SALESMAN) ;
		else if (userRole == UserRole.OPERATOR) qw.eq("id", user.getUserId());
		else qw.eq("id", 0);
		qw.eq("role_id", tenant_op.getId());
		return labelAndValue(userService, qw, "realName", "id");
	}

	/**
	 * 获取用户对应空运工作号规则
	 */
	@GetMapping("/seaworkrules")
	@ApiOperationSupport(order = 19)
	@ApiOperation(value = "获取用户对应工作号规则", notes = "")
	public Object getSeaWorkrules() {
		BladeUser user = getUser();
		QueryWrapper<WorkNumRules> qw = new QueryWrapper<>();
		qw.like("users", user.getUserId());
		qw.eq("tenant_id", user.getTenantId());
		qw.eq("purpose", WorkRuleUsage.SEA.getValue());
		return labelAndValue(workNumRulesService, qw, "rule", "id");
	}

	/**
	 * 获取全部操作员数据
	 */
	@GetMapping("/alloperator")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "获取全部操作员数据", notes = "")
	public Object getAllOperator() {
		BladeUser user = getUser();
		QueryWrapper<User> qw = new QueryWrapper<>();
		qw.eq("role_id", CommonUtil.getTenantRole(UserRole.OPERATOR, user, roleService).getId());
		qw.eq("tenant_id", getUser().getTenantId());
		return labelAndValue(userService, qw, "realName", "id");
	}


	/**
	 * 模糊查询获取空运港口数据
	 */
	@GetMapping("/airport")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "模糊查询获取空运港口数据", notes = "")
	public Object getAirport(Air air) {
		return fuzzySearchData(airService, air, "airCode");
	}

	/**
	 * 模糊查询获取海运港口数据
	 */
	@GetMapping("/seaport")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "模糊查询获取海运港口数据", notes = "")
	public Object getSeaport(ShippingPort shippingPort) {
		return fuzzySearchData(shippingPortService, shippingPort, "portCode");
	}

	/**
	 * 获取用户对应工作号规则
	 */
	@GetMapping("/workrules")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "获取用户对应工作号规则", notes = "")
	public Object getWorkrules() {
		BladeUser user = getUser();
		QueryWrapper<WorkNumRules> qw = new QueryWrapper<>();
		qw.like("users", user.getUserId());
		qw.eq("tenant_id", user.getTenantId());
		return labelAndValue(workNumRulesService, "rule", "id");
	}

	/**
	 * 获取用户对应空运工作号规则
	 */
	@GetMapping("/airworkrules")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "获取用户对应工作号规则", notes = "")
	public Object getAirWorkrules() {
		BladeUser user = getUser();
		QueryWrapper<WorkNumRules> qw = new QueryWrapper<>();
		qw.like("users", user.getUserId());
		qw.eq("tenant_id", user.getTenantId());
		qw.eq("purpose", WorkRuleUsage.AIR.getValue());
		return labelAndValue(workNumRulesService, qw, "rule", "id");
	}

	/**
	 * 获取用户自己的客户数据
	 */
	@GetMapping("/clientdata")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取用户自己的客户数据", notes = "")
	public Object getClient() {
		BladeUser user = getUser();
		QueryWrapper<ClientData> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		if (!getUser().getRoleId().equals(CommonConstant.ADMIN_ROLE_ID) && !getUser().getRoleId().contains(CommonConstant.OPERATOR_ROLE_ID))
			qw.eq("create_user", getUser().getUserId());
		qw.eq("client_type", ClientType.CLIENT.getValue()).or().eq("client_type", ClientType.CLIENT_SUPPLIER);
		return labelAndValue(clientDataService, qw, "fullName", "id");
	}

	/**
	 * 获取租户所有客户数据
	 */
	@GetMapping("/tenant/clientdata")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取租户的客户数据", notes = "")
	public Object getTenantClient() {
		BladeUser user = getUser();
		QueryWrapper<ClientData> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		return labelAndValue(clientDataService, qw, "fullName", "id");
	}

	/**
	 * 获取银行账户数据
	 */
	@GetMapping("/bankaccount")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取租户的银行账号数据", notes = "")
	public Object getBankAccount() {
		BladeUser user = getUser();
		QueryWrapper<BankAccount> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		return labelAndValue(bankAccountService, qw, "accountName", "id");
	}


	/**
	 * 获取供应商数据
	 */
	@GetMapping("/supplier")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取供应商数据", notes = "")
	public Object getSupplier() {
		BladeUser user = getUser();
		QueryWrapper<ClientData> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		qw.eq("client_type", ClientType.CLIENT_SUPPLIER.getValue()).or().eq("client_type", ClientType.SUPPLIER.getValue());
		return labelAndValue(clientDataService, qw, "fullName", "id");
	}

	/**
	 * 获取收付款单位
	 */
	@GetMapping("/prclient")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取供应商数据", notes = "")
	public Object getPRClient(ReceivablePayable receivablePayable) {
		BladeUser user = getUser();
		if (receivablePayable == null || receivablePayable.getInternalOrderNo() == null) return null;
		QueryWrapper<ClientData> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		return labelAndValue(clientDataService, qw, "fullName", "id");
	}


	/**
	 * 判断用户身份是 admin 还是业务员还是操作员
	 */

	@PostMapping("/judgeUser")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "用户列表", notes = "传入user")
	public R<Object> judgeUser() {
		BladeUser user = getUser();
		UserRole userRole = CommonUtil.judgeUserRole(user, roleService);
		if (userRole == UserRole.ADMINISTRATOR) return R.data(UserRole.ADMIN);
		return R.data(userRole);
	}

	/**
	 * 模糊查询费用数据
	 */
	@GetMapping("/costtype")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "模糊查询费用数据", notes = "")
	public Object getCostType(Costtype costtype) {
		BladeUser user = getUser();
		QueryWrapper<Costtype> qw = new QueryWrapper<>();
		qw.eq("tenant_id", user.getTenantId());
		return fuzzySearchData(costtypeService, costtype, "costCode");
	}

	/**
	 * 模糊查询获取主单号
	 */
	@GetMapping("/mainOrderNo")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "模糊查询获取主单号", notes = "")
	public Object getMainOrderNo(AirBusiness airBusiness) {
		BladeUser user = getUser();
		QueryWrapper<AirBusiness> qw = new QueryWrapper<>();
		qw.like("main_order_no", airBusiness.getMainOrderNo());
		qw.eq("is_main", 1);
		qw.eq("tenant_id", user.getTenantId());
		if (StringUtils.isEmpty(airBusiness.getMainOrderNo())) return new ArrayList<>();
		return labelAndValue(airBusinessService, qw, "mainOrderNo", "mainOrderNo");
	}


	/**
	 * 获取空运港口数据
	 */
	@GetMapping("/airportOne")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "获取空运港口数据", notes = "")
	public Object getAirportOne() {
		QueryWrapper<Air> qw = new QueryWrapper<>();
		//qw.like("air_code", air.getAirCode());
		return labelAndValue(airService, qw, "airCode", "airCode");
	}

	/**
	 * 获取空运港口数据
	 */
	@GetMapping("/seaportOne")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取海运1港口数据", notes = "")
	public Object getSeaportOne() {
		QueryWrapper<ShippingPort> qw = new QueryWrapper<>();
		return labelAndValue(shippingPortService, qw, "portCode", "portCode");
	}


	/**
	 * 获取包装数据
	 */
	@GetMapping("/package")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "获取包装数据", notes = "")
	public Object getPackage() {
		return labelAndValue(packtypeService, "packReferred", "packReferred");
	}

	/**
	 * 获取租户所有用户
	 */
	@GetMapping("/tenant/alluser")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "租户所有用户数据", notes = "")
	public Object getTenantAllUser() {
		return labelAndValue(userService, "realName", "id");
	}

	/**
	 * 获取租户作业项目名
	 */
	@GetMapping("taskname")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取租户作业项目名", notes = "")
	public Object getTenantTaskName() {
		return labelAndValue(taskNameService, "name", "name");
	}

	/**
	 * 获取承运人 航空公司
	 */
	@GetMapping("aircompany")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取承运人 航空公司", notes = "")
	public Object getAirCompany() {
		QueryWrapper<ClientData> qw = new QueryWrapper<>();
		BladeUser user = getUser();
		qw.eq("tenant_id", user.getTenantId()).like("company_type", CompanyTypeEnum.AIR_COMPANY);
		return labelAndValue(clientDataService, qw, "fullName", "id");
	}

	/**
	 * 获取航司代理
	 */
	@GetMapping("aircompanyagent")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取承运人 航空公司", notes = "")
	public Object getAirCompanyAgent() {
		QueryWrapper<ClientData> qw = new QueryWrapper<>();
		BladeUser user = getUser();
		qw.eq("tenant_id", user.getTenantId()).like("company_type", CompanyTypeEnum.AIRLINECOMPANY_AGENT);
		return labelAndValue(clientDataService, qw, "fullName", "id");
	}
}
