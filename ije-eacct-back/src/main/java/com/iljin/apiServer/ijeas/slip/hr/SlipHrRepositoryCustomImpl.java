package com.iljin.apiServer.ijeas.slip.hr;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class SlipHrRepositoryCustomImpl implements SlipHrRepositoryCustom{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Integer getPayrollBatchId(Integer orgId){
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT APPS.CBO_SP_PAYROLL_BATCH_S.NEXTVAL AS tempPayrollBatchId" +
                "  FROM DUAL"
        );

        Query query = entityManager.createNativeQuery(sb.toString());

        if((com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SlipHrDto.class)).isEmpty()){
            return -1;
        }

        Integer batchId = (com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SlipHrDto.class)).get(0).tempPayrollBatchId.intValue();

        sb = new StringBuilder();

        sb.append(
                "SELECT *" +
                "  FROM CBO_SP_PAYROLL_UPLOAD" +
                " WHERE ORG_ID =  :orgId" +
                "   AND PAYROLL_BATCH_ID = :payrollBatchId"
        );

        query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", orgId);
        query.setParameter("payrollBatchId", batchId);

        if(!(com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SlipHrDto.class)).isEmpty()){
            return -1;
        }

        return batchId;
    }

}
