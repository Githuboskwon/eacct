package com.iljin.apiServer.ijeas.system.code;

import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_code_dt")
@IdClass(CodeDetailKey.class)
public class CodeDetail extends BaseEntity {

	@Id
	@Column(name="group_cd", nullable=false)
	String groupCd;

	@Id
	@Column(name="detail_cd")
	String detailCd;

	@Id
	@Column(name="comp_cd", nullable=false)
	String compCd;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumns({
	    @JoinColumn(name = "group_cd", referencedColumnName="group_cd", insertable = false, updatable = false),
		@JoinColumn(name = "comp_cd", referencedColumnName="comp_cd", insertable = false, updatable = false)
	})
    CodeHeader codeHeader;
	
	@Column(name = "detail_nm")
	String detailNm;

	@Column(name = "use_yn")
	String useYn;

	@Column(name = "order_seq")
	Integer orderSeq;

	@Column(name = "detail_desc")
	String detailDesc;

	@Column(name = "remark1")
	String remark1;

	@Column(name = "remark2")
	String remark2;

	@Column(name = "remark3")
	String remark3;

	@Column(name = "remark4")
	String remark4;

	@Column(name = "remark5")
	String remark5;

	@Builder
	public CodeDetail(String groupCd, String detailCd, String compCd, String detailNm, String useYn, Integer orderSeq, String detailDesc, String remark1, String remark2,
					  String remark3, String remark4, String remark5, String regId, LocalDateTime regDtm, String chgId, LocalDateTime chgDtm) {
		this.groupCd = groupCd;
		this.detailCd = detailCd;
		this.compCd = compCd;
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
}
