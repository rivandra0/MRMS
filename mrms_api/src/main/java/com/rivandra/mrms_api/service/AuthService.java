package com.rivandra.mrms_api.service;

import model.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rivandra.mrms_api.mapper.UserMapper;

@Service
public class AuthService {
    @Autowired
    private UserMapper _userMapper;


    public User login(String userId, String password) {
        
        User usr = _userMapper.getOneWithPassword(userId, password);

        if(usr == null) {
            throw new RuntimeException("user not found");
        }
        return usr;

    }

    public User getUser(String userId) {
       return _userMapper.getOneWithId(userId);

    }
}
