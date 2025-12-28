package com.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import com.entity.ShangjiaEntity;
import com.service.ShangjiaService;
import com.service.ShangpinxinxiService;
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

import com.entity.OrdersEntity;
import com.entity.view.OrdersView;

import com.service.OrdersService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 订单
 * 后端接口
 * @author
 * @email
 */
@RestController
@RequestMapping("/orders")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ShangpinxinxiService shangpinxinxiService;
    @Autowired
    private ShangjiaService shangjiaService;


    /**
     * 后端列表（支持管理员/商家/用户过滤）
     */
    @RequestMapping("/page")
    @OperationLog(value = "获取订单后端分页列表")
    public R page(@RequestParam Map<String, Object> params, OrdersEntity orders,
                  HttpServletRequest request) {
        if (!request.getSession().getAttribute("role").toString().equals("管理员") &&
                !request.getSession().getAttribute("role").toString().equals("商家")) {
            orders.setUserid((Long) request.getSession().getAttribute("userId"));
        }

        if (request.getSession().getAttribute("role").toString().equals("商家")) {
            ShangjiaEntity shangjiaEntity = shangjiaService.selectById((Long) request.getSession().getAttribute("userId"));
            orders.setZhanghao(shangjiaEntity.getZhanghao());
        }

        EntityWrapper<OrdersEntity> ew = new EntityWrapper<OrdersEntity>();
        PageUtils page = ordersService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, orders), params), params));

        return R.ok().put("data", page);
    }

    /**
     * 前端列表（普通用户访问）
     */
    @RequestMapping("/list")
    @OperationLog(value = "获取订单前端列表")
    public R list(@RequestParam Map<String, Object> params, OrdersEntity orders, HttpServletRequest request) {
        EntityWrapper<OrdersEntity> ew = new EntityWrapper<OrdersEntity>();
        PageUtils page = ordersService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, orders), params), params));
        return R.ok().put("data", page);
    }

    /**
     * 精准条件列表查询
     */
    @RequestMapping("/lists")
    @OperationLog(value = "获取订单精准条件列表")
    public R list(OrdersEntity orders) {
        EntityWrapper<OrdersEntity> ew = new EntityWrapper<OrdersEntity>();
        ew.allEq(MPUtil.allEQMapPre(orders, "orders"));
        return R.ok().put("data", ordersService.selectListView(ew));
    }

    /**
     * 条件查询订单
     */
    @RequestMapping("/query")
    @OperationLog(value = "条件查询订单信息")
    public R query(OrdersEntity orders) {
        EntityWrapper<OrdersEntity> ew = new EntityWrapper<OrdersEntity>();
        ew.allEq(MPUtil.allEQMapPre(orders, "orders"));
        OrdersView ordersView = ordersService.selectView(ew);
        return R.ok("查询订单成功").put("data", ordersView);
    }

    /**
     * 后端详情（管理员/商家查看）
     */
    @RequestMapping("/info/{id}")
    @OperationLog(value = "获取订单后端详情")
    public R info(@PathVariable("id") Long id) {
        OrdersEntity orders = ordersService.selectById(id);
        return R.ok().put("data", orders);
    }

    /**
     * 前端详情（普通用户查看）
     */
    @RequestMapping("/detail/{id}")
    @OperationLog(value = "获取订单前端详情")
    public R detail(@PathVariable("id") Long id) {
        OrdersEntity orders = ordersService.selectById(id);
        return R.ok().put("data", orders);
    }




    /**
     * 后端保存（用户下单）
     */
    @RequestMapping("/save")
    @OperationLog(value = "保存订单后端信息（用户下单）")
    public R save(@RequestBody OrdersEntity orders, HttpServletRequest request) {
        orders.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        orders.setUserid((Long) request.getSession().getAttribute("userId"));
        ordersService.insert(orders);
        return R.ok();
    }

    /**
     * 前端保存（用户下单）
     */
    @RequestMapping("/add")
    @OperationLog(value = "保存订单前端信息（用户下单）")
    public R add(@RequestBody OrdersEntity orders, HttpServletRequest request) {
        orders.setId(new Date().getTime() + new Double(Math.floor(Math.random()*1000)).longValue());
        ordersService.insert(orders);
        return R.ok();
    }

    /**
     * 修改订单状态/信息
     */
    @RequestMapping("/update")
    @OperationLog(value = "修改订单信息")
    public R update(@RequestBody OrdersEntity orders, HttpServletRequest request) {
        ordersService.updateById(orders);
        return R.ok();
    }


    /**
     * 批量删除订单
     */
    @RequestMapping("/delete")
    @OperationLog(value = "批量删除订单")
    public R delete(@RequestBody Long[] ids) {
        ordersService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 提醒接口（订单状态提醒统计）
     */
    @RequestMapping("/remind/{columnName}/{type}")
    @OperationLog(value = "获取订单提醒数量")
    public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request,
                         @PathVariable("type") String type, @RequestParam Map<String, Object> map) {
        map.put("column", columnName);
        map.put("type", type);

        if (type.equals("2")) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            // 日期处理逻辑...
        }

        Wrapper<OrdersEntity> wrapper = new EntityWrapper<OrdersEntity>();
        // 权限过滤和条件组装...

        int count = ordersService.selectCount(wrapper);
        return R.ok().put("count", count);
    }
}