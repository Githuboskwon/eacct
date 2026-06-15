package com.iljin.apiServer.ijeas.sm.apprLine;

import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface ApprLineQdslRepository {
    List<ApprLineDto> getApprLineHdList(ApprLineDto apprLineHd);

    List<ApprLineDto> getApprLineDtList(ApprLineDto apprLineHd);

    ResponseEntity<String> delApprLineDt(ApprLineDt apprLineDt);

    ResponseEntity<String> deleteAllList(ApprLineDt apprLineDt);

    ResponseEntity<String> delApprLineHd(ApprLineHd apprLineHd);

    ResponseEntity<String> saveApprLineDt(List<ApprLineDt> apprLineDt);
}