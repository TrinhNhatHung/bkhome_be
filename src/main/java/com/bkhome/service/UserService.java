package com.bkhome.service;

import com.bkhome.dao.UserDao;
import com.bkhome.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService extends EntityService<User>{

    @Autowired
    private UserDao userDao;

    public Map<String, Object> getUserById(String userId) {
        User user = userDao.getById(User.class, userId);
        if (user == null){
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("fullname", user.getFullname());
        map.put("phone", user.getPhone());
        map.put("avatar", user.getAvatar());
        return map;
    }

    public boolean isExist(String userId){
        User user = userDao.getById(User.class, userId);
        return user != null;
    }

    public boolean checkLogin (User requestUser){
        User user = userDao.getById(User.class,requestUser.getId());
        if (user == null){
            return false;
        }

        return user.getPassword().equals(requestUser.getPassword());
    }

    public void addUser(User user) {
        userDao.insertOrUpdate(user);
    }
}
