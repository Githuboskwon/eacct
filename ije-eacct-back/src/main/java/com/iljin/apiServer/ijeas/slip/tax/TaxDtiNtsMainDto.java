package com.iljin.apiServer.ijeas.slip.tax;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TaxDtiNtsMainDto implements Serializable {

    private static final long serialVersionUID = 113522502342970800L;

    Long cnt;

    String issueId;
    String supbuyType;
    String dtiType;
    String amendYn;
    Integer interfaceBatchId;
    Integer fileIdx;
    String issueDate;
    String issueDt;
    LocalDateTime dtiSdate;
    String issueSystem;
    String typeCode;
    String typeCodeTxt;
    String purpCode;
    String interNo;
    String exchangedDocId;
    String suId;
    String suMin;
    String suName;
    String suRepres;
    String SuBustype;
    String suIndtype;
    String suAddr;
    String suDeptname;
    String suPersname;
    String suTelno;
    String suEmail;
    String ipId;
    String ipMin;
    String ipName;
    String ipRepres;
    String ipBustype;
    String ipIndtype;
    String ipAddr;
    String ipDeptname1;
    String ipDeptname2;
    String ipPersname1;
    String ipPersname2;
    String ipTelno1;
    String ipTelno2;
    String ipEmail1;
    String ipEmail2;
    String ipTypecode;
    String bpId;
    String bpMin;
    String bpName;
    String bpRepres;
    String bpBustype;
    String bpIndtype;
    String bpAddr;
    String bpDeptname;
    String bpPersname;
    String bpTelno;
    String bpEmail;
    BigDecimal cashAmount;
    BigDecimal checkAmount;
    BigDecimal noteAmount;
    BigDecimal receivableAmount;
    BigDecimal chargetotal;
    BigDecimal taxtotal;
    BigDecimal grandtotal;
    String descText1;
    String descText2;
    String descText3;
    String amendCode;
    String ernam;
    LocalDateTime erdat;
    String lastUpdatedBy;
    String taxStatus;
    String slipTypeText;


    @QueryProjection
    public TaxDtiNtsMainDto(String issueId, String supbuyType, String dtiType, String amendYn, Integer interfaceBatchId, Integer fileIdx, String issueDate, String issueDt, LocalDateTime dtiSdate, String issueSystem, String typeCode, String typeCodeTxt, String purpCode, String interNo, String exchangedDocId, String suId, String suMin, String suName, String suRepres, String suBustype, String suIndtype, String suAddr, String suDeptname, String suPersname, String suTelno, String suEmail, String ipId, String ipMin, String ipName, String ipRepres, String ipBustype, String ipIndtype, String ipAddr, String ipDeptname1, String ipDeptname2, String ipPersname1, String ipPersname2, String ipTelno1, String ipTelno2, String ipEmail1, String ipEmail2, String ipTypecode, String bpId, String bpMin, String bpName, String bpRepres, String bpBustype, String bpIndtype, String bpAddr, String bpDeptname, String bpPersname, String bpTelno, String bpEmail, BigDecimal cashAmount, BigDecimal checkAmount, BigDecimal noteAmount, BigDecimal receivableAmount, BigDecimal chargetotal, BigDecimal taxtotal, BigDecimal grandtotal, String descText1, String descText2, String descText3, String amendCode, String ernam, LocalDateTime erdat, String lastUpdatedBy, String taxStatus, String slipTypeText) {
        this.issueId = issueId;
        this.supbuyType = supbuyType;
        this.dtiType = dtiType;
        this.amendYn = amendYn;
        this.interfaceBatchId = interfaceBatchId;
        this.fileIdx = fileIdx;
        this.issueDate = issueDate;
        this.issueDt = issueDt;
        this.dtiSdate = dtiSdate;
        this.issueSystem = issueSystem;
        this.typeCode = typeCode;
        this.typeCodeTxt = typeCodeTxt;
        this.purpCode = purpCode;
        this.interNo = interNo;
        this.exchangedDocId = exchangedDocId;
        this.suId = suId;
        this.suMin = suMin;
        this.suName = suName;
        this.suRepres = suRepres;
        this.SuBustype = suBustype;
        this.suIndtype = suIndtype;
        this.suAddr = suAddr;
        this.suDeptname = suDeptname;
        this.suPersname = suPersname;
        this.suTelno = suTelno;
        this.suEmail = suEmail;
        this.ipId = ipId;
        this.ipMin = ipMin;
        this.ipName = ipName;
        this.ipRepres = ipRepres;
        this.ipBustype = ipBustype;
        this.ipIndtype = ipIndtype;
        this.ipAddr = ipAddr;
        this.ipDeptname1 = ipDeptname1;
        this.ipDeptname2 = ipDeptname2;
        this.ipPersname1 = ipPersname1;
        this.ipPersname2 = ipPersname2;
        this.ipTelno1 = ipTelno1;
        this.ipTelno2 = ipTelno2;
        this.ipEmail1 = ipEmail1;
        this.ipEmail2 = ipEmail2;
        this.ipTypecode = ipTypecode;
        this.bpId = bpId;
        this.bpMin = bpMin;
        this.bpName = bpName;
        this.bpRepres = bpRepres;
        this.bpBustype = bpBustype;
        this.bpIndtype = bpIndtype;
        this.bpAddr = bpAddr;
        this.bpDeptname = bpDeptname;
        this.bpPersname = bpPersname;
        this.bpTelno = bpTelno;
        this.bpEmail = bpEmail;
        this.cashAmount = cashAmount;
        this.checkAmount = checkAmount;
        this.noteAmount = noteAmount;
        this.receivableAmount = receivableAmount;
        this.chargetotal = chargetotal;
        this.taxtotal = taxtotal;
        this.grandtotal = grandtotal;
        this.descText1 = descText1;
        this.descText2 = descText2;
        this.descText3 = descText3;
        this.amendCode = amendCode;
        this.ernam = ernam;
        this.erdat = erdat;
        this.lastUpdatedBy = lastUpdatedBy;
        this.taxStatus = taxStatus;
        this.slipTypeText = slipTypeText;
    }
/***
     * 엔티티 값 그대로 가져옴
     */
//    String issueId;
//    String supbuyType;
//    String dtiType;
//    String amendYn;
//    Integer interfaceBatchId;
//    Integer fileIdx;
//    LocalDateTime dtiWdate;
//    LocalDateTime dtiIdate;
//    LocalDateTime dtiSdate;
//    String issueSystem;
//    String typeCodea;
//    String taxDemand;
//    String seqId;
//    String exchangedDocId;
//    String supComRegno;
//    String supBizplaceCode;
//    String supComName;
//    String supEmpName;
//    String supComType;
//    String supComClassify;
//    String supComAddr;
//    String supDeptName;
//    String supPersonName;
//    String supTelNum;
//    String supEmail;
//    String byrComRegno;
//    String byrBizplaceCode;
//    String byrComName;
//    String byrEmpName;
//    String byrComType;
//    String byrComClassify;
//    String byrComAddr;
//    String byrDeptName;
//    String byrPersonName;
//    String byrTelNum;
//    String byrEmail;
//    String byrDeptName2;
//    String byrPersonName2;
//    String byrTelNum2;
//    String byrEmail2;
//    String byrBusinessTypeCode;
//    String brokerComRegno;
//    String brokerBizplaceCode;
//    String brokerComName;
//    String brokerEmpName;
//    String brokerComType;
//    String brokerComClassify;
//    String brokerComAddr;
//    String brokerDeptName;
//    String brokerPersonName;
//    String brokerTelNum;
//    String brokerEmail;
//    Integer cashAmount;
//    Integer checkAmount;
//    Integer noteAmount;
//    Integer receivableAmount;
//    Integer supAmount;
//    Integer taxAmount;
//    Integer totalAmount;
//    String remark;
//    String remark2;
//    String remark3;
//    String dtiMsg;
//    String amendCode;
//    String createdBy;
//    LocalDateTime creationDate;
//    String lastUpdatedBy;
//    String oriIssueId;



    LocalDateTime dtiWdate;
    LocalDateTime dtiIdate;
    String taxDemand;
    String seqId;
    String supComRegno;
    String supBizplaceCode;
    String supComName;
    String supEmpName;
    String supComType;
    String supComClassify;
    String supComAddr;
    String supDeptName;
    String supPersonName;
    String supTelNum;
    String supEmail;
    String byrComRegno;
    String byrBizplaceCode;
    String byrComName;
    String byrEmpName;
    String byrComType;
    String byrComClassify;
    String byrComAddr;
    String byrDeptName;
    String byrPersonName;
    String byrTelNum;
    String byrEmail;
    String byrDeptName2;
    String byrPersonName2;
    String byrTelNum2;
    String byrEmail2;
    String byrBusinessTypeCode;
    String brokerComRegno;
    String brokerBizplaceCode;
    String brokerComName;
    String brokerEmpName;
    String brokerComType;
    String brokerComClassify;
    String brokerComAddr;
    String brokerDeptName;
    String brokerPersonName;
    String brokerTelNum;
    String brokerEmail;
    BigDecimal supAmount;
    BigDecimal taxAmount;
    BigDecimal totalAmount;
    String remark;
    String remark2;
    String remark3;
    String createdBy;
    LocalDateTime creationDate;
    LocalDateTime lastUpdateDate;
    String dtiTypeTxt;
    String slipNo;
    String slipStatus;
    String slipStatusNm;
    String amendCodeNm;
    String erpNum;
    String erpName;

    @QueryProjection
    public TaxDtiNtsMainDto(String issueId, String supbuyType, String dtiType, String amendYn, Integer interfaceBatchId, Integer fileIdx, LocalDateTime dtiWdate,
                            LocalDateTime dtiIdate, LocalDateTime dtiSdate, String issueSystem, String typeCode, String typeCodeTxt, String taxDemand, String seqId, String exchangedDocId,
                            String supComRegno, String supBizplaceCode, String supComName, String supEmpName, String supComType, String supComClassify, String supComAddr,
                            String supDeptName, String supPersonName, String supTelNum, String supEmail, String byrComRegno, String byrBizplaceCode, String byrComName,
                            String byrEmpName, String byrComType, String byrComClassify, String byrComAddr, String byrDeptName, String byrDeptName2, String byrPersonName,
                            String byrPersonName2, String byrTelNum, String byrTelNum2, String byrEmail, String byrEmail2, String byrBusinessTypeCode, String brokerComRegno,
                            String brokerBizplaceCode, String brokerComName, String brokerEmpName, String brokerComType, String brokerComClassify, String brokerComAddr,
                            String brokerDeptName, String brokerPersonName, String brokerTelNum, String brokerEmail, BigDecimal cashAmount, BigDecimal checkAmount,
                            BigDecimal noteAmount, BigDecimal receivableAmount, BigDecimal supAmount, BigDecimal taxAmount, BigDecimal totalAmount, String remark,
                            String remark2, String remark3, String amendCode, String amendCodeNm, String createdBy, LocalDateTime creationDate, String lastUpdatedBy,
                            String dtiTypeTxt, String slipNo, String slipStatus, String erpNum, String erpName) {
        this.issueId = issueId;
        this.supbuyType = supbuyType;
        this.dtiType = dtiType;
        this.amendYn = amendYn;
        this.interfaceBatchId = interfaceBatchId;
        this.fileIdx = fileIdx;
        this.dtiWdate = dtiWdate;
        this.dtiIdate = dtiIdate;
        this.dtiSdate = dtiSdate;
        this.issueSystem = issueSystem;
        this.typeCode = typeCode;
        this.typeCodeTxt = typeCodeTxt;
        this.seqId = seqId;
        this.interNo = interNo;
        this.exchangedDocId = exchangedDocId;
        this.supComRegno = supComRegno;
        this.supBizplaceCode = supBizplaceCode;
        this.supComName = supComName;
        this.supEmpName = supEmpName;
        this.supComType = supComType;
        this.supComClassify = supComClassify;
        this.supComAddr = supComAddr;
        this.supDeptName = supDeptName;
        this.supPersonName = supPersonName;
        this.supTelNum = supTelNum;
        this.supEmail = supEmail;
        this.byrComRegno = byrComRegno;
        this.byrBizplaceCode = byrBizplaceCode;
        this.byrComName = byrComName;
        this.byrEmpName = byrEmpName;
        this.byrComType = byrComType;
        this.byrComClassify = byrComClassify;
        this.byrComAddr = byrComAddr;
        this.byrDeptName = byrDeptName;
        this.byrDeptName2 = byrDeptName2;
        this.byrPersonName = byrPersonName;
        this.byrPersonName2 = byrPersonName2;
        this.byrTelNum = byrTelNum;
        this.byrTelNum2 = byrTelNum2;
        this.byrEmail = byrEmail;
        this.byrEmail2 = byrEmail2;
        this.byrBusinessTypeCode = byrBusinessTypeCode;
        this.brokerComRegno = brokerComRegno;
        this.brokerBizplaceCode = brokerBizplaceCode;
        this.brokerComName = brokerComName;
        this.brokerEmpName = brokerEmpName;
        this.brokerComType = brokerComType;
        this.brokerComClassify = brokerComClassify;
        this.brokerComAddr = brokerComAddr;
        this.brokerDeptName = brokerDeptName;
        this.brokerPersonName = brokerPersonName;
        this.brokerTelNum = brokerTelNum;
        this.brokerEmail = brokerEmail;
        this.cashAmount = cashAmount;
        this.checkAmount = checkAmount;
        this.noteAmount = noteAmount;
        this.receivableAmount = receivableAmount;
        this.supAmount = supAmount;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.remark = remark;
        this.remark2 = remark2;
        this.remark3 = remark3;
        this.amendCode = amendCode;
        this.amendCodeNm = amendCodeNm;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.dtiTypeTxt = dtiTypeTxt;
        this.slipNo = slipNo;
        this.slipStatus = slipStatus;
        this.slipStatusNm = slipStatusNm;
        this.erpNum = erpNum;
        this.erpName = erpName;
    }

    @QueryProjection
    public TaxDtiNtsMainDto(Long cnt) {
        this.cnt = cnt;
    }
}
