package com.chad3x4.myproject.service;

import java.io.InputStream;
import java.util.Date;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.multipart.MultipartFile;

import com.chad3x4.myproject.dto.FileInfoDTO;
import com.chad3x4.myproject.mapper.FileMapper;
import com.chad3x4.myproject.model.FileInfo;
import com.chad3x4.myproject.repository.FileInfoRepository;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;

@Service
public class FileInfoService {

    @Value("${minio.bucketName}")
    private String bucketName;

    @Value("${minio.endpoint}")
    private String minioUrl;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileInfoRepository fileRepository;

    @Autowired
    private AuthenticationService authService;

    public void listAllBuckets() {
        try {
            minioClient.listBuckets().stream().forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public FileInfoDTO uploadFile(MultipartFile file) {
        String author = authService.getCurrentUsername();
        
        String fileName = generateFileName(file);

        if (fileRepository.existsByFileName(fileName)) {
            throw new TransactionSystemException("Filename is existed");
        }

        String filePath = minioUrl + "/" + bucketName + "/" + fileName;
        FileInfoDTO fileInfoDto = new FileInfoDTO();

        fileInfoDto.setAuthor(author);
        fileInfoDto.setFileName(fileName);
        fileInfoDto.setFilePath(filePath);

        FileInfo uploadedFile = fileMapper.toFileInfo(fileInfoDto);

        fileRepository.save(uploadedFile);

        try (InputStream is = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName).stream(is, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
        } catch (Exception e) {
            fileRepository.deleteById(uploadedFile.getFileId());
            throw new RuntimeException("Failed to store image file.", e);
        }

        return fileInfoDto;
    }

    public InputStream getFilfe(String fileName) throws Exception{
        try {
            InputStream is = minioClient.getObject(
                                GetObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .build());
            return is;
        } catch (Exception ex) {
            throw new RuntimeException("Get file failed!!!");
        }
    }

    public boolean deleteFile(String fileName) {
        if (fileRepository.existsByFileName(fileName)) {
            FileInfo fileInfo = fileRepository.findByFileName(fileName);
            String author = fileInfo.getAuthor().getUsername();

            if (author.equals(authService.getCurrentUsername())) {
                try {
                    minioClient.removeObject(
                    RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());

                    fileRepository.delete(fileInfo);

                    return true;
                } catch (Exception ex) {
                    return false;
                }
            } else {
                throw new AccessDeniedException("Don't have permission");
            }
        } else {
            return false; 
        }
    }

    private String generateFileName(MultipartFile file) {
        return new Date().getTime() + "-" + Objects.requireNonNull(file.getOriginalFilename()).replace(" ", "_");
    }
    
}
