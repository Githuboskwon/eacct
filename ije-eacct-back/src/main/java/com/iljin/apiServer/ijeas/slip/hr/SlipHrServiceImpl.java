package com.iljin.apiServer.ijeas.slip.hr;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class SlipHrServiceImpl implements SlipHrService {
    private final PayrollBatchRepository payrollBatchRepository;
    private final PayrollUploadRepository payrollUploadRepository;
    private final SlipHrRepositoryCustom slipHrRepositoryCustom;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PayrollBatch> getPayrollBatchList(SlipHrDto slipHrDto){
        List<PayrollBatch> list = payrollBatchRepository.findByOrgIdAndBatchPeriod(slipHrDto.orgId, slipHrDto.batchPeriod);
        return list;
    }

    @Override
    public List<PayrollUpload> getPayrollUploadList(SlipHrDto slipHrDto){
        List<PayrollUpload> list = payrollUploadRepository.findByOrgIdAndPayrollBatchIdAndValidationFlagLikeIgnoreCase(slipHrDto.orgId, slipHrDto.payrollBatchId, slipHrDto.validationFlag);
        return list;
    }

    @Override
    public Map<String, String> deletePayroll(SlipHrDto slipHrDto){
        Map<String, String> result = new HashMap<>();

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("SP_DELETE_PAYROLL");
        storedProcedureQuery.setParameter("P_ORG_ID", slipHrDto.orgId);
        storedProcedureQuery.setParameter("P_PAYROLL_BATCH_ID", slipHrDto.payrollBatchId);
        storedProcedureQuery.setParameter("P_UPLOAD_EMP_NO", slipHrDto.uploadEmpNo);
        storedProcedureQuery.execute();

        result.put("Message", (String) storedProcedureQuery.getOutputParameterValue("ERR_MSG"));
        result.put("Code", (String) storedProcedureQuery.getOutputParameterValue("ERR_CODE"));


        return result;
    }

    @Override
    public Map<String, String> uploadPayroll(List<SlipHrDto> list){
        Map<String, String> result = new HashMap<>();

        //batch id 채번
        Integer batchId = slipHrRepositoryCustom.getPayrollBatchId(list.get(0).orgId);

        if(batchId.intValue() == -1){
            result.put("Message", "batchID 채번 오류");
            return result;
        }

        //insert CBO_SP_PAYROLL_BATCH
        PayrollBatch payrollBatch = new PayrollBatch(
                list.get(0).orgId,
                list.get(0).ledgerId,
                batchId,
                list.get(0).batchPeriod,
                list.get(0).payrollBatchName,
                list.get(0).uploadEmpNo,
                list.get(0).uploadEmpNo,
                LocalDateTime.now(),
                list.get(0).uploadEmpNo,
                LocalDateTime.now()
        );

        payrollBatchRepository.save(payrollBatch);


        //insert CBO_SP_PAYROLL_UPLOAD
        for(int i=0; i<list.size(); i++){
            PayrollUpload payrollUpload = new PayrollUpload(
                    list.get(i).orgId,
                    list.get(i).ledgerId,
                    batchId,
                    i+1,
                    LocalDate.parse(list.get(i).slipDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(),
                    list.get(i).payrollTypeCode,
                    list.get(i).payrollTypeName,
                    list.get(i).uploadTitle,
                    list.get(i).retireEmpNo,
                    list.get(i).uploadEmpNo,
                    list.get(i).coaSegment3,
                    list.get(i).coaSegment4,
                    list.get(i).coaSegment5,
                    list.get(i).payrollAmount,
                    list.get(i).lineDescription,
                    "N",
                    null,
                    null,
                    "N",
                    "",
                    null,
                    "",
                    "",
                    "",
                    "",
                    null,
                    "",
                    list.get(i).uploadEmpNo,
                    LocalDateTime.now(),
                    list.get(i).uploadEmpNo,
                    LocalDateTime.now(),
                    list.get(i).termName,
                    list.get(i).bankAccountName,
                    list.get(i).dueDate.isEmpty() ? null : LocalDate.parse(list.get(i).dueDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()
            );

            payrollUploadRepository.save(payrollUpload);
        }

        result.put("Message", "업로드 성공");

        return result;
    }

    @Override
    public Map<String, String> validationPayroll(Integer payrollBatchId){
        Map<String, String> result = new HashMap<>();

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_PAYROLL_PKG.PAYROLL_VALIDATION");
        storedProcedureQuery.setParameter("P_PAYROLL_BATCH_ID", payrollBatchId);
        storedProcedureQuery.execute();


        result.put("Flag", (String) storedProcedureQuery.getOutputParameterValue("x_err_flag"));

        if(result.get("Flag").equals("S")){
            result.put("Code", "Success");
            result.put("Message", "검증에 성공했습니다.");
        }else{
            result.put("Code", (String) storedProcedureQuery.getOutputParameterValue("x_err_step"));
            result.put("Message", (String) storedProcedureQuery.getOutputParameterValue("x_err_msg"));
        }

        return result;
    }

    public Map<String, String> createPayroll(Integer payrollBatchId){
        Map<String, String> result = new HashMap<>();

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_PAYROLL_PKG.PAYROLL_SLIP_CREATE");
        storedProcedureQuery.setParameter("P_PAYROLL_BATCH_ID", payrollBatchId);
        storedProcedureQuery.execute();


        result.put("Flag", (String) storedProcedureQuery.getOutputParameterValue("x_err_flag"));

        if(result.get("Flag").equals("S")){
            result.put("Code", "Success");
            result.put("Message", "전표작성에 성공했습니다.");
        }else{
            result.put("Code", (String) storedProcedureQuery.getOutputParameterValue("x_err_step"));
            result.put("Message", (String) storedProcedureQuery.getOutputParameterValue("x_err_msg"));
        }

        return result;
    }
}
