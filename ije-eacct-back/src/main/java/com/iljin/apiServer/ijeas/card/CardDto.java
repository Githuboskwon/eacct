package com.iljin.apiServer.ijeas.card;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@NoArgsConstructor
@Data
public class CardDto {

    String crdNo;

    String compCd;

    String crdStatCd;

    String crdUsgCd;

    String crdFgCd;

    String crdPssrId;

    String crdPubcDt;

    String crdVldYm;

    String crdAbltDt;

    String crdCuryCd;

    BigDecimal crdPlmtAmt;

    String crdCompCd;

    String bnkCd;

    String stlBacctNo;

    String stlDd;

    String dpstrNm;

    String preCrdNo;

    String crdOln;

    String delYn;

    String vendCd;

    String vendNm;

    String bacctTctlYn;

    String slipProcDeptCd;

    String crdUseId;

    String crdUseStrDt;

    String crdUseEndDt;

    String compNm;

    String crdStatNm;

    String crdFgNm;

    String crdPssrNm;

    String crdCompNm;

    String crdPssrDut;

    String crdPssrDept;

    String crdPssrCompCd;

    String crdPssrCompNm;

    String crdUseNm;

    String crdUseDut;

    String crdUseDept;

    String searchDtmFr;

    String searchDtmTo;

    Integer page;

    Integer limit;



    /*
     * 조회 클릭 event
     * 리스트 조회
     * */
    @QueryProjection
    public CardDto(String crdNo,String compCd,String compNm,String crdStatCd,String crdStatNm,String crdFgCd,String crdFgNm,
                   String crdPssrId,String crdPssrNm,String crdPubcDt,String crdVldYm,BigDecimal crdPlmtAmt,String crdCompCd,String crdCompNm,
                   String crdOln
            ) {
        this.crdNo =  crdNo;
        this.compCd =  compCd;
        this.compNm = compNm;
        this.crdStatCd =  crdStatCd;
        this.crdStatNm = crdStatNm;
        this.crdFgCd =  crdFgCd;
        this.crdFgNm = crdFgNm;
        this.crdPssrId =  crdPssrId;
        this.crdPssrNm = crdPssrNm;
        this.crdPubcDt =  crdPubcDt;
        this.crdVldYm = crdVldYm;
        this.crdPlmtAmt =  crdPlmtAmt;
        this.crdCompCd =  crdCompCd;
        this.crdCompNm = crdCompNm;
        this.crdOln =  crdOln;
    }

    /*
     * 조회 클릭 event
     * 디테일 조회
     * */
    @QueryProjection
    public CardDto(String crdNo,String crdCompCd,String crdStatCd,String crdPssrId,String crdPssrNm,
                   String crdPssrDut,String crdPssrDept,String crdPssrCompCd,String crdPssrCompNm,
                   String crdUsgCd,String crdPubcDt,String crdAbltDt,BigDecimal crdPlmtAmt,String crdVldYm,
                   String preCrdNo,String crdFgCd,String crdUseId,String crdUseNm,String crdUseDut,
                   String crdUseDept,String bnkCd,String stlDd,String crdUseStrDt,String crdUseEndDt,
                   String stlBacctNo,String crdOln,String vendCd,String vendNm
    ) {
        this.crdNo =  crdNo;
        this.crdCompCd =  crdCompCd;
        this.crdStatCd =  crdStatCd;
        this.crdPssrId =  crdPssrId;
        this.crdPssrNm =  crdPssrNm;
        this.crdPssrDut =  crdPssrDut;
        this.crdPssrDept =  crdPssrDept;
        this.crdPssrCompCd =  crdPssrCompCd;
        this.crdPssrCompNm =  crdPssrCompNm;
        this.crdUsgCd =  crdUsgCd;
        this.crdPubcDt =  crdPubcDt;
        this.crdAbltDt =  crdAbltDt;
        this.crdPlmtAmt =  crdPlmtAmt;
        this.crdVldYm =  crdVldYm;
        this.preCrdNo =  preCrdNo;
        this.crdFgCd =  crdFgCd;
        this.crdUseId =  crdUseId;
        this.crdUseNm =  crdUseNm;
        this.crdUseDut =  crdUseDut;
        this.crdUseDept =  crdUseDept;
        this.bnkCd =  bnkCd;
        this.stlDd =  stlDd;
        this.crdUseStrDt =  crdUseStrDt;
        this.crdUseEndDt =  crdUseEndDt;
        this.stlBacctNo =  stlBacctNo;
        this.crdOln =  crdOln;
        this.vendCd =  vendCd;
        this.vendNm =  vendNm;
    }

}
