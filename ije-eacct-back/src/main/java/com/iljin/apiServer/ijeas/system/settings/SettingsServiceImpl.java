package com.iljin.apiServer.ijeas.system.settings;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.card.CardUseListDto;
import com.iljin.apiServer.ijeas.slip.SlipDetailDto;
import com.iljin.apiServer.ijeas.slip.tax.TaxDtiNtsMainDto;
import com.iljin.apiServer.ijeas.slip.tax.TaxDtiNtsMainQdslRepository;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodService;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import com.iljin.apiServer.ijeas.system.emp.EmployeeDto;
import com.iljin.apiServer.ijeas.system.menu.UserMenu;
import com.iljin.apiServer.ijeas.system.menu.UserMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SettingsServiceImpl implements SettingsService {
    private final Util util;
    private final UserMenuRepository userMenuRepository;
    private final AcctPeriodService acctPeriodService;
    private final SettingsRepositoryCustom settingsRepositoryCustom;
    private final TaxDtiNtsMainQdslRepository taxDtiNtsMainQdslRepository;

    @Override
    public Map<String, Object> getDashboardInfo(String compCd, String loginId) {
        User loginUser = util.getLoginUser();
        String roleCd = loginUser.getRoles().get(0).getRole();
        String loginUserDeptCd = loginUser.getDeptCd();

        // 마감상태여부 Open, 메인기간여부 체크 Min ~ Max
        String fromMainOpenDate = acctPeriodService.getMainAcctPeriodFromOpenDate();
        String nextMonthDate = acctPeriodService.getMainAcctPeriodToOpenDate();

        LocalDate localDate = LocalDate.parse(fromMainOpenDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        String thisMonth = String.valueOf(localDate.getMonthValue());

        String firstDay = "01";

        if(thisMonth.length() < 2) {
            thisMonth = "0" +thisMonth;
        }

        String localMonthDate = localDate.getYear() + thisMonth + firstDay;

        Map<String, Object> result = new HashMap<>();

        // 메인회계기간 시작기간
        result.put("localMonthDate", localMonthDate);

        // 메인회계기간 종료기간
        result.put("nextMonthDate", nextMonthDate);
        /*
         * 01. 메인매뉴
         * */
        /*List<MenuDto> mnList = dashboardRepositoryCustom.getAllMenuList();*/
//        List<MenuAuthDto> mnList = settingsRepositoryCustom.getAuthMenuList(roleCd, compCd);//권한별 매뉴 조회
//        if(mnList.size() > 0) {
//            result.put("menuList", mnList);
//        } else {
//            result.put("menuList", null);
//        }

        /*
         * 02. 메인바로가기 - 개인화 메뉴
         * */
//        List<UserMenuDto> umList = settingsRepositoryCustom.getUserMenuList(compCd, loginId);
//        if(umList.size() > 0) {
//            result.put("userMenuList", umList);
//        } else {
//            result.put("userMenuList", null);
//        }

        /*
         * 03. 법인카드 내역 - 미처리 3건
         * */
        List<CardUseListDto> unCardUseList = settingsRepositoryCustom.getUntrCardUseList(compCd, loginId, localMonthDate, nextMonthDate);
        if(unCardUseList.size() > 0) {
            result.put("unCardList", unCardUseList);
        } else {
            result.put("unCardList", null);
        }

        /*
         * 04. 법인카드 미처리 내역 - 위임자 리스트
         * */
        List<EmployeeDto> untrDelegateCard = settingsRepositoryCustom.getUntrDelegateCardUseList(compCd, loginId, localMonthDate, nextMonthDate);
        if(untrDelegateCard.size() > 0) {
            result.put("untrDelegateCard", untrDelegateCard);
        } else {
            result.put("untrDelegateCard", null);
        }


        /*
         * 04. 경비처리 내역 - 전표 작성 3건
         * */
//        List<SlipHeaderDto> rcSlipList = settingsRepositoryCustom.getRecentSlipList(compCd, loginId, localMonthDate, nextMonthDate);
//        if(rcSlipList.size() > 0) {
//            result.put("rcSlipList", rcSlipList);
//        } else {
//            result.put("rcSlipList", null);
//        }

        /*
         * 05. 사용현황 - 현재년도의 확정된 전표의 계정별 통계
         * */
//        List<SlipDetailDto> psCond = settingsRepositoryCustom.getPresentCondition(compCd, loginId, thisYear);
//        if(psCond.size() > 0) {
//            result.put("psCond", psCond);
//        } else {
//            result.put("psCond", null);
//        }

        // ###### 별도 API 생성  시작 ######
//        List<SlipDetailDto> psCond = settingsRepositoryCustom.getPresentBudgetCondition(compCd, loginUserDeptCd, "202306");
//
//        // 복리후생비
//        List<SlipDetailDto> doughnutData1 = psCond.stream()
//                .filter(f -> f.getBudgetAcctCode().equals("B550814") )
//                .collect(Collectors.toList());
//
//        // 접대비
//        List<SlipDetailDto> doughnutData2 = psCond.stream()
//                .filter(f -> f.getBudgetAcctCode().equals("B550813") )
//                .collect(Collectors.toList());
//
//        if(doughnutData1.size() > 0) {
//            result.put("doughnutData1", doughnutData1);
//        } else {
//            result.put("doughnutData1", null);
//        }
//
//        if(doughnutData2.size() > 0) {
//            result.put("doughnutData2", doughnutData2);
//        } else {
//            result.put("doughnutData2", null);
//        }
        // ###### 별도 API 생성 종료 ######

        /* 06. 처리예정건수 */
        Map<String, Object> counts = new HashMap<>();
        // result1 - 법인카드 미처리
        List<SettingsDto> untrCard = settingsRepositoryCustom.getCountUntrCardUseList(compCd, loginId, localMonthDate, nextMonthDate);
        if(untrCard.size() > 0) {
            counts.put("untrCard", untrCard);
        } else {
            counts.put("untrCard", null);
        }

        // result2 - 세금계산서 미처리
        //List<SettingsDto> untrEtax = settingsRepositoryCustom.getCountUntrEtaxList(compCd, localMonthDate, nextMonthDate);
        List<TaxDtiNtsMainDto> untrEtax = taxDtiNtsMainQdslRepository.getCountUntrEtaxList(compCd, localMonthDate, nextMonthDate);
        if(untrEtax.size() > 0) {
            counts.put("untrEtax", untrEtax);
        } else {
            counts.put("untrEtax", null);
        }

        // result3 - 항공권 법인카드 미처리
        List<SettingsDto> untrAirlineCard = settingsRepositoryCustom.getCountUntrAirlineCardUseList(compCd, loginId, localMonthDate, nextMonthDate);
        if(untrAirlineCard.size() > 0) {
            counts.put("untrAirlineCard", untrAirlineCard);
        } else {
            counts.put("untrAirlineCard", null);
        }

        // result4 - 전표 미상신
        List<SettingsDto> unApprSlip = settingsRepositoryCustom.getCountUnApprovalSlip(compCd, loginId, localMonthDate, nextMonthDate);
        if(unApprSlip.size() > 0) {
            counts.put("unApprSlip", unApprSlip);
        } else {
            counts.put("unApprSlip", null);
        }

        // result5 - 전표 결재반려, 검인반려한 문서 count
        List<SettingsDto> rjct = settingsRepositoryCustom.getCountRejected(compCd, loginId, localMonthDate, nextMonthDate);
        if(rjct.size() > 0) {
            counts.put("rjct", rjct);
        } else {
            counts.put("rjct", null);
        }

        // result6 - 결재할 문서(전체)
        List<SettingsDto> todoAppr = settingsRepositoryCustom.getCountTodoApproval(compCd, loginId);
        if(todoAppr.size() > 0) {
            counts.put("todoAppr", todoAppr);
        } else {
            counts.put("todoAppr", null);
        }

        // result4 - 예산 미상신
//        List<SettingsDto> unApprBdgt = settingsRepositoryCustom.getCountUnApprovalBdgt(compCd, loginId);
//        if(unApprBdgt.size() > 0) {
//            counts.put("unApprBdgt", null);
//        } else {
//            counts.put("unApprBdgt", null);
//        }

        // result6 - 전표 반려한 문서
//         List<SettingsDto> rjctAppr = settingsRepositoryCustom.getCountRejectedApproval(compCd, loginId, thisMonth);
//         if(rjctAppr.size() > 0) {
//             counts.put("rjctAppr", rjctAppr);
//         } else {
//             counts.put("rjctAppr", null);
//         }
        // result7 - 결재한 문서
//        List<SettingsDto> completeAppr = settingsRepositoryCustom.getCountCompleteApproval(compCd, loginId, todayMonthStartDate, todayNextDate);
//        if(completeAppr.size() > 0) {
//            counts.put("completeAppr", completeAppr);
//        } else {
//            counts.put("completeAppr", null);
//        }
        // result8 - 상신한 문서
//        List<SettingsDto> requestAppr = settingsRepositoryCustom.getCountRequestApproval(compCd, loginId, todayMonthStartDate, todayNextDate);
//        if(requestAppr.size() > 0) {
//            counts.put("requestAppr", requestAppr);
//        } else {
//            counts.put("requestAppr", null);
//        }

        // result9 - 전표 결재반려한 문서
//        List<SettingsDto> rjctAppr = settingsRepositoryCustom.getCountRejectedApproval(compCd, loginId, localMonthDate, nextMonthDate);
//        if(rjctAppr.size() > 0) {
//            counts.put("rjctAppr", rjctAppr);
//        } else {
//            counts.put("rjctAppr", null);
//        }
        // result10 - 재경반려한 문서
//        List<SettingsDto> rjctFnc = settingsRepositoryCustom.getCountRejectedFinance(compCd, loginId, localMonthDate, nextMonthDate);
//        if(rjctFnc.size() > 0) {
//            counts.put("rjctFnc", rjctFnc);
//        } else {
//            counts.put("rjctFnc", null);
//        }
        // result11 - ERP 전표 미상신
//        List<SettingsDto> unApprErpSlip = settingsRepositoryCustom.getCountUnApprovalErpSlip(compCd, loginId, localMonthDate, nextMonthDate);
//        if(unApprErpSlip.size() > 0) {
//            counts.put("unApprErpSlip", unApprErpSlip);
//        } else {
//            counts.put("unApprErpSlip", null);
//        }
        // result12 - 예산 결재 반려한 문서
//        List<SettingsDto> rjctBjt = settingsRepositoryCustom.getCountRejectedBudget(compCd, loginId, localMonthDate, nextMonthDate);
//        if(rjctFnc.size() > 0) {
//            counts.put("rjctBjt", rjctBjt);
//        } else {
//            counts.put("rjctBjt", null);
//        }
        // result 13 - 기획 반려한 문서
//        List<SettingsDto> rjctPln = settingsRepositoryCustom.getCountRejectedPlanning(compCd, loginId, localMonthDate, nextMonthDate);
//        if(rjctFnc.size() > 0) {
//            counts.put("rjctPln", rjctPln);
//        } else {
//            counts.put("rjctPln", null);
//        }

        if(counts.size() > 0) {
            result.put("counts", counts);
        } else {
            result.put("counts", null);
        }


        return result;
    }

    @Override
    public Map<String, Object> getDashboardChartInfo(String compCd, String loginId) {
        User loginUser = util.getLoginUser();
        // 예산 조회이기때문에 예산부서를 사용 할 것
        String loginUserDeptCd = loginUser.getEmployee().getCctrCd();

        Map<String, Object> result = new HashMap<>();

        LocalDate now = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
        String localMonthDate = now.format(formatter);

        List<SlipDetailDto> psCond = settingsRepositoryCustom.getPresentBudgetCondition(compCd, loginUserDeptCd, localMonthDate);

        // 복리후생비
        List<SlipDetailDto> doughnutData1 = psCond.stream()
                .filter(f -> f.getBudgetAcctCode().equals("B550814") )
                .collect(Collectors.toList());

        // 접대비
        List<SlipDetailDto> doughnutData2 = psCond.stream()
                .filter(f -> f.getBudgetAcctCode().equals("B550813") )
                .collect(Collectors.toList());

        if(doughnutData1.size() > 0) {
            result.put("doughnutData1", doughnutData1);
        } else {
            result.put("doughnutData1", null);
        }

        if(doughnutData2.size() > 0) {
            result.put("doughnutData2", doughnutData2);
        } else {
            result.put("doughnutData2", null);
        }

        return result;
    }

    @Override
    public List<SettingsDto> getUserSettings(String loginId) {
        List<SettingsDto> list = new ArrayList<>();
        String compCd = util.getLoginCompCd();

        list = userMenuRepository.getUserSettings(loginId,compCd)
                .stream()
                .map(s -> new SettingsDto(
                        String.valueOf(Optional.ofNullable(s[0]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[1]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[2]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[3]).orElse(""))
                        ,String.valueOf(Optional.ofNullable(s[4]).orElse(""))
                ))
                .collect(Collectors.toList());

        return list;
    }

    @Override
    public ResponseEntity<String> saveUserSettings(List<SettingsDto> list) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        if (list.size() > 0) {
            userMenuRepository.deleteByCompCdAndUserId(compCd, loginId);
            for (SettingsDto settingsDto : list) {
                UserMenu userMenu = new UserMenu();
                userMenu.setCompCd(settingsDto.getCompCd());
                userMenu.setUserId(settingsDto.getLoginId());
                userMenu.setMenuNo(settingsDto.getMenuNo());
                userMenu.setMenuIconCd(settingsDto.getMenuIconCd());
                userMenu.setMenuOrdr(settingsDto.getMenuOrdr());

                userMenuRepository.save(userMenu);
            }
        }else{
            // 선택된 메뉴가 없을때는 삭제만 하도록 변경.
            userMenuRepository.deleteByCompCdAndUserId(compCd,loginId);
        }
        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }
}
