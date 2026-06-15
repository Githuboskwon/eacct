package com.iljin.apiServer.ijeas.sm.jini;

import com.iljin.apiServer.core.audit.BaseEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "U_JINI")
@Data
@NoArgsConstructor
@SequenceGenerator(sequenceName = "U_JINI_SEQ", name = "U_JINI_SEQ_GENERATOR", allocationSize = 1)
public class Jini extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "U_JINI_SEQ_GENERATOR")
    BigDecimal id;

    @Column(name = "COMP_CD")
    String compCd;

    @Column(name = "DOCUMENT_ID")
    String documentId;

    @Column(name = "JINI_ID")
    String jiniId;

    @Column(name = "JINI_NM")
    String jiniNm;

    @Column(name = "JINI_URL")
    String jiniUrl;

    @Builder
    public Jini(BigDecimal id, String compCd, String documentId, String jiniId, String jiniNm,
        String jiniUrl) {
        this.id = id;
        this.compCd = compCd;
        this.documentId = documentId;
        this.jiniId = jiniId;
        this.jiniNm = jiniNm;
        this.jiniUrl = jiniUrl;
    }

    public static Jini from(JiniDto jiniDto, String compCd) {
        return Jini.builder()
            .compCd(compCd)
            .documentId(jiniDto.getSlipNo())
            .jiniId(jiniDto.getJiniId())
            .jiniNm(jiniDto.getJiniNm())
            .jiniUrl(jiniDto.getJiniUrl())
            .build();
    }
    public static Jini from(JiniDto jiniDto, String slipNo, String compCd) {
        return Jini.builder()
            .compCd(compCd)
            .documentId(slipNo)
            .jiniId(jiniDto.getJiniId())
            .jiniNm(jiniDto.getJiniNm())
            .jiniUrl(jiniDto.getJiniUrl())
            .build();
    }
}
