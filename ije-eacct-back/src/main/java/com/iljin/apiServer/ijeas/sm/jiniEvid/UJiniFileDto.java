package com.iljin.apiServer.ijeas.sm.jiniEvid;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@Data
public class UJiniFileDto {

    BigDecimal id;

    String compCd;

    String fileType;

    String documentHId;

    String remark;

    BigDecimal seq;

    String originalName;

    String storedPath;

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
    public UJiniFileDto(BigDecimal id, String compCd, String fileType, String documentHId, String remark,
        BigDecimal seq, String originalName, String storedPath, String storedName, String downloadUrl, String fileKind,
        Long fileAmount, String attribute1, String attribute2, String attribute3, String attribute4,
        String attribute5) {
        this.id = id;
        this.compCd = compCd;
        this.fileType = fileType;
        this.documentHId = documentHId;
        this.remark = remark;
        this.seq = seq;
        this.originalName = originalName;
        this.storedPath = storedPath;
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
