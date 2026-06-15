package com.iljin.apiServer.ijeas.slip.header;


import com.iljin.apiServer.core.util.Util;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.iljin.apiServer.ijeas.slip.header.QSlipBusinessTrip.slipBusinessTrip;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SlipBusinessTripQdslRepositoryImpl implements SlipBusinessTripQdslRepository {

    private final Util util;

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SlipBusinessTripDto> getBusinessTripList(SlipBusinessTripDto slipBusinessTripDto){
        String ledgerId  = util.getLoginUser().getEmployee().getLedgerId();
        String compCd  = util.getLoginCompCd();

        return queryFactory
                .select(new QSlipBusinessTripDto(
                        slipBusinessTrip.compCd,
                        slipBusinessTrip.masterSlipNo,
                        slipBusinessTrip.masterSlipHeaderId,
                        slipBusinessTrip.seq,
                        slipBusinessTrip.ledgerId,
                        slipBusinessTrip.detailSlipHeaderId,
                        slipBusinessTrip.detailSlipNo,
                        slipBusinessTrip.detailGlDt,
                        slipBusinessTrip.detailTrxType,
                        slipBusinessTrip.detailVendorId,
                        slipBusinessTrip.detailVendorNm,
                        slipBusinessTrip.detailUsedCur,
                        slipBusinessTrip.detailUsedAmt,
                        slipBusinessTrip.detailRegId,
                        slipBusinessTrip.detailRegDtm
                ))
                .from(slipBusinessTrip)
                .where(slipBusinessTrip.compCd.eq(compCd),
                        slipBusinessTrip.ledgerId.eq(Integer.valueOf(ledgerId)),
                        slipBusinessTrip.masterSlipNo.eq(slipBusinessTripDto.getMasterSlipNo()),
                        slipBusinessTrip.masterSlipHeaderId.eq(slipBusinessTripDto.getMasterSlipHeaderId())
                )
                .orderBy(slipBusinessTrip.seq.asc())
                .fetch();
    }

}
