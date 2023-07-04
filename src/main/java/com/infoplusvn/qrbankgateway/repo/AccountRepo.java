package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.UserAccountInfo;
import com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin;
import com.infoplusvn.qrbankgateway.entity.AccountEntity;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AccountRepo  extends JpaRepository<AccountEntity, Long> {
    @Transactional
    @Query("SELECT t from AccountEntity t where t.username = :username ")
    AccountEntity findByUsername(@Param("username") String username);

    @Query("select t.userId from AccountEntity t where t.username =:username")
    Long findUserIdByUsername(String username);

    @Query("SELECT t.roles from AccountEntity t where t.username = :username ")
    String findRolesByUserName(@Param("username") String username);

    void deleteByUsername (String username);


    @Query("SELECT u.firstName AS firstName, u.lastName AS lastName, a.accountNumber AS accountNumber, a.amount AS amount " +
                "FROM UserEntity u JOIN AccountEntity a ON u.id = a.userId " +
                "WHERE a.username = :userName")
    UserAccountInfo findUserAccountInfo(@Param("userName") String userName);

    @Query("select new com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin(a.id,a.username,a.enabled,a.createOn,a.roles,a.password,u.email,u.phone,u.company,u.address,u.firstName,u.lastName,a.userId) " +
            "from UserEntity u JOIN AccountEntity a ON u.id = a.userId")
    List<UserDTORoleAdmin> findAllAccount();

    @Query("select new com.infoplusvn.qrbankgateway.dto.user.UserDTORoleAdmin(a.id,a.username,a.enabled,a.createOn,a.roles,a.password,u.email,u.phone,u.company,u.address,u.firstName,u.lastName,a.userId) " +
            "from UserEntity u JOIN AccountEntity a ON u.id = a.userId WHERE a.username = :username")
    UserDTORoleAdmin findDTOEditAccount(String username);
}
