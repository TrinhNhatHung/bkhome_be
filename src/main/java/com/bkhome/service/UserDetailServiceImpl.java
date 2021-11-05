package com.bkhome.service;

import com.bkhome.dao.UserDao;
import com.bkhome.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {
        User user = userDao.getById(User.class, uid);
        if (user == null) {
            return null;
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getId(), user.getPassword(), roles);
    }
}
