package com.iljin.apiServer.ijeas.sm.evid;

import com.iljin.apiServer.core.audit.BaseEntity;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
@Table(name =  "U_FILE")
@Data
@SequenceGenerator(sequenceName = "U_FILE_SEQ", name = "U_FILE_SEQ_GENERATOR", allocationSize = 1)
public class UFile extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "U_FILE_SEQ_GENERATOR")
    BigDecimal id;

    @Column(name = "COMP_CD")
    String compCd;

    @Column(name = "FILE_TYPE")
    String fileType;

    @Column(name = "DOCUMENT_H_ID")
    String documentHId;

    @Column(name = "REMARK")
    String remark;

    @Column(name = "SEQ")
    BigDecimal seq;

    @Column(name = "ORIGINAL_NAME")
    String originalName;

    @Column(name = "STORED_NAME")
    String storedName;

    @Column(name = "DOWNLOAD_URL")
    String downloadUrl;

    @Column(name = "FILE_KIND")
    String fileKind;

    @Column(name = "FILE_AMOUNT")
    Long fileAmount;

    @Column(name = "ATTRIBUTE_1")
    String attribute1;

    @Column(name = "ATTRIBUTE_2")
    String attribute2;

    @Column(name = "ATTRIBUTE_3")
    String attribute3;

    @Column(name = "ATTRIBUTE_4")
    String attribute4;

    @Column(name = "ATTRIBUTE_5")
    String attribute5;


    @Builder
    public UFile(BigDecimal id, String compCd, String fileType, String documentHId, String remark, BigDecimal seq,
        String originalName, String storedName, String downloadUrl, String fileKind, Long fileAmount,
        String attribute1, String attribute2, String attribute3, String attribute4, String attribute5) {
        this.id = id;
        this.compCd = compCd;
        this.fileType = fileType;
        this.documentHId = documentHId;
        this.remark = remark;
        this.seq = seq;
        this.originalName = originalName;
        this.storedName = storedName;
        this.downloadUrl = downloadUrl;
        this.fileKind = fileKind;
        this.fileAmount = fileAmount;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
    }
}
