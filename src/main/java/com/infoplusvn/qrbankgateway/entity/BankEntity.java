package com.infoplusvn.qrbankgateway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "BANK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "bin")
    private String bin;

    @Column(name = "short_name")
    private String shortName;

    private String logo = "https://api.vietqr.io/img/ICB.png";


}
