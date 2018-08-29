package com.padshift.sonic.repository;

import com.padshift.sonic.entities.UserPreference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by ruzieljonm on 19/07/2018.
 */
@Repository("userPreferenceRepository")
public interface UserPreferenceRepository extends JpaRepository<UserPreference,Long> {


    UserPreference findByUserId(int userId);

    ArrayList<UserPreference> findAllByUserId(int userId);
}
