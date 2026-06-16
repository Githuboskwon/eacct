package com.iljin.apiServer.ijeas.ims.pjtRegistInfo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

@NamedStoredProcedureQuery(
        name = "CBOSLIP.CBO_CT_ACTUAL_P",
        procedureName = "CBOSLIP.CBO_CT_ACTUAL_P",
        resultClasses = Map.class,
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_PERIOD_FR"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_PERIOD_TO"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_IN_AMT_PER_FLAG"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_PROJECT_NAME"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_PROJECT_GUBUN"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_COUNTRY_CODE"),
                @StoredProcedureParameter(mode = ParameterMode.IN, type = String.class, name = "P_COUNTRY_NAME"),
                @StoredProcedureParameter(mode = ParameterMode.REF_CURSOR, type = void.class, name = "OUT_CURSOR")
        }
)

@Getter
@NoArgsConstructor
@IdClass(PjtRegistInfoKey.class)
@Table(name = "SPJTM")
@Entity
public class PjtRegistInfo {

    // 직책코드
    @Id
    @Column(name = "ORG_ID",  nullable = false)
    String orgId;
    
    //PJT ID
    @Id
    @Column(name = "PROJECT_ID",  nullable = false)
    String projectId;

    //PJT 코드
    @Id
    @Column(name = "PROJECT_CODE",  nullable = false)
    String projectCode;

    //PJT 구분
    @Column(name = "PROJECT_GUBUN")
    String projectGubun;

    //PJT 명
    @Column(name = "PROJECT_NAME")
    String projectName;

    //타스크 ID
    @Column(name = "TASK_ID",  nullable = false)
    String taskId;

    //타스크 번호
    @Id
    @Column(name = "TASK_NUMBER",  nullable = false)
    String taskNumber;

    //타스크 명
    @Column(name = "TASK_NAME")
    String taskName;

    //PJT 관리번호
    @Id
    @Column(name = "PROJECT_MANAGE_NO",  nullable = false)
    String projectManageNo;

    //PJT 시작일
    @Column(name = "START_DATE")
    String startDate;

    //PJT 종료일
    @Column(name = "END_DATE")
    String endDate;

    //PJT 기간(개월수)
    @Column(name = "TOTAL_MONTH")
    String totalMonth;

    //수주금액
    @Column(name = "CT_AMOUNT")
    String ctAmount;

    //수주금액 외화
    @Column(name = "CT_AMOUNT_FOR")
    String ctAmountFor;

    //상태
    @Column(name = "PROJECT_STATUS")
    String projectStatus;

    //소장/담당자 사번
    @Column(name = "PJT_SITE_MANAGER_USER_ID")
    String pjtSiteManagerUserId;

    //소장/담당자 명
    @Column(name = "PJT_SITE_MANAGER_USER_NAME")
    String pjtSiteManagerUserName;

    //소장/담당자 연락처
    @Column(name = "PJT_SITE_MANAGER_PHONE")
    String pjtSiteManagerPhone;

    //영업담당 사번
    @Column(name = "PJT_BUSINESS_USER_ID")
    String pjtBusinessUserId;

    //영업담당 명
    @Column(name = "PJT_BUSINESS_USER_NAME")
    String pjtBusinessUserName;

    //지역
    @Column(name = "PJT_LOCATION")
    String pjtLocation;

    //A:공사국내, B:공사해외
    @Column(name = "PJT_TYPE")
    String pjtType;

    //전표번호
    @Column(name = "SLIP_NO")
    String slipNo;

    //첨부파일 KEY
    @Column(name = "DOC_NO")
    String docNo;

    //비고
    @Column(name = "REMARK")
    String remark;

    //부서코드
    @Column(name = "DEPT_CODE")
    String deptCode;

    //부서명
    @Column(name = "DEPT_NAME")
    String deptName;

    //팀ID - 사용안함
    @Column(name = "JOB_ID")
    String jobId;

    //팀명 - 사용안함
    @Column(name = "JOB_NAME")
    String jobName;

    //작성일
    @Column(name = "ADD_DATE")
    String addDate;

    //작성시간
    @Column(name = "ADD_TIME")
    String addTime;

    //작성자
    @Column(name = "ADD_USER_ID")
    String addUserId;

    //수정일
    @Column(name = "CHANGE_DATE")
    String changeDate;

    //수정시간
    @Column(name = "CHANGE_TIME")
    String changeTime;

    //수정자
    @Column(name = "CHANGE_USER_ID")
    String changeUserId;

    //고객 ID
    @Column(name = "CUSTOMER_ID")
    String customerId;

    //고객명
    @Column(name = "CUSTOMER_NAME")
    String customerName;

    //최종수요처코드
    @Column(name = "MAIN_CONTRACT_CODE")
    String mainContractCode;

    //최종수요처명
    @Column(name = "MAIN_CONTRACT_NAME")
    String mainContractName;

    //수주일
    @Column(name = "CT_DATE")
    String ctDate;

    //통화
    @Column(name = "CT_CURRENCY")
    String ctCurrency;

    //환율
    @Column(name = "CT_EXCHANGE_RATE")
    String ctExchangeRate;

    //환율일자
    @Column(name = "CT_EXCHANGE_RATE_DATE")
    String ctExchangeRateDate;

    @Column(name = "PJT_JOB_ID")
    String pjtJobId;

    @Column(name = "PJT_JOB_NAME")
    String pjtJobName;

    @Column(name = "COUNTRY_CODE")
    String countryCode;

    @Column(name = "COUNTRY_NAME")
    String countryName;

    @Column(name = "ISSUE")
    String issue;

    @Column(name = "ISSUE_MEASURE")
    String issueMeasure;

    @Column(name = "ISSUE_CHANGE_DATE")
    String issueChangeDate;

    @Column(name = "ISSUE_CHANGE_TIME")
    String issueChangeTime;

    @Column(name = "ISSUE_CHANGE_USER_ID")
    String issueChangeUserId;

    @Id
    @Column(name = "DEGREE", nullable = false)
    String degree;

    @Column(name = "WEEKLY_ISSUE")
    String weeklyIssue;

    @Column(name = "ATTACH_NO")
    String attachNo;

    //예산부서 - 팀
    @Column(name = "BDEPT_CODE")
    String bdeptCode;
    
    //예산부서 - 이름
    @Column(name = "BDEPT_NAME")
    String bdeptName;

    @Column(name = "PJT_BDEPT_CODE")
    String pjtBdeptCode;

    @Column(name = "PJT_BDEPT_NAME")
    String pjtBdeptName;

    @Column(name = "CT_ORI_AMOUNT")
    String ctOriAmount;

    @Builder
    public PjtRegistInfo(String orgId, String projectId, String projectCode, String projectGubun, String projectName,
                         String taskId, String taskNumber, String taskName, String projectManageNo,
                         String startDate, String endDate, String totalMonth, String ctAmount, String ctAmountFor,
                         String projectStatus, String pjtSiteManagerUserId, String pjtSiteManagerUserName,
                         String pjtSiteManagerPhone, String pjtBusinessUserId, String pjtBusinessUserName,
                         String pjtLocation, String pjtType, String slipNo, String docNo, String remark,
                         String deptCode, String deptName, String jobId, String jobName, String addDate,
                         String addTime, String addUserId, String changeDate, String changeTime, String changeUserId,
                         String customerId, String customerName, String mainContractCode, String mainContractName,
                         String ctDate, String ctCurrency, String ctExchangeRate, String ctExchangeRateDate,
                         String pjtJobId, String pjtJobName, String countryCode, String countryName, String issue,
                         String issueMeasure, String issueChangeDate, String issueChangeTime, String issueChangeUserId,
                         String degree, String weeklyIssue, String attachNo, String bdeptCode, String bdeptName, String pjtBdeptCode,
                         String pjtBdeptName, String ctOriAmount) {
        this.orgId = orgId;
        this.projectId = projectId;
        this.projectCode = projectCode;
        this.projectGubun = projectGubun;
        this.projectName = projectName;
        this.taskId = taskId;
        this.taskNumber = taskNumber;
        this.taskName = taskName;
        this.projectManageNo = projectManageNo;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalMonth = totalMonth;
        this.ctAmount = ctAmount;
        this.ctAmountFor = ctAmountFor;
        this.projectStatus = projectStatus;
        this.pjtSiteManagerUserId = pjtSiteManagerUserId;
        this.pjtSiteManagerUserName = pjtSiteManagerUserName;
        this.pjtSiteManagerPhone = pjtSiteManagerPhone;
        this.pjtBusinessUserId = pjtBusinessUserId;
        this.pjtBusinessUserName = pjtBusinessUserName;
        this.pjtLocation = pjtLocation;
        this.pjtType = pjtType;
        this.slipNo = slipNo;
        this.docNo = docNo;
        this.remark = remark;
        this.deptCode = deptCode;
        this.deptName = deptName;
        this.jobId = jobId;
        this.jobName = jobName;
        this.addDate = addDate;
        this.addTime = addTime;
        this.addUserId = addUserId;
        this.changeDate = changeDate;
        this.changeTime = changeTime;
        this.changeUserId = changeUserId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.mainContractCode = mainContractCode;
        this.mainContractName = mainContractName;
        this.ctDate = ctDate;
        this.ctCurrency = ctCurrency;
        this.ctExchangeRate = ctExchangeRate;
        this.ctExchangeRateDate = ctExchangeRateDate;
        this.pjtJobId = pjtJobId;
        this.pjtJobName = pjtJobName;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.issue = issue;
        this.issueMeasure = issueMeasure;
        this.issueChangeDate = issueChangeDate;
        this.issueChangeTime = issueChangeTime;
        this.issueChangeUserId = issueChangeUserId;
        this.degree = degree;
        this.weeklyIssue = weeklyIssue;
        this.attachNo = attachNo;
        this.bdeptCode = bdeptCode;
        this.bdeptName = bdeptName;
        this.pjtBdeptCode = pjtBdeptCode;
        this.pjtBdeptName = pjtBdeptName;
        this.ctOriAmount = ctOriAmount;
    }

    public void update(PjtRegistInfoDto pjtRegistInfoDto){
        this.startDate = pjtRegistInfoDto.getStartDate();
        this.endDate = pjtRegistInfoDto.getEndDate();
        this.totalMonth = pjtRegistInfoDto.getTotalMonth();
        this.pjtSiteManagerUserId = pjtRegistInfoDto.getPjtSiteManagerUserId();
        this.pjtSiteManagerUserName = pjtRegistInfoDto.getPjtSiteManagerUserName();
        this.addUserId = pjtRegistInfoDto.getAddUserId();
        this.changeDate = pjtRegistInfoDto.getChangeDate();
        this.changeTime = pjtRegistInfoDto.getChangeTime();
        this.changeUserId = pjtRegistInfoDto.getChangeUserId();
    }

}

