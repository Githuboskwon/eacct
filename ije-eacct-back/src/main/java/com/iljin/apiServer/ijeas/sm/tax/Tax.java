package com.iljin.apiServer.ijeas.sm.tax;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_mst_tax")
@IdClass(TaxKey.class)
@Data
public class Tax {

    // 세금코드 
	@Id
    @Column(name = "tax_cd", nullable = false)
    String taxCd;

    // 회사코드
    @Id
    @Column(name = "comp_cd", nullable = false)
    String compCd;

    // 세금코드명 
    @Column(name = "tax_nm", nullable = false)
    String taxNm;

    // 세율 
    @Column(name = "tax_rt", nullable = false)
    int taxRt;

    // 사용여부 
    @Column(name = "use_yn", nullable = false)
    String useYn;

    // 정렬순서 
    @Column(name = "order_seq", nullable = false)
    int orderSeq;

    // 증빙유형코드 
    @Column(name = "evd_type_cd")
    String evdTypeCd;

    // 증빙유형명 
    @Column(name = "evd_type_nm")
    String evdTypeNm;

    // 비고1 
    @Column(name = "ref1")
    String ref1;

    // 비고2 
    @Column(name = "ref2")
    String ref2;

    // 비고3 
    @Column(name = "ref3")
    String ref3;

    // 등록자ID 
    @Column(name = "reg_id")
    String regId;

    // 등록일시 
	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "reg_dtm", nullable = false)
    LocalDateTime regDtm;

    // 수정자ID 
    @Column(name = "chg_id")
    String chgId;

    // 수정일시 
	@UpdateTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "chg_dtm", nullable = false)
    LocalDateTime chgDtm;

    // IF ID 
    @Column(name = "picode")
    String picode;

    // IF 상태 
    @Column(name = "pistat")
    String pistat;

    // IF 일자 
    @Column(name = "pidate")
    String pidate;

    // IF 시간 
    @Column(name = "pitime")
    String pitime;

    // IF 유저 
    @Column(name = "piuser")
    String piuser;

    // 전송메시지 
    @Column(name = "pimsg")
    String pimsg;

    // 메시지ID 
    @Column(name = "pimsgid")
    String pimsgid;

}
