package com.iljin.apiServer.ijeas.system.oil;

import com.iljin.apiServer.core.util.Util;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class OilPriceServiceImpl implements OilPriceService {

    private final Util util;
    private final OilPriceRepository oilPriceRepository;
    private final OilPriceRepositoryCustom oilPriceRepositoryCustom;

    private final OilPriceQdslRepository oilPriceQdslRepository;

    @Override
    public List<OilPriceDto> getOilPriceList(OilPriceDto oilPriceDto) {
        return oilPriceQdslRepository.getOilPriceList(oilPriceDto);
    }

    @Override
    public ResponseEntity<String> saveOilPrice(List<OilPriceDto> oilPrices) {
        String compCd = util.getLoginCompCd();

        for(OilPriceDto oilPriceDto : oilPrices) {
            String baseYm = oilPriceDto.getBaseYm();
            if(!baseYm.isEmpty()) {
                baseYm = baseYm.replaceAll("-", "");
            }

            OilPriceKey oilPriceKey = new OilPriceKey();
            oilPriceKey.setCompCd(compCd);
            oilPriceKey.setBaseYm(baseYm);
            oilPriceKey.setOilKindCd(oilPriceDto.getOilKindCd());

            Optional<OilPrice> result = oilPriceRepository.findById(oilPriceKey);
            if(result.isPresent()) {
                //update
                result.ifPresent(c -> {
                    c.update(oilPriceDto);
                });
            } else {
                //insert
                Optional<OilPrice> dupl = oilPriceRepository.findByCompCdAndBaseYmAndOilKindCd(compCd, baseYm, oilPriceDto.getOilKindCd());

                if(dupl.isPresent()) {
                    throw new RuntimeException("기준연월(" + oilPriceDto.getBaseYm() + ")에 유종(" + oilPriceDto.getOilKindCd() + ") 가 있어 신규 추가할 수 없습니다.");
                } else {
                    OilPrice oilPrice = OilPrice.builder()
                                    .compCd(compCd)
                                    .baseYm(baseYm)
                                    .oilKindCd(oilPriceDto.getOilKindCd())
                                    .oilUpce(oilPriceDto.getOilUpce())
                                    .oilEff(oilPriceDto.getOilEff())
                                    .remark(oilPriceDto.getRemark())
                                    .build();
                    oilPriceRepository.save(oilPrice);
                }
            }
        }
        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteOilPrice(OilPriceDto oilPriceDto) {
        String compCd = oilPriceDto.getCompCd();
        String baseYm = oilPriceDto.getBaseYm();
        String oilKindCd = oilPriceDto.getOilKindCd();
        if(!baseYm.isEmpty()) {
            baseYm = baseYm.replaceAll("-", "");
        }

        OilPriceKey oilPriceKey = new OilPriceKey();
        oilPriceKey.setCompCd(compCd);
        oilPriceKey.setBaseYm(baseYm);
        oilPriceKey.setOilKindCd(oilKindCd);

        oilPriceRepository.deleteById(oilPriceKey);

        return new ResponseEntity<>("삭제되었습니다.", HttpStatus.OK);
    }

    @Override
    public OilPriceDto getOilPriceSlip(OilPriceDto oilPriceDto) {
         List<OilPriceDto> oilPriceDtos = oilPriceQdslRepository.getOilPriceSlip(oilPriceDto);
         if(oilPriceDtos.size() == 0) {
             throw new RuntimeException("유류비가 등록되어 있지 않습니다.");
         }
         return oilPriceDtos.get(0);
    }
}
