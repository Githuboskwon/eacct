package com.iljin.apiServer.ijeas.sm.tax;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.system.code.CodeDetail;
import com.iljin.apiServer.ijeas.system.code.CodeDetailRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TaxServiceImpl implements TaxService {

    private final CodeDetailRepository codeDetailRepository;
    private final TaxRepository taxRepository;
    private final TaxRepositoryCustom taxRepositoryCustom;

    private final Util util;
    private static final Logger logger = LoggerFactory.getLogger(TaxServiceImpl.class);

    @Override
	public List<Tax> getByEvdTypeCd(String evdTypeCd) {
		return taxRepository.findByCompCdAndEvdTypeCd(util.getLoginCompCd(), evdTypeCd);
	}

    @Override
    public List<TaxDto> getTaxCodeList(TaxDto taxDto) {
        return taxRepositoryCustom.getTaxCodeList(taxDto);
    }

    @Override
    public ResponseEntity<String> saveTaxCodes(List<TaxDto> taxDtos) {
        User loginUser = util.getLoginUser();
        String loginId = loginUser.getLoginId();
        String compCd = loginUser.getCompCd();

        for(TaxDto taxDto : taxDtos) {
            String taxCompCd = taxDto.getCompCd();
            String evdTypeCd = taxDto.getEvdTypeCd();
            String taxCd = taxDto.getTaxCd();

            /* 증빙유형명 조회를 위해 코드로 찾기 */
            Optional<CodeDetail> codeDetail = codeDetailRepository.findByCompCdAndGroupCdAndUseYnAndDetailCd(
                    taxCompCd
                    ,"EVD_TYPE_CD"
                    ,"Y"
                    ,evdTypeCd);

            if(codeDetail.isPresent()){
                String evdTypeNm = codeDetail.get().getDetailNm();
                taxDto.setEvdTypeNm(evdTypeNm);
            }

            TaxKey taxKey = new TaxKey();
            taxKey.setCompCd(taxCompCd);
            taxKey.setTaxCd(taxCd);

            Optional<Tax> tax = taxRepository.findById(taxKey);
            if(tax.isPresent()) {
                //update
                tax.ifPresent(c -> {
                    c.setTaxNm(taxDto.getTaxNm());
                    c.setTaxRt(taxDto.getTaxRt().intValue());
                    c.setUseYn(taxDto.getUseYn());
                    c.setOrderSeq(taxDto.getOrderSeq().intValue());
                    c.setEvdTypeCd(taxDto.getEvdTypeCd());
                    c.setEvdTypeNm(taxDto.getEvdTypeNm());
                    c.setRef1(taxDto.getRef1());
                    c.setRef2(taxDto.getRef2());
                    c.setRef3(taxDto.getRef3());
                    c.setChgId(loginId);
                    c.setChgDtm(LocalDateTime.now());

                    taxRepository.save(c);
                });
            } else {
                //insert
                Tax c = new Tax();
                try {
                    PropertyUtils.copyProperties(c, taxDto);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                c.setRegId(loginId);
                c.setRegDtm(LocalDateTime.now());
                c.setChgId(loginId);
                c.setChgDtm(LocalDateTime.now());

                taxRepository.save(c);
            }
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteTaxCode(TaxDto taxDto) {
        TaxKey taxKey = new TaxKey();
        taxKey.setCompCd(taxDto.getCompCd());
        taxKey.setTaxCd(taxDto.getTaxCd());

        taxRepository.deleteById(taxKey);

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

}
