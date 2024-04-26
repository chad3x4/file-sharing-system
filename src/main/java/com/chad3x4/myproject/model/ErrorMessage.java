package com.chad3x4.myproject.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {
    private int statusCode;
    private String message;
}

/* 
 * SHOULD USE ENUM
 * Error status code:
 * 1000 Successful
 * 1001 Argument invalid
 * 1002
 * 1003 Credentials invalid
 * 1004 User not found
 * 1005 User existed
 * 1006 Don't have permission
 * 1007 File doesn't exist
 * 1008
 * 1009 Unknown error
 */