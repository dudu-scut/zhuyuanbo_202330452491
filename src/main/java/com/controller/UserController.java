package com.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.TokenEntity;
import com.entity.UserEntity;
import com.service.TokenService;
import com.service.UserService;
import com.utils.CommonUtil;
import com.utils.MPUtil;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

/**
 * 登录相关
 */
@RequestMapping("users")
@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	/**
	 * 登录
	 */
	@IgnoreAuth
	@PostMapping(value = "/login")
	@OperationLog(value = "用户登录")
	public R login(String username, String password, String captcha, HttpServletRequest request) {
		UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));
		if(user==null || !user.getPassword().equals(password)) {
			return R.error("账号或密码不正确");
		}
		String token = tokenService.generateToken(user.getId(), username, "users", user.getRole());
		return R.ok().put("token", token);
	}

	/**
	 * 注册
	 */
	@IgnoreAuth
	@PostMapping(value = "/register")
	@OperationLog(value = "用户注册")
	public R register(@RequestBody UserEntity user) {
//    	ValidatorUtils.validateEntity(user);
		if(userService.selectOne(new EntityWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
			return R.error("用户已存在");
		}
		userService.insert(user);
		return R.ok();
	}

	/**
	 * 退出
	 */
	@GetMapping(value = "logout")
	@OperationLog(value = "用户退出")
	public R logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return R.ok("退出成功");
	}

	/**
	 * 密码重置
	 */
	@IgnoreAuth
	@RequestMapping(value = "/resetPass")
	@OperationLog(value = "重置用户密码")
	public R resetPass(String username, HttpServletRequest request) {
		UserEntity user = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", username));
		if(user==null) {
			return R.error("账号不存在");
		}
		user.setPassword("123456");
		userService.update(user, null);
		return R.ok("密码已重置为：123456");
	}

	/**
	 * 列表（分页）
	 */
	@RequestMapping("/page")
	@OperationLog(value = "获取用户分页列表")
	public R page(@RequestParam Map<String, Object> params, UserEntity user) {
		EntityWrapper<UserEntity> ew = new EntityWrapper<UserEntity>();
		PageUtils page = userService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.allLike(ew, user), params), params));
		return R.ok().put("data", page);
	}

	/**
	 * 列表（精准条件）
	 */
	@RequestMapping("/list")
	@OperationLog(value = "获取用户精准条件列表")
	public R list(UserEntity user) {
		EntityWrapper<UserEntity> ew = new EntityWrapper<UserEntity>();
		ew.allEq(MPUtil.allEQMapPre(user, "user"));
		return R.ok().put("data", userService.selectListView(ew));
	}

	/**
	 * 详情
	 */
	@RequestMapping("/info/{id}")
	@OperationLog(value = "获取用户详情")
	public R info(@PathVariable("id") String id) {
		UserEntity user = userService.selectById(id);
		return R.ok().put("data", user);
	}

	/**
	 * 获取当前用户Session信息
	 */
	@RequestMapping("/session")
	@OperationLog(value = "获取当前用户信息")
	public R getCurrUser(HttpServletRequest request) {
		Long id = (Long) request.getSession().getAttribute("userId");
		UserEntity user = userService.selectById(id);
		return R.ok().put("data", user);
	}

	/**
	 * 保存（新增用户）
	 */
	@PostMapping("/save")
	@OperationLog(value = "保存用户信息")
	public R save(@RequestBody UserEntity user) {
//    	ValidatorUtils.validateEntity(user);
		if(userService.selectOne(new EntityWrapper<UserEntity>().eq("username", user.getUsername())) != null) {
			return R.error("用户已存在");
		}
		userService.insert(user);
		return R.ok();
	}

	/**
	 * 修改用户信息
	 */
	@RequestMapping("/update")
	@OperationLog(value = "修改用户信息")
	public R update(@RequestBody UserEntity user) {
//        ValidatorUtils.validateEntity(user);
		UserEntity u = userService.selectOne(new EntityWrapper<UserEntity>().eq("username", user.getUsername()));
		if(u != null && u.getId() != user.getId() && u.getUsername().equals(user.getUsername())) {
			return R.error("用户名已存在。");
		}
		userService.updateById(user);
		return R.ok();
	}

	/**
	 * 删除用户（批量）
	 */
	@RequestMapping("/delete")
	@OperationLog(value = "批量删除用户")
	public R delete(@RequestBody Long[] ids) {
		userService.deleteBatchIds(Arrays.asList(ids));
		return R.ok();
	}
}