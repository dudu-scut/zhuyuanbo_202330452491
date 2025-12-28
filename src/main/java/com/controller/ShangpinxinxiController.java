package com.controller;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;

import com.annotation.OperationLog;
import com.service.*;
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

import com.entity.ShangpinxinxiEntity;
import com.entity.view.ShangpinxinxiView;

import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 商品信息
 * 后端接口
 * @author 
 * @email
 */
@RestController
@RequestMapping("/shangpinxinxi")
public class ShangpinxinxiController {
    @Autowired
    private ShangpinxinxiService shangpinxinxiService;
    @Autowired
    private CartService cartService;
    @Autowired
    private BrowseRecordService browseRecordService;
    @Autowired
    private OrdersService ordersService;

    /**
     * 后端列表
     */
    @RequestMapping("/page")
    @OperationLog(value = "获取商品后端分页列表")
    public R page(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shangjia")) {
			shangpinxinxi.setZhanghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
		PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @OperationLog(value = "获取商品前端端分页列表")
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
		PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));
        List<ShangpinxinxiEntity> productList = (List<ShangpinxinxiEntity>) page.getList();
        String userId = request.getHeader("userId");

        // 获取用户偏好信息
        List<Long> cartProductIds = cartService.getProductIdsByUserId(userId);
        List<Long> orderProductIds = ordersService.getProductIdsByUserId(userId);
        List<Long> browseProductIds = browseRecordService.getProductIdsByUserId(userId);

        // 合并用户偏好的商品 ID
        Set<Long> preferredProductIds = new HashSet<>();
        preferredProductIds.addAll(cartProductIds);
        preferredProductIds.addAll(orderProductIds);
        preferredProductIds.addAll(browseProductIds);

        // 提取用户偏好商品的类别
        Set<String> preferredCategories = new HashSet<>();
        for (Long productId : preferredProductIds) {
            ShangpinxinxiEntity product = shangpinxinxiService.selectById(productId);
            if (product != null) {
                preferredCategories.add(product.getLeibie());
            }
        }

        // 对商品列表进行排序
        List<ShangpinxinxiEntity> sortedProductList = new ArrayList<>();
        // 先添加偏好类别商品
        for (String category : preferredCategories) {
            for (ShangpinxinxiEntity product : productList) {
                if (category.equals(product.getLeibie())) {
                    sortedProductList.add(product);
                }
            }
        }
        // 再添加其他商品
        for (ShangpinxinxiEntity product : productList) {
            if (!preferredCategories.contains(product.getLeibie())) {
                sortedProductList.add(product);
            }
        }
        // 更新分页结果
        page.setList(sortedProductList);
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    @OperationLog(value = "获取商品后端列表")
    public R list( ShangpinxinxiEntity shangpinxinxi){
       	EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( shangpinxinxi, "shangpinxinxi")); 
        return R.ok().put("data", shangpinxinxiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    @OperationLog(value = "查询商品")
    public R query(ShangpinxinxiEntity shangpinxinxi){
        EntityWrapper< ShangpinxinxiEntity> ew = new EntityWrapper< ShangpinxinxiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( shangpinxinxi, "shangpinxinxi")); 
		ShangpinxinxiView shangpinxinxiView =  shangpinxinxiService.selectView(ew);
		return R.ok("查询商品信息成功").put("data", shangpinxinxiView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    @OperationLog(value = "查询商品详情")
    public R info(@PathVariable("id") Long id){
        ShangpinxinxiEntity shangpinxinxi = shangpinxinxiService.selectById(id);
		shangpinxinxi.setClicknum(shangpinxinxi.getClicknum()+1);
		shangpinxinxi.setClicktime(new Date());
		shangpinxinxiService.updateById(shangpinxinxi);
        return R.ok().put("data", shangpinxinxi);
    }

    /**
     * 前端详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    @OperationLog(value = "查询商品详情")
    public R detail(@PathVariable("id") Long id){
        ShangpinxinxiEntity shangpinxinxi = shangpinxinxiService.selectById(id);
		shangpinxinxi.setClicknum(shangpinxinxi.getClicknum()+1);
		shangpinxinxi.setClicktime(new Date());
		shangpinxinxiService.updateById(shangpinxinxi);
        return R.ok().put("data", shangpinxinxi);
    }
    


    /**
     * 赞或踩
     */
    @RequestMapping("/thumbsup/{id}")
    @OperationLog(value = "赞或踩")
    public R vote(@PathVariable("id") String id,String type){
        ShangpinxinxiEntity shangpinxinxi = shangpinxinxiService.selectById(id);
        if(type.equals("1")) {
        	shangpinxinxi.setThumbsupnum(shangpinxinxi.getThumbsupnum()+1);
        } else {
        	shangpinxinxi.setCrazilynum(shangpinxinxi.getCrazilynum()+1);
        }
        shangpinxinxiService.updateById(shangpinxinxi);
        return R.ok("投票成功");
    }

    /**
     * 后端保存
     */
    @RequestMapping("/save")
    @OperationLog(value = "新增商品")
    public R save(@RequestBody ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
    	shangpinxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangpinxinxi);
        shangpinxinxiService.insert(shangpinxinxi);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    @OperationLog(value = "新增商品")
    public R add(@RequestBody ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
    	shangpinxinxi.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(shangpinxinxi);
        shangpinxinxiService.insert(shangpinxinxi);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @OperationLog(value = "修改商品")
    public R update(@RequestBody ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request){
        //ValidatorUtils.validateEntity(shangpinxinxi);
        shangpinxinxiService.updateById(shangpinxinxi);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @OperationLog(value = "删除商品")
    public R delete(@RequestBody Long[] ids){
        shangpinxinxiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
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
		
		Wrapper<ShangpinxinxiEntity> wrapper = new EntityWrapper<ShangpinxinxiEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("shangjia")) {
			wrapper.eq("zhanghao", (String)request.getSession().getAttribute("username"));
		}

		int count = shangpinxinxiService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	
	/**
     * 前端智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,ShangpinxinxiEntity shangpinxinxi, HttpServletRequest request,String pre){
        EntityWrapper<ShangpinxinxiEntity> ew = new EntityWrapper<ShangpinxinxiEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            String newKey = entry.getKey();
            if (pre.endsWith(".")) {
                newMap.put(pre + newKey, entry.getValue());
            } else if (StringUtils.isEmpty(pre)) {
                newMap.put(newKey, entry.getValue());
            } else {
                newMap.put(pre + "." + newKey, entry.getValue());
            }
        }
		params.put("sort", "clicknum");
        params.put("order", "desc");
		PageUtils page = shangpinxinxiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, shangpinxinxi), params), params));
        return R.ok().put("data", page);
    }



}
