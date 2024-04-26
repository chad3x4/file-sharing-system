package com.chad3x4.myproject.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chad3x4.myproject.dto.UserDTO;
import com.chad3x4.myproject.model.AuthRequest;
import com.chad3x4.myproject.model.User;
import com.chad3x4.myproject.service.AuthenticationService;
import com.chad3x4.myproject.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome page without login";
    }

    @GetMapping("/user/welcome-after-login")
    public String welcomeBack() {
        return "Welcome back";
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@Valid @RequestBody AuthRequest request) {
        String jwtToken = authenService.login(request);

        return ResponseEntity.ok().body(jwtToken);
    }

    @PostMapping("/auth/signup")
    public ResponseEntity<UserDTO> signup(@Valid @RequestBody User newUser) {
        UserDTO newUserDto = userService.addUser(newUser);

        return new ResponseEntity<UserDTO>(newUserDto, HttpStatus.CREATED);
    }

    // CRUD for ADMIN role
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> retrieveAllUsers() {
        List<UserDTO> users = userService.findAll();

        return ResponseEntity.ok().body(users);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserDTO> getUser(@PathVariable(name = "username") String username) {
        UserDTO user = userService.findUser(username);

        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserDTO> addUser(@Valid @RequestBody User newUser) throws URISyntaxException {
        UserDTO newUserDto = userService.addUser(newUser);
        
        return new ResponseEntity<UserDTO>(newUserDto, HttpStatus.CREATED);
    }
    
    @PutMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateUser(@RequestBody UserDTO updatedUser, @PathVariable(name = "username") String currentUsername) {        
        userService.saveUser(updatedUser, currentUsername);
        
        return ResponseEntity.ok().body(updatedUser);
    }
    
    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "username") String username) {
        userService.deleteUserByUsername(username);

        return ResponseEntity.ok().body(new HashMap<>().put("message", "Deleted user: " + username));
    }
}
