package com.az.farecard.service.impl;

import com.az.farecard.entity.Role;
import com.az.farecard.entity.User;
import com.az.farecard.repository.RoleRepository;
import com.az.farecard.repository.UserRepository;
import com.az.farecard.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Azhar Mobeen
 * @since 8/5/2022
 */

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public Role saveUserRole(Role role) {
        log.info("saveUserRole called for Role {}", role);
        return roleRepository.save(role);
    }

    @Override
    public User saveUser(User user) {
        log.info("saveUser called for User {}", user);
        return userRepository.save(user);
    }

    @Override
    public List<User> fetchAllUser() {
        log.info("Fetch all user");
        return userRepository.findAll();
    }

    @Override
    public boolean assignUserRole(String username, String roleName) {
        log.info("Assigning Role {}, to User {}", roleName, username);
        Role role = roleRepository.findByName(roleName);
        User user = userRepository.findByUsername(username);
        user.getRoles().add(role);
        return false;
    }

    @Override
    public User fetchUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}