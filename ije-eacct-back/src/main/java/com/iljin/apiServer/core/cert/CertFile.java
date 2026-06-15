package com.iljin.apiServer.core.cert;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "a_cert_file")
public class CertFile {

    @Id
    @Column(name ="file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer fileId;

    @Column(name = "effect_st_dt")
    String effectStDt;

    @Column(name = "effect_end_dt")
    String effectEndDt;

    @Column(name = "pw")
    String pw;

    @Column(name = "cert_name")
    String certName;

    @Column(name = "remark")
    String remark;

    @Column(name = "reg_id")
    String regId;

    // 등록일시
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "reg_dtm", nullable = false, updatable = false)
    LocalDateTime regDtm;

    @Column(name = "chg_id")
    String chgId;

    // 수정일시
    @UpdateTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "chg_dtm", nullable = false)
    LocalDateTime chgDtm;

    //기본 생성자 추가
    public CertFile() {

    }
}
