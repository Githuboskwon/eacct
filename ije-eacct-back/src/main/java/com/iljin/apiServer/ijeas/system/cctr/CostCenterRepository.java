package com.iljin.apiServer.ijeas.system.cctr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, CostCenterKey> {
	
//	@Query(value=" with recursive CTE as " +
//			" ( " +
//			" 	select " +
//			" 		A.*, " +
//			" 		convert(CONCAT(A.UPPER_CD,'|',A.CCTR_CD),varchar(255)) as sort" +
//			" 	from " +
//			" 		TB_MST_CCTR A " +
//			" 	where " +
//			" 		A.CCTR_CD = 'O000000001' " +
//			" 	union all select " +
//			" 		B.*, " +
//			" 		convert(CONCAT(C.SORT,'|',B.CCTR_CD),varchar(255)) " +
//			" 	from " +
//			" 		TB_MST_CCTR B " +
//			" 	inner join CTE C on " +
//			" 		B.UPPER_CD = C.CCTR_CD " +
//			" ) " +
//			" select " +
//			" 	CCTR_CD as cctrCd, " +
//			" 	COMP_CD as compCd, " +
//			" 	CCTR_NM as cctrNm, " +
//			" 	CCTR_OLN as cctrOln, " +
//			" 	BA_CD as baCd, " +
//			" 	UPPER_CD as upperCd, " +
//			" 	UPPER_NM as upperNm, " +
//			" 	VLD_PERD_STR_DT as vldPerdStrDt, " +
//			" 	VLD_PERD_END_DT as vldPerdEndDt, " +
//			" 	USE_YN as useYn, " +
//			" 	PCTR_CD as pctrCd, " +
//			" 	BP_CD as bpCd, " +
//			" 	BP_CLS_YN as bpClsYn, " +
//			" 	OLD_DEPT_CD as oldDeptCd, " +
//			" 	TEAM_CD as teamCd, " +
//			" 	ORG_FG_CD as orgFgCd, " +
//			" 	LVL_CD as lvlCd, " +
//			" 	BUD_YN as budYn, " +
//			" 	PICODE as picode, " +
//			" 	PISTAT as pistat, " +
//			" 	PIDATE as pidate, " +
//			" 	PITIME as pitime, " +
//			" 	PIUSER as piuser, " +
//			" 	PIMSG as pimsg, " +
//			" 	PIMSGID as pimsgid, " +
//			" 	SORT " +
//			" from " +
//			" 	CTE " +
//			" where " + 
//			" 	COMP_CD = :compCd " + 
//			"   and USE_YN = 'Y' " +
//			"   and VLD_PERD_END_DT = '99991231' " +
//			" 	and (CCTR_CD like concat(ifnull(:value,''),'%') " + 
//			" 	or lower(CCTR_NM) like concat('%',ifnull(:value,''),'%')) " + 
//			" order by SORT ", nativeQuery=true)
	@Query(value=" select " +
			" 	CCTR_CD as cctrCd, " +
			" 	COMP_CD as compCd, " +
			" 	CCTR_NM as cctrNm, " +
			" 	CCTR_OLN as cctrOln, " +
			" 	BA_CD as baCd, " +
			" 	UPPER_CD as upperCd, " +
			" 	UPPER_NM as upperNm, " +
			" 	VLD_PERD_STR_DT as vldPerdStrDt, " +
			" 	VLD_PERD_END_DT as vldPerdEndDt, " +
			" 	USE_YN as useYn, " +
			" 	PCTR_CD as pctrCd, " +
			" 	BP_CD as bpCd, " +
			" 	BP_CLS_YN as bpClsYn, " +
			" 	OLD_DEPT_CD as oldDeptCd, " +
			" 	TEAM_CD as teamCd, " +
			" 	ORG_FG_CD as orgFgCd, " +
			" 	LVL_CD as lvlCd, " +
			" 	BUD_YN as budYn, " +
			" 	PICODE as picode, " +
			" 	PISTAT as pistat, " +
			" 	PIDATE as pidate, " +
			" 	PITIME as pitime, " +
			" 	PIUSER as piuser, " +
			" 	PIMSG as pimsg, " +
			" 	PIMSGID as pimsgid " +
			" from " +
			" 	TB_MST_CCTR " +
			" where " +
			" 	COMP_CD = :compCd " +
			"   and USE_YN = 'Y' " +
			"   and VLD_PERD_END_DT = '99991231' " +
			" 	and (CCTR_CD like (nvl(:value,'')||'%') " +
			" 	or lower(CCTR_NM) like ('%'||ifnull(:value,'')||'%')) " +
			" order by ORG_FG_CD ", nativeQuery=true)
	List<CostCenter> getByCctrCdOrCctrNmContaining(@Param("compCd") String compCd, @Param("value") String value);

}
