package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.User;
import com.padshift.sonic.entities.UserPreference;
import com.padshift.sonic.repository.UserPreferenceRepository;
import com.padshift.sonic.repository.UserRepository;
import com.padshift.sonic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ruzieljonm on 27/06/2018.
 */
@Service("userService")
public class UserServiceImpl implements UserService {


    @Autowired
    public UserRepository userRepository;

    @Autowired
    public UserPreferenceRepository userPreferenceRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByUsernameAndPassword(String userName, String userPass) {
        return userRepository.findByUserNameAndUserPass(userName,userPass);
    }

    @Override
    public User findUserByUsername(String userName) {
        return userRepository.findByUserName(userName);
    }

    @Override
    public void saveUserPreference(UserPreference userpref) {
        userPreferenceRepository.save(userpref);
    }


}
