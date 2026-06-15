package com.iljin.apiServer.ijeas.system.settings;

import com.iljin.apiServer.ijeas.card.CardUseListDto;
import com.iljin.apiServer.ijeas.slip.SlipDetailDto;
import com.iljin.apiServer.ijeas.slip.SlipHeaderDto;
import com.iljin.apiServer.ijeas.system.authority.MenuAuthDto;
import com.iljin.apiServer.ijeas.system.emp.EmployeeDto;
import com.iljin.apiServer.ijeas.system.menu.MenuDto;
import com.iljin.apiServer.ijeas.system.menu.UserMenuDto;

import java.util.List;

public interface SettingsRepositoryCustom {
    List<MenuDto> getAllMenuList();

    List<UserMenuDto> getUserMenuList(String compCd, String loginId);

    List<CardUseListDto> getUntrCardUseList(String compCd, String loginId, String previousDate, String nextDate);

    List<SlipHeaderDto> getRecentSlipList(String compCd, String loginId, String previousDate, String nextDate);

    List<EmployeeDto> getUntrDelegateCardUseList(String compCd, String loginId, String previousDate, String nextDate);

    List<SlipDetailDto> getPresentCondition(String compCd, String loginId, String thisYear);

    List<SlipDetailDto> getPresentBudgetCondition(String compCd, String deptCd, String thisYear);

    List<SettingsDto> getCountUntrCardUseList(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountUntrAirlineCardUseList(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountUntrEtaxList(String compCd, String previousDate, String nextDate);

    List<SettingsDto> getCountUnApprovalSlip(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountUnApprovalErpSlip(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountUnApprovalBdgt(String compCd, String loginId);

    List<SettingsDto> getCountTodoApproval(String compCd, String loginId);

    List<SettingsDto> getCountTodoApproval(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountRejected(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountRejectedApproval(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountRejectedFinance(String compCd, String loginId, String previousDate, String nextDa);

    List<MenuDto> getDashboardSettings(String loginId, String roleCd);

    List<MenuAuthDto> getAuthMenuList(String roleCd, String compCd);

    List<SettingsDto> getCountCompleteApproval(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountRequestApproval(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountRejectedBudget(String compCd, String loginId, String previousDate, String nextDate);

    List<SettingsDto> getCountRejectedPlanning(String compCd, String loginId, String previousDate, String nextDate);
}
