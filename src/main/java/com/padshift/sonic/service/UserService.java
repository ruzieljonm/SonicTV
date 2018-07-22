package com.padshift.sonic.service;

import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
import org.springframework.stereotype.Service;

/**
 * Created by ruzieljonm on 28/06/2018.
 */
@Service
public interface UserService {

    void saveUser(User user);
    User findUserByUsernameAndPassword(String userName, String userPass);




    void saveUserPreference(UserPreference userpref);



    UserPreference findUserPreferenceByUserId(int userId);

    void saveUserHistory(UserHistory userhist);

    User findByUsername(String username);

    ;
}
