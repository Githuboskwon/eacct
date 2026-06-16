package com.iljin.apiServer.ijeas.es.erpViews;

import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.ijeas.slip.SlipDetailDto;
import com.iljin.apiServer.ijeas.slip.SlipDto;
import com.iljin.apiServer.ijeas.slip.SlipTypeCd;
import com.iljin.apiServer.ijeas.slip.detail.SlipTrafficInfoDto;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CBO_SP_SLIP_LINE")
@Entity
public class ErpSlipLine {

    @Column(name = "SLIP_HEADER_ID", nullable = false)
    BigDecimal slipHeaderId;

    @Id
    @Column(name = "SLIP_LINE_ID", nullable = false)
    BigDecimal slipLineId;

    @Column(name = "SLIP_LINE_NUMBER", nullable = false)
    Integer slipLineNumber;

    @Column(name = "LINE_TYPE_LOOKUP_CODE", nullable = false)
    String lineTypeLookupCode;

    @Column(name = "ORG_ID", nullable = false)
    String orgId;

    @Column(name = "LEDGER_ID", nullable = false)
    BigDecimal ledgerId;

    @Column(name = "TTYPE_INTERFACE_MODULE", nullable = false)
    String ttypeInterfaceModule;

    @Column(name = "SOURCE")
    String source;

    @Column(name = "DR_CR", nullable = false)
    String drCr;

    @Column(name = "COMP_CODE", nullable = false)
    String compCode;

    @Column(name = "BUDGET_DEPT_CODE", nullable = false)
    String budgetDeptCode;

    @Column(name = "PROJECT_CODE")
    String projectCode;

    @Column(name = "PROJECT_ID")
    BigDecimal projectId;

    @Column(name = "TASK_CODE")
    String taskCode;

    @Column(name = "TASK_ID")
    BigDecimal taskId;

    @Column(name = "ITEM_GROUP_CODE", nullable = false)
    String itemGroupCode;

    @Column(name = "CODE_COMBINATION_ID")
    BigDecimal codeCombinationId;

    @Column(name = "ACCT_CODE")
    String acctCode;

    @Column(name = "INTERFACE_SLIP_TYPE", nullable = false)
    String interfaceSlipType;

    @Column(name = "ACTUAL_DEPT_CODE", nullable = false)
    String actualDeptCode;

    @Column(name = "TR_ACCT_CODE")
    String trAcctCode;

    @Column(name = "SLIP_TYPE_CODE")
    String slipTypeCode;

    @Column(name = "SEGMENT9_CODE")
    String segment9Code;

    @Column(name = "SEGMENT10_CODE")
    String segment10Code;

    @Column(name = "SUPPLY_AMOUNT")
    String supplyAmount;

    @Column(name = "ACCOUNTED_SUPPLY_AMOUNT")
    BigDecimal accountedSupplyAmount;

    @Column(name = "VAT_AMOUNT")
    BigDecimal vatAmount;

    @Column(name = "ACCOUNTED_VAT_AMOUNT")
    BigDecimal accountedVatAmount;

    @Column(name = "TAX_CODE")
    String taxCode;

    @Column(name = "TAX_ID")
    BigDecimal taxId;

    @Column(name = "TAX_ACCT_CODE")
    String taxAcctCode;

    @Column(name = "TAX_CODE_COMBINATION_ID")
    BigDecimal taxCodeCombinationId;

    @Column(name = "DESCRIPTION")
    String description;

    @Column(name = "CARD_USED_NO")
    String cardUsedNo;

    @Column(name = "QUANTITY_INVOICED")
    Integer quantityInvoiced;

    @Column(name = "UNIT_PRICE")
    Integer unitPrice;

    @Column(name = "ASSETS_TRACKING_FLAG")
    String assetsTrackingFlag;

    @Column(name = "PO_HEADER_ID")
    Integer poHeaderId;

    @Column(name = "PO_LINE_ID")
    Integer poLineId;

    @Column(name = "PO_LINE_LOCATION_ID")
    Integer poLineLocationId;

    @Column(name = "PO_DISTRIBUTION_ID")
    Integer poDistributionId;

    @Column(name = "RCV_TRANSACTION_ID")
    Integer rcvTransactionId;

    @Column(name = "PO_UNIT_OF_MEASURE")
    String poUnitOfMeasure;

    @Column(name = "INVENTORY_ITEM_ID")
    Integer inventoryItemId;

    @Column(name = "PREPAY_INVOICE_ID")
    Integer prepayInvoiceId;

    @Column(name = "PREPAY_LINE_NUMBER")
    Integer prepayLineNumber;

    @Column(name = "INVOICE_INCLUDES_PREPAY_FLAG")
    String invoiceIncludesPrepayFlag;

    @Column(name = "VALIDATION_FLAG")
    String validationFlag;

    @Column(name = "SLIP_INTERFACE_FLAG")
    String slipInterfaceFlag;

    @Column(name = "SLIP_INTERFACE_ERROR_MSG")
    String slipInterfaceErrorMsg;

    @Column(name = "ATTRIBUTE_CATEGORY")
    String attributeCategory;

    @Column(name = "ATTRIBUTE1")
    String attribute1;

    @Column(name = "ATTRIBUTE2")
    String attribute2;

    @Column(name = "ATTRIBUTE3")
    String attribute3;

    @Column(name = "ATTRIBUTE4")
    String attribute4;

    @Column(name = "ATTRIBUTE5")
    String attribute5;

    @Column(name = "ATTRIBUTE6")
    String attribute6;

    @Column(name = "ATTRIBUTE7")
    String attribute7;

    @Column(name = "ATTRIBUTE8")
    String attribute8;

    @Column(name = "ATTRIBUTE9")
    String attribute9;

    @Column(name = "ATTRIBUTE10")
    String attribute10;

    @Column(name = "ATTRIBUTE11")
    String attribute11;

    @Column(name = "ATTRIBUTE12")
    String attribute12;

    @Column(name = "ATTRIBUTE13")
    String attribute13;

    @Column(name = "ATTRIBUTE14")
    String attribute14;

    @Column(name = "ATTRIBUTE15")
    String attribute15;

    @Column(name = "GLOBAL_ATTRIBUTE_CATEGORY")
    String globalAttributeCategory;

    @Column(name = "GLOBAL_ATTRIBUTE1")
    String globalAttribute1;

    @Column(name = "GLOBAL_ATTRIBUTE2")
    String globalAttribute2;

    @Column(name = "GLOBAL_ATTRIBUTE3")
    String globalAttribute3;

    @Column(name = "GLOBAL_ATTRIBUTE4")
    String globalAttribute4;

    @Column(name = "GLOBAL_ATTRIBUTE5")
    String globalAttribute5;

    @Column(name = "GLOBAL_ATTRIBUTE6")
    String globalAttribute6;

    @Column(name = "GLOBAL_ATTRIBUTE7")
    String globalAttribute7;

    @Column(name = "GLOBAL_ATTRIBUTE8")
    String globalAttribute8;

    @Column(name = "GLOBAL_ATTRIBUTE9")
    String globalAttribute9;

    @Column(name = "GLOBAL_ATTRIBUTE10")
    String globalAttribute10;

    @Column(name = "GLOBAL_ATTRIBUTE11")
    String globalAttribute11;

    @Column(name = "GLOBAL_ATTRIBUTE12")
    String globalAttribute12;

    @Column(name = "GLOBAL_ATTRIBUTE13")
    String globalAttribute13;

    @Column(name = "GLOBAL_ATTRIBUTE14")
    String globalAttribute14;

    @Column(name = "GLOBAL_ATTRIBUTE15")
    String globalAttribute15;

    @Column(name = "GLOBAL_ATTRIBUTE16")
    String globalAttribute16;

    @Column(name = "GLOBAL_ATTRIBUTE17")
    String globalAttribute17;

    @Column(name = "GLOBAL_ATTRIBUTE18")
    String globalAttribute18;

    @Column(name = "GLOBAL_ATTRIBUTE19")
    String globalAttribute19;

    @Column(name = "GLOBAL_ATTRIBUTE20")
    String globalAttribute20;

    @Column(name = "LINE_ATTRIBUTE1")
    String lineAttribute1;

    @Column(name = "LINE_ATTRIBUTE2")
    String lineAttribute2;

    @Column(name = "LINE_ATTRIBUTE3")
    String lineAttribute3;

    @Column(name = "LINE_ATTRIBUTE4")
    String lineAttribute4;

    @Column(name = "LINE_ATTRIBUTE5")
    String lineAttribute5;

    @Column(name = "LINE_ATTRIBUTE6")
    String lineAttribute6;

    @Column(name = "LINE_ATTRIBUTE7")
    String lineAttribute7;

    @Column(name = "LINE_ATTRIBUTE8")
    String lineAttribute8;

    @Column(name = "LINE_ATTRIBUTE9")
    String lineAttribute9;

    @Column(name = "LINE_ATTRIBUTE10")
    String lineAttribute10;

    @Column(name = "CREATED_PERSON_ID", nullable = false)
    BigDecimal createdPersonId;

    @Column(name = "CREATED_EMP_NO")
    String createdEmpNo;

    @Column(name = "CREATION_DATE")
    LocalDateTime creationDate;

    @Column(name = "LAST_UPDATED_PERSON_ID", nullable = false)
    BigDecimal lastUpdatedPersonId;

    @Column(name = "LAST_UPDATED_EMP_NO")
    String lastUpdatedEmpNo;

    @Column(name = "LAST_UPDATE_DATE", nullable = false)
    LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATE_LOGIN")
    Integer lastUpdateLogin;

    @Column(name = "RECEIPT_NUMBER")
    String receiptNumber;

    @Column(name = "RECEIPT_LINE_NUMBER")
    String receiptLineNumber;

    @Column(name = "PO_RELEASE_ID")
    BigDecimal poReleaseId;

    @Column(name = "ORGANIZATION_ID")
    Integer organizationId;

    @Column(name = "ITEM_CODE")
    String itemCode;

    @Column(name = "LINE_GROUP_NUMBER")
    BigDecimal lineGroupNumber;

    @Column(name = "AWT_GROUP_ID")
    BigDecimal awtGroupId;


    public static ErpSlipLine from(SlipDto slipDto, SlipTrafficInfoDto infoDto, int slipLineNumber, int idx) {
        SlipDetailDto slipDetailDto = slipDto.getSlipDetailDtoList().get(idx);
        String attribute9 = "";
        if("SPAP167".equals(slipDto.getTrxTypeCode())) {
            attribute9 = slipDetailDto.getOAmtType();
        } else if ("SPAP170".equals(slipDto.getTrxTypeCode())) {
            attribute9 = slipDetailDto.getIAmtType();
        }
        return ErpSlipLine.builder()
            .slipHeaderId(slipDto.getSlipHeaderId())
            .slipLineId(infoDto.getSlipLineId())
            .slipLineNumber(slipLineNumber)
            .lineTypeLookupCode(slipDetailDto.getLineTypeLookupCode())
            .orgId(slipDto.getCompCd())
            .ledgerId(slipDetailDto.getLedgerId())
            .ttypeInterfaceModule(slipDto.getTtypeInterfaceModule())
            .compCode(slipDetailDto.getCompCode())
            .source(slipDetailDto.getSource())
            .budgetDeptCode(slipDetailDto.getBudgetDeptCode())
            .projectCode(slipDetailDto.getProjectCode())
            .projectId(slipDetailDto.getProjectId())
            .taskCode(slipDetailDto.getTaskCode())
            .taskId(slipDetailDto.getTaskId())
            .itemGroupCode(slipDetailDto.getItemGroupCode())
            .codeCombinationId(slipDetailDto.getCodeCombinationId())
            .acctCode(slipDetailDto.getAcctCode())
            .drCr(slipDetailDto.getDrCr())
            .interfaceSlipType(slipDto.getTtypeInterfaceSlipType())
            .actualDeptCode(slipDetailDto.getActualDeptCode())
            .trAcctCode(slipDetailDto.getTrAcctCode())
            .slipTypeCode(slipDetailDto.getSlipTypeCode())
            .segment9Code(slipDetailDto.getSegment9Code())
            .segment10Code(slipDetailDto.getSegment10Code())
            .supplyAmount(infoDto.getAmt())
            .accountedSupplyAmount(nonNull(infoDto.getAmt()) ? new BigDecimal(infoDto.getAmt()) : null)
            .vatAmount(slipDetailDto.getVatAmount())
            .accountedVatAmount(slipDetailDto.getAccountedVatAmount())
//            .vatAmount(new BigDecimal(0))
//            .accountedVatAmount(new BigDecimal(0))
            .taxCode(slipDetailDto.getTaxCode())
            .taxId(slipDetailDto.getTaxId())
            .taxAcctCode(slipDetailDto.getTaxAcctCode())
            .taxCodeCombinationId(slipDetailDto.getTaxCodeCombinationId())
            .description(infoDto.getRemark())
            .cardUsedNo(slipDetailDto.getCardUsedNo())
            .assetsTrackingFlag(slipDetailDto.getAssetsTrackingFlag())
            .validationFlag("N")
            .slipInterfaceFlag("N")
            .slipInterfaceErrorMsg(slipDetailDto.getSlipInterfaceErrorMsg())
            .attributeCategory(slipDetailDto.getAttributeCategory())
            .attribute1(slipDetailDto.getAttribute1())
            .attribute2(slipDetailDto.getAttribute2())
            .attribute3(slipDetailDto.getAttribute3())
            .attribute4(slipDetailDto.getAttribute4())
            .attribute5(slipDetailDto.getAttribute5())
            .attribute6(slipDetailDto.getC_type())
            .attribute7(slipDetailDto.getSubTrxType())
            .attribute8(slipDetailDto.getSubTrxTypeName())
            .attribute9(attribute9)
            .attribute10(slipDetailDto.getAttribute10())
            .attribute11(slipDetailDto.getAttribute11())
            .attribute12(slipDetailDto.getAttribute12())
            .attribute13(slipDetailDto.getAttribute13())
            .attribute14(slipDetailDto.getAttribute14())
            .attribute15(slipDetailDto.getAttribute15())
            .globalAttribute8(slipDetailDto.getAttribute8())
            .globalAttribute12(slipDetailDto.getGlobalAttribute12())
            .globalAttribute13(slipDetailDto.getGlobalAttribute13())
            .globalAttribute14(slipDetailDto.getGlobalAttribute14())
            .globalAttribute15(slipDetailDto.getGlobalAttribute15())
            .globalAttribute16(slipDetailDto.getGlobalAttribute16())
            .globalAttribute17(slipDetailDto.getGlobalAttribute17())
            .globalAttribute18(slipDetailDto.getGlobalAttribute18())
            .globalAttribute19(slipDetailDto.getGlobalAttribute19())
            .globalAttribute20(slipDetailDto.getGlobalAttribute20())
            .lineAttribute1(slipDetailDto.getLineAttribute1())
            .createdPersonId(new BigDecimal(slipDto.getPersonId()))
            .createdEmpNo(slipDto.getEmpNo())
            .creationDate(LocalDateTime.now())
            .lastUpdatedPersonId(new BigDecimal(slipDto.getPersonId()))
            .lastUpdatedEmpNo(slipDto.getEmpNo())
            .lastUpdateDate(LocalDateTime.now())
            .build();
    }

    public static ErpSlipLine from(SlipDto slipDto, BigDecimal slipLineId, int slipLineNumber, int idx) {
        SlipDetailDto slipDetailDto = slipDto.getSlipDetailDtoList().get(idx);
        String attribute9 = "";
        String attribute6 = slipDetailDto.getC_type();
        if("SPAP167".equals(slipDto.getTrxTypeCode())) {
            attribute9 = slipDetailDto.getOAmtType();
        } else if ("SPAP170".equals(slipDto.getTrxTypeCode())) {
            attribute9 = slipDetailDto.getIAmtType();
        }
        BigDecimal awtGroupId = slipDetailDto.getAwtGroupId();
        // 잡급비
        if(SlipTypeCd.ETCAWT.getCode().equals(slipDto.getSlipTypeCd()) || SlipTypeCd.AWT.getCode().equals(slipDto.getSlipTypeCd())) {
            attribute6 = slipDetailDto.getAttribute6();
            if(hasText(slipDetailDto.getWithholdingTaxCode())) {
                awtGroupId = new BigDecimal(slipDetailDto.getWithholdingTaxCode());
            }
        }

        // 잡급비가 아니면서 비어있을때
        if (attribute6 == null || attribute6.isEmpty()) {
            attribute6 = slipDetailDto.getAttribute6();
        }

        return ErpSlipLine.builder()
            .slipHeaderId(slipDto.getSlipHeaderId())
            .slipLineId(slipLineId)
            .slipLineNumber(slipLineNumber)
            .lineTypeLookupCode(slipDetailDto.getLineTypeLookupCode())
            .orgId(slipDto.getCompCd())
            .ledgerId(slipDetailDto.getLedgerId())
            .ttypeInterfaceModule(slipDto.getTtypeInterfaceModule())
            .source(slipDetailDto.getSource())
            .compCode(slipDetailDto.getCompCode())
            .budgetDeptCode(slipDetailDto.getBudgetDeptCode())
            .projectCode(slipDetailDto.getProjectCode())
            .projectId(slipDetailDto.getProjectId())
            .taskCode(slipDetailDto.getTaskCode())
            .taskId(slipDetailDto.getTaskId())
            .itemGroupCode(slipDetailDto.getItemGroupCode())
            .codeCombinationId(slipDetailDto.getCodeCombinationId())
            .acctCode(slipDetailDto.getAcctCode())
            .drCr(slipDetailDto.getDrCr())
            .interfaceSlipType(slipDto.getTtypeInterfaceSlipType())
            .actualDeptCode(slipDetailDto.getActualDeptCode())
            .trAcctCode(slipDetailDto.getTrAcctCode())
            .slipTypeCode(slipDetailDto.getSlipTypeCode())
            .segment9Code(slipDetailDto.getSegment9Code())
            .segment10Code(slipDetailDto.getSegment10Code())
            .supplyAmount(nonNull(slipDetailDto.getSupplyAmount()) ? slipDetailDto.getSupplyAmount().toString() : null)
            .accountedSupplyAmount(slipDetailDto.getAccountedSupplyAmount())
            .vatAmount(slipDetailDto.getVatAmount())
            .accountedVatAmount(slipDetailDto.getAccountedVatAmount())
            .taxCode(slipDetailDto.getTaxCode())
            .taxId(slipDetailDto.getTaxId())
            .taxAcctCode(slipDetailDto.getTaxAcctCode())
            .taxCodeCombinationId(slipDetailDto.getTaxCodeCombinationId())
            .description(slipDetailDto.getDescription())
            .cardUsedNo(slipDetailDto.getCardUsedNo())
            .assetsTrackingFlag(slipDetailDto.getAssetsTrackingFlag())
            .validationFlag("N")
            .slipInterfaceFlag("N")
            .slipInterfaceErrorMsg(slipDetailDto.getSlipInterfaceErrorMsg())
            .attributeCategory(slipDetailDto.getAttributeCategory())
            .attribute1(slipDetailDto.getAttribute1())
            .attribute2(slipDetailDto.getAttribute2())
            .attribute3(slipDetailDto.getAttribute3())
            .attribute4(slipDetailDto.getAttribute4())
            .attribute5(slipDetailDto.getAttribute5())
            .attribute6(attribute6)
            .attribute7(slipDetailDto.getSubTrxType())
            .attribute8(slipDetailDto.getSubTrxTypeName())
            .attribute9(attribute9)
            .attribute10(slipDetailDto.getAttribute10())
            .attribute11(slipDetailDto.getAttribute11())
            .attribute12(slipDetailDto.getAttribute12())
            .attribute13(slipDetailDto.getAttribute13())
            .attribute14(slipDetailDto.getAttribute14())
            .attribute15(slipDetailDto.getAttribute15())
            .globalAttribute8(slipDetailDto.getGlobalAttribute8())
            .globalAttribute12(slipDetailDto.getGlobalAttribute12())
            .globalAttribute13(slipDetailDto.getGlobalAttribute13())
            .globalAttribute14(slipDetailDto.getGlobalAttribute14())
            .globalAttribute15(slipDetailDto.getGlobalAttribute15())
            .globalAttribute16(slipDetailDto.getGlobalAttribute16())
            .globalAttribute17(slipDetailDto.getGlobalAttribute17())
            .globalAttribute18(slipDetailDto.getGlobalAttribute18())
            .globalAttribute19(slipDetailDto.getGlobalAttribute19())
            .globalAttribute20(slipDetailDto.getGlobalAttribute20())
            .lineAttribute1(slipDetailDto.getLineAttribute1())
            .createdPersonId(new BigDecimal(slipDto.getPersonId()))
            .createdEmpNo(slipDto.getEmpNo())
            .creationDate(LocalDateTime.now())
            .lastUpdatedPersonId(new BigDecimal(slipDto.getPersonId()))
            .lastUpdatedEmpNo(slipDto.getEmpNo())
            .lastUpdateDate(LocalDateTime.now())
            .awtGroupId(awtGroupId)
            .build();
    }

    public static ErpSlipLine copy(BigDecimal slipHeaderId, BigDecimal slipLineId, ErpSlipLine erpSlipLine, Employee emp) {
        return ErpSlipLine.builder()
            .slipHeaderId(slipHeaderId)
            .slipLineId(slipLineId)
            .slipLineNumber(erpSlipLine.getSlipLineNumber())
            .lineTypeLookupCode(erpSlipLine.getLineTypeLookupCode())
            .orgId(erpSlipLine.getOrgId())
            .ledgerId(erpSlipLine.getLedgerId())
            .ttypeInterfaceModule(erpSlipLine.getTtypeInterfaceModule())
            .source(erpSlipLine.getSource())
            .compCode(erpSlipLine.getCompCode())
            .budgetDeptCode(erpSlipLine.getBudgetDeptCode())
            .projectCode(erpSlipLine.getProjectCode())
            .projectId(erpSlipLine.getProjectId())
            .taskCode(erpSlipLine.getTaskCode())
            .taskId(erpSlipLine.getTaskId())
            .itemGroupCode(erpSlipLine.getItemGroupCode())
            .codeCombinationId(erpSlipLine.getCodeCombinationId())
            .acctCode(erpSlipLine.getAcctCode())
            .drCr(erpSlipLine.getDrCr())
            .interfaceSlipType(erpSlipLine.getInterfaceSlipType())
            .actualDeptCode(erpSlipLine.getActualDeptCode())
            .trAcctCode(erpSlipLine.getTrAcctCode())
            .slipTypeCode(erpSlipLine.getSlipTypeCode())
            .segment9Code(erpSlipLine.getSegment9Code())
            .segment10Code(erpSlipLine.getSegment10Code())
            .supplyAmount(erpSlipLine.getSupplyAmount())
            .accountedSupplyAmount(erpSlipLine.getAccountedSupplyAmount())
            .vatAmount(erpSlipLine.getVatAmount())
            .accountedVatAmount(erpSlipLine.getAccountedVatAmount())
            .taxCode(erpSlipLine.getTaxCode())
            .taxId(erpSlipLine.getTaxId())
            .taxAcctCode(erpSlipLine.getTaxAcctCode())
            .taxCodeCombinationId(erpSlipLine.getTaxCodeCombinationId())
            .description(erpSlipLine.getDescription())
            .cardUsedNo(erpSlipLine.getCardUsedNo())
            .assetsTrackingFlag(erpSlipLine.getAssetsTrackingFlag())
            .validationFlag("N")
            .slipInterfaceFlag("N")
            .slipInterfaceErrorMsg(erpSlipLine.getSlipInterfaceErrorMsg())
            .attributeCategory(erpSlipLine.getAttributeCategory())
            .attribute1(erpSlipLine.getAttribute1())
            .attribute2(erpSlipLine.getAttribute2())
            .attribute3(erpSlipLine.getAttribute3())
            .attribute4(erpSlipLine.getAttribute4())
            .attribute5(erpSlipLine.getAttribute5())
            .attribute6(erpSlipLine.getAttribute6())
            .attribute7(erpSlipLine.getAttribute7())
            .attribute8(erpSlipLine.getAttribute8())
            .attribute9(erpSlipLine.getAttribute9())
            .attribute10(erpSlipLine.getAttribute10())
            .attribute11(erpSlipLine.getAttribute11())
            .attribute12(erpSlipLine.getAttribute12())
            .attribute13(erpSlipLine.getAttribute13())
            .attribute14(erpSlipLine.getAttribute14())
            .attribute15(erpSlipLine.getAttribute15())
            .globalAttribute8(erpSlipLine.getGlobalAttribute8())
            .globalAttribute12(erpSlipLine.getGlobalAttribute12())
            .globalAttribute13(erpSlipLine.getGlobalAttribute13())
            .globalAttribute14(erpSlipLine.getGlobalAttribute14())
            .globalAttribute15(erpSlipLine.getGlobalAttribute15())
            .globalAttribute16(erpSlipLine.getGlobalAttribute16())
            .globalAttribute17(erpSlipLine.getGlobalAttribute17())
            .globalAttribute18(erpSlipLine.getGlobalAttribute18())
            .globalAttribute19(erpSlipLine.getGlobalAttribute19())
            .globalAttribute20(erpSlipLine.getGlobalAttribute20())
            .lineAttribute1(erpSlipLine.getLineAttribute1())
            .createdPersonId(new BigDecimal(emp.getPersonId()))
            .createdEmpNo(emp.getEmpNo())
            .creationDate(LocalDateTime.now())
            .lastUpdatedPersonId(new BigDecimal(emp.getPersonId()))
            .lastUpdatedEmpNo(emp.getEmpNo())
            .lastUpdateDate(LocalDateTime.now())
            .awtGroupId(erpSlipLine.getAwtGroupId())
            .build();
    }

}
