package com.iljin.apiServer.ijeas.sm.apprLine;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

public interface ApprLineService {

    @Transactional(readOnly = true)
    List<ApprLineDto> getApprLineHdList(ApprLineDto apprLineHd);

    List<ApprLineDto> getApprLineDtList(ApprLineDto apprLineHd);

    ResponseEntity<String> delApprLineDt(ApprLineDt apprLinedt);

    ResponseEntity<String> delApprLineHd(ApprLineHd apprLineHd);

    ResponseEntity<String> saveApprLineHd(List<ApprLineHd> apprLineHd);

    ResponseEntity<String> saveApprLineDt(List<ApprLineDt> apprLineDt);

    ResponseEntity<String> deleteAllList(ApprLineDt apprLineDt);

    @Transactional(readOnly = true)
    List<ApprLineDto> getMainList(ApprLineDto apprLineDto);

}
