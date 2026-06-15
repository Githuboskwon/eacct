package com.iljin.apiServer.core.scheduled;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;
//    List<ScheduleDto> getEmpByWorking

}
