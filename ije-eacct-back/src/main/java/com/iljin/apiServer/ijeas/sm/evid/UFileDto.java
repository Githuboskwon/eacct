package com.iljin.apiServer.ijeas.sm.evid;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@Data
public class UFileDto {

    BigDecimal id;

    String compCd;

    String fileType;

    String documentHId;

    String remark;

    BigDecimal seq;

    String originalName;

    String storedName;

    String downloadUrl;

    String fileKind;

    Long fileAmount;

    String attribute1;

    String attribute2;

    String attribute3;

    String attribute4;

    String attribute5;

    List<MultipartFile> files;

    List<BigDecimal> fileIdList;

    List<String> slipNoList;

    @QueryProjection
    public UFileDto(BigDecimal id, String compCd, String fileType, String documentHId, String remark,
        BigDecimal seq, String originalName, String storedName, String downloadUrl, String fileKind,
        Long fileAmount, String attribute1, String attribute2, String attribute3, String attribute4,
        String attribute5) {
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
