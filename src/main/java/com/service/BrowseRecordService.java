package com.service;

import com.entity.BrowseRecord;

import java.util.List;

public interface BrowseRecordService {
    void saveBrowseRecord(BrowseRecord browseRecord);

    List<Long> getProductIdsByUserId(String userId);
}