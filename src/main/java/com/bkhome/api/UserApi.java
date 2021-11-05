package com.bkhome.api;

import com.bkhome.persistence.User;
import com.bkhome.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class UserApi {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/user/info")
    public ResponseEntity<?> getUserInfo() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> result = userService.getUserById(userDetails.getUsername());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@ModelAttribute User user) {
        if (userService.isExist(user.getId())) {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Username đã tồn tại");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
        userService.addUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@ModelAttribute User user) {
        boolean result = userService.checkLogin(user);
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            Map<String, String> map = new HashMap<>();
            map.put("message", "Username hoặc password không đúng");
            return new ResponseEntity<>(map, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/checkLogin")
    public ResponseEntity<?> checkLogin() {
        Map<String, Object> map = new HashMap<>();
        try {
            UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            System.out.println(userDetails.getUsername());
            map.put("isAuthenticated", true);
        } catch (Exception e){
            e.printStackTrace();
            map.put("isAuthenticated", false);
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
