package com.padshift.sonic.service;

import com.padshift.sonic.entities.Criteria;
import com.padshift.sonic.entities.Status;
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

    Status[] updateMV();

    void saveGenretoDB();

    void updateTopMusic();

    void updateGenreTags();

    void addCriteria(Criteria criteria);

    void showSeq();
}
