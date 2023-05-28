package com.infoplusvn.qrbankgateway.service;

import com.infoplusvn.qrbankgateway.constant.CommonConstant;
import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleUser;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORegisterRequest;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import com.infoplusvn.qrbankgateway.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {

        return userRepo.findAll();
    }

    public UserAccountInfo findUserAccountInfo(String userName){
        return userRepo.findUserAccountInfo(userName);
    }

    public UserEntity createUser(UserDTORegisterRequest userRequest) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRequest.getUsername());
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setEnabled(true);
        userEntity.setCreateOn(LocalDateTime.now());
        userEntity.setRoles(CommonConstant.ROLE_USER);

        return userRepo.save(userEntity);
    }


    public UserEntity roleUserUpdateUser(UserDTORoleUser userRequest) {

        UserEntity userEntity = userRepo.findByUsername(userRequest.getUsername());

        userEntity.setUsername(userRequest.getUsername());
        //userEntity.setPassword(userRequest.getPassword());
        userEntity.setEmail(userRequest.getEmail());
        userEntity.setPhone(userRequest.getPhone());
        userEntity.setCompany(userRequest.getCompany());
        userEntity.setAddress(userRequest.getAddress());
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());

        return userRepo.save(userEntity);
    }

    public UserEntity roleAdminUpdateUser(UserDTORoleAdmin userDTO){
        UserEntity userEntity = userRepo.findByUsername(userDTO.getUsername());
//        userEntity.setUsername(userDTO.getUsername());
//        userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setEnabled(userDTO.isEnabled());
        userEntity.setRoles(userDTO.getRoles());

        return userRepo.save(userEntity);

    }

    public UserEntity deactiveUser(UserDTORoleAdmin userRequest) {

        UserEntity user = userRepo.findByUsername(userRequest.getUsername());
        user.setEnabled(false);
        return userRepo.save(user);
    }

    public void  deleteByUserName(String username){
        userRepo.deleteByUsername(username);
    }



    public UserEntity getUserById(long id) {
        return userRepo.findOneById(id);
    }

    public UserEntity getUserByUserName(String username) {
        return userRepo.findByUsername(username);
    }

    public UserEntity getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

//    public UserEntity getEmailByUserName
//    public UserEntity roleAdminUpdateUser(UserDTORoleAdmin userDTO) {
//    }
}
