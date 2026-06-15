package com.iljin.apiServer.core.audit;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public abstract class BaseTimeEntity {

    @CreatedDate
    @Column(updatable = false, name = "reg_dtm")
    private LocalDateTime regDtm;

    @LastModifiedDate
    @Column(name = "chg_dtm")
    private LocalDateTime chgDtm;
}
