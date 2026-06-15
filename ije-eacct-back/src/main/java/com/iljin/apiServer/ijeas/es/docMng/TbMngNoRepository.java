package com.iljin.apiServer.ijeas.es.docMng;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TbMngNoRepository extends JpaRepository<TbMngNo, TbMngNoKey> {
    List<TbMngNo> findByModuleIdAndReqTypeCd(String moduleId, String reqTypeCd);

    @Transactional
    @Modifying
    @Query("update TbMngNo t set t.reqDt = ?1, t.lastSeq = ?2 where t.moduleId = ?3 and t.reqTypeCd = ?4")
    int updateReqDtAndLastSeqByModuleIdAndReqTypeCd(String reqDt, Integer lastSeq, String moduleId, String reqTypeCd);

}
