package com.iljin.apiServer.ijeas.es.gl;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlCodesDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpGlTermsDto;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class GlServiceImpl implements GlService {


    private final Util util;

    private final GlQdslRepository glQdslRepository;

    private final GlRepositoryCustom glRepositoryCustom;


    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<ErpGlTermsDto> getErpTermList(ErpSlipRequestDto erpSlipRequestDto) {

        List<ErpGlTermsDto> erpGlTermsDtoList = new ArrayList<>();

        if("KRW".equals(erpSlipRequestDto.getCurrencyCd())){
            erpSlipRequestDto.setCurrencyType("K");
        }else{
            erpSlipRequestDto.setCurrencyType("F");
        }

        if(!"Y".equals(erpSlipRequestDto.getSearchYn())){
            erpGlTermsDtoList = glRepositoryCustom.getErpTermList(erpSlipRequestDto);
        }else{
            erpGlTermsDtoList = glQdslRepository.getErpTermList(erpSlipRequestDto);
        }

        return erpGlTermsDtoList;
    }

    @Override
    public ErpGlTermsDto getErpTermGetDueDate(ErpGlTermsDto erpGlTermsDto) throws Exception {
        String dmString = "D";
        ErpGlTermsDto resultDto = new ErpGlTermsDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String x_due_date = "";
        String x_maturity_date = "X";

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_COMMON_PKG.GET_DUE_DATE");
        storedProcedureQuery.setParameter("P_INTERFACE_MODULE", erpGlTermsDto.getTypeInterfaceModule());
        storedProcedureQuery.setParameter("P_TERM_ID", erpGlTermsDto.getTermId());
        storedProcedureQuery.setParameter("P_GL_DATE", LocalDateTime.parse(erpGlTermsDto.getPostDt(), formatter));
        storedProcedureQuery.setParameter("P_CALC_TYPE", dmString);
        storedProcedureQuery.execute();

        LocalDateTime lo_due_date = (LocalDateTime) storedProcedureQuery.getOutputParameterValue("X_DUE_DATE");

        if(lo_due_date != null){
            x_due_date = formatter.format(lo_due_date);
        }

        if("Y".equals(erpGlTermsDto.getNoteFlag())){
            dmString = "M";

            StoredProcedureQuery storedProcedureQuery2 = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_COMMON_PKG.GET_DUE_DATE");
            storedProcedureQuery2.setParameter("P_INTERFACE_MODULE", erpGlTermsDto.getTypeInterfaceModule());
            storedProcedureQuery2.setParameter("P_TERM_ID", erpGlTermsDto.getTermId());
            storedProcedureQuery2.setParameter("P_GL_DATE", LocalDateTime.parse(erpGlTermsDto.getPostDt(), formatter));
            storedProcedureQuery2.setParameter("P_CALC_TYPE", dmString);
            storedProcedureQuery2.execute();

            LocalDateTime lo_maturity_date = (LocalDateTime) storedProcedureQuery2.getOutputParameterValue("X_DUE_DATE");

            if(lo_maturity_date != null){
                x_maturity_date = formatter.format(lo_maturity_date);
            }
        }

        resultDto.setXDueDate(!"".equals(x_due_date) ? x_due_date.replaceAll("-", "").substring(0,8) : "");
        resultDto.setXMaturityDate(!"".equals(x_maturity_date) ? x_maturity_date.equals("X") ? "X" : x_maturity_date.replaceAll("-", "").substring(0,8) : "");
        resultDto.setTypeInterfaceModule(erpGlTermsDto.getTypeInterfaceModule());

        ErpGlTermsDto statusDto = doDueDateChange(erpGlTermsDto);

        resultDto.setTermsStatus(statusDto.getTermsStatus());
        resultDto.setTermsFlag(statusDto.getTermsFlag());
        resultDto.setTermsValue(statusDto.getTermsValue());

        return resultDto;
    }

    @Override
    public List<ErpGlCodesDto> getTaxIsuueTypeList(ErpSlipRequestDto erpSlipRequestDto) {
        return glQdslRepository.getTaxIsuueTypeList(erpSlipRequestDto);
    }

    private ErpGlTermsDto doDueDateChange(ErpGlTermsDto erpGlTermsDto){
        ErpGlTermsDto resultDto = new ErpGlTermsDto();
        ErpGlTermsDto selectDto = new ErpGlTermsDto();
        ErpGlTermsDto selectDto2 = new ErpGlTermsDto();

        try {

            resultDto.setTermsStatus(1);
            resultDto.setTermsFlag(true);

            int all_change_cnt =	0;
            int dept_change_cnt =	0;
            String dt_change_flag = "";

            String dept_code = erpGlTermsDto.getDeptCd();
            String ea_enabled_flag = "Y";
            String dt_change_all = "ALL";
            String dt_change_dept_code = "00000";

            selectDto.setSpEnabledFlag(ea_enabled_flag);
            selectDto.setTermId(erpGlTermsDto.getTermId());
            selectDto.setDeptCd(dt_change_dept_code);
            selectDto.setDtChangeFlag(dt_change_all);

            //결제예정일 변경 적용범위 검증
            all_change_cnt = Math.toIntExact(glQdslRepository.getTermsDateCnt(selectDto));

            //전체 변경
            if(all_change_cnt == 1) {
                //Set
                resultDto.setTermsValue("Y");
            } else {
                //결제조건 등록여부
                selectDto2.setSpEnabledFlag(ea_enabled_flag);
                selectDto2.setTermId(erpGlTermsDto.getTermId());
                selectDto2.setDeptCd(dept_code);

                dept_change_cnt = Math.toIntExact(glQdslRepository.getTermsDateCnt(selectDto2));

                if(dept_change_cnt == 1) {
                    //결제예정일 변경 Flag 조회
                    dt_change_flag = glQdslRepository.getTermsChangeFlag(selectDto2);
                    //Set
                    resultDto.setTermsValue("".equals(dt_change_flag) ? "N" : dt_change_flag);
                } else {
                    //Set
                    resultDto.setTermsValue("N");
                }

            }

        } catch (Exception e) {
            resultDto.setTermsStatus(0);
            resultDto.setTermsFlag(false);
            resultDto.setTermsValue("null");
        }

        return resultDto;
    }

    @Override
    public List<ErpGlCodesDto> getGlSlipTypeList(ErpGlSlipDto erpGlSlipDto) {
        return glRepositoryCustom.getGlSlipTypeList(erpGlSlipDto);
    }
}
