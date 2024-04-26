package com.chad3x4.myproject.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chad3x4.myproject.dto.UserDTO;
import com.chad3x4.myproject.model.User;
import com.chad3x4.myproject.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public List<UserDTO> findAll() {
        List<User> result = repository.findAll();
        return result
                .stream()
                .map(user -> mapper.map(user, UserDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findUser(String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) || 
                username.equals(authentication.getName())) {

            User user = repository.findUserByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            UserDTO userDto = mapper.map(user, UserDTO.class);
                    
            return userDto;
        } else throw new AccessDeniedException("Don't have permission");
    }

    @Override
    public UserDTO addUser(User newUser) {
        if (!repository.existsByUsername(newUser.getUsername())) {
            newUser.setPassword(encoder.encode(newUser.getPassword()));
            newUser.setRoles("USER");
            repository.save(newUser);

            return mapper.map(newUser, UserDTO.class);
        } else {
            throw new DataIntegrityViolationException("Username existed");
        }
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO updatedUser, String currentUsername) {
        if (repository.existsByUsername(updatedUser.getUsername()))
            throw new DataIntegrityViolationException("Username existed");
        User existingUser = repository
                            .findUserByUsername(currentUsername)
                            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + currentUsername));

        mapper.map(updatedUser, existingUser);

        User result = repository.save(existingUser);

        return mapper.map(result, UserDTO.class);
    }

    @Override
    @Transactional
    public void deleteUserByUsername(String username) {
        if (repository.existsByUsername(username)) {
            repository.deleteUserByUsername(username);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}
