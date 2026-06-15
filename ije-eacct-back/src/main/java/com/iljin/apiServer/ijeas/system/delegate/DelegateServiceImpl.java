package com.iljin.apiServer.ijeas.system.delegate;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DelegateServiceImpl implements DelegateService {

    private final DelegateQdslRepository delegateQdslRepository;

    private final DelegateRepository delegateRepository;

    @Override
    public List<DelegateDto> getDelegateList(DelegateDto delegateDto) {
        return delegateQdslRepository.getDelegateList(delegateDto);
    }

    @Override
    public ResponseEntity<String> deleteDelegate(@RequestBody DelegateDto delegateDto){

        Integer count = delegateRepository.countByGiveUserIdAndReceiveUserId(delegateDto.getGiveUserId(),delegateDto.getReceiveUserId());

        if (count == 0){
            throw new RuntimeException("존재하지 않는 데이터 입니다.");
        }

        DelegateKey delegateKey = new DelegateKey();
        delegateKey.setCompCd(delegateDto.getCompCd());
        delegateKey.setGiveUserId(delegateDto.getGiveUserId());
        delegateKey.setReceiveUserId(delegateDto.getReceiveUserId());
        delegateKey.setDelegateSeq(delegateDto.getDelegateSeq());

        Optional<Delegate> result = delegateRepository.findById(delegateKey);
        if(result.isPresent()) {
            result.ifPresent(c -> {
                    c.delete(delegateDto);
                });
        }
        return new ResponseEntity<>("해제되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveDelegate(DelegateDto delegateDto) {

        if(delegateDto.getGiveUserId().equals(delegateDto.getReceiveUserId())){
            throw new RuntimeException("위임자와 동일한 수임자는 지정할 수 없습니다.");
        }

        Integer count = delegateRepository.countByGiveUserIdAndReceiveUserIdAndDateBetween(delegateDto.getGiveUserId(),delegateDto.getReceiveUserId(),delegateDto.getStartDate(),delegateDto.getEndDate());

        if (count > 0){
            throw new RuntimeException("위임기간이 중복되는 건이 존재합니다.");
        }else{

            Integer seq = delegateRepository.countByGiveUserIdAndReceiveUserId(delegateDto.getGiveUserId(),delegateDto.getReceiveUserId());

            Delegate newDelegate = Delegate.builder()
                    .compCd(delegateDto.getCompCd())
                    .giveUserId(delegateDto.getGiveUserId())
                    .receiveUserId(delegateDto.getReceiveUserId())
                    .delegateSeq(BigDecimal.valueOf(seq+1))
                    .delegateStatCd("1") // 위임
                    .startDate(delegateDto.getStartDate())
                    .endDate(delegateDto.getEndDate())
                    .remark(delegateDto.getRemark())
                    .build();
            delegateRepository.save(newDelegate);

            return new ResponseEntity<>("생성 되었습니다.", HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<String> updateDelegate(DelegateDto delegateDto) {

        if(delegateDto.getGiveUserId().equals(delegateDto.getReceiveUserId())){
            throw new RuntimeException("위임자와 동일한 수임자는 지정할 수 없습니다.");
        }

        Integer count = delegateRepository.countByGiveUserIdAndReceiveUserIdAndDateBetweenAndDelegateSeqNot(delegateDto.getGiveUserId(),delegateDto.getReceiveUserId(),delegateDto.getStartDate(),delegateDto.getEndDate(), delegateDto.getDelegateSeq());

        if (count > 0){
            throw new RuntimeException("위임기간이 중복되는 건이 존재합니다.");
        }else{

            Optional<Delegate> delegate = delegateRepository.findByGiveUserIdAndReceiveUserIdAndDelegateSeq(delegateDto.getGiveUserId(),delegateDto.getReceiveUserId(),delegateDto.getDelegateSeq());

            DelegateKey delegateKey = new DelegateKey();
            delegateKey.setCompCd(delegateDto.getCompCd());
            delegateKey.setGiveUserId(delegateDto.getGiveUserId());
            delegateKey.setReceiveUserId(delegateDto.getReceiveUserId());
            delegateKey.setDelegateSeq(delegateDto.getDelegateSeq());
            Optional<Delegate> result = delegateRepository.findById(delegateKey);

            if(result.isPresent()) {
                result.ifPresent(c -> {
                    c.update(delegateDto);
                });
            }

            return new ResponseEntity<>("수정되었습니다.", HttpStatus.OK);

        }
    }

}
