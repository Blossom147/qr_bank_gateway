package com.infoplusvn.qrbankgateway.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserDto {
    private String username;
    private boolean enabled;
    private LocalDateTime createOn;
    private String roles;
    private String email;

    // Constructors, getters, and setters
}
