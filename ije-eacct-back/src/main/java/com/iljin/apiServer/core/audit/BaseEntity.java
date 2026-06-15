package com.iljin.apiServer.core.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{

    @CreatedBy
    @Column(updatable = false, name = "reg_id")
    private String regId;

    @LastModifiedBy
    @Column(name = "chg_id")
    private String chgId;
}
