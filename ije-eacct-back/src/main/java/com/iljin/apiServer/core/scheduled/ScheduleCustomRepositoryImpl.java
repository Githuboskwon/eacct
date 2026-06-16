package com.iljin.apiServer.core.scheduled;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ScheduleCustomRepositoryImpl implements ScheduleCustomRepository{

    @PersistenceContext
    private EntityManager entityManager;
//    List<ScheduleDto> getEmpByWorking

}
