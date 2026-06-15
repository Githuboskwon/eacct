package com.iljin.apiServer.ijeas.slip.hr;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface SlipHrService {
    public List<PayrollBatch> getPayrollBatchList(SlipHrDto slipHrDto);

    public List<PayrollUpload> getPayrollUploadList(SlipHrDto slipHrDto);

    public Map<String, String> deletePayroll(SlipHrDto slipHrDto);

    @Transactional
    public Map<String, String> uploadPayroll(List<SlipHrDto> list);

    public Map<String, String> validationPayroll(Integer payrollBatchId);

    public Map<String, String> createPayroll(Integer payrollBatchId);
}
