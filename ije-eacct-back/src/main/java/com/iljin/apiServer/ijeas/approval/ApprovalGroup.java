package com.iljin.apiServer.ijeas.approval;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ApprovalGroup implements Serializable {

    List<ApprovalHeaderDto> approvalHeaderDtos;

    List<ApprovalDetail> approvalDetails;

}
