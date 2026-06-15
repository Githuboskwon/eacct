package com.iljin.apiServer.ijeas.sm.tax;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TaxRepository extends JpaRepository<Tax, TaxKey> {
	
	@Query(value=" select " +
			" 	tax_cd as 'key', " +
			" 	tax_nm as 'value' " +
			" from " +
			" 	tb_mst_tax " +
			" where " +
			" 	comp_cd = :compCd " +
			" 	and evd_type_cd = :evdTypeCd " +
			" 	and use_yn = 'Y' ", nativeQuery = true)
	List<Map<String,String>> getByEvdTypeCd(@Param("compCd") String compCd, @Param("evdTypeCd") String evdTypeCd);
	
	@Query(" select e from Tax e where e.compCd = :compCd and e.evdTypeCd LIKE CONCAT('%', :evdTypeCd, '%') and e.useYn = 'Y' ")
	List<Tax> findByCompCdAndEvdTypeCd(@Param("compCd") String compCd, @Param("evdTypeCd") String evdTypeCd);

	Optional<Tax> findByCompCdAndUseYnAndTaxNm(String compCd, String useYn, String taxNm);

}
