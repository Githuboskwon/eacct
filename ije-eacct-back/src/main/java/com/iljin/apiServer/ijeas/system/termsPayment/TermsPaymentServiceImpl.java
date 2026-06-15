package com.iljin.apiServer.ijeas.system.termsPayment;



import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTerms;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsKey;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsRepository;
import com.iljin.apiServer.ijeas.system.confirm.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TermsPaymentServiceImpl implements TermsPaymentService {

    private final Util util;

    private final TermsPaymentQdslRepository termsPaymentQdslRepository;

    private final TermsPaymentRepository termsPaymentRepository;

    private final ErpGlTermsRepository erpGlTermsRepository;

    private final ConfirmRepository confirmRepository;


    @Override
    public List<TermsPaymentDto> getTermsPaymentList(TermsPaymentDto termsPaymentDto) {
        return termsPaymentQdslRepository.getTermsPaymentList(termsPaymentDto);
    }

    @Override
    public String deleteTermsPaymentList(List<TermsPaymentDto> termsPaymentDtoList){
        String compCd = util.getLoginCompCd();

        String msg = "삭제되었습니다";

        for(TermsPaymentDto termsPaymentDto : termsPaymentDtoList){

            Integer termId 				= termsPaymentDto.getTermId();
            String deptCode 			= termsPaymentDto.getDeptCd();

            TermsPaymentKey termsPaymentKey = new TermsPaymentKey();
            termsPaymentKey.setCompCd(compCd);
            termsPaymentKey.setTermId(termId);
            termsPaymentKey.setDeptCd(deptCode);

            termsPaymentRepository.deleteById(termsPaymentKey);

        }

        return msg;
    }

    @Override
    public String saveTermsPaymentList(List<TermsPaymentDto> termsPaymentDtoList) {

        String compCd = util.getLoginCompCd();
        String msg = "저장 되었습니다.";

        for(TermsPaymentDto termsPaymentDto : termsPaymentDtoList){

            Integer termId 				= termsPaymentDto.getTermId();
            String name 				= termsPaymentDto.getName();
            String description 			= termsPaymentDto.getDescription();
            String notesFlag 			= termsPaymentDto.getNotesFlag();
            String maturityDays 		= termsPaymentDto.getMaturityDays();
            String dueDateCalcFlag 	    = termsPaymentDto.getDueDateCalcFlag();
            String currencyType 		= termsPaymentDto.getCurrencyType();
            String paymentMethod 		= termsPaymentDto.getPaymentMethod();
            String vendorAcctCheck 	    = termsPaymentDto.getVendorAcctCheck();
            String trxTypeCode		    = termsPaymentDto.getTrxTypeCd();
            String trxTypeName 		= termsPaymentDto.getTrxTypeNm();
            String deptCode 			= termsPaymentDto.getDeptCd();
            String deptName 			= termsPaymentDto.getName();
            String eaEnabledFlag 		= termsPaymentDto.getEaEnabledFlag();
            String insertYn 			= termsPaymentDto.getInsertYn();
            String ptEnabledFlag		= termsPaymentDto.getPtEnabledFlag();
            String dtChangeFlag		= termsPaymentDto.getDtChangeFlag();
            String oriDeptCode		= termsPaymentDto.getOriDeptCd();
            String oriDeptName		= termsPaymentDto.getOriDeptNm();
            String spEnabledFlag		= "";

            //신규
            if("Y".equals(insertYn)){
                //결제예정일 변경 전체설정
                if(dtChangeFlag.equals("ALL")) {
                    deptCode = "00000";
                    deptName = "전체";
                }

                Integer count =  termsPaymentRepository.countByCompCdAndTermIdAndDeptCd(compCd,termId,deptCode);

                if(count == 0){

                    ErpGlTermsKey erpGlTermsKey = new ErpGlTermsKey();
                    erpGlTermsKey.setOrgId(compCd);
                    erpGlTermsKey.setTermId(termId);

                    Optional<ErpGlTerms> erpGlTerms = erpGlTermsRepository.findById(erpGlTermsKey);

                    String trxType = erpGlTerms.get().getTrxType();
                    String maturity_days = erpGlTerms.get().getMaturityDays();
                    String due_date_calc_flag = erpGlTerms.get().getDueDateCalcFlag();
                    String currency_type		= erpGlTerms.get().getCurrencyType();
                    String sp_enabled_flag		= erpGlTerms.get().getSpEnabledFlag();
                    String note_flag            = erpGlTerms.get().getNotesFlag();
                    String vendor_acct_check = erpGlTerms.get().getVendorAcctCheck();

                    TermsPayment termsPayment = TermsPayment.builder()
                          .trxType(trxType)
                          .termId(termId)
                          .name(name)
                          .description(description)
                          .compCd(compCd)
                          .notesFlag(note_flag)
                          .maturityDays(maturity_days)
                          .dueDateCalcFlag(due_date_calc_flag)
                          .currencyType(currencyType)
                          .paymentMethod(paymentMethod)
                          .vendorAcctCheck(vendor_acct_check)
                          .trxTypeCd(trxTypeCode)
                          .trxTypeNm(trxTypeName)
                          .deptCd(deptCode)
                          .deptNm(deptName)
                          .spEnabledFlag(sp_enabled_flag)
                          .eaEnabledFlag(eaEnabledFlag)
                          .ptEnabledFlag(ptEnabledFlag)
                          .dtChangeFlag(dtChangeFlag)
                          .build();

                    termsPaymentRepository.save(termsPayment);

                }else{
                    throw new RuntimeException("이미 등록되어 있는 데이터가 존재합니다.");
                }

            }else{
            // 기존
                if("ALL".equals(dtChangeFlag)){
                    deptCode = "00000";
                    deptName = "전체";
                }

                Integer count =  termsPaymentRepository.countByCompCdAndTermIdAndDeptCd(compCd,termId,deptCode);

                // 기존의 것인데 없다면 만들어준다
                if(count == 0){

                    ErpGlTermsKey erpGlTermsKey = new ErpGlTermsKey();
                    erpGlTermsKey.setOrgId(compCd);
                    erpGlTermsKey.setTermId(termId);

                    Optional<ErpGlTerms> erpGlTerms = erpGlTermsRepository.findById(erpGlTermsKey);

                    String trxType = erpGlTerms.get().getTrxType();
                    String maturity_days = erpGlTerms.get().getMaturityDays();
                    String due_date_calc_flag = erpGlTerms.get().getDueDateCalcFlag();
                    String currency_type		= erpGlTerms.get().getCurrencyType();
                    String sp_enabled_flag		= erpGlTerms.get().getSpEnabledFlag();
                    String note_flag            = erpGlTerms.get().getNotesFlag();
                    String vendor_acct_check = erpGlTerms.get().getVendorAcctCheck();

                    TermsPayment termsPayment = TermsPayment.builder()
                            .trxType(trxType)
                            .termId(termId)
                            .name(name)
                            .description(description)
                            .compCd(compCd)
                            .notesFlag(note_flag)
                            .maturityDays(maturity_days)
                            .dueDateCalcFlag(due_date_calc_flag)
                            .currencyType(currencyType)
                            .paymentMethod(paymentMethod)
                            .vendorAcctCheck(vendor_acct_check)
                            .trxTypeCd(trxTypeCode)
                            .trxTypeNm(trxTypeName)
                            .deptCd(deptCode)
                            .deptNm(deptName)
                            .spEnabledFlag(sp_enabled_flag)
                            .eaEnabledFlag(eaEnabledFlag)
                            .ptEnabledFlag(ptEnabledFlag)
                            .dtChangeFlag(dtChangeFlag)
                            .build();

                    termsPaymentRepository.save(termsPayment);

                }else{
                    // 기존의 데이터 업데이트
                    termsPaymentRepository.updateSetEaEnabledFlagAndPtEnabledFlagAndDtChangeFlagByCompCdAndTermIdAndDeptCd(eaEnabledFlag,ptEnabledFlag,dtChangeFlag,compCd,termId,deptCode);

                    if(!deptCode.equals(oriDeptCode)){
                        // 부서가 변경 되었다면 삭제
                        termsPaymentRepository.deleteByCompCdAndTermIdAndDeptCd(compCd,termId,oriDeptCode);
                    }
                }
            }
        }
        return msg;
    }


}
