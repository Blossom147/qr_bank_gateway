package com.infoplusvn.qrbankgateway.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "COUNTRY")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, name = "country_code")
    private String countryCode;

    @Column(name = "country_name")
    private String countryName;
}
