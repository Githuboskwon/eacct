package com.iljin.apiServer.ijeas.card;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import com.iljin.apiServer.ijeas.system.emp.EmployeeKey;
import com.iljin.apiServer.ijeas.system.emp.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private static final Logger logger = LoggerFactory.getLogger(CardServiceImpl.class);

    private final Util util;

    private final CardRepository cardRepository;

    private final CardUseListRepository cardUseListRepository;

    private final CardQdslRepository cardQdslRepository;

    private final EmployeeRepository employeeRepository;

    private final CardRepositoryCustom cardRepositoryCustom;

    @Override
    public ResponseEntity<String> saveBcCard(CardDto cardDto) {


        Card cardInfo = new Card();
        try {
            PropertyUtils.copyProperties(cardInfo, cardDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String crdNo = cardDto.getCrdNo();
        String compCd = cardDto.getCompCd();

        CardKey cardKey = new CardKey(compCd,crdNo);

        Optional<Card> card = cardRepository.findById(cardKey);

        cardRepository.save(cardInfo);


        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }


    @Override
    public List<CardDto> getBusinessCreditCardList(CardDto cardDto, Pageable pageable) {
        return cardQdslRepository.getBusinessCreditCardList(cardDto,pageable);
    }

    @Override
    public List<CardDto> getBusinessCreditCardPopList(CardDto cardDto, Pageable pageable) {
        return cardQdslRepository.getBusinessCreditCardPopList(cardDto,pageable);
    }

    @Override
    public Long getBusinessCreditCardCount(CardDto cardDto) {
        return cardQdslRepository.getBusinessCreditCardCount(cardDto);
    }


    public ResponseEntity<String> changeCardStatus(CardUseListDto cardUseListDto){

        if(cardUseListDto.getStatus().equals("01")) {

            CardUseListKey cardUseListKey = new CardUseListKey();
            cardUseListKey.setCompCd(cardUseListDto.getCompCd());
            cardUseListKey.setUsedNo(cardUseListDto.getUsedNo());

            Optional<CardUseList> cardUseList = cardUseListRepository.findById(cardUseListKey);

            if (cardUseList.isPresent()) {
                cardUseList.ifPresent(c -> {
                    c.updateStatus07();
                });
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);

    };

    public ResponseEntity<String> cancelCardStatus(CardUseListDto cardUseListDto){

        if(cardUseListDto.getStatus().equals("07")) {

            CardUseListKey cardUseListKey = new CardUseListKey();
            cardUseListKey.setCompCd(cardUseListDto.getCompCd());
            cardUseListKey.setUsedNo(cardUseListDto.getUsedNo());

            Optional<CardUseList> cardUseList = cardUseListRepository.findById(cardUseListKey);

            if (cardUseList.isPresent()) {
                cardUseList.ifPresent(c -> {
                    c.cancelStatus01();
                });
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);

    };

    public ResponseEntity<String> updateCardEmpInfo(CardUseListDto cardUseListDto){

        String status = cardUseListDto.getStatus();

        if(status.equals("01") || status.equals("CR") || status.equals("SD") || status.equals("FR") || status.equals("SC")) {
            CardUseListKey cardUseListKey = new CardUseListKey();
            cardUseListKey.setCompCd(cardUseListDto.getCompCd());
            cardUseListKey.setUsedNo(cardUseListDto.getUsedNo());

            Optional<CardUseList> cardUseList = cardUseListRepository.findById(cardUseListKey);

            EmployeeKey empKey = new EmployeeKey(cardUseListDto.getUserId(),cardUseListDto.getCompCd());

            Optional<Employee> emp = employeeRepository.findById(empKey);

            if (cardUseList.isPresent()) {
                cardUseList.ifPresent(c -> {
                    c.updateCardEmpInfo(cardUseListDto.getUserId(),cardUseListDto.getUserNm(),emp.get().getDeptCd(),emp.get().getDeptNm());
                });
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);

    }

    @Override
    public List<CardUseListDto> getSimpleCardUsedData(String apprNo) {
        return cardQdslRepository.getSimpleCardUsedData(apprNo);
    }

    ;

//    @Override
//    public Map<String, Object> getByCardNo(String crdNo) {
//        Map<String, Object> result = new HashMap<>();
//
//        cardRepositoryCustom.getByCardNo(crdNo).ifPresent(card -> {
//            result.put("cardDetail", card);
//        });
//
//        return result;
//    }
//
    @Override
    public ResponseEntity<String> deleteCard(String crdNo) {

        String compCd  = util.getLoginCompCd();
        CardKey cardKey = new CardKey(compCd,crdNo);


        cardRepository.findById(cardKey).ifPresent(c -> {
            cardRepository.delete(c);
        });

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }


    @Override
    public List<CardDto> getCardDetail(String crdNo) {
        return cardQdslRepository.getCardDetail(crdNo);
    }

    @Override
    public ResponseEntity<String> saveCard(CardDto cardDto){
        String compCd = util.getLoginCompCd();
        String crdNo = cardDto.getCrdNo();

        CardKey cardKey = new CardKey(compCd,crdNo);

        Optional<Card> cardInfo = cardRepository.findById(cardKey);

        if(cardInfo.isPresent()) {
            //update
            cardInfo.ifPresent(s -> {
                s.update(cardDto);
            });
        } else {
            //insert
            Card card = Card.builder()
                    .compCd(compCd)
                    .crdNo(cardDto.getCrdNo())
                    .crdPubcDt(cardDto.getCrdPubcDt())
                    .crdVldYm(cardDto.getCrdVldYm())
                    .crdAbltDt(cardDto.getCrdAbltDt())
                    .crdPlmtAmt(cardDto.getCrdPlmtAmt())
                    .stlBacctNo(cardDto.getStlBacctNo())
                    .preCrdNo(cardDto.getPreCrdNo())
                    .crdUseStrDt(cardDto.getCrdUseStrDt())
                    .crdUseEndDt(cardDto.getCrdUseEndDt())
                    .bacctTctlYn(cardDto.getBacctTctlYn())
                    .crdCompCd(cardDto.getCrdCompCd())
                    .preCrdNo(cardDto.getPreCrdNo())
                    .crdPssrId(cardDto.getCrdPssrId())
                    .crdOln(cardDto.getCrdOln())
                    .crdUseStrDt(cardDto.getCrdUseStrDt())
                    .crdUseEndDt(cardDto.getCrdUseEndDt())
                    .crdUsgCd(cardDto.getCrdUsgCd())
                    .crdVldYm(cardDto.getCrdVldYm())
                    .dpstrNm(cardDto.getDpstrNm())
                    .stlDd(cardDto.getStlDd())
                    .vendCd(cardDto.getVendCd())
                    .vendNm(cardDto.getVendNm())
                    .crdStatCd(cardDto.getCrdStatCd())
                    .crdFgCd(cardDto.getCrdFgCd())
                    .bnkCd(cardDto.getBnkCd())
                    .crdUseId(cardDto.getCrdUseId())
                    .build();
            cardRepository.save(card);
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }


    @Override
    public List<CardUseListDto> getCardUseList(CardUseListDto cardUseListDto) {
        return cardQdslRepository.getCardUseList(cardUseListDto);
    }


    @Override
    public List<CardUseListDto> getCardPopupUseList(CardUseListDto cardUseListDto) {
        //return cardQdslRepository.getCardPopupUseList(cardUseListDto);
        return cardRepositoryCustom.getCardPopupUseList(cardUseListDto);
    }

//
//    @Override
//    public List<CardDelegateDto> getCardDelegatingList(CardDelegateDto cardDelegateDto) {
//        return cardRepositoryCustom.getCardDelegatingList(cardDelegateDto);
//    }
//
//    @Override
//    public ResponseEntity<String> saveCardDelegating(CardDelegateDto cardDelegateDto) {
//        User loginUser = util.getLoginUser();
//        String loginId = loginUser.getLoginId();
//        String compCd = loginUser.getCompCd();
//
//        String crdNo = cardDelegateDto.getCrdNo();
//        String cdlgId = cardDelegateDto.getCdlgId();
//        String nomnId = cardDelegateDto.getNomnId();
//        String cdlgStrDt = cardDelegateDto.getCdlgStrDt().replaceAll("-", "");
//        String cdlgEndDt = cardDelegateDto.getCdlgEndDt().replaceAll("-", "");
//        /* 법인카드/위임자/수임자 순번 */
//        BigDecimal cdlgSeq = Optional.ofNullable(cardDelegateDto.getCdlgSeq()).orElse(BigDecimal.valueOf(0));
//
//        if(cdlgSeq.compareTo(BigDecimal.valueOf(0)) == 0) {
//            /* new Insert */
//            Optional<CardDelegate> cardDelegateChk = cardDelegateRepository.findByCrdNoAndCdlgIdAndNomnIdAndCdlgStrDtGreaterThanEqualAndCdlgEndDtLessThanEqualAndCdlgStatCd(
//                    crdNo,
//                    cdlgId,
//                    nomnId,
//                    cdlgStrDt,
//                    cdlgEndDt,
//                    CardDelegateStatus.DELEGATING.getCode());
//
//            Optional<CardDelegate> cardDuplicationChk = cardDelegateRepository.findTopByCrdNoAndNomnIdAndCdlgStatCdOrderByCdlgSeqDesc(
//                    crdNo,
//                    nomnId,
//                    CardDelegateStatus.DELEGATING.getCode());
//
//            if(cardDelegateChk.isPresent()) {
//                throw new RuntimeException("위임기간이 중복되는 건이 존재합니다.");
//            } else if(cardDuplicationChk.isPresent()) {
//                throw new RuntimeException("수임자가 중복되는 건이 존재합니다.");
//            } else {
//                /* copy data */
//                CardDelegate cardDelegate = new CardDelegate();
//                try {
//                    PropertyUtils.copyProperties(cardDelegate, cardDelegateDto);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                Optional<Card> card = cardRepository.findById(crdNo);
//
//                Optional<CardDelegate> cd = cardDelegateRepository.findTopByCrdNoAndCdlgIdOrderByCdlgSeqDesc(
//                        crdNo,
//                        cdlgId);
//                if(cd.isPresent()) {
//                    /* 같은 위임자, 수임자, Max seq */
//                    cdlgSeq = cd.get().getCdlgSeq().add(BigDecimal.valueOf(1));
//                    cardDelegate.setCdlgSeq(cdlgSeq);
//                } else {
//                    /* 위임순번 : 1 */
//                    cardDelegate.setCdlgSeq(BigDecimal.valueOf(1));
//                }
//
//                /* 공통 값 설정 */
//                cardDelegate.setCompCd(card.get().getCompCd());
//                cardDelegate.setCdlgStatCd(CardDelegateStatus.DELEGATING.getCode());
//                cardDelegate.setCdlgExeDtm(LocalDateTime.now());
//                cardDelegate.setRegId(loginId);
//                cardDelegate.setRegDtm(LocalDateTime.now());
//                cardDelegate.setChgId(loginId);
//                cardDelegate.setChgDtm(LocalDateTime.now());
//
//                cardDelegateRepository.save(cardDelegate);
//            }
//        } else {
//            /* Update */
//            Optional<Card> card = cardRepository.findById(crdNo);
//
//            CardDelegateKey cardDelegateKey = new CardDelegateKey();
//            cardDelegateKey.setCrdNo(crdNo);
//            cardDelegateKey.setCompCd(card.get().getCompCd());
//            cardDelegateKey.setCdlgId(cdlgId);
//            cardDelegateKey.setCdlgSeq(cdlgSeq);
//            cardDelegateRepository.findById(cardDelegateKey).ifPresent(cardDelegate -> {
//                cardDelegate.setCdlgStrDt(cdlgStrDt);
//                cardDelegate.setCdlgEndDt(cdlgEndDt);
//                cardDelegate.setChgId(loginId);
//                cardDelegate.setChgDtm(LocalDateTime.now());
//
//                cardDelegateRepository.save(cardDelegate);
//            });
//        }
//
//        /*
//         * TB_CARD_USE_LIST
//         * USE_DTLS_STAT_CD = '10' : 미처리
//         * 경비처리자/부서 -> 수임자/부서
//         * */
//        List<CardUseList> cardUseLists = cardUseListRepository.findByCardNoAndUseDtlsStatCd(crdNo, UseDtlsStatus.UNTREATED.getCode());
//        if(cardUseLists.size() > 0) {
//            for(CardUseList cardUseList : cardUseLists) {
//                cardUseList.setSlipProcId(cardDelegateDto.getNomnId());
//                cardUseList.setSlipProcDeptCd(cardDelegateDto.getNomnDeptCd());
//                cardUseList.setChgId(loginId);
//                cardUseList.setChgDtm(LocalDateTime.now());
//                cardUseListRepository.save(cardUseList);
//            }
//        }
//
//        return new ResponseEntity<>("저장되었습니다", HttpStatus.OK);
//    }
//
//    @Override
//    public ResponseEntity<String> cancelCardDelegating(CardDelegateDto cardDelegateDto) {
//        User loginUser = util.getLoginUser();
//        String loginId = loginUser.getLoginId();
//
//        String crdNo = cardDelegateDto.getCrdNo();
//        String cdlgId = cardDelegateDto.getCdlgId();
//        String nomnId = cardDelegateDto.getNomnId();
//        BigDecimal cdlgSeq = cardDelegateDto.getCdlgSeq();
//
//        /* 법인카드 정보 */
//        Optional<Card> card = cardRepository.findById(crdNo);
//
//        if(!card.isPresent()){
//            throw new CardException("해당 법인카드가 존재하지 않습니다");
//        }
//
//        /* 법인카드 소유자 정보 */
//        EmpKey empKey = new EmpKey(card.get().crdPssrId, card.get().getCompCd());
//        Optional<Emp> emp = empRepository.findById(empKey);
//
//        /* 지정해제 */
//        cardDelegateRepository.findByCrdNoAndCdlgIdAndNomnIdAndCdlgSeq(crdNo, cdlgId, nomnId, cdlgSeq).ifPresent(cardDelegate -> {
//            cardDelegate.setCdlgStatCd(CardDelegateStatus.CANCEL.getCode());
//            cardDelegate.setChgId(loginId);
//            cardDelegate.setChgDtm(LocalDateTime.now());
//            cardDelegateRepository.save(cardDelegate);
//        });
//
//        /*
//         * TB_CARD_USE_LIST
//         * USE_DTLS_STAT_CD = '10' : 미처리
//         * 경비처리자/부서 -> 카드소유자/부서
//         * */
//        List<CardUseList> cardUseLists = cardUseListRepository.findByCardNoAndUseDtlsStatCd(crdNo, UseDtlsStatus.UNTREATED.getCode());
//        if(cardUseLists.size() > 0) {
//            for (CardUseList cardUseList : cardUseLists) {
//                cardUseList.setSlipProcId(emp.get().getEmpNo());
//                cardUseList.setSlipProcDeptCd(emp.get().getDeptCd());
//                cardUseList.setChgId(loginId);
//                cardUseList.setChgDtm(LocalDateTime.now());
//                cardUseListRepository.save(cardUseList);
//            }
//        }
//
//        return new ResponseEntity<>("지정해제되었습니다.", HttpStatus.OK);
//    }
//
//    @Override
//    public List<CardUseListDto> getCardUseList(CardUseListDto cardUseListDto) {
//        return cardRepositoryCustom.getCardUseList(cardUseListDto);
//    }
//
//    @Override
//    public ResponseEntity<String> cancelPrivateCost(CardUseListDto cardUseListDto) {
//        User loginUser = util.getLoginUser();
//        String loginId = loginUser.getLoginId();
//
//        String useDtlsNo = cardUseListDto.getUseDtlsNo();
//        cardUseListRepository.findById(useDtlsNo).ifPresent(cardUseList -> {
//            if(cardUseList.getUseDtlsStatCd().equals(UseDtlsStatus.PRIVATE_COST.getCode())) {
//                cardUseList.setUseDtlsStatCd(UseDtlsStatus.UNTREATED.getCode());
//                cardUseList.setChgId(loginId);
//                cardUseList.setChgDtm(LocalDateTime.now());
//            }
//            cardUseListRepository.save(cardUseList);
//        });
//
//        return new ResponseEntity<>("취소되었습니다.", HttpStatus.OK);
//    }
//
//    @Override
//    public List<CardBillDto> getCardBillReqList(CardBillDto cardBillDto) {
//        return cardRepositoryCustom.getCardBillReqList(cardBillDto);
//    }
//
//    @Override
//    public Map<String, Object> getCardComparisonResult(CardCompareDto cardCompareDto) {
//        Map<String, String> sc = new HashMap<>();
//        sc.put("compCd", cardCompareDto.getCompCd());
//        sc.put("reqYm", cardCompareDto.getReqYm());
//        sc.put("crdCompCd", cardCompareDto.getCrdCompCd());
//        sc.put("crdFgCd", cardCompareDto.getCrdFgCd());
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("searchCond", sc);
//
//        cardRepositoryCustom.getCardCompare(cardCompareDto).ifPresent(compareDto -> {
//            result.put("cardCompare", compareDto);
//        });
//
//        return result;
//    }
//
//    @Override
//    public ResponseEntity<String> runCardComparing(CardBillDto cardBillDto) {
//        User loginUser = util.getLoginUser();
//        String loginId = loginUser.getLoginId();
//
//        /*
//        * 청구현황
//        * 조회조건으로 조회
//        * */
//        List<CardBillDto> list = cardRepositoryCustom.getCardBillReqList(cardBillDto);
//        for(CardBillDto cardBill : list) {
//            String compCd = cardBill.getCompCd();
//            String cardNo = cardBill.getCardNo();
//            String apprNo = cardBill.getApprNo();
//            BigDecimal purchTot = cardBill.getBillTot();
//            Optional<CardUseListDto> comparing = cardRepositoryCustom.getCardComparing(compCd, cardNo, apprNo, purchTot);
//            if(comparing.isPresent()) {
//                //대상확인
//                /* Update TB_CARD_INV_LIST */
//                cardBillRepository.findById(cardBill.getReqDtlsNo()).ifPresent(c -> {
//                    c.setRecncPrgsCd("20");
//                    c.setChgId(loginId);
//                    c.setChgDtm(LocalDateTime.now());
//                    cardBillRepository.save(c);
//                });
//
//                /* Update TB_CARD_USE_LIST */
//                cardUseListRepository.findByCompCdAndCardNoAndApprNoAndPurchTot(compCd, cardNo, apprNo, purchTot).ifPresent(c -> {
//                    c.setReqYm(cardBill.getReqYm());
//                    c.setRecncPrgsCd("20");
//                    c.setChgId(loginId);
//                    c.setChgDtm(LocalDateTime.now());
//                    cardUseListRepository.save(c);
//                });
//            } else {
//                //대상없음
//                /* Update TB_CARD_INV_LIST */
//                cardBillRepository.findById(cardBill.getReqDtlsNo()).ifPresent(c -> {
//                    c.setRecncPrgsCd("30");
//                    c.setChgId(loginId);
//                    c.setChgDtm(LocalDateTime.now());
//                    cardBillRepository.save(c);
//                });
//            }
//        }
//
//        return new ResponseEntity<>("대사완료", HttpStatus.OK);
//    }
//
//    @Override
//    public List<CardCompareDto> getCardComparisonStateList(CardCompareDto cardCompareDto) {
//
//        return cardRepositoryCustom.getCardComparisonStateList(cardCompareDto);
//    }
//
//    @Override
//    public List<CardDto> getPersonalCardList() {
//        return cardRepositoryCustom.getPersonalCardList(util.getLoginCompCd(), util.getLoginId());
//    }
//
//    @Override
//    public List<CardUseListDto> getCardUseList2(CardUseListDto cardUseListDto) {
//        return cardRepositoryCustom.getCardUseList2(cardUseListDto);
//    }

}
