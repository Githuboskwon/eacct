package com.iljin.apiServer.ijeas.system.item;

import java.util.List;

public interface ItemService {

    List<ItemDto> getItemList(ItemDto itemDto);

    List<ItemDto> getSlipItemList(ItemDto itemDto);
}
