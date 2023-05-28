package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.entity.BankEntity;
import com.infoplusvn.qrbankgateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BankRepo extends JpaRepository<BankEntity, Long> {
    BankEntity findByBin(String bin);

    @Query("SELECT t from BankEntity t where t.shortName = :shortName ")
    BankEntity findByShortName(@Param("shortName") String shortName);


}
