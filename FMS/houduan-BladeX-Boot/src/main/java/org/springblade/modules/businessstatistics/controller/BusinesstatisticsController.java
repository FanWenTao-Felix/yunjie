package org.springblade.modules.businessstatistics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.clientManagement.service.IClientDataService;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.modules.businessstatistics.entity.Businessstatistics;
import org.springblade.modules.businessstatistics.vo.BusinessstatisticsVO;
import org.springblade.modules.businessstatistics.wrapper.BusinessstatisticsWrapper;
import org.springblade.modules.businessstatistics.service.IBusinessstatisticsService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


/**
 *  控制器
 *
 * @author BladeX
 * @since 2019-09-23
 */
@RestController
@AllArgsConstructor
@RequestMapping("statistics/bushinesstatistics")
@Api(value = "", tags = "接口")
public class BusinesstatisticsController extends BladeController {

	private IBusinessstatisticsService businessstatisticsService;
	private IClientDataService iClientDataService;
	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入bushinesstatistics")
	public R<BusinessstatisticsVO> detail(Businessstatistics businessstatistics) {
		Businessstatistics detail = businessstatisticsService.getOne(Condition.getQueryWrapper(businessstatistics));
		return R.data(BusinessstatisticsWrapper.build().entityVO(detail));
	}
	/**
	 * 海陆分页
	 */
	@GetMapping("/airlist")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入bushinesstatistics")
	public R<IPage<BusinessstatisticsVO>> airlist(@RequestParam Map<String,Object> sales, Query query) {
		LocalDateTime begindate=null;
		LocalDateTime enddate=null;
		String client=null;
		String type=null;
		QueryWrapper<Businessstatistics> queryWrapper;
		if(businessstatisticsService.keygetValue(sales,"begindate").equals("NaN-NaN-NaN NaN:NaN:NaN")){
			System.out.println("执行了为空的");
			sales.keySet().removeIf(key -> key.equals("begindate"));
			sales.keySet().removeIf(key -> key.equals("enddate"));
			sales.keySet().removeIf(key -> key.equals("num"));
			sales.keySet().removeIf(key -> key.equals("client"));
			queryWrapper = Condition.getQueryWrapper(sales, Businessstatistics.class);
		}else {
			System.out.println("执行了不为空的");
			List<LocalDateTime> localDateTimes=businessstatisticsService.arrayToString(sales);
			for(int i=0;i<=1;i++){if(i==0){begindate=localDateTimes.get(0);}else {enddate=localDateTimes.get(1);}}
			client=businessstatisticsService.getclient(sales);
			type=businessstatisticsService.gettype(sales);
			sales.keySet().removeIf(key -> key.equals("begindate"));
			sales.keySet().removeIf(key -> key.equals("enddate"));
			sales.keySet().removeIf(key -> key.equals("num"));
			sales.keySet().removeIf(key -> key.equals("client"));
			queryWrapper = Condition.getQueryWrapper(sales, Businessstatistics.class);
			queryWrapper.between("create_time", begindate, enddate);
			if(client==null){}else {queryWrapper.eq("client",client);}
			if(type==null){}else {queryWrapper.eq("tran_type",type);}

		}
		IPage<Businessstatistics> pages = businessstatisticsService.page(Condition.getPage(query), queryWrapper);
		return R.data(BusinessstatisticsWrapper.build().pageVO(pages));
	}

	/**
	 * 每个客户详情查询
	 */
	@GetMapping("/getListdetail")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "分页", notes = "传入bushinesstatistics")
	public R<IPage<BusinessstatisticsVO>> getListdetail(@RequestParam String num, Query query) {
		QueryWrapper<Businessstatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("client",num);
		IPage<Businessstatistics> pages = businessstatisticsService.page(Condition.getPage(query), queryWrapper);
		return R.data(BusinessstatisticsWrapper.build().pageVO(pages));
	}

	/**
	 * 客户分页
	 */
	/*
	* BUG：时间条件搜索
	* */
	@GetMapping("/clientlist")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入bushinesstatistics")
	public R<IPage<BusinessstatisticsVO>> clientlist(@RequestParam Map<String,Object> sales,
													 Query query,
													 BusinessstatisticsVO businessstatisticsVO) {
		LocalDateTime begindate=null;
		LocalDateTime enddate=null;
		QueryWrapper<Businessstatistics> queryWrapper;
		if(businessstatisticsService.keygetValue(sales,"begindate").equals("NaN-NaN-NaN NaN:NaN:NaN")){
			System.out.println("执行了为空的");
			sales.keySet().removeIf(key -> key.equals("begindate"));
			sales.keySet().removeIf(key -> key.equals("enddate"));
			sales.keySet().removeIf(key -> key.equals("num"));
			sales.keySet().removeIf(key -> key.equals("client"));
			queryWrapper = Condition.getQueryWrapper(sales, Businessstatistics.class);
		}else {
			System.out.println("执行了不为空的");
			List<LocalDateTime> localDateTimes=businessstatisticsService.arrayToString(sales);
			for(int i=0;i<=1;i++){if(i==0){begindate=localDateTimes.get(0);}else {enddate=localDateTimes.get(1);}}
			/*sales.keySet().removeIf(key -> key.equals("begindate"));
			sales.keySet().removeIf(key -> key.equals("enddate"));
			sales.keySet().removeIf(key -> key.equals("num"));
			sales.keySet().removeIf(key -> key.equals("client"));*/

			//queryWrapper = Condition.getQueryWrapper(sales, Businessstatistics.class);
			queryWrapper=new QueryWrapper<>();
			queryWrapper.between("create_time", begindate, enddate);

		}
		IPage<BusinessstatisticsVO> pages = businessstatisticsService.selectSalesmanagementPage(Condition.getPage(query), queryWrapper);
		return R.data(pages);
	}

	/**
	 * 每个客户详情查询
	 */
	@GetMapping("/clientdetail")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "分页", notes = "传入bushinesstatistics")
	public R<IPage<BusinessstatisticsVO>> clientdetail(@RequestParam String num, Query query) {
		QueryWrapper<Businessstatistics> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("client",num);
		IPage<Businessstatistics> pages = businessstatisticsService.page(Condition.getPage(query), queryWrapper);
		return R.data(BusinessstatisticsWrapper.build().pageVO(pages));
	}



	/**
	 * 业绩分页
	 */
	@GetMapping("/resultsbusiness")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "分页", notes = "传入bushinesstatistics")
	public R<IPage<BusinessstatisticsVO>> resultsbusiness(@RequestParam Map<String,Object> sales, Query query) {
		LocalDateTime begindate=null;
		LocalDateTime enddate=null;
		QueryWrapper<Businessstatistics> queryWrapper;
		if(businessstatisticsService.keygetValue(sales,"begindate").equals("NaN-NaN-NaN NaN:NaN:NaN")){
			System.out.println("执行了为空的");
			sales.keySet().removeIf(key -> key.equals("begindate"));
			sales.keySet().removeIf(key -> key.equals("enddate"));
			sales.keySet().removeIf(key -> key.equals("num"));
			sales.keySet().removeIf(key -> key.equals("client"));
			queryWrapper = Condition.getQueryWrapper(sales, Businessstatistics.class);
			//queryWrapper.lambda().eq(Businessstatistics::getOrdertype, "0");
		}else {
			System.out.println("执行了不为空的");
			List<LocalDateTime> localDateTimes=businessstatisticsService.arrayToString(sales);
			for(int i=0;i<=1;i++){if(i==0){begindate=localDateTimes.get(0);}else {enddate=localDateTimes.get(1);}}
			sales.keySet().removeIf(key -> key.equals("begindate"));
			sales.keySet().removeIf(key -> key.equals("enddate"));
			sales.keySet().removeIf(key -> key.equals("num"));
			sales.keySet().removeIf(key -> key.equals("client"));
			queryWrapper = Condition.getQueryWrapper(sales, Businessstatistics.class);
			//queryWrapper.lambda().eq(Businessstatistics::getOrdertype, "0");
			queryWrapper.between("create_time", begindate, enddate);
		}
		IPage<Businessstatistics> pages = businessstatisticsService.page(Condition.getPage(query), queryWrapper);
		return R.data(BusinessstatisticsWrapper.build().pageVO(pages));
	}







	/**
	 * 获得总票数
	 */
	@GetMapping("/getallpiao")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "分页", notes = "")
	public R selectBydistinct() {
		int all=businessstatisticsService.list().size();
		return R.data(all);
	}

	/**
	 * 根据用户获得票数
	 */
	@GetMapping("/clientgetnum")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "分页", notes = "")
	public R clientgetnum() {
		List<Businessstatistics> list=businessstatisticsService.list();
		return R.data(list);
	}




















	/**
	 * 新增
	 */
	@PostMapping("/save")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "新增", notes = "传入bushinesstatistics")
	public R save(@Valid @RequestBody Businessstatistics businessstatistics) {
		return R.status(businessstatisticsService.save(businessstatistics));
	}
	/**
	 * 修改
	 */
	@PostMapping("/update")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "修改", notes = "传入bushinesstatistics")
	public R update(@Valid @RequestBody Businessstatistics businessstatistics) {
		return R.status(businessstatisticsService.updateById(businessstatistics));
	}
	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "新增或修改", notes = "传入bushinesstatistics")
	public R submit(@Valid @RequestBody Businessstatistics businessstatistics) {
		return R.status(businessstatisticsService.saveOrUpdate(businessstatistics));
	}
	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(businessstatisticsService.removeByIds(Func.toLongList(ids)));
	}

}
