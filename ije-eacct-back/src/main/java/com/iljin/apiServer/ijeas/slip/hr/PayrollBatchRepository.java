package com.iljin.apiServer.ijeas.slip.hr;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PayrollBatchRepository extends JpaRepository<PayrollBatch, PayrollBatchKey> {
    List<PayrollBatch> findByOrgIdAndBatchPeriod(@NonNull Integer orgId, @NonNull String batchPeriod);
}