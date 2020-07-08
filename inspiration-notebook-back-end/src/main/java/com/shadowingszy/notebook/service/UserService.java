package com.shadowingszy.notebook.service;

import com.shadowingszy.notebook.entity.User;
import com.shadowingszy.notebook.mapper.UserMapper;
import com.shadowingszy.tools.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {
    UserMapper userMapper;

    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ResponseMessage login(String username, String password, String userType) {
        ArrayList<User> queryUserArrayList = userMapper.queryUserByUserInformation(username, password, userType);
        if (queryUserArrayList.size() == 1) {
            return new ResponseMessage("200", "登录成功", queryUserArrayList.get(0));
        } else {
            return new ResponseMessage("1000", "登录失败，请检查用户名和密码是否正确", "登录失败，请检查用户名和密码是否正确");
        }
    }

    public ResponseMessage register(String username, String password, String userType) {
        User registerUser = new User(0, username, password, userType);
        int userCount = userMapper.queryUserCountByUsername(username);
        if (userCount == 0) {
            int registerStatus = userMapper.insertUser(registerUser);
            if (registerStatus > 0) {
                ArrayList<User> queryUserArrayList = userMapper.queryUserByUserInformation(username, password, userType);
                return new ResponseMessage("200", "注册成功", queryUserArrayList.get(0));
            } else {
                return new ResponseMessage("1001", "注册失败", "注册失败");
            }
        } else {
            return new ResponseMessage("1002", "注册失败，用户名重复", "注册失败，用户名重复");
        }
    }

    public ResponseMessage updateUser(int userId, String username, String password) {
        int userUpdateStatus = userMapper.updateUser(userId, username, password);
        if (userUpdateStatus > 0) {
            ArrayList<User> queryUserArrayList = userMapper.queryUserByUserId(userId);
            if (queryUserArrayList.size() == 1) {
                return new ResponseMessage("200", "修改成功", queryUserArrayList.get(0));
            } else {
                return new ResponseMessage("1009", "修改失败", "修改失败");
            }
        } else {
            return new ResponseMessage("1008", "修改失败", "修改失败");
        }
    }
}
