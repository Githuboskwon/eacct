package com.iljin.apiServer.ijeas.system.acct;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import jakarta.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "CBO_SP_DFF_COLUMN_V")
public class CboSpDffColumn implements Serializable {
    private static final long serialVersionUID = 1114008246179015839L;
    @Id
    @Column(name = "DESCRIPTIVE_FLEXFIELD_NAME")
    String descriptiveFlexfieldName;

    @Id
    @Column(name = "COLUMN_SEQ_NUM")
    Integer columnSeqnum;

    @Column(name = "APPLICATION_SHORT_NAME")
    String applicationShortName;

    @Column(name = "CONTEXT_CODE")
    String contextCode;

    @Column(name = "COLUMN_NAME")
    String columnName;

    @Column(name = "FORM_PROMPT")
    String formPrompt;

    @Column(name = "END_USER_COLUMN_NAME")
    String endUserColumnName;

    @Column(name = "DEFAULT_VALUE")
    String defaultValue;

    @Column(name = "REQUIRED_FLAG")
    String requiredFlag;

    @Column(name = "VALUE_SET_NAME")
    String valueSetName;

    @Column(name = "VALUE_SET_TYPE")
    String valueSetType;

    @Column(name = "MAXIMUM_SIZE")
    Integer maximunSize;

    @Column(name = "VALIDATION_TYPE")
    String validationType;

    @Column(name = "FORMAT_TYPE")
    String formatType;

    @Column(name = "ALPHANUMERIC_ALLOWED_FLAG")
    String alphanumericAllowedFlag;

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

    @Column(name = "FLEX_VALUE_SET_ID")
    Integer flexValueSetId;


}
