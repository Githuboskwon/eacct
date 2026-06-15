package com.iljin.apiServer.ijeas.mobile.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tb_board_m")
@IdClass(MoBoardKey.class)
public class MoBoard {

    @Id
    @Column(name = "id")
    Long id;

    @Id
    @Column(name = "comp_cd")
    String compCd;

    @Id
    @Column(name = "login_id")
    String loginId;

    @Column(name = "title")
    String title;

    @Column(name = "content")
    String content;

    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "create_date", nullable = false)
    LocalDateTime createDate;

    @Column(name = "hit")
    Integer hit;
}
