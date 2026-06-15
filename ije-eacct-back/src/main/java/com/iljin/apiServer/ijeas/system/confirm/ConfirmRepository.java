package com.iljin.apiServer.ijeas.system.confirm;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConfirmRepository extends JpaRepository<Confirm, ConfirmKey> {

    @Override
    Optional<Confirm> findById(ConfirmKey confirmKey);

    Integer countByConfirmSeqAndDeptCd(String confirmSeq, String deptCd);

    @Query("SELECT COUNT(c) FROM Confirm c " +
            "WHERE c.confirmSeq = :pervConfirmSeq " +
            "AND c.deptCd = :deptCd " +
            "AND c.confirmEndAmt >= :confirmStartAmt " )
    Integer countByConfirmSeqAndDeptCdAndConfirmStartAmtUnderAmt(@Param("pervConfirmSeq") String pervConfirmSeq,@Param("deptCd") String deptCd,@Param("confirmStartAmt") BigDecimal confirmStartAmt);

    @Query("SELECT e FROM Confirm e WHERE e.compCd = :compCd AND e.deptCd = :deptCd AND :totalAmt BETWEEN e.confirmStartAmt AND e.confirmEndAmt order by e.confirmSeq ")
    List<Confirm> findConfirmList(@Param("compCd") String compCd, @Param("deptCd") String deptCd, @Param("totalAmt") BigDecimal totalAmt);
}
