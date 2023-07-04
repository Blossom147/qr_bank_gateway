package com.infoplusvn.qrbankgateway.converter;

import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import org.modelmapper.AbstractConverter;
import org.springframework.stereotype.Component;
import javax.persistence.Tuple;
import org.springframework.core.convert.converter.Converter;
import java.time.LocalDateTime;

@Component
public class UserDTORoleAdminConverter extends AbstractConverter<Tuple, UserDTORoleAdmin> {

    @Override
    public UserDTORoleAdmin convert(Tuple tuple) {
        UserDTORoleAdmin userDTO = new UserDTORoleAdmin();
        userDTO.setId(tuple.get("id", Long.class));
        userDTO.setUsername(tuple.get("username", String.class));
        userDTO.setEnabled(tuple.get("enabled", Boolean.class));
        userDTO.setCreateOn(tuple.get("createOn", LocalDateTime.class));
        userDTO.setRoles(tuple.get("roles", String.class));
        userDTO.setPassword(tuple.get("password", String.class));
        userDTO.setEmail(tuple.get("email", String.class));
        userDTO.setPhone(tuple.get("phone", String.class));
        userDTO.setCompany(tuple.get("company", String.class));
        userDTO.setAddress(tuple.get("address", String.class));
        userDTO.setFirstName(tuple.get("firstName", String.class));
        userDTO.setLastName(tuple.get("lastName", String.class));
        userDTO.setUserId(tuple.get("userId", Long.class));

        return userDTO;
    }
}

