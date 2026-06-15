package com.iljin.apiServer.ijeas.slip.tax;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TaxDtiNtsMainQdslRepositoryTest {

    @Autowired
    TaxDtiNtsMainQdslRepository taxDtiNtsMainQdslRepository;

    @Test
    @DisplayName("세금계산서리스트 조회")
    public void getTaxDtiNtsMainList() {

        LocalDateTime fromDate = getLocalDateTime(20230701L);
        LocalDateTime toDate = getLocalDateTime(20230702L);

        SearchTaxDtiNtsDto searchTaxDtiNtsDto = new SearchTaxDtiNtsDto();
        searchTaxDtiNtsDto.setSearchFrom(fromDate);
        searchTaxDtiNtsDto.setSearchTo(toDate);
        searchTaxDtiNtsDto.setSearchSlipHeaderId(BigDecimal.valueOf(255973));
        searchTaxDtiNtsDto.setSearchTaxEvidenceType("110");
        searchTaxDtiNtsDto.setSearchTaxSmartbillNo("202307191023071932320014");
        searchTaxDtiNtsDto.setSearchIpPersname("김도형");
        searchTaxDtiNtsDto.setSearchSuId("135-86-29442");

        List<TaxDtiNtsMainDto> taxDtiNtsMainList = taxDtiNtsMainQdslRepository.getTaxDtiNtsMainList(searchTaxDtiNtsDto);

        assertNotNull(taxDtiNtsMainList);
    }

    @NotNull
    private static LocalDateTime getLocalDateTime(long fromDate) {
        int year = (int) (fromDate / 10000);
        int month = (int) (fromDate % 10000 / 100);
        int day = (int) (fromDate % 100);

        // LocalDateTime으로 변환합니다. 시간 정보가 없으므로 기본적으로 00:00:00으로 설정됩니다.
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, 0, 0);
        return dateTime;
    }

}