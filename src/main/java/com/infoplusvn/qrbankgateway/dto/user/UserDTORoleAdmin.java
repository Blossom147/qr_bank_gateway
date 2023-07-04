package com.infoplusvn.qrbankgateway.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTORoleAdmin {

    private Long id;

    private String username;

    private boolean enabled;

    private LocalDateTime createOn;

    private String roles;

    private String password;

    private String email;

    private String phone;

    private String company;

    private String address;

    private String firstName;

    private String lastName;

    private Long userId;

}
