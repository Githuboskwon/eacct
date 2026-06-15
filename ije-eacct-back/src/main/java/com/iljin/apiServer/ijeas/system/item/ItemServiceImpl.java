package com.iljin.apiServer.ijeas.system.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService{
    private final ItemQdslRepository itemQdslRepository;

    @Override
    public List<ItemDto> getItemList(ItemDto itemDto){
        return itemQdslRepository.getItemList(itemDto);
    }

    @Override
    public List<ItemDto> getSlipItemList(ItemDto itemDto){
        return itemQdslRepository.getSlipItemList(itemDto);
    }
}
