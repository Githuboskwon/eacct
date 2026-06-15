package com.iljin.apiServer.ijeas.slip.etax;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Table(name ="CBOTAX.XXSB_DTI_Status")
@IdClass(XxsbDtiStatusKey.class)
@Entity
public class XxsbDtiStatus {
    @Id
    @Column(name = "CONVERSATION_ID")
    String conversationId;

    @Id
    @Column(name = "SUPBUY_TYPE")
    String supbuyType;

    @Id
    @Column(name = "DIRECTION")
    String direction;

    @Column(name = "DTI_STATUS")
    String dtiStatus;
}
