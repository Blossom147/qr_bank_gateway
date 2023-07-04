package com.infoplusvn.qrbankgateway.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class EditAccountDTO {

    private String username;
    private boolean enabled;
    private String phone;
    private String roles;
    private String email;

    // Constructors, getters, and setters
}
