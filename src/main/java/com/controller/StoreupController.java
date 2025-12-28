package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;
import com.annotation.OperationLog; // 确保导入自定义注解

import com.entity.StoreupEntity;
import com.entity.view.StoreupView;

import com.service.StoreupService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 收藏表
 * 后端接口
 * @author
 * @email
 */
@RestController
@RequestMapping("/storeup")
public class StoreupController {
    @Autowired
    private StoreupService storeupService;


    /**
     * 后端分页列表（管理员/用户视角）
     */
    @RequestMapping("/page")
    @OperationLog(value = "获取收藏后端分页列表")
    public R page(@RequestParam Map<String, Object> params, StoreupEntity storeup,
                  HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            storeup.setUserid((Long) request.getSession().getAttribute("userId"));
        }
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();
        PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, storeup), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表（普通用户收藏列表）
     */
    @RequestMapping("/list")
    @OperationLog(value = "获取收藏前端列表")
    public R list(@RequestParam Map<String, Object> params, StoreupEntity storeup, HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员")) {
            storeup.setUserid((Long) request.getSession().getAttribute("userId"));
        }
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();
        PageUtils page = storeupService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, storeup), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 精准条件列表查询
     */
    @RequestMapping("/lists")
    @OperationLog(value = "获取收藏精准条件列表")
    public R list(StoreupEntity storeup) {
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();
        ew.allEq(MPUtil.allEQMapPre(storeup, "storeup"));
        return R.ok().put("data", storeupService.selectListView(ew));
    }

    /**
     * 条件查询收藏信息
     */
    @RequestMapping("/query")
    @OperationLog(value = "条件查询收藏信息")
    public R query(StoreupEntity storeup) {
        EntityWrapper<StoreupEntity> ew = new EntityWrapper<StoreupEntity>();
        ew.allEq(MPUtil.allEQMapPre(storeup, "storeup"));
        StoreupView storeupView = storeupService.selectView(ew);
        return R.ok("查询收藏表成功").put("data", storeupView);
    }

    /**
     * 后端详情（管理员查看）
     */
    @RequestMapping("/info/{id}")
    @OperationLog(value = "获取收藏后端详情")
    public R info(@PathVariable("id") Long id) {
        StoreupEntity storeup = storeupService.selectById(id);
        return R.ok().put("data", storeup);
    }

    /**
     * 前端详情（用户自查）
     */
    @RequestMapping("/detail/{id}")
    @OperationLog(value = "获取收藏前端详情")
    public R detail(@PathVariable("id") Long id) {
        StoreupEntity storeup = storeupService.selectById(id);
        return R.ok().put("data", storeup);
    }




    /**
     * 后端保存（用户添加收藏）
     */
    @RequestMapping("/save")
    @OperationLog(value = "保存收藏后端信息（用户添加）")
    public R save(@RequestBody StoreupEntity storeup, HttpServletRequest request) {
        storeup.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        storeup.setUserid((Long) request.getSession().getAttribute("userId"));
        storeupService.insert(storeup);
        return R.ok();
    }

    /**
     * 前端保存（用户添加收藏）
     */
    @RequestMapping("/add")
    @OperationLog(value = "保存收藏前端信息（用户添加）")
    public R add(@RequestBody StoreupEntity storeup, HttpServletRequest request) {
        storeup.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        storeup.setUserid((Long) request.getSession().getAttribute("userId"));
        storeupService.insert(storeup);
        return R.ok();
    }

    /**
     * 修改收藏信息
     */
    @RequestMapping("/update")
    @OperationLog(value = "修改收藏信息")
    public R update(@RequestBody StoreupEntity storeup, HttpServletRequest request) {
        storeupService.updateById(storeup);
        return R.ok();
    }


    /**
     * 批量删除收藏
     */
    @RequestMapping("/delete")
    @OperationLog(value = "批量删除收藏信息")
    public R delete(@RequestBody Long[] ids) {
        storeupService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口（收藏状态提醒统计）
     */
    @RequestMapping("/remind/{columnName}/{type}")
    @OperationLog(value = "获取收藏提醒数量")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
                         @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            // 日期处理逻辑...
        }

        Wrapper<StoreupEntity> wrapper = new EntityWrapper<StoreupEntity>();
        // 权限过滤和条件组装...

        int count = storeupService.selectCount(wrapper);
        return R.ok().put("count", count);
    }
}