package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

    @Transactional

    void deleteById (Long userId);

    UserEntity findByEmail(String email);

    UserEntity findOneById(Long id);

//    UserEntity findEmailByUsername(String username);

//    @Query("SELECT t.roles from UserEntity t where t.username = :username ")
//    String findRolesByUserName(@Param("username") String username);


    @Query("SELECT t.email from UserEntity t where t.email = :email")
    String findMail(@Param("email") String email);


}