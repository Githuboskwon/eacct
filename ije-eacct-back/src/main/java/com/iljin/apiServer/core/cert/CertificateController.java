package com.iljin.apiServer.core.cert;

import com.iljin.apiServer.core.security.aes.AES_Encryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cert")
public class CertificateController {

    private final CertificateService certificateService;

    @Autowired
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping(value={"/list","/list/{searchDate}"})
    public ResponseEntity<List<CertFileDto>> getCertificateList(@PathVariable(required = false) String searchDate) {

        List<CertFileDto> list = certificateService.getCertFiles(searchDate);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveCertificate(@RequestBody List<CertFileDto> certFileDtos) throws Exception {
        return certificateService.saveCertificate(certFileDtos);
    }

    @DeleteMapping("/del")
    public ResponseEntity<String> delCertificate(@RequestParam String fileId) {
        return certificateService.deleteCertificate(fileId);
    }

    @GetMapping(value={"/list/decrypt","/list/decrypt/{searchDate}"})
    public ResponseEntity<List<CertFileDto>> getCertificateListWithDecrypt(@PathVariable(required = false) String searchDate) throws Exception {

        List<CertFileDto> list = certificateService.getCertFilesWithDecrypt(searchDate);

        if(list.size() > 0){
            AES_Encryption aes_Encryption = new AES_Encryption();

            for(CertFileDto certFileDto : list) {
                String pw = certFileDto.getPwFake();
                certFileDto.setPwConfirmFake(aes_Encryption.decrypt(pw));
            }
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
