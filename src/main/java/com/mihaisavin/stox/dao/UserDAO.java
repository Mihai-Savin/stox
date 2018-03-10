package com.mihaisavin.stox.dao;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserDAO<T> {
    T update(T model);

    UserDetails findByUsername(String username);

    String getUsersEmail(long userId);
}