package com.nieghborapp.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Data
public class EmailDto {
    private final String toAddress;
    private final String senderAddress;
    private final String subject ;
    private final String content ;
}
