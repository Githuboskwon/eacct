package com.iljin.apiServer.ijeas.system.settings;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface SettingsService {

    Map<String, Object> getDashboardInfo(String compCd, String loginId);

    Map<String, Object> getDashboardChartInfo(String compCd, String loginId);

    List<SettingsDto> getUserSettings(String loginId);

    @Modifying
    @Transactional
    ResponseEntity<String> saveUserSettings(List<SettingsDto> settingsDtos);
}
