package com.iljin.apiServer.ijeas.bond;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.iljin.apiServer.core.security.user.User;
import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.erpViews.ErpPaProjectsAllDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BondServiceImpl implements BondService {

    private final BondMstQdslRepository bondMstQdslRepository;
    private final BondMstRepository bondMstRepository;

    private final BondHeaderRepository bondHeaderRepository;
    private final BondHeaderQdslRepository bondHeaderQdslRepository;
    private final BondRepositoryCustom bondRepositoryCustom;
    private final Util util;

    @Override
    public List<BondMstDto> getBondMstList(BondMstDto bondMstDto) {
        return bondMstQdslRepository.getBondMstList(bondMstDto);
    }

    @Override
    public String saveBondMstList(List<BondMstDto> bondMstDtos) {
        User loginUser = util.getLoginUser();
        String compCd = loginUser.getCompCd();
        for(BondMstDto bondMstDto : bondMstDtos) {
            BondMstKey bondMstKey = new BondMstKey(bondMstDto.getCompCd(), bondMstDto.getRefNo());
            Optional<BondMst> result = bondMstRepository.findById(bondMstKey);

            if(result.isPresent()) {
                if((bondHeaderRepository.existsByCompCdAndRefNo(compCd, bondMstDto.getRefNo()) && isNull(bondMstDto.getReleaseDt()))){
                    throw new BondMstException("해당 ref no의 BOND경비전표가 존재합니다. 수정할 수 없습니다.");
                }

                result.ifPresent(b -> {
                    if(nonNull(bondMstDto.getReleaseDt())) {
                        b.changeReleaseDt(bondMstDto.getReleaseDt());
                    } else {
                        b.update(bondMstDto);
                    }
                });
            } else {
                BondMst bondMst = BondMst.builder()
                    .compCd(compCd)
                    .refNo(bondMstDto.getRefNo())
                    .bondCd(bondMstDto.getBondCd())
                    .benCountry(bondMstDto.getBenCountry())
                    .projectNm(bondMstDto.getProjectNm())
                    .intBankNm(bondMstDto.getIntBankNm())
                    .openingDt(bondMstDto.getOpeningDt())
                    .budget(bondMstDto.getBudget())
                    .customerNm(bondMstDto.getCustomerNm())
                    .customerId(bondMstDto.getCustomerId())
                    .projectId(bondMstDto.getProjectId())
                    .localBankNm(bondMstDto.getLocalBankNm())
                    .localBankId(bondMstDto.getLocalBankId())
                    .currencyAmt(bondMstDto.getCurrencyAmt())
                    .currencyCd(bondMstDto.getCurrencyCd())
                    .build();
                bondMstRepository.save(bondMst);
            }
        }
        return "저장되었습니다.";
    }

    @Override
    public String deleteBondMstList(List<BondMstDto> bondMstDtos) {
        String compCdLoginUser = util.getLoginCompCd();
        for(BondMstDto bondMstDto : bondMstDtos) {
            if(bondHeaderRepository.existsByCompCdAndRefNo(compCdLoginUser, bondMstDto.getRefNo())){
                throw new BondMstException("해당 ref_no의 BOND경비전표가 존재합니다. 삭제할 수 없습니다.");
            }

            BondMstKey bondMstKey = new BondMstKey(compCdLoginUser, bondMstDto.getRefNo());
            if(!bondMstRepository.existsById(bondMstKey)) {
                throw new RuntimeException("삭제할 BOND가 존재하지 않습니다.");
            }
            bondMstRepository.deleteById(bondMstKey);
        }
        return "삭제되었습니다.";
    }

    @Override
    public List<BondExpendDto> getBondExpendList(BondExpendDto bondExpendDto) {
        return bondRepositoryCustom.getBondExpendList(bondExpendDto);
    }

    @Override
    public List<ErpPaProjectsAllDto> getProjectList(ErpPaProjectsAllDto erpPaProjectsAllDto) {
        return bondHeaderQdslRepository.getProjectList(erpPaProjectsAllDto.getSearch());
    }
}
