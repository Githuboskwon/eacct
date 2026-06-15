package com.iljin.apiServer.ijeas.mobile.board;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MoBoardServiceImple implements MoBoardService{
    private final MoBoardRepository moBoardRepository;
    private final Util util;

    @Override
    public ResponseEntity<Object> saveBoard(MoBoard moBoard){
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        String loginId = loginUser.getLoginId();

        moBoard.setCompCd(compCd);
        moBoard.setLoginId(loginId);
        moBoardRepository.save(moBoard);
        return new ResponseEntity<>(moBoard, HttpStatus.OK);
    }

    @Override
    public MoBoard detailBoard(Long id, String writeId) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        return moBoardRepository.findByIdContainsAndCompCdContainsAndLoginId(id,compCd,writeId);
    }

    @Override
    public List<MoBoard> searchBoard(MoBoardDto moBoardDto) {
        String keyWord = moBoardDto.getKeyWord() != null ? moBoardDto.getKeyWord() : "";
        String searchDtmFr = moBoardDto.getSearchDtmFr();
        String searchDtmTo = moBoardDto.getSearchDtmTo();
        return moBoardRepository.searchBoard(keyWord, searchDtmFr, searchDtmTo);
    }
}
