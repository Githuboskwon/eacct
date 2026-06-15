package com.iljin.apiServer.ijeas.sm.jiniEvid;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class UJiniFileController {

    private final UJiniFileService uJiniFileService;

    @ExceptionHandler(UJiniFileException.class)
    public ResponseEntity<Error> uJiniFileNotFound(UJiniFileException e) {
        Error error = new Error("2001" + e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 증빙첨부 - 공통팝업
     * 목록 조회
     * @param docMngNo is 문서번호(문서관리번호)
     *                 문서번호로 기 저장된 [첨부파일]을 조회하기 위함.
     * */
    @GetMapping(value = {"/evid/jiniFileList", "/evid/jiniFileList/{docMngNo}"})
    public ResponseEntity<Map<String, Object>> getFileList(@PathVariable(required = false) String docMngNo) {
        Map<String, Object> map = uJiniFileService.getFileList(docMngNo);
        if(map.size() > 0) {
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * @param files
     * @param documentHId
     * */
    @PostMapping(value = {"/evid/addJiniFiles", "/evid/addJiniFiles/{documentHId}"})
    public ResponseEntity<List<UJiniFile>> uploadEvidenceFiles(@PathVariable(required = false) String documentHId, MultipartFile[] files) {
        List<MultipartFile> fileList = new ArrayList<>();
        for(MultipartFile file : files) {
            fileList.add(file);
        }
        UJiniFileDto uJiniFileDto = new UJiniFileDto();
        uJiniFileDto.setDocumentHId(documentHId);
        uJiniFileDto.setFiles(fileList);
        List<UJiniFile> list = uJiniFileService.uploadEvidenceFiles(uJiniFileDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }




    /**
     *
     * */
    @PostMapping("/evid/removeJiniFiles")
    public ResponseEntity<String> deleteEvidFiles(@RequestBody UJiniFileDto uJiniFileDto) {
        return new ResponseEntity<>(uJiniFileService.deleteEvidenceFiles(uJiniFileDto), HttpStatus.OK);
    }


}
