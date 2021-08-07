package com.abl.mydata.auth.service;

import com.abl.mydata.auth.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// 비즈니스 로직
@Service
public class UserDaoService {

    private static List<User> users = new ArrayList<>();

    static {
        users.add(new User(1, "Kimee", new Date(), "pass1", "701010-1111111"));
        users.add(new User(2, "Lee", new Date(), "pass2", "991211-2104593"));
        users.add(new User(3, "Jun", new Date(), "pass3", "980323-1032442"));
    }

    public List<User> findAll() {
        return users;
    }

    public User findOne(int id) {

        for (User user: users) {
            if(user.getId() == id)
                return user;
        }

        return null;
    }

    public User save(User user) {

        if(user.getId() == null) {
            user.setId(users.size() + 1);
        }

        users.add(user);
        return user;
    }

    public User update(User user) {

        User upUser = users.stream().filter(u -> u.getId().equals(user.getId())).findFirst().orElse(null);

        if(upUser == null) {
            return null;
        }

        upUser.setName(user.getName());
        upUser.setJoinDate(user.getJoinDate());
        return upUser;
    }

    public boolean deleteById(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

}

