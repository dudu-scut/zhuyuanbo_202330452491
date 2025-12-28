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

import com.entity.ShangpinfenleiEntity;
import com.entity.view.ShangpinfenleiView;

import com.service.ShangpinfenleiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 商品分类
 * 后端接口
 * @author
 * @email
 */
@RestController
@RequestMapping("/shangpinfenlei")
public class ShangpinfenleiController {
    @Autowired
    private ShangpinfenleiService shangpinfenleiService;


    /**
     * 后端分页列表
     */
    @RequestMapping("/page")
    @OperationLog(value = "获取商品分类后端分页列表")
    public R page(@RequestParam Map<String, Object> params, ShangpinfenleiEntity shangpinfenlei,
                  HttpServletRequest request) {
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        PageUtils page = shangpinfenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinfenlei), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 前端列表
     */
    @RequestMapping("/list")
    @OperationLog(value = "获取商品分类前端列表")
    public R list(@RequestParam Map<String, Object> params, ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request) {
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        PageUtils page = shangpinfenleiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinfenlei), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 精准条件列表查询
     */
    @RequestMapping("/lists")
    @OperationLog(value = "获取商品分类精准条件列表")
    public R list(ShangpinfenleiEntity shangpinfenlei) {
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.allEq(MPUtil.allEQMapPre(shangpinfenlei, "shangpinfenlei"));
        return R.ok().put("data", shangpinfenleiService.selectListView(ew));
    }

    /**
     * 条件查询商品分类
     */
    @RequestMapping("/query")
    @OperationLog(value = "条件查询商品分类信息")
    public R query(ShangpinfenleiEntity shangpinfenlei) {
        EntityWrapper<ShangpinfenleiEntity> ew = new EntityWrapper<ShangpinfenleiEntity>();
        ew.allEq(MPUtil.allEQMapPre(shangpinfenlei, "shangpinfenlei"));
        ShangpinfenleiView shangpinfenleiView = shangpinfenleiService.selectView(ew);
        return R.ok("查询商品分类成功").put("data", shangpinfenleiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    @OperationLog(value = "获取商品分类后端详情")
    public R info(@PathVariable("id") Long id) {
        ShangpinfenleiEntity shangpinfenlei = shangpinfenleiService.selectById(id);
        return R.ok().put("data", shangpinfenlei);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    @OperationLog(value = "获取商品分类前端详情")
    public R detail(@PathVariable("id") Long id) {
        ShangpinfenleiEntity shangpinfenlei = shangpinfenleiService.selectById(id);
        return R.ok().put("data", shangpinfenlei);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @OperationLog(value = "保存商品分类后端信息")
    public R save(@RequestBody ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request) {
        shangpinfenlei.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        shangpinfenleiService.insert(shangpinfenlei);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @OperationLog(value = "保存商品分类前端信息")
    public R add(@RequestBody ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request) {
        shangpinfenlei.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        shangpinfenleiService.insert(shangpinfenlei);
        return R.ok();
    }

    /**
     * 修改商品分类
     */
    @RequestMapping("/update")
    @OperationLog(value = "修改商品分类信息")
    public R update(@RequestBody ShangpinfenleiEntity shangpinfenlei, HttpServletRequest request) {
        shangpinfenleiService.updateById(shangpinfenlei);
        return R.ok();
    }


    /**
     * 批量删除商品分类
     */
    @RequestMapping("/delete")
    @OperationLog(value = "批量删除商品分类信息")
    public R delete(@RequestBody Long[] ids) {
        shangpinfenleiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口（分类信息提醒统计）
     */
    @RequestMapping("/remind/{columnName}/{type}")
    @OperationLog(value = "获取商品分类提醒数量")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
                         @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if(type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            Date remindStartDate = null;
            Date remindEndDate = null;
            if(map.get("remindstart")!=null) {
                Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH,remindStart);
                remindStartDate = c.getTime();
                map.put("remindstart", sdf.format(remindStartDate));
            }
            if(map.get("remindend")!=null) {
                Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
                c.setTime(new Date());
                c.add(Calendar.DAY_OF_MONTH,remindEnd);
                remindEndDate = c.getTime();
                map.put("remindend", sdf.format(remindEndDate));
            }
        }

        Wrapper<ShangpinfenleiEntity> wrapper = new EntityWrapper<ShangpinfenleiEntity>();
        if(map.get("remindstart")!=null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if(map.get("remindend")!=null) {
            wrapper.le(columnName, map.get("remindend"));
        }


        int count = shangpinfenleiService.selectCount(wrapper);
        return R.ok().put("count", count);
    }
}