package com.iljin.apiServer.ijeas.slip.etax;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SalesTaxInvoiceRepository {
    List<SalesTaxInvoiceDto> getSalesTaxInvoiceList(SalesTaxInvoiceDto salesTaxInvoiceDto);

    List<SalesTaxInvoiceDto> getLine(Integer trxId);

    List<SalesTaxInvoiceDto> getSalesTaxInvoiceIssueList(SalesTaxInvoiceDto salesTaxInvoiceDto);

    List<SalesTaxInvoiceDto> getItem(Integer etaxIssueId);

    List<SalesTaxInvoiceDto> getSlipNo(Integer etaxIssueId);

    List<SalesTaxInvoiceDto> getModifyInfo(BigDecimal compCd, String oriIssueId);

    List<SalesTaxInvoiceDto> getModifyList(SalesTaxInvoiceDto salesTaxInvoiceDto);
}
