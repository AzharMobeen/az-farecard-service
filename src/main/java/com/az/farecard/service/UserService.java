package com.az.farecard.service;

import com.az.farecard.entity.Role;
import com.az.farecard.entity.User;

import java.util.List;

/**
 * @author Azhar Mobeen
 * @since 8/5/2022
 */
public interface UserService {

    Role saveUserRole(Role role);
    User saveUser(User user);
    List<User> fetchAllUser();
    boolean assignUserRole(String username, String roleName);
    User fetchUser(String username);
}
