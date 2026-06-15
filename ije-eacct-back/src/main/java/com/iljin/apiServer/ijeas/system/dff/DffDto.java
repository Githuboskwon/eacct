package com.iljin.apiServer.ijeas.system.dff;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class DffDto implements Serializable {
    private static final long serialVersionUID = -6351216012471879263L;

    String applicationShortCd;
    String acctCd;
    String mngItemCd;
    String mngItemNm;
    String mngItemTypeCd;
    String compCd;
    String defaultValue;
    String useCd;
    Integer fixLen;
    String hAlignCd;
    Integer orderSeq;
    String useYn;
    String requiredYn;
    String validationType;
    Integer flexValueSetId;
    String treeYn;
    String treeCd;
    String treeSeq;




    @QueryProjection
    public DffDto(String applicationShortCd, String acctCd, String mngItemCd, String mngItemNm, String mngItemTypeCd, Integer fixLen, String hAlignCd, Integer orderSeq,
                  String requiredYn, String validationType, Integer flexValueSetId, String treeYn, String treeCd, String treeSeq){
        this.applicationShortCd = applicationShortCd;
        this.acctCd = acctCd;
        this.mngItemCd = mngItemCd;
        this.mngItemNm = mngItemNm;
        this.mngItemTypeCd = mngItemTypeCd;
        this.fixLen = fixLen;
        this.hAlignCd = hAlignCd;
        this.orderSeq = orderSeq;
        this.requiredYn = requiredYn;
        this.validationType = validationType;
        this.flexValueSetId = flexValueSetId;
        this.treeYn = treeYn;
        this.treeCd = treeCd;
        this.treeSeq = treeSeq;
    }

    @QueryProjection
    public DffDto(String applicationShortCd, String acctCd, String mngItemCd, String mngItemNm, String mngItemTypeCd, String compCd, String defaultValue, String useCd,
                  Integer fixLen, String hAlignCd, Integer orderSeq, String useYn, String requiredYn, String validationType, Integer flexValueSetId, String treeYn,
                  String treeCd, String treeSeq) {
        this.applicationShortCd = applicationShortCd;
        this.acctCd = acctCd;
        this.mngItemCd = mngItemCd;
        this.mngItemNm = mngItemNm;
        this.mngItemTypeCd = mngItemTypeCd;
        this.compCd = compCd;
        this.defaultValue = defaultValue;
        this.useCd = useCd;
        this.fixLen = fixLen;
        this.hAlignCd = hAlignCd;
        this.orderSeq = orderSeq;
        this.useYn = useYn;
        this.requiredYn = requiredYn;
        this.validationType = validationType;
        this.flexValueSetId = flexValueSetId;
        this.treeYn = treeYn;
        this.treeCd = treeCd;
        this.treeSeq = treeSeq;
    }

    String valueName;
    String meaningName;
    BigDecimal rowCnt;

    public DffDto(String valueName, String meaningName, BigDecimal rowCnt){
        this.valueName = valueName;
        this.meaningName = meaningName;
        this.rowCnt = rowCnt;
    }

    String beforeValue;
    Integer page;
    Integer count;
    String search;
    public DffDto(Integer flexValueSetId, String beforeValue, Integer page, Integer count, String search){
        this.flexValueSetId = flexValueSetId;
        this.beforeValue = beforeValue;
        this.page = page;
        this.count = count;
        this.search = search;
    }

    public DffDto(BigDecimal count){
        this.count = count.intValue();
    }
}
