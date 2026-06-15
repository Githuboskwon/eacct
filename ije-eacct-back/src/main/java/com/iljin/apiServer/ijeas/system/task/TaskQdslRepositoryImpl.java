package com.iljin.apiServer.ijeas.system.task;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

import static com.iljin.apiServer.ijeas.system.task.QTask.task;
import static java.util.Objects.nonNull;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class TaskQdslRepositoryImpl implements TaskQdslRepository{
    private final JPAQueryFactory queryFactory;
    @Override
    public List<TaskDto> getSlipTaskList(TaskDto taskDto){
        return queryFactory
                .select(new QTaskDto(
                    task.compCd, task.projectId, task.taskNo, task.taskNm, task.taskId, task.taskItemGroup ,task.itemGroupType
                ))
                .from(task)
                .where(compCdEq(taskDto.compCd),
                        projectIdEq(taskDto.projectId),
                        taskNoContains(taskDto.taskNo),
                        taskNmContains(taskDto.taskNm),
                        taskIdContains(taskDto.taskId)
                )
                .orderBy(task.taskNo.desc())
                .fetch();
    }

    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? task.compCd.eq(compCd) : null;
    }

    private BooleanExpression projectIdEq(BigDecimal projectId) {
        return nonNull(projectId) ? task.projectId.eq(projectId) : null;
    }

    private BooleanExpression taskNoContains(String taskNo) {
        return hasText(taskNo) ? task.taskNo.like("%"+taskNo+"%") : null;
    }

    private BooleanExpression taskNmContains(String taskNm) {
        return hasText(taskNm) ? task.taskNm.like("%"+taskNm+"%") : null;
    }

    private BooleanExpression taskIdContains(BigDecimal taskId) {
        return nonNull(taskId) ? task.taskId.like("%"+taskId.toString()+"%") : null;
    }
}
