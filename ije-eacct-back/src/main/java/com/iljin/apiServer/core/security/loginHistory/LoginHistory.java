package com.iljin.apiServer.core.security.loginHistory;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@SequenceGenerator(
        name="LOGIN_HISTORY_GEN", //시퀀스 제너레이터 이름
        sequenceName="login_history_seq", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)//Oracle
@Table(name = "tb_user_login_history")
public class LoginHistory {
    @Id
    @Column(name = "log_id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "LOGIN_HISTORY_GEN")//Oracle
    Long logId;

    @Column(name = "connect_id")
    String connectId;

    @Column(name = "connect_ip")
    String connectIp;

    @Column(name = "connect_mthd")
    String connectMthd;

    @Column(name = "connect_error")
    String connectError;

    @Column(name = "connect_url")
    String connectUrl;

    @CreatedDate
    @Column(name = "creation_date")
    LocalDateTime creationDate;

    @Builder
    public LoginHistory(String connectId, String connectIp, String connectMthd, String connectError, String connectUrl, LocalDateTime creationDate) {
        this.connectId = connectId;
        this.connectIp = connectIp;
        this.connectMthd = connectMthd;
        this.connectError = connectError;
        this.connectUrl = connectUrl;
        this.creationDate = creationDate;
    }
}
