package com.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.annotation.IgnoreAuth;
import com.annotation.OperationLog; // 确保导入正确的注解
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.entity.ConfigEntity;
import com.service.ConfigService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.ValidatorUtils;

@RequestMapping("config")
@RestController
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 后端分页列表
     */
    @RequestMapping("/page")
    @OperationLog(value = "获取配置信息分页列表")
    public R page(@RequestParam Map<String, Object> params, ConfigEntity config) {
        EntityWrapper<ConfigEntity> ew = new EntityWrapper<ConfigEntity>();
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 前端列表（免认证）
     */
    @IgnoreAuth
    @RequestMapping("/list")
    @OperationLog(value = "获取配置信息列表")
    public R list(@RequestParam Map<String, Object> params, ConfigEntity config) {
        EntityWrapper<ConfigEntity> ew = new EntityWrapper<ConfigEntity>();
        PageUtils page = configService.queryPage(params);
        return R.ok().put("data", page);
    }

    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    @OperationLog(value = "获取配置信息详情")
    public R info(@PathVariable("id") String id) {
        ConfigEntity config = configService.selectById(id);
        return R.ok().put("data", config);
    }

    /**
     * 前端详情（免认证）
     */
    @IgnoreAuth
    @RequestMapping("/detail/{id}")
    @OperationLog(value = "获取配置信息详情")
    public R detail(@PathVariable("id") String id) {
        ConfigEntity config = configService.selectById(id);
        return R.ok().put("data", config);
    }

    /**
     * 根据名称获取信息
     */
    @RequestMapping("/info")
    @OperationLog(value = "根据名称获取配置信息")
    public R infoByName(@RequestParam String name) {
        ConfigEntity config = configService.selectOne(new EntityWrapper<ConfigEntity>().eq("name", "faceFile"));
        return R.ok().put("data", config);
    }

    /**
     * 保存配置
     */
    @PostMapping("/save")
    @OperationLog(value = "保存配置信息")
    public R save(@RequestBody ConfigEntity config) {
//    	ValidatorUtils.validateEntity(config);
        configService.insert(config);
        return R.ok();
    }

    /**
     * 修改配置
     */
    @RequestMapping("/update")
    @OperationLog(value = "修改配置信息")
    public R update(@RequestBody ConfigEntity config) {
//        ValidatorUtils.validateEntity(config);
        configService.updateById(config);//全部更新
        return R.ok();
    }

    /**
     * 删除配置
     */
    @RequestMapping("/delete")
    @OperationLog(value = "删除配置信息")
    public R delete(@RequestBody Long[] ids) {
        configService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
}