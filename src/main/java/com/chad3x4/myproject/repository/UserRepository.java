package com.chad3x4.myproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chad3x4.myproject.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findUserByUsername(String username);
    public void deleteUserByUsername(String username);
    public boolean existsByUsername(String username);

}
