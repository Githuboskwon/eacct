package com.iljin.apiServer.ijeas.es.docMng;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name = "TB_MNG_NO")
@IdClass(TbMngNoKey.class)
@Entity
public class TbMngNo {
    @Id
    @Column(name = "MODULE_ID")
    String moduleId;

    @Id
    @Column(name = "REQ_TYPE_CD")
    String reqTypeCd;

    @Column(name = "REQ_DT")
    String reqDt;

    @Column(name = "LAST_SEQ")
    Integer lastSeq;
}
