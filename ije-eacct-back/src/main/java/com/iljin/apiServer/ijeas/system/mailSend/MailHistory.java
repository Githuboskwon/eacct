package com.iljin.apiServer.ijeas.system.mailSend;

import com.iljin.apiServer.core.audit.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "TB_MAIL_HISTORY")
@IdClass(MailHistoryKey.class)
public class MailHistory extends BaseEntity {


    @Column(name = "COMP_CD", nullable = false)
    String compCd;

    @Id
    @Column(name = "MAIL_SEND_DT", nullable = false)
    String mailSendDt;

    @Id
    @Column(name = "MAIL_SEQ", nullable = false)
    BigDecimal mailSeq;

    @Id
    @Column(name = "MAIL_TYPE_CD", nullable = false)
    String mailTypeCd;

    @Column(name = "RECEIVE_USER_CD")
    String receiveUserCd;

    @Builder
    public MailHistory(String compCd, String mailSendDt, BigDecimal mailSeq, String mailTypeCd,
        String receiveUserCd) {
        this.compCd = compCd;
        this.mailSendDt = mailSendDt;
        this.mailSeq = mailSeq;
        this.mailTypeCd = mailTypeCd;
        this.receiveUserCd = receiveUserCd;
    }
}
