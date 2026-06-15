package com.iljin.apiServer.ijeas.slip.etax;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface SalesTaxInvoiceService {
    List<SalesTaxInvoiceDto> getSalesTaxInvoiceList(SalesTaxInvoiceDto salesTaxInvoiceDto);

    List<SalesTaxInvoiceDto> getLine(Integer trxId);

    @Transactional
    Map<String, String> accounting(Integer customerTrxId);

    @Transactional
    Map<String, String> cancelInvoice(List<SalesTaxInvoiceDto> list);

    @Transactional
    Map<String, String> confirmSum(List<SalesTaxInvoiceDto> list);

    @Transactional
    Map<String, String> confirmOne(List<SalesTaxInvoiceDto> list);

    @Transactional
    Map<String, String> confirmNot(List<SalesTaxInvoiceDto> list);

    @Transactional
    Map<String, String> modifyAmount(SalesTaxInvoiceDto salesTaxInvoiceDto);

    List<SalesTaxInvoiceDto> getSalesTaxInvoiceIssueList(SalesTaxInvoiceDto salesTaxInvoiceDto);

    List<SalesTaxInvoiceDto> getItem(Integer etaxIssueId);

    List<SalesTaxInvoiceDto> getSlipNo(Integer etaxIssueId);


    Map<String, String> issueTaxInvoice(List<SalesTaxInvoiceDto> list);

    Map<String, String> reIssueTaxInvoice(List<SalesTaxInvoiceDto> list);

    Map<String, String> cancelIssueTaxInvoice(List<SalesTaxInvoiceDto> list);

    Map<String, String> emailReSend(List<SalesTaxInvoiceDto> list);

    @Transactional
    Map<String, String> reverseSave(List<SalesTaxInvoiceDto> list);

    @Transactional
    Map<String, String> reverseCancel(List<SalesTaxInvoiceDto> list);

    List<SalesTaxInvoiceDto> getModifyList(SalesTaxInvoiceDto salesTaxInvoiceDto);

    Map<String, String> etaxModify(List<SalesTaxInvoiceDto> list) throws Exception;

    Map<String, String> preview(SalesTaxInvoiceDto salesTaxInvoiceDto);

    @Transactional
    Map<String, String> modifyItem(SalesTaxInvoiceDto salesTaxInvoiceDto);
}
