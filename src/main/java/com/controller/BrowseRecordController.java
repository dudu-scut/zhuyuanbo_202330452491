package com.controller;

import com.entity.BrowseRecord;
import com.service.BrowseRecordService;
import com.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/browseRecord")
public class BrowseRecordController {

    @Autowired
    private BrowseRecordService browseRecordService;

    @PostMapping("/save")
    public R saveBrowseRecord(@RequestBody BrowseRecord browseRecord) {
        try {
            browseRecord.setBrowseTime(new Date());
            browseRecordService.saveBrowseRecord(browseRecord);
            return R.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return R.error();
        }
    }
}    