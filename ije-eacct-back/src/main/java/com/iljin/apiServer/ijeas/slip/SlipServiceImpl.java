package com.iljin.apiServer.ijeas.slip;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.approval.ApprovalDetailRepository;
import com.iljin.apiServer.ijeas.approval.ApprovalHeader;
import com.iljin.apiServer.ijeas.approval.ApprovalHeaderRepository;
import com.iljin.apiServer.ijeas.bond.BondHeader;
import com.iljin.apiServer.ijeas.bond.BondHeaderDto;
import com.iljin.apiServer.ijeas.bond.BondHeaderQdslRepository;
import com.iljin.apiServer.ijeas.bond.BondHeaderRepository;
import com.iljin.apiServer.ijeas.bond.BondMst;
import com.iljin.apiServer.ijeas.bond.BondMstRepository;
import com.iljin.apiServer.ijeas.card.CardUseList;
import com.iljin.apiServer.ijeas.card.CardUseListRepository;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeader;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeaderRepository;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipLine;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipLineRepository;
import com.iljin.apiServer.ijeas.slip.detail.SlipTrafficDt;
import com.iljin.apiServer.ijeas.slip.detail.SlipTrafficDtRepository;
import com.iljin.apiServer.ijeas.slip.detail.SlipTrafficInfoDto;
import com.iljin.apiServer.ijeas.slip.etax.XxsbDtiInvoiceRepository;
import com.iljin.apiServer.ijeas.slip.header.SlipBusinessTrip;
import com.iljin.apiServer.ijeas.slip.header.SlipBusinessTripDto;
import com.iljin.apiServer.ijeas.slip.header.SlipBusinessTripKey;
import com.iljin.apiServer.ijeas.slip.header.SlipBusinessTripRepository;
import com.iljin.apiServer.ijeas.slip.header.SlipTrafficHd;
import com.iljin.apiServer.ijeas.slip.header.SlipTrafficHdKey;
import com.iljin.apiServer.ijeas.slip.header.SlipTrafficHdRepository;
import com.iljin.apiServer.ijeas.slip.history.SlipHistoryDto;
import com.iljin.apiServer.ijeas.slip.prepayment.PrepaymentApply;
import com.iljin.apiServer.ijeas.slip.prepayment.PrepaymentApplyRepository;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchy;
import com.iljin.apiServer.ijeas.slipCommon.hierarchy.CoaHierarchyDto;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodDto;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodQdslRepository;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodRepository;
import com.iljin.apiServer.ijeas.sm.close.ClosStatCd;
import com.iljin.apiServer.ijeas.system.code.CodeDetail;
import com.iljin.apiServer.ijeas.system.code.CodeDetailRepository;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import com.iljin.apiServer.ijeas.system.emp.EmployeeRepository;
import com.iljin.apiServer.ijeas.system.expend.SlipExpend;
import com.iljin.apiServer.ijeas.system.expend.SlipExpendDto;
import com.iljin.apiServer.ijeas.system.expend.SlipExpendKey;
import com.iljin.apiServer.ijeas.system.expend.SlipExpendRespository;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.StoredProcedureQuery;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SlipServiceImpl implements SlipService{

    private final SlipHeaderRepository slipHeaderRepository;
    private final SlipRepositoryCustom slipRepositoryCustom;
    private final SlipQdslRepositoryImpl slipQdslRepositoryImpl;
    private final Util util;
    private final AcctPeriodRepository acctPeriodRepository;
    private final AcctPeriodQdslRepository acctPeriodQdslRepository;
    private final ErpSlipHeaderRepository erpSlipHeaderRepository;
    private final ApprovalHeaderRepository approvalHeaderRepository;
    private final ApprovalDetailRepository approvalDetailRepository;

    private final PrepaymentApplyRepository prepaymentApplyRepository;
    private final SlipExpendRespository slipExpendRespository;
    private final SlipBusinessTripRepository slipBusinessTripRepository;

    private final BondHeaderRepository bondHeaderRepository;

    private final BondHeaderQdslRepository bondHeaderQdslRepository;

    private final BondMstRepository bondMstRepository;

    private final SlipTrafficHdRepository slipTrafficHdRepository;

    private final SlipTrafficDtRepository slipTrafficDtRepository;
    private final SlipDetailRepository slipDetailRepository;

    private final SlipQdslRepository slipQdslRepository;

    private final ErpSlipLineRepository erpSlipLineRepository;

    private final CardUseListRepository cardUseListRepository;

    private final CodeDetailRepository codeDetailRepository;

    private final EmployeeRepository employeeRepository;

    private final EntityManager entityManager;
    private final XxsbDtiInvoiceRepository xxsbDtiInvoiceRepository;


    @Override
    public BigDecimal getSlipHistoryCount(SlipHistoryDto slipHistoryDto) {
        return slipRepositoryCustom.getSlipHistoryCount(slipHistoryDto);
    }

    @Override
    public List<SlipHistoryDto> getSlipHistory(SlipHistoryDto slipHistoryDto) {
        return slipQdslRepository.getSlipHistory(slipHistoryDto);
        //return slipRepositoryCustom.getSlipHistory(slipHistoryDto);
    }

    @Override
    public List<SlipHistoryDto> getSlipHistoryExcel(SlipHistoryDto slipHistoryDto) {
        return slipQdslRepository.getSlipHistoryExcel(slipHistoryDto);
        //return slipRepositoryCustom.getSlipHistory(slipHistoryDto);
    }

    @Override
    public SlipHeaderDto getSlipHeader(String slipNo){
        String loginCompCd = util.getLoginCompCd();

        if(hasText(slipNo)){
            List<SlipHeaderDto> list = slipRepositoryCustom.getSlipHeader(slipNo);

            if(hasText(list.get(0).slipTypeCd)){
                //여비교통비 추가조회
                if(list.get(0).slipTypeCd.equals("TRAFFIC")){
                    SlipTrafficHdKey slipTrafficHdKey = new SlipTrafficHdKey(loginCompCd, slipNo);
                    SlipTrafficHd slipTrafficHd = slipTrafficHdRepository.findById(slipTrafficHdKey).orElseThrow(() -> new RuntimeException("교통비 전표가 존재하지 않습니다."));

                    list.get(0).tripPlace = slipTrafficHd.getTripPlace();
                    list.get(0).tripFromDt = slipTrafficHd.getTripFromDt();
                    list.get(0).tripToDt = slipTrafficHd.getTripToDt();
                    list.get(0).tripObj = slipTrafficHd.getTripObj();
                    list.get(0).projectNo = slipTrafficHd.getProjectNo();
                    list.get(0).tripCd = slipTrafficHd.getTripCd();
                    list.get(0).temp1 = slipTrafficHd.getTemp1();
                    list.get(0).temp2 = slipTrafficHd.getTemp2();
                    list.get(0).temp3 = slipTrafficHd.getTemp3();
                    list.get(0).temp4 = slipTrafficHd.getTemp4();
                    list.get(0).temp5 = slipTrafficHd.getTemp5();
                }

                //출장비 추가 조회
                if(list.get(0).slipTypeCd.equals("TRIP")){
                    List<SlipBusinessTrip> slipBusinessTripList = slipBusinessTripRepository.findAllByCompCdAndMasterSlipNoAndMasterSlipHeaderId(loginCompCd, slipNo, list.get(0).slipHeaderId);

                    list.get(0).slipBusinessTripList = slipBusinessTripList;
                }

                //BOND 추가 조회
                if(list.get(0).slipTypeCd.equals("BOND")){
                    List<BondHeaderDto> bondHeaderList = slipQdslRepository.getBondSlipHeader(loginCompCd, slipNo);

                    list.get(0).bond = bondHeaderList.get(0);
                }
            }

            return list.get(0);
        }else{
            return null;
        }
    }

    @Override
    public List<SlipDetailDto> getSlipDetail(String slipNo){
        if(hasText(slipNo)){
            List<SlipDetailDto> list = slipRepositoryCustom.getSlipDetail(slipNo);

            return list;
        }else{
            return null;
        }
    }

    @Override
    public List<SlipDetailDto> getSlipTrafficDetail(String slipNo){
        if(hasText(slipNo)){
            List<SlipDetailDto> list = slipRepositoryCustom.getSlipTrafficDetail(slipNo);

            return list;
        }else{
            return null;
        }
    }

    @Override
    public List<SlipHeaderDto> getAirlineSlip(SlipHeaderDto slipHeaderDto){
        List<SlipHeaderDto> list = slipRepositoryCustom.getAirlineSlip(slipHeaderDto);
        return list;
    }

    @Override
    public String saveSlip(SlipDto slipDto) {

        String loginCompCd = util.getLoginCompCd();
        String postingDt = slipDto.getPostingDt();
        String ttypeInterfaceModule = slipDto.getTtypeInterfaceModule();

        // 1. 전자전표 회계기간 체크
        checkAcctPeriod(postingDt);
        // 2. erp 회계기간 체크
        checkErpAccrPeriod(postingDt, slipDto.getLedgerId(), ttypeInterfaceModule);
        // 3. 기본 정보 호출 (프로젝트코드, 거래유형 등 )
        setCcidAndTaxCcid(slipDto);

        // 4. 전자전표 채번
        if(isNull(slipDto.getSlipNo()) || slipDto.getSlipNo().isEmpty()) {
            Map<String, Object> slipNumberList = getSlipNumbers(ttypeInterfaceModule);
            slipDto.setSlipNo((String) slipNumberList.get("slipNo"));
            slipDto.setSlipHeaderId((BigDecimal) slipNumberList.get("slipHeaderId"));
            slipDto.setSlipGroupNo((String) slipNumberList.get("slipGroupNo"));
        }
        String slipNo = slipDto.getSlipNo();
        BigDecimal slipHeaderId = slipDto.getSlipHeaderId();
        String slipTypeCd = slipDto.getSlipTypeCd();

        if (slipDto.getTaxSmartbillNo() != null && !slipDto.getTaxSmartbillNo().isEmpty()) {
            String slipNumberResult = erpSlipHeaderRepository.existsBySmartBillNoCheck(Integer.parseInt(loginCompCd),slipNo,slipDto.getTaxSmartbillNo());

            if (slipNumberResult != null) throw new SlipException("이미 사용된 세금계산서 입니다.<br>사용 전표 번호: " + slipNumberResult);
        }

        if(!slipHeaderRepository.existsBycompCdAndSlipNo(loginCompCd, slipNo)) {
            saveNewSlipHeader(slipDto);

        } else {
            checkAlreadyApproved(slipDto);
            // 여기서 교통비삭제
            resetExistingSlipValues(slipDto);
            updateSlipHeader(slipDto);
        }

        saveSubHeaderBySlipTypeCd(slipDto);

        // 라인시작
        // 전기 급여전표가 아닐 경우
        if(!("HRAP001".equals(slipDto.getSlipType()) && "81".equals(loginCompCd))) {
            // detail 존재할 경우 삭제
            deleteExistingDetail(slipDto);

            List<SlipDetailDto> slipDetailDtoList = slipDto.getSlipDetailDtoList();
            int slipLineNumber = 1;
            for(int i = 0; i < slipDetailDtoList.size(); i++) {
                SlipDetailDto slipDetailDto = slipDetailDtoList.get(i);

                setLineCcidAndTaxCcid(slipDetailDto);

                // 교통비일 경우
                if(SlipTypeCd.TRAFFIC.getCode().equals(slipTypeCd)) {
                    SlipTrafficDt slipTrafficDt = SlipTrafficDt.from(slipDto, i);
                    slipTrafficDtRepository.save(slipTrafficDt);

                    // 유류비, 기타비에 따라 detail 저장
                    slipLineNumber = saveSlipDetailByAmt(slipDto, slipTrafficDt, slipLineNumber, i);

                } else {
                    // 일반 저장
                    BigDecimal slipLineId = slipDetailRepository.getSlipLineId()
                        .orElseThrow(() -> new RuntimeException("CBO_SP_SLIP_LINE_S.NEXTVAL ERROR"));
                    String bungaeNo = slipDetailRepository.getBungaeNo(util.getLoginCompCd());

                    if(!SlipTypeCd.PO.getCode().equals(slipTypeCd) && !SlipTypeCd.IM.getCode().equals(slipTypeCd)) {
                        SlipDetail slipDetail = SlipDetail.from(slipDto, slipLineId, bungaeNo, i);
                        slipDetailRepository.save(slipDetail);
                        ErpSlipLine erpSlipLine = ErpSlipLine.from(slipDto, slipLineId,
                            slipLineNumber++, i);
                        erpSlipLineRepository.save(erpSlipLine);
                    }

                    // 카드 내역 변경
                    if(nonNull(slipDetailDto.getCardUsedNo()) && !"".equals(slipDetailDto.getCardUsedNo())) {
                        CardUseList cardUseList = cardUseListRepository.findByCompCdAndUsedNo(util.getLoginCompCd(), slipDetailDto.getCardUsedNo())
                            .orElseThrow(() -> new RuntimeException("카드내역이 존재하지 않습니다."));
                        cardUseList.updateSlipInfo(slipDto.getSlipNo(), "SV", slipDto.getSlipHeaderId(), slipLineId);
                    }
                }
            }
        }

        if("deletePrepay".equals(slipDto.getTemp())) {
            // 선급반제 신청 모두 삭제(flag 업데이트)
            List<PrepaymentApply> prepaymentApplyList = prepaymentApplyRepository.findAllByOrgIdAndSlipHeaderIdAndLedgerId(Integer.parseInt(loginCompCd), slipHeaderId, slipDto.getLedgerId());
            for(PrepaymentApply prepaymentApply : prepaymentApplyList) {
                prepaymentApply.updateFlag(prepaymentApply.getAmountToApply(), slipDto.getPersonId());
            }
        }

        // 예산 체크
        List<SlipHistoryDto> cntList = slipRepositoryCustom.getCoaHierarchyCnt(slipHeaderId, Integer.parseInt(loginCompCd), slipDto.getLedgerId(), slipDto.getPostingDt());
        if(!cntList.isEmpty() && !cntList.get(0).getCount().equals(new BigDecimal(0))) {
            slipRepositoryCustom.callBudgetCheckProcedure(slipHeaderId);
        }
        return slipNo;
    }

    public void checkAcctPeriod(String postingDt){
        List<AcctPeriodDto> acctPeriodDtos = acctPeriodQdslRepository.getCheckAcctPeriodList(util.getLoginCompCd(), postingDt);
        if(acctPeriodDtos.size() == 0) {
            throw new SlipException("회계일자에 대한 마감등록이 없습니다.");
        }
        if(acctPeriodDtos.size() > 1) {
            throw new SlipException("회계일자에 대한 마감등록이 잘못됐습니다.");
        }
        if (acctPeriodDtos.get(0).getClosStatCd().equals(ClosStatCd.CLOSE.getCode())) {
            throw new SlipException("전자전표의 회계일자가 CLOSE되었습니다.");
        }
    }

    private void checkErpAccrPeriod(String postingDt, BigDecimal ledgerId, String ttypeInterfaceModule) {
        String openYn = acctPeriodRepository.checkErpAcctPeriod(postingDt, ledgerId, ttypeInterfaceModule);
        if(isNull(openYn)) {
            throw new RuntimeException("CBO_GL_COMMON_PKG.GET_PERIOD_INFO : Call Error.");
        }
        if(!"O".equals(openYn)) {
            throw new SlipException("ERP 회계기간이 오픈되지 않았습니다.");
        }
    }

    private void setCcidAndTaxCcid(SlipDto slipDto) {
        BigDecimal codeCombinationId = slipHeaderRepository.callPkgGetCcid(slipDto.getLedgerId(), slipDto.getCompCode(),
            slipDto.getBudgetDeptCode() , slipDto.getActualDeptCode(), slipDto.getAcctCode(), slipDto.getItemGroupCode(),
            slipDto.getProjectCode(), slipDto.getTrBankAcctCode(), slipDto.getSlipTypeCode(), slipDto.getSegment9Code(), slipDto.getSegment10Code());
        if(isNull(codeCombinationId)){
            throw new RuntimeException("APPS.CBO_GL_COMMON_PKG.GET_CCID : Call Error.");
        }
        if(codeCombinationId.equals(new BigDecimal(-1))) {
            throw new RuntimeException("CODE_COMBINATION_ID : Call Error.");
        }

        BigDecimal taxCodeCombinationId = null;
        if(nonNull(slipDto.getTaxAcctCode()) && !slipDto.getTaxAcctCode().isEmpty()) {
            taxCodeCombinationId = slipHeaderRepository.callPkgGetCcid(slipDto.getLedgerId(), slipDto.getCompCode(), slipDto.getBudgetDeptCode() ,
                slipDto.getActualDeptCode(), slipDto.getTaxAcctCode(), slipDto.getItemGroupCode(), slipDto.getProjectCode(), slipDto.getTrBankAcctCode(),
                slipDto.getSlipTypeCode(), slipDto.getSegment9Code(), slipDto.getSegment10Code());
            if(isNull(taxCodeCombinationId)) {
                throw new RuntimeException("CBO_GL_COMMON_PKG.GET_CCID : Call Error.(Tax CCID)");
            }
            if(taxCodeCombinationId.equals(new BigDecimal(-1))) {
                throw new RuntimeException("TAX_CODE_COMBINATION_ID : Call Error.");
            }
        }

        slipDto.setCodeCombinationId(codeCombinationId);
        slipDto.setTaxCodeCombinationId(taxCodeCombinationId);
    }

    private Map<String, Object> getSlipNumbers(String ttypeInterfaceModule) {

        BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();
        String slipNo = "";
        if("AP".equals(ttypeInterfaceModule)) {
            slipNo = slipHeaderRepository.callGetSlipNumber("SPI", util.getLoginCompCd());
        } else if ("AR".equals(ttypeInterfaceModule)) {
            slipNo = slipHeaderRepository.callGetSlipNumber("SPA", util.getLoginCompCd());
        }
        String slipGroupNo = slipHeaderRepository.callGetSlipNumber("GRP", util.getLoginCompCd());

        if(isNull(slipHeaderId)) {
            throw new RuntimeException("CBO_SP_SLIP_HEADER_S.NEXTVAL ERROR.");
        }
        if(isNull(slipNo) || slipNo.isEmpty()) {
            throw new RuntimeException("SLIP_NUMBER ERROR.");
        }
        if(isNull(slipGroupNo) || slipGroupNo.isEmpty()) {
            throw new RuntimeException("SLIP_GROUP_NUMBER ERROR.");
        }

        Map<String, Object> slipNumberList = new HashMap<>();
        slipNumberList.put("slipHeaderId", slipHeaderId);
        slipNumberList.put("slipNo", slipNo);
        slipNumberList.put("slipGroupNo", slipGroupNo);
        return slipNumberList;
    }

    private void saveNewSlipHeader(SlipDto slipDto) {

        List<SlipHeaderDto> slipHeaderDtoList = slipRepositoryCustom.getSlipInfo(util.getLoginCompCd(), slipDto.getSlipNo(), slipDto.getSlipHeaderId());
        SlipHeader slipHeader = SlipHeader.from(slipDto);
        ErpSlipHeader erpSlipHeader = ErpSlipHeader.from(slipDto);
        if(!slipHeaderDtoList.isEmpty()) {
            SlipHeaderDto slipHeaderDto = slipHeaderDtoList.get(0);
            slipHeader.updateTaxbillInfo(slipHeaderDto);
            erpSlipHeader.updateTaxLocationCode(slipHeaderDto.getTaxLocationCode());
        }
        slipHeaderRepository.save(slipHeader);
        erpSlipHeaderRepository.save(erpSlipHeader);
    }

    private void checkAlreadyApproved(SlipDto slipDto) {
        ErpSlipHeader erpSlipHeaderCheck = erpSlipHeaderRepository.findByOrgIdAndSlipHeaderId(Integer.parseInt(util.getLoginCompCd()), slipDto.getSlipHeaderId())
            .orElseThrow(() -> new SlipException("전표상태 Error. 전표 필수 정보가 잘못됐습니다."));
        if("Y".equals(erpSlipHeaderCheck.getSlipDataFixFlag())) {
            throw new SlipException("이미 상신된 전표 입니다.");
        }
    }

    private void resetExistingSlipValues(SlipDto slipDto) {
        String slipNo = slipDto.getSlipNo();
        String loginCompCd = util.getLoginCompCd();
        BigDecimal slipHeaderId = slipDto.getSlipHeaderId();

        SlipHeader slipHeader = slipHeaderRepository.findById(new SlipHeaderKey(loginCompCd, slipNo))
            .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다"));

        String slipTypeCd = slipHeader.getSlipTypeCd(); // 전표양식코드
        if(!slipTypeCd.equals(slipDto.getSlipTypeCd()) || !slipHeader.getEmpNo().equals(slipDto.getEmpNo())) {
            // 서브헤더
            if(SlipTypeCd.EXPEND.getCode().equals(slipTypeCd)) {
                if(!slipExpendRespository.existsById(new SlipExpendKey(loginCompCd, slipNo, slipHeaderId))) {
                    throw new RuntimeException("경조금이 존재하지 않습니다.");
                }
                slipExpendRespository.deleteById(new SlipExpendKey(loginCompCd, slipNo, slipHeaderId));
            } else if(SlipTypeCd.TRIP.getCode().equals(slipTypeCd)) {
                if(!slipBusinessTripRepository.existsByCompCdAndMasterSlipNoAndMasterSlipHeaderId(loginCompCd, slipNo, slipHeaderId)) {
                    throw new RuntimeException("출장내역이 존재하지 않습니다.");
                }
                slipBusinessTripRepository.deleteAllByCompCdAndMasterSlipNoAndMasterSlipHeaderId(loginCompCd, slipNo, slipHeaderId);
            } else if(SlipTypeCd.BOND.getCode().equals(slipTypeCd)) {
                if(!bondHeaderRepository.existsByCompCdAndSlipNo(loginCompCd, slipNo)) {
                    throw new RuntimeException("BOND가 존재하지 않습니다.");
                }
                bondHeaderRepository.deleteByCompCdAndSlipNo(loginCompCd, slipNo);
            } else if(SlipTypeCd.TRAFFIC.getCode().equals(slipTypeCd)) {
                if(!slipTrafficHdRepository.existsById(new SlipTrafficHdKey(loginCompCd, slipNo))){
                    throw new RuntimeException("교통비가 존재하지 않습니다");
                }
                slipTrafficDtRepository.deleteByCompCdAndSlipNo(loginCompCd, slipDto.getSlipNo());
                slipTrafficHdRepository.deleteById(new SlipTrafficHdKey(loginCompCd, slipNo));

            }

            // 상세디테일
            slipDetailRepository.deleteAllByCompCdAndSlipNo(loginCompCd, slipNo);
            erpSlipLineRepository.deleteAllByOrgIdAndSlipHeaderId(loginCompCd, slipHeaderId);

            ErpSlipHeader erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndSlipHeaderId(Integer.parseInt(loginCompCd), slipHeaderId)
                .orElseThrow(() -> new RuntimeException("ERP 전표가 존재하지 않습니다."));
            erpSlipHeader.reset();
            slipHeader.reset();
        }
    }

    private void updateSlipHeader(SlipDto slipDto) {
        String slipNo = slipDto.getSlipNo();
        BigDecimal slipHeaderId = slipDto.getSlipHeaderId();

        ErpSlipHeader erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndSlipNumberAndSlipHeaderId(Integer.parseInt(util.getLoginCompCd()), slipNo, slipHeaderId)
                .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
        SlipHeader slipHeaderUpdate = slipHeaderRepository.findByCompCdAndSlipNoAndSlipHeaderId(util.getLoginCompCd(), slipNo, slipHeaderId)
                .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));

        slipHeaderUpdate.update(slipDto);
        erpSlipHeader.update(slipDto);

        List<SlipHeaderDto> slipHeaderDtoList = slipRepositoryCustom.getSlipInfo(util.getLoginCompCd(), slipDto.getSlipNo(), slipDto.getSlipHeaderId());

        if(!slipHeaderDtoList.isEmpty()) {
//            throw new RuntimeException("전표가 존재하지 않습니다.");
            SlipHeaderDto slipHeaderDto = slipHeaderDtoList.get(0);
            slipHeaderUpdate.updateTaxbillInfo(slipHeaderDto);
            erpSlipHeader.updateTaxLocationCode(slipHeaderDto.getTaxLocationCode());
        }
    }

    private void saveSubHeaderBySlipTypeCd(SlipDto slipDto) {
        String loginCompCd = util.getLoginCompCd();
        String slipNo = slipDto.getSlipNo();
        BigDecimal slipHeaderId = slipDto.getSlipHeaderId();

        String slipTypeCd = slipDto.getSlipTypeCd();  // 전표양식코드

        if(SlipTypeCd.EXPEND.getCode().equals(slipTypeCd)) {
            SlipExpendDto slipExpendDto = slipDto.getSlipExpendDto();
            if(!slipExpendRespository.existsByCompCdAndSlipHeaderId(loginCompCd, slipDto.getSlipHeaderId())) {
                SlipExpend slipExpend = SlipExpend.from(slipDto);
                slipExpendRespository.save(slipExpend);
            } else {
                SlipExpend slipExpend = slipExpendRespository.findByCompCdAndSlipHeaderId(
                        loginCompCd, slipDto.getSlipHeaderId())
                    .orElseThrow(() -> new RuntimeException("경조금이 존재하지 않습니다."));
                slipExpend.update(slipExpendDto);
            }
        }

        if(SlipTypeCd.TRIP.getCode().equals(slipTypeCd)) {
            List<SlipBusinessTripDto> businessTripDtoList = slipDto.getSlipBusinessTripDtoList();
            for(int i = 0; i < businessTripDtoList.size(); i++) {
                SlipBusinessTripDto businessTripDto = businessTripDtoList.get(i);
                if(!slipBusinessTripRepository.existsById(new SlipBusinessTripKey(loginCompCd, slipNo, slipHeaderId, businessTripDto.getSeq()))) {
                    SlipBusinessTrip slipBusinessTrip = SlipBusinessTrip.from(slipDto, i);
                    slipBusinessTripRepository.save(slipBusinessTrip);
                } else {
                    SlipBusinessTrip slipBusinessTrip =slipBusinessTripRepository.findById(new SlipBusinessTripKey(loginCompCd, slipNo, slipHeaderId, businessTripDto.getSeq()))
                        .orElseThrow(() -> new RuntimeException("출장내역이 존재하지 않습니다."));
                    slipBusinessTrip.update(businessTripDto);
                }
            }
        }

        if(SlipTypeCd.TRAFFIC.getCode().equals(slipTypeCd)) {
            SlipTrafficHdKey slipTrafficHdKey = new SlipTrafficHdKey(loginCompCd, slipNo);
            if(!slipTrafficHdRepository.existsById(slipTrafficHdKey)) {
                SlipTrafficHd slipTrafficHd = SlipTrafficHd.from(slipDto);
                slipTrafficHdRepository.save(slipTrafficHd);
            } else {
                SlipTrafficHd slipTrafficHd = slipTrafficHdRepository.findById(slipTrafficHdKey)
                    .orElseThrow(() -> new RuntimeException("교통비가 존재하지 않습니다."));
                slipTrafficHd.update(slipDto.getSlipTrafficHdDto());
            }
        }

        if(SlipTypeCd.BOND.getCode().equals(slipTypeCd)) {
            BondHeaderDto bondHeaderDto = slipDto.getBondHeaderDto();
            List<BondHeader> bondLocalList = bondHeaderQdslRepository.findAllByTypeAndStatusNotIn(slipDto);
            if(!bondLocalList.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("해당 REF_NO로 진행중인 국내수수료 BOND전표가 존재합니다. 기존 전표가 완료처리가 되지 않으면 전표를 생성할 수 없습니다. <br/> 전표번호 : ");
                for(BondHeader bondLocal : bondLocalList) {
                    sb.append(bondLocal.getSlipNo() + "<br/>");
                }
                throw new SlipException(sb.toString());
            }

            if(!bondHeaderQdslRepository.existsByTypeAndStatusIn(loginCompCd, bondHeaderDto.getRefNo(), slipDto.getSlipHeaderId()) && ("Y".equals(bondHeaderDto.getSplitEtcYn()) || "OVERSEA".equals(bondHeaderDto.getType()) )) {
                throw new SlipException("해당 REF_NO로 결재완료된 국내수수료 BOND전표가 없으면 [분할 및 기타비용], [해외수수료] 전표를 진행할 수 없습니다.");
            }

            bondHeaderRepository.deleteByCompCdAndSlipHeaderId(loginCompCd, slipHeaderId);
            BondMst bondMst = bondMstRepository.findByCompCdAndRefNo(loginCompCd, bondHeaderDto.getRefNo())
                .orElseThrow(() -> new RuntimeException("BOND MASTER가 존재하지 않습니다."));
            BondHeader bondHeader = BondHeader.from(slipDto, bondMst);
            bondHeaderRepository.save(bondHeader);
        }
    }

    private void deleteExistingDetail(SlipDto slipDto) {
        String loginCompCd = util.getLoginCompCd();
        BigDecimal slipHeaderId = slipDto.getSlipHeaderId();
        String slipTypeCd = slipDto.getSlipTypeCd();
        if(slipDetailRepository.existsByCompCdAndSlipNoAndSlipHeaderId(loginCompCd, slipDto.getSlipNo(), slipDto.getSlipHeaderId())){
            // 법인카드 사용 내역 초기화 -> 전체 필요
            List<CardUseList> cardUseLists = cardUseListRepository.findAllByCompCdAndSlipHeaderId(loginCompCd, slipHeaderId);
            for(CardUseList cardUseList : cardUseLists) {
                cardUseList.resetSlipInfo();
            }

            // 교통비 라인 삭제
            if(SlipTypeCd.TRAFFIC.getCode().equals(slipTypeCd)) {
                slipTrafficDtRepository.deleteByCompCdAndSlipNo(loginCompCd, slipDto.getSlipNo());
            }
            if(!SlipTypeCd.PO.getCode().equals(slipTypeCd) && !SlipTypeCd.IM.getCode().equals(slipTypeCd)) {
                // detail 삭제
                slipDetailRepository.deleteByCompCdAndSlipHeaderId(loginCompCd, slipHeaderId);
            }

        }
        if(!SlipTypeCd.PO.getCode().equals(slipTypeCd) && !SlipTypeCd.IM.getCode().equals(slipTypeCd)) {
            erpSlipLineRepository.deleteByOrgIdAndSlipHeaderId(loginCompCd, slipHeaderId);
        }
    }

    private void setLineCcidAndTaxCcid(SlipDetailDto slipDetailDto) {
        BigDecimal lineCodeCombinationId = slipHeaderRepository.callPkgGetCcid(slipDetailDto.getLedgerId(), slipDetailDto.getCompCode(), slipDetailDto.getBudgetDeptCode(), slipDetailDto.getActualDeptCode(),
            slipDetailDto.getAcctCode(), slipDetailDto.getItemGroupCode(), slipDetailDto.getProjectCode(), slipDetailDto.getTrAcctCode(), slipDetailDto.getSlipTypeCode(), slipDetailDto.getSegment9Code(), slipDetailDto.getSegment10Code());
        if(isNull(lineCodeCombinationId)){
            throw new SlipException("APPS.CBO_GL_COMMON_PKG.GET_CCID : Call Error.");
        }
        if(lineCodeCombinationId.equals(new BigDecimal(-1))) {
            throw new SlipException("LINE CODE_COMBINATION_ID : Call Error.");
        }

        BigDecimal lineTaxCombinationId = null;
        if((hasText(slipDetailDto.getTaxAcctCode()) && nonNull(slipDetailDto.getTaxId())) || (!"".equals(
            slipDetailDto.getTaxAcctCode()) && !"".equals(slipDetailDto.getTaxId()))) {
            lineTaxCombinationId = slipHeaderRepository.callPkgGetCcid(slipDetailDto.getLedgerId(), slipDetailDto.getCompCode(), slipDetailDto.getBudgetDeptCode(), slipDetailDto.getActualDeptCode(),
                slipDetailDto.getTaxAcctCode(), slipDetailDto.getItemGroupCode(), slipDetailDto.getProjectCode(), slipDetailDto.getTrAcctCode(), slipDetailDto.getSlipTypeCode(), slipDetailDto.getSegment9Code(), slipDetailDto.getSegment10Code());
            if(isNull(lineTaxCombinationId)) {
                throw new SlipException("APPS.CBO_GL_COMMON_PKG.GET_CCID : Call Error.(Line Tax CCID)");
            }
            if(lineTaxCombinationId.equals(new BigDecimal(-1))) {
                throw new SlipException("TAX_CODE_COMBINATION_ID : Call Error.");
            }
        }

        slipDetailDto.setCodeCombinationId(lineCodeCombinationId);
        slipDetailDto.setTaxCodeCombinationId(lineTaxCombinationId);
    }

    private int saveSlipDetailByAmt(SlipDto slipDto, SlipTrafficDt slipTrafficDt, int slipLineNumber, int slipDetailSeq) {
        String loginCompCd = util.getLoginCompCd();

        // 유류비
        String oilAmt = slipTrafficDt.getOilAmt();
        if (nonNull(oilAmt) && !oilAmt.equals("") && !oilAmt.equals("0")) {
            BigDecimal slipLineId = slipDetailRepository.getSlipLineId()
                .orElseThrow(() -> new RuntimeException("CBO_SP_SLIP_LINE_S.NEXTVAL ERROR"));
            String bungaeNoOilAmt = slipDetailRepository.getBungaeNo(util.getLoginCompCd());
            SlipTrafficInfoDto infoDto = new SlipTrafficInfoDto(String.valueOf(slipDetailSeq +1), slipLineId, bungaeNoOilAmt, oilAmt, "02", "", " 교통비", "D");
            saveSlipTrafficDetail(slipDto, infoDto, slipLineNumber++, slipDetailSeq);
        }
        // 기타금액1
        String etcAmt1 = slipTrafficDt.getEtcAmt1();
        if (nonNull(etcAmt1) && !etcAmt1.equals("") && !etcAmt1.equals("0")) {
            BigDecimal slipLineId = slipDetailRepository.getSlipLineId()
                .orElseThrow(() -> new RuntimeException("CBO_SP_SLIP_LINE_S.NEXTVAL ERROR"));
            String bungaeNoEtc1 = slipDetailRepository.getBungaeNo(util.getLoginCompCd());
            CodeDetail trafficType = codeDetailRepository.findByCompCdAndGroupCdAndUseYnAndDetailCd(
                    loginCompCd, "TRAFFIC_TYPE_CD", "Y", slipTrafficDt.getEtcType1())
                .orElseThrow(() -> new RuntimeException("교통비 코드가 존재하지 않습니다."));
            SlipTrafficInfoDto infoDto = new SlipTrafficInfoDto(String.valueOf(slipDetailSeq +1), slipLineId, bungaeNoEtc1, etcAmt1, slipTrafficDt.getEtcChitCd1(),
                slipTrafficDt.getEtcUsedNo1(), " " + trafficType.getDetailNm(), slipTrafficDt.getEtcType1());
            saveSlipTrafficDetail(slipDto, infoDto, slipLineNumber++, slipDetailSeq);
        }
        // 기타금액2
        String etcAmt2 = slipTrafficDt.getEtcAmt2();
        if (nonNull(etcAmt2) && !etcAmt2.equals("") && !etcAmt2.equals("0")) {
            BigDecimal slipLineId = slipDetailRepository.getSlipLineId()
                .orElseThrow(() -> new RuntimeException("CBO_SP_SLIP_LINE_S.NEXTVAL ERROR"));
            String bungaeNoEtc2 = slipDetailRepository.getBungaeNo(util.getLoginCompCd());
            CodeDetail trafficType = codeDetailRepository.findByCompCdAndGroupCdAndUseYnAndDetailCd(
                    loginCompCd, "TRAFFIC_TYPE_CD", "Y", slipTrafficDt.getEtcType2())
                .orElseThrow(() -> new RuntimeException("교통비 코드가 존재하지 않습니다."));
            SlipTrafficInfoDto infoDto = new SlipTrafficInfoDto(String.valueOf(slipDetailSeq +1), slipLineId, bungaeNoEtc2, etcAmt2, slipTrafficDt.getEtcChitCd2(),
                slipTrafficDt.getEtcUsedNo2(), " " + trafficType.getDetailNm(), slipTrafficDt.getEtcType2());
            saveSlipTrafficDetail(slipDto, infoDto, slipLineNumber++, slipDetailSeq);
        }
        // 기타금액3
        String etcAmt3 = slipTrafficDt.getEtcAmt3();
        if (nonNull(etcAmt3) && !etcAmt3.equals("") && !etcAmt3.equals("0")) {
            BigDecimal slipLineId = slipDetailRepository.getSlipLineId()
                .orElseThrow(() -> new RuntimeException("CBO_SP_SLIP_LINE_S.NEXTVAL ERROR"));
            String bungaeNoEtc3 = slipDetailRepository.getBungaeNo(util.getLoginCompCd());
            CodeDetail trafficType = codeDetailRepository.findByCompCdAndGroupCdAndUseYnAndDetailCd(
                    loginCompCd, "TRAFFIC_TYPE_CD", "Y", slipTrafficDt.getEtcType3())
                .orElseThrow(() -> new RuntimeException("교통비 코드가 존재하지 않습니다."));
            SlipTrafficInfoDto infoDto = new SlipTrafficInfoDto(String.valueOf(slipDetailSeq +1), slipLineId, bungaeNoEtc3, etcAmt3, slipTrafficDt.getEtcChitCd3(),
                slipTrafficDt.getEtcUsedNo3(), " " + trafficType.getDetailNm(), slipTrafficDt.getEtcType3());
            saveSlipTrafficDetail(slipDto, infoDto, slipLineNumber++, slipDetailSeq);
        }
        return slipLineNumber;
    }

    private void saveSlipTrafficDetail(SlipDto slipDto, SlipTrafficInfoDto infoDto, int sliplineNumber, int slipDetailSeq) {
        SlipDetailDto slipDetailDto = slipDto.getSlipDetailDtoList().get(slipDetailSeq);
        SlipDetail slipDetailOilAmt = SlipDetail.from(slipDto, infoDto, slipDetailSeq);
        slipDetailRepository.save(slipDetailOilAmt);
        ErpSlipLine erpSlipLine = ErpSlipLine.from(slipDto, infoDto, sliplineNumber, slipDetailSeq);
        erpSlipLineRepository.save(erpSlipLine);
        if(hasText(slipDetailDto.getCardUsedNo())) {
            CardUseList cardUseList = cardUseListRepository.findByCompCdAndUsedNo(util.getLoginCompCd(), slipDetailDto.getCardUsedNo())
                .orElseThrow(() -> new RuntimeException("카드내역이 존재하지 않습니다."));
            cardUseList.updateSlipInfo(slipDto.getSlipNo(), "SV", slipDto.getSlipHeaderId(), infoDto.getSlipLineId());
        }
    }

    @Override
    public Object deleteSlip(List<SlipDto> slipDtos) throws Exception {
        String compCd = util.getLoginCompCd();
        Integer personId = util.getLoginUser().getEmployee().getPersonId();

        for(SlipDto slipDto : slipDtos){
            String slip_group_yn = slipDto.getSlipGroupYn();
            BigDecimal approval_group_id = slipDto.getApprovalGroupId();
            BigDecimal slip_header_id = slipDto.getSlipHeaderId();
            String slip_type = slipDto.getSlipType();
            BigDecimal ledger_id = slipDto.getLedgerId();
            String trx_type_code = slip_type;

            // 그룹 전표일 경우
            if("Y".equals(slip_group_yn) && "GROUP".equals(slip_type)){


                List<SlipHeaderDto> slipHeaderDtos = slipQdslRepository.getSlipStatusGroup(compCd,approval_group_id);

                if(slipHeaderDtos != null && slipHeaderDtos.size() > 0){

                    for(SlipHeaderDto slipHeaderDto : slipHeaderDtos){
                        BigDecimal line_slip_header_id = slipHeaderDto.getSlipHeaderId();
                        String line_company_code = slipHeaderDto.getCompCd();
                        String line_slip_type = slipHeaderDto.getSlipType();
                        BigDecimal line_ledger_id = slipHeaderDto.getLedgerId();
                        String line_status = slipHeaderDto.getSlipStatus();
                        String line_slip_if_flag = slipHeaderDto.getSlipIfFlag();
                        String line_slip_no = slipHeaderDto.getSlipNo();
                        String line_taxSmartbillNo = slipHeaderDto.getTaxSmartbillNo();

                        if(!("SV".equals(line_status) || "VC".equals(line_status) || "VE".equals(line_status) || "IE".equals(line_status))
                                || "AR".equals(line_status) || "CR".equals(line_status)){
                            if("Y".equals(line_slip_if_flag)){
                                throw new Exception("전표삭제는 저장됨, 검증됨, 검증오류, 반려, (ERP생성되지 않은)이관오류 상태만 삭제 가능합니다.");
                            }
                        }

                        //헤더 삭제 FLAG 코드변경
                        Optional<SlipHeader> slipHeader =  slipHeaderRepository.findByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                        if(slipHeader.isPresent()) {
                            slipHeader.ifPresent(c -> {
                                c.setDeleteFlag();
                            });
                        }

                        // erp 헤더 삭제 FLAG 코드변경
                        Optional<ErpSlipHeader> erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndSlipHeaderId(Integer.valueOf(line_company_code), line_slip_header_id);
                        if(erpSlipHeader.isPresent()) {
                            erpSlipHeader.ifPresent(c -> {
                                c.setDeleteFlag();
                            });
                        }

                        // 세금 테이블 삭제
                        if(xxsbDtiInvoiceRepository.countByInvoiceNumAndApproveId(line_slip_no,line_taxSmartbillNo) > 0){
                            xxsbDtiInvoiceRepository.deleteByInvoiceNumAndApproveId(line_slip_no,line_taxSmartbillNo);
                        }

                        //결재 헤더 수정
                        Optional<ApprovalHeader> approvalHeader = approvalHeaderRepository.findByCompCdAndApprGroupId(line_company_code, line_slip_header_id);
                        if(approvalHeader.isPresent()) {
                            approvalHeader.ifPresent(c -> {
                                c.setSlipStatusSD();
                            });
                        }

                        //카드데이터 초기화
                        Optional<CardUseList>  cardUseList = cardUseListRepository.findByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                        if(cardUseList.isPresent()) {
                            cardUseList.ifPresent(c -> {
                                c.resetStatus();
                            });
                        }


                        // 교통비 전표 디테일/헤더 삭제
                        if("SPAP003".equals(line_slip_type)){
                            slipTrafficDtRepository.deleteByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                            Optional<SlipTrafficDt> slipTrafficDt = slipTrafficDtRepository.findByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                            if(!slipTrafficDt.isPresent()) {
                                slipTrafficHdRepository.deleteByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                            }
                        }

                        // 경조금 헤더 삭제
                        if("SPAP006".equals(line_slip_type)){
                            slipExpendRespository.deleteByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                        }

                        // bond 전표 헤더 삭제
                        if("SPAP010".equals(line_slip_type)){
                            bondHeaderRepository.deleteByCompCdAndSlipHeaderId(line_company_code,line_slip_header_id);
                        }

                        // 출장 전표 헤더 삭제
                        if("SPAP167".equals(trx_type_code)){
                            slipBusinessTripRepository.deleteByCompCdAndMasterSlipHeaderId(line_company_code,line_slip_header_id);
                        }

                        //선급반제 신청 모두 삭제(flag 업데이트)
                        List<PrepaymentApply> prepaymentApplys = prepaymentApplyRepository.findAllByOrgIdAndSlipHeaderIdAndLedgerId(Integer.valueOf(line_company_code),line_slip_header_id,line_ledger_id);
                        for(PrepaymentApply prepaymentApply : prepaymentApplys){
                            prepaymentApply.updateFlag(prepaymentApply.getAmountToApply(),personId);
                        }

                    }

                }

            }else{

                // 그룹 전표가 아닐때
                List<SlipHeaderDto> slipHeaderDto = slipQdslRepository.getSlipStatus(compCd,approval_group_id);

                if(slipHeaderDto != null && slipHeaderDto.size() > 0){

                    String line_status = slipHeaderDto.get(0).getStatus();
                    String line_slip_if_flag = slipHeaderDto.get(0).getSlipIfFlag();
                    String line_slip_no = slipHeaderDto.get(0).getSlipNo();
                    String line_taxSmartbillNo = slipHeaderDto.get(0).getTaxSmartbillNo();
                    if(!("SV".equals(line_status) || "VC".equals(line_status) || "VE".equals(line_status) || "IE".equals(line_status))){
                        if("Y".equals(line_slip_if_flag)){
                            throw new Exception("전표삭제는 저장됨, 검증됨, 검증오류, (ERP생성되지 않은)이관오류 상태만 삭제 가능합니다.");
                        }
                    }

                    //헤더 삭제 FLAG 코드변경
                    Optional<SlipHeader> slipHeader =  slipHeaderRepository.findByCompCdAndSlipHeaderId(compCd,slip_header_id);
                    if(slipHeader.isPresent()) {
                        slipHeader.ifPresent(c -> {
                            c.setDeleteFlag();
                        });
                    }

                    // erp 헤더 삭제 FLAG 코드변경
                    Optional<ErpSlipHeader> erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndSlipHeaderId(Integer.valueOf(compCd), slip_header_id);
                    if(erpSlipHeader.isPresent()) {
                        erpSlipHeader.ifPresent(c -> {
                            c.setDeleteFlag();
                        });
                    }

                    // 세금 테이블 삭제
                    if(xxsbDtiInvoiceRepository.countByInvoiceNumAndApproveId(line_slip_no,line_taxSmartbillNo) > 0){
                        xxsbDtiInvoiceRepository.deleteByInvoiceNumAndApproveId(line_slip_no,line_taxSmartbillNo);
                    }


                    //결재 헤더 수정
                    Optional<ApprovalHeader> approvalHeader = approvalHeaderRepository.findByCompCdAndApprGroupId(compCd, slip_header_id);
                    if(approvalHeader.isPresent()) {
                        approvalHeader.ifPresent(c -> {
                            c.setSlipStatusSD();
                        });
                    }

                    //카드데이터 초기화
                    List<CardUseList>  cardUseLists = cardUseListRepository.findAllByCompCdAndSlipHeaderId(compCd,slip_header_id);
                    for(CardUseList cardUseList: cardUseLists) {
                        cardUseList.resetStatus();
                    }

                    // 교통비 전표 디테일/헤더 삭제
                    if("SPAP003".equals(slip_type)){
                        slipTrafficDtRepository.deleteByCompCdAndSlipHeaderId(compCd,slip_header_id);
                        Optional<SlipTrafficDt> slipTrafficDt = slipTrafficDtRepository.findByCompCdAndSlipHeaderId(compCd,slip_header_id);
                        if(!slipTrafficDt.isPresent()) {
                            slipTrafficHdRepository.deleteByCompCdAndSlipHeaderId(compCd,slip_header_id);
                        }
                    }

                    // 경조금 헤더 삭제
                    if("SPAP006".equals(slip_type)){
                        slipExpendRespository.deleteByCompCdAndSlipHeaderId(compCd,slip_header_id);
                    }

                    // bond 전표 헤더 삭제
                    if("SPAP010".equals(slip_type)){
                        bondHeaderRepository.deleteByCompCdAndSlipHeaderId(compCd,slip_header_id);
                    }

                    // 출장 전표 헤더 삭제
                    if("SPAP167".equals(trx_type_code)){
                        slipBusinessTripRepository.deleteByCompCdAndMasterSlipHeaderId(compCd,slip_header_id);
                    }

                    //선급반제 신청 모두 삭제(flag 업데이트)
                    List<PrepaymentApply> prepaymentApplys = prepaymentApplyRepository.findAllByOrgIdAndSlipHeaderIdAndLedgerId(Integer.valueOf(compCd),slip_header_id,ledger_id);
                    for(PrepaymentApply prepaymentApply : prepaymentApplys){
                        prepaymentApply.updateFlag(prepaymentApply.getAmountToApply(),personId);
                    }

                }

            }

        }// end for

        return "삭제되었습니다.";
    }


    @Override
    public Map<String, String> changeAppr(List<SlipHistoryDto> slipHistoryDto) throws Exception {
        Map<String,String> map = new HashMap<>();

        for(int i=0; i<slipHistoryDto.size(); i++) {

            Optional<ApprovalHeader> getApprHd = approvalHeaderRepository.findByCompCdAndApprGroupId(util.getLoginCompCd(), slipHistoryDto.get(i).getApprovalGroupId());

            ApprovalHeader approvalHeader = null;
            if (getApprHd.isPresent()) {
                approvalHeader = getApprHd.get();
            } else {
                throw (new Exception("검인 라인을 조회 할 수 없습니다."));
            }

            Optional<Employee> getEmp = employeeRepository.findByCompCdAndEmpNo(util.getLoginCompCd(), slipHistoryDto.get(i).getChangeNo());

            Employee emp = null;
            if (getEmp.isPresent()) {
                emp = getEmp.get();
            }else{
                throw (new Exception("사원정보가 없습니다."));
            }

            approvalDetailRepository.changeApprDt(emp.getEmpNo(), emp.getEmpNm(), emp.getDeptNm(), emp.getJobNm(), approvalHeader.getApprGroupId(), approvalHeader.getNextStage());

            approvalHeaderRepository.changeApprHd(emp.getEmpNo(), approvalHeader.getApprGroupId());

        }

        map.put("message", "변경되었습니다.");
        return map;
    }

    @Override
    public ResponseEntity<String> reuseSlip(SlipDto slipDto) {
        String loginCompCd = util.getLoginCompCd();
        Employee loginEmp = util.getLoginUser().getEmployee();

        SlipHeader slipHeader =  slipHeaderRepository.findByCompCdAndSlipNo(loginCompCd, slipDto.getSlipNo())
            .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
        ErpSlipHeader erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndSlipHeaderId(Integer.parseInt(loginCompCd), slipHeader.getSlipHeaderId())
            .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));

        String slipTypeCd = slipHeader.getSlipTypeCd();
        String status = slipHeader.getStatus();
        // 법인카드(출장비)이거나 세금계산서일 때
        if(SlipTypeCd.CARD.getCode().equals(slipTypeCd) || SlipTypeCd.ACARD.getCode().equals(slipTypeCd) || SlipTypeCd.TRIP.getCode().equals(slipTypeCd) || hasText(slipHeader.getTaxSmartbillNo())) {
            // 반려가 아니면
            if(!(SlipStatusType.AR.getCode().equals(status) || SlipStatusType.CR.getCode().equals(status) || SlipStatusType.SC.getCode().equals(status))) {
                throw new SlipException("재사용할 수 없는 전표유형입니다.");
            }
        }

        // 구매전표, 인사전표, 선급금 정산전표
        if(SlipTypeCd.PO.getCode().equals(slipTypeCd) || SlipTypeCd.IM.getCode().equals(slipTypeCd) || SlipTypeCd.HR.getCode().equals(slipTypeCd)
            || "K".equals(erpSlipHeader.getPrepaymentApplyFlag())) {
            throw new SlipException("재사용이 불가한 전표유형입니다.");
        }

        // 재사용 가능 여부 N (재사용 불가능 전표)
        if("N".equals(slipHeader.getSlipReusePossibleYn())) {
            throw new SlipException("재사용할 수 없는 전표입니다.");
        }

        // 재사용 여부 Y (이미 재사용한 전표)
        if("Y".equals(slipHeader.getSlipCopyYn())) {
            throw new SlipException("이미 재사용하여 재사용이 불가한 전표입니다.");
        }

        List<SlipDetail> slipDetailList = slipDetailRepository.findAllByCompCdAndSlipNoOrderBySlipSeq(loginCompCd, slipDto.getSlipNo());
        List<ErpSlipLine> erpSlipLineList =  erpSlipLineRepository.findAllByOrgIdAndSlipHeaderId(loginCompCd, slipHeader.getSlipHeaderId());
        
        // 재사용
        Map<String, Object> slipNumberList = getSlipNumbers(erpSlipHeader.getTtypeInterfaceModule());
        String slipNoNew = (String) slipNumberList.get("slipNo");
        BigDecimal slipHeaderIdNew = (BigDecimal) slipNumberList.get("slipHeaderId");
        String slipGroupNoNew = (String) slipNumberList.get("slipGroupNo");

        String slipIfFlag = erpSlipHeader.getSlipIfFlag();

        // ERP 전송 여부 N일 경우
//        if("N".equals(slipIfFlag)){
//            // 통제예산 초기화
//            slipBudgetBranceCheck(slipHeader.getSlipHeaderId(), "Y");
//        }

        // 기존전표 복사하여 신규전표 카피
        SlipHeader slipHeaderCopy = SlipHeader.copy(slipNoNew, slipHeaderIdNew, slipGroupNoNew, slipHeader);
        slipHeaderRepository.save(slipHeaderCopy);
        if(SlipTypeCd.CARD.getCode().equals(slipTypeCd) || SlipTypeCd.ACARD.getCode().equals(slipTypeCd) || SlipTypeCd.TRIP.getCode().equals(slipTypeCd) || SlipTypeCd.BOND.getCode().equals(slipTypeCd) || hasText(slipHeader.getTaxSmartbillNo()) || "K".equals(erpSlipHeader.getPrepaymentApplyFlag())) {
            slipHeader.changeSlipCopyYn("Y");
        }


        // 카드내역 초기화
        if(SlipTypeCd.CARD.getCode().equals(slipTypeCd) || SlipTypeCd.ACARD.getCode().equals(slipTypeCd) || SlipTypeCd.TRIP.getCode().equals(slipTypeCd)){
            List<CardUseList> cardUseLists = cardUseListRepository.findAllByCompCdAndSlipHeaderId(loginCompCd, slipHeader.getSlipHeaderId());
            for(CardUseList cardUseList : cardUseLists) {
                cardUseList.resetSlipInfo();
            }
        }

        ErpSlipHeader erpSlipHeaderCopy = ErpSlipHeader.copy(slipNoNew, slipHeaderIdNew, slipGroupNoNew, erpSlipHeader, loginEmp);
        erpSlipHeaderRepository.save(erpSlipHeaderCopy);

        // 세금계산서 초기화 > 세금계산서가 존재하는 경우 재사용 못하도록 변경
        if(hasText(slipHeader.getTaxSmartbillNo()) || hasText(erpSlipHeader.getTaxSmartbillNo())) {
            throw new SlipException("세금계산서가 존재하여 재사용이 불가한 전표입니다.");
//            xxsbDtiInvoiceRepository.deleteByInvoiceNumAndApproveId(slipHeader.getSlipNo(), slipHeader.getTaxSmartbillNo());
//            slipHeader.changeTaxSmartBillNo("");
//            slipHeader.changeTaxbillSuId(null);
//            erpSlipHeader.changeTaxSmartbillNoAndGlobalAttribute14("", "");
//            erpSlipHeaderRepository.save(erpSlipHeader);
        }

        // slipHeader subheader 저장
        if(SlipTypeCd.EXPEND.getCode().equals(slipTypeCd)) {
            SlipExpend slipExpend = slipExpendRespository.findByCompCdAndSlipHeaderId(loginCompCd, slipHeader.getSlipHeaderId())
                .orElseThrow(() -> new RuntimeException("경조금이 존재하지 않습니다."));
            SlipExpend slipExpendCopy = SlipExpend.copy(slipNoNew, slipHeaderIdNew, slipExpend);
            slipExpendRespository.save(slipExpendCopy);
        }

        if(SlipTypeCd.TRIP.getCode().equals(slipTypeCd)) {
            List<SlipBusinessTrip> slipBusinessTripList = slipBusinessTripRepository.findAllByCompCdAndMasterSlipNoAndMasterSlipHeaderId(loginCompCd, slipHeader.getSlipNo(), slipHeader.getSlipHeaderId());
            for(SlipBusinessTrip slipBusinessTrip : slipBusinessTripList) {
                SlipBusinessTrip slipBusinessTripCopy = SlipBusinessTrip.copy(slipNoNew, slipHeaderIdNew, slipBusinessTrip);
                slipBusinessTripRepository.save(slipBusinessTripCopy);
            }
        }

        if(SlipTypeCd.TRAFFIC.getCode().equals(slipTypeCd)) {
            SlipTrafficHd slipTrafficHd = slipTrafficHdRepository.findById(new SlipTrafficHdKey(loginCompCd, slipHeader.getSlipNo()))
                .orElseThrow(() -> new RuntimeException("교통비가 존재하지 않습니다."));
            SlipTrafficHd slipTrafficHdCopy = SlipTrafficHd.copy(slipNoNew, slipHeaderIdNew, slipTrafficHd);
            slipTrafficHdRepository.save(slipTrafficHdCopy);
        }

        if(SlipTypeCd.BOND.getCode().equals(slipTypeCd)) {

            BondHeader bondHeader = bondHeaderRepository.findByCompCdAndSlipNo(loginCompCd, slipHeader.getSlipNo())
                .orElseThrow(() -> new RuntimeException("BOND 정보가 존재하지 않습니다."));
            // bond 전표 유효성 검사 확인
            if(bondHeaderQdslRepository.existsByTypeAndStatusIn(loginCompCd, bondHeader.getRefNo(), slipHeaderIdNew) && "Y".equals(bondHeader.getSplitEtcYn()) || "OVERSEA".equals(bondHeader.getType()) ) {
                throw new RuntimeException("해당 REF_NO로 결재완료된 국내수수료 BOND전표가 없으면 [분할 및 기타비용], [해외수수료] 전표를 진행할 수 없습니다.");
            }

            BondHeader bondHeaderCopy = BondHeader.copy(slipNoNew, slipHeaderIdNew, bondHeader);
            bondHeaderRepository.save(bondHeaderCopy);
        }

        for(int i = 0; i < slipDetailList.size(); i++) {
            BigDecimal slipLineIdNew = slipDetailRepository.getSlipLineId()
                .orElseThrow(() -> new RuntimeException("CBO_SP_SLIP_LINE_S.NEXTVAL ERROR"));
            String bungaeNoNew = slipDetailRepository.getBungaeNo(util.getLoginCompCd());
            SlipDetail slipDetail = slipDetailList.get(i);
            ErpSlipLine erpSlipLine = erpSlipLineList.get(i);

            SlipDetail slipDetailCopy = SlipDetail.copy(slipNoNew, slipHeaderIdNew, bungaeNoNew, slipLineIdNew, slipDetail);
            slipDetailRepository.save(slipDetailCopy);

            ErpSlipLine erpSlipLineCopy = ErpSlipLine.copy(slipHeaderIdNew, slipLineIdNew, erpSlipLineList.get(i), loginEmp);
            erpSlipLineRepository.save(erpSlipLineCopy);

            String cardUsedNo = erpSlipLine.getCardUsedNo();

            // 법인카드 정보 초기화 1. cardUsedNo 업데이트 2. cardUseList 업데이트
            if(SlipTypeCd.CARD.getCode().equals(slipTypeCd) || SlipTypeCd.ACARD.getCode().equals(slipTypeCd)) {
                CardUseList cardUseList = cardUseListRepository.findByCompCdAndUsedNo(loginCompCd, cardUsedNo)
                    .orElseThrow(() -> new RuntimeException("카드 내역이 존재하지 않습니다."));
                cardUseList.updateSlipInfo(slipNoNew, "SV", slipHeaderIdNew, slipLineIdNew);
            // 출장비 전표인 경우도 포함. 현금과 섞여 있기 때문에 분기 처리
            }else if (SlipTypeCd.TRIP.getCode().equals(slipTypeCd)) {
                Optional<CardUseList> cardUseListOptional = cardUseListRepository.findByCompCdAndUsedNo(loginCompCd, cardUsedNo);
                cardUseListOptional.ifPresent(cardUseList ->
                        cardUseList.updateSlipInfo(slipNoNew, "SV", slipHeaderIdNew, slipLineIdNew));
            }

        }

        // 교통비 detail 저장
        List<SlipTrafficDt> slipTrafficDtList = slipTrafficDtRepository.findAllByCompCdAndSlipHeaderId(loginCompCd, slipHeader.getSlipHeaderId());
        for(SlipTrafficDt slipTrafficDt: slipTrafficDtList) {
            SlipTrafficDt slipTrafficDtCopy = SlipTrafficDt.copy(slipNoNew, slipHeaderIdNew, slipTrafficDt);
            slipTrafficDtRepository.save(slipTrafficDtCopy);
        }

        // 예산 체크
//        List<SlipHistoryDto> cntList = slipRepositoryCustom.getCoaHierarchyCnt(slipHeaderIdNew, Integer.parseInt(loginCompCd), slipHeader.getLedgerId(), slipHeader.getPostingDt());
//        if(!cntList.isEmpty() && !cntList.get(0).getCount().equals(new BigDecimal(0))) {
//            slipRepositoryCustom.callBudgetCheckProcedure(slipHeaderIdNew);
//        }
        return new ResponseEntity<>(slipNoNew, HttpStatus.CREATED);
    }

    public void slipBudgetBranceCheck(BigDecimal slipHeaderId, String flag){

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_GL_BUDGET_PKG.AP_SLIP_ENCUMBRANCE");
        storedProcedureQuery.setParameter("p_slip_header_id", slipHeaderId);
        storedProcedureQuery.setParameter("p_reject_flag", flag);
        storedProcedureQuery.execute();

        String errFlag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");
        String errMsg = (String) storedProcedureQuery.getOutputParameterValue("x_err_msg");

        if(!"S".equals(errFlag)) {
            throw new RuntimeException(" Msg : " + errMsg);
        }
    }


    @Override
    public List<SlipDto> getTaxLocationCode(String deptCd){
        if(hasText(deptCd)){
            List<SlipDto> list = slipRepositoryCustom.getTaxLocationCode(deptCd);

            if(list.isEmpty()){
                list = slipRepositoryCustom.getTaxLocationCode("all");
            }

            return list;
        }else{
            return null;
        }
    }


    @Override
    public List<CoaHierarchyDto> getControlledAccount(){
        return slipQdslRepositoryImpl.getControlledAccount();
    }

}
