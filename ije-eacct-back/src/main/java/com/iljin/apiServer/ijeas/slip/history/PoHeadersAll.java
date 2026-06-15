package com.iljin.apiServer.ijeas.slip.history;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Table(name = "APPS.PO_HEADERS_ALL")
@Entity
public class PoHeadersAll implements Serializable {
    private static final long serialVersionUID = 1712525407826733830L;

    @Id
    @Column(name = "PO_HEADER_ID")
    Integer poHeaderId;

    @Column(name = "SEGMENT1")
    String segment1;
}
