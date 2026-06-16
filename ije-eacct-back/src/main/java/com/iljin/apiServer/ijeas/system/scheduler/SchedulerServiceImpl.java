package com.iljin.apiServer.ijeas.system.scheduler;

import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService{

    private final SchedulerRepository schedulerRepository;
    private final SchedulerQdslRepository schedulerQdslRepository;
    @PersistenceContext
    private final EntityManager entityManager;
    private final Util util;

    @Override
    public List<SchedulerDto> getSchedulerList(SchedulerDto schedulerDto) {
        return schedulerQdslRepository.getSchedulerList(schedulerDto);
    }

    @Override
    public String saveSchedulerList(List<SchedulerDto> schedulerDtos) {
        String compCd = util.getLoginUser().getCompCd();
        for(SchedulerDto schedulerDto : schedulerDtos) {
            String schedulerCd = schedulerDto.getSchedulerCd();
            SchedulerKey schedulerKey = SchedulerKey.builder()
                    .compCd(compCd)
                    .schedulerCd(schedulerCd)
                    .build();

            Optional<Scheduler> result = schedulerRepository.findById(schedulerKey);
            if(result.isPresent()) {
                result.ifPresent(s -> {
                    s.update(schedulerDto);
                });
            } else {
                Scheduler scheduler = Scheduler.builder()
                        .compCd(compCd)
                        .schedulerCd(schedulerDto.getSchedulerCd())
                        .schedulerNm(schedulerDto.getSchedulerNm())
                        .useYn(schedulerDto.getUseYn())
                        .remark(schedulerDto.getRemark())
                        .build();
                schedulerRepository.save(scheduler);
            }
        }
        return "저장되었습니다.";
    }

    @Override
    public String deleteScheduler(SchedulerDto schedulerDto) {
        SchedulerKey schedulerKey = SchedulerKey.builder()
                .compCd(schedulerDto.getCompCd())
                .schedulerCd(schedulerDto.getSchedulerCd())
                .build();
        if(!schedulerRepository.existsById(schedulerKey)) {
            throw new RuntimeException("삭제할 스케줄러가 존재하지 않습니다.");
        }

        schedulerRepository.deleteById(schedulerKey);
        return "삭제되었습니다.";
    }

    @Override
    public String executeScheduler(SchedulerDto schedulerDto) {
        SchedulerKey schedulerKey = SchedulerKey.builder()
                .compCd(schedulerDto.getCompCd())
                .schedulerCd(schedulerDto.schedulerCd)
                .build();
        Scheduler scheduler = schedulerRepository.findById(schedulerKey)
                .orElseThrow(() -> new RuntimeException("해당 스케줄러가 존재하지 않습니다."));

        StoredProcedureQuery storedProcedureQuery = entityManager.createStoredProcedureQuery(scheduler.schedulerCd);
        storedProcedureQuery.execute();
        return "실행되었습니다.";
    }
}
