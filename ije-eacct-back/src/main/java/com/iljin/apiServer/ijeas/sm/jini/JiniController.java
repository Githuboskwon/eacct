package com.iljin.apiServer.ijeas.sm.jini;

import com.iljin.apiServer.core.util.Error;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

@RestController
@CrossOrigin
@RequestMapping("/api/jini")
@RequiredArgsConstructor
public class JiniController {

    private final JiniService jiniService;

    @ExceptionHandler(JiniException.class)
    public ResponseEntity<Error> JiniNotFound(JiniException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * 저장
     */
    @PostMapping("/document")
    public ResponseEntity<String> saveJiniDocs(@RequestBody List<JiniDto> jiniDtoList) {
        return new ResponseEntity<>(jiniService.saveJiniDocs(jiniDtoList), HttpStatus.OK);
    }


    /**
     * 조회
     */
    @GetMapping("/document/{documentId}")
    public ResponseEntity<List<JiniDto>> getJiniDocs(@PathVariable String documentId) {
        return new ResponseEntity<>(jiniService.getJiniDocs(documentId), HttpStatus.OK);
    }

    /**
     * 삭제
     */
    @PostMapping("/document/delete")
    public ResponseEntity<String> deleteJiniDocs(@RequestBody List<JiniDto> jiniDtoList) {
        return new ResponseEntity<>(jiniService.deleteJiniDocs(jiniDtoList), HttpStatus.OK);
    }

    /**
     * 기안서 일괄 적용 조회
     */
    @GetMapping("/previous")
    public ResponseEntity<List<JiniDto>> getJiniDocsByPreviousMonth(
        @RequestParam(value = "customer", required = false) String customer,
        @RequestParam(value = "writer", required = false) String writer) {
        return new ResponseEntity<>(jiniService.getJiniDocsByPreviousMonth(customer, writer), HttpStatus.OK);
    }

    /**
     * 기안서 일괄 적용 저장
     * */
    @PostMapping("/bundle")
    public ResponseEntity<String> saveJiniDocsByBundle(@RequestBody BundleJiniDocs bundleJiniDocs) {
        return new ResponseEntity<>(jiniService.saveJiniDocsByBundle(bundleJiniDocs), HttpStatus.OK);
    }

}


@Getter
@Setter
class BundleJiniDocs {
    List<JiniDto> jiniDtoList;

    List<String> slipNoList;
}