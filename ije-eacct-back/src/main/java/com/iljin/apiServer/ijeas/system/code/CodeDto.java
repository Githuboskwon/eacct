package com.iljin.apiServer.ijeas.system.code;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@Data
public class CodeDto implements Serializable {

	private static final long serialVersionUID = 7877347016397708465L;

	int page;
	int size;
	int no;

	String groupCd;
	String groupNm;
	String groupDesc;
	String compCd;
	String detailCd;
	String detailNm;
	String useYn;
	Integer orderSeq;
	String detailDesc;
	String remark1;
	String remark2;
	String remark3;
	String remark4;
	String remark5;

	/*
	* EA-06-01 공통코드관리
	* 그룹코드 조회
	* */
	@QueryProjection
	public CodeDto(String compCd, String groupCd, String groupNm, String useYn, String groupDesc) {
		this.compCd = compCd;
		this.groupCd = groupCd;
		this.groupNm = groupNm;
		this.useYn = useYn;
		this.groupDesc = groupDesc;
	}
	/*
	* EA-06-01 공통코드관리
	* 그룹코드 클릭 event
	* 상세코드 조회
	* */
	@QueryProjection
	public CodeDto(String compCd, String groupCd, String detailCd, String detailNm, String useYn
			, Integer orderSeq, String detailDesc
			, String remark1, String remark2, String remark3, String remark4, String remark5
			) {
		this.compCd = compCd;
		this.groupCd = groupCd;
		this.detailCd = detailCd;
		this.detailNm = detailNm;
		this.useYn = useYn;
		this.orderSeq = orderSeq;
		this.detailDesc = detailDesc;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.remark3 = remark3;
		this.remark4 = remark4;
		this.remark5 = remark5;
	}


	@QueryProjection
	public CodeDto(String compCd, String groupCd, String groupNm, String useYn, String detailCd, String detailNm) {
		this.compCd = compCd;
		this.groupCd = groupCd;
		this.groupNm = groupNm;
		this.useYn = useYn;
		this.detailCd = detailCd;
		this.detailNm = detailNm;
	}

	@QueryProjection
	public CodeDto(String detailCd, String detailNm, String remark1, String remark2, String remark3, String remark4, String remark5) {
		this.detailCd = detailCd;
		this.detailNm = detailNm;
		this.remark1 = remark1;
		this.remark2 = remark2;
		this.remark3 = remark3;
		this.remark4 = remark4;
		this.remark5 = remark5;
	}
}
