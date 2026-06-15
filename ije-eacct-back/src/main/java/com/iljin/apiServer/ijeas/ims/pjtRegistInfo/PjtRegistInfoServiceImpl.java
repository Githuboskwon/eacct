package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;

import com.iljin.apiServer.core.util.Util;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PjtRegistInfoServiceImpl implements PjtRegistInfoService {

    private final PjtRegistInfoQdslRepository pjtRegistInfoQdslRepository;

    private final PjtRegistInfoRepository pjtRegistInfoRepository;

    private Util util;

    @Override
    public List<PjtRegistInfoDto> getRegistInfoList(PjtRegistInfoDto pjtRegistInfoDto) {
        return pjtRegistInfoQdslRepository.getRegistInfoList(pjtRegistInfoDto);
    }

    @Override
    public String saveRegistInfoList(List<PjtRegistInfoDto> pjtRegistInfoDtos) {

        String compCd = util.getLoginCompCd();

        for(PjtRegistInfoDto pjtRegistInfoDto : pjtRegistInfoDtos){

            PjtRegistInfoKey pjtRegistInfoKey = new PjtRegistInfoKey(compCd, pjtRegistInfoDto.getProjectId(), pjtRegistInfoDto.getProjectCode(), pjtRegistInfoDto.getTaskNumber(), pjtRegistInfoDto.getProjectManageNo(), pjtRegistInfoDto.getDegree());
            Optional<PjtRegistInfo> sp = pjtRegistInfoRepository.findById(pjtRegistInfoKey);

            if(sp.isPresent()){
                sp.ifPresent(c ->{
                    c.update(pjtRegistInfoDto);
                });
            };
        }
        return "저장되었습니다.";
    }
}
