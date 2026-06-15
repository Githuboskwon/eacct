package com.iljin.apiServer.ijeas.sm.jini;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.slip.SlipHeader;
import com.iljin.apiServer.ijeas.slip.SlipHeaderRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JiniServiceImpl implements JiniService{

    private final Util util;

    private final JiniRepository jiniRepository;

    private final JiniQdslRepository jiniQdslRepository;

    private final SlipHeaderRepository slipHeaderRepository;

    @Override
    public String saveJiniDocs(List<JiniDto> jiniDtoList) {
        String loginCompCd = util.getLoginCompCd();
        if(jiniDtoList.size() > 0) {
//            jiniRepository.deleteByCompCdAndDocumentId(loginCompCd, jiniDtoList.get(0).getSlipNo());

            String docTitle = "";
            String docUrl = "";

            //기존 프레임워크 방식
            /*jiniDtoList.forEach(dto -> {
                Jini jini = Jini.from(dto, loginCompCd);
                jiniRepository.save(jini);
            });*/

            //TB_SLIP_HD 저장 방식 변경
            for(int i=0; i<jiniDtoList.size(); i++){
                docTitle += jiniDtoList.get(i).jiniNm;
                docUrl += jiniDtoList.get(i).jiniId;

                if((i+1) != jiniDtoList.size()){
                    docTitle += ",";
                    docUrl += "|||";
                }
            }

            SlipHeader slipheader = slipHeaderRepository.findByCompCdAndSlipNo(loginCompCd, jiniDtoList.get(0).getSlipNo()).orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));
            //slipheader.updateGroupwareDoc(loginCompCd, jiniDtoList.get(0).getSlipNo(), docTitle, docUrl);
            slipheader.updatetestSlip(docTitle, docUrl);

            //slipHeaderRepository.save(slipheader);
        }
        return "저장되었습니다.";
    }

    @Override
    public List<JiniDto> getJiniDocs(String documentId) {
        String loginCompCd = util.getLoginCompCd();
        /*return jiniRepository.findAllByCompCdAndDocumentId(loginCompCd, documentId).stream()
            .map(JiniDto::from).collect(Collectors.toList());*/

        //TB_SLIP_HD 저장 방식 변경
        List<JiniDto> list = new ArrayList<>();

        SlipHeader slipheader = slipHeaderRepository.findBySlipNo(documentId).orElseThrow(() -> new RuntimeException("전표가 존재하지 않습니다."));

        if(slipheader.getDocTitle() != null){
            if(slipheader.getDocTitle().indexOf(",") > -1 && slipheader.getDocUrl().indexOf("|||") > -1){
                String [] splitDocTitle = slipheader.getDocTitle().split(",");
                String [] splitDocNo = slipheader.getDocUrl().split("\\|\\|\\|");

                for(int i=0; i<splitDocNo.length; i++){
                    String docUrl = "";
                    if(splitDocNo[i].indexOf("_AA") > -1){
                        docUrl = "https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID=" + splitDocNo[i].split("_")[1];
                    } else {
                        docUrl = splitDocNo[i];
                    }

                    list.add(new JiniDto(loginCompCd, slipheader.getSlipNo(), splitDocNo[i], splitDocTitle[i], docUrl));
                }
            }else{
                String docUrl = "";
                if(slipheader.getDocUrl().indexOf("_AA") > -1){
                    docUrl = "https://jini.iljin.co.kr/ekp/service/openapi/IF_CUS_EAP_002_goView?APP_ID=" + slipheader.getDocUrl().split("_")[1];
                }else{
                    docUrl = slipheader.getDocUrl();
                }

                list.add(new JiniDto(loginCompCd, slipheader.getSlipNo(), slipheader.getDocUrl(), slipheader.getDocTitle(), docUrl));
            }
        }else{
            return null;
        }

        return list;
    }

    @Override
    public String deleteJiniDocs(List<JiniDto> jiniDtoList) {
        String loginCompCd = util.getLoginCompCd();
        jiniRepository.deleteByCompCdAndDocumentId(loginCompCd, jiniDtoList.get(0).getSlipNo());
        return "삭제되었습니다.";
    }

    @Override
    public List<JiniDto> getJiniDocsByPreviousMonth(String customer, String writer) {
        return jiniQdslRepository.getJiniDocsByPreviousMonth(customer, writer).stream()
            .map(JiniDto::from).collect(Collectors.toList());
    }

    @Override
    public String saveJiniDocsByBundle(BundleJiniDocs bundleJiniDocs) {
        List<JiniDto> jiniDtoList = bundleJiniDocs.getJiniDtoList();
        List<String> slipNoList = bundleJiniDocs.getSlipNoList();

        for(String slipNo : slipNoList) {
            jiniDtoList.get(0).setSlipNo(slipNo);
            saveJiniDocs(jiniDtoList);
        }
        return "저장되었습니다.";
    }

}
