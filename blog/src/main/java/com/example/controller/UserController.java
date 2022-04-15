package com.example.controller;


import com.example.common.lang.Result;
import com.example.entity.User;
import com.example.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @RequiresAuthentication
    @GetMapping("/index")
    public Result index(){
        User user = userService.getById(1L);
        return Result.succ(user);
    }

    @PostMapping("/save")
    public Object testUser(@Validated @RequestBody User user) {
        return user.toString();
    }

}
