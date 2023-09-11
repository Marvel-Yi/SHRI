package com.capstone.shri.util;

import com.capstone.shri.entity.User;
import org.springframework.stereotype.Component;

@Component
public class HostHolder {
    private ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public void remove() {
        userThreadLocal.remove();
    }

    public void setUser(User user) {
        userThreadLocal.set(user);
    }

    public User getUser() {
        return userThreadLocal.get();
    }
}
