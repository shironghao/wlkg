package com.wlkg.user.controller;

import com.wlkg.user.dto.UserDto;
import com.wlkg.user.pojo.User;
import com.wlkg.user.pojo.User_Info;
import com.wlkg.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type) {




        return ResponseEntity.ok(this.userService.checkData(data, type));
    }

    //发送手机验证码
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(String phone){
        Boolean boo = userService.sendVerifyCode(phone);
        return ResponseEntity.ok(null);
    }



    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Boolean> register(@Valid User user, @RequestParam("code") String code) {
        Boolean flag = this.userService.register(user, code);
        return ResponseEntity.ok(flag);
    }


    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        User user = this.userService.queryUser(username, password);
        return ResponseEntity.ok(user);
    }


    //个人信息回显
    @GetMapping("/select")
    public ResponseEntity<User_Info> selectUser(@RequestParam("id") Integer userId){
        User_Info userInfo= userService.selectUser(userId);
        return ResponseEntity.ok(userInfo);
    }

    //修改
    @PutMapping("/update")
    public ResponseEntity<Void> update( UserDto userDto){
        System.out.println(userDto);
        userService.update(userDto);
        return ResponseEntity.ok(null);
    }



}
