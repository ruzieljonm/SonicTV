package com.padshift.sonic.service;

import com.padshift.sonic.entities.UserHistory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by ruzieljonm on 10/10/2018.
 */
@Service
public interface AdminService {
    ArrayList<String> findDistinctSequenceId();

    ArrayList<UserHistory> findAllBySeqid(String s);
}
