package com.iljin.apiServer.ijeas.sm.apprLine;

import static com.iljin.apiServer.ijeas.sm.apprLine.QApprLineDt.apprLineDt;
import static com.iljin.apiServer.ijeas.sm.apprLine.QApprLineHd.apprLineHd;
import static com.iljin.apiServer.ijeas.system.emp.QEmployee.employee;
import static org.springframework.util.StringUtils.hasText;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
@Repository
public class ApprLineQdslRepositoryImpl implements ApprLineQdslRepository {
    private final JPAQueryFactory queryFactory;

    private final ApprLineHdRepository apprLineHdRepository;
    private final ApprLineDtRepository apprLineDtRepository;

    // 헤더 조회
    @Override
    public List<ApprLineDto> getApprLineHdList(ApprLineDto alh){
        return queryFactory
                .select( new QApprLineDto(apprLineHd.compCd,
                         apprLineHd.userId,
                         ExpressionUtils.as(JPAExpressions
                                        .select(employee.empNm)
                                        .from(employee)
                                        .where(employee.compCd.eq(apprLineHd.compCd), employee.empNo.eq(apprLineHd.userId))
                                ,"userNm"),
                         apprLineHd.apprSeq,
                         apprLineHd.apprLineTitle,
                         apprLineHd.mainApprYn,
                         apprLineHd.remark,
                         apprLineHd.useYn,
                         apprLineHd.regId,
                         apprLineHd.regDtm,
                         apprLineHd.chgId,
                         apprLineHd.chgDtm,
                         Expressions.asString("").as("temp")
                        ))
                .from(apprLineHd)
                .where(apprLineHd.compCd.eq(alh.compCd),
                       hdUserIdEq(alh.userId),
                       hdApprLineTitleLike(alh.apprLineTitle),
                       hdMainApprYnEq(alh.mainApprYn),
                       hdUseYnEq(alh.useYn) )
                .orderBy(apprLineHd.userId.asc(), apprLineHd.apprSeq.asc())
                .fetch();
    }

    // 디테일 조회
    @Override
    public List<ApprLineDto> getApprLineDtList(ApprLineDto alh){
       return queryFactory
                .select( new QApprLineDto(apprLineDt.compCd,
                        apprLineDt.userId,
                        apprLineDt.apprSeq,
                        apprLineDt.subApprSeq,
                        apprLineDt.apprUserId,
                        apprLineDt.apprTypeCd,
                        employee.empNm,
                        employee.jobCd,
                        employee.jobNm,
                        employee.deptCd,
                        employee.deptNm,
                        employee.serveCd
                        ))
                .from(apprLineDt)
                .innerJoin(employee).on(employee.compCd.eq(apprLineDt.compCd), employee.empNo.eq(apprLineDt.apprUserId))
                .where(apprLineDt.compCd.eq(alh.compCd),
                       apprLineDt.apprSeq.eq(alh.apprSeq) ,
                       dtUserIdEq(alh.userId) )
                .orderBy(apprLineDt.subApprSeq.asc())
                .fetch();
    }

    // 헤더 라인 삭제
    @Override
    public ResponseEntity<String> delApprLineHd(ApprLineHd alh){
        long count = queryFactory
                .delete(apprLineHd)
                .where( apprLineHd.compCd.eq(alh.compCd),
                        apprLineHd.userId.eq(alh.userId),
                        apprLineHd.apprSeq.eq(alh.apprSeq)
                )
                .execute();

        //중간 라인 삭제시 순번 수정
        long count2 = queryFactory
                .update(apprLineHd)
                .set(apprLineHd.apprSeq, apprLineHd.apprSeq.add(-1))
                .where( apprLineHd.compCd.eq(alh.compCd),
                        apprLineHd.userId.eq(alh.userId),
                        apprLineHd.apprSeq.gt(alh.apprSeq))
                .execute();

        return new ResponseEntity<>(count + "건 삭제되었습니다.", HttpStatus.OK);
    }

    // 디테일 라인 삭제
    @Override
    public ResponseEntity<String> delApprLineDt(ApprLineDt ald){
        long count = queryFactory
                .delete(apprLineDt)
                .where( apprLineDt.compCd.eq(ald.compCd),
                        apprLineDt.userId.eq(ald.userId),
                        apprLineDt.apprSeq.eq(ald.apprSeq),
                        apprLineDt.subApprSeq.eq(ald.subApprSeq)
                )
                .execute();

        //중간 라인 삭제시 순번 수정
        long count2 = queryFactory
                .update(apprLineDt)
                .set(apprLineDt.subApprSeq, apprLineDt.subApprSeq.add(-1))
                .where( apprLineDt.compCd.eq(ald.compCd),
                        apprLineDt.userId.eq(ald.userId),
                        apprLineDt.apprSeq.eq(ald.apprSeq),
                        apprLineDt.subApprSeq.gt(ald.subApprSeq))
                .execute();

        return new ResponseEntity<>(count + "건 삭제되었습니다.", HttpStatus.OK);
    }

    //디테일 전부 삭제
    @Override
    public ResponseEntity<String> deleteAllList(ApprLineDt ald){
        long count = queryFactory
                .delete(apprLineDt)
                .where( apprLineDt.compCd.eq(ald.compCd),
                        apprLineDt.userId.eq(ald.userId),
                        apprLineDt.apprSeq.eq(ald.apprSeq)
                )
                .execute();

        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    //라인 저장
    public ResponseEntity<String> saveApprLineDt(List<ApprLineDt> ald){
        long count = queryFactory
                .delete(apprLineDt)
                .where( apprLineDt.compCd.eq(ald.get(0).compCd),
                        apprLineDt.userId.eq(ald.get(0).userId),
                        apprLineDt.apprSeq.eq(ald.get(0).apprSeq),
                        apprLineDt.subApprSeq.gt(ald.size())
                )
                .execute();

        ald.forEach(dt -> {
            apprLineDtRepository.save(dt);
        });


        return new ResponseEntity<>("저장되었습니다.", HttpStatus.OK);
    }

    //커스텀 조건
    private BooleanExpression hdUserIdEq(String userId) {
        return hasText(userId) ? apprLineHd.userId.eq(userId) : null;
    }

    private BooleanExpression hdApprLineTitleLike(String apprLineTitle) {
        return hasText(apprLineTitle) ? apprLineHd.apprLineTitle.containsIgnoreCase(apprLineTitle.toUpperCase()) : null;
    }

    private BooleanExpression hdMainApprYnEq(String mainApprYn) {
        return hasText(mainApprYn) ? apprLineHd.mainApprYn.eq(mainApprYn) : null;
    }

    private BooleanExpression hdUseYnEq(String useYn) {
        return hasText(useYn) ? apprLineHd.useYn.eq(useYn) : null;
    }

    private BooleanExpression dtUserIdEq(String userId) {
        return hasText(userId) ? apprLineDt.userId.eq(userId) : null;
    }

    private BooleanExpression hdApprSeqEq(Integer apprSeq) {
        return !StringUtils.isEmpty(apprSeq) ? apprLineHd.apprSeq.eq(apprSeq) : null;
    }
}
