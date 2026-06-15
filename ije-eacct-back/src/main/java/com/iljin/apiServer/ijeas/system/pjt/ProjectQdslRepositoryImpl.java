package com.iljin.apiServer.ijeas.system.pjt;

import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.PjtRegistInfoDto;
import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.QPjtRegistInfo;
import com.iljin.apiServer.ijeas.ims.pjtRegistInfo.QPjtRegistInfoDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.iljin.apiServer.ijeas.system.pjt.QCboApEmpSegment6.cboApEmpSegment6;
import static com.iljin.apiServer.ijeas.system.pjt.QProject.project;
import static com.iljin.apiServer.ijeas.ims.pjtRegistInfo.QPjtRegistInfo.pjtRegistInfo;
import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Repository
public class ProjectQdslRepositoryImpl implements ProjectQdslRepository{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProjectDto> getProjectList(String compCd, String value) {
        return queryFactory
            .select(new QProjectDto(
                project.compCd,
                project.projectCd,
                project.projectNm,
                project.projectId
            ))
            .from(project)
            .where(compCdEq(compCd),
                searchCondition(value))
            .fetch();
    }

    @Override
    public List<ProjectDto> getSlipProjectList(ProjectDto projectDto){
        List<ProjectDto> result = queryFactory
                .select(new QProjectDto(
                        project.compCd,
                        project.projectCd,
                        project.projectNm,
                        project.projectId
                ))
                .from(project)
                .where(
                        project.projectCd.in(JPAExpressions.select(cboApEmpSegment6.segment6)
                                                        .from(cboApEmpSegment6)
                                                        .where(cboApEmpSegment6.personId.eq(projectDto.personId)
                                                                .and(cboApEmpSegment6.enabledFlag.eq("Y"))
                                                                .and(cboApEmpSegment6.orgId.eq(Integer.valueOf(projectDto.compCd))))
                        )
                        .and(compCdEq(projectDto.compCd))
                        //.and(project.startDateActive.loe(LocalDate.parse(projectDto.postingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()))
                        //.and(project.endDateActive.goe(LocalDate.parse(projectDto.postingDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay()))
                        .and(searchCondition(projectDto.projectCd).or(searchCondition(projectDto.projectNm)))
                )
                .orderBy(project.projectCd.asc())
                .fetch();


        return result;
    }

    @Override
    public List<PjtRegistInfoDto> getRegistInfoProjectList(PjtRegistInfoDto pjtRegistInfoDto){

        QPjtRegistInfo pjtRegistInfo1 = new QPjtRegistInfo("pjtRegistInfo");

        return queryFactory
                .selectDistinct(new QPjtRegistInfoDto(
                        pjtRegistInfo.projectManageNo,
                        pjtRegistInfo.projectName.concat("_").concat(pjtRegistInfo.taskName).as("projectManageNm")
                ))
                .from(pjtRegistInfo)
                .where(
                        projectManageNoLk(pjtRegistInfoDto.getProjectManageNo()),
                        projectManageNmLk(pjtRegistInfoDto.getProjectManageNm())
//                        pjtRegistInfo.degree.eq(JPAExpressions.select(pjtRegistInfo1.degree.max())
//                                                .from(pjtRegistInfo1)
//                                                .where(pjtRegistInfo1.orgId.eq(pjtRegistInfo.orgId), pjtRegistInfo1.projectManageNo.eq(pjtRegistInfo.projectManageNo)))
                )
                .orderBy(pjtRegistInfo.projectManageNo.desc())
                .fetch();
    }

    private BooleanExpression compCdEq(String compCd) {
        return hasText(compCd) ? project.compCd.eq(compCd) : null;
    }

    private BooleanBuilder searchCondition(String value) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(hasText(value)) {
            booleanBuilder.or(project.projectCd.contains(value))
                .or(project.projectNm.contains(value));
        }
        return booleanBuilder;
    }

    private BooleanExpression projectNmContains(String projectNm) {
        return hasText(projectNm) ? project.projectNm.contains(projectNm) : null;
    }

    private BooleanExpression projectManageNoLk(String projectManageNo) {
        return hasText(projectManageNo) ? pjtRegistInfo.projectManageNo.toUpperCase().likeIgnoreCase("%" + projectManageNo.toUpperCase() + "%") : null;
    }

    private BooleanExpression projectManageNmLk(String projectManageNm) {
        return hasText(projectManageNm) ? pjtRegistInfo.projectName.concat(pjtRegistInfo.taskName).toUpperCase().likeIgnoreCase("%" + projectManageNm.toUpperCase() + "%") : null;
    }




}
