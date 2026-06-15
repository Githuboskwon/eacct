package com.iljin.apiServer.ijeas.system.dept;


import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.cctr.QCostCenter.costCenter;
import static com.iljin.apiServer.ijeas.system.dept.QDeptAuth.deptAuth;

@RequiredArgsConstructor
@Repository
public class DeptAuthQdslRepositoryImpl implements DeptAuthQdslRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DeptAuthDto> getDeptAuthList(DeptAuthDto deptAuthDto) {

        return queryFactory
                .select(new QDeptAuthDto(
                        deptAuth.compCd,
                        deptAuth.deptCd,
                        costCenter.deptNm.as("deptNm"),
                        deptAuth.remark
                        ))
                .from(deptAuth)
                .leftJoin(costCenter).on(deptAuth.deptCd.eq(costCenter.deptCd))
                .where(deptAuth.compCd.eq(deptAuthDto.getCompCd())
                .and(deptAuth.empNo.eq(deptAuthDto.getEmpNo()))
                )
                .orderBy(deptAuth.deptCd.asc())
                .fetch();
    }
}
