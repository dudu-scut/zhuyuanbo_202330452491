package com.entity;


import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.util.Date;

@TableName("browse_record")
public class BrowseRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long goodsId;
    private Long userId;
    private Long stayDuration;
    private Date browseTime;

    // 无参构造函数
    public BrowseRecord() {
    }

    // 有参构造函数
    public BrowseRecord(Long goodsId, Long userId, Long stayDuration, Date browseTime) {
        this.goodsId = goodsId;
        this.userId = userId;
        this.stayDuration = stayDuration;
        this.browseTime = browseTime;
    }

    // Getters 和 Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getStayDuration() {
        return stayDuration;
    }

    public void setStayDuration(Long stayDuration) {
        this.stayDuration = stayDuration;
    }

    public Date getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(Date browseTime) {
        this.browseTime = browseTime;
    }
}