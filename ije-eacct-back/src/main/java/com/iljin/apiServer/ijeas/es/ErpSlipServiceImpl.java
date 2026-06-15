package com.iljin.apiServer.ijeas.es;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.bulk.ApPaymentsHd;
import com.iljin.apiServer.ijeas.es.bulk.ApPaymentsHdKey;
import com.iljin.apiServer.ijeas.es.bulk.ApPaymentsHdQdslRepository;
import com.iljin.apiServer.ijeas.es.bulk.ApPaymentsHdRepository;
import com.iljin.apiServer.ijeas.es.bulk.ErpBondSlipDto;
import com.iljin.apiServer.ijeas.es.bulk.ErpBulkSlipDto;
import com.iljin.apiServer.ijeas.es.collection.ErpCollectionSlipDto;
import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionDt;
import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionDtQdslRepository;
import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionDtRepository;
import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionHd;
import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionHdQdslRepository;
import com.iljin.apiServer.ijeas.es.collection.SpTrCollectionHdRepository;
import com.iljin.apiServer.ijeas.es.erpViews.ErpSlipRequestDto;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesBatch;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesBatchQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesCleared;
import com.iljin.apiServer.ijeas.es.erpViews.bond.ErpApFuturesClearedQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.bulk.ErpApPaymentsBatch;
import com.iljin.apiServer.ijeas.es.erpViews.bulk.ErpApPaymentsBatchQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrn;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrnHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.collection.ErpTrFundTrnHeadersQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.frgn.ErpSpOsSlip;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTransactionsDto;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTransactionsQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTrxHeadersDto;
import com.iljin.apiServer.ijeas.es.erpViews.fund.ErpTrTrxHeadersQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlHeaders;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlHeadersQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlLines;
import com.iljin.apiServer.ijeas.es.erpViews.gl.ErpGlLinesQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsHeader;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsHeaderQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsLines;
import com.iljin.apiServer.ijeas.es.erpViews.item.ErpApPaymentsLinesQdslRepository;
import com.iljin.apiServer.ijeas.es.erpViews.sale.ErpArTrxHeaders;
import com.iljin.apiServer.ijeas.es.fund.*;
import com.iljin.apiServer.ijeas.es.gl.ErpGlSlipDto;
import com.iljin.apiServer.ijeas.es.gl.GlDetailDto;
import com.iljin.apiServer.ijeas.es.gl.GlDetailQdslRepository;
import com.iljin.apiServer.ijeas.es.gl.GlDetailRepository;
import com.iljin.apiServer.ijeas.es.gl.GlHeader;
import com.iljin.apiServer.ijeas.es.gl.GlHeaderQdslRepository;
import com.iljin.apiServer.ijeas.es.gl.GlHeaderRepository;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsDt;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsDtDto;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsDtKey;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsDtQdslRepository;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsDtRepository;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsItem;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsItemDto;
import com.iljin.apiServer.ijeas.es.item.ApPaymentsItemRepository;
import com.iljin.apiServer.ijeas.es.item.ErpItemSlipDto;
import com.iljin.apiServer.ijeas.es.sale.*;
import com.iljin.apiServer.ijeas.slip.SlipException;
import com.iljin.apiServer.ijeas.slip.SlipHeader;
import com.iljin.apiServer.ijeas.slip.SlipHeaderKey;
import com.iljin.apiServer.ijeas.slip.SlipHeaderRepository;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodDto;
import com.iljin.apiServer.ijeas.sm.close.AcctPeriodQdslRepository;
import com.iljin.apiServer.ijeas.sm.close.ClosStatCd;
import com.iljin.apiServer.ijeas.system.emp.Employee;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ErpSlipServiceImpl implements ErpSlipService {

    private final ErpApPaymentsHeaderQdslRepository erpApPaymentsHeaderQdslRepository;

    private final ErpApPaymentsLinesQdslRepository erpApPaymentsLinesQdslRepository;

    private final ErpApPaymentsBatchQdslRepository erpApPaymentsBatchQdslRepository;

    private final ErpApFuturesBatchQdslRepository erpApFuturesBatchQdslRepository;

    private final ErpApFuturesClearedQdslRepository erpApFuturesClearedQdslRepository;

    private final ErpTrTrxHeadersQdslRepository erpTrTrxHeadersQdslRepository;

    private final ErpSalesOverseasQdslRepository erpSalesOverseasQdslRepository;

    private final ErpSalesOverseasRepository erpSalesOverseasRepository;

    private final ErpTrTransactionsQdslRepository erpTrTransactionsQdslRepository;

    private final ErpTrFundTrnHeadersQdslRepository erpTrFundTrnHeadersQdslRepository;

    private final SlipHeaderRepository slipHeaderRepository;

    private final ApPaymentsHdRepository apPaymentsHdRepository;

    private final SpTrFundHdRepository spTrFundHdRepository;

    private final SpTrFundDtRepository spTrFundDtRepository;

    private final SpTrCollectionHdRepository spTrCollectionHdRepository;

    private final SpTrCollectionDtRepository spTrCollectionDtRepository;

    private final SpTrCollectionHdQdslRepository spTrCollectionHdQdslRepository;

    private final SpTrCollectionDtQdslRepository spTrCollectionDtQdslRepository;

    private final ApPaymentsDtRepository apPaymentsDtRepository;

    private final ApPaymentsHdQdslRepository apPaymentsHdQdslRepository;

    private final ApPaymentsDtQdslRepository apPaymentsDtQdslRepository;

    private final SpTrFundHdQdslRepository spTrFundHdQdslRepository;

    private final SpTrFundDtQdslRepository spTrFundDtQdslRepository;

    private final ApPaymentsItemRepository apPaymentsItemRepository;

    private final ErpGlHeadersQdslRepository erpGlHeadersQdslRepository;

    private final GlHeaderRepository glHeaderRepository;

    private final ErpGlLinesQdslRepository erpGlLinesQdslRepository;

    private final GlDetailRepository glDetailRepository;

    private final GlHeaderQdslRepository glHeaderQdslRepository;

    private final GlDetailQdslRepository glDetailQdslRepository;

    private final AcctPeriodQdslRepository acctPeriodQdslRepository;

    private final Util util;


    /**
     * 건별지급 가져오기
     * */
    @Override
    public String pullErpItemSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpApPaymentsHeader> erpApPaymentsHeaderList = erpApPaymentsHeaderQdslRepository.pullErpApPaymentsHeader(slipTypeCd, erpSlipRequestDto);
        Employee loginEmp = util.getLoginUser().getEmployee();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpApPaymentsHeaderList.size() > 0) {
            for (ErpApPaymentsHeader erpApPaymentsHeader : erpApPaymentsHeaderList) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpApPaymentsHeader, slipNo, slipHeaderId, loginEmp);
                slipHeaderRepository.save(slipHeader);

                ApPaymentsDt apPaymentsDt = ApPaymentsDt.from(erpApPaymentsHeader, slipNo, slipHeaderId);
                apPaymentsDtRepository.save(apPaymentsDt);

                List<ErpApPaymentsLines> linesList = erpApPaymentsLinesQdslRepository.pullErpApPaymentsLinesByCheckId(erpApPaymentsHeader.getOrgId(), erpApPaymentsHeader.getCheckId());
                if(linesList.size() > 0) {
                    for (ErpApPaymentsLines erpApPaymentsLines: linesList) {
                        ApPaymentsItem apPaymentsItem = ApPaymentsItem.from(erpApPaymentsLines, slipNo, slipHeaderId);
                        apPaymentsItemRepository.save(apPaymentsItem);
                    }
                }
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * 대량지급 가져오기
     * */
    @Override
    public String pullErpBulkSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpApPaymentsBatch> erpApPaymentsBatchList = erpApPaymentsBatchQdslRepository.pullErpApPaymentsBatch(slipTypeCd, erpSlipRequestDto);
        Employee loginEmp = util.getLoginUser().getEmployee();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpApPaymentsBatchList.size() > 0) {
            for(ErpApPaymentsBatch erpApPaymentsBatch : erpApPaymentsBatchList) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpApPaymentsBatch, slipNo, slipHeaderId, loginEmp);
                slipHeaderRepository.save(slipHeader);

                ApPaymentsHd apPaymentsHd = ApPaymentsHd.from(erpApPaymentsBatch, slipNo, Integer.parseInt(slipHeaderId.toString()));
                apPaymentsHdRepository.save(apPaymentsHd);

                List<ErpApPaymentsHeader> headerList = erpApPaymentsHeaderQdslRepository.pullErpApPaymentsHeadersByeslipTransferBatchId(String.valueOf(erpApPaymentsBatch.getOrgId()), erpApPaymentsBatch.getEslipTransferBatchId());
                if(headerList.size() > 0 ){
                    for(ErpApPaymentsHeader erpApPaymentsHeader : headerList){
                        ApPaymentsDt apPaymentsDt = ApPaymentsDt.from(erpApPaymentsHeader , slipNo, slipHeaderId, slipTypeCd);
                        apPaymentsDtRepository.save(apPaymentsDt);
                    }
                }
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * 전자채권 가져오기
     * */
    @Override
    public String pullErpBondSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpApFuturesBatch> erpApFuturesBatchList = erpApFuturesBatchQdslRepository.pullErpApFuturesBatch(slipTypeCd, erpSlipRequestDto);
        Employee emp = util.getLoginUser().getEmployee();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpApFuturesBatchList.size() > 0) {
            for(ErpApFuturesBatch erpApFuturesBatch : erpApFuturesBatchList) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpApFuturesBatch, slipNo, slipHeaderId, emp);
                slipHeaderRepository.save(slipHeader);

                ApPaymentsHd apPaymentsHd = ApPaymentsHd.from(erpApFuturesBatch, slipNo, Integer.parseInt(slipHeaderId.toString()));
                apPaymentsHdRepository.save(apPaymentsHd);

                List<ErpApFuturesCleared> clearedList = erpApFuturesClearedQdslRepository.pullErpApFuturesClearedByeslipTransferBatchId(erpApFuturesBatch.getOrgId(), erpApFuturesBatch.getEslipTransferBatchId());
                if(clearedList.size() > 0 ){
                    for(ErpApFuturesCleared erpApFuturesCleared : clearedList){
                        ApPaymentsDt apPaymentsDt = ApPaymentsDt.from(erpApFuturesCleared , slipNo, slipHeaderId);
                        apPaymentsDtRepository.save(apPaymentsDt);
                    }
                }
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }


    /**
     * 자금전표 가져오기
     * */
    @Override
    public String pullErpFundSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpTrTrxHeadersDto> erpTrTrxHeaderlist = erpTrTrxHeadersQdslRepository.pullErpTrTransactions(slipTypeCd, erpSlipRequestDto);
        Employee emp = util.getLoginUser().getEmployee();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpTrTrxHeaderlist.size() > 0) {
            for(ErpTrTrxHeadersDto erpTrTrxHeaders : erpTrTrxHeaderlist) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpTrTrxHeaders, slipNo, slipHeaderId, emp);
                slipHeaderRepository.save(slipHeader);

                SpTrFundHd spTrFundHd = SpTrFundHd.from(erpTrTrxHeaders , slipNo , slipHeaderId);
                spTrFundHdRepository.save(spTrFundHd);

                List<ErpTrTransactionsDto> transactionsList = erpTrTransactionsQdslRepository.getErpTrTransactionsList(erpTrTrxHeaders);
                if(transactionsList.size() > 0){
                    for(ErpTrTransactionsDto erpTrTransactions : transactionsList){
                        SpTrFundDt spTrFundDt = SpTrFundDt.from(erpTrTransactions, slipNo, slipHeaderId , spTrFundDtRepository.getSlipSeq());
                        spTrFundDtRepository.save(spTrFundDt);
                    }
                }
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * 집금전표 가져오기
     * */
    @Override
    public String pullErpClctSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpTrFundTrnHeaders> erpTrFundTrnHeadersList = erpTrFundTrnHeadersQdslRepository.pullErpTrFundTrnHeaders(slipTypeCd, erpSlipRequestDto);
        Employee emp = util.getLoginUser().getEmployee();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpTrFundTrnHeadersList.size() > 0) {
            for(ErpTrFundTrnHeaders erpTrTrxHeaders : erpTrFundTrnHeadersList) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpTrTrxHeaders, slipNo, slipHeaderId, emp);
                slipHeaderRepository.save(slipHeader);

                SpTrCollectionHd spTrCollectionHd = SpTrCollectionHd.from(erpTrTrxHeaders, slipNo, slipHeaderId);
                spTrCollectionHdRepository.save(spTrCollectionHd);

                List<ErpTrFundTrn> spTrCollectionDtList = spTrCollectionDtQdslRepository.pullErpSpTrCollectionDtList(erpTrTrxHeaders);

                if(spTrCollectionDtList.size() > 0){
                    for(ErpTrFundTrn erpTrFundTrn : spTrCollectionDtList){
                        SpTrCollectionDt spTrCollectionDt = SpTrCollectionDt.from(erpTrFundTrn, slipNo, slipHeaderId , spTrCollectionDtRepository.getSlipSeq());
                        spTrCollectionDtRepository.save(spTrCollectionDt);
                    }
                }
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * GL전표 가져오기
     * */
    @Override
    public String pullErpGlSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpGlHeaders> erpGlHeadersList = erpGlHeadersQdslRepository.pullErpGlHeaders(slipTypeCd, erpSlipRequestDto);
        Employee loginEmp = util.getLoginUser().getEmployee();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        String returnMsg = "가져올 전표가 없습니다.";

        if(erpGlHeadersList.size() > 0) {
            for(ErpGlHeaders erpGlHeaders: erpGlHeadersList) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpGlHeaders, slipNo, slipHeaderId, loginEmp);

                slipHeaderRepository.save(slipHeader);

                GlHeader glHeader = GlHeader.from(erpGlHeaders, slipNo, slipHeaderId);
                glHeaderRepository.save(glHeader);

//                List<ErpGlLines> erpGlLinesList = erpGlLinesQdslRepository.pullErpGlLinesByJeHeaderId(erpGlHeaders.getJeHeaderId());
//                if(erpGlLinesList.size() > 0) {
//                    int idx = 1;
//                    for(ErpGlLines erpGlLines: erpGlLinesList) {
//                        GlDetail glDetail = GlDetail.from(erpGlLines, slipNo, compCd, idx++);
//                        glDetailRepository.save(glDetail);
//                    }
//                }
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * 매출전표 가져오기
     * */
    @Override
    public String pullErpSalesSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpArTrxHeaders> erpArTrxHeaders = erpSalesOverseasQdslRepository.pullErpArTrxHeaders(slipTypeCd, erpSlipRequestDto);
        Employee loginEmp = util.getLoginUser().getEmployee();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpArTrxHeaders.size() > 0) {
            for(ErpArTrxHeaders erpArTrxHeader : erpArTrxHeaders) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpArTrxHeader, slipNo, slipHeaderId, loginEmp , compCd);
                slipHeaderRepository.save(slipHeader);

                ErpSalesOverseas erpSalesOverseas = ErpSalesOverseas.from(erpArTrxHeader , slipNo , slipHeaderId, compCd);
                erpSalesOverseasRepository.save(erpSalesOverseas);
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * 해외전표 가져오기
     * */
    @Override
    public String pullErpForeignSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpSpOsSlip> erpSpOsSlips = erpSalesOverseasQdslRepository.pullErpSpOsSlip(slipTypeCd, erpSlipRequestDto);
        Employee loginEmp = util.getLoginUser().getEmployee();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        String returnMsg = "가져올 전표가 없습니다";

        if (erpSpOsSlips.size() > 0) {
            for(ErpSpOsSlip erpSpOsSlip : erpSpOsSlips) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpSpOsSlip, slipNo, slipHeaderId, loginEmp , compCd);
                slipHeaderRepository.save(slipHeader);

                ErpSalesOverseas erpSalesOverseas = ErpSalesOverseas.from(erpSpOsSlip , slipNo , slipHeaderId, compCd);
                erpSalesOverseasRepository.save(erpSalesOverseas);
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    /**
     * 수출전표 가져오기
     * */
    @Override
    public String pullErpExportSlipListFromErp(String slipTypeCd, ErpSlipRequestDto erpSlipRequestDto) {
        Employee loginEmp = util.getLoginUser().getEmployee();

        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();

        String returnMsg = "가져올 전표가 없습니다";

//        String yearMonth = erpSlipRequestDto.getSearchMonth().replaceAll("-", "");

        // 회계기간 먼저 체크
        // checkAcctPeriod(yearMonth);

        // 선택된 달의 데이터 먼저 삭제
//        erpSalesOverseasRepository.deleteByMonth(compCd,erpSlipRequestDto.getSearchMonth(),erpSlipRequestDto.getDiv());
//        slipHeaderRepository.deleteByMonth(compCd,yearMonth,erpSlipRequestDto.getDiv());


        List<ErpArTrxHeaders> erpArTrxHeaders = erpSalesOverseasQdslRepository.pullErpArTrxHeaders_export(slipTypeCd, erpSlipRequestDto);


        if (erpArTrxHeaders.size() > 0) {
            for(ErpArTrxHeaders erpArTrxHeader : erpArTrxHeaders) {
                String slipNo = apPaymentsDtRepository.getErpSlipNo(util.getLoginCompCd());
                BigDecimal slipHeaderId = slipHeaderRepository.getSlipHeaderId();

                SlipHeader slipHeader = SlipHeader.from(erpArTrxHeader, slipNo, slipHeaderId, loginEmp , compCd, "");
                slipHeaderRepository.save(slipHeader);

                ErpSalesOverseas erpSalesOverseas = ErpSalesOverseas.from(erpArTrxHeader , slipNo , slipHeaderId, compCd, "");
                erpSalesOverseasRepository.save(erpSalesOverseas);
            }
            returnMsg = "가져오기가 완료되었습니다.";
        }
        return returnMsg;
    }

    @Override
    public List<ErpItemSlipDto> getErpItemSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return apPaymentsDtQdslRepository.getErpItemSlipList(erpSlipRequestDto);
    }

    @Override
    public List<ErpGlSlipDto> getErpGlSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return glHeaderQdslRepository.getErpGlSlipList(erpSlipRequestDto);
    }

    @Override
    public List<ErpBulkSlipDto> getErpBulkSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return apPaymentsHdQdslRepository.getErpBulkSlipList(erpSlipRequestDto);
    }

    @Override
    public List<ErpBondSlipDto> getErpBondSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return apPaymentsHdQdslRepository.getErpBondSlipList(erpSlipRequestDto);
    }

    @Override
    public List<ErpFundSlipDto> getErpFundSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return erpTrTrxHeadersQdslRepository.getErpFundSlipList(erpSlipRequestDto);
    }

    @Override
    public List<ErpCollectionSlipDto> getErpCollectionSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return spTrCollectionHdQdslRepository.getErpCollectionSlipList(erpSlipRequestDto);
    }

    @Override
    public List<ErpSalesOverseasDto> getErpSalesSlipList(ErpSlipRequestDto erpSlipRequestDto) {
        return erpSalesOverseasQdslRepository.getErpSalesSlipList(erpSlipRequestDto);
    }

    @Override
    public ErpItemSlipDto getErpItemSlipDetail(ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpItemSlipDto> itemSlipDtos = apPaymentsDtQdslRepository.getErpItemSlipDetail(
            erpSlipRequestDto);
        if (itemSlipDtos.isEmpty()) {
            throw new RuntimeException("전표가 존재하지 않습니다.");
        }
        ErpItemSlipDto result = itemSlipDtos.get(0);

        List<ApPaymentsItem> itemList = apPaymentsItemRepository.findByCompCdAndCheckId(result.getCompCd(), result.getCheckId());
        List<ApPaymentsItemDto> dtoList = itemList.stream()
            .map(ApPaymentsItemDto::from).collect(Collectors.toList());
        result.setItemList(dtoList);
        return result;
    }

    @Override
    public ErpBulkSlipDto getErpBulkSlipDetail(ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpBulkSlipDto> bulkSlipDtos = apPaymentsHdQdslRepository.getErpBulkSlipDetail(
                erpSlipRequestDto);
        if (bulkSlipDtos.isEmpty()) {
            throw new RuntimeException("전표가 존재하지 않습니다.");
        }
        ErpBulkSlipDto result = bulkSlipDtos.get(0);

        List<ApPaymentsDtDto> bulkList = apPaymentsDtQdslRepository.pullErpApPaymentsDtList(result.getCompCd(), result.getEslipTransferBatchId());
        result.setDtList(bulkList);
        return result;
    }

    @Override
    public ErpFundSlipDto getErpFundSlipDetail(ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpFundSlipDto> fundSlipDtos = spTrFundHdQdslRepository.getErpFundSlipDetail(erpSlipRequestDto);
        if (fundSlipDtos.isEmpty()) {
            throw new RuntimeException("전표가 존재하지 않습니다.");
        }
        ErpFundSlipDto result = fundSlipDtos.get(0);

        List<SpTrFundDt> fundList = spTrFundDtQdslRepository.pullErpSpTrFundDtList(result.getCompCd(), result.getSlipNo());
        result.setDtList(fundList);
        return result;
    }

    @Override
    public ErpCollectionSlipDto getErpCollectionSlipDetail(ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpCollectionSlipDto> collectionSlipDtos = spTrCollectionHdQdslRepository.getErpCollectionSlip(erpSlipRequestDto);
        if (collectionSlipDtos.isEmpty()) {
            throw new RuntimeException("전표가 존재하지 않습니다.");
        }
        ErpCollectionSlipDto result = collectionSlipDtos.get(0);

        List<SpTrCollectionDt> fundList = spTrCollectionDtQdslRepository.erpSpTrCollectionDtList(result.getCompCd(), result.getSlipNo());
        result.setDtList(fundList);
        return result;
    }

    @Override
    public ErpGlSlipDto getErpGlSlipDetail(ErpSlipRequestDto erpSlipRequestDto) {
        List<ErpGlSlipDto> glSlipDtos = glHeaderQdslRepository.getErpGlSlipDetail(erpSlipRequestDto);
        if (glSlipDtos.isEmpty()) {
            throw new RuntimeException("전표가 존재하지 않습니다.");
        }
        ErpGlSlipDto result = glSlipDtos.get(0);

        List<ErpGlLines> detailList = erpGlLinesQdslRepository.getErpGlLinesByJeHeaderId(result.getJeHeaderId());
        List<GlDetailDto> dtoList = detailList.stream()
            .map(GlDetailDto::from).collect(Collectors.toList());
        result.changeDetailList(dtoList);
        return result;
    }

    @Override
    public String deleteErpSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());
            ApPaymentsDtKey apPaymentsDtKey = ApPaymentsDtKey.builder()
                .compCd(compCdLoginUser)
                .slipNo(dto.getSlipNo())
                .slipType(dto.getSlipType())
                .checkId(dto.getCheckId())
                .build();

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }
            ApPaymentsDt apPaymentsDt = apPaymentsDtRepository.findById(apPaymentsDtKey)
                .orElseThrow(() -> new RuntimeException("삭제할 전표가 존재하지 않습니다."));
            BigDecimal eslipTransferId = apPaymentsDt.getEslipTransferId();

            apPaymentsDtRepository.cancelPullErpItemSlip(eslipTransferId, "P");

            apPaymentsItemRepository.deleteByCompCdAndCheckId(compCdLoginUser, dto.getCheckId());
            apPaymentsDtRepository.deleteById(apPaymentsDtKey);
            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpBulkSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) { // 대량지급/전자채권 삭제
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }


            String eslipTransferBatchId = dto.getEslipTransferBatchId();
            apPaymentsDtRepository.cancelPullErpBulkSlip(eslipTransferBatchId);
            apPaymentsDtRepository.cancelPullErpItemSlipByEslipTransterBatchId(eslipTransferBatchId, "S");

            ApPaymentsHdKey apPaymentsHdKey = ApPaymentsHdKey.builder()
                    .compCd(compCdLoginUser)
                    .slipNo(dto.getSlipNo())
                    .slipType(dto.getSlipType())
                    .eslipTransferBatchId(eslipTransferBatchId)
                    .build();

            List<ApPaymentsDtDto> apPaymentsDtDtos = apPaymentsDtRepository.findByCompCdAndEslipTransferBatchId(compCdLoginUser , eslipTransferBatchId);
            for(ApPaymentsDtDto dtos : apPaymentsDtDtos){
                ApPaymentsDtKey apPaymentsDtKey = ApPaymentsDtKey.builder()
                        .compCd(compCdLoginUser)
                        .slipNo(dtos.getSlipNo())
                        .slipType(dtos.getSlipType())
                        .checkId(dtos.getCheckId())
                        .build();

                ApPaymentsDt apPaymentsDt = apPaymentsDtRepository.findById(apPaymentsDtKey)
                        .orElseThrow(() -> new RuntimeException("삭제할 전표가 존재하지 않습니다."));
                apPaymentsDtRepository.deleteById(apPaymentsDtKey);
            }
            apPaymentsHdRepository.deleteById(apPaymentsHdKey);
            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpGlSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String loginCompCd = util.getLoginCompCd();
        for(ErpSlipRequestDto dto : erpSlipRequestDtos) {
            String slipNo = dto.getSlipNo();
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(loginCompCd, slipNo);
            if(!slipHeaderRepository.existsById(slipHeaderKey) || !glHeaderRepository.existsByCompCdAndSlipNo(loginCompCd, slipNo)) {
                throw new RuntimeException("삭제할 전표가 없습니다.");
            }

//            glDetailRepository.deleteByCompCdAndSlipNo(loginCompCd, slipNo);
            glHeaderRepository.deleteByCompCdAndSlipNo(loginCompCd, slipNo);
            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpBondSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());
            String slipNo = dto.getSlipNo();

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }

            apPaymentsDtRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);
            apPaymentsDtRepository.flush();

            apPaymentsHdRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);

            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpFundSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());
            String slipNo = dto.getSlipNo();

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }

            spTrFundDtRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);
            spTrFundDtRepository.flush();

            spTrFundHdRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);

            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpClctSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());
            String slipNo = dto.getSlipNo();

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }

            spTrCollectionDtRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);
            spTrCollectionDtRepository.flush();

            spTrCollectionHdRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);

            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpSaleAndFrgnSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());
            String slipNo = dto.getSlipNo();

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }

            erpSalesOverseasRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);

            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public String deleteErpExportSlip(List<ErpSlipRequestDto> erpSlipRequestDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());
            String slipNo = dto.getSlipNo();

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("삭제할 전표가 존재하지 않습니다.");
            }

            erpSalesOverseasRepository.deleteByCompCdAndSlipNo(compCdLoginUser,slipNo);

            slipHeaderRepository.deleteById(slipHeaderKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public ResponseEntity<String> saveExportSlip (List<ErpSlipRequestDto> erpSlipRequestDtos){
        String compCdLoginUser = util.getLoginCompCd();
        for (ErpSlipRequestDto dto : erpSlipRequestDtos) {
            String slipNo = dto.getSlipNo();
            String remark = dto.getRemark();
            String certAmendYn = dto.getCertAmendYn();
            SlipHeaderKey slipHeaderKey = new SlipHeaderKey(compCdLoginUser, dto.getSlipNo());

            if (!slipHeaderRepository.existsById(slipHeaderKey)) {
                throw new RuntimeException("저장할 전표가 존재하지 않습니다.");
            }

            erpSalesOverseasRepository.updateRemark(remark,certAmendYn,compCdLoginUser,slipNo);
        }

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    public void checkAcctPeriod(String postingDt){
        List<AcctPeriodDto> acctPeriodDtos = acctPeriodQdslRepository.getCheckAcctPeriodList(util.getLoginCompCd(), postingDt);
        if(acctPeriodDtos.size() == 0) {
            throw new ErpSlipException("회계일자에 대한 마감등록이 없습니다.");
        }
        if(acctPeriodDtos.size() > 1) {
            throw new ErpSlipException("회계일자에 대한 마감등록이 잘못됐습니다.");
        }
        if (acctPeriodDtos.get(0).getClosStatCd().equals(ClosStatCd.CLOSE.getCode())) {
            throw new ErpSlipException("전자전표의 회계일자가 CLOSE되었습니다.");
        }
    }

}
