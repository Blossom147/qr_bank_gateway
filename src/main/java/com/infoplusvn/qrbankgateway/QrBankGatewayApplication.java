package com.infoplusvn.qrbankgateway;

import com.infoplusvn.qrbankgateway.converter.UserDTORoleAdminConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class QrBankGatewayApplication {

    @Autowired
    private UserDTORoleAdminConverter userDTORoleAdminConverter;

    @Bean
    public ModelMapper customModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(userDTORoleAdminConverter);
        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(QrBankGatewayApplication.class, args);
    }
}