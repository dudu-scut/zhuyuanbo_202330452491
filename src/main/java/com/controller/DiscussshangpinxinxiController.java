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
import com.annotation.OperationLog; // 确保导入注解

import com.entity.DiscussshangpinxinxiEntity;
import com.entity.view.DiscussshangpinxinxiView;

import com.service.DiscussshangpinxinxiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 商品信息评论表
 * 后端接口
 * @author
 * @email
 */
@RestController
@RequestMapping("/discussshangpinxinxi")
public class DiscussshangpinxinxiController {
    @Autowired
    private DiscussshangpinxinxiService discussshangpinxinxiService;


    /**
     * 后端列表（分页）
     */
    @RequestMapping("/page")
    @OperationLog(value = "获取商品评论后端分页列表")
    public R page(@RequestParam Map<String, Object> params, DiscussshangpinxinxiEntity discussshangpinxinxi,
                  HttpServletRequest request) {
        EntityWrapper<DiscussshangpinxinxiEntity> ew = new EntityWrapper<DiscussshangpinxinxiEntity>();
        PageUtils page = discussshangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussshangpinxinxi), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表（免认证）
     */
    @IgnoreAuth
    @RequestMapping("/list")
    @OperationLog(value = "获取商品评论前端列表")
    public R list(@RequestParam Map<String, Object> params, DiscussshangpinxinxiEntity discussshangpinxinxi, HttpServletRequest request) {
        EntityWrapper<DiscussshangpinxinxiEntity> ew = new EntityWrapper<DiscussshangpinxinxiEntity>();
        PageUtils page = discussshangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, discussshangpinxinxi), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 精准条件列表
     */
    @RequestMapping("/lists")
    @OperationLog(value = "获取商品评论精准条件列表")
    public R list(DiscussshangpinxinxiEntity discussshangpinxinxi) {
        EntityWrapper<DiscussshangpinxinxiEntity> ew = new EntityWrapper<DiscussshangpinxinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(discussshangpinxinxi, "discussshangpinxinxi"));
        return R.ok().put("data", discussshangpinxinxiService.selectListView(ew));
    }

    /**
     * 条件查询
     */
    @RequestMapping("/query")
    @OperationLog(value = "条件查询商品评论信息")
    public R query(DiscussshangpinxinxiEntity discussshangpinxinxi) {
        EntityWrapper<DiscussshangpinxinxiEntity> ew = new EntityWrapper<DiscussshangpinxinxiEntity>();
        ew.allEq(MPUtil.allEQMapPre(discussshangpinxinxi, "discussshangpinxinxi"));
        DiscussshangpinxinxiView discussshangpinxinxiView = discussshangpinxinxiService.selectView(ew);
        return R.ok("查询商品信息评论表成功").put("data", discussshangpinxinxiView);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    @OperationLog(value = "获取商品评论后端详情")
    public R info(@PathVariable("id") Long id) {
        DiscussshangpinxinxiEntity discussshangpinxinxi = discussshangpinxinxiService.selectById(id);
        return R.ok().put("data", discussshangpinxinxi);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    @OperationLog(value = "获取商品评论前端详情")
    public R detail(@PathVariable("id") Long id) {
        DiscussshangpinxinxiEntity discussshangpinxinxi = discussshangpinxinxiService.selectById(id);
        return R.ok().put("data", discussshangpinxinxi);
    }




    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @OperationLog(value = "保存商品评论后端信息")
    public R save(@RequestBody DiscussshangpinxinxiEntity discussshangpinxinxi, HttpServletRequest request) {
        discussshangpinxinxi.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        //ValidatorUtils.validateEntity(discussshangpinxinxi);
        discussshangpinxinxiService.insert(discussshangpinxinxi);
        return R.ok();
    }

    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @OperationLog(value = "保存商品评论前端信息")
    public R add(@RequestBody DiscussshangpinxinxiEntity discussshangpinxinxi, HttpServletRequest request) {
        discussshangpinxinxi.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        //ValidatorUtils.validateEntity(discussshangpinxinxi);
        discussshangpinxinxiService.insert(discussshangpinxinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @OperationLog(value = "修改商品评论信息")
    public R update(@RequestBody DiscussshangpinxinxiEntity discussshangpinxinxi, HttpServletRequest request) {
        //ValidatorUtils.validateEntity(discussshangpinxinxi);
        discussshangpinxinxiService.updateById(discussshangpinxinxi);//全部更新
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    @OperationLog(value = "删除商品评论信息")
    public R delete(@RequestBody Long[] ids) {
        discussshangpinxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口（统计提醒数量）
     */
    @RequestMapping("/remind/{columnName}/{type}")
    @OperationLog(value = "获取商品评论提醒数量")
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

        Wrapper<DiscussshangpinxinxiEntity> wrapper = new EntityWrapper<DiscussshangpinxinxiEntity>();
        if(map.get("remindstart")!=null) {
            wrapper.ge(columnName, map.get("remindstart"));
        }
        if(map.get("remindend")!=null) {
            wrapper.le(columnName, map.get("remindend"));
        }


        int count = discussshangpinxinxiService.selectCount(wrapper);
        return R.ok().put("count", count);
    }
}