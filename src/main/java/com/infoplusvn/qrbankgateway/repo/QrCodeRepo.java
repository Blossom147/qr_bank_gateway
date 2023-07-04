package com.infoplusvn.qrbankgateway.repo;

import com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser;
import com.infoplusvn.qrbankgateway.dto.QR.QrCodeListDTO;
import com.infoplusvn.qrbankgateway.dto.common.Payment.TransactionDTO;
import com.infoplusvn.qrbankgateway.entity.QRCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface QrCodeRepo extends JpaRepository<QRCodeEntity,Long> {

    @Query("SELECT t from QRCodeEntity t ORDER BY t.updateOn DESC")
    List<QRCodeEntity> getAllQR();

    @Query("SELECT t from QRCodeEntity t where t.id = :qrId")
    QRCodeEntity getById(Long qrId);

    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.text,t.qrImage,t.qrThemeImage,t.updateOn) " +
            "from QRCodeEntity t where t.createdUser =:createdUser and t.enabled = true ORDER BY t.updateOn DESC")
    List<QrCodeDTORoleUser> findByCreatedUserRoleUser(@Param("createdUser") String createdUser);

    @Query("SELECT t.qrThemeImage from QRCodeEntity t where t.id = :qrId")
    String getQrThemeImageById(@Param("qrId") Long qrId);

    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.text,t.qrImage,t.qrThemeImage,t.updateOn) from QRCodeEntity t where t.id =:qrId ")
    QrCodeDTORoleUser getQrDTOById(@Param("qrId") Long qrId);

    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.text,t.qrImage,t.qrThemeImage,t.updateOn) from QRCodeEntity t where t.createdUser =:createdUser and t.enabled = false ORDER BY t.updateOn DESC")
    List<QrCodeDTORoleUser> findByCreatedUserAndEnabledFalseRoleUser(@Param("createdUser") String createdUser);

    @Query("SELECT COUNT(t) FROM QRCodeEntity t")
    Long countQRCode();

    // Tìm kiếm
    @Query("SELECT new com.infoplusvn.qrbankgateway.dto.QR.QrCodeDTORoleUser(t.id,t.qrName,t.createdUser,t.qrType,t.trnDt,t.text,t.qrImage,t.qrThemeImage,t.updateOn) " +
            "FROM QRCodeEntity t " +
            "WHERE t.createdUser =:createdUser " +
            "and (:qrType IS '' OR t.qrType = :qrType) and t.enabled = true ORDER BY t.updateOn DESC")
    List<QrCodeDTORoleUser> searchQR( @Param("qrType") String qrType,
                                      @Param("createdUser") String createdUser);

    // Update
    @Modifying
    @Query("UPDATE QRCodeEntity t SET t.qrName = :qrName, t.qrType = :qrType WHERE t.id = :id")
    void updateQrNameAndQrTypeById(@Param("qrName") String qrName, @Param("qrType") String qrType, @Param("id") Long id);

}
