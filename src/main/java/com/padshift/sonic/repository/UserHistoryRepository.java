package com.padshift.sonic.repository;

import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
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
    List<UserHistory> findByuserId(String userid);

    List<UserHistory> findAllByUserIdAndVideoid(String userId, String videoid);

    @Query("select distinct videoid from UserHistory")
    ArrayList<String> findDistinctVid();

    @Query("select distinct vidRating from UserHistory where userId = :userid and videoid = :vidid")
    String findRatingByUserIdAndVideoid(@Param("userid") String userId, @Param("vidid") String vidId);

    @Query("select distinct userId from UserHistory where userId <> :currentuser")
    ArrayList<String> findDistinctUser(@Param("currentuser") String currentuserId);

    @Query("select distinct userName from UserHistory where userId = :currentuser")
    String findUserByUserId(@Param("currentuser") String currentuserId);

    @Query("select distinct userId from UserHistory where userId = :currentuserid")
    String findCurrentByUserId(@Param("currentuserid") String currentuserId);
}