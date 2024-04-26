package com.chad3x4.myproject.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chad3x4.myproject.dto.FileInfoDTO;
import com.chad3x4.myproject.model.ErrorMessage;
import com.chad3x4.myproject.service.FileInfoService;

@RestController
@RequestMapping("/v1")
public class FileController {
    @Autowired
    private FileInfoService fileService;
    
    @GetMapping("/file")
    public String listAllBuckets() {
        fileService.listAllBuckets();
        return "All buckets are printed in console";
    }

    @GetMapping("/file/{fileName}")
    public byte[] getFile(@PathVariable(name = "fileName") String fileName) throws Exception{
        InputStream file = fileService.getFilfe(fileName);

        return file.readAllBytes();
    }
    

    @PostMapping("/file")
    public ResponseEntity<FileInfoDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        FileInfoDTO uploadedFileInfo = fileService.uploadFile(file);

        return new ResponseEntity<FileInfoDTO>(uploadedFileInfo, HttpStatus.CREATED);
    }

    @DeleteMapping("/file/{fileName}")
    public ResponseEntity<ErrorMessage> deleteFile(@PathVariable(name = "fileName") String fileName) {
        if (fileService.deleteFile(fileName)) {
            return new ResponseEntity<ErrorMessage>(new ErrorMessage(1000, fileName + " deleted!!!"), HttpStatus.OK);
        } else {
            return new  ResponseEntity<ErrorMessage>(new ErrorMessage(1007, fileName + " not found"), HttpStatus.BAD_REQUEST);
        }
    }
}
