package com.padshift.sonic.service;

import com.padshift.sonic.entities.Criteria;
import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by ruzieljonm on 28/06/2018.
 */
@Service
public interface UserService {

    void saveUser(User user);





    void saveUserPreference(UserPreference userpref);



    UserPreference findUserPreferenceByUserId(int userId);

    void saveUserHistory(UserHistory userhist);

    User findByUsername(String username);


    User findByUsernameAndPassword(String userName, String userPass);


    User findByUserId(int userid);

    ArrayList<UserPreference> findAllGenrePreferenceByUserId(int userid);

    UserPreference findUserPreferenceByUserIdAndGenreId(int userId, int i);

    void saveCriteria(Criteria criteria);

    ArrayList<Criteria> findAllCriteria();


    void deleteCriteriaByCriteriaId(int deletethis);

    Criteria findCriteriaByCriteriaName(String userinput);

    Criteria findCriteriaByCriteriaId(int editthis);

    ArrayList<String> findDistinctUser(String currentuserId);

    ArrayList<User> findAll();

    String findUserByUserId(String userId);

    String findCurrentByUserId(String currentuserId);
}
