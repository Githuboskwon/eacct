package com.iljin.apiServer.core.cert;

import com.iljin.apiServer.core.security.aes.AES_Encryption;
import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
//import com.iljin.apiServer.ijeas.sm.evid.UfileDto;
//import com.iljin.apiServer.ijeas.sm.evid.UfileService;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Repository
public class CertificateServiceImpl implements CertificateService {

    @PersistenceContext
    private EntityManager entityManager;

    private final CertFileRepository certFileRepository;

    private final Util util;

//    private final UfileService ufileService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public List<CertFileDto> getCertFiles(String searchDate) {

        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT FILE_ID" +
                "     , EFFECT_ST_DT" +
                "     , EFFECT_END_DT" +
                "     , CASE" +
                "             WHEN LENGTH(PW) > 0 THEN '********'" +
                "       END AS PW" +
                "     , CASE" +
                "             WHEN LENGTH(PW) > 0 THEN '********' " +
                "       END AS PW_CONFIRM" +
                "     , CERT_NAME" +
                "     , REMARK" +
                "     , (SELECT COUNT(1) FROM U_FILE S1 WHERE S1.DOCUMENT_H_ID = F.FILE_ID) AS FILE_CNT" +
                "     , REG_ID" +
                "     , TO_CHAR(REG_DTM, 'YYYY-MM-DD HH24:MI:SS') AS REG_DTM" +
                "     , CHG_ID" +
                "     , TO_CHAR(CHG_DTM, 'YYYY-MM-DD HH24:MI:SS') AS CHG_DTM" +
                "  FROM A_CERT_FILE F");

        if(!StringUtils.isEmpty(searchDate)) {
            sb.append(" WHERE :searchDate BETWEEN EFFECT_ST_DT  AND EFFECT_END_DT");
        }

        sb.append(" ORDER BY EFFECT_ST_DT DESC");

        Query query = entityManager.createNativeQuery(sb.toString());

        if (!StringUtils.isEmpty(searchDate)) {
            query.setParameter("searchDate", searchDate);
        }

        return new JpaResultMapper().list(query, CertFileDto.class);
    }

    @Override
    public ResponseEntity<String> saveCertificate(List<CertFileDto> certFileDtos) throws Exception {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();

        AES_Encryption aes_Encryption = new AES_Encryption();

        for(CertFileDto certFileDto : certFileDtos) {

            Optional<CertFile> tax = certFileRepository.findByFileId(certFileDto.getFileId());

            if(tax.isPresent()) {

                String pw = certFileDto.getPwFake();

                if(!"********".equals(pw)){

                    String dec = aes_Encryption.encrypt(pw);

                    //update
                    tax.ifPresent(c -> {
                        c.setEffectStDt(certFileDto.getEffectStDt());
                        c.setEffectEndDt(certFileDto.getEffectEndDt());
                        c.setPw(dec);
                        c.setCertName(certFileDto.getCertName());
                        c.setRemark(certFileDto.getRemark());
                        c.setChgId(loginId);
                        c.setChgDtm(LocalDateTime.now());

                        certFileRepository.save(c);
                    });

                }else{
                    //update
                    tax.ifPresent(c -> {
                        c.setEffectStDt(certFileDto.getEffectStDt());
                        c.setEffectEndDt(certFileDto.getEffectEndDt());
                        c.setCertName(certFileDto.getCertName());
                        c.setRemark(certFileDto.getRemark());
                        c.setChgId(loginId);
                        c.setChgDtm(LocalDateTime.now());

                        certFileRepository.save(c);
                    });
                }


            } else {
                //insert
                CertFile c = new CertFile();
                try {
                    String dec = aes_Encryption.encrypt(certFileDto.getPwFake());
                    certFileDto.setPw(dec);
                    PropertyUtils.copyProperties(c, certFileDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                c.setRegId(loginId);
                c.setRegDtm(LocalDateTime.now());
                c.setChgId(loginId);
                c.setChgDtm(LocalDateTime.now());

                certFileRepository.save(c);
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

//    @Override
    public ResponseEntity<String> deleteCertificate(String fileId) {

        // 첨부파일 삭제
//        UfileDto ufileDto = new UfileDto();
//
//        ufileDto.setDocumentHId(fileId);
//
//        ufileService.deleteCertFiles(ufileDto);

        // 공인인증서 관리 삭제
        certFileRepository.deleteById(Integer.parseInt(fileId));

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

    @Override
    public List<CertFileDto> getCertFilesWithDecrypt(String searchDate) {

        StringBuilder sb = new StringBuilder();
        sb.append("" +
                "SELECT FILE_ID" +
                "     , EFFECT_ST_DT" +
                "     , EFFECT_END_DT" +
                "     , PW" +
                "     , PW AS PW_CONFIRM" +
                "     , CERT_NAME" +
                "     , REMARK" +
                "     , (SELECT COUNT(1) FROM U_FILE S1 WHERE S1.DOCUMENT_H_ID = F.FILE_ID) AS FILE_CNT" +
                "     , REG_ID" +
                "     , DATE_FORMAT(REG_DTM, '%Y-%M-%D %H:%S:%I') AS REG_DTM" +
                "     , CHG_ID" +
                "     , DATE_FORMAT(CHG_DTM, '%Y-%M-%D %H:%S:%I') AS CHG_DTM" +
                "  FROM A_CERT_FILE F");

        if(!StringUtils.isEmpty(searchDate)) {
            sb.append(" WHERE :searchDate BETWEEN EFFECT_ST_DT  AND EFFECT_END_DT");
        }

        sb.append(" ORDER BY EFFECT_ST_DT DESC");

        Query query = entityManager.createNativeQuery(sb.toString());

        if (!StringUtils.isEmpty(searchDate)) {
            query.setParameter("searchDate", searchDate);
        }

        return new JpaResultMapper().list(query, CertFileDto.class);
    }
}
