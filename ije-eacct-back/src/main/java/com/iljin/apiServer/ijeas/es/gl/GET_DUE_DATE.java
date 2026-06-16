package com.iljin.apiServer.ijeas.es.gl;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_COMMON_PKG.GET_DUE_DATE",
        procedureName = "APPS.CBO_SP_COMMON_PKG.GET_DUE_DATE",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_INTERFACE_MODULE"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "P_TERM_ID"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = LocalDateTime.class, name = "P_GL_DATE"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_CALC_TYPE"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = LocalDateTime.class, name = "X_DUE_DATE"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "X_ERR_FLAG"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "X_ERR_STEP"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = String.class, name = "X_ERR_MSG"),
        }
)


@Entity
@NoArgsConstructor
@Table(name ="GET_DUE_DATE")
@Data
public class GET_DUE_DATE {

        @Column(name = "P_INTERFACE_MODULE")
        String pInterfaceModule;

        @Id
        @Column(name = "P_TERM_ID")
        BigDecimal pTermId;

        @Column(name = "P_GL_DATE")
        LocalDateTime pGlDate;

        @Column(name = "P_CALC_TYPE")
        String pCalcType;

        @Column(name = "X_DUE_DATE")
        LocalDateTime xDueDate;

        @Column(name = "X_ERR_FLAG")
        String xErrFlag;

        @Column(name = "X_ERR_STEP")
        String xErrStep;

        @Column(name = "X_ERR_MSG")
        String xErrMsg;
}
