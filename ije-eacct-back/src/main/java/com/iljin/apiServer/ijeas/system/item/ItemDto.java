package com.iljin.apiServer.ijeas.system.item;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ItemDto implements Serializable {
    private static final long serialVersionUID = 5247513635913481626L;
    String compCd;
    String itemGroupCd;
    String itemGroupNm;
    LocalDateTime startDateActive;
    LocalDateTime endDateActive;
    String segmentNum;
    String enabledFlag;
    String attribute1;
    String attribute2;
    String attribute3;
    String attribute4;
    String attribute5;
    String attribute6;
    String attribute7;
    String attribute8;
    String attribute9;
    String attribute10;
    String attribute11;
    String attribute12;
    String attribute13;
    String attribute14;
    String attribute15;

    Integer personId;
    String postingDate;

    @QueryProjection
    public ItemDto(String compCd, String itemGroupCd, String itemGroupNm, LocalDateTime startDateActive, LocalDateTime endDateActive, String segmentNum, String enabledFlag,
                   String attribute1, String attribute2, String attribute3, String attribute4, String attribute5, String attribute6, String attribute7, String attribute8,
                   String attribute9, String attribute10, String attribute11, String attribute12, String attribute13, String attribute14, String attribute15) {
        this.compCd = compCd;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.startDateActive = startDateActive;
        this.endDateActive = endDateActive;
        this.segmentNum = segmentNum;
        this.enabledFlag = enabledFlag;
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.attribute3 = attribute3;
        this.attribute4 = attribute4;
        this.attribute5 = attribute5;
        this.attribute6 = attribute6;
        this.attribute7 = attribute7;
        this.attribute8 = attribute8;
        this.attribute9 = attribute9;
        this.attribute10 = attribute10;
        this.attribute11 = attribute11;
        this.attribute12 = attribute12;
        this.attribute13 = attribute13;
        this.attribute14 = attribute14;
        this.attribute15 = attribute15;
    }

    @QueryProjection
    public ItemDto(String compCd, String itemGroupCd, String itemGroupNm, String attribute1) {
        this.compCd = compCd;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.attribute1 = attribute1;
    }

    @QueryProjection
    public ItemDto(String compCd, String itemGroupCd, String itemGroupNm, String attribute1, Integer personId, String postingDate) {
        this.compCd = compCd;
        this.itemGroupCd = itemGroupCd;
        this.itemGroupNm = itemGroupNm;
        this.attribute1 = attribute1;
        this.personId = personId;
        this.postingDate = postingDate;
    }
}
