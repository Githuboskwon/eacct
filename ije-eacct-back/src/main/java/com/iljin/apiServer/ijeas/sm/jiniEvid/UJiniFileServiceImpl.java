package com.iljin.apiServer.ijeas.sm.jiniEvid;

import com.iljin.apiServer.core.config.FileStorageConfig;
import com.iljin.apiServer.core.files.FileStorageException;
import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class UJiniFileServiceImpl implements UJiniFileService {

    private final Util util;

    private final Environment environment;

    private final Path fileStorageLocation;

    private final Path jiniFileStorageLocation;

    private final UJiniFileQdslRepository uJiniFileQdslRepository;

    private final UJiniFileRepository uJiniFileRepository;

    private final Path xmlFileStorageLocation;

    @Autowired
    public UJiniFileServiceImpl(Util util, Environment environment, FileStorageConfig fileStorageConfig,
        UJiniFileRepository uJiniFileRepository, UJiniFileQdslRepository uJiniFileQdslRepository) {
        this.util = util;
        this.environment = environment;
        this.uJiniFileRepository = uJiniFileRepository;
        this.uJiniFileQdslRepository = uJiniFileQdslRepository;
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
            .toAbsolutePath().normalize();
        this.jiniFileStorageLocation = Paths.get(fileStorageConfig.getUploadDir(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")))
                .toAbsolutePath().normalize();

        try{
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("파일이 저장될 디렉토리를 생성하지 못했습니다.", e);
        }

        try{
            Files.createDirectories(this.jiniFileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("그룹웨어 파일이 저장될 디렉토리를 생성하지 못했습니다.", e);
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

        Map<String, Object> result = new HashMap<>();

        if(!StringUtils.isEmpty(docMngNo) && !docMngNo.equals("undefined")) {
            List<UJiniFile> aFiles = uJiniFileRepository.findByCompCdAndDocumentHIdOrderByRegDtmDesc(compCd, docMngNo);
            result.put("aFiles", aFiles);
        } else {
            result.put("aFiles", null);
        }
        return result;
    }

    @Override
    public List<UJiniFile> uploadEvidenceFiles(UJiniFileDto uJiniFileDto) {
        List<UJiniFile> list = new ArrayList<>();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        String documentHId = uJiniFileDto.getDocumentHId();
        String fileType = "";//관리구분(전표/예산)

        fileType = UJiniFileType.ESLIP.getCode();


        List<MultipartFile> files = uJiniFileDto.getFiles();
        for(MultipartFile file : files) {
            String uploadFileName = file.getOriginalFilename();

            uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(uploadFileName));
            String ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            Long fileSize = file.getSize();
            String storedPath = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")).toString();


            UUID uuid = UUID.randomUUID();
            String storedName = uuid.toString() + ext;

            try {
                if(fileName.contains("..")) {
                    throw new FileStorageException("파일명에 허용되지 않는 문자가 포함되어 있습니다." + fileName);
                }
                /*
                 * File IO on Server
                 * */
                Path targetLocation = this.jiniFileStorageLocation.resolve(storedName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                    /*
                     * create Thumbnail image
                     * */
                    Thumbnails.of(new File(String.valueOf(targetLocation)))
                        .size(37, 35)
                        .toFile(new File(String.valueOf(this.jiniFileStorageLocation.resolve("thumbnail-" + storedName))));
                }

                BigDecimal seq = BigDecimal.valueOf(0);

                Optional<UJiniFile> s = uJiniFileRepository.findTopByCompCdAndDocumentHIdOrderBySeqDesc(compCd, documentHId);
                if(s.isPresent()) {
                    seq = s.get().getSeq();
                    seq.add(new BigDecimal(1));
                }

                /*
                 * Insert Data to U_FILE Table
                 * */

                UJiniFile uJiniFile = new UJiniFile();
                uJiniFile.setCompCd(compCd);
                uJiniFile.setFileType(fileType);
                uJiniFile.setDocumentHId(documentHId);
                uJiniFile.setSeq(seq);
                uJiniFile.setOriginalName(fileName);
                uJiniFile.setStoredPath(storedPath);
                uJiniFile.setStoredName(storedName);
                uJiniFile.setDownloadUrl(getDownloadFileUri());
                uJiniFile.setFileKind(ext);
                uJiniFile.setFileAmount(fileSize);
                if(!ext.equals(".pdf") && !ext.equals(".tif") && !ext.equals(".tiff") &&
                    !ext.equals(".xls") && !ext.equals(".xlsx") && !ext.equals(".doc") && !ext.equals(".docx") && !ext.equals(".zip")) {
                    uJiniFile.setAttribute1("thumbnail-" +storedName);
                    uJiniFile.setAttribute2(getDownloadFileUri());
                }

                uJiniFileRepository.save(uJiniFile);
                list.add(uJiniFile);
            } catch (IOException ex) {
                throw new FileStorageException("파일" + fileName + "을 저장할 수 없습니다. 다시 시도해 보세요.", ex);
            }
        }
        return list;
    }


    @Override
    public String deleteEvidenceFiles(UJiniFileDto uJiniFileDto) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        String documentHId = uJiniFileDto.getDocumentHId();
        BigDecimal id = uJiniFileDto.getId();

        if(StringUtils.isEmpty(id)) {
            List<UJiniFile> fileList = uJiniFileRepository.findByCompCdAndDocumentHId(compCd, documentHId);
            for(UJiniFile uJiniFile : fileList) {
                uJiniFileRepository.deleteById(uJiniFile.getId());
                String storedPath = uJiniFile.getStoredPath();
                String storedName = uJiniFile.getStoredName();
                String attr1 = uJiniFile.getAttribute1();
                String ext = uJiniFile.getFileKind();

                Path targetLocation = this.fileStorageLocation.resolve(storedPath + "/" + storedName);
                Path targetLocation2 = null;
                if(Objects.nonNull(attr1)) {
                    targetLocation2 = this.fileStorageLocation.resolve(storedPath + "/" + attr1);
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
        } else {
            uJiniFileRepository.findById(id).ifPresent(c -> {
                uJiniFileRepository.deleteById(c.getId());
                String storedPath = c.getStoredPath();
                String storedName = c.getStoredName();
                String attr1 = c.getAttribute1();
                String ext = c.getFileKind();

                Path targetLocation = this.fileStorageLocation.resolve(storedPath + "/" + storedName);
                Path targetLocation2 = null;
                if(Objects.nonNull(attr1)) {
                    targetLocation2 = this.fileStorageLocation.resolve(storedPath + "/" + attr1);
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
            });
        }
        return "삭제되었습니다.";
    }

    @Override
    public UJiniFile getFileInfo(String compCd, BigDecimal id){
        List<UJiniFile> list = uJiniFileRepository.findByCompCdAndId(compCd, id);

        if(list.size() == 1){
            return list.get(0);
        }else {
            return null;
        }
    }

    private String getDownloadFileUri() {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/file/downloadJiniFile/")
                .toUriString();
    }


}
