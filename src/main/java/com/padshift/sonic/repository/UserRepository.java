package com.padshift.sonic.repository;

import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by ruzieljonm on 28/06/2018.
 */

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User,Long>  {
    User findByUserNameAndUserPass(String userName, String userPass);

    User findByUserName(String userName);

    User findByUserId(int userid);


}