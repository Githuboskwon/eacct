package com.iljin.apiServer.ijeas.sm.jini;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JiniDto {

    BigDecimal id;

    String compCd;

    String documentId;

    String jiniId;

    String jiniNm;

    String jiniUrl;

    String slipNo;

    @Builder
    public JiniDto(BigDecimal id, String compCd, String documentId, String jiniId, String jiniNm,
        String jiniUrl) {
        this.id = id;
        this.compCd = compCd;
        this.documentId = documentId;
        this.jiniId = jiniId;
        this.jiniNm = jiniNm;
        this.jiniUrl = jiniUrl;
    }

    public static JiniDto from (Jini jini) {
        return JiniDto.builder()
            .id(jini.getId())
            .compCd(jini.getCompCd())
            .documentId(jini.getDocumentId())
            .jiniId(jini.getJiniId())
            .jiniNm(jini.getJiniNm())
            .jiniUrl(jini.getJiniUrl())
            .build();
    }

    public JiniDto(String compCd, String documentId, String jiniId, String jiniNm, String jiniUrl) {
        this.compCd = compCd;
        this.documentId = documentId;
        this.jiniId = jiniId;
        this.jiniNm = jiniNm;
        this.jiniUrl = jiniUrl;
    }
}
