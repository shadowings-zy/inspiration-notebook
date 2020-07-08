package com.shadowingszy.notebook.controller;

import com.shadowingszy.notebook.service.UserService;
import com.shadowingszy.tools.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(allowCredentials = "true", origins = {"*", "null"})
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String userLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("userType") String userType
    ) {
        ResponseMessage response = userService.login(username, password, userType);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String userRegister(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("userType") String userType
    ) {
        ResponseMessage response = userService.register(username, password, userType);
        return ResponseMessage.toJSONString(response);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String userRegister(
            @RequestParam("userId") int userId,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        ResponseMessage response = userService.updateUser(userId, username, password);
        return ResponseMessage.toJSONString(response);
    }
}
