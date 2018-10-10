package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.repository.UserHistoryRepository;
import com.padshift.sonic.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by ruzieljonm on 10/10/2018.
 */
@Service("adminService")
public class AdminServiceImpl implements AdminService{

    @Autowired
    UserHistoryRepository userHistoryRepository;

    @Override
    public ArrayList<String> findDistinctSequenceId() {
        return userHistoryRepository.findDistinctSequenceId();
    }

    @Override
    public ArrayList<UserHistory> findAllBySeqid(String s) {
        return userHistoryRepository.findBySeqid(s);
    }
}
