package com.service.impl;

import com.dao.BrowseRecordDao;
import com.entity.BrowseRecord;
import com.service.BrowseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrowseRecordServiceImpl implements BrowseRecordService {

    @Autowired
    private BrowseRecordDao browseRecordDao;

    @Override
    public void saveBrowseRecord(BrowseRecord browseRecord) {
        browseRecordDao.insert(browseRecord);
    }

    @Override
    public List<Long> getProductIdsByUserId(String userId) {
        return browseRecordDao.getProductIdsByUserId(userId);
    }
}    