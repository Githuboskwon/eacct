package com.iljin.apiServer.ijeas.system.dff;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DffServiceImpl implements DffService {
    @PersistenceContext
    private EntityManager entityManager;
    private final DffQdslRepository dffQdslRepository;

    @Override
    public List<DffDto> getDffFromAccount(DffDto dffDto){
        return dffQdslRepository.getDffFromAccount(dffDto);
    }


    @Override
    public List<DffDto> getDffDetail(DffDto dffDto){
        if(dffDto.flexValueSetId == null){
            return null;
        }

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_GL_DFF_PKG.GET_DFF_VALUESET");
        storedProcedureQuery.setParameter("P_VALUE_SET_ID", Integer.parseInt(String.valueOf(dffDto.flexValueSetId)));
        storedProcedureQuery.setParameter("P_VALIDATION_TYPE", "F");
        storedProcedureQuery.execute();

        int allCount = (int)storedProcedureQuery.getOutputParameterValue("P_TOTAL_COUNT");

        int result = dffQdslRepository.getDffDetailCount(dffDto.beforeValue, dffDto.search);

        if(result > 0){
            List<DffDto> list = dffQdslRepository.getDffDetail(dffDto.beforeValue, dffDto.page, dffDto.search);
            list.get(0).count = result;
            return list;
        }else{
            return null;
        }

    }
}
