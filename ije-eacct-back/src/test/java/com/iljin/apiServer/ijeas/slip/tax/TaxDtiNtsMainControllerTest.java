package com.iljin.apiServer.ijeas.slip.tax;

import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
public class TaxDtiNtsMainControllerTest {

    private MockMvc mockMvc;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .alwaysDo(document("TaxDtiNtsMainController/{method-name}/{step}")).build();
    }

    @Test
    public void getTaxDtiNtsMainList() throws Exception {

        // 입력으로 받은 문자열
        String fd = "2023-07-01 00:00:00";
        String td = "2023-07-02 00:00:00";

        // 입력 문자열을 LocalDateTime으로 파싱합니다.
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime fromDT = LocalDateTime.parse(fd, inputFormatter);
        LocalDateTime toDT = LocalDateTime.parse(td, inputFormatter);


        SearchTaxDtiNtsDto searchTaxDtiNtsDto = new SearchTaxDtiNtsDto();
        searchTaxDtiNtsDto.setSearchFrom(fromDT);
        searchTaxDtiNtsDto.setSearchTo(toDT);
        searchTaxDtiNtsDto.setSearchSlipHeaderId(BigDecimal.valueOf(255973));
        searchTaxDtiNtsDto.setSearchTaxEvidenceType("110");
        searchTaxDtiNtsDto.setSearchTaxSmartbillNo("202307191023071932320014");
        searchTaxDtiNtsDto.setSearchSuId("135-86-29442");


        Gson gson = new Gson();
        String json = gson.toJson(searchTaxDtiNtsDto);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/tax/dtiNtsMain/list")
                .header("Host", "localhost:8081")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
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