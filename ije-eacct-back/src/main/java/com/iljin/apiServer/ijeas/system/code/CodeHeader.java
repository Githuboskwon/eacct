package com.iljin.apiServer.ijeas.system.code;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iljin.apiServer.core.audit.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "tb_code_hd")
@IdClass(CodeHeaderKey.class)
public class CodeHeader extends BaseEntity {
	
	@Id
	@Column(name="group_cd", nullable=false)
	String groupCd;

	@Id
	@Column(name="comp_cd", nullable=false)
	String compCd;

	@OneToMany(mappedBy = "codeHeader", fetch = FetchType.LAZY)
	@JsonIgnore
	List<CodeDetail> codeDetails;
	
	@Column(name = "group_nm")
	String groupNm;

	@Column(name = "group_desc")
	String groupDesc;

	@Column(name = "use_yn")
	String useYn;

	@Builder
	public CodeHeader(String groupCd, String compCd, String groupNm, String groupDesc, String useYn) {
		this.groupCd = groupCd;
		this.compCd = compCd;
		this.groupNm = groupNm;
		this.groupDesc = groupDesc;
		this.useYn = useYn;
	}
	
	public CodeHeader updateCodeHeader(String groupNm, String useYn, String groupDesc) {
		this.groupNm = groupNm;
		this.useYn = useYn;
		this.groupDesc = groupDesc;

		return this;
	}

}
