package com.iljin.apiServer.ijeas.slip.erpSubmit;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;

@NamedStoredProcedureQuery(
        name = "APPS.CBO_SP_SLIP_PKG.CREATE_SLIP_CONCURRENT",
        procedureName = "APPS.CBO_SP_SLIP_PKG.CREATE_SLIP_CONCURRENT",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = BigDecimal.class, name = "P_APPROVAL_GROUP_ID"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_SLIP_PROCESS"),
        }
)


@Entity
@NoArgsConstructor
@Table(name ="CREATE_SLIP_CONCURRENT")
@Data
public class CREATE_SLIP_CONCURRENT {
        @Id
        @Column(name = "P_APPROVAL_GROUP_ID")
        BigDecimal p_approval_group_id;

        @Column(name = "P_SLIP_PROCESS")
        String p_slip_process;
}
