package com.iljin.apiServer.ijeas.system.scheduler;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchedulerRepository extends JpaRepository<Scheduler, SchedulerKey> {

    Optional<Scheduler> findById(SchedulerKey schedulerKey);
}
