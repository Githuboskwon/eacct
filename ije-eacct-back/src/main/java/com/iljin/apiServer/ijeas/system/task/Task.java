package com.iljin.apiServer.ijeas.system.task;

import com.iljin.apiServer.core.audit.BaseEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@IdClass(TaskKey.class)
@Table(name = "TB_TASK_GROUP")
@Entity
public class Task extends BaseEntity {

    @Column(name = "COMP_CD")
    String compCd;

    @Id
    @Column(name = "PROJECT_ID")
    BigDecimal projectId;

    @Column(name = "TASK_NO")
    String taskNo;

    @Column(name = "TASK_NM")
    String taskNm;

    @Id
    @Column(name = "TASK_ID")
    BigDecimal taskId;

    @Column(name = "TASK_ITEM_GROUP")
    String taskItemGroup;

    @Column(name = "ITEM_GROUP_TYPE")
    String itemGroupType;

}
