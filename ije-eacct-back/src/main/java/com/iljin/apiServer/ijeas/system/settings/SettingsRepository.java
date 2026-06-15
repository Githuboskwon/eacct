package com.iljin.apiServer.ijeas.system.settings;

import com.iljin.apiServer.ijeas.system.menu.Menu;
import com.iljin.apiServer.ijeas.system.menu.MenuKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Menu, MenuKey> {
}
