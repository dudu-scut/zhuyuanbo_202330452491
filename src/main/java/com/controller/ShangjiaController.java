package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.OperationLog; // 确保导入注解

import com.entity.ShangjiaEntity;
import com.entity.view.ShangjiaView;

import com.service.ShangjiaService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MPUtil;


/**
 * 商家
 * 后端接口
 * @author
 * @email
 */
@RestController
@RequestMapping("/shangjia")
public class ShangjiaController {
	@Autowired
	private ShangjiaService shangjiaService;
	@Autowired
	private TokenService tokenService;

	/**
	 * 商家登录
	 */
	@IgnoreAuth
	@RequestMapping(value = "/login")
	@OperationLog(value = "商家登录")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		ShangjiaEntity user = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", username));
		if (user == null || !user.getMima().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(user.getId(), username, "shangjia", "商家");
		return R.ok().put("token", token);
	}

	/**
	 * 商家注册
	 */
	@IgnoreAuth
	@RequestMapping("/register")
	@OperationLog(value = "商家注册")
	public R register(@RequestBody ShangjiaEntity shangjia) {
		ShangjiaEntity user = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", shangjia.getZhanghao()));
		if (user != null) {
			return R.error("注册用户已存在");
		}
		Long uId = new Date().getTime();
		shangjia.setId(uId);
		shangjiaService.insert(shangjia);
		return R.ok();
	}

	/**
	 * 商家退出
	 */
	@RequestMapping("/logout")
	@OperationLog(value = "商家退出")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}

	/**
	 * 获取当前商家信息
	 */
	@RequestMapping("/session")
	@OperationLog(value = "获取当前商家信息")
	public R getCurrUser(HttpServletRequest request) {
		Long id = (Long) request.getSession().getAttribute("userId");
		ShangjiaEntity user = shangjiaService.selectById(id);
		return R.ok().put("data", user);
	}

	/**
	 * 重置商家密码
	 */
	@IgnoreAuth
	@RequestMapping(value = "/resetPass")
	@OperationLog(value = "重置商家密码")
	public R resetPass(String username, HttpServletRequest request) {
		ShangjiaEntity user = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", username));
		if (user == null) {
			return R.error("账号不存在");
		}
		user.setMima("123456");
		shangjiaService.updateById(user);
		return R.ok("密码已重置为：123456");
	}

	/**
	 * 后端列表（商家管理）
	 */
	@RequestMapping("/page")
	@OperationLog(value = "获取商家后端分页列表")
	public R page(@RequestParam Map<String, Object> params, ShangjiaEntity shangjia, HttpServletRequest request) {
		EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
		PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
		return R.ok().put("data", page);
	}

	/**
	 * 前端列表（商家查询）
	 */
	@RequestMapping("/list")
	@OperationLog(value = "获取商家前端列表")
	public R list(@RequestParam Map<String, Object> params, ShangjiaEntity shangjia, HttpServletRequest request) {
		EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
		PageUtils page = shangjiaService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangjia), params), params));
		return R.ok().put("data", page);
	}

	/**
	 * 精准条件列表查询
	 */
	@RequestMapping("/lists")
	@OperationLog(value = "获取商家精准条件列表")
	public R list(ShangjiaEntity shangjia) {
		EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
		ew.allEq(MPUtil.allEQMapPre(shangjia, "shangjia"));
		return R.ok().put("data", shangjiaService.selectListView(ew));
	}

	/**
	 * 条件查询商家信息
	 */
	@RequestMapping("/query")
	@OperationLog(value = "条件查询商家信息")
	public R query(ShangjiaEntity shangjia) {
		EntityWrapper<ShangjiaEntity> ew = new EntityWrapper<ShangjiaEntity>();
		ew.allEq(MPUtil.allEQMapPre(shangjia, "shangjia"));
		ShangjiaView shangjiaView = shangjiaService.selectView(ew);
		return R.ok("查询商家成功").put("data", shangjiaView);
	}

	/**
	 * 后端详情（管理员查看）
	 */
	@RequestMapping("/info/{id}")
	@OperationLog(value = "获取商家后端详情")
	public R info(@PathVariable("id") Long id) {
		ShangjiaEntity shangjia = shangjiaService.selectById(id);
		return R.ok().put("data", shangjia);
	}

	/**
	 * 前端详情（商家自查）
	 */
	@RequestMapping("/detail/{id}")
	@OperationLog(value = "获取商家前端详情")
	public R detail(@PathVariable("id") Long id) {
		ShangjiaEntity shangjia = shangjiaService.selectById(id);
		return R.ok().put("data", shangjia);
	}

	/**
	 * 后端保存（新增商家）
	 */
	@RequestMapping("/save")
	@OperationLog(value = "保存商家信息（后端新增）")
	public R save(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request) {
		shangjia.setId(new Date().getTime());
		ShangjiaEntity user = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", shangjia.getZhanghao()));
		if (user != null) {
			return R.error("用户已存在");
		}
		shangjiaService.insert(shangjia);
		return R.ok();
	}

	/**
	 * 前端保存（商家注册）
	 */
	@RequestMapping("/add")
	@OperationLog(value = "新增商家信息（前端注册）")
	public R add(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request) {
		shangjia.setId(new Date().getTime());
		ShangjiaEntity user = shangjiaService.selectOne(new EntityWrapper<ShangjiaEntity>().eq("zhanghao", shangjia.getZhanghao()));
		if (user != null) {
			return R.error("用户已存在");
		}
		shangjiaService.insert(shangjia);
		return R.ok();
	}

	/**
	 * 修改商家信息
	 */
	@RequestMapping("/update")
	@OperationLog(value = "修改商家信息")
	public R update(@RequestBody ShangjiaEntity shangjia, HttpServletRequest request) {
		shangjiaService.updateById(shangjia);
		return R.ok();
	}

	/**
	 * 批量删除商家
	 */
	@RequestMapping("/delete")
	@OperationLog(value = "批量删除商家信息")
	public R delete(@RequestBody Long[] ids) {
		shangjiaService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}

	/**
	 * 提醒接口（商家信息提醒统计）
	 */
	@RequestMapping("/remind/{columnName}/{type}")
	@OperationLog(value = "获取商家提醒数量")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
						 @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		if (type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			// 日期处理逻辑...
		}
		Wrapper<ShangjiaEntity> wrapper = new EntityWrapper<ShangjiaEntity>();
		// 条件组装...
		int count = shangjiaService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
}