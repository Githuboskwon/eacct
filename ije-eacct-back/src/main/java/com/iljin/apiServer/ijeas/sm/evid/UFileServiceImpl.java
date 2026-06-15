package com.iljin.apiServer.ijeas.sm.evid;

import com.iljin.apiServer.core.config.FileStorageConfig;
import com.iljin.apiServer.core.files.FileStorageException;
import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class UFileServiceImpl implements UFileService{

    private final Util util;

    private final Environment environment;

    private final Path fileStorageLocation;

    private final UFileQdslRepository uFileQdslRepository;

    private final UFileRepository uFileRepository;

    private final Path xmlFileStorageLocation;

    @Autowired
    public UFileServiceImpl(Util util, Environment environment, FileStorageConfig fileStorageConfig,
        UFileRepository uFileRepository, UFileQdslRepository uFileQdslRepository) {
        this.util = util;
        this.environment = environment;
        this.uFileRepository = uFileRepository;
        this.uFileQdslRepository = uFileQdslRepository;
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
            .toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("파일이 저장될 디렉토리를 생성하지 못했습니다.", e);
        }

        this.xmlFileStorageLocation = Paths.get(fileStorageConfig.getXmlUploadDir())
            .toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.xmlFileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("세금계산서 파일이 저장될 디렉토리를 생성하지 못했습니다.", e);
        }
    }

    @Override
    public Map<String, Object> getFileList(String docMngNo) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();
//        if(!StringUtils.isEmpty(docMngNo)) {
//            docMngNo = docMngNo.toLowerCase();
//        }

        Map<String, Object> result = new HashMap<>();

        List<UFile> mFiles = uFileRepository.findByCompCdAndAttribute3AndRegIdAndAttribute4NotOrderByRegDtmDesc(
            compCd, UFileType.MOBILE.getCode(), loginId, "Y");
        result.put("mFiles", mFiles);

        if(!StringUtils.isEmpty(docMngNo) && !docMngNo.equals("undefined")) {
            List<UFile> aFiles = uFileRepository.findByCompCdAndDocumentHIdOrderByRegDtmDesc(compCd, docMngNo);
            result.put("aFiles", aFiles);
        } else {
            result.put("aFiles", null);
        }
        return result;
    }

    @Override
    public List<UFile> uploadEvidenceFiles(UFileDto uFileDto) {
        List<UFile> list = new ArrayList<>();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        String documentHId = uFileDto.getDocumentHId();
        String fileType = "";//관리구분(전표/예산)
        int idx = 0;
        for(int i = 0; i < documentHId.length(); i++) {
            if (documentHId.charAt(i) >= '0' && documentHId.charAt(i) <= '9') {
                idx = i;
                break;
            }
        }
        fileType = documentHId.substring(0, idx);
        if (fileType.equals("APP")) {
            fileType = UFileType.ESLIP.getCode();
        } else if (fileType.equals("SPIE") || fileType.equals("IMIE") || fileType.equals("SPAE") || fileType.equals("POIE") || fileType.equals("SPTE") //전기
                    || fileType.equals("SPAH") || fileType.equals("SPIH") || fileType.equals("SPTH")    //홀딩스
                    || fileType.equals("SPAD") || fileType.equals("SPID") || fileType.equals("SPTD")    //디앤코
                    || fileType.equals("SPAP") || fileType.equals("SPIP")                               //파트너즈
        ) {
            //전표번호 전체 적용
            fileType = UFileType.ESLIP.getCode();
        }

        List<MultipartFile> files = uFileDto.getFiles();
        for(MultipartFile file : files) {
            String uploadFileName = file.getOriginalFilename();

            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(uploadFileName));
            String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            Long fileSize = file.getSize();

            UUID uuid = UUID.randomUUID();
            String storedName = uuid.toString() + ext;

            try {
                if(fileName.contains("..")) {
                    throw new FileStorageException("파일명에 허용되지 않는 문자가 포함되어 있습니다." + fileName);
                }
                /*
                 * File IO on Server
                 * */
                Path targetLocation = this.fileStorageLocation.resolve(storedName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                    /*
                     * create Thumbnail image
                     * */
                    Thumbnails.of(new File(String.valueOf(targetLocation)))
                        .size(37, 35)
                        .toFile(new File(String.valueOf(this.fileStorageLocation.resolve("thumbnail-" + storedName))));
                }

                BigDecimal seq = BigDecimal.valueOf(0);
                if(!StringUtils.isEmpty(fileType)) {
                    Optional<UFile> s = uFileRepository.findTopByCompCdAndDocumentHIdOrderBySeqDesc(compCd, documentHId);
                    if(s.isPresent()) {
                        seq = s.get().getSeq();
                    }
                    seq.add(new BigDecimal(1));
                }

                /*
                 * Insert Data to U_FILE Table
                 * */

                UFile uFile = new UFile();
                uFile.setCompCd(compCd);
                uFile.setFileType(fileType);
                uFile.setDocumentHId(documentHId);
                if(!StringUtils.isEmpty(fileType)) {
                    uFile.setSeq(seq);
                }
                uFile.setOriginalName(fileName);
                uFile.setStoredName(storedName);
                uFile.setDownloadUrl(getDownloadFileUri(storedName));
                uFile.setFileKind(ext);
                uFile.setFileAmount(fileSize);
                if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                    !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                    uFile.setAttribute1("thumbnail-" +storedName);
                    uFile.setAttribute2(getDownloadFileUri("thumbnail-" +storedName));
                }

                uFileRepository.save(uFile);
                list.add(uFile);
            } catch (IOException ex) {
                throw new FileStorageException("파일" + fileName + "을 저장할 수 없습니다. 다시 시도해 보세요.", ex);
            }
        }
        return list;
    }

    @Override
    public List<UFile> uploadEvidenceFilesMobile(UFileDto uFileDto) {
        List<UFile> list = new ArrayList<>();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        String documentHId = uFileDto.getDocumentHId();
        String fileType = UFileType.MOBILE.getCode();

        List<MultipartFile> files = uFileDto.getFiles();
        for(MultipartFile file: files) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename().replaceAll(" ", "")));
            String ext = fileName.substring(fileName.lastIndexOf("."));
            Long fileSize = file.getSize();

            UUID uuid = UUID.randomUUID();
            String storedName = uuid.toString() + ext;

            try {
                if(fileName.contains("..")) {
                    throw new FileStorageException("파일명에 허용되지 않는 문자가 포함되어 있습니다." + fileName);
                }

                /*
                 *
                 * */
                Path targetLocation = this.fileStorageLocation.resolve(storedName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                if(!ext.equals(".pdf")) {
                    /*
                     *  create Thumbnail image
                     * */
                    Thumbnails.of(new File(String.valueOf(targetLocation)))
                        .size(37, 35)
                        .toFile(new File(String.valueOf(this.fileStorageLocation.resolve("thumbnail-" + storedName))));
                }

                /*
                 * 기존 문서 관리번호에 대해 저장된 첨부파일 마지막 seq 조회
                 * value = 0 : on Mobile
                 * */
                BigDecimal seq = new BigDecimal(0);

                /*
                 * Insert Data to U_FILE Table
                 * */
                UFile uFile = new UFile();
                uFile.setCompCd(compCd);
                uFile.setFileType(fileType);
                uFile.setOriginalName(fileName);
                uFile.setStoredName(storedName);

                uFile.setDownloadUrl(getDownloadFileUri(storedName));
                uFile.setFileKind(ext);
                uFile.setFileAmount(fileSize);
                if(!ext.equals(".pdf")) {
                    uFile.setAttribute1("thumbnail-" + storedName);
                    uFile.setAttribute2(getDownloadFileUri("thumbnail-" + storedName));
                }
                uFile.setAttribute3(UFileType.MOBILE.getCode());
                uFile.setAttribute4("N");

                uFileRepository.save(uFile);
                list.add(uFile);
            } catch (IOException e) {
                throw new FileStorageException("파일" + fileName + "을 저장할 수 없습니다. 다시 시도해보세요.", e);
            }
        }
        return list;
    }

    @Override
    public List<UFile> uploadEvidenceFilesBundle(UFileDto uFileDto) {
        List<UFile> list = new ArrayList<>();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        for(String slipNo : uFileDto.getSlipNoList()) {
            String documentHId = slipNo;
            String fileType = "";//관리구분(전표/예산)
            int idx = 0;
            for(int i = 0; i < slipNo.length(); i++) {
                if (documentHId.charAt(i) >= '0' && documentHId.charAt(i) <= '9') {
                    idx = i;
                    break;
                }
            }
            fileType = documentHId.substring(0, idx);
            if (fileType.equals("APP")) {
                fileType = UFileType.ESLIP.getCode();
            } else if (fileType.equals("SPIE") || fileType.equals("IMIE") || fileType.equals("SPAE") || fileType.equals("POIE") || fileType.equals("SPTE") //전기
                || fileType.equals("SPAH") || fileType.equals("SPIH") || fileType.equals("SPTH")    //홀딩스
                || fileType.equals("SPAD") || fileType.equals("SPID") || fileType.equals("SPTD")    //디앤코
                || fileType.equals("SPAP") || fileType.equals("SPIP")                               //파트너즈
            ) {
                //전표번호 전체 적용
                fileType = UFileType.ESLIP.getCode();
            }

            List<MultipartFile> files = uFileDto.getFiles();
            for(MultipartFile file : files) {
                String uploadFileName =file.getOriginalFilename();
                //IE has file path
                uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(uploadFileName));
                String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                Long fileSize = file.getSize();

                UUID uuid = UUID.randomUUID();
                String storedName = uuid.toString() + ext;

                try {
                    if(fileName.contains("..")) {
                        throw new FileStorageException("파일명에 허용되지 않는 문자가 포함되어 있습니다." + fileName);
                    }

                    /*
                     * File IO on Server
                     * */
                    Path targetLocation = this.fileStorageLocation.resolve(storedName);
                    Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                    if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                        !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".txt") && !ext.equals(".zip")) {
                        /*
                         * create Thumbnail image
                         * */
                        Thumbnails.of(new File(String.valueOf(targetLocation)))
                            .size(37, 35)
                            .toFile(new File(String.valueOf(this.fileStorageLocation.resolve("thumbnail-" +storedName))));
                    }

                    /*
                     * 기존 문서 관리번호에 대해 저장된 첨부파일 마지막 seq 조회
                     * */
                    BigDecimal seq = new BigDecimal(0);
                    if(!StringUtils.isEmpty(fileType)) {
                        //case. EA or BD : 전표 또는 예산 (임시번호가 아닐 때)
                        Optional<UFile> s = uFileRepository.findTopByCompCdAndDocumentHIdOrderBySeqDesc(compCd, slipNo);
                        if(s.isPresent()) {
                            seq = s.get().getSeq();
                        }
                        seq = seq.add(new BigDecimal(1));
                    }

                    /*
                     * Insert Data to U_FILE Table
                     * */
                    UFile uf = new UFile();
                    uf.setCompCd(compCd);
                    uf.setFileType(fileType);
                    uf.setDocumentHId(slipNo);
                    if(!StringUtils.isEmpty(fileType)) {
                        //case. EA or BD : 전표 또는 예산 (임시번호가 아닐 때)
                        uf.setSeq(seq);
                    }
                    uf.setOriginalName(fileName);
                    uf.setStoredName(storedName);
                    uf.setDownloadUrl(getDownloadFileUri(storedName));
                    uf.setFileKind(ext);
                    uf.setFileAmount(fileSize);
                    if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                        !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                        uf.setAttribute1("thumbnail-" +storedName);
                        uf.setAttribute2(getDownloadFileUri("thumbnail-" +storedName));
                    }

                    uFileRepository.save(uf);
                    list.add(uf);
                } catch(IOException ex) {
                    throw new FileStorageException("파일 " + fileName + "을 저장할 수 없습니다. 다시 시도해 보세요.", ex);
                }
            }
        }

        return list;
    }

    @Override
    public String deleteEvidenceFiles(UFileDto uFileDto) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        String documentHId = uFileDto.getDocumentHId();
        BigDecimal id = uFileDto.getId();

        if(StringUtils.isEmpty(id)) {
            List<UFile> fileList = uFileRepository.findByCompCdAndDocumentHId(compCd, documentHId);
            for(UFile uFile : fileList) {
                String attr3 = uFile.getAttribute3();
                if(Objects.nonNull(attr3)) {
                    if(attr3.equals(UFileType.MOBILE.getCode())) {
                        uFile.setFileType(UFileType.MOBILE.getCode());
                        uFile.setDocumentHId("");
                        uFile.setSeq(new BigDecimal(0));
                        uFile.setAttribute4("");
                    }
                } else {
                    uFileRepository.deleteById(uFile.getId());
                    String storedName = uFile.getStoredName();
                    String attr1 = uFile.getAttribute1();
                    String ext = uFile.getFileKind();

                    Path targetLocation = this.fileStorageLocation.resolve(storedName);
                    Path targetLocation2 = null;
                    if(Objects.nonNull(attr1)) {
                        targetLocation2 = this.fileStorageLocation.resolve(attr1);
                    }
                    try {
                        Files.delete(targetLocation);
                        if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                        !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                            Files.delete(targetLocation2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            uFileRepository.findById(id).ifPresent(c -> {
                if(Objects.nonNull(c.getAttribute3())) {
                    if(c.getAttribute3().equals(UFileType.MOBILE.getCode())) {
                        c.setFileType(UFileType.MOBILE.getCode());
                        c.setDocumentHId("");
                        c.setSeq(new BigDecimal(0));
                        c.setAttribute4("");
                    }
                } else {
                    uFileRepository.deleteById(c.getId());

                    String storedName = c.getStoredName();
                    String attr1 = c.getAttribute1();
                    String ext = c.getFileKind();

                    Path targetLocation = this.fileStorageLocation.resolve(storedName);
                    Path targetLocation2 = null;
                    if(Objects.nonNull(attr1)) {
                        targetLocation2 = this.fileStorageLocation.resolve(attr1);
                    }
                    try {
                        Files.delete(targetLocation);
                        if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                        !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                            Files.delete(targetLocation2);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return "삭제되었습니다.";
    }

    @Override
    public String removeMobileEvidenceFiles(UFileDto uFileDto) {
        List<BigDecimal> fileIds = uFileDto.getFileIdList();
        for(BigDecimal id: fileIds) {
            uFileRepository.findById(id).ifPresent(c -> {
                String sFileName = c.getStoredName();
                String sThumbnail = c.getAttribute1();

                Path sFilePath = this.fileStorageLocation.resolve(sFileName).normalize();
                try {
                    Files.deleteIfExists(sFilePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Path sThumbPath = this.fileStorageLocation.resolve(sFileName).normalize();
                try {
                    Files.deleteIfExists(sThumbPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                uFileRepository.deleteById(c.getId());
            });
        }
        return "삭제되었습니다.";
    }

    @Override
    public Optional<UFile> mobileCheckFile(UFileDto uFileDto) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        String documentHId = uFileDto.getDocumentHId();
        BigDecimal id = uFileDto.getId();

        String fileType = "APP";
        BigDecimal seq = new BigDecimal(0);

        /*
         * 기존 문서 관리번호에 대해 저장된 첨부파일 마지막 seq 조회
         * */
        Optional<UFile> s = uFileRepository.findTopByCompCdAndDocumentHIdOrderBySeqDesc(compCd, documentHId);
        if(s.isPresent()) {
            seq = s.get().getSeq();
        }
        seq = seq.add(new BigDecimal(1));

        String finalFileType = fileType;
        BigDecimal finalSeq = seq;
        uFileRepository.findById(id).ifPresent(c -> {
            c.setDocumentHId(documentHId);
            c.setFileType(finalFileType);
            c.setSeq(finalSeq);
            c.setAttribute4("Y");

            uFileRepository.save(c);
        });

        Optional<UFile> uFile = uFileRepository.findById(id);
        return uFile;
    }

    @Override
    public UFile getFileInfo(String compCd, BigDecimal id){
        List<UFile> list = uFileRepository.findByCompCdAndId(compCd, id);

        if(list.size() == 1){
            return list.get(0);
        }else {
            return null;
        }
    }



    private String getDownloadFileUri(String fileName) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/download2/")
                .path(fileName)
                .toUriString();
    }


}
