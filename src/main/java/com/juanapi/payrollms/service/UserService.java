package com.juanapi.payrollms.service;


import com.juanapi.payrollms.model.User;
import com.juanapi.payrollms.model.Role;
import com.juanapi.payrollms.repository.RoleRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Set;
import com.juanapi.payrollms.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
    }
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) .collect(Collectors.toList());
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username).orElse(null);
    }

    public User createUser(String username, String rawPassword, Set<String> roleNames) {
        if (userRepo.findByUsername(username).isPresent()) {
            return userRepo.findByUsername(username).get();
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        if (roleNames != null) {
            Set<Role> roles = roleNames.stream()
                .map(name -> roleRepo.findByName(name).orElseGet(() -> {
                    Role r = new Role();
                    r.setName(name);
                    return roleRepo.save(r);
                }))
                .collect(java.util.stream.Collectors.toSet());
            user.setRoles(roles);
        }
        return userRepo.save(user);
    }


}
