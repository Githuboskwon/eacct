package com.iljin.apiServer.ijeas.system.expend;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ExpendServiceImpl implements ExpendService {

    private final Util util;

    private final ExpendQdslRepository expendQdslRepository;

    private final ExpendRepository expendRepository;

    @Override
    public List<ExpendDto> getExpendList(ExpendDto expendDto) {
        return expendQdslRepository.getExpendList(expendDto);
    }

    @Override
    public ResponseEntity<String> saveExpend(List<ExpendDto> expendDtos) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        for(ExpendDto expendDto : expendDtos) {

            ExpendKey expendKey = new ExpendKey();
            expendKey.setCompCd(compCd);
            expendKey.setExpendCd(expendDto.getExpendCd());
            expendKey.setStartDate(expendDto.getStartDate());

            Optional<Expend> result = expendRepository.findById(expendKey);
            if(result.isPresent()) {
                //update
                result.ifPresent(c -> {
                    c.updateExpend(
                            expendDto.getExpendCd(),
                            expendDto.getEndDate(),
                            expendDto.getWreathYn(),
                            expendDto.getMutualYn(),
                            expendDto.getHoliday(),
                            expendDto.getExpendAmt(),
                            expendDto.getRemark()
                    );

                    expendRepository.save(c);
                });
            } else {
                //insert
                Optional<Expend> dupl = expendRepository.findByExpendCdAndStartDate(expendDto.getExpendCd(), expendDto.getStartDate());

                if(dupl.isPresent()) {
                    throw new RuntimeException("중복 데이터가 존재합니다.");
                } else {
                    Expend expend = new Expend().builder()
                                    .compCd(compCd)
                                    .startDate(expendDto.getStartDate())
                                    .endDate(expendDto.getEndDate())
                                    .expendCd(expendDto.getExpendCd())
                                    .wreathYn(expendDto.getWreathYn())
                                    .mutualYn(expendDto.getMutualYn())
                                    .holiday(expendDto.getHoliday())
                                    .expendAmt(expendDto.getExpendAmt())
                                    .remark(expendDto.getRemark())
                                    .build();
                    expendRepository.save(expend);
                }
            }
        }
        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteExpend(ExpendDto expendDto) {
        String compCd = expendDto.getCompCd();
        String expendCd = expendDto.getExpendCd();
        String startDate = expendDto.getStartDate();
        ExpendKey expendKey = new ExpendKey();

        expendKey.setCompCd(compCd);
        expendKey.setExpendCd(expendCd);
        expendKey.setStartDate(startDate);

        Optional<Expend> dupl = expendRepository.findByExpendCdAndStartDate(expendDto.getExpendCd(), expendDto.getStartDate());

        if(dupl.isEmpty()) {
            throw new RuntimeException("데이터가 없습니다.");
        }

        expendRepository.deleteById(expendKey);

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }
}
