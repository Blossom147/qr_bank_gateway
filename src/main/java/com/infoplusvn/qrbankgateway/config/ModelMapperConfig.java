package com.infoplusvn.qrbankgateway.config;

import com.infoplusvn.qrbankgateway.converter.UserDTORoleAdminConverter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ModelMapperConfig {

    @Autowired
    private UserDTORoleAdminConverter userDTORoleAdminConverter;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addConverter(userDTORoleAdminConverter);
        return modelMapper;
    }
}
