package com.iljin.apiServer.ijeas.ims.pjtProgressChart;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class PjtProgressChartDto implements Serializable {

    String orgId;

    //PJT ID
    String projectId;

    //PJT 코드
    String projectCode;

    //PJT 구분
    String projectGubun;


    //PJT 명
    String projectName;
    
    //타스크 ID
    String taskId;

    //타스크 번호
    String taskNumber;

    //타스크 명
    String taskName;

    //PJT 관리번호
    String projectManageNo;

    String PROJECT_MANAGE_NO;

    //PJT 관리명
    String projectManageNm;

    //PJT 시작일
    String startDate;

    //PJT 종료일
    String endDate;

    //PJT 기간(개월수)
    String totalMonth;

    //수주금액
    String ctAmount;

    //수주금액 외화
    String ctAmountFor;

    //상태
    String projectStatus;

    //소장/담당자 사번
    String pjtSiteManagerUserId;

    //소장/담당자 명
    String pjtSiteManagerUserName;

    //소장/담당자 연락처
    String pjtSiteManagerPhone;

    //영업담당 사번
    String pjtBusinessUserId;

    //영업담당 명
    String pjtBusinessUserName;

    //지역
    String pjtLocation;

    //A:공사국내, B:공사해외
    String pjtType;

    //전표번호
    String slipNo;

    //첨부파일 KEY
    String docNo;

    //비고
    String remark;

    //부서코드
    String deptCode;

    //부서명
    String deptName;

    //팀ID - 사용안함
    String jobId;

    //팀명 - 사용안함
    String jobName;

    //작성일
    String addDate;

    //작성시간
    String addTime;

    //작성자
    String addUserId;

    //수정일
    String changeDate;

    //수정시간
    String changeTime;

    //수정자
    String changeUserId;

    //고객 ID
    String customerId;

    //고객명
    String customerName;

    //최종수요처코드
    String mainContractCode;

    //최종수요처명
    String mainContractName;

    //수주일
    String ctDate;

    //통화
    String ctCurrency;

    //환율
    String ctExchangeRate;

    //환율일자
    String ctExchangeRateDate;

    String pjtJobId;

    String pjtJobName;

    String countryCode;

    String countryName;

    String issue;

    String issueMeasure;

    String issueChangeDate;

    String issueChangeTime;

    String issueChangeUserId;

    String degree;

    String weeklyIssue;

    String attachNo;
    
    //예산부서 - 팀
    String bdeptCode;

    //예산부서 - 이름
    String bdeptName;

    String pjtBdeptCode;

    String pjtBdeptName;

    String ctOriAmount;

    String slipType;

    String saveStartDate;

    String saveEndDate;

    String addUserNameUse;

    String content;

    List<String> param;

    String inAmtPerFlag;

    public PjtProgressChartDto(String issue, String issueMeasure, String weeklyIssue) {
        this.issue = issue;
        this.issueMeasure = issueMeasure;
        this.weeklyIssue = weeklyIssue;
    }

}
