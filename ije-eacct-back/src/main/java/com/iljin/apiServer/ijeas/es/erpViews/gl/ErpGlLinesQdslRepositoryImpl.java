package com.iljin.apiServer.ijeas.es.erpViews.gl;

import static com.iljin.apiServer.ijeas.es.erpViews.gl.QErpGlLines.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ErpGlLinesQdslRepositoryImpl implements ErpGlLinesQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ErpGlLines> getErpGlLinesByJeHeaderId(BigDecimal jeHeaderId) {
        return queryFactory
            .selectFrom(erpGlLines)
            .where(erpGlLines.jeHeaderId.eq(jeHeaderId))
            .fetch();
    }

}
