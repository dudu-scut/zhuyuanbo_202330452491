package com.dao;

import com.entity.BrowseRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BrowseRecordDao {
    void insert(BrowseRecord browseRecord);

    List<Long> getProductIdsByUserId(String userId);
}