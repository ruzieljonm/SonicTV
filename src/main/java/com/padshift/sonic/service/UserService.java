package com.padshift.sonic.service;

import com.padshift.sonic.entities.*;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
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

//    void saveCriteria(Criteria criteria);

    ArrayList<Criteria> findAllCriteria();


    void deleteCriteriaByCriteriaId(int deletethis);

    Criteria findCriteriaByCriteriaName(String userinput);

    Criteria findCriteriaByCriteriaId(int editthis);


    float computeInitialVideoWeight(VideoDetails vid, User user);

    void seqRulMin(String userid, String sessionid);

//    void seqRulMin(String userid, String sessionid);
//
//
//    ArrayList<String> findDistinctSequenceId();
}
