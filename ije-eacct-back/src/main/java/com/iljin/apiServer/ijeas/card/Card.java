package com.iljin.apiServer.ijeas.card;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "TB_CARD_MASTER")
@NoArgsConstructor
@IdClass(CardKey.class)
public class Card extends BaseEntity {

    @Id
    @Column(name = "CRD_NO", nullable = false)
    String crdNo;

    @Id
    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Column(name = "CRD_STAT_CD")
    String crdStatCd;

    @Column(name = "CRD_USG_CD")
    String crdUsgCd;

    @Column(name = "CRD_FG_CD")
    String crdFgCd;

    @Column(name = "CRD_PSSR_ID")
    String crdPssrId;

    @Column(name = "CRD_PUBC_DT")
    String crdPubcDt;

    @Column(name = "CRD_VLD_YM")
    String crdVldYm;

    @Column(name = "CRD_ABLT_DT")
    String crdAbltDt;

    @Column(name = "CRD_CURY_CD")
    String crdCuryCd;

    @Column(name = "CRD_PLMT_AMT")
    BigDecimal crdPlmtAmt;

    @Column(name = "CRD_COMP_CD")
    String crdCompCd;

    @Column(name = "BNK_CD")
    String bnkCd;

    @Column(name = "STL_BACCT_NO")
    String stlBacctNo;

    @Column(name = "STL_DD")
    String stlDd;

    @Column(name = "DPSTR_NM")
    String dpstrNm;

    @Column(name = "PRE_CRD_NO")
    String preCrdNo;

    @Column(name = "CRD_OLN")
    String crdOln;

    @Column(name = "DEL_YN")
    String delYn;

    @Column(name = "VEND_CD")
    String vendCd;

    @Column(name = "VEND_NM")
    String vendNm;

    @Column(name = "BACCT_TCTL_YN")
    String bacctTctlYn;

    @Column(name = "SLIP_PROC_DEPT_CD")
    String slipProcDeptCd;

    @Column(name = "CRD_USE_ID")
    String crdUseId;

    @Column(name = "CRD_USE_STR_DT")
    String crdUseStrDt;

    @Column(name = "CRD_USE_END_DT")
    String crdUseEndDt;



    @Builder
    public Card(String crdNo,String compCd,String crdStatCd,String crdUsgCd,String crdFgCd,String crdPssrId,String crdPubcDt,
                String crdVldYm,String crdAbltDt,String crdCuryCd,BigDecimal crdPlmtAmt,String crdCompCd,String bnkCd,String stlBacctNo,
                String stlDd,String dpstrNm,String preCrdNo,String crdOln,String delYn,String vendCd,String vendNm,String bacctTctlYn,
                String slipProcDeptCd,String crdUseId,String crdUseStrDt,String crdUseEndDt
    ) {
        this.crdNo =  crdNo;
        this.compCd =  compCd;
        this.crdStatCd =  crdStatCd;
        this.crdUsgCd =  crdUsgCd;
        this.crdFgCd =  crdFgCd;
        this.crdPssrId =  crdPssrId;
        this.crdPubcDt =  crdPubcDt;
        this.crdVldYm =  crdVldYm;
        this.crdAbltDt =  crdAbltDt;
        this.crdCuryCd =  crdCuryCd;
        this.crdPlmtAmt =  crdPlmtAmt;
        this.crdCompCd =  crdCompCd;
        this.bnkCd =  bnkCd;
        this.stlBacctNo =  stlBacctNo;
        this.stlDd =  stlDd;
        this.dpstrNm =  dpstrNm;
        this.preCrdNo =  preCrdNo;
        this.crdOln =  crdOln;
        this.delYn =  delYn;
        this.vendCd =  vendCd;
        this.vendNm =  vendNm;
        this.bacctTctlYn =  bacctTctlYn;
        this.slipProcDeptCd =  slipProcDeptCd;
        this.crdUseId =  crdUseId;
        this.crdUseStrDt =  crdUseStrDt;
        this.crdUseEndDt =  crdUseEndDt;
    }

    public Card update (CardDto cardDto){
        this.crdNo = cardDto.getCrdNo() ;
        this.crdPubcDt = cardDto.getCrdPubcDt() ;
        this.crdVldYm = cardDto.getCrdVldYm() ;
        this.crdAbltDt = cardDto.getCrdAbltDt() ;
        this.crdPlmtAmt = cardDto.getCrdPlmtAmt() ;
        this.stlBacctNo = cardDto.getStlBacctNo() ;
        this.preCrdNo = cardDto.getPreCrdNo() ;
        this.crdUseStrDt = cardDto.getCrdUseStrDt() ;
        this.crdUseEndDt = cardDto.getCrdUseEndDt() ;
        this.bacctTctlYn = cardDto.getBacctTctlYn() ;
        this.crdCompCd = cardDto.getCrdCompCd() ;
        this.preCrdNo = cardDto.getPreCrdNo() ;
        this.crdPssrId = cardDto.getCrdPssrId() ;
        this.crdOln = cardDto.getCrdOln() ;
        this.crdUseStrDt = cardDto.getCrdUseStrDt() ;
        this.crdUseEndDt = cardDto.getCrdUseEndDt() ;
        this.crdUsgCd = cardDto.getCrdUsgCd() ;
        this.crdVldYm = cardDto.getCrdVldYm() ;
        this.dpstrNm = cardDto.getDpstrNm() ;
        this.stlDd = cardDto.getStlDd() ;
        this.vendCd = cardDto.getVendCd() ;
        this.vendNm = cardDto.getVendNm() ;
        this.crdStatCd = cardDto.getCrdStatCd();
        this.crdFgCd = cardDto.getCrdFgCd();
        this.bnkCd = cardDto.getBnkCd();
        this.crdUseId = cardDto.getCrdUseId();
        return this;
    }

}
