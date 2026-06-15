package com.iljin.apiServer.ijeas.mobile.board;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface MoBoardService {

    @Modifying
    ResponseEntity<Object> saveBoard(MoBoard moBoard);

    MoBoard detailBoard(Long id, String writeId);

    List<MoBoard> searchBoard(MoBoardDto moBoardDto);
}
