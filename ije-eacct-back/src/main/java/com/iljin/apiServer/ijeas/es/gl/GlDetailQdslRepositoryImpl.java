package com.iljin.apiServer.ijeas.es.gl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class GlDetailQdslRepositoryImpl implements GlDetailQdslRepository{

    private final JPAQueryFactory queryFactory;

//    @Override
//    public List<ErpGlSlipDto> getErpGlSlipDetail(ErpSlipRequestDto searchDto) {
//        return queryFactory
//            .select(new QGlDetail)
//    }

}
