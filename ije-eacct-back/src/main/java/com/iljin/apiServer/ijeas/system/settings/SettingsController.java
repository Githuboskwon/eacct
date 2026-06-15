package com.iljin.apiServer.ijeas.system.settings;

import com.iljin.apiServer.core.util.Error;
import com.iljin.apiServer.ijeas.system.authority.AuthorityException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/settings")
public class SettingsController {
    private final SettingsService settingsService;

    @ExceptionHandler(AuthorityException.class)
    public ResponseEntity<Error> SettingsNotFound(SettingsException e) {
        Error error = new Error(2001, e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * EA-08-01 MainPage(Dashboard)
     * @param compCd is 로그인한 사용자 회사코드
     * @param loginId is 로그인한 사용자 아이디
     * This returns Json Object(format : Map)
     * */
    @GetMapping("/dashboard/{compCd}/{loginId}")
    public ResponseEntity<Map<String, Object>> getDashboardInfo(@PathVariable String compCd,
                                                                @PathVariable String loginId) {
        Map<String, Object> map = settingsService.getDashboardInfo(compCd, loginId);
        if(map.size() > 0) {
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/dashboardChart/{compCd}/{loginId}")
    public ResponseEntity<Map<String, Object>> getDashboardChartInfo(@PathVariable String compCd,
                                                                     @PathVariable String loginId) {
        Map<String, Object> map = settingsService.getDashboardChartInfo(compCd, loginId);
        if(map.size() > 0) {
            return new ResponseEntity<>(map, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * EA-08-03 Settings(custom MainPage menu)
     * Desc. 설정 조회
     * @param loginId is 현재 로그인 사용자 아이디
     * */
    @GetMapping("/{loginId}")
    public ResponseEntity<List<SettingsDto>> getUserSettings(@PathVariable String loginId) {
        List<SettingsDto> list = settingsService.getUserSettings(loginId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    /**
     * EA-08-03 Settings(custom MainPage menu)
     * Desc. 저장
     * @param dashboardDto is jsonArray.
     * Desc. param needs compCd, loginId, menuNo, menuIconCd for each row.
     * */
    @PutMapping("/")
    public ResponseEntity<String> saveUserSettings(@RequestBody List<SettingsDto> dashboardDto) {
        return settingsService.saveUserSettings(dashboardDto);
    }
}
