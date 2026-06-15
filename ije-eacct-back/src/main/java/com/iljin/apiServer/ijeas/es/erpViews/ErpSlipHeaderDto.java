package com.iljin.apiServer.ijeas.es.erpViews;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ErpSlipHeaderDto implements Serializable {

    private static final long serialVersionUID = 1362934925090213230L;

    // 전자전표 Header ID
    BigDecimal slipHeaderId;

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

    // 장부ID
    Integer ledgerId;

    // Header 법인코드(SEGMENT1)
    String compCode;

    // Header 예산부서코드(SEGMENT2)
    String budgetDeptCode;

    // Header 프로젝트코드(SEGMENT6)
    String projectCode;

    // Header 프로젝트 ID
    Integer projectId;

    // Header Task코드
    String taskCode;

    // Header Task ID
    String taskId;

    // Header 제품군코드(SEGMENT5)
    String itemGroupCode;

    // Header CCID
    Integer codeCombinationId;

    // 차대변 구분
    String drCr;

    // Header 계정코드(SEGMENT4)
    String acctCode;

    // 회계일자
    LocalDateTime glDate;

    // 전표일자(invoice_date, trx_date)
    LocalDateTime slipDate;

    // 세무증빙유형코드
    String taxEvidenceType;

    // 통화코드
    String slipCurrencyCode;

    // 환율유형
    String exchangeRateType;

    // 환율일자
    LocalDateTime exchangeDate;

    // 환율
    Integer exchangeRate;

    // 전표금액(입력)
    Integer enteredAmount;

    // 전표금액(원화)
    Integer accountedAmount;

    // 연관전표모듈
    String referenceSlipModule;

    // 연관전표번호
    String referenceSlipNumber;

    // 적요
    String description;

    // 통합거래처번호
    String integrationVendorNum;

    Integer vendorId;

    Integer vendorSiteId;

    Integer vendorPartyId;

    Integer vendorPartySiteId;

    Integer customerId;

    Integer customerSiteId;

    Integer customerPartyId;

    Integer customerPartySiteId;

    // 수금/지급방법
    String termName;

    // 수금/지급 방법ID
    Integer termId;

    // 수금/지급 예정일자
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
    LocalDateTime maturityDate;

    // 원천세ID
    Integer awtGroupId;

    // 원천세 사업장코드
    String awtLocationCode;

    // Header 부가세 사업장코드
    String taxLocationCode;

    // 발행구분코드(정/역발행)
    String taxIssueTypeCode;

    // 부가세코드
    String taxCode;

    // 부가세코드 ID
    Integer vatTaxId;

    // 부가세계정코드(SEGMENT4)
    String taxAcctCode;

    // Header TAX CCID
    Integer taxCodeCombinationId;

    // 증빙거래처번호
    String evidenceVendorNum;

    // 증빙거래처ID
    Integer evidenceVendorCustSiteId;

    // 증빙작성일자
    LocalDateTime evidenceDate;

    // 세금계산서번호(스마트빌 No)
    String taxSmartbillNo;

    // 공급가액
    Integer supplyAmount;

    // 부가세액
    Integer taxAmount;

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
    Integer sourceSlipHeaderId;

    // PO번호
    String poNumber;

    // 전표표시대상 flag
    String slipDisplayFlag;

    String slipCreationTargetFlag;

    // 전표상태(진행단계)
    String slipStatus;

    // 검증여부 FALG
    String validationFlag;

    // 수정/변경 가능 상태 여부(Y:불가,N:가능)
    String slipDataFixFlag;

    // 결재그룹번호
    Integer approvalGroupId;

    // 결재완료 Flag
    String approvalCompleteFlag;

    // 전표 생성 전 Validation Flag
    String createValidationFlag;

    // 전표생성 전 Validation 오류 내역
    String createValidationErrMsg;

    // Interface 여부 FLAG
    String slipIfFlag;

    // AP/AR 전표 ID
    Integer stdInvoiceTrxId;

    // Interface 일자
    LocalDateTime slipIfDate;

    // Interface 전 마지막 승(검)인자
    String slipIfLastApprovalUser;

    // Interface 시 오류 내역
    String slipInterfaceErrorMsg;

    // 원장 전기 여부
    String postingFlag;

    // 원장 전기 일자
    LocalDateTime postingDate;

    // 지급/수금/어음만기 예정일 Update Flag
    String dueDateUpdateFlag;

    // 전자전표 삭제 여부
    String slipDeleteFlag;

    // 전자전표 삭제처리일자
    LocalDateTime slipDeleteDate;

    // ERP 전표 취소일자
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

    LocalDateTime creationDate;

    Integer lastUpdatedPersonId;

    String lastUpdatedEmpNo;

    LocalDateTime lastUpdateDate;

    Integer lastUpdateLogin;

    // 증빙 Key
    String scanNo;

    // 부가세액 검증 예외
    String vatAmtExceptionFlag;

    // 중간전송 여부
    String slipForcedIfFlag;

    String globalAttribute16;

    @QueryProjection
    public ErpSlipHeaderDto(String slipIfFlag, String trxTypeCode, String slipDataFixFlag, String slipForcedIfFlag, String approvalCompleteFlag){
        this.slipIfFlag = slipIfFlag;
        this.trxTypeCode = trxTypeCode;
        this.slipDataFixFlag = slipDataFixFlag;
        this.slipForcedIfFlag = slipForcedIfFlag;
        this.approvalCompleteFlag = approvalCompleteFlag;
    }

    @QueryProjection
    public ErpSlipHeaderDto(String slipIfFlag, String approvalCompleteFlag, String slipForcedIfFlag){
        this.slipIfFlag = slipIfFlag;
        this.slipForcedIfFlag = slipForcedIfFlag;
        this.approvalCompleteFlag = approvalCompleteFlag;
    }
}
