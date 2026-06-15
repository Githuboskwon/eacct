package com.iljin.apiServer.ijeas.system.vendor;


import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface VendorService {
    public List<VendorDto> getVendorList(VendorDto vendorDto, Pageable pageable);

    public Long getVendorCount(VendorDto vendorDto);

    public List<VendorDto> getBankList(VendorDto vendorDto);

    public List<VendorDto> getVendorListSearch(VendorDto vendorDto, Pageable pageable);

    public Map<String, String> callErpVendorUpdate();

    List<VendorDto> getTermsList(VendorDto vendorDto);
}
