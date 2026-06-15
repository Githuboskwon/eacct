package com.iljin.apiServer.ijeas.system.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/api/item")
public class ItemController {
    private final ItemService itemService;

    @PostMapping("/list")
    public ResponseEntity<List<ItemDto>> getItemList(@RequestBody ItemDto itemDto){
        return new ResponseEntity<>(itemService.getItemList(itemDto), HttpStatus.OK);
    }

    @PostMapping("/slip/list")
    public ResponseEntity<List<ItemDto>> getSlipItemList(@RequestBody ItemDto itemDto){
        return new ResponseEntity<>(itemService.getSlipItemList(itemDto), HttpStatus.OK);
    }

}
