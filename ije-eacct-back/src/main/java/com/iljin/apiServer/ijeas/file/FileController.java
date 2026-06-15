package com.iljin.apiServer.ijeas.file;

import com.iljin.apiServer.core.files.File;
import com.iljin.apiServer.core.files.FileResponse;
import com.iljin.apiServer.core.files.FileService;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.sm.evid.UFile;
import com.iljin.apiServer.ijeas.sm.evid.UFileService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import com.iljin.apiServer.ijeas.sm.jiniEvid.UJiniFile;
import com.iljin.apiServer.ijeas.sm.jiniEvid.UJiniFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/file")
public class FileController {

    private final FileService fileService;
    private final UFileService uFileService;
    private final UJiniFileService uJiniFileService;
    private final Util util;

    @PostMapping("/upload")
    public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        File uFile = fileService.storeFile(file);

        return new FileResponse(uFile.getId(),
                uFile.getOriginalName(),
                uFile.getStoredName(),
                uFile.getDownloadUrl(),
                file.getContentType(), file.getSize());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFileById(@PathVariable String id) {
        fileService.deleteById(Long.parseLong(id));

        return new ResponseEntity<>("파일이 삭제되었습니다.", HttpStatus.OK);
    }

    @PostMapping("/upload/files")
    public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @GetMapping("/download/{storedFileName:.*}")
    public ResponseEntity<Resource> downloadByStoredName(@PathVariable String storedFileName, @RequestParam(value = "id", required = false) Long id, HttpServletRequest request) {
        return fileService.downloadFile(storedFileName, id, request);
    }

    @GetMapping("/download2/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile2(@PathVariable String fileName, @RequestParam(value = "id", required = false)
        BigDecimal id, HttpServletRequest request) throws UnsupportedEncodingException {
        String contentType = null;
        Resource resource = null;
        String originalFileName = null;

        try{
            if(id != null) {
                originalFileName = fileService.getOriginalFileName(id.longValue());
            } else {
                originalFileName = fileService.getOriginalFileNameByStoredFileName(fileName);
            }

            resource = fileService.loadFileAsResource(fileName);
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        if(contentType.equals("application/pdf")) {
            contentType += "; charset=UTF-8";
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\"");
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\"");
        }

        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .headers(headers)
            .body(resource);
    }


    @GetMapping("/download/before/{fileName:.+}")
    public ResponseEntity<Resource> downloadBeforeFile(@PathVariable String fileName, @RequestParam(value = "id", required = false)
    BigDecimal id, HttpServletRequest request) throws UnsupportedEncodingException {
        String contentType = null;
        Resource resource = null;
        String originalFileName = null;
        String path = null;
        String ext = null;

        try{
            if(id != null) {
                UFile ufile = uFileService.getFileInfo(util.getLoginCompCd(), id);
                if(ufile != null){
                    originalFileName = ufile.getOriginalName();
                    path = ufile.getAttribute5();
                    ext = ufile.getFileKind();
                }
            }

            resource = fileService.loadOldFileAsResource(path, fileName);
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath() + ext);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        if(contentType.equals("application/pdf")) {
            contentType += "; charset=UTF-8";
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\"");
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\"");
        }

        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(resource);
    }


    @GetMapping("/downloadJiniFile/{id}")
    public ResponseEntity<Resource> downloadJiniFile(@PathVariable BigDecimal id, HttpServletRequest request) throws UnsupportedEncodingException {
        String contentType = null;
        Resource resource = null;
        String originalFileName = null;
        String path = null;
        String storedName = null;

        try{
            if(id != null) {
                UJiniFile uJinifile = uJiniFileService.getFileInfo(util.getLoginCompCd(), id);
                if(uJinifile != null){
                    originalFileName = uJinifile.getOriginalName();
                    path = uJinifile.getStoredPath();
                    storedName = uJinifile.getStoredName();
                }
            }

            resource = fileService.loadJiniFileAsResource(path, storedName);
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        if(contentType.equals("application/pdf")) {
            contentType += "; charset=UTF-8";
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\"");
        } else {
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + URLEncoder.encode(originalFileName, "UTF-8") + "\"");
        }

        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "Content-Disposition");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .headers(headers)
                .body(resource);
    }
}
