package com.iljin.apiServer.ijeas.sm.evid;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UFileQdslRepositoryImpl implements UFileQdslRepository{

    private final JPAQueryFactory queryFactory;

}
