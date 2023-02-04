package com.nieghborapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.util.Date;
@Data @RequiredArgsConstructor @AllArgsConstructor
public class ErrorResponseDto {
    int httpStatus;

    String message ;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-mm-yyyy hh:mm:ss")
    @LastModifiedDate
    Date  timestamp ;

    public ErrorResponseDto(int value, String message) {
        httpStatus = value ;
        this.message = message;
        this.timestamp = new Date();
    }
}
