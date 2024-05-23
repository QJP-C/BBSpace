package com.be.bang.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RedisMsgDto {
    private LocalDateTime sendTime;
    private String msg;
}
