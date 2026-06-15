package com.iljin.apiServer.ijeas.sm.apprLine;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/apprLine")
@RequiredArgsConstructor
public class ApprLineController {
    private final ApprLineService apprLineService;

    @ExceptionHandler(ApprLineException.class)
    public ResponseEntity<Error> accountNotFound(ApprLineException e) {
        Error error = new Error("2001"+e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/group")
    public ResponseEntity<List<ApprLineDto>> getGroupList(@RequestBody ApprLineDto apprLineHd) {
        List<ApprLineDto> list = apprLineService.getApprLineHdList(apprLineHd);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/detail")
    public ResponseEntity<List<ApprLineDto>> getDetailList(@RequestBody ApprLineDto apprLineHd) {
        List<ApprLineDto> list = apprLineService.getApprLineDtList(apprLineHd);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/delDetail")
    public ResponseEntity<String> delDetailLine(@RequestBody ApprLineDt apprLineDt){
        return apprLineService.delApprLineDt(apprLineDt);
    }

    @Transactional
    @PostMapping("/delGroup")
    public ResponseEntity<String> delGroupLine(@RequestBody ApprLineHd apprLineHd){
        return apprLineService.delApprLineHd(apprLineHd);
    }

    @Transactional
    @PostMapping("/saveGroup")
    public ResponseEntity<String> saveGroup(@RequestBody List<ApprLineHd> apprLineHd){
        return apprLineService.saveApprLineHd(apprLineHd);
    }

    @Transactional
    @PostMapping("/saveLine")
    public ResponseEntity<String> saveList(@RequestBody List<ApprLineDt> apprLineDt){
        return apprLineService.saveApprLineDt(apprLineDt);
    }

    @Transactional
    @PostMapping("/delAllLine")
    public ResponseEntity<String> deleteAllList(@RequestBody ApprLineDt apprLineDt){
        return apprLineService.deleteAllList(apprLineDt);
    }

    @PostMapping("/main")
    public ResponseEntity<List<ApprLineDto>> getMainList(@RequestBody ApprLineDto apprLineDto) {
        return new ResponseEntity<>(apprLineService.getMainList(apprLineDto), HttpStatus.OK);
    }

}
