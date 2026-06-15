package com.iljin.apiServer.ijeas.es.docMng;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class TbMngNoKey implements Serializable {
    private static final long serialVersionUID = -2585444904266021394L;

    String moduleId;
    String reqTypeCd;
}
