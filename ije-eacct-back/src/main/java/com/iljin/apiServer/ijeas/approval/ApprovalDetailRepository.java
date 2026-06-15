package com.iljin.apiServer.ijeas.approval;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ApprovalDetailRepository extends JpaRepository<ApprovalDetail, ApprovalDetailKey> {

    // Optional<ApprovalDetail> findOne(ApprovalDetailKey approvalDetailKey);
    Optional<ApprovalDetail> findByApprNo(String apprNo);

    List<ApprovalDetail> findByCompCdAndApprGroupId(String apprNo, BigDecimal apprGroupId);

//    Optional<ApprovalDetail> findTopByApprNoDesc(String apprNo);

    void deleteByApprNo(String apprNo);

    Boolean existsByCompCdAndApprGroupId(String compCd, BigDecimal apprGroupId);

    Boolean existsBycompCdAndApprGroupIdAndApprTypeCd(String compCd, BigDecimal ApprGroupId, String ApprTypeCd);

    Optional<ApprovalDetail> findByCompCdAndApprGroupIdAndApprStage(String compCd, BigDecimal apprGroupId, String apprStage);

    Boolean existsByCompCdAndApprGroupIdAndSlipNo(String compCd, BigDecimal apprGroupId, String slipNo);

    @Transactional
    @Modifying
    @Query("update ApprovalDetail a set a.aprverId = ?1, a.aprverNm = ?2, a.aprverDeptNm = ?3, a.aprverJobNm = ?4 " +
            "where a.apprGroupId = ?5 and a.apprStage = ?6")
    int changeApprDt(String aprverId, String aprverNm, String aprverDeptNm, String aprverJobNm, BigDecimal apprGroupId, String apprStage);

    @Transactional
    @Modifying
    void deleteByCompCdAndApprGroupId(String compCd, BigDecimal apprGroupId);

    @Modifying
    @Query("UPDATE ApprovalDetail a SET a.slipStatus = :status WHERE a.compCd = :compCd AND a.apprGroupId = :apprGroupId")
    void updateStatusByCompCdAndApprGroupId(@Param("status") String status, @Param("compCd") String compCd, @Param("apprGroupId") BigDecimal apprGroupId );

    @Modifying
    @Query("UPDATE ApprovalDetail a SET a.slipStatus = :status, a.apprDesc = :apprDesc WHERE a.compCd = :compCd AND a.apprGroupId = :apprGroupId AND a.apprStage = :stage")
    void updateStatusAndApprDescByCompCdAndApprGroupIdAndApprStage(@Param("status") String status, @Param("apprDesc") String apprDesc, @Param("compCd") String compCd, @Param("apprGroupId") BigDecimal apprGroupId, @Param("stage")String stage);


}
