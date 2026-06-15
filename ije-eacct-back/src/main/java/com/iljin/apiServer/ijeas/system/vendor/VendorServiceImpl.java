package com.iljin.apiServer.ijeas.system.vendor;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class VendorServiceImpl implements VendorService{
    private final VendorRepository vendorRepository;
    private final VendorQdslRepository vendorQdslRepository;
    private final VendorRepositoryCustom vendorRepositoryCustom;

    @Override
    public List<VendorDto> getVendorList(VendorDto vendorDto, Pageable pageable){
        List<VendorDto> list = vendorQdslRepository.getVendorList(vendorDto, pageable);

        for(int i=0; i<list.size(); i++){
            if(list.get(i).vendorId != null && list.get(i).vendorSiteId != null)
            list.get(i).prepayCnt = vendorRepositoryCustom.getPrepayCount(list.get(i).vendorId, list.get(i).vendorSiteId, vendorDto.getCurrencyCode());
        }

        return list;
    }

    @Override
    public Long getVendorCount(VendorDto vendorDto){
        return vendorQdslRepository.getVendorCount(vendorDto);
    }

    @Override
    public List<VendorDto> getBankList(VendorDto vendorDto){
        return vendorQdslRepository.getBankList(vendorDto);
    }

    @Override
    public List<VendorDto> getVendorListSearch(VendorDto vendorDto, Pageable pageable){
        return vendorQdslRepository.getVendorListSearch(vendorDto, pageable);
    }

    @Override
    public Map<String, String> callErpVendorUpdate(){
        return vendorQdslRepository.callErpVendorUpdate();
    }

    @Override
    public List<VendorDto> getTermsList(VendorDto vendorDto){
        return vendorQdslRepository.getTermsList(vendorDto);
    }
}
