package com.iljin.apiServer.ijeas.approval;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

import com.iljin.apiServer.core.mPush.MobilePushService;
import com.iljin.apiServer.core.mail.MailDto;
import com.iljin.apiServer.core.mail.MailService;
import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.approval.dlgt.ApprovalDelegate;
import com.iljin.apiServer.ijeas.approval.dlgt.ApprovalDelegateDto;
import com.iljin.apiServer.ijeas.approval.dlgt.ApprovalDelegateKey;
import com.iljin.apiServer.ijeas.approval.dlgt.ApprovalDelegateRepository;
import com.iljin.apiServer.ijeas.approval.rule.ApprovalRule;
import com.iljin.apiServer.ijeas.approval.rule.ApprovalRuleDto;
import com.iljin.apiServer.ijeas.approval.rule.ApprovalRuleKey;
import com.iljin.apiServer.ijeas.approval.rule.ApprovalRuleRepository;
import com.iljin.apiServer.ijeas.bond.BondHeader;
import com.iljin.apiServer.ijeas.bond.BondHeaderDto;
import com.iljin.apiServer.ijeas.bond.BondHeaderQdslRepository;
import com.iljin.apiServer.ijeas.bond.BondHeaderRepository;
import com.iljin.apiServer.ijeas.card.CardUseListRepository;
import com.iljin.apiServer.ijeas.es.SlipType;
import com.iljin.apiServer.ijeas.es.bulk.ApPaymentsHdRepository;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeader;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipHeaderRepository;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipLineRepository;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsDtRepository;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsItemRepository;
import com.iljin.apiServer.ijeas.slip.*;
import com.iljin.apiServer.ijeas.slip.etax.XxsbDtiInvoiceRepository;
import com.iljin.apiServer.ijeas.slip.prepayment.PrepaymentApplyRepository;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodDto;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodQdslRepository;
import com.iljin.apiServer.ijeas.sm.close.ClosStatCd;
import com.iljin.apiServer.ijeas.system.confirm.Confirm;
import com.iljin.apiServer.ijeas.system.confirm.ConfirmRepository;
import com.iljin.apiServer.ijeas.system.delegate.DelegateDto;
import com.iljin.apiServer.ijeas.system.delegate.DelegateQdslRepository;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import com.iljin.apiServer.ijeas.system.emp.EmployeeKey;
import com.iljin.apiServer.ijeas.system.emp.EmployeeRepository;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final Util util;
    private final EmployeeRepository employeeRepository;
    private final MobilePushService pushService;

    private final AcctPeriodQdslRepository acctPeriodQdslRepository;
    private final ApprovalHeaderRepository approvalHeaderRepository;
    private final ApprovalDetailRepository approvalDetailRepository;
    private final ApprovalRepositoryCustom approvalRepositoryCustom;
    private final ApprovalDelegateRepository approvalDelegateRepository;
    private final ApprovalRuleRepository approvalRuleRepository;
    private final ApprovalQdslRepository approvalQdslRepository;
    private final SlipHeaderRepository slipHeaderRepository;
    private final SlipQdslRepository slipQdslRepository;
    private final ErpSlipHeaderRepository erpSlipHeaderRepository;
    private final ErpSlipLineRepository erpSlipLineRepository;
    private final CardUseListRepository cardUseListRepository;
    private final BondHeaderQdslRepository bondHeaderQdslRepository;
    private final BondHeaderRepository bondHeaderRepository;
    private final ConfirmRepository confirmRepository;
    private final Environment environment;
    private final MailService mailService;
    private final ApPaymentsDtRepository apPaymentsDtRepository;
    private final ApPaymentsItemRepository apPaymentsItemRepository;
    private final ApPaymentsHdRepository apPaymentsHdRepository;
    private final PrepaymentApplyRepository prepaymentApplyRepository;
    private final XxsbDtiInvoiceRepository xxsbDtiInvoiceRepository;
    private final DelegateQdslRepository delegateQdslRepository;


    private String getApprNo() {
        return approvalHeaderRepository.getApprNo(util.getLoginCompCd());
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ApprovalHeaderDto> getApprTodoList(ApprovalHeaderDto approvalHeaderDto) {
        String methodName = new Object() {}
                .getClass()
                .getEnclosingMethod()
                .getName();
        log.info("Service.Method : " + methodName);
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);

        return approvalQdslRepository.getApprTodoList(approvalHeaderDto);
//        return approvalRepositoryCustom.getApprTodoList(approvalHeaderDto);
    }

    @Override
    public List<ApprovalHeaderDto> getApprDoneList(ApprovalHeaderDto approvalHeaderDto) {
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);

        return approvalQdslRepository.getApprDoneList(approvalHeaderDto);
//        return approvalRepositoryCustom.getApprDoneList(approvalHeaderDto);
    }

    @Override
    public List<ApprovalHeaderDto> getApprReqList(ApprovalHeaderDto approvalHeaderDto) {
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);

        return approvalQdslRepository.getApprReqList(approvalHeaderDto);
//        return approvalRepositoryCustom.getApprReqList(approvalHeaderDto);
    }

    @Override
    public List<ApprovalHeaderDto> getRefList(ApprovalHeaderDto approvalHeaderDto) {
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);

        return approvalQdslRepository.getRefList(approvalHeaderDto);
//        return approvalRepositoryCustom.getApprReqList(approvalHeaderDto);
    }

    @Override
    public Map<String, Object> getApproval(String docNo) {
        Map<String, Object> map = new HashMap<String, Object>();
//        String docMngNo = "";// 문서관리번호
//        String docModulType = "";// 문서 모듈 타입(EA/BD/AP/GR)

        List<ApprovalHeaderDto> apprHeader = approvalRepositoryCustom.getApprHeader(docNo);
        if (!apprHeader.isEmpty()) {
            List<ApprovalDetailDto> apprDetails = approvalRepositoryCustom.getApprDetail(docNo);

            map.put("apprHeader", apprHeader);
            map.put("apprDetails", apprDetails);
        }

//        docMngNo = apprHeader.get(0).getSlipNo();
//        docModulType = docMngNo.substring(0, 2);

        //TODO 전자결재에 가져올 정보 조회 및 응답 데이터 구성 ====================================================
/*
        if (docModulType.equals("EA")) {
            // 전표
            Optional<SlipHeaderDto> slipHeader = slipRepositoryCustom.getSlipHeader(docMngNo);
            slipHeader.ifPresent(header -> {
                // slipTypeCd : E1 (법인카드 추가조회)
                if (SlipType.CARD.getCode().equals(header.getSlipTypeCd())) {
                    CardSlipHeaderDto cardHeader = new CardSlipHeaderDto();
                    try {
                        PropertyUtils.copyProperties(cardHeader, header);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cardUseListRepositoryCustom.getCardUseList(cardHeader.getUseDtlsNo()).ifPresent(card -> {
                        cardHeader.setCrdPssrId(card.getCrdPssrId());
                        cardHeader.setCrdPssrNm(card.getCrdPssrNm());
                        cardHeader.setCrdPssrTitle(card.getCrdPssrTitle());
                        cardHeader.setCrdFgCd(card.getCrdFgCd());
                        cardHeader.setCrdFgNm(card.getCrdFgNm());
                        cardHeader.setApprDate(card.getApprDate());
                        cardHeader.setApprTime(card.getApprTime());
                        cardHeader.setMerchAddr(card.getMerchAddr());
                        cardHeader.setEtc3(card.getEtc3());
                        cardHeader.setMccName(card.getMccName());
                        cardHeader.setProcPeriod(card.getProcPeriod());
                        cardHeader.setTaxTypeCd(card.getTaxTypeCd());
                        cardHeader.setTaxTypeNm(card.getTaxTypeNm());
                        cardHeader.setTips(card.getTips());
                    });
                    map.put("slipHeader", cardHeader);
                } else {
                    map.put("slipHeader", header);
                }
                List<SlipDetailDto> slipDetails = slipRepositoryCustom.getSlipDetails(header.getEaSlipNo());
                map.put("slipDetails", slipDetails);
            });
        } else if (docModulType.equals("GR")) {
            // 그룹결재
            Optional<SlipGroupDto> slipGroupHeader = slipGroupRepositoryCustom.getSlipGroupHeader(docMngNo);
            if (slipGroupHeader.isPresent()) {
                SlipHistoryDto slipHistoryDto = new SlipHistoryDto();
                slipHistoryDto.setGrSlipNo(docMngNo);
                List<SlipHistoryDto> slipGroupDetails = slipManagementService.getSlipHistories(slipHistoryDto);

                map.put("slipGroupHeader", slipGroupHeader);
                map.put("slipGroupDetails", slipGroupDetails);
            }
        } else if (docModulType.equals("BD")) {
            // 예산
            Optional<BudgetHeaderDto> budgetHeader = budgetRepositoryCustom.getBudgetHeader(docMngNo);
            if (budgetHeader.isPresent()) {
                List<BudgetDetailDto> budgetDetails = budgetRepositoryCustom.getBudgetDetails(docMngNo);

                map.put("budgetHeader", budgetHeader);
                map.put("budgetDetails", budgetDetails);
            }
        }*/

        return map;
    }

    @Override
    public String cancelApproval(ApprovalHeaderDto approvalHeaderDto) {
        String loginCompCd = util.getLoginCompCd();
        BigDecimal apprGroupId = approvalHeaderDto.getApprGroupId();
        String slipNo = approvalHeaderDto.getSlipNo();

        if(!approvalHeaderDto.getDraftId().equals(util.getLoginId())) {
            DelegateDto delegateDto = DelegateDto.builder()
                .compCd(loginCompCd)
                .receiveUserId(util.getLoginId())
                .startDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .endDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .delegateStatCd("1").build();
            Boolean flag = false;
            List<DelegateDto> delegatorList = delegateQdslRepository.getPersonalList(delegateDto);
            for(DelegateDto dto: delegatorList) {
                if(approvalHeaderDto.getDraftId().equals(dto.getGiveUserId())) {
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                throw new ApprovalException("상신자 및 수임인 외에는 상신취소할 수 없습니다.");
            }
        }

        SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndSlipNoAndApprovalGroupId(loginCompCd,  slipNo, apprGroupId)
            .orElseThrow(() -> new RuntimeException("전표 정보가 존재하지 않습니다. 1"));
        if("Y".equals(slipHeader.getTransferType())) {
            throw new ApprovalException("중간 전송된 건입니다.");
        }

        if(!approvalQdslRepository.existsByFirstStage(approvalHeaderDto)) {
            throw new ApprovalException("결재가 이미 진행되었습니다.");
        }

        BigDecimal slipHeaderId = slipHeader.getSlipHeaderId();
        // header status 변경
        SlipHeader slipHeaderStatus =  slipHeaderRepository.findByCompCdAndSlipNoAndSlipHeaderId(loginCompCd, slipNo, slipHeaderId)
            .orElseThrow(() -> new RuntimeException("전표 정보가 존재하지 않습니다. 2"));
        slipHeaderStatus.changeStatus("SV");

        List<ErpSlipHeader> erpSlipHeaderList = erpSlipHeaderRepository.findAllByOrgIdAndSlipHeaderId(Integer.parseInt(loginCompCd), slipHeaderId);
        if(erpSlipHeaderList.size() > 0) {
            erpSlipHeaderRepository.updateSlipStatusAndValidationFlagAndSlipDataFixFlag(SlipStatusType.SV.getCode(), "N", "N", Integer.parseInt(loginCompCd), slipHeaderId);
        }
        erpSlipHeaderRepository.updateSlipStatusAndValidationFlagAndSlipDataFixFlagBySourceSlipId(SlipStatusType.SV.getCode(), "N", "N", Integer.parseInt(loginCompCd), slipHeaderId);

        // 카드데이터의 상태값도 03(결재진행중) 바꾸기위해서 추가
        cardUseListRepository.updateStatusByCompCdAndSlipHeaderIdAndFlag(SlipStatusType.SV.getCode(), loginCompCd, slipHeaderId, "07");

        // 결재 라인 삭제
        approvalDetailRepository.deleteByCompCdAndApprGroupId(loginCompCd, apprGroupId);
        
        //결재 헤더 삭제
        approvalHeaderRepository.deleteByCompCdAndApprGroupId(loginCompCd, apprGroupId);

        String slipType = slipHeader.getSlipType();
        if(SlipType.ITEM.getCode().equals(slipType) || SlipType.BULK.getCode().equals(slipType) || SlipType.BOND.getCode().equals(slipType)
        || SlipType.FUND.getCode().equals(slipType) || SlipType.CLCT.getCode().equals(slipType) || SlipType.BUDGET.getCode().equals(slipType)
        || SlipType.PJT.getCode().equals(slipType)){

        } else {
            slipBudgetBranceCheck(slipHeaderId, "Y");
        }
        return "상신 취소되었습니다.";
    }

    @Override
    public String cancelApprovalBundle(List<ApprovalHeaderDto> approvalHeaderDtoList) {
        for(ApprovalHeaderDto approvalHeaderDto : approvalHeaderDtoList) {
            cancelApproval(approvalHeaderDto);
        }
        return "상신 취소되었습니다.";
    }

    @Override
    public ResponseEntity<String> doApproval(ApprovalDetailDto approvalDetailDto) {
        String loginCompCd = util.getLoginCompCd();
        BigDecimal apprGroupId = approvalDetailDto.getApprGroupId();
        String nextType = "";
        String currentType = "";
        // 결재 헤더 결재순번 구하기
        List<ApprovalHeaderDto> approvalHeaderDtos = approvalQdslRepository.getNextStage(approvalDetailDto);

        Integer nextStage = null;
        Integer nextStageNext = null;
        if(!approvalHeaderDtos.isEmpty()) {
            ApprovalHeaderDto approvalHeaderDto = approvalHeaderDtos.get(0);
            nextStage = Integer.parseInt(approvalHeaderDto.getNextStage());
            nextStageNext = Integer.parseInt(approvalHeaderDto.getNextStage()) + 1;
            currentType = approvalHeaderDto.getApprTypeCd();
        }

        String nextStageStr = String.valueOf(nextStage);
        String nextStageNextStr = String.valueOf(nextStageNext);
        // 본인 결재, 검인 유무 로직 추가
        if(approvalRepositoryCustom.getApprCnt(approvalDetailDto.getApprGroupId(), nextStageStr).get(0).getCnt().equals(new BigDecimal(0))) {
            throw new RuntimeException("이미 처리되었습니다.");
        }
        // 다음 결재 detail 불러오기
        ApprovalDetail approvalDetail = approvalDetailRepository.findByCompCdAndApprGroupIdAndApprStage(util.getLoginCompCd(), apprGroupId, String.valueOf(nextStageNext))
                .orElse(null);
        String appCheck = "end";
        String nextAppUserId = "";

        // 다음 결재가 있을경우 결재마스터 업데이트
        if(nonNull(approvalDetail)) {
            appCheck = "ing";
            nextType = approvalDetail.getApprTypeCd();
            nextAppUserId = approvalDetail.getAprverId();
        }

        // 최종 결재, 다음 검인 or 최종 결재, 뒤로 검인 없음
        if(("01".equals(currentType) && "02".equals(nextType)) || ("01".equals(currentType) && "end".equals(appCheck))) {

            List<ErpSlipHeader> erpSlipHeaderList = erpSlipHeaderRepository.findAllByOrgIdAndApprovalGroupIdAndSlipDisplayFlagEquals(
                Integer.parseInt(util.getLoginCompCd()), apprGroupId, "Y");
            for (ErpSlipHeader erpSlipHeader : erpSlipHeaderList) {
                if (!"Y".equals(erpSlipHeader.getSlipForcedIfFlag())) {
                    String slipIfFlag = erpSlipHeader.getSlipIfFlag();
                    if ((slipIfFlag.equals("N") || slipIfFlag.equals("E")) && (isNull(erpSlipHeader.getStdInvoiceTrxId()) || "".equals(erpSlipHeader.getStdInvoiceTrxId()))) {
                        erpSlipHeaderRepository.updateApprovalCompleteFlagByOrgIdAndApprovalGroupId("Y", Integer.parseInt(loginCompCd), apprGroupId);
                        approvalRepositoryCustom.callCreateSlipConcurrent(apprGroupId);
                    }
                }
            }

//            erpSlipHeaderRepository.updateSlipFlags(Integer.parseInt(loginCompCd), apprGroupId);
        }

        // 다음 결재 또는 검인 존재
        ApprovalHeader approvalHeader = approvalHeaderRepository.findByCompCdAndApprGroupId(loginCompCd, apprGroupId)
            .orElseThrow(() -> new RuntimeException("결재가 존재하지 않습니다."));
        if("ing".equals(appCheck)) {
            if("01".equals(nextType)) {
                approvalHeader.updateForNextStage(nextStageNextStr, SlipStatusType.AP.getCode(), nextAppUserId);
                String receiveMailUser = nextAppUserId + "@iljin.co.kr";
                String receiveMailUserNm = approvalDetail.getAprverNm();

                Employee sendMailUser = employeeRepository.findById(new EmployeeKey(approvalHeader.getDraftId(), util.getLoginCompCd()))
                    .orElseThrow(() -> new RuntimeException("기안자 정보가 존재하지 않습니다."));
                if(hasText(receiveMailUserNm)) {
                    String sendMailId = "system@iljin.co.kr";
                    String sendMailUserNm = sendMailUser.getEmpNm();
                    String mailSubject = "[결재진행] 기안자 ["+ sendMailUserNm +"]이 상신한 전자전표가 결재 요청 되었습니다. ";
                    String mailText = "";
                    mailText =  "전자전표_[결재진행] 기안자 ["+ sendMailUserNm +"]이 상신한 전자전표가 결재 요청 되었습니다. <br><br><a href='http://"
                        + environment.getProperty("server.domain-name")
                        + "/apprPendLst' target='_blank'>전자결재문서 Open</a>";

                    MailDto mailDto = MailDto.builder()
                        .from(sendMailId)
                        .to(receiveMailUser)
                        .subject(mailSubject)
                        .text(mailText)
                        .build();

                    mailService.sendSimpleMessage(mailDto);
                }
                
            } else if("02".equals(nextType)) {
                approvalHeader.updateForNextStage(nextStageNextStr, SlipStatusType.CP.getCode(), nextAppUserId);
                erpSlipHeaderRepository.updateSlipStatusByOrgIdAndApprovalGroupId(SlipStatusType.CP.getCode(), Integer.parseInt(loginCompCd), apprGroupId);
                SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndSlipHeaderIdAndApprovalGroupId(loginCompCd, apprGroupId, apprGroupId)
                    .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
                slipHeader.changeStatus(SlipStatusType.CP.getCode());
            }

        } else if("end".equals(appCheck)) {
            // 이번이 결재 or 검인 완료

            // 지금 결재가 마지막일 경우 결재 업데이트
            approvalHeader.updateForNextStage("", SlipStatusType.CC.getCode(), "");

            // slip header 결재완료로 상태값 변경
            SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndApprovalGroupId(loginCompCd,
                    apprGroupId)
                .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
            slipHeader.changeStatus(SlipStatusType.CC.getCode());

            // erp 전표헤더에 검인완료 상태값 변경
            erpSlipHeaderRepository.updateSlipStatusByOrgIdAndApprovalGroupId(SlipStatusType.CC.getCode(),
                Integer.parseInt(loginCompCd), apprGroupId);

            // 상신될경우 물고 있는 카드데이터의 상태값도 CC(결재완료) 바꾸기 위해서 추가
            approvalQdslRepository.updateCardUseList(loginCompCd, apprGroupId);

            if (SlipTypeCd.BOND.getCode().equals(approvalDetailDto.getSlipTypeCd())) {
                List<BondHeaderDto> bondHeaderDtos = bondHeaderQdslRepository.findAllByCompCdAndSlipNoAndSlipHeaderId(
                    loginCompCd, approvalDetailDto.getSlipNo(), apprGroupId);
                if (bondHeaderDtos.size() != 1) {
                    throw new RuntimeException("BOND가 존재하지 않습니다.");
                }

                // bond 전표 결재완료시 국내수수료건일경우 amend_seq를 증가시킨다(화면에서 amend관련하여
                // 컨트롤 편하게 하기위해서 최종결재완료시에 증가시켜준다.
                BondHeaderDto bondHeaderDto = bondHeaderDtos.get(0);
                if (bondHeaderQdslRepository.existsByLocalComplete(loginCompCd,
                    bondHeaderDto.getRefNo(), apprGroupId)
                    && "LOCAL".equals(bondHeaderDto.getType()) && !"Y".equals(
                    bondHeaderDto.getSplitEtcYn())) {
                    // 신규건이 아니면서 국내수수료이고, 분배 및 기타가 아닌 경우 amendSeq를 1 증가시킨다.
                    BondHeader bondHeader = bondHeaderRepository.findByCompCdAndSlipHeaderId(
                            loginCompCd, apprGroupId)
                        .orElseThrow(() -> new RuntimeException("BOND가 존재하지 않습니다"));
                    bondHeader.addOneToAmendSeq();
                }
            }

            if("01".equals(currentType)) {
                approvalRepositoryCustom.callConfirmCheck(apprGroupId);
            }
        }

        // 증빙 후첨 상태값 변경
        SlipHeader slipHeaderFlagUpdate = slipHeaderRepository.findByCompCdAndSlipNo(loginCompCd, approvalDetailDto.getSlipNo())
            .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
        slipHeaderFlagUpdate.changeEvidenceYn(approvalDetailDto.getEvidenceYn());

        // 결재 디테일 현재 결재자 완료 상태로 변경
        ApprovalDetail approvalDetailUpdate = approvalDetailRepository.findByCompCdAndApprGroupIdAndApprStage(loginCompCd, apprGroupId, String.valueOf(nextStage))
            .orElseThrow(() -> new RuntimeException("결재가 존재하지 않습니다."));
        if("end".equals(appCheck)) {
            approvalDetailUpdate.changeStatus(SlipStatusType.CC.getCode());
        } else {
            if("01".equals(currentType)) {
                approvalDetailUpdate.changeStatus(SlipStatusType.AP.getCode());
            }
        }

        Employee aAprver = employeeRepository.findById(new EmployeeKey(util.getLoginId(), loginCompCd))
                .orElseThrow(() -> new RuntimeException("사원이 존재하지 않습니다."));
        approvalDetailUpdate.changeApprDesc(approvalDetailDto.getApprDesc());
        approvalDetailUpdate.changeAAprver(aAprver);


        return new ResponseEntity<>("결재되었습니다.", HttpStatus.OK);
    }

    @Override
    public String doApprovalBundle(List<ApprovalDetailDto> approvalDetailDtos) {
        for(ApprovalDetailDto approvalDetailDto : approvalDetailDtos) {
            this.doApproval(approvalDetailDto);
        }
        return "결재가 완료되었습니다.";
    }

    @Override
    public ResponseEntity<String> rejectApproval(ApprovalHeaderDto approvalHeaderDto) {

        String loginCompCd = util.getLoginCompCd();
        BigDecimal apprGroupId = new BigDecimal(approvalHeaderDto.getApprovalGroupId());

        SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndApprovalGroupId(loginCompCd, apprGroupId)
            .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));

        String slipTypeCd = slipHeader.getSlipTypeCd();
        String slipType = slipHeader.getSlipType();
        String slipNo = approvalHeaderDto.getSlipNo();
        BigDecimal slipHeaderId = slipHeader.getSlipHeaderId();

        String apprStage = "";
        String apprTypeCd = "";
        String apprDesc = approvalHeaderDto.getApprDesc();

        String postingDt = slipHeader.getPostingDt();

        // 전자전표 회계기간 체크
        checkAcctPeriod(postingDt);

        // 해당 스테이지 결재 정보 가져오기
        if(!SlipStatusType.CC.getCode().equals(slipHeader.getStatus())) {
            List<ApprovalHeaderDto> approvalHeaderDtos = approvalQdslRepository.findByCompCdAndSlipHeaderId(loginCompCd, apprGroupId);
            if(approvalHeaderDtos.size() != 1) {
                throw new RuntimeException("결재 상태 오류입니다.");
            }
            apprTypeCd = approvalHeaderDtos.get(0).getApprTypeCd();
            apprStage = approvalHeaderDtos.get(0).getApprStage();
        }

        // 상태값 변경
        String slipStatusUpdate = "";
        if(SlipStatusType.CC.getCode().equals(slipHeader.getStatus())) {
            slipStatusUpdate = SlipStatusType.SC.getCode();
        } else if(ApprovalType.TYPE_APPROVAL.getCode().equals(apprTypeCd)) {
            slipStatusUpdate = SlipStatusType.AR.getCode();
        } else if(ApprovalType.TYPE_SEAL.getCode().equals(apprTypeCd)) {
            slipStatusUpdate = SlipStatusType.CR.getCode();
        }

        // 결재 헤더 상태값 변경
        approvalHeaderRepository.updateStatusByCompCdAndApprGroupIdInSlipHeader(slipStatusUpdate, loginCompCd, apprGroupId);

        // 해당 스테이지 결재 디테일 상태값 변경
        if(!SlipStatusType.SC.getCode().equals(slipStatusUpdate)) {
            ApprovalDetail approvalDetail = approvalDetailRepository.findByCompCdAndApprGroupIdAndApprStage(loginCompCd, apprGroupId, apprStage)
                .orElseThrow(() -> new RuntimeException("결재가 존재하지 않습니다."));
            approvalDetail.changeStatus(slipStatusUpdate);
            approvalDetail.changeApprDesc(apprDesc);
            approvalDetail.changeAAprver(util.getLoginUser().getEmployee());
        } else {
            Integer maxStage = approvalQdslRepository.getMaxStage(loginCompCd, apprGroupId);
            Employee loginEmp = util.getLoginUser().getEmployee();
            ApprovalDetail approvalDetail = ApprovalDetail.builder()
                .compCd(loginCompCd)
                .apprGroupId(apprGroupId)
                .slipNo(slipHeader.getSlipNo())
                .apprNo(this.getApprNo())
                .apprTypeCd(ApprovalType.TYPE_SEAL.getCode())
                .apprStage(String.valueOf(maxStage + 1))
                .slipStatus(slipStatusUpdate)
                .aprverId(loginEmp.getEmpNo())
                .aprverNm(loginEmp.getEmpNm())
                .aprverDeptNm(loginEmp.getDeptNm())
                .aprverJobNm(loginEmp.getJobNm())
                .aAprverId(loginEmp.getEmpNo())
                .aAprverNm(loginEmp.getEmpNm())
                .aAprverJobNm(loginEmp.getJobNm())
                .aAprverDeptNm(loginEmp.getDeptNm())
                .apprDtm(LocalDateTime.now())
                .apprDesc(apprDesc)
                .build();
            approvalDetailRepository.save(approvalDetail);
        }


        // 전표 헤더 상태값 변경
        slipHeader.changeStatus(slipStatusUpdate);

        // ERP 취소 프로시저 호출
        List<ErpSlipHeader> erpSlipHeaderList = erpSlipHeaderRepository.findByOrgIdAndApprovalGroupId(Integer.parseInt(loginCompCd), apprGroupId);
        String prepaymentApplyFlag = "";
        for(ErpSlipHeader erpSlipHeader : erpSlipHeaderList) {
            prepaymentApplyFlag = erpSlipHeaderList.get(0).getPrepaymentApplyFlag();
            if("Y".equals(erpSlipHeader.getSlipIfFlag()) || "ING".equals(erpSlipHeader.getSlipIfFlag())){
                approvalRepositoryCustom.callSlipCancelConcurrent(apprGroupId);
            }
        }
        
        if(ApprovalType.TYPE_SEAL.getCode().equals(apprTypeCd)) {
            slipHeader.changeSlipReusePossibleYn(approvalHeaderDto.getSlipReusePossibleYn());
        }

        // erp 인터페이스 상태값 변경
        erpSlipHeaderRepository.updateSlipStatusAndSlipDataFixFlagByOrgIdAndApprovalGroupId(slipStatusUpdate, "N", Integer.parseInt(loginCompCd), apprGroupId);

        if(!"".equals(slipStatusUpdate)) {
            // 전표 재사용 N인 검인반려 || 선급금전표 || 구매전표 -> 법인카드 혹은 세금계산서 초기화
            if((ApprovalType.TYPE_SEAL.getCode().equals(apprTypeCd) && "N".equals(approvalHeaderDto.getSlipReusePossibleYn()))
                || "K".equals(prepaymentApplyFlag)
                || SlipTypeCd.PO.getCode().equals(slipTypeCd) || SlipTypeCd.IM.getCode().equals(slipTypeCd)) {
                // 법인카드 초기화
                cardUseListRepository.resetByCompCdAndApprovalGroupId(loginCompCd, apprGroupId);

                // 세금계산서 초기화
                String taxSmartbillNo = slipHeader.getTaxSmartbillNo();
                erpSlipHeaderRepository.updateTaxSmartbillNoAndGlobalAttribute14ByOrgIdAndApprovalGroupId("", "", Integer.parseInt(loginCompCd), apprGroupId);
                xxsbDtiInvoiceRepository.deleteByInvoiceNumAndApproveId(slipNo, taxSmartbillNo);
                slipHeader.changeTaxSmartBillNo("");
                slipHeader.changeTaxbillSuId("");
            } else {
                // 법인카드 미처리(반려) 상태로 업데이트
                cardUseListRepository.updateStatusByCompCdAndApprovalGroupId("RU", loginCompCd, apprGroupId);
            }
        }

        if("01".equals(approvalHeaderDto.getSlipType())) {

        } else if(SlipType.ITEM.getCode().equals(slipType)) {

            apPaymentsItemRepository.deleteByCompCdAndSlipNoAndSlipHeaderId(loginCompCd, slipNo, slipHeaderId);
            apPaymentsDtRepository.deleteByCompCdAndSlipNoAndSlipHeaderId(loginCompCd, slipNo, slipHeaderId);

        } else if(SlipType.BULK.getCode().equals(slipType)) {

            apPaymentsDtRepository.deleteByCompCdAndSlipNoAndSlipHeaderId(loginCompCd, slipNo, slipHeaderId);
            apPaymentsHdRepository.deleteByCompCdAndSlipNoAndSlipHeaderId(loginCompCd, slipNo, slipHeaderId);

        } else if(SlipType.FUND.getCode().equals(slipType)) {
        } else if(SlipType.CLCT.getCode().equals(slipType)) {
        } else if(SlipType.BUDGET.getCode().equals(slipType)) {
        } else if(SlipType.PJT.getCode().equals(slipType)) {

        } else {
            //선급반제 신청 모두 삭제(flag 업데이트)
            Employee emp = util.getLoginUser().getEmployee();
            BigDecimal personId = new BigDecimal(emp.getPersonId());
            BigDecimal ledgerId = new BigDecimal(emp.getLedgerId());
            prepaymentApplyRepository.updateByOrgIdAndSlipHeaderIdAndLedgerId("Y", personId, loginCompCd,
                apprGroupId, Integer.parseInt(loginCompCd), ledgerId);

        }

        if(SlipType.ITEM.getCode().equals(slipType) || SlipType.BULK.getCode().equals(slipType) || SlipType.BOND.getCode().equals(slipType)
        || SlipType.FUND.getCode().equals(slipType) || SlipType.CLCT.getCode().equals(slipType) || SlipType.BUDGET.getCode().equals(slipType)
        || SlipType.PJT.getCode().equals(slipType)) {
//            SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndApprovalGroupId(loginCompCd, apprGroupId)
//                .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
            slipHeader.changeStatus(SlipStatusType.SD.getCode());

            ApprovalHeader approvalHeader = approvalHeaderRepository.findByCompCdAndApprGroupId(loginCompCd, apprGroupId)
                .orElseThrow(() -> new RuntimeException("결재 정보가 존재하지 않습니다."));
            approvalHeader.changeSlipStatus("SD");
        } else {

            // 예산통제 관련 프로시저 콜 (통제예산 초기화)
            if("N".equals(erpSlipHeaderList.get(0).getSlipIfFlag())) {
                slipBudgetBranceCheck(slipHeaderId, "Y");
            }
        }
        
        // 반려시 기안자에게 메일 발송
        ApprovalHeader approvalHeader = approvalHeaderRepository.findByCompCdAndApprGroupId(loginCompCd, apprGroupId)
            .orElseThrow(() -> new RuntimeException("결재 정보가 존재하지 않습니다."));

        // 기안자 정보 불러오기
        String draftId = approvalHeader.getDraftId();
        Employee draftEmp = employeeRepository.findByCompCdAndEmpNo(loginCompCd, approvalHeader.getDraftId())
            .orElseThrow(() -> new RuntimeException("기안자 정보가 존재하지 않습니다."));
        if(hasText(draftId) && draftId.length() > 6) {
            draftId = draftId.substring(0, 6);
        }

        String receiveMailUser = draftId + "@iljin.co.kr";
        String receiveMailUserNm = draftEmp.getEmpNm();
        
        if(hasText(receiveMailUserNm)) {
            String sendMailId = "system@iljin.co.kr";
            String mailSubject = "[결재반려] 기안자 [" + receiveMailUserNm + "]이 상신한 전자전표가 반려되었습니다. ";
            String mailText = "";
            mailText = "전자전표_[결재반려] 기안자 [" + receiveMailUserNm
                + "]이 상신한 전자전표가 반려되었습니다. <br><br><a href='http://"
                + environment.getProperty("server.domain-name")
                + "/apprReqLst' target='_blank'>전자결재문서 Open</a>";

            MailDto mailDto = MailDto.builder()
                .from(sendMailId)
                .to(receiveMailUser)
                .subject(mailSubject)
                .text(mailText)
                .build();

                mailService.sendSimpleMessage(mailDto);
        }

        return new ResponseEntity<>("반려가 완료되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> rejectApprovalBundle(ApprovalHeaderDto approvalHeaderDto) {
        List<ApprovalHeaderDto> dtos = approvalHeaderDto.getApprovalHeaderDtos();
        for(ApprovalHeaderDto dto : dtos) {
            dto.setApprDesc(approvalHeaderDto.getApprDesc());
            rejectApproval(dto);
        }
        return new ResponseEntity<>("반려가 완료되었습니다.", HttpStatus.OK);
    }

//    @Override
//    public ResponseEntity<String> doApproval(ApprovalDetailDto approvalDetailDto) {
//
//        User loginUser = util.getLoginUser();
//        String loginId = loginUser.getLoginId();
//        String compCd = loginUser.getCompCd();
//
//        EmployeeKey empKey = new EmployeeKey(loginId, compCd);
//        Optional<Employee> emp = employeeRepository.findById(empKey);
//
//        String currentApproNo = approvalDetailDto.getApprNo();
//        Short currentApprSeq = approvalDetailDto.getApprSeq();
//        /*
//         * update table table : TB_APPR_DT
//         */
//        approvalDetailRepository.findByApprNoAndApprSeq(currentApproNo, currentApprSeq).ifPresent(c -> {
//            c.setApprFgCd(approvalDetailDto.getApprFgCd());
//            c.setApprDesc(approvalDetailDto.getApprDesc());
//            c.setApprDtm(LocalDateTime.now());
//            approvalDetailRepository.save(c);
//        });
//
//        /* max Approval Seq */
//        Optional<ApprovalDetail> approvalDetail = approvalDetailRepository
//                .findTopByApprNoOrderByApprSeqDesc(currentApproNo);
//        Short apprMaxSeq = approvalDetail.get().getApprSeq();
//
//        /*
//         * update Header Tables
//         */
//        approvalHeaderRepository.findById(currentApproNo).ifPresent(approvalHeader -> {
//            String docType = approvalHeader.getDocTypeCd(); // 결재문서 유형(전표/예산:SLIP/BDGT)
//            String docMngno = approvalHeader.getDocMngNo(); // 문서관리번호
//            String apprType = docMngno.substring(0, 2); // 그룹결재 구분을 위한 변수
//
//            if (approvalDetailDto.getApprFgCd().equals("A")) { // 승인
//                if (currentApprSeq == apprMaxSeq) {
//                    /*
//                     * TB_APPR_HD.DOC_STAT_CD = 'APR' 최종결재
//                     */
//                    // ApprovalHeader
//                    approvalHeader.setDocStatCd(ApprovalState.COMPLETED_APPROVAL.getCode());
//                    approvalHeader.setFnlSeq(currentApprSeq);
//                    approvalHeaderRepository.save(approvalHeader);
//
//                    // ApprovalDetail
//                    approvalDetailRepository.findByApprNoAndApprSeq(currentApproNo, currentApprSeq).ifPresent(c -> {
//                        c.setApprDesc(approvalDetailDto.getApprDesc());
//                        c.setAAprverId(loginId);
//                        c.setAAprverDeptNm(emp.get().getDeptNm());
//                        c.setAAprverJobNm(emp.get().getJobNm());
//                        c.setAAprverNm(emp.get().getEmpNm());
//                        approvalDetailRepository.save(c);
//                    });
//
//                } else {
//                    /*
//                     * TB_APPR_HD.DOC_STAT_CD = 'ING' 결재 진행
//                     */
//                    // ApprovalHeader
//                    approvalHeader.setDocStatCd(ApprovalState.PROGRESS_APPROVAL.getCode());
//                    approvalHeader.setFnlSeq(currentApprSeq);
//                    approvalHeaderRepository.save(approvalHeader);
//
//                    // ApprovalDetail
//                    approvalDetailRepository.findByApprNoAndApprSeq(currentApproNo, currentApprSeq).ifPresent(c -> {
//                        c.setApprDesc(approvalDetailDto.getApprDesc());
//                        c.setAAprverId(loginId);
//                        c.setAAprverDeptNm(emp.get().getDeptNm());
//                        c.setAAprverJobNm(emp.get().getJobNm());
//                        c.setAAprverNm(emp.get().getEmpNm());
//                        approvalDetailRepository.save(c);
//                    });
//                }
//            } else if (approvalDetailDto.getApprFgCd().equals("R")) {
//                // ApprovalHeader
//                approvalHeader.setDocStatCd(ApprovalState.REJECT_APPROVAL.getCode());
//                approvalHeader.setFnlSeq(currentApprSeq);
//                approvalHeaderRepository.save(approvalHeader);
//
//                // ApprovalDetail
//                approvalDetailRepository.findByApprNoAndApprSeq(currentApproNo, currentApprSeq).ifPresent(c -> {
//                    c.setApprDesc(approvalDetailDto.getApprDesc());
//                    c.setAAprverId(loginId);
//                    c.setAAprverDeptNm(emp.get().getDeptNm());
//                    c.setAAprverJobNm(emp.get().getJobNm());
//                    c.setAAprverNm(emp.get().getEmpNm());
//                    approvalDetailRepository.save(c);
//                });
//            }
//        });
//
//        return new ResponseEntity<>("결재하였습니다.", HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<String> requestApproval(ApprovalHeaderDto approvalHeaderDto) {

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        BigDecimal apprGroupId = approvalHeaderDto.getApprGroupId();
        String slipNo = approvalHeaderDto.getSlipNo();
        String slipType = approvalHeaderDto.getSlipType();
        BigDecimal slipHeaderId = approvalHeaderDto.getSlipHeaderId();
        //발생전표인 경우 : TRUE, 발생전표가 아닌경우 : FALSE
        Boolean accrualFlag = accrualSlipFlag(slipType);

        // ERP ValidationCheck 패키지 호출 (발생전표인 경우)

        if(Boolean.TRUE.equals(accrualFlag)) slipValidationCheck(apprGroupId);


        // 상신 여부 체크
        if(Boolean.TRUE.equals(approvalHeaderRepository.existsByCompCdAndApprGroupIdAndSlipNo(compCd, apprGroupId, slipNo))) {
            throw new ApprovalException("이미 상신된 문서입니다. (전표번호: " + slipNo + ")");
        }
        // 결재 헤더, 상세 저장
        saveApprovalHeaderDetail(compCd, approvalHeaderDto);


        // 발생전표만 체크 후 업데이트 (ERP전표, 비용예산전표,  체크, 업데이트 하지않음.)
        if(Boolean.TRUE.equals(accrualFlag)){
            // ERP 인터페이스 전표
            ErpSlipHeader erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndSlipNumberAndTrxTypeCodeAndSlipHeaderId(
                    Integer.parseInt(compCd),
                    slipNo,
                    slipType,
                    slipHeaderId).orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
            // ERP전표 Validation
            erpSlipHeaderValidation(erpSlipHeader);

            // 상신 상태 업데이트
            ErpSlipHeader byOrgIdAndSlipHeaderId = erpSlipHeaderRepository.findByOrgIdAndSlipHeaderId(
                    Integer.parseInt(compCd),
                    slipHeaderId).orElseThrow(() -> new ApprovalException("전표가 존재하지 않습니다."));
            byOrgIdAndSlipHeaderId.updateApprovalFlag(SlipStatusType.AP.getCode(), "Y", "Y");

            // 채권채무 동시생성 전표 처리위한 업데이트 해준다.(AS-IS로직)
            erpSlipHeaderRepository.findByOrgIdAndSourceSlipHeaderId(Integer.parseInt(compCd), slipHeaderId)
                    .stream()
                    .findFirst()
                    .ifPresent(u -> erpSlipHeaderRepository.updateSlipDataFixFlagByOrgIdAndSourceSlipHeaderId("Y", Integer.parseInt(compCd), slipHeaderId));

            // 카드데이터의 상태값도 03(결재진행중) 바꾸기위해서 추가 (AS-IS로직)
            cardUseListRepository.findAllByCompCdAndSlipHeaderIdAndStatusNotEqual(compCd, slipHeaderId, "07")
                    .stream()
                    .findFirst()
                    .ifPresent(u -> cardUseListRepository.updateStatusByCompCdAndSlipHeaderIdAndFlag(SlipStatusType.AP.getCode(), compCd, slipHeaderId, "07"));
        }

        // 전자전표 HEADER STATUS 변경
        SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndSlipNoAndSlipTypeAndSlipHeaderId(
                compCd,
                slipNo,
                slipType,
                slipHeaderId).orElseThrow(() -> new ApprovalException("전표가 존재하지 않습니다."));
        slipHeader.changeStatus("AP");

        // 발생전표만 체크 후 업데이트 (ERP전표, 비용예산전표는 체크X)
        if(Boolean.TRUE.equals(accrualFlag)) slipBudgetBranceCheck(slipHeaderId, "N");

        // 메일 발송
        sendApprReqMail(approvalHeaderDto);

        return new ResponseEntity<>("상신 완료되었습니다.", HttpStatus.CREATED);
    }

    private void sendApprReqMail(ApprovalHeaderDto approvalHeaderDto) {
        // 다음결재자 확인
        approvalHeaderDto.setNextStage("1");
        ApprovalHeaderDto nextAppUserInfo = approvalQdslRepository.getNextAppUser(approvalHeaderDto);
        String nextAppUserId = nextAppUserInfo.getNextAppUserId();
        if(hasText(nextAppUserId) && nextAppUserId.length() > 6) {
            nextAppUserId = nextAppUserId.substring(0, 6);
        }
        String receiveMailUser = nextAppUserId + "@iljin.co.kr";
        String receiveMailUserNm = nextAppUserInfo.getNextAppUserNm();
        String type = nextAppUserInfo.getApprTypeCd();

        Employee sendMailUser = employeeRepository.findById(new EmployeeKey(approvalHeaderDto.getDraftId(), util.getLoginCompCd()))
            .orElseThrow(() -> new RuntimeException("기안자 정보가 존재하지 않습니다."));
        if(hasText(receiveMailUserNm) && !"02".equals(type)) {
            String sendMailId = "system@iljin.co.kr";
            String sendMailUserNm = sendMailUser.getEmpNm();
            String mailSubject = "[결재진행] 기안자 ["+ sendMailUserNm +"]이 상신한 전자전표가 결재 요청 되었습니다. ";
            String mailText = "";
            mailText =  "전자전표_[결재진행] 기안자 ["+ sendMailUserNm +"]이 상신한 전자전표가 결재 요청 되었습니다. <br><br><a href='http://"
                + environment.getProperty("server.domain-name")
                + "/apprPendLst' target='_blank'>전자결재문서 Open</a>";

            MailDto mailDto = MailDto.builder()
                .from(sendMailId)
                .to(receiveMailUser)
                .subject(mailSubject)
                .text(mailText)
                .build();

            mailService.sendSimpleMessage(mailDto);
        }
    }

    private void saveApprovalHeaderDetail(String compCd, ApprovalHeaderDto approvalHeaderDto){

        /**
         * 1. 걸재헤더 저장
         * 2. 결재상세_결재라인 저장
         * 3. 결재상세_금액별검인라인 저장
         **/

        /** 1. 결재헤더 저장 **/
        ApprovalHeader approvalHeader = ApprovalHeader.builder()
                .compCd(compCd)
                .apprGroupId(approvalHeaderDto.getApprGroupId())
                .slipNo(approvalHeaderDto.getSlipNo())
                .slipType(approvalHeaderDto.getSlipType())
                .docTitleNm(approvalHeaderDto.getDocTitleNm())
                .docContents(approvalHeaderDto.getDocContents())
                .draftId(approvalHeaderDto.getDraftId())
                .slipStatus(SlipStatusType.AP.getCode())
                .nextAppUserId(approvalHeaderDto.getApprovalDetails().get(0).getAprverId())
                .nextStage("1")
                .glDate(approvalHeaderDto.getGlDate())
                .refUserId(approvalHeaderDto.getRefUserId())
                .sendUserId(approvalHeaderDto.getSendUserId())
                .remark(approvalHeaderDto.getRemark())
                .build();
        approvalHeaderRepository.save(approvalHeader);

        /** 2. 결재상세_결재라인 저장 **/
        if (!approvalHeaderDto.getApprovalDetails().isEmpty()) {

            int idx = 1;
            for (ApprovalDetail approvalDetail : approvalHeaderDto.getApprovalDetails()) {
                String apprTypeCd = approvalDetail.getApprTypeCd();

                EmployeeKey empKey = new EmployeeKey(approvalDetail.getAprverId(), compCd);
                Optional<Employee> emp = employeeRepository.findById(empKey);

                ApprovalDetail apprDt = ApprovalDetail.builder()
                        .compCd(compCd)
                        .apprGroupId(approvalHeaderDto.getApprGroupId())
                        .slipNo(approvalHeaderDto.getSlipNo())
                        .apprNo(this.getApprNo())
                        .apprTypeCd(ApprovalType.TYPE_APPROVAL.getCode()) // 01 : 결재, 02 : 검인
                        .apprStage(String.valueOf((idx++)))
                        .slipStatus(SlipStatusType.AP.getCode())
                        .aprverId(approvalDetail.getAprverId())
                        .aprverNm(emp.get().getEmpNm())
                        .aprverDeptNm(emp.get().getDeptNm())
                        .aprverJobNm(emp.get().getJobNm())
                        .build();
                approvalDetailRepository.save(apprDt);
            }

            /** 3. 결재상세_금액별검인라인 저장 발생전표만 검인라인 저장한다.(ERP전표, 비용예산, 시공전표는 검인하지 않음) **/
            if(Boolean.TRUE.equals(accrualSlipFlag(approvalHeaderDto.getSlipType()))){

                //경조금 선수금매출 - 회계팀 소유현(420534) 대리 최종 검인
                if(approvalHeaderDto.getSlipType().equals("SPAR010")){
                    EmployeeKey confirmEmpKey = new EmployeeKey("420534", compCd);
                    Optional<Employee> confirmEmp = employeeRepository.findById(confirmEmpKey);

                    ApprovalDetail apprDt = ApprovalDetail.builder()
                            .compCd(compCd)
                            .apprGroupId(approvalHeaderDto.getApprGroupId())
                            .slipNo(approvalHeaderDto.getSlipNo())
                            .apprNo(this.getApprNo())
                            .apprTypeCd(ApprovalType.TYPE_SEAL.getCode()) // 01 : 결재, 02 : 검인
                            .apprStage(String.valueOf((idx++)))
                            .slipStatus(SlipStatusType.AP.getCode())
                            .aprverId("420534")
                            .aprverNm(confirmEmp.get().getEmpNm())
                            .aprverDeptNm(confirmEmp.get().getDeptNm())
                            .aprverJobNm(confirmEmp.get().getJobNm())
                            .build();
                    approvalDetailRepository.save(apprDt);
                }else{
                    //경조금 거래유형 - 인사팀 왕지혜(440623) 사원 1번 검인라인 세팅
                    if(approvalHeaderDto.getSlipType().equals("SPAP006")){
                        EmployeeKey confirmEmpKey = new EmployeeKey("440623", compCd);
                        Optional<Employee> confirmEmp = employeeRepository.findById(confirmEmpKey);

                        ApprovalDetail apprDt = ApprovalDetail.builder()
                                .compCd(compCd)
                                .apprGroupId(approvalHeaderDto.getApprGroupId())
                                .slipNo(approvalHeaderDto.getSlipNo())
                                .apprNo(this.getApprNo())
                                .apprTypeCd(ApprovalType.TYPE_SEAL.getCode()) // 01 : 결재, 02 : 검인
                                .apprStage(String.valueOf((idx++)))
                                .slipStatus(SlipStatusType.AP.getCode())
                                .aprverId("440623")
                                .aprverNm(confirmEmp.get().getEmpNm())
                                .aprverDeptNm(confirmEmp.get().getDeptNm())
                                .aprverJobNm(confirmEmp.get().getJobNm())
                                .build();
                        approvalDetailRepository.save(apprDt);
                    }

                    //잡급비 거래유형 - 인사팀 김소영(300478) 과장 마지막 결재 라인 세팅
                    if(approvalHeaderDto.getSlipType().equals("SPAP074")
                            || approvalHeaderDto.getSlipType().equals("SPAP199")
                            || approvalHeaderDto.getSlipType().equals("SPAP200")){
                        EmployeeKey confirmEmpKey = new EmployeeKey("300478", compCd);
                        Optional<Employee> confirmEmp = employeeRepository.findById(confirmEmpKey);

                        ApprovalDetail apprDt = ApprovalDetail.builder()
                                .compCd(compCd)
                                .apprGroupId(approvalHeaderDto.getApprGroupId())
                                .slipNo(approvalHeaderDto.getSlipNo())
                                .apprNo(this.getApprNo())
                                .apprTypeCd(ApprovalType.TYPE_APPROVAL.getCode()) // 01 : 결재, 02 : 검인
                                .apprStage(String.valueOf((idx++)))
                                .slipStatus(SlipStatusType.AP.getCode())
                                .aprverId("300478")
                                .aprverNm(confirmEmp.get().getEmpNm())
                                .aprverDeptNm(confirmEmp.get().getDeptNm())
                                .aprverJobNm(confirmEmp.get().getJobNm())
                                .build();
                        approvalDetailRepository.save(apprDt);
                    }

                    /** 결재라인 금액별 검인 리스트 저장(음수인 경우 절대값으로 리스트 저장)**/

                    SlipHeader slipheader = slipHeaderRepository.findBySlipNo(approvalHeaderDto.getSlipNo()).orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
                    BigDecimal usedAmt = new BigDecimal(slipheader.getUsedAmt()).abs();
//                    List<Confirm> confirmList = confirmRepository.findConfirmList(compCd,approvalHeaderDto.getDeptCd(),approvalHeaderDto.getTotalAmt().abs());
                    List<Confirm> confirmList = confirmRepository.findConfirmList(compCd,approvalHeaderDto.getDeptCd(),usedAmt); // 프론트에서 외화 값을 잘못 가져오는 경우가 있어 로직 변경

                    for (Confirm confirm : confirmList) {
                        EmployeeKey confirmEmpKey = new EmployeeKey(confirm.getConfirmUserId(), compCd);
                        Optional<Employee> confirmEmp = employeeRepository.findById(confirmEmpKey);

                        ApprovalDetail apprDt = ApprovalDetail.builder()
                                .compCd(compCd)
                                .apprGroupId(approvalHeaderDto.getApprGroupId())
                                .slipNo(approvalHeaderDto.getSlipNo())
                                .apprNo(this.getApprNo())
                                .apprTypeCd(ApprovalType.TYPE_SEAL.getCode()) // 01 : 결재, 02 : 검인
                                .apprStage(String.valueOf((idx++)))
                                .slipStatus(SlipStatusType.AP.getCode())
                                .aprverId(confirm.getConfirmUserId())
                                .aprverNm(confirmEmp.get().getEmpNm())
                                .aprverDeptNm(confirmEmp.get().getDeptNm())
                                .aprverJobNm(confirmEmp.get().getJobNm())
                                .build();
                        approvalDetailRepository.save(apprDt);
                    }

                    // 검인 라인 없이 상신하는 경우 체크
                    if (confirmList.isEmpty()) {
                        throw new ApprovalException("검인라인이 존재하지 않습니다. \n 관리자에게 문의 하여 주세요.");
                    }
                }

            }
        } else {
            throw new ApprovalException("결재라인이 존재하지 않습니다.");
        }
    }

    private Boolean accrualSlipFlag(String slipType) {
        /*
         * 일반전표(ERP전표X) - 아래 전표리스트가 아닌 경우 TRUE
         * @param slipType : 21 건별지급 (ITEM)
         *      : 22 대량지급 (BULK)
         *      : 23 전자채권만기 (BOND)
         *      : 24 자금전표 (FUND)
         *      : 25 집금전표 (CLCT)
         *      : 27 GL전표 (GL)
         *      : 28 매출전표 (SALE)
         *      : 29 해외전표 (FRGN)
         *      : 90 비용예산 (BUDGET)
         *      : 91 PRJ실행예산기안(PJT)
         * */
        List<String> nonAccrualList = Arrays.asList(SlipType.ITEM.getCode(), SlipType.BULK.getCode(),
                                                    SlipType.BOND.getCode(), SlipType.FUND.getCode(),
                                                    SlipType.CLCT.getCode(), SlipType.GL.getCode(),
                                                    SlipType.SALE.getCode(), SlipType.FRGN.getCode(),
                                                    SlipType.BUDGET.getCode(), SlipType.PJT.getCode());
        return !nonAccrualList.contains(slipType);
    }

    private static void erpSlipHeaderValidation(ErpSlipHeader erpSlipHeader) {
        if(!(erpSlipHeader.getSlipStatus().equals("VC") && erpSlipHeader.getValidationFlag().equals("Y"))){
            throw new ApprovalException("검증에 문제가 있습니다.");
        }
        if(erpSlipHeader.getSlipDataFixFlag().equals("Y")){
            throw new ApprovalException("이미 상신된 전표 입니다.");
        }
        if(erpSlipHeader.getApprovalCompleteFlag().equals("Y")){
            throw new ApprovalException("전표 상태값이 잘못됐습니다. 시스템 담당자에게 문의 바랍니다.");
        }
        if(erpSlipHeader.getSlipForcedIfFlag().equals("Y")){
            throw new ApprovalException("이미 ERP로 전송된 전표 입니다. 시스템 담당자에게 문의 바랍니다.");
        }
    }

    @Override
    public ResponseEntity<String> requestApprovalSingle(ApprovalHeaderDto approvalHeaderDto) {
        String message = "상신되었습니다.";
        this.requestApproval(approvalHeaderDto);
//        try {
//            this.requestApproval(approvalHeaderDto);
//        }catch (ApprovalException e) {
//            erpSlipHeaderRepository.updateValidationFlagAndSlipStatusByOrgIdAndApprovalGroupId("N", "SV", Integer.parseInt(util.getLoginCompCd()), approvalHeaderDto.getApprGroupId());
//            erpSlipLineRepository.updateValidationFlagByOrgIdAndSlipHeaderId("N", util.getLoginCompCd(), approvalHeaderDto.getApprGroupId());
//            message = e.getMessage();
//        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @Override
    public String requestApprovalBundle(ApprovalGroup approvalGroup) {
        String message = "상신되었습니다.";
        List<String> errMsgs = new ArrayList<>();
        Integer failCount = 0;
        // 실패
        List<ApprovalHeaderDto> approvalHeaderDtos = approvalGroup.getApprovalHeaderDtos();
        List<ApprovalDetail> approvalDetails = approvalGroup.getApprovalDetails();
        for(int i = 0; i < approvalHeaderDtos.size(); i++ ) {
            ApprovalHeaderDto approvalHeaderDto = approvalHeaderDtos.get(i);
            approvalHeaderDto.setApprovalDetails(approvalDetails);
//            this.requestApproval(approvalHeaderDto);
            try {
                this.requestApproval(approvalHeaderDto);
            }catch (ApprovalException e) {
                failCount++;
                erpSlipHeaderRepository.updateValidationFlagAndSlipStatusByOrgIdAndApprovalGroupId("N", "SV", Integer.parseInt(util.getLoginCompCd()), approvalHeaderDto.getApprGroupId());
                erpSlipLineRepository.updateValidationFlagByOrgIdAndSlipHeaderId("N", util.getLoginCompCd(), approvalHeaderDto.getApprGroupId());
                approvalHeaderRepository.deleteByCompCdAndApprGroupId(util.getLoginCompCd(),approvalHeaderDto.getApprGroupId());
                approvalDetailRepository.deleteByCompCdAndApprGroupId(util.getLoginCompCd(),approvalHeaderDto.getApprGroupId());
                errMsgs.add(approvalHeaderDto.getSlipNo() + " : " + e.getMessage() + "<br/>");
            }
        }

        // 에러 메시지 생성
        StringBuilder sb = new StringBuilder();
        if(failCount > 0) {
            Integer successCount = approvalHeaderDtos.size() - failCount;
            sb.append(successCount + "건 성공 " + failCount + "건 실패 <br/>");
            for(int i = 0; i < errMsgs.size(); i++) {
                sb.append(errMsgs.get(i));
            }
            message = sb.toString();
        }
        return message;
    }

    @Override
    public ResponseEntity<String> verifySlip(ApprovalDetailDto approvalDetailDto) {
        String loginCompCd = util.getLoginCompCd();
        BigDecimal apprGroupId = approvalDetailDto.getApprGroupId();
        String nextType = "";
        String currentType = "";
        // 결재 헤더 결재순번 구하기
        List<ApprovalHeaderDto> approvalHeaderDtos = approvalQdslRepository.getNextStage(approvalDetailDto);

        Integer nextStage = null;
        Integer nextStageNext = null;

        if(!approvalHeaderDtos.isEmpty()) {
            ApprovalHeaderDto approvalHeaderDto = approvalHeaderDtos.get(0);
            nextStage = Integer.parseInt(approvalHeaderDto.getNextStage());
            nextStageNext = Integer.parseInt(approvalHeaderDto.getNextStage()) + 1;
            currentType = approvalHeaderDto.getApprTypeCd();
        }

        String nextStageStr = String.valueOf(nextStage);
        String nextStageNextStr = String.valueOf(nextStageNext);

        // 본인 결재, 검인 유무 로직 추가
        if(approvalRepositoryCustom.getApprCnt(approvalDetailDto.getApprGroupId(), nextStageStr).get(0).getCnt().equals(new BigDecimal(0))) {
            throw new RuntimeException("이미 처리되었습니다.");
        }
        // 다음 결재 detail 불러오기
        ApprovalDetail approvalDetail = approvalDetailRepository.findByCompCdAndApprGroupIdAndApprStage(loginCompCd, apprGroupId, String.valueOf(nextStageNext))
            .orElse(null);
        String appCheck = "end";
        String nextAppUserId = "";

        // 다음 결재가 있을경우 결재마스터 업데이트
        if(nonNull(approvalDetail)) {
            appCheck = "ing";
            nextType = approvalDetail.getApprTypeCd();
            nextAppUserId = approvalDetail.getAprverId();
        }

        if("02".equals(currentType)) {
            List<ErpSlipHeader> erpSlipHeaderList = erpSlipHeaderRepository.findAllByOrgIdAndApprovalGroupIdAndSlipDisplayFlagEquals(Integer.parseInt(loginCompCd), apprGroupId, "Y");
            for(ErpSlipHeader erpSlipHeader: erpSlipHeaderList) {
                if(!"Y".equals(erpSlipHeader.getSlipIfFlag())) {
                    throw new ApprovalException("전표전송 상태가 올바르지 않습니다. 시스템 담당자에게 문의하여 해결 후 검인 진행바랍니다.");
                }
            }
        }

        // 다음 결재 또는 검인자가 존재
        ApprovalHeader approvalHeader = approvalHeaderRepository.findByCompCdAndApprGroupId(loginCompCd, apprGroupId)
            .orElseThrow(() -> new RuntimeException("결재가 존재하지 않습니다."));
        if("ing".equals(appCheck)) {
            approvalHeader.updateForNextStage(nextStageNextStr, SlipStatusType.CP.getCode(), nextAppUserId);

            if("02".equals(nextType)) {
                erpSlipHeaderRepository.updateSlipStatusByOrgIdAndApprovalGroupId(SlipStatusType.CP.getCode(), Integer.parseInt(loginCompCd), apprGroupId);
                SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndSlipHeaderIdAndApprovalGroupId(loginCompCd, apprGroupId, apprGroupId)
                    .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
                slipHeader.changeStatus(SlipStatusType.CP.getCode());
            }

        } else if("end".equals(appCheck)) {
            // 이번이 결재 or 검인 완료
            if("02".equals(currentType)) {
                // 최종 검인완료후 지급, 수금 가능 정보 변경 프로시져 call
                approvalRepositoryCustom.callConfirmCheck(apprGroupId);
            }

            // 지금 결재가 마지막일 경우 결재마스터 업데이트
            approvalHeader.updateForNextStage("", SlipStatusType.CC.getCode(), "");

            // slip header 결재완료로 상태값 변경
            SlipHeader slipHeader = slipHeaderRepository.findByCompCdAndApprovalGroupId(loginCompCd,
                    apprGroupId)
                .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
            slipHeader.changeStatus(SlipStatusType.CC.getCode());

            // erp 전표헤더에 검인완료 상태값 변경
            erpSlipHeaderRepository.updateSlipStatusByOrgIdAndApprovalGroupId(SlipStatusType.CC.getCode(),
                Integer.parseInt(loginCompCd), apprGroupId);

            // 상신될경우 물고 있는 카드데이터의 상태값도 CC(결재완료) 바꾸기 위해서 추가
            approvalQdslRepository.updateCardUseList(loginCompCd, apprGroupId);

            if (SlipTypeCd.BOND.getCode().equals(approvalDetailDto.getSlipTypeCd())) {
                List<BondHeaderDto> bondHeaderDtos = bondHeaderQdslRepository.findAllByCompCdAndSlipNoAndSlipHeaderId(
                    loginCompCd, approvalDetailDto.getSlipNo(), apprGroupId);
                if (bondHeaderDtos.size() != 1) {
                    throw new RuntimeException("BOND가 존재하지 않습니다.");
                }

                BondHeaderDto bondHeaderDto = bondHeaderDtos.get(0);
                if (bondHeaderQdslRepository.existsByLocalComplete(loginCompCd,
                    bondHeaderDto.getRefNo(), apprGroupId)
                    && "LOCAL".equals(bondHeaderDto.getType()) && !"Y".equals(
                    bondHeaderDto.getSplitEtcYn())) {
                    // 신규건이 아니면서 국내수수료이고, 분배 및 기타가 아닌 경우 amendSeq를 1 증가시킨다.
                    BondHeader bondHeader = bondHeaderRepository.findByCompCdAndSlipHeaderId(
                            loginCompCd, apprGroupId)
                        .orElseThrow(() -> new RuntimeException("BOND가 존재하지 않습니다"));
                    bondHeader.addOneToAmendSeq();
                }
            }

            // 최종 검인 완료시 참조자에게 메일 발송
            if(hasText(approvalHeader.getRefUserId())) {
                String[] refUsers = approvalHeader.getRefUserId().split(" , ");

                // 기안자 정보 불러오기
                Employee draftEmp = employeeRepository.findByCompCdAndEmpNo(loginCompCd, approvalHeader.getDraftId())
                    .orElseThrow(() -> new RuntimeException("기안자 정보가 존재하지 않습니다."));

                for(int i = 0; i < refUsers.length; i++) {

                    // 참조자 정보 불러오기
                    String refEmpNo = refUsers[i];
                    if(hasText(refEmpNo) && refEmpNo.length() > 6) {
                        refEmpNo = refEmpNo.substring(0, 6);
                    }

                    String receiveMailUser = refEmpNo + "@iljin.co.kr";
                    String draftEmpNm = draftEmp.getEmpNm();
                    if(hasText(draftEmpNm)) {
                        String sendMailId = "system@iljin.co.kr";
                        String mailSubject = "[검인확정] 기안자 [" + draftEmpNm + "]이 상신한 전자전표가 검인 확정되었습니다. ";
                        String mailText = "";
                        mailText = "전자전표_[검인확정] 기안자 [" + draftEmpNm
                            + "]이 상신한 전자전표가 검인 확정되었습니다. <br><br><a href='http://"
                            + environment.getProperty("server.domain-name")
                            + "/refLst' target='_blank'>전자결재문서 Open</a>";

                        MailDto mailDto = MailDto.builder()
                            .from(sendMailId)
                            .to(receiveMailUser)
                            .subject(mailSubject)
                            .text(mailText)
                            .build();

                mailService.sendSimpleMessage(mailDto);
                    }
                }
            }

        }

        // 증빙 후첨 상태값 변경
        SlipHeader slipHeaderFlagUpdate = slipHeaderRepository.findByCompCdAndSlipNo(loginCompCd, approvalDetailDto.getSlipNo())
            .orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
        slipHeaderFlagUpdate.changeEvidenceYn(approvalDetailDto.getEvidenceYn());

        // 결재 디테일 현재 결재자 완료 상태로 변경
        ApprovalDetail approvalDetailUpdate = approvalDetailRepository.findByCompCdAndApprGroupIdAndApprStage(loginCompCd, apprGroupId, String.valueOf(nextStage))
            .orElseThrow(() -> new RuntimeException("결재가 존재하지 않습니다."));
        if("end".equals(appCheck)) {
            approvalDetailUpdate.changeStatus("CC");
        } else {
            if("02".equals(currentType)) {
                approvalDetailUpdate.changeStatus("CP");
            }
        }

        Employee aAprver = employeeRepository.findById(new EmployeeKey(util.getLoginId(), loginCompCd))
            .orElseThrow(() -> new RuntimeException("사원이 존재하지 않습니다."));
        approvalDetailUpdate.changeApprDesc(approvalDetailDto.getApprDesc());
        approvalDetailUpdate.changeAAprver(aAprver);

        return new ResponseEntity<>("검인되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifySlipBundle(List<ApprovalDetailDto> approvalDetailDtos) {
        String message = "검인되었습니다.";
        for(ApprovalDetailDto approvalDetailDto: approvalDetailDtos) {
            this.verifySlip(approvalDetailDto);
        }
        return new ResponseEntity<>("검인되었습니다.", HttpStatus.OK);
    }

    @Override
    public List<ApprovalEmployeeDto> getApprovalEmpList() {
        return approvalRepositoryCustom.getApprovalEmpList();
    }

    @Override
    public List<ApprovalDelegateDto> getApprovalDlgtList(ApprovalDelegateDto approvalDelegateDto) {
        return approvalRepositoryCustom.getApprovalDlgtList(approvalDelegateDto);
    }

    @Override
    public ResponseEntity<String> saveApprovalDlgt(ApprovalDelegateDto approvalDelegateDto) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();
        //String loginId = "341002";
        //String compCd = "101600";

        // 위임자
        String adlgId = approvalDelegateDto.getAdlgId();
        // 수임자
        String actId = approvalDelegateDto.getActId();
        // 위임 순번
        Short adlgSeq = Optional.ofNullable(approvalDelegateDto.getAdlgSeq()).orElse((short) 0);
        ApprovalDelegateKey approvalDelegateKey = new ApprovalDelegateKey(adlgId, actId, adlgSeq);

        if (adlgSeq == 0) {
            /* 신규 위임 */
            Optional<ApprovalDelegate> approvalDelegate = approvalDelegateRepository
                    .findTopByAdlgIdAndActIdOrderByAdlgSeqDesc(adlgId, actId);

            if (approvalDelegate.isPresent()) {
                /*
                 * 같은 위임자, 수임자 조건인 경우 Max seq 처리
                 */
                if (approvalDelegate.get().getAdlgStatCd().equals(ApprovalDlgtStatus.DELEGATING.getCode())) {
                    /*
                     * Validation 상태가 위임인 건 중에서 위임자, 수임자 조건으로 위임기간이 겹치는 건이 있는지 확인
                     */

                    SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd");
                    Date adlgStrDt = new Date();// 입력한
                    Date adlgEndDt = new Date();// 입력한
                    Date adlgStrDtStd = new Date();// 조회한
                    Date adlgEndDtStd = new Date();// 조회한
                    try {
                        adlgStrDt = dt.parse(approvalDelegateDto.getAdlgStrDt().replaceAll("-", ""));
                        adlgEndDt = dt.parse(approvalDelegateDto.getAdlgEndDt().replaceAll("-", ""));
                        adlgStrDtStd = dt.parse(approvalDelegate.get().getAdlgStrDt().replaceAll("-", ""));
                        adlgEndDtStd = dt.parse(approvalDelegate.get().getAdlgEndDt().replaceAll("-", ""));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (adlgStrDt.after(adlgEndDtStd) || adlgEndDt.before(adlgStrDtStd)) {
                        // 위임기간이 겹치지 않는 경우
                    } else {
                        // 위임기간이 겹치는 경우
                        throw new ApprovalException("위임기간이 중복되는 건이 존재합니다.");
                    }
                }

                /* set Value */
                ApprovalDelegate c = new ApprovalDelegate();

                short i = approvalDelegate.get().getAdlgSeq();
                i = (short) (i + 1);

                c.setAdlgId(approvalDelegateDto.getAdlgId());
                c.setActId(approvalDelegateDto.getActId());
                c.setCompCd(compCd);
                c.setAdlgSeq(i);
                c.setAdlgStatCd(ApprovalDlgtStatus.DELEGATING.getCode());
                c.setAdlgStrDt(approvalDelegateDto.getAdlgStrDt());
                c.setAdlgEndDt(approvalDelegateDto.getAdlgEndDt());
                c.setRegId(loginId);
                c.setRegDtm(LocalDateTime.now());
                c.setChgId(loginId);
                c.setChgDtm(LocalDateTime.now());

                approvalDelegateRepository.save(c);
            } else {
                /* seq : 1 */
                adlgSeq++;

                ApprovalDelegate approvalDelegate1 = new ApprovalDelegate();
                approvalDelegate1.setAdlgId(approvalDelegateDto.getAdlgId());
                approvalDelegate1.setActId(approvalDelegateDto.getActId());
                approvalDelegate1.setAdlgSeq(adlgSeq);
                approvalDelegate1.setAdlgStatCd(ApprovalDlgtStatus.DELEGATING.getCode());
                approvalDelegate1.setAdlgStrDt(approvalDelegateDto.getAdlgStrDt());
                approvalDelegate1.setAdlgEndDt(approvalDelegateDto.getAdlgEndDt());
                approvalDelegate1.setCompCd(compCd);
                approvalDelegate1.setRegId(loginId);
                approvalDelegate1.setRegDtm(LocalDateTime.now());
                approvalDelegate1.setChgId(loginId);
                approvalDelegate1.setChgDtm(LocalDateTime.now());

                approvalDelegateRepository.save(approvalDelegate1);
            }
        } else {
            /* 기존 기간 수정 */
            approvalDelegateRepository.findById(approvalDelegateKey).ifPresent(approvalDelegate -> {
                /* approvalDelegate.setAdlgSeq(finalAdlgSeq); */
                approvalDelegate.setAdlgStrDt(approvalDelegateDto.getAdlgStrDt());
                approvalDelegate.setAdlgEndDt(approvalDelegateDto.getAdlgEndDt());
                approvalDelegate.setChgId(loginId);
                approvalDelegate.setChgDtm(LocalDateTime.now());

                approvalDelegateRepository.save(approvalDelegate);
            });
        }

        return new ResponseEntity<>("Saved.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> cancelApprovalDlgt(ApprovalDelegateDto approvalDelegateDto) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        //String loginId = "341002";

        ApprovalDelegateKey approvalDelegateKey = new ApprovalDelegateKey(approvalDelegateDto.getAdlgId(),
                approvalDelegateDto.getActId(), approvalDelegateDto.getAdlgSeq());

        approvalDelegateRepository.findById(approvalDelegateKey).ifPresent(approvalDelegate -> {
            approvalDelegate.setAdlgStatCd(ApprovalDlgtStatus.CANCEL.getCode());
            approvalDelegate.setAdlgRvcDtm(LocalDateTime.now());
            approvalDelegate.setChgId(loginId);
            approvalDelegate.setChgDtm(LocalDateTime.now());

            approvalDelegateRepository.save(approvalDelegate);
        });

        return new ResponseEntity<>("Canceled.", HttpStatus.OK);
    }

    @Override
    public List<ApprovalRuleDto> getApprRuleList(ApprovalRuleDto approvalRuleDto) {
        return approvalRepositoryCustom.getApprRuleList(approvalRuleDto);
    }

    @Override
    public ResponseEntity<String> deleteApprovalRule(List<ApprovalRuleDto> approvalRuleDtoList) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        for (ApprovalRuleDto approvalRuleDto : approvalRuleDtoList) {
            ApprovalRuleKey approvalRuleKey = new ApprovalRuleKey(compCd,
                    approvalRuleDto.getDocTypeCd(),
                    approvalRuleDto.getDtlTypeCd(),
                    approvalRuleDto.getCurCd(),
                    approvalRuleDto.getRuleSeq());

            approvalRuleRepository.deleteById(approvalRuleKey);
        }

        return new ResponseEntity<>("removed.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveApprovalRules(List<ApprovalRule> approvalRules) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();
        //String loginId = "341002";
        //String compCd = "101600";

        for (ApprovalRule approvalRule : approvalRules) {
            String docTypeCd = approvalRule.getDocTypeCd();
            String dtlTypeCd = approvalRule.getDtlTypeCd();
            String curCd = approvalRule.getCurCd();
            Short ruleSeq = Optional.ofNullable(approvalRule.getRuleSeq()).orElse((short) 0);

            ApprovalRuleKey approvalRuleKey = new ApprovalRuleKey(compCd, docTypeCd, dtlTypeCd, curCd, ruleSeq);

            if (ruleSeq == 0) {
                // insert
                // search max Rule sequence
                Optional<ApprovalRule> maxRule = approvalRuleRepository
                        .findTopByCompCdAndDocTypeCdAndDtlTypeCdAndCurCdOrderByRuleSeqDesc(compCd, docTypeCd, dtlTypeCd,
                                curCd);

                if (maxRule.isPresent()) {
                    ruleSeq = maxRule.get().getRuleSeq();
                }

                ruleSeq++;
                approvalRule.setCompCd(compCd);
                approvalRule.setRuleSeq(ruleSeq);
                approvalRule.setRegId(loginId);
                approvalRule.setRegiDtm(LocalDateTime.now());
                approvalRule.setChgId(loginId);
                approvalRule.setChgDtm(LocalDateTime.now());
                approvalRuleRepository.save(approvalRule);
            } else {
                // update
                approvalRuleRepository.findById(approvalRuleKey).ifPresent(c -> {
                    c.setUseYn(approvalRule.getUseYn());
                    c.setMaxAmt(approvalRule.getMaxAmt());
                    c.setApprTypeCd(approvalRule.getApprTypeCd());
                    c.setFixYn(approvalRule.getFixYn());
                    c.setAprverClassCd(approvalRule.getAprverClassCd());
                    c.setAprverClassVal(approvalRule.getAprverClassVal());
                    c.setRemark(approvalRule.getRemark());
                    c.setChgId(loginId);
                    c.setChgDtm(LocalDateTime.now());
                    approvalRuleRepository.save(c);
                });
            }
        }

        return new ResponseEntity<>("saved.", HttpStatus.OK);
    }

    @Override
    public List<ApprovalHeaderDto> getApprIngList(ApprovalHeaderDto approvalHeaderDto) {
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);
        //approvalHeaderDto.setLoginId("340363");
        return approvalRepositoryCustom.getApprIngList(approvalHeaderDto);
    }

    @Override
    public List<ApprovalHeaderDto> getApprAprList(ApprovalHeaderDto approvalHeaderDto) {
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);
        return approvalRepositoryCustom.getApprAprList(approvalHeaderDto);
    }

    @Override
    public List<ApprovalHeaderDto> getApprRejList(ApprovalHeaderDto approvalHeaderDto) {
        String loginId = util.getLoginUserId();
        approvalHeaderDto.setLoginId(loginId);
        return approvalRepositoryCustom.getApprRejList(approvalHeaderDto);
    }

    @Override
    public List<ApprovalEmployeeDto> getApprovalDeptTreeList() {
        return approvalRepositoryCustom.getApprovalDeptTreeList();
    }

    @Override
    public List<ApprovalRuleDto> getApprRuleLines(String docTypeCd, String dtlTypeCd, String curCd, String maxAmt) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();

        String clean1 = maxAmt.replaceAll("[^0-9]", "");

        ApprovalRuleDto approvalRuleDto = new ApprovalRuleDto();
        approvalRuleDto.setCompCd(compCd);
        approvalRuleDto.setDocTypeCd(docTypeCd);
        approvalRuleDto.setDtlTypeCd(dtlTypeCd);
        approvalRuleDto.setCurCd(curCd);
        approvalRuleDto.setMaxAmt(BigDecimal.valueOf(Long.parseLong(clean1)));

        return approvalRepositoryCustom.getApprRuleLines(loginId, approvalRuleDto);
    }

    @Override
    public ResponseEntity<String> getDelegatingCheck(String adlgId, String actId) {
        //String compCd = util.getLoginCompCd();
        String compCd = "101600";

        LocalDate date = LocalDate.now();
        String todayDt = String.valueOf(date).replaceAll("[^0-9]", "");

        Optional<ApprovalDelegate> delegate = approvalDelegateRepository
                .findByAdlgIdAndActIdAndCompCdAndAdlgStatCdAndAdlgStrDtLessThanEqualAndAdlgEndDtGreaterThanEqual(adlgId,
                        actId, compCd, "10", todayDt, todayDt);

        String result = "";
        if (delegate.isPresent()) {
            result = "Y";
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public void slipValidationCheck(BigDecimal apprGroupId){

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.VALIDATION_MAIN");
        storedProcedureQuery.setParameter("p_approval_group_id", apprGroupId);
        storedProcedureQuery.setParameter("p_validation_call_function", "RQ");
        storedProcedureQuery.execute();

        String errFlag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");
        String errStep = (String) storedProcedureQuery.getOutputParameterValue("x_err_step");
        String errMsg = (String) storedProcedureQuery.getOutputParameterValue("x_err_msg");

        if(!"S".equals(errFlag)) {
            throw new ApprovalException("Step : " + errStep + " Msg : " + errMsg);
        }
    }

    public void slipBudgetBranceCheck(BigDecimal slipHeaderId, String flag){

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_GL_BUDGET_PKG.AP_SLIP_ENCUMBRANCE");
        storedProcedureQuery.setParameter("p_slip_header_id", slipHeaderId);
        storedProcedureQuery.setParameter("p_reject_flag", flag);
        storedProcedureQuery.execute();

        String errFlag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");
        String errMsg = (String) storedProcedureQuery.getOutputParameterValue("x_err_msg");

        if(!"S".equals(errFlag)) {
            throw new ApprovalException(" Msg : " + errMsg);
        }
    }

    public void checkAcctPeriod(String postingDt){
        List<AcctPeriodDto> acctPeriodDtos = acctPeriodQdslRepository.getCheckAcctPeriodList(util.getLoginCompCd(), postingDt);
        if(acctPeriodDtos.size() == 0) {
            throw new ApprovalException("회계일자에 대한 마감등록이 없습니다.");
        }
        if(acctPeriodDtos.size() > 1) {
            throw new ApprovalException("회계일자에 대한 마감등록이 잘못됐습니다.");
        }
        if (acctPeriodDtos.get(0).getClosStatCd().equals(ClosStatCd.CLOSE.getCode())) {
            throw new ApprovalException("전자전표의 회계일자가 CLOSE되었습니다.");
        }
    }

}
