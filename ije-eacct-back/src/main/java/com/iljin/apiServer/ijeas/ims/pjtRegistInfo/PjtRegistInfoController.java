package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/pjt")
public class PjtRegistInfoController {

    private final PjtRegistInfoService pjtRegistInfoService;

    /**
     * PJT 등록정보 조회
     * @param pjtRegistInfoDto
     */
    @PostMapping("/registInfo/list")
    public ResponseEntity<List<PjtRegistInfoDto>> getRegistInfoList(@RequestBody PjtRegistInfoDto pjtRegistInfoDto){
        List<PjtRegistInfoDto> list = pjtRegistInfoService.getRegistInfoList(pjtRegistInfoDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    /**
     * PJT 등록정보 저장
     */
    @PostMapping("/registInfo/save")
    public ResponseEntity<String> saveRegistInfoList(@RequestBody List<PjtRegistInfoDto> pjtRegistInfoDtos){
        return new ResponseEntity<>(pjtRegistInfoService.saveRegistInfoList(pjtRegistInfoDtos), HttpStatus.OK);
    }

}
