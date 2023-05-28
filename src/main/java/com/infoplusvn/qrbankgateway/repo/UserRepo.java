package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepo extends JpaRepository<UserEntity, Long> {

//    UserEntity findByUsername(String username);
//    @Query("DELETE from com.infoplusvn.qrbankgateway.entity.UserEntity t where t.username = :username")
    @Transactional
    void deleteByUsername (String username);

    UserEntity findByEmail(String email);

    UserEntity findOneById(Long id);

    UserEntity findEmailByUsername(String username);


    @Query("SELECT t.email from UserEntity t where t.email = :email")
    String findMail(@Param("email") String email);

    @Query("SELECT t from UserEntity t where t.username = :username and t.enabled = true")
    UserEntity findByUsername(@Param("username") String username);


    @Query("SELECT u.firstName AS firstName, u.lastName AS lastName, a.accountNumber AS accountNumber, a.amount AS amount FROM UserEntity u JOIN AccountEntity a ON u.id = a.userId WHERE u.username = :userName")
    UserAccountInfo findUserAccountInfo(@Param("userName") String userName);
}