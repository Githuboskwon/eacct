package com.iljin.apiServer.ijeas.slip.hr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

public interface PayrollUploadRepository extends JpaRepository<PayrollUpload, PayrollUploadKey> {
    List<PayrollUpload> findByOrgIdAndPayrollBatchIdAndValidationFlagLikeIgnoreCase(@NonNull Integer orgId, Integer payrollBatchId, @Nullable String validationFlag);

}