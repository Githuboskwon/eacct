package com.iljin.apiServer.ijeas.costBudget;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@NamedStoredProcedureQuery(
        /* as-is GET_SP_BDEPT_CODE_P */
        name = "APPS.CBO_SP_BD_VS_ACT_PKG.GET_SP_CCTR_CD_SPLIT_P",
        procedureName = "APPS.CBO_SP_BD_VS_ACT_PKG.GET_SP_CCTR_CD_SPLIT_P",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "P_LEDGER_ID"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_PERIOD_YEAR"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_PERIOD_MONTH"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_CCTR_CD"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_GET_TYPE"),
        }
)


@Entity
@NoArgsConstructor
@Table(name ="GET_SP_CCTR_CD_SPLIT_P")
@Data
public class GET_SP_CCTR_CD_SPLIT_P {
        @Id
        @Column(name = "P_LEDGER_ID")
        BigDecimal pLedgerId;

        @Column(name = "P_PERIOD_YEAR")
        String pPeriodYear;

        @Column(name = "P_PERIOD_MONTH")
        String pPeriodMonth;

        @Column(name = "P_CCTR_CD")
        String pCctrCd;

        @Column(name = "P_GET_TYPE")
        String pGetType;
}
