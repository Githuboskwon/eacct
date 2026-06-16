package com.iljin.apiServer.ijeas.system.dff;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;


@NamedStoredProcedureQuery(
        name = "APPS.CBO_GL_DFF_PKG.GET_DFF_VALUESET",
        procedureName = "APPS.CBO_GL_DFF_PKG.GET_DFF_VALUESET",
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = Integer.class, name = "P_VALUE_SET_ID"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_VALIDATION_TYPE"),
                @StoredProcedureParameter(mode = ParameterMode.OUT, type = Integer.class, name = "P_TOTAL_COUNT")
        }
)


@Getter
@NoArgsConstructor
@IdClass(DffKey.class)
@Table(name = "TB_MST_MNG_ITEM")
@Entity
public class Dff extends BaseEntity {
    @Id
    @Column(name = "APPLICATION_SHORT_CD")
    String applicationShortCd;

    @Id
    @Column(name = "ACCT_CD")
    String acctCd;

    @Id
    @Column(name = "MNG_ITEM_CD")
    String mngItemCd;

    @Column(name = "MNG_ITEM_NM")
    String mngItemNm;

    @Column(name = "MNG_ITEM_TYPE_CD")
    String mngItemTypeCd;

    @Column(name = "COMP_CD")
    String compCd;

    @Column(name = "DEFAULT_VALUE")
    String defaultValue;

    @Column(name = "USE_CD")
    String useCd;

    @Column(name = "FIX_LEN")
    Integer fixLen;

    @Column(name = "H_ALIGN_CD")
    String hAlignCd;

    @Column(name = "ORDER_SEQ")
    Integer orderSeq;

    @Column(name = "USE_YN")
    String useYn;

    @Column(name = "REQUIRED_YN")
    String requiredYn;

    @Column(name = "VALIDATION_TYPE")
    String validationType;

    @Column(name = "FLEX_VALUE_SET_ID")
    Integer flexValueSetId;

    @Column(name = "TREE_YN")
    String treeYn;

    @Column(name = "TREE_CD")
    String treeCd;

    @Column(name = "TREE_SEQ")
    String treeSeq;


}
