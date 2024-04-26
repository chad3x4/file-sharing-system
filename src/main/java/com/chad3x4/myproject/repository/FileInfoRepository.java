package com.chad3x4.myproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chad3x4.myproject.model.FileInfo;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long>{
    public List<FileInfo> findByAuthorUserId(long authorId);
    public FileInfo findByFileName(String fileName);
    public boolean existsByFileName(String fileName);
}
