package com.nieghborapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

 @Data    @AllArgsConstructor
public class EmailDto {
    private final String toAddress;
    private final String senderAddress;
    private final String subject ;
    private final String content ;


}
