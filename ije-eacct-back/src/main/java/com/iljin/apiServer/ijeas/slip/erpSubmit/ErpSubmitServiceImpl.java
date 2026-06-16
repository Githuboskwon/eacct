package com.iljin.apiServer.ijeas.slip.erpSubmit;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.ErpSlipSubmitDto;
import com.iljin.apiServer.ijeas.es.erpViews.*;
import com.iljin.apiServer.ijeas.slip.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.util.*;


@AllArgsConstructor
@Service
public class ErpSubmitServiceImpl implements ErpSubmitService {

    private final SlipHeaderRepository slipHeaderRepository;
    private final ErpSubmitRepositoryCustom erpSubmitRepositoryCustom;
    private final Util util;
    private final ErpSlipHeaderRepository erpSlipHeaderRepository;

    private final ErpSubmitQdslRepository erpSubmitQdslRepository;

    @PersistenceContext
    private EntityManager entityManager;



    @Override
    public List<ErpSlipSubmitDto> getErpSlipSubmitList(ErpSlipSubmitDto erpSlipSubmitDto) {
        return erpSubmitRepositoryCustom.getErpSlipSubmitList(erpSlipSubmitDto);
    }

    @Override
    public List<ErpGlTrxTypeHdDto> getErpSlipSubmitDealTypeList(String search) {
        return erpSubmitQdslRepository.getErpSlipSubmitDealTypeList(search);
    }

    @Override
    public ResponseEntity<String> slipTransferErp(List<ErpSlipSubmitDto> erpSlipSubmitDtos){
        String compCd = util.getLoginCompCd();
        String userId = util.getLoginUserId();
        String totalMessage = "";

        for(ErpSlipSubmitDto erpSlipSubmitDto : erpSlipSubmitDtos){

            String slip_no = erpSlipSubmitDto.getSlipNo();
            String company_code = String.valueOf(erpSlipSubmitDto.getCompCd());
            String approval_group_id = String.valueOf(erpSlipSubmitDto.getApprovalGroupId());
            String add_user_id = erpSlipSubmitDto.getErpAppUserId();
            String slip_type = erpSlipSubmitDto.getSlipType();

            String if_check = "";
            String approval_yn= "";
            String slip_if_fail_yn = "";
            String approval_complete_fail_yn = "";
            String slip_forced_if_flag = "";
            List<ErpSlipHeaderDto> slipCheck = erpSubmitQdslRepository.getErpSlipStatus(erpSlipSubmitDto);

            if(slipCheck.size() == 1){
                String slip_if_flag = slipCheck.get(0).getSlipIfFlag();
                String slip_data_fix_flag = slipCheck.get(0).getSlipDataFixFlag();
                String approval_complete_flag = slipCheck.get(0).getApprovalCompleteFlag();
                slip_forced_if_flag = slipCheck.get(0).getSlipForcedIfFlag();

                if(!"N".equals(slip_if_flag)){
                    slip_if_fail_yn = "Y";
                }
                if("Y".equals(approval_complete_flag)){
                    approval_complete_fail_yn = "Y";
                }
                if("Y".equals(slip_data_fix_flag)){
                    approval_yn = "Y";
                }

            }else{
                if_check = "Y";
            }

            if("Y".equals(if_check)){
                totalMessage += slip_no + " : 중복이 존재하는 전표입니다. \n\n";
                continue;
            }
            if("Y".equals(slip_if_fail_yn)){
                totalMessage += slip_no + " : ERP반영 여부를 확인해주세요. \n\n";
                continue;
            }
            if("Y".equals(approval_complete_fail_yn)){
                totalMessage += slip_no + " : 결재 완료된 전표입니다. \n\n";
                continue;
            }
            if(!"Y".equals(approval_yn)){
                totalMessage += slip_no + " : 상신되지 않은 전표 입니다.\n\n";
                continue;
            }

//            Optional<SlipHeader> slipHeader = slipHeaderRepository.findByCompCdAndApprovalGroupId(compCd,erpSlipSubmitDto.getApprovalGroupId());
//            if(slipHeader.isPresent()) {
//                slipHeader.ifPresent(c -> {
//                    c.setTransferTypeY();
//                });
//            }

            //중복 call 방지 위해 먼저 업데이트하고 시작
            // TB_SLIP_HD 전표헤더에 결재완료로 상태값 변경
            slipHeaderRepository.findByCompCdAndApprovalGroupId(compCd,erpSlipSubmitDto.getApprovalGroupId())
                    .ifPresent(u -> u.setTransferType("Y"));

            //중간전송 상태 변경
//            Optional<ErpSlipHeader> erpSlipHeader1 = erpSlipHeaderRepository.findByOrgIdAndApprovalGroupId(Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId());
//            if(erpSlipHeader1.isPresent()) {
//                erpSlipHeader1.ifPresent(c -> {
//                    c.setSlipForcedIfFlagING();
//                });
//            }

            // erp 전표헤더에 결재완료 상태값 변경
//            Optional<ErpSlipHeader> erpSlipHeader2 = erpSlipHeaderRepository.findByOrgIdAndApprovalGroupId(Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId());
//            if(erpSlipHeader2.isPresent()) {
//                erpSlipHeader2.ifPresent(c -> {
//                    c.setApprovalCompleteFlagY();
//                });
//            }

            //중간전송 상태 변경, ERP전표헤더 결재완료 상태값 변경
            erpSlipHeaderRepository.findByOrgIdAndApprovalGroupId(Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId())
                    .stream()
                    .findFirst()
                    .ifPresent(u -> erpSlipHeaderRepository.updateSlipForcedIfFlagAndApprovalCompleteFlagByOrgIdAndApprovalGroupId(
                            "Y", Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId()));

            List<ErpSlipHeaderDto> checkSf = erpSubmitQdslRepository.getErpSlipFlag(erpSlipSubmitDto);

            if(checkSf.size() == 1){

                if(!"Y".equals(checkSf.get(0)) && !"Y".equals(slip_forced_if_flag)){

                    //ERP 반영 여부 상태 변경
                    // CBO_SP_SLIP_HEADER 전표헤더에 SLIP_IF_FLAG 상태값 ING로 상태값 변경
//                    Optional<ErpSlipHeader> erpSlipHeader = erpSlipHeaderRepository.findByOrgIdAndApprovalGroupId(Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId());
//                    if(erpSlipHeader.isPresent()) {
//                        erpSlipHeader.ifPresent(c -> {
//                            c.setSlipIfFlagING();
//                        });
//                    }

                    //ERP 반영 여부 상태 변경
                    // CBO_SP_SLIP_HEADER 전표헤더에 SLIP_IF_FLAG 상태값 ING로 상태값 변경
                    erpSlipHeaderRepository.findByOrgIdAndApprovalGroupId(Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId())
                            .stream()
                            .findFirst()
                            .ifPresent(u -> erpSlipHeaderRepository.updateSlipIfFlagByOrgIdAndApprovalGroupId("ING", Integer.parseInt(compCd), erpSlipSubmitDto.getApprovalGroupId()));


                    StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_SLIP_CONCURRENT");
                    storedProcedureQuery.setParameter("P_APPROVAL_GROUP_ID", erpSlipSubmitDto.getApprovalGroupId());
                    storedProcedureQuery.setParameter("P_SLIP_PROCESS", "");
                    storedProcedureQuery.execute();

                }
            }

            totalMessage += slip_no + " : 전송요청 성공 \n\n";
        }



        return new ResponseEntity<>(totalMessage, HttpStatus.OK);

    }


}
