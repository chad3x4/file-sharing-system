package com.chad3x4.myproject.dto;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileInfoDTO {
    @NotBlank(message = "File name is mandatory")
    private String fileName;
    
    private String filePath;

    @NotBlank(message = "Author is mandatory")
    private String author;

    @CreationTimestamp
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}
