package com.chad3x4.myproject.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.chad3x4.myproject.model.CustomUserDetails;
import com.chad3x4.myproject.model.User;
import com.chad3x4.myproject.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<User> userOptional = repository.findUserByUsername(username);

        return userOptional.map(u -> new CustomUserDetails(u.getUsername(), u.getPassword(), u.getRoles()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));
    }
    
}
