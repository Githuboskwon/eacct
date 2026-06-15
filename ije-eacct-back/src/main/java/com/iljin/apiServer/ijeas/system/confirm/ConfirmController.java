package com.iljin.apiServer.ijeas.system.confirm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api")
public class ConfirmController {

    private final ConfirmService confirmService;

    @PostMapping("/confirm/list")
    public ResponseEntity<List<ConfirmDto>> getConfirmList(@RequestBody ConfirmDto confirmDto) {
        List<ConfirmDto> list = confirmService.getConfirmList(confirmDto);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/confirm/delete")
    public ResponseEntity<String> deleteConfirmSeq(@RequestBody List<ConfirmDto> confirmDto) {
        for(ConfirmDto list : confirmDto){
            confirmService.deleteConfirmSeq(list);
        }
        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/confirm/save")
    public ResponseEntity<String> saveConfirmSeq(@RequestBody ConfirmDto confirmDto) {
        return confirmService.saveConfirmSeq(confirmDto);
    }

    @Transactional
    @PostMapping("/confirm/update")
    public ResponseEntity<String> updateConfirmSeq(@RequestBody ConfirmDto confirmDto) {
        return confirmService.updateConfirmSeq(confirmDto);
    }

}
