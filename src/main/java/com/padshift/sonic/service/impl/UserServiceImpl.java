package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.UserHistory;
import com.padshift.sonic.entities.UserPreference;
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
    public ArrayList<UserPreference> findAllByUserId(int userId) {
        return userPreferenceRepository.findAllByUserId(userId);
    }


}
