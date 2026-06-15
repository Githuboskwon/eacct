package com.iljin.apiServer.ijeas.sm.jiniEvid;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UJiniFileQdslRepositoryImpl implements UJiniFileQdslRepository {

    private final JPAQueryFactory queryFactory;

}
