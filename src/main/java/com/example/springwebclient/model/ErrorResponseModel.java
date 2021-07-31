package com.example.springwebclient.model;

import lombok.Data;

@Data
public class ErrorResponseModel {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
