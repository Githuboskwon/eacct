package com.iljin.apiServer.ijeas.sm.apprLine;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ApprLineRepositoryCustomImpl implements ApprLineRepositoryCustom {

    private final JPAQueryFactory queryFactory;


}
