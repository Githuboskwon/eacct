package com.iljin.apiServer.ijeas.system.confirm;



import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ConfirmServiceImpl implements ConfirmService {

    private final Util util;

    private final ConfirmQdslRepository confirmQdslRepository;

    private final ConfirmRepository confirmRepository;


    @Override
    public List<ConfirmDto> getConfirmList(ConfirmDto confirmDto) {
        return confirmQdslRepository.getConfirmList(confirmDto);
    }

    @Override
    public ResponseEntity<String> deleteConfirmSeq(@RequestBody ConfirmDto confirmDto){
        String compCd = util.getLoginCompCd();

        ConfirmKey confirmKey = new ConfirmKey();
        confirmKey.setCompCd(compCd);
        confirmKey.setDeptCd(confirmDto.getDeptCd());
        confirmKey.setConfirmUserId(confirmDto.getConfirmUserId());
        confirmKey.setConfirmSeq(confirmDto.getConfirmSeq());

        confirmRepository.deleteById(confirmKey);

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> saveConfirmSeq(ConfirmDto confirmDto) {
        String compCd = util.getLoginCompCd();

        String prevSeq = String.valueOf((Integer.parseInt(confirmDto.getConfirmSeq()) - 1));

        // 중복된 된 검인 순서가 있는지?
        Integer count = confirmRepository.countByConfirmSeqAndDeptCd(confirmDto.getConfirmSeq(),confirmDto.getDeptCd());

        // 이전 검인 순서의 종료 금액 보다 높은지
        Integer cnt = confirmRepository.countByConfirmSeqAndDeptCdAndConfirmStartAmtUnderAmt(prevSeq,confirmDto.getDeptCd(),confirmDto.getConfirmStartAmt());

        if (count > 0){
            throw new RuntimeException("해당 부서의 검인 순서가 이미 존재합니다.");
        }

//        if (cnt > 0 && Integer.parseInt(prevSeq) > 0){
//            throw new RuntimeException("이전 검인 기준 종료금액이 현재 검인 기준 시작 금액보다 큽니다.");
//        }

        Confirm newConfirm = Confirm.builder()
                .compCd(compCd)
                .deptCd(confirmDto.getDeptCd())
                .confirmUserId(confirmDto.getConfirmUserId())
                .confirmSeq(confirmDto.getConfirmSeq())
                .confirmStartAmt(confirmDto.getConfirmStartAmt())
                .confirmEndAmt(confirmDto.getConfirmEndAmt())
                .remark(confirmDto.getRemark())
                .build();
        confirmRepository.save(newConfirm);

        return new ResponseEntity<>("생성 되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateConfirmSeq(ConfirmDto confirmDto) {
        String compCd = util.getLoginCompCd();

        String prevSeq = String.valueOf((Integer.parseInt(confirmDto.getConfirmSeq()) - 1));

        // 이전 검인 순서의 종료 금액 보다 높은지
        Integer cnt = confirmRepository.countByConfirmSeqAndDeptCdAndConfirmStartAmtUnderAmt(prevSeq,confirmDto.getDeptCd(),confirmDto.getConfirmStartAmt());

//        if (cnt > 0 && Integer.parseInt(prevSeq) > 0){
//            throw new RuntimeException("이전 검인 기준 종료금액이 현재 검인 기준 시작 금액보다 큽니다.");
//        }

        ConfirmKey confirmKey = new ConfirmKey();
        confirmKey.setCompCd(compCd);
        confirmKey.setDeptCd(confirmDto.getDeptCd());
        confirmKey.setConfirmUserId(confirmDto.getConfirmUserId());
        confirmKey.setConfirmSeq(confirmDto.getConfirmSeq());

        Optional<Confirm> confirm = confirmRepository.findById(confirmKey);

        if(confirm.isPresent()){
            confirm.ifPresent(c -> {
               c.update(confirmDto);
            });
        }

        return new ResponseEntity<>("수정 되었습니다.", HttpStatus.OK);
    }


}
