package com.iljin.apiServer.ijeas.sm.evid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/api")
@RequiredArgsConstructor
public class UFileController {

    private final UFileService uFileService;

    @ExceptionHandler(UFileException.class)
    public ResponseEntity<Error> uFileNotFound(UFileException e) {
        Error error = new Error("2001" + e.getMessage(), e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 증빙첨부 - 공통팝업
     * 목록 조회
     * @param docMngNo is 문서번호(문서관리번호)
     *                 문서번호로 기 저장된 [첨부파일]을 조회하기 위함.
     * */
    @GetMapping(value = {"/evid/fileList", "/evid/fileList/{docMngNo}"})
    public ResponseEntity<Map<String, Object>> getFileList(@PathVariable(required = false) String docMngNo) {
        Map<String, Object> map = uFileService.getFileList(docMngNo);
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
    @PostMapping(value = {"/evid/addFiles", "/evid/addFiles/{documentHId}"})
    public ResponseEntity<List<UFile>> uploadEvidenceFiles(@PathVariable(required = false) String documentHId, MultipartFile[] files) {
        List<MultipartFile> fileList = new ArrayList<>();
        for(MultipartFile file : files) {
            fileList.add(file);
        }
        UFileDto uFileDto = new UFileDto();
        uFileDto.setDocumentHId(documentHId);
        uFileDto.setFiles(fileList);
        List<UFile> list = uFileService.uploadEvidenceFiles(uFileDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     *
     * */
    @PostMapping(value = "/evid/addfiles/bundle")
    public ResponseEntity<List<UFile>> uploadEvidenceFilesByBundle(MultipartFile[] files, @RequestParam("list") String slipNos) {
        List<MultipartFile> fileList = new ArrayList<>();
        for(MultipartFile file: files) {
            fileList.add(file);
        }

        slipNos = slipNos.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\"", "");

        UFileDto uFileDto = new UFileDto();
        uFileDto.setFiles(fileList);
        List<String> slipNoList = new ArrayList<>(Arrays.asList(slipNos.split(",")));
        uFileDto.setSlipNoList(slipNoList);

        List<UFile> list = uFileService.uploadEvidenceFilesBundle(uFileDto);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     *
     * */
    @PostMapping("/evid/addFiles/mobile")
    public ResponseEntity<List<UFile>> uploadEvidenceFilesMb(MultipartFile[] files) {
        List<MultipartFile> fileList = new ArrayList<>();
        for(MultipartFile file: files) {
            fileList.add(file);
        }

        UFileDto uFileDto = new UFileDto();
        uFileDto.setFiles(fileList);
        List<UFile> list = uFileService.uploadEvidenceFilesMobile(uFileDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     *
     * */
    @PostMapping("/evid/removeFiles")
    public ResponseEntity<String> deleteEvidFiles(@RequestBody UFileDto uFileDto) {
        return new ResponseEntity<>(uFileService.deleteEvidenceFiles(uFileDto), HttpStatus.OK);
    }

    @PostMapping("/evid/removeFiles/mobile")
    public ResponseEntity<String> removeMobileEvidenceFiles(@RequestBody UFileDto uFileDto) {
        return new ResponseEntity<>(uFileService.removeMobileEvidenceFiles(uFileDto), HttpStatus.OK);
    }

    @PostMapping("/evid/mCheck")
    public ResponseEntity<Optional<UFile>> mCheckFile(@RequestBody UFileDto uFileDto) {
        return new ResponseEntity<>(uFileService.mobileCheckFile(uFileDto), HttpStatus.OK);
    }





}
