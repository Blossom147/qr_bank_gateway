package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser;
import com.infoplusvn.qrbankgateway.dto.QR.QrCodeListDTO;
import com.infoplusvn.qrbankgateway.entity.QRCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QrCodeRepo extends JpaRepository<QRCodeEntity,Long> {

    @Query("SELECT t from QRCodeEntity t ORDER BY t.id DESC")
    List<QRCodeEntity> getAllQR();

    @Query("SELECT t from QRCodeEntity t where t.id = :qrId")
    QRCodeEntity getById(Long qrId);

    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.customerName,t.text,t.qrImage,t.qrThemeImage,t.updateOn) from QRCodeEntity t where t.createdUser =:createdUser and t.enabled = true ORDER BY t.updateOn DESC")
    List<QrCodeDTORoleUser> findByCreatedUserRoleUser(@Param("createdUser") String createdUser);

    @Query("SELECT t.qrThemeImage from QRCodeEntity t where t.id = :qrId")
    String getQrThemeImageById(@Param("qrId") Long qrId);

    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.customerName,t.text,t.qrImage,t.qrThemeImage,t.updateOn) from QRCodeEntity t where t.id =:qrId ")
    QrCodeDTORoleUser getQrDTOById(@Param("qrId") Long qrId);

    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.customerName,t.text,t.qrImage,t.qrThemeImage,t.updateOn) from QRCodeEntity t where t.createdUser =:createdUser and t.enabled = false ORDER BY t.updateOn DESC")
    List<QrCodeDTORoleUser> findByCreatedUserAndEnabledFalseRoleUser(@Param("createdUser") String createdUser);

    @Query("SELECT COUNT(t) FROM QRCodeEntity t")
    Long countQRCode();


}
