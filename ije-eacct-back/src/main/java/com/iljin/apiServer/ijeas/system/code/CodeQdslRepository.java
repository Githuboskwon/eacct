package com.iljin.apiServer.ijeas.system.code;

import com.iljin.apiServer.core.util.Pair;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CodeQdslRepository {

    List<Pair> getUsedComboByCodeDto(CodeDto codeDto);

    List<Pair> getUsedComboSeqByCodeDto(CodeDto codeDto);

    List<CodeDto> getGroupCodeList(CodeDto codeDto);

    List<CodeDto> getGroupCodeDetailList(CodeDto codeDto);

    List<Map> findByGroupCd(String compCd, String groupCd);

    List<Map> findByGroupCdAndRemark3(String compCd, String groupCd, String remark3);

    Optional<CodeDto> getGroupCodeDetailCode(CodeDto codeDto);

    List<Map> getCodeAll();

    Page<CodeDto> searchGroupCodePageSimple(CodeDto codeDto, Pageable pageable);
}
