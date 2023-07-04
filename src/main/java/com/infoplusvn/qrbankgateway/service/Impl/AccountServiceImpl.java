package com.infoplusvn.qrbankgateway.service.Impl;

import com.infoplusvn.qrbankgateway.constant.CommonConstant;
import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.dto.user.EditAccountDTO;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORegisterRequest;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleUser;
import com.infoplusvn.qrbankgateway.entity.AccountEntity;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import com.infoplusvn.qrbankgateway.repo.AccountRepo;
import com.infoplusvn.qrbankgateway.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountServiceImpl {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public AccountEntity createAccount(UserDTORegisterRequest userRequest) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setUsername(userRequest.getUsername());
        accountEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        accountEntity.setEnabled(true);
        accountEntity.setCreateOn(LocalDateTime.now());
        accountEntity.setRoles(CommonConstant.ROLE_USER);

        return accountRepo.save(accountEntity);
    }

    public AccountEntity deactiveUser(UserDTORoleAdmin userRequest) {

        AccountEntity accountEntity = accountRepo.findByUsername(userRequest.getUsername());
//        user.setEnabled(false);
        return accountRepo.save(accountEntity);
    }

    public Long getUserIdByUsername(String username){
        return accountRepo.findUserIdByUsername(username);
    }

    public UserAccountInfo findUserAccountInfo(String userName){
        return accountRepo.findUserAccountInfo(userName);
    }

    public String findRolesByUserName(String username){
        return accountRepo.findRolesByUserName(username);
    }


    public AccountEntity findAccountByUsername(String username){
        return accountRepo.findByUsername(username);
    }

    public AccountEntity roleAdminUpdateUser(EditAccountDTO userDTO){

        AccountEntity accountEntity = accountRepo.findByUsername(userDTO.getUsername());

        Long user_id = accountRepo.findUserIdByUsername(userDTO.getUsername());
        accountEntity.setUsername(userDTO.getUsername());
        accountEntity.setEnabled(userDTO.isEnabled());
        accountEntity.setRoles(userDTO.getRoles());

        accountRepo.save(accountEntity);

        UserEntity userEntity = userRepo.findOneById(user_id);
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPhone(userDTO.getPhone());
        userRepo.save(userEntity);

        return accountEntity;

    }

    public AccountEntity roleUserUpdateUser(UserDTORoleUser userDTO){

        AccountEntity accountEntity = accountRepo.findByUsername(userDTO.getUsername());

        Long user_id = accountRepo.findUserIdByUsername(userDTO.getUsername());

        UserEntity userEntity = userRepo.findOneById(user_id);
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPhone(userDTO.getPhone());
        userEntity.setFirstName(userDTO.getFirstName());
        userEntity.setLastName(userDTO.getLastName());
        userEntity.setAddress(userDTO.getAddress());
        userEntity.setCompany(userDTO.getCompany());
        userRepo.save(userEntity);

        return accountEntity;

    }

    public UserDTORoleAdmin findDTOEditAccount(String username) {
        return accountRepo.findDTOEditAccount(username);
    }

    public List<UserDTORoleAdmin> findAllAccount(){
        return accountRepo.findAllAccount();
    }

    public AccountEntity getUserByUserName(String username) {
        return accountRepo.findByUsername(username);
    }

    @Transactional
    public void deleteAccount(String username, Long userId){
        accountRepo.deleteByUsername(username);
        userRepo.deleteById(userId);
    }
}
