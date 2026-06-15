package com.iljin.apiServer.ijeas.mobile.board;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/api/mobile/board")
public class MoBoardController {

    private final MoBoardService moBoardService;

    @PutMapping("/saveBoard")
    public ResponseEntity<Object> saveBoard(@RequestBody MoBoard moBoard){

        return moBoardService.saveBoard(moBoard);
    }

    @GetMapping("/detailBoard")
    public MoBoard detailBoard(@RequestParam Long id, @RequestParam String writeId ){
        return moBoardService.detailBoard(id, writeId);
    }

    @PostMapping("/searchBoard")
    public List<MoBoard> searchBoard(@RequestBody MoBoardDto moBoardDto){
        return moBoardService.searchBoard(moBoardDto);
    }
}
