package com.iljin.apiServer.ijeas.system.delegate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api")
public class DelegateController {

    private final DelegateService delegateService;

    private final DelegateQdslRepository delegateQdslRepository;

    @PostMapping("/delegate/list")
    public ResponseEntity<List<DelegateDto>> getDelegateList(@RequestBody DelegateDto delegateDto) {
        List<DelegateDto> list = delegateService.getDelegateList(delegateDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/delegate/delete")
    public ResponseEntity<String> deleteDelegate(@RequestBody DelegateDto delegateDto) {
        delegateService.deleteDelegate(delegateDto);
        return new ResponseEntity<>("해제되었습니다.", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/delegate/save")
    public ResponseEntity<String> saveConfirmSeq(@RequestBody DelegateDto delegateDto) {
        return delegateService.saveDelegate(delegateDto);
    }

    @Transactional
    @PostMapping("/delegate/update")
    public ResponseEntity<String> updateDelegate(@RequestBody DelegateDto delegateDto) {
        return delegateService.updateDelegate(delegateDto);
    }

    @PostMapping("/delegate/personal")
    public ResponseEntity<List<DelegateDto>> getPersonalList(@RequestBody DelegateDto delegateDto){
        List<DelegateDto> list = delegateQdslRepository.getPersonalList(delegateDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
