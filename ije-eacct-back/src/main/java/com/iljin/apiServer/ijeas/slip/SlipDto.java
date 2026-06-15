package com.iljin.apiServer.ijeas.slip;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iljin.apiServer.ijeas.bond.BondHeaderDto;
import com.iljin.apiServer.ijeas.slip.header.SlipBusinessTripDto;
import com.iljin.apiServer.ijeas.slip.header.SlipTrafficHdDto;
import com.iljin.apiServer.ijeas.system.expend.SlipExpendDto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SlipDto implements Serializable {

    private static final long serialVersionUID = 7243103578505930972L;

    String temp;

    String empNo;

    Integer personId;
    //회사코드
    String compCd;

    // 전표번호
    String slipNo;

    // 전표헤더ID
    BigDecimal slipHeaderId;

    // 전표 그룹번호
    String slipGroupNo;

    // 귀속부서 번호
    String deptNo;

    // 거래유형코드
    String slipType;

    // 전표유형코드
    String slipTypeCd;

    // 전자전표유형
    String slipForm;

    // 전표상태
    String status;

    // 회계일자
    String postingDt;

    // 헤더 적요
    String headerRemark;

    // 통화코드. 통화코드
    String usedCur;

    // 사용금액
    String usedAmt;

    // 외화금액
    String usedForAmt;

    // 스캔증빙
    String scanNo;

    // 첨부파일번호
    String docNo;

    // 실물증빙 추가 대상 여부 (Y, '')
    String evidenceYn;

    // 결재번호
    BigDecimal approvalGroupId;

    // 세금계산서 공급가액.
    BigDecimal taxbillSupplyAmt;

    // 세금계산서 부가세액
    BigDecimal taxbillTaxAmt;

    // 세금계산서 총액
    BigDecimal taxbillTotalAmt;

    // 세금계산서 KEY 번호(승인번호)
    String taxSmartbillNo;

    // LEDGER_ID
    BigDecimal ledgerId;

    // 사용안함
    String validationFlag;

    // 사용안함
    String errorMsg;

    //WING 문서연결 제목
    String docTitle;

    // WING 문서연결 URL
    String docUrl;

    // ERP전표ID
    String erpInvoiceId;

    // ERP전표상신자ID
    String erpAppUserId;

    // 사용안함
    String erpVendorNm;

    // ERP-자금전표 KEY
    String erpXtrSlipType;

    // ERP-자금전표 KEY
    String erpTransactionNo;

    // 부가세액 조정 FLAG
    String taxbillAmtModifyYn;

    // 세금계산서 공급자 사업자번호
    String taxbillSuId;

    // 중간전송여부
    String transferType;

    // 비고
    String remark;

    String prepaymentYn;

    // 전표 재사용 여부
    String slipReusePossibleYn;

    // 전표 복사 여부
    String slipCopyYn;

    // 전자전표 Number
    String slipNumber;

    // 전표 그룹번호
    String slipGroupNumber;

    // invoice_type_lookup_code
    String invoiceTypeLookupCode;

    // Batch Source 유형(AP invoice_type_lookup_code)
    String batchSourceName;

    String categoryName;

    // 거래유형코드
    String trxTypeCode;

    // 거래유형 출처
    String ttypeInputModule;

    // 거래유형 인터페이스 모듈
    String ttypeInterfaceModule;

    // 거래유형 인터페이스 전표유형
    String ttypeInterfaceSlipType;

    // 거래유형 선급여부
    String ttypePrepaymentFlag;

    // 거래유형 연결계정
    String ttypeClearingAcctCode;

    // 거래유형 세부유형
    String ttypeAddInfoType;

    // 거래유형 통합거래처번호
    String ttypeIntegrationVendorNum;

    // 거래유형 Term ID
    Integer ttypePaymentReceiptTermId;

    Integer orgId;


    // Header 법인코드(SEGMENT1)
    String compCode;

    // Header 예산부서코드(SEGMENT2)
    String budgetDeptCode;

    // Header 프로젝트코드(SEGMENT6)
    String projectCode;

    // Header 프로젝트 ID
    BigDecimal projectId;

    // Header Task코드
    String taskCode;

    // Header Task ID
    String taskId;

    // Header 제품군코드(SEGMENT5)
    String itemGroupCode;

    // Header CCID
    BigDecimal codeCombinationId;

    // 차대변 구분
    String drCr;

    // Header 계정코드(SEGMENT4)
    String acctCode;

    // 회계일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime glDate;

    // 전표일자(invoice_date, trx_date)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime slipDate;

    // 세무증빙유형코드
    String taxEvidenceType;

    // 통화코드
    String slipCurrencyCode;

    // 환율유형
    String exchangeRateType;

    // 환율일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime exchangeDate;

    // 환율
    BigDecimal exchangeRate;

    // 전표금액(입력)
    BigDecimal enteredAmount;

    // 전표금액(원화)
    BigDecimal accountedAmount;

    // 연관전표모듈
    String referenceSlipModule;

    // 연관전표번호
    String referenceSlipNumber;

    // 적요
    String description;

    // 통합거래처번호
    String integrationVendorNum;

    BigDecimal vendorId;

    BigDecimal vendorSiteId;

    BigDecimal vendorPartyId;

    BigDecimal vendorPartySiteId;

    BigDecimal customerId;

    BigDecimal customerSiteId;

    BigDecimal customerPartyId;

    BigDecimal customerPartySiteId;

    // 수금/지급방법
    String termName;

    // 수금/지급 방법ID
    Integer termId;

    // 수금/지급 예정일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime termDueDate;

    // 선급금정산 대상 여부
    String prepaymentApplyFlag;

    // 수금/지급방법
    String paymentReceiptMethodCode;

    // 지급그룹
    String payGroupLookupCode;

    // 어음여부
    String noteFlag;

    // 어음만기일
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime maturityDate;

    // 원천세ID
    Integer awtGroupId;

    // 원천세 사업장코드
    String awtLocationCode;

    // Header 부가세 사업장코드
    String taxLocationCode;

    // Header 부가세 사업장명
    String taxLocationName;

    // 발행구분코드(정/역발행)
    String taxIssueTypeCode;

    // 부가세코드
    String taxCode;

    String taxRateCode;

    // 부가세코드 ID
    Integer vatTaxId;

    Integer taxRateId;

    // 부가세계정코드(SEGMENT4)
    String taxAcctCode;

    // Header TAX CCID
    BigDecimal taxCodeCombinationId;

    // 증빙거래처번호
    String evidenceVendorNum;

    // 증빙거래처ID
    BigDecimal evidenceVendorCustSiteId;

    // 증빙작성일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime evidenceDate;

    // 공급가액
    BigDecimal supplyAmount;

    // 부가세액
    BigDecimal taxAmount;

    // Header 귀속부서코드(SEGMENT3)
    String actualDeptCode;

    // Header 금융계좌코드(SEGMENT7)
    String trBankAcctCode;

    // Header 전표유형코드(SEGMENT8)
    String slipTypeCode;

    // Header 여분1코드(SEGMENT9)
    String segment9Code;

    // Header 여분1코드(SEGMENT10)
    String segment10Code;

    // 계좌ID
    Integer bankAccountId;

    // 계좌명
    String bankAccountName;

    // Source 원정보 ID
    BigDecimal sourceSlipHeaderId;

    // PO번호
    String poNumber;

    // 전표표시대상 flag
    String slipDisplayFlag;

    String slipCreationTargetFlag;

    // 전표상태(진행단계)
    String slipStatus;

    // 수정/변경 가능 상태 여부(Y:불가,N:가능)
    String slipDataFixFlag;

    // 결재완료 Flag
    String approvalCompleteFlag;

    // 전표 생성 전 Validation Flag
    String createValidationFlag;

    // 전표생성 전 Validation 오류 내역
    String createValidationErrMsg;

    // Interface 여부 FLAG
    String slipIfFlag;

    // AP/AR 전표 ID
    BigDecimal stdInvoiceTrxId;

    // Interface 일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime slipIfDate;

    // Interface 전 마지막 승(검)인자
    String slipIfLastApprovalUser;

    // Interface 시 오류 내역
    String slipInterfaceErrorMsg;

    // 원장 전기 여부
    String postingFlag;

    // 원장 전기 일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime postingDate;

    // 지급/수금/어음만기 예정일 Update Flag
    String dueDateUpdateFlag;

    // 전자전표 삭제 여부
    String slipDeleteFlag;

    // 전자전표 삭제처리일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime slipDeleteDate;

    // ERP 전표 취소일자
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime erpSlipCancelDate;

    // ERP 전표 취소 처리 담당자
    String erpSlipCancelUser;

    String attributeCategory;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    String attribute6;

    String attribute7;

    String attribute8;

    String attribute9;

    String attribute10;

    String attribute11;

    String attribute12;

    String attribute13;

    String attribute14;

    String attribute15;

    String repaymentDueDate;

    String globalAttributeCategory;

    String globalAttribute1;

    String globalAttribute2;

    String globalAttribute3;

    String globalAttribute4;

    String globalAttribute5;

    String globalAttribute6;

    String globalAttribute7;

    String globalAttribute8;

    String globalAttribute9;

    String globalAttribute10;

    String globalAttribute11;

    String globalAttribute12;

    String globalAttribute13;

    String globalAttribute14;

    String globalAttribute15;

    String headerAttribute1;

    String headerAttribute2;

    String headerAttribute3;

    String headerAttribute4;

    String headerAttribute5;

    String headerAttribute6;

    String headerAttribute7;

    String headerAttribute8;

    String headerAttribute9;

    String headerAttribute10;

    Integer createdPersonId;

    String createdEmpNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creationDate;

    Integer lastUpdatedPersonId;

    String lastUpdatedEmpNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime lastUpdateDate;

    Integer lastUpdateLogin;

    // 그룹전표 여부
    String slipGroupYn;

    // 부가세액 검증 예외
    String vatAmtExceptionFlag;

    // 중간전송 여부
    String slipForcedIfFlag;

    String globalAttribute16;

    // 외근출장지역. 외근출장지역
    String tripPlace;

    // 출장시작일
    String tripFromDt;

    // 출장종료일
    String tripToDt;

    // 출장목적
    String tripObj;

    // 프로젝트번호
    String projectNo;

    // 출장코드
    String tripCd;

    // 임시필드1
    String temp1;

    // 자가운전 보조금 해당여부
    String temp2;

    // 외근신청서 작성여부
    String temp3;

    String temp4;

    String temp5;

    // CBO_SP_SLIP_HEADER 테이블
//    ErpSlipHeaderDto erpSlipHeaderDto;

    // 경조금 TB_SLIP_EXPEND_MONEY
    SlipExpendDto slipExpendDto;

    // 출장 리스트 TB_SLIP_BUSINESS_TRIP
    List<SlipBusinessTripDto> slipBusinessTripDtoList;

    // 교통비 TB_SLIP_TRAFFIC_HD
    SlipTrafficHdDto slipTrafficHdDto;

    // BOND 전표 헤더 TB_BOND_HD
    BondHeaderDto bondHeaderDto;

    List<SlipDetailDto> slipDetailDtoList = new ArrayList<>();

    String deptCd;

    public SlipDto(String taxLocationCode, String taxLocationName){
        this.taxLocationCode = taxLocationCode;
        this.taxLocationName = taxLocationName;
    }

}
