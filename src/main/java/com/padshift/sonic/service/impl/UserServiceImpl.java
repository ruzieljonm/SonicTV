package com.padshift.sonic.service.impl;

import com.padshift.sonic.entities.User;
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

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public User findUserByUsernameAndPassword(String userName, String userPass) {
        return userRepository.findByUserNameAndUserPass(userName,userPass);
    }

}
