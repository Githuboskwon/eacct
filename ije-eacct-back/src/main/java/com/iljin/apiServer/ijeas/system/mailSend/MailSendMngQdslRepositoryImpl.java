package com.iljin.apiServer.ijeas.system.mailSend;


import static com.iljin.apiServer.ijeas.card.QCardUseList.*;

import com.iljin.apiServer.ijeas.card.QCardUseList;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MailSendMngQdslRepositoryImpl implements MailSendMngQdslRepository{

    private final JPAQueryFactory queryFactory;

//    @Override
//    public List<MailHistoryDto> getUnApprUserList(MailHistoryDto mailHistoryDto) {
//        return queryFactory
//            .select()
//            .from(cardUseList)
//            .
//    }

}
