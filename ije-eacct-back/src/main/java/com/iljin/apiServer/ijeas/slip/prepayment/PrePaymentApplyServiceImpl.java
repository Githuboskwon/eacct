package com.iljin.apiServer.ijeas.slip.prepayment;


import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class PrePaymentApplyServiceImpl implements PrePaymentApplyService {

    private final PrepaymentApplyRepository prepaymentApplyRepository;

    private final PrePaymentApplyQdslRepository prePaymentApplyQdslRepository;
    
    private final Util util;

    @Override
    public List<ApplyPrepaysDto> getAdvancedList(ErpSlipRequestDto erpSlipRequestDto) {
        return prePaymentApplyQdslRepository.getAdvancedList(erpSlipRequestDto);
    }

    @Override
    public List<PrepaymentApplyDto> getReimbursementList(ErpSlipRequestDto erpSlipRequestDto) {
        return prePaymentApplyQdslRepository.getReimbursementList(erpSlipRequestDto);
    }

    @Override
    public ResponseEntity<String> saveAdvanced(@RequestBody List<PrepaymentApplyDto> prepaymentApplyDtos){
//        try {
            for(PrepaymentApplyDto prepaymentApplyDto : prepaymentApplyDtos){

                BigDecimal prepaymentApplyId = prepaymentApplyRepository.getPrepaymentApplyId();

                PrepaymentApply prepaymentApply = PrepaymentApply.builder()
                    .prepaymentApplyId(prepaymentApplyId)
                    .slipHeaderId(prepaymentApplyDto.getSlipHeaderId())
                    .orgId(prepaymentApplyDto.getOrgId())
                    .ledgerId(prepaymentApplyDto.getLedgerId())
                    .prepayInvoiceId(prepaymentApplyDto.getPrepayInvoiceId())
                    .prepayLineNumber(prepaymentApplyDto.getPrepayLineNumber())
                    .prepayAmountRemaining(prepaymentApplyDto.getPrepayAmountRemaining())
                    .prepayAmount(prepaymentApplyDto.getPrepayAmount())
                    .amountToApply(prepaymentApplyDto.getAmountToApply())
                    .applyGlDate(prepaymentApplyDto.getApplyGlDate())
                    .vendorId(prepaymentApplyDto.getVendorId())
                    .vendorSiteId(prepaymentApplyDto.getVendorSiteId())
                    .invoiceCurrencyCode(prepaymentApplyDto.getInvoiceCurrencyCode())
                    .prepayCancelledFlag("N")
                    .applyProcessFlag("N")
                    .createdPersonId(prepaymentApplyDto.getCreatedPersonId())
                    .creationDate(LocalDateTime.now())
                    .lastUpdatedPersonId(prepaymentApplyDto.getLastUpdatedPersonId())
                    .lastUpdateDate(LocalDateTime.now())
                    .build();
                prepaymentApplyRepository.save(prepaymentApply);
            }

//        }catch(Exception exception){
//            throw new RuntimeException("반제 처리가 실패 하였습니다.");
//        }

        return new ResponseEntity<>("반제 처리가 성공 하였습니다.", HttpStatus.OK);
    }


    @Override
    public ResponseEntity<String> deleteAdvanced(@RequestBody List<PrepaymentApplyDto> prepaymentApplyDtos) {
        try {
            Integer personId = util.getLoginUser().getEmployee().getPersonId();

            for (PrepaymentApplyDto prepaymentApplyDto : prepaymentApplyDtos) {

                Optional<PrepaymentApply> prepaymentApply =
                        prepaymentApplyRepository.findByPrepaymentApplyIdAndSlipHeaderIdAndOrgIdAndLedgerIdAndPrepayInvoiceIdAndPrepayLineNumber
                                (prepaymentApplyDto.getPrepaymentApplyId(), prepaymentApplyDto.getSlipHeaderId(), prepaymentApplyDto.getOrgId(),
                                        prepaymentApplyDto.getLedgerId(), prepaymentApplyDto.getPrepayInvoiceId(), prepaymentApplyDto.getPrepayLineNumber());

                if (prepaymentApply.isPresent()) {
                    prepaymentApply.ifPresent(c -> {
                        c.updateFlag(prepaymentApply.get().getAmountToApply(), personId);
                    });
                }

            }
        }catch(Exception exception){
            throw new RuntimeException("미반제 처리가 실패 하였습니다.");
        }

        return new ResponseEntity<>("미반제 처리가 성공 하였습니다.", HttpStatus.OK);
    }

}
