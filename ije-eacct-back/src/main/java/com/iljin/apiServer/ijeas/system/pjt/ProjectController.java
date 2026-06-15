package com.iljin.apiServer.ijeas.system.pjt;


import java.util.List;

import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * 프로젝트 List 조회
     * 프로젝트 명으로 조회
     */
    @GetMapping(value={"/list", "/list/{value}"})
    public ResponseEntity<List<ProjectDto>> getProjectList(@PathVariable(required = false) String value) {
        return new ResponseEntity<>(projectService.getProjectList(value), HttpStatus.OK);
    }

    @PostMapping("/slip/list")
    public ResponseEntity<List<ProjectDto>> getSlipProjectList(@RequestBody ProjectDto projectDto){
        return new ResponseEntity<>(projectService.getSlipProjectList(projectDto), HttpStatus.OK);
    }

    /**
     * 시공관리
     * 프로젝트 관리 번호 조회
     */
    @PostMapping("/registInfo/list")
    public ResponseEntity<List<PjtRegistInfoDto>> getRegistInfoProjectList(@RequestBody PjtRegistInfoDto pjtRegistInfoDto){
        return new ResponseEntity<>(projectService.getRegistInfoProjectList(pjtRegistInfoDto), HttpStatus.OK);
    }

}
