package com.padshift.sonic.repository;

import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by ruzieljonm on 23/07/2018.
 */
public interface UserHistoryRepository extends JpaRepository<UserHistory,Long> {
}
