package com.iljin.apiServer.ijeas.sm.close;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.code.CodeDetailRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AcctPeriodServiceImpl implements AcctPeriodService {

    private final Util util;

    private final AcctPeriodRepository acctPeriodRepository;
    private final AcctPeriodRepositoryCustom acctPeriodRepositoryCustom;

    private final CodeDetailRepository codeDetailRepository;

    @Override
    public List<AcctPeriodDto> getAcctPeriodList(AcctPeriodDto acctPeriodDto) {
        return acctPeriodRepositoryCustom.getAcctPeriodList(acctPeriodDto);
    }

    public List<AcctPeriodDto> getAcctPeriodOpenList(String closStatCd) {
        AcctPeriodDto acctPeriodDto = new AcctPeriodDto();
        acctPeriodDto.setCompCd(util.getLoginUser().getCompCd());
        acctPeriodDto.setClosStatCd(closStatCd);

        return acctPeriodRepositoryCustom.getAcctPeriodOpenList(acctPeriodDto);
    }

    public AcctPeriodDto getAcctPeriodOpenCloseDate() {
        return acctPeriodRepositoryCustom.getAcctPeriodOpenCloseDate();
    }

    @Override
    public String getAcctPeriodOpenDate() {
        String openDate = "";
        Optional<AcctPeriod> acctPeriod = acctPeriodRepository.findTopByClosStatCdOrderByClosYmAsc(ClosStatCd.OPEN.getCode());

        if(acctPeriod.isPresent()){
            openDate = util.getStartDate(acctPeriod.get().closYm);

        }else{
            LocalDate localDate = LocalDate.now();
            String yearMonth = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            openDate = util.getStartDate(yearMonth);
        }

        return openDate;
    }

    @Override
    public String getMainAcctPeriodFromOpenDate() {
        String openDate = "";
        Optional<AcctPeriod> acctPeriod = acctPeriodRepository.findTopByClosStatCdAndMainCloseYnOrderByClosYmAsc(ClosStatCd.OPEN.getCode(), "Y");

        if(acctPeriod.isPresent()){
            openDate = util.getStartDate(acctPeriod.get().closYm);
        }else{
            LocalDate localDate = LocalDate.now();
            String yearMonth = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            openDate = util.getStartDate(yearMonth);
        }

        return openDate;
    }

    @Override
    public String getMainAcctPeriodToOpenDate() {
        String endDate = "";
        Optional<AcctPeriod> acctPeriod = acctPeriodRepository.findTopByClosStatCdAndMainCloseYnOrderByClosYmDesc(ClosStatCd.OPEN.getCode(), "Y");

        if(acctPeriod.isPresent()){
            endDate = util.getEndDate(acctPeriod.get().closYm);
        }else{
            LocalDate localDate = LocalDate.now();
            String yearMonth = localDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
            endDate = util.getEndDate(yearMonth);
        }

        return endDate;
    }

    @Override
    public ResponseEntity<String> saveAcctPeriod(List<AcctPeriodDto> acctPeriodDtos) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();

        for(AcctPeriodDto acctPeriodDto : acctPeriodDtos) {
            acctPeriodDto.setClosYm(acctPeriodDto.getClosYm().replaceAll("-", ""));

            AcctPeriodKey acctPeriodKey = new AcctPeriodKey();
            acctPeriodKey.setCompCd(acctPeriodDto.getCompCd());
            acctPeriodKey.setBaCd(acctPeriodDto.getBaCd());
            acctPeriodKey.setClosYm(acctPeriodDto.getClosYm());

            Optional<AcctPeriod> acctPeriod = acctPeriodRepository.findById(acctPeriodKey);
            if(acctPeriod.isPresent()) {
                //update
                acctPeriod.ifPresent(c -> {
                    c.setMainCloseYn(acctPeriodDto.getMainCloseYn());
                    c.setClosStatCd(acctPeriodDto.getClosStatCd());
                    c.setChgId(loginId);
                    c.setChgDtm(LocalDateTime.now());

                    acctPeriodRepository.save(c);
                });
            } else {
                //insert
                AcctPeriod c = new AcctPeriod();
                try {
                    PropertyUtils.copyProperties(c, acctPeriodDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                c.setMainCloseYn(c.getMainCloseYn());
                c.setCompCd(c.getCompCd());
                c.setRegId(loginId);
                c.setRegDtm(LocalDateTime.now());
                c.setChgId(loginId);
                c.setChgDtm(LocalDateTime.now());

                acctPeriodRepository.save(c);
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteAcctPeriod(AcctPeriodDto acctPeriodDto) {
        AcctPeriodKey acctPeriodKey = new AcctPeriodKey();
        acctPeriodKey.setCompCd(acctPeriodDto.getCompCd());
        acctPeriodKey.setBaCd(acctPeriodDto.getBaCd());
        acctPeriodKey.setClosYm(acctPeriodDto.getClosYm().replaceAll("-", ""));

        acctPeriodRepository.deleteById(acctPeriodKey);

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveAcctPeriodMonthlyAuto(String closeYm) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();
        closeYm = closeYm.replaceAll("-", "");

        List<Map> list = codeDetailRepository.findByGroupCd(compCd, "BA_CD");
        for(Map c : list) {
            String baCd = c.get("detailCd").toString();

            AcctPeriodKey acctPeriodKey = new AcctPeriodKey();
            acctPeriodKey.setCompCd(compCd);
            acctPeriodKey.setBaCd(baCd);
            acctPeriodKey.setClosYm(closeYm);
            Optional<AcctPeriod> acctPeriod = acctPeriodRepository.findById(acctPeriodKey);
            if(acctPeriod.isPresent()) {
                throw new RuntimeException("기존 내역이 존재합니다.");
            } else {
                AcctPeriod period = new AcctPeriod();
                period.setCompCd(compCd);
                period.setBaCd(baCd);
                period.setClosYm(closeYm);
                period.setClosStatCd("Open");
                period.setClosOln("오픈");
                period.setRegId(loginId);
                period.setRegDtm(LocalDateTime.now());
                period.setChgId(loginId);
                period.setChgDtm(LocalDateTime.now());

                acctPeriodRepository.save(period);
            }
        }

        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> openAcctPeriod(String closeYm) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();
        closeYm = closeYm.replaceAll("-", "");

        List<AcctPeriod> list = acctPeriodRepository.findByCompCdAndClosYmOrderByBaCd(compCd, closeYm);
        for(AcctPeriod acctPeriod : list) {
            acctPeriod.setClosStatCd("Open");
            acctPeriod.setChgId(loginId);
            acctPeriod.setChgDtm(LocalDateTime.now());

            acctPeriodRepository.save(acctPeriod);
        }

        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> closeAcctPeriod(String closeYm) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();
        closeYm = closeYm.replaceAll("-", "");

        List<AcctPeriod> list = acctPeriodRepository.findByCompCdAndClosYmOrderByBaCd(compCd, closeYm);
        for(AcctPeriod acctPeriod : list) {
            acctPeriod.setClosStatCd("Close");
            acctPeriod.setChgId(loginId);
            acctPeriod.setChgDtm(LocalDateTime.now());

            acctPeriodRepository.save(acctPeriod);
        }

        return new ResponseEntity<>("Ok", HttpStatus.OK);
    }
}
