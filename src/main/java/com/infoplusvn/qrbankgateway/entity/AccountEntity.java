package com.infoplusvn.qrbankgateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "ACCOUNT")
@AllArgsConstructor
@NoArgsConstructor
public class AccountEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String amount;

    private Long userId;

    private String accountNumber;

    private boolean enabled;

    @Column(name = "create_on")
    private LocalDateTime createOn;

    private String roles;

}
