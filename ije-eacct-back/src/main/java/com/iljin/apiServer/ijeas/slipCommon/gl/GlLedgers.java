package com.iljin.apiServer.ijeas.slipCommon.gl;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "GL_LEDGERS")
public class GlLedgers {

	@Id
	@Column(name = "LEDGER_ID" , nullable=false)
	Integer ledgerId;

	@Column(name = "NAME" , nullable=false)
	String name;

	@Column(name = "SHORT_NAME" , nullable=false)
	String shortName;

	@Column(name = "DESCRIPTION")
	String description;

	@Column(name = "LEDGER_CATEGORY_CODE" , nullable=false)
	String ledgerCategoryCode;

	@Column(name = "ALC_LEDGER_TYPE_CODE" , nullable=false)
	String alcLedgerTypeCode;

	@Column(name = "OBJECT_TYPE_CODE" , nullable=false)
	String objectTypeCode;

	@Column(name = "LE_LEDGER_TYPE_CODE" , nullable=false)
	String leLedgerTypeCode;

	@Column(name = "COMPLETION_STATUS_CODE")
	String completionStatusCode;

	@Column(name = "CONFIGURATION_ID")
	Integer configurationId;

	@Column(name = "CHART_OF_ACCOUNTS_ID" , nullable=false)
	BigDecimal chartOfAccountsId;

	@Column(name = "CURRENCY_CODE" , nullable=false)
	String currencyCode;

	@Column(name = "PERIOD_SET_NAME" , nullable=false)
	String periodSetName;

	@Column(name = "ACCOUNTED_PERIOD_TYPE" , nullable=false)
	String accountedPeriodType;

	@Column(name = "FIRST_LEDGER_PERIOD_NAME" , nullable=false)
	String firstLedgerPeriodName;

	@Column(name = "RET_EARN_CODE_COMBINATION_ID" , nullable=false)
	BigDecimal retEarnCodeCombinationId;

	@Column(name = "SUSPENSE_ALLOWED_FLAG" , nullable=false)
	String suspenseAllowedFlag;

	@Column(name = "ALLOW_INTERCOMPANY_POST_FLAG" , nullable=false)
	String allowIntercompanyPostFlag;

	@Column(name = "TRACK_ROUNDING_IMBALANCE_FLAG" , nullable=false)
	String trackRoundingImbalanceFlag;

	@Column(name = "ENABLE_AVERAGE_BALANCES_FLAG" , nullable=false)
	String enableAverageBalancesFlag;

	@Column(name = "CUM_TRANS_CODE_COMBINATION_ID")
	BigDecimal cumTransCodeCombinationId;

	@Column(name = "RES_ENCUMB_CODE_COMBINATION_ID")
	BigDecimal resEncumbCodeCombinationId;

	@Column(name = "NET_INCOME_CODE_COMBINATION_ID")
	BigDecimal netIncomeCodeCombinationId;

	@Column(name = "ROUNDING_CODE_COMBINATION_ID")
	BigDecimal roundingCodeCombinationId;

	@Column(name = "ENABLE_BUDGETARY_CONTROL_FLAG" , nullable=false)
	String enableBudgetaryControlFlag;

	@Column(name = "REQUIRE_BUDGET_JOURNALS_FLAG" , nullable=false)
	String requireBudgetJournalsFlag;

	@Column(name = "ENABLE_JE_APPROVAL_FLAG" , nullable=false)
	String enableJeApprovalFlag;

	@Column(name = "ENABLE_AUTOMATIC_TAX_FLAG" , nullable=false)
	String enableAutomaticTaxFlag;

	@Column(name = "CONSOLIDATION_LEDGER_FLAG" , nullable=false)
	String consolidationLedgerFlag;

	@Column(name = "TRANSLATE_EOD_FLAG" , nullable=false)
	String translateEodFlag;

	@Column(name = "TRANSLATE_QATD_FLAG" , nullable=false)
	String translateQatdFlag;

	@Column(name = "TRANSLATE_YATD_FLAG" , nullable=false)
	String translateYatdFlag;

	@Column(name = "TRANSACTION_CALENDAR_ID")
	BigDecimal transactionCalendarId;

	@Column(name = "DAILY_TRANSLATION_RATE_TYPE")
	String dailyTranslationRateType;

	@Column(name = "AUTOMATICALLY_CREATED_FLAG" , nullable=false)
	String automaticallyCreatedFlag;

	@Column(name = "BAL_SEG_VALUE_OPTION_CODE" , nullable=false)
	String balSegValueOptionCode;

	@Column(name = "BAL_SEG_COLUMN_NAME" , nullable=false)
	String balSegColumnName;

	@Column(name = "MGT_SEG_VALUE_OPTION_CODE" , nullable=false)
	String mgtSegValueOptionCode;

	@Column(name = "MGT_SEG_COLUMN_NAME")
	String mgtSegColumnName;

	@Column(name = "BAL_SEG_VALUE_SET_ID" , nullable=false)
	BigDecimal balSegValueSetId;

	@Column(name = "MGT_SEG_VALUE_SET_ID")
	BigDecimal mgtSegValueSetId;

	@Column(name = "IMPLICIT_ACCESS_SET_ID")
	BigDecimal implicitAccessSetId;

	@Column(name = "CRITERIA_SET_ID")
	BigDecimal criteriaSetId;

	@Column(name = "FUTURE_ENTERABLE_PERIODS_LIMIT" , nullable=false)
	Integer futureEnterablePeriodsLimit;

	@Column(name = "LEDGER_ATTRIBUTES" , nullable=false)
	String ledgerAttributes;

	@Column(name = "IMPLICIT_LEDGER_SET_ID")
	BigDecimal implicitLedgerSetId;

	@Column(name = "LATEST_OPENED_PERIOD_NAME")
	String latestOpenedPeriodName;

	@Column(name = "LATEST_ENCUMBRANCE_YEAR")
	Integer latestEncumbranceYear;

	@Column(name = "PERIOD_AVERAGE_RATE_TYPE")
	String periodAverageRateType;

	@Column(name = "PERIOD_END_RATE_TYPE")
	String periodEndRateType;

	@Column(name = "BUDGET_PERIOD_AVG_RATE_TYPE")
	String budgetPeriodAvgRateType;

	@Column(name = "BUDGET_PERIOD_END_RATE_TYPE")
	String budgetPeriodEndRateType;

	@Column(name = "SLA_ACCOUNTING_METHOD_CODE")
	String slaAccountingMethodCode;

	@Column(name = "SLA_ACCOUNTING_METHOD_TYPE")
	String slaAccountingMethodType;

	@Column(name = "SLA_DESCRIPTION_LANGUAGE")
	String slaDescriptionLanguage;

	@Column(name = "SLA_ENTERED_CUR_BAL_SUS_CCID")
	String slaEnteredCurBalSusCcid;

	@Column(name = "SLA_SEQUENCING_FLAG")
	String slaSequencingFlag;

	@Column(name = "SLA_BAL_BY_LEDGER_CURR_FLAG")
	String slaBalByLedgerCurrFlag;

	@Column(name = "SLA_LEDGER_CUR_BAL_SUS_CCID")
	BigDecimal slaLedgerCurBalSusCcid;

	@Column(name = "ENABLE_SECONDARY_TRACK_FLAG")
	String enableSecondaryTrackFlag;

	@Column(name = "ENABLE_REVAL_SS_TRACK_FLAG")
	String enableRevalSsTrackFlag;

	@Column(name = "LAST_UPDATE_DATE" , nullable=false)
	LocalDateTime lastUpdateDate;

	@Column(name = "LAST_UPDATED_BY" , nullable=false)
	BigDecimal lastUpdatedBy;

	@Column(name = "CREATION_DATE" , nullable=false)
	LocalDateTime creationDate;

	@Column(name = "CREATED_BY" , nullable=false)
	BigDecimal createdBy;

	@Column(name = "LAST_UPDATE_LOGIN")
	BigDecimal lastUpdateLogin;

	@Column(name = "CONTEXT")
	String context;

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

	@Column(name = "ENABLE_RECONCILIATION_FLAG" , nullable=false)
	String enableReconciliationFlag;

	@Column(name = "CREATE_JE_FLAG" , nullable=false)
	String createJeFlag;

	@Column(name = "SLA_LEDGER_CASH_BASIS_FLAG")
	String slaLedgerCashBasisFlag;

	@Column(name = "COMPLETE_FLAG")
	String completeFlag;

	@Column(name = "COMMITMENT_BUDGET_FLAG")
	String commitmentBudgetFlag;

	@Column(name = "NET_CLOSING_BAL_FLAG" , nullable=false)
	String netClosingBalFlag;

	@Column(name = "AUTOMATE_SEC_JRNL_REV_FLAG")
	String automateSecJrnlRevFlag;



	@Builder
	public GlLedgers(Integer ledgerId,String name,String shortName,String description,String ledgerCategoryCode,String alcLedgerTypeCode,
					 String objectTypeCode,String leLedgerTypeCode,String completionStatusCode,Integer configurationId,BigDecimal chartOfAccountsId,
					 String currencyCode,String periodSetName,String accountedPeriodType,String firstLedgerPeriodName,BigDecimal retEarnCodeCombinationId,
					 String suspenseAllowedFlag,String allowIntercompanyPostFlag,String trackRoundingImbalanceFlag,String enableAverageBalancesFlag,
					 BigDecimal cumTransCodeCombinationId,BigDecimal resEncumbCodeCombinationId,BigDecimal netIncomeCodeCombinationId,BigDecimal roundingCodeCombinationId,
					 String enableBudgetaryControlFlag,String requireBudgetJournalsFlag,String enableJeApprovalFlag,String enableAutomaticTaxFlag,
					 String consolidationLedgerFlag,String translateEodFlag,String translateQatdFlag,String translateYatdFlag,BigDecimal transactionCalendarId,
					 String dailyTranslationRateType,String automaticallyCreatedFlag,String balSegValueOptionCode,String balSegColumnName,String mgtSegValueOptionCode,
					 String mgtSegColumnName,BigDecimal balSegValueSetId,BigDecimal mgtSegValueSetId,BigDecimal implicitAccessSetId,BigDecimal criteriaSetId,
					 Integer futureEnterablePeriodsLimit,String ledgerAttributes,BigDecimal implicitLedgerSetId,String latestOpenedPeriodName,Integer latestEncumbranceYear,
					 String periodAverageRateType,String periodEndRateType,String budgetPeriodAvgRateType,String budgetPeriodEndRateType,String slaAccountingMethodCode,
					 String slaAccountingMethodType,String slaDescriptionLanguage,String slaEnteredCurBalSusCcid,String slaSequencingFlag,String slaBalByLedgerCurrFlag,
					 BigDecimal slaLedgerCurBalSusCcid,String enableSecondaryTrackFlag,String enableRevalSsTrackFlag,LocalDateTime lastUpdateDate,BigDecimal lastUpdatedBy,
					 LocalDateTime creationDate,BigDecimal createdBy,BigDecimal lastUpdateLogin,String context,String attribute1,String attribute2,String attribute3,
					 String attribute4,String attribute5,String attribute6,String attribute7,String attribute8,String attribute9,String attribute10,String attribute11,
					 String attribute12,String attribute13,String attribute14,String attribute15,String enableReconciliationFlag,String createJeFlag,String slaLedgerCashBasisFlag,
					 String completeFlag,String commitmentBudgetFlag,String netClosingBalFlag,String automateSecJrnlRevFlag) {
		this.ledgerId = ledgerId;
		this.name = name;
		this.shortName = shortName;
		this.description = description;
		this.ledgerCategoryCode = ledgerCategoryCode;
		this.alcLedgerTypeCode = alcLedgerTypeCode;
		this.objectTypeCode = objectTypeCode;
		this.leLedgerTypeCode = leLedgerTypeCode;
		this.completionStatusCode = completionStatusCode;
		this.configurationId = configurationId;
		this.chartOfAccountsId = chartOfAccountsId;
		this.currencyCode = currencyCode;
		this.periodSetName = periodSetName;
		this.accountedPeriodType = accountedPeriodType;
		this.firstLedgerPeriodName = firstLedgerPeriodName;
		this.retEarnCodeCombinationId = retEarnCodeCombinationId;
		this.suspenseAllowedFlag = suspenseAllowedFlag;
		this.allowIntercompanyPostFlag = allowIntercompanyPostFlag;
		this.trackRoundingImbalanceFlag = trackRoundingImbalanceFlag;
		this.enableAverageBalancesFlag = enableAverageBalancesFlag;
		this.cumTransCodeCombinationId = cumTransCodeCombinationId;
		this.resEncumbCodeCombinationId = resEncumbCodeCombinationId;
		this.netIncomeCodeCombinationId = netIncomeCodeCombinationId;
		this.roundingCodeCombinationId = roundingCodeCombinationId;
		this.enableBudgetaryControlFlag = enableBudgetaryControlFlag;
		this.requireBudgetJournalsFlag = requireBudgetJournalsFlag;
		this.enableJeApprovalFlag = enableJeApprovalFlag;
		this.enableAutomaticTaxFlag = enableAutomaticTaxFlag;
		this.consolidationLedgerFlag = consolidationLedgerFlag;
		this.translateEodFlag = translateEodFlag;
		this.translateQatdFlag = translateQatdFlag;
		this.translateYatdFlag = translateYatdFlag;
		this.transactionCalendarId = transactionCalendarId;
		this.dailyTranslationRateType = dailyTranslationRateType;
		this.automaticallyCreatedFlag = automaticallyCreatedFlag;
		this.balSegValueOptionCode = balSegValueOptionCode;
		this.balSegColumnName = balSegColumnName;
		this.mgtSegValueOptionCode = mgtSegValueOptionCode;
		this.mgtSegColumnName = mgtSegColumnName;
		this.balSegValueSetId = balSegValueSetId;
		this.mgtSegValueSetId = mgtSegValueSetId;
		this.implicitAccessSetId = implicitAccessSetId;
		this.criteriaSetId = criteriaSetId;
		this.futureEnterablePeriodsLimit = futureEnterablePeriodsLimit;
		this.ledgerAttributes = ledgerAttributes;
		this.implicitLedgerSetId = implicitLedgerSetId;
		this.latestOpenedPeriodName = latestOpenedPeriodName;
		this.latestEncumbranceYear = latestEncumbranceYear;
		this.periodAverageRateType = periodAverageRateType;
		this.periodEndRateType = periodEndRateType;
		this.budgetPeriodAvgRateType = budgetPeriodAvgRateType;
		this.budgetPeriodEndRateType = budgetPeriodEndRateType;
		this.slaAccountingMethodCode = slaAccountingMethodCode;
		this.slaAccountingMethodType = slaAccountingMethodType;
		this.slaDescriptionLanguage = slaDescriptionLanguage;
		this.slaEnteredCurBalSusCcid = slaEnteredCurBalSusCcid;
		this.slaSequencingFlag = slaSequencingFlag;
		this.slaBalByLedgerCurrFlag = slaBalByLedgerCurrFlag;
		this.slaLedgerCurBalSusCcid = slaLedgerCurBalSusCcid;
		this.enableSecondaryTrackFlag = enableSecondaryTrackFlag;
		this.enableRevalSsTrackFlag = enableRevalSsTrackFlag;
		this.lastUpdateDate = lastUpdateDate;
		this.lastUpdatedBy = lastUpdatedBy;
		this.creationDate = creationDate;
		this.createdBy = createdBy;
		this.lastUpdateLogin = lastUpdateLogin;
		this.context = context;
		this.attribute1 = attribute1;
		this.attribute2 = attribute2;
		this.attribute3 = attribute3;
		this.attribute4 = attribute4;
		this.attribute5 = attribute5;
		this.attribute6 = attribute6;
		this.attribute7 = attribute7;
		this.attribute8 = attribute8;
		this.attribute9 = attribute9;
		this.attribute10 = attribute10;
		this.attribute11 = attribute11;
		this.attribute12 = attribute12;
		this.attribute13 = attribute13;
		this.attribute14 = attribute14;
		this.attribute15 = attribute15;
		this.enableReconciliationFlag = enableReconciliationFlag;
		this.createJeFlag = createJeFlag;
		this.slaLedgerCashBasisFlag = slaLedgerCashBasisFlag;
		this.completeFlag = completeFlag;
		this.commitmentBudgetFlag = commitmentBudgetFlag;
		this.netClosingBalFlag = netClosingBalFlag;
		this.automateSecJrnlRevFlag = automateSecJrnlRevFlag;
	}
}
