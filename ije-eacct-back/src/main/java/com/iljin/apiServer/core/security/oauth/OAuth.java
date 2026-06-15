package com.iljin.apiServer.core.security.oauth;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@SequenceGenerator(
        name="OAUTH_GEN", //시퀀스 제너레이터 이름
        sequenceName="oauth_seq", //시퀀스 이름
        initialValue=1, //시작값
        allocationSize=1 //메모리를 통해 할당할 범위 사이즈
)//Oracle
@Table(name = "A_OAUTH")
public class OAuth {
    @Id
    @Column(name = "id")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "OAUTH_GEN")//Oracle
    private Long id;

    @Column(name = "emp_no", nullable = false)
    private String empNo;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "picture")
    private String picture;

    @Column(name = "type")
    private String type;

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @Builder
    public OAuth(String empNo, String email, String type, String name, String picture) {
        this.empNo = empNo;
        this.email = email;
        this.type = type;
        this.name = name;
        this.picture = picture;
    }

    public OAuth update(String name, String picture) {
        this.name = name;
        this.picture = picture;

        return this;
    }

    public OAuth updateEmpNo(String empNo) {
        this.empNo = empNo;

        return this;
    }
}
