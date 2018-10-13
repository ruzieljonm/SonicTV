package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.Criteria;
import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
import com.padshift.sonic.repository.CriteriaRepository;
import com.padshift.sonic.repository.UserHistoryRepository;
import com.padshift.sonic.repository.UserPreferenceRepository;
import com.padshift.sonic.repository.UserRepository;
import com.padshift.sonic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Created by ruzieljonm on 27/06/2018.
 */
@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public UserHistoryRepository userHistoryRepository;

    @Autowired
    CriteriaRepository criteriaRepository;


    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }




    @Override
    public void saveUserPreference(UserPreference userpref) {
        userPreferenceRepository.save(userpref);
    }

    @Override
    public UserPreference findUserPreferenceByUserId(int userId) {
        return userPreferenceRepository.findByUserId(userId);
    }

    @Override
    public void saveUserHistory(UserHistory userhist) {
        userHistoryRepository.save(userhist);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    @Override
    public User findByUsernameAndPassword(String userName, String userPass) {
        return userRepository.findByUserNameAndUserPass(userName, userPass);
    }

    @Override
    public User findByUserId(int userid) {
        return userRepository.findByUserId(userid);
    }

    @Override
    public ArrayList<UserPreference> findAllGenrePreferenceByUserId(int userid) {
        return userPreferenceRepository.findAllByUserId(userid);
    }

    @Override
    public UserPreference findUserPreferenceByUserIdAndGenreId(int userId, int i) {
        return userPreferenceRepository.findByUserIdAndGenreId(userId,i);
    }

    @Override
    public void saveCriteria(Criteria criteria) {
        criteriaRepository.save(criteria);
    }

    @Override
    public ArrayList<Criteria> findAllCriteria() {
        return (ArrayList<Criteria>) criteriaRepository.findAll();
    }

    @Override
    public void deleteCriteriaByCriteriaId(int deletethis) {
        criteriaRepository.deleteByCriteriaId(deletethis);
        System.out.println("DELETING");
    }

    @Override
    public Criteria findCriteriaByCriteriaName(String userinput) {
        return criteriaRepository.findByCriteriaName(userinput);
    }

    @Override
    public Criteria findCriteriaByCriteriaId(int editthis) {
        return criteriaRepository.findByCriteriaId(editthis);
    }

    @Override
    public ArrayList<String> findDistinctUser(String currentuserId) {
        return userHistoryRepository.findDistinctUser(currentuserId);
    }

    @Override
    public ArrayList<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public String findUserByUserId(String userId) {
        return userHistoryRepository.findUserByUserId(userId);
    }

    @Override
    public String findCurrentByUserId(String currentuserId) {
        return userHistoryRepository.findCurrentByUserId(currentuserId);
    }


}
