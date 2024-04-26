package com.chad3x4.myproject.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.chad3x4.myproject.dto.FileInfoDTO;
import com.chad3x4.myproject.model.FileInfo;
import com.chad3x4.myproject.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class FileMapper {
    @Autowired
    private UserRepository repository;

    public FileInfo toFileInfo(FileInfoDTO fileDto) {
        FileInfo result = new FileInfo();
        String authorName = fileDto.getAuthor();

        result.setFileName(fileDto.getFileName());
        result.setFilePath(fileDto.getFilePath());
        result.setCreatedDate(fileDto.getCreatedDate());
        result.setUpdatedDate(fileDto.getUpdatedDate());
        result.setAuthor(
            repository.findUserByUsername(authorName)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + authorName))
        );
        
        return result;
    }
}
