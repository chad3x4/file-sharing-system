package com.chad3x4.myproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.chad3x4.myproject.model.Authority;
import java.util.List;


public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    public List<Authority> findByUserId(long userId);
    public List<Authority> findByFileId(long fileId);
}
