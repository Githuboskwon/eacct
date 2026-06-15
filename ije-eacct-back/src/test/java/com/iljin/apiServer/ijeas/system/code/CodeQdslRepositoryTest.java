package com.iljin.apiServer.ijeas.system.code;

import com.iljin.apiServer.core.util.Pair;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CodeQdslRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(CodeQdslRepositoryTest.class);

    @Autowired
    CodeHeaderRepository codeHeaderRepository;

    @Autowired
    CodeDetailRepository codeDetailRepository;

    @Autowired
    CodeRepositoryCustom codeRepositoryCustom;

    @Autowired
    CodeQdslRepository codeQdslRepository;

    @Autowired
    CodeService codeService;

    final String COMP_CD = "1";
    final String SLIP_TYPE_CD = "E3";


    /**
     * querydsl 적용
     */

    @Test
    @DisplayName("공통코드 상세 조회, Pair")
    public void testGetUsedComboByCodeDto() throws Exception {
        //given
        CodeDto codeDto = new CodeDto();
        codeDto.setCompCd("100010");
        codeDto.setGroupCd("BP_CD");
        //when
        List<Pair> usedComboByCodeDto = codeQdslRepository.getUsedComboByCodeDto(codeDto);
        //then
        assertNotNull(usedComboByCodeDto);
    }


    @Test
    @DisplayName("공통코드 상세 조회, byCodeDto")
    public void testGetByCodeDto() throws Exception {
        //given
        CodeDto codeDto = new CodeDto();
        codeDto.setCompCd("100010");
        codeDto.setGroupCd("BP_CD");
        //codeDto.setRemark3("test");
        //when
        List<Map> byCodeDto = codeService.getByCodeDto(codeDto);
        //then
        assertNotNull(byCodeDto);
    }

    @Test
    @DisplayName("공통코드 헤더 리스트 조회")
    public void testGetGroupCodeList() throws Exception {
        //given
        CodeDto codeDto = new CodeDto();
        codeDto.setCompCd("100010");
        codeDto.setGroupCd("BP_CD");
        codeDto.setUseYn("Y");
        //when
        List<CodeDto> groupCodeList = codeQdslRepository.getGroupCodeList(codeDto);
        //then
        assertNotNull(groupCodeList);
    }

    @Test
    @DisplayName("공통코드 상세 리스트 조회")
    public void testGetGroupCodeDetailList() throws Exception {
        //given
        CodeDto codeDto = new CodeDto();
        codeDto.setCompCd("100010");
        codeDto.setGroupCd("BP_CD");
        codeDto.setUseYn("Y");
        //when
        List<CodeDto> groupCodeDetailList = codeQdslRepository.getGroupCodeDetailList(codeDto);
        //then
        assertNotNull(groupCodeDetailList);
    }

    @Test
    @DisplayName("groupCd가 TYPE_CODE 인 상세코드 조회")
    public void testGetGroupCodeDetailCode() throws Exception {
        //given
        CodeDto codeDto = new CodeDto();
        codeDto.setCompCd("100010");
        codeDto.setDetailCd("0101");
        //when
        Optional<CodeDto> groupCodeDetailCode = codeQdslRepository.getGroupCodeDetailCode(codeDto);
        //then
        assertNotNull(groupCodeDetailCode);
    }

    @Test
    @DisplayName("공통코드 헤더 페이지 조회")
    public void testSearchGroupCodePageSimple() throws Exception {
        //given
        CodeDto codeDto = new CodeDto();
        codeDto.setCompCd("100010");
        PageRequest pageRequest = PageRequest.of(0, 10);
        //when
        Page<CodeDto> codeDtos = codeQdslRepository.searchGroupCodePageSimple(codeDto, pageRequest);
        //then
        assertNotNull(codeDtos);
    }

}