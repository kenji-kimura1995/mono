package com.example.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UserService {

    @Transactional
    public void create(User user) {
        user.persist();
    }

    public List<User> listAll() {
        return User.listAll();
    }
}
