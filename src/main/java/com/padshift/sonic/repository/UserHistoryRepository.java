package com.padshift.sonic.repository;

import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ruzieljonm on 23/07/2018.
 */
@Repository("userHistoryRepository")
public interface UserHistoryRepository extends JpaRepository<UserHistory,Long> {
    List<UserHistory> findByuserId(int userid);


    List<UserHistory> findAllByUserNameAndVideoid(String userName, String videoid);
}
