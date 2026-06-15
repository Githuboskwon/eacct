package com.iljin.apiServer.ijeas.system.trx;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TrxServiceImpl implements TrxService{
    private final TrxQdslRepository trxQdslRepository;

    @Override
    public List<TrxDto> getTrxList(TrxDto trxDto){
        return trxQdslRepository.getTrxList(trxDto);
    }

    @Override
    public List<TrxDto> getSlipTrxList(TrxDto trxDto){
        return trxQdslRepository.getSlipTrxList(trxDto);
    }

    @Override
    public List<TrxDto> getAwtInfo(TrxDto trxDto){
        return trxQdslRepository.getAwtInfo(trxDto);
    }
}
