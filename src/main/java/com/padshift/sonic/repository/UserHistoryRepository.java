package com.padshift.sonic.repository;

import com.padshift.sonic.entities.UserHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruzieljonm on 23/07/2018.
 */
@Repository("userHistoryRepository")
public interface UserHistoryRepository extends JpaRepository<UserHistory,Long> {
    List<UserHistory> findByuserId(int userid);

    @Query("select distinct seqid from UserHistory")
    ArrayList<String> findDistinctSequenceId();

    ArrayList<UserHistory> findBySeqid(String s);

    @Query("select distinct seqid from UserHistory where user_id = :uid")
    ArrayList<String> findDistinctSequenceIdByUserid(@Param("uid") int userid);


//    ArrayList<String> findDistinctSeqidByUserid(int i);
}
