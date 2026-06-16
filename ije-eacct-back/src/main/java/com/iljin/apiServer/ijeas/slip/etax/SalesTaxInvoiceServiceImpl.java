package com.iljin.apiServer.ijeas.slip.etax;

import com.iljin.apiServer.core.util.Util;
import com.iljin.apiServer.ijeas.es.docMng.TbMngNoRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@AllArgsConstructor
@Service
public class SalesTaxInvoiceServiceImpl implements SalesTaxInvoiceService{
    private final SalesTaxInvoiceRepository salesTaxInvoiceRepository;
    private final RaCustomerTrxAllRepository raCustomerTrxAllRepository;
    private final CboArTrxMergeRepository cboArTrxMergeRepository;
    private final CboArEtaxIssueRepository cboArEtaxIssueRepository;
    private final CboArEtaxIssueInfoRepository cboArEtaxIssueInfoRepository;
    private final XxsbDtiInvoiceRepository xxsbDtiInvoiceRepository;
    private final TbMngNoRepository tbMngNoRepository;
    private final XxsbDtiMainRepository xxsbDtiMainRepository;
    private final XxsbDtiItemRepository xxsbDtiItemRepository;
    private final XxsbDtiStatusRepository xxsbDtiStatusRepository;
    private final Util util;

    //스마트빌 접속정보
    //@Value("${etax.smartbill.url}")
    private final String smartbillUrl = "http://197.200.1.19:10002";
    //@Value("${etax.smartbill.id}")
    private final String smartbillId = "ije12486";
    //@Value("${etax.smartbill.pwd}")
    private final String smartbillPwd = "a1234567";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SalesTaxInvoiceDto> getSalesTaxInvoiceList(SalesTaxInvoiceDto salesTaxInvoiceDto){

        return salesTaxInvoiceRepository.getSalesTaxInvoiceList(salesTaxInvoiceDto);
    }

    @Override
    public List<SalesTaxInvoiceDto> getLine(Integer trxId){

        return salesTaxInvoiceRepository.getLine(trxId);
    }

    @Override @Transactional
    public Map<String, String> accounting(Integer customerTrxId){
        Map<String, String> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT NVL(APPS.CBO_SP_COMMON_PKG.GET_AR_ACCOUNTING_STATUS(" + customerTrxId + "), 'N') AS COMPLETE_YN" +
                "  FROM DUAL "
        );

        Query query = entityManager.createNativeQuery(sb.toString());

        if((com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class)).isEmpty()){
            result.put("Message", "매출채권 Accounting 확인 오류. CUSTOMER_TRX_ID : " + customerTrxId);
            return result;
        }

        String completeYn = (com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class)).get(0).completeYn;

        if(completeYn.equals("N")){
            StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM");
            storedProcedureQuery.setParameter("p_customer_trx_id", customerTrxId);
            storedProcedureQuery.execute();

            String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");

            if(err_flag.equals("S")){
                result.put("Message", "Accounting 성공");
            }else{
                result.put("Message", "매출채권 Accounting 오류. CUSTOMER_TRX_ID : " + customerTrxId);
            }

        }else{
            result.put("Message", "매출채권 Accounting 확인 오류. CUSTOMER_TRX_ID : " + customerTrxId);
        }

        return result;
    }

    @Override @Transactional
    public Map<String, String> cancelInvoice(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        for(int i=0; i<list.size(); i++){
            List<CboArTrxMerge> trxList = cboArTrxMergeRepository.findByEtaxIssueId(new BigDecimal(list.get(i).etaxIssueId));

            for(int j=0; j<trxList.size(); j++){
                raCustomerTrxAllRepository.updateGlobalAttribute21ByCustomerTrxId(trxList.get(j).customerTrxId);
            }

            cboArTrxMergeRepository.updateDeleteFlagByEtaxIssueId(new BigDecimal(list.get(i).etaxIssueId));

            List<CboArEtaxIssue> etaxIssueList = cboArEtaxIssueRepository.findByEtaxIssueId(new BigDecimal(list.get(i).etaxIssueId));
            if(etaxIssueList.size() == 1){
                xxsbDtiInvoiceRepository.deleteByConversationId(etaxIssueList.get(0).conversationId);
            }
        }

        return result;
    }

    @Override @Transactional
    public Map<String, String> confirmSum(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT CBOSLIP.CBO_AR_ETAX_ISSUE_S.NEXTVAL AS etaxIssueId " +
                "  FROM DUAL "
        );

        Query query = entityManager.createNativeQuery(sb.toString());

        BigDecimal newEtaxIssueId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newEtaxIssueId;

        for(int i=0; i<list.size(); i++){
            CboArTrxMerge cboArTrxMerge = new CboArTrxMerge();

            cboArTrxMerge.etaxIssueId = newEtaxIssueId;
            cboArTrxMerge.customerTrxId = list.get(i).customerTrxId;
            cboArTrxMerge.deleteFlag = "N";
            cboArTrxMerge.createdPersonId = new BigDecimal(list.get(i).createdPersonId);
            cboArTrxMerge.creationDate = LocalDateTime.now();
            cboArTrxMerge.lastUpdatedPersonId = new BigDecimal(list.get(i).createdPersonId);
            cboArTrxMerge.lastUpdateDate = LocalDateTime.now();
            cboArTrxMerge.etaxExcludeFlag = "N";

            cboArTrxMergeRepository.save(cboArTrxMerge);

            raCustomerTrxAllRepository.updateGlobalAttribute4AndGlobalAttribute9AndGlobalAttribute10AndGlobalAttribute21ByCustomerTrxId(
                    list.get(i).taxEvidenceType,  list.get(i).deptCd, list.get(i).createdPersonId, String.valueOf(newEtaxIssueId), list.get(i).customerTrxId
            );
        }

        if(result.isEmpty()){
            result.put("Message", "월합확정 되었습니다.");
        }

        return result;
    }

    @Override @Transactional
    public Map<String, String> confirmOne(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();



        for(int i=0; i<list.size(); i++){
            StringBuilder sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_ISSUE_S.NEXTVAL AS etaxIssueId " +
                            "  FROM DUAL "
            );

            Query query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newEtaxIssueId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newEtaxIssueId;


            CboArTrxMerge cboArTrxMerge = new CboArTrxMerge();

            cboArTrxMerge.etaxIssueId = newEtaxIssueId;
            cboArTrxMerge.customerTrxId = list.get(i).customerTrxId;
            cboArTrxMerge.deleteFlag = "N";
            cboArTrxMerge.createdPersonId = new BigDecimal(list.get(i).createdPersonId);
            cboArTrxMerge.creationDate = LocalDateTime.now();
            cboArTrxMerge.lastUpdatedPersonId = new BigDecimal(list.get(i).createdPersonId);
            cboArTrxMerge.lastUpdateDate = LocalDateTime.now();
            cboArTrxMerge.etaxExcludeFlag = "N";

            cboArTrxMergeRepository.save(cboArTrxMerge);

            raCustomerTrxAllRepository.updateGlobalAttribute4AndGlobalAttribute9AndGlobalAttribute10AndGlobalAttribute21ByCustomerTrxId(
                    list.get(i).taxEvidenceType,  list.get(i).deptCd, list.get(i).createdPersonId, String.valueOf(newEtaxIssueId), list.get(i).customerTrxId
            );
        }

        if(result.isEmpty()){
            result.put("Message", "개별확정 되었습니다.");
        }

        return result;
    }


    @Override @Transactional
    public Map<String, String> confirmNot(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();



        for(int i=0; i<list.size(); i++){
            StringBuilder sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_ISSUE_S.NEXTVAL AS etaxIssueId " +
                            "  FROM DUAL "
            );

            Query query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newEtaxIssueId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newEtaxIssueId;


            CboArTrxMerge cboArTrxMerge = new CboArTrxMerge();

            cboArTrxMerge.etaxIssueId = newEtaxIssueId;
            cboArTrxMerge.customerTrxId = list.get(i).customerTrxId;
            cboArTrxMerge.deleteFlag = "N";
            cboArTrxMerge.createdPersonId = new BigDecimal(list.get(i).createdPersonId);
            cboArTrxMerge.creationDate = LocalDateTime.now();
            cboArTrxMerge.lastUpdatedPersonId = new BigDecimal(list.get(i).createdPersonId);
            cboArTrxMerge.lastUpdateDate = LocalDateTime.now();
            cboArTrxMerge.etaxExcludeFlag = "Y";

            cboArTrxMergeRepository.save(cboArTrxMerge);

            raCustomerTrxAllRepository.updateGlobalAttribute4AndGlobalAttribute9AndGlobalAttribute10AndGlobalAttribute21ByCustomerTrxId(
                    list.get(i).taxEvidenceType,  list.get(i).deptCd, list.get(i).createdPersonId, String.valueOf(newEtaxIssueId), list.get(i).customerTrxId
            );

            sb = new StringBuilder();

            sb.append(
                    "SELECT NVL(APPS.CBO_SP_COMMON_PKG.GET_AR_ACCOUNTING_STATUS(" + list.get(i).customerTrxId + "), 'N') AS COMPLETE_YN" +
                            "  FROM DUAL "
            );

            query = entityManager.createNativeQuery(sb.toString());

            String completeYn = (com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class)).get(0).completeYn;

            if(completeYn.equals("N")){
                StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM");
                storedProcedureQuery.setParameter("p_customer_trx_id", list.get(i).customerTrxId.intValue());
                storedProcedureQuery.execute();

                String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");

                if(err_flag.equals("S")){
                    result.put("Message", "Accounting 성공");
                }else{
                    result.put("Message", "매출채권 Accounting 오류. CUSTOMER_TRX_ID : " + list.get(i).customerTrxId);
                }
            }

        }

        if(result.isEmpty()){
            result.put("Message", "발행대상제외 되었습니다.");
        }

        return result;
    }

    @Override @Transactional
    public Map<String, String> modifyAmount(SalesTaxInvoiceDto salesTaxInvoiceDto){
        Map<String, String> result = new HashMap<>();

        raCustomerTrxAllRepository.updateGlobalAttribute22AndGlobalAttribute23ByCustomerTrxId(salesTaxInvoiceDto.modifySupplyAmount, salesTaxInvoiceDto.modifyTaxAmount, salesTaxInvoiceDto.customerTrxId);

        StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_COMMON_PKG.TRX_AMT_MODIFY");
        storedProcedureQuery.setParameter("p_customer_trx_id", salesTaxInvoiceDto.customerTrxId);
        storedProcedureQuery.execute();

        String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");
        String err_step = (String) storedProcedureQuery.getOutputParameterValue("x_err_step");
        String err_msg = (String) storedProcedureQuery.getOutputParameterValue("x_err_msg");

        if(err_flag.equals("S")){
            result.put("Message", "금액 수정 성공");
        }else{
            result.put("Message", "Step : " + err_step + "\nMessage : " + err_msg);

        }

        return result;
    }



    //매출세금계산서 발행

    @Override
    public List<SalesTaxInvoiceDto> getSalesTaxInvoiceIssueList(SalesTaxInvoiceDto salesTaxInvoiceDto){
        return salesTaxInvoiceRepository.getSalesTaxInvoiceIssueList(salesTaxInvoiceDto);
    }

    @Override
    public List<SalesTaxInvoiceDto> getItem(Integer etaxIssueId){
        return salesTaxInvoiceRepository.getItem(etaxIssueId);
    }

    @Override
    public List<SalesTaxInvoiceDto> getSlipNo(Integer etaxIssueId){
        return salesTaxInvoiceRepository.getSlipNo(etaxIssueId);
    }

    @Override
    public Map<String, String> issueTaxInvoice(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        //회사 정보 조회
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                "     , VAT_REGISTRATION_NUM_NON " +
                "     , REPRESENTATIVE_NAME " +
                "     , CUST_BUSINESS_CONDITION " +
                "     , CUST_BUSINESS_TYPE " +
                "     , CORPORATION_NAME " +
                "     , ADDRESS " +
                "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                " WHERE ORG_ID = :orgId"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", list.get(0).orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{

            for(int i=0; i<list.size(); i++){
                if(list.get(i).direction.equals("1")){
                    result.put("Message", "역발행은 [역발행저장] 버튼으로 처리 하시기 바랍니다.");
                    return result;
                }
            }


            //interface batch id 채번
            sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_IF_BATCH_S.NEXTVAL AS NEW_INTERFACE_BATCH_ID, 'temp' AS temp" +
                    "  FROM DUAL "
            );

            query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newInterfaceBatchId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId;


            //발행 메인 작업
            result = issueTaxInvoiceSub1(list, result, corperInfo, newInterfaceBatchId);
            if ((!result.isEmpty())) return result;

            //entityManager.flush();
            //end

            //Smartbill API call
            String param = "?BATCH_ID=" + newInterfaceBatchId;
            param += "&ID=" + smartbillId;
            param += "&PASS=" + smartbillPwd;

            String connection_url = smartbillUrl + "/callSB_V3/XXSB_DTI_ARISSUE.asp";

            connection_url += param;


            URL smartbillSever = null;
            try {
                smartbillSever = new URL(connection_url);
                HttpURLConnection con = (HttpURLConnection) smartbillSever.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                Map map = con.getHeaderFields();
                int responseCode = con.getResponseCode();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            String endMsg = "X";

            //Accounting
            for(int i=0; i<list.size(); i++){
                sb = new StringBuilder();

                sb.append(
                        "SELECT ETAX_ISSUE_ID, CUSTOMER_TRX_ID " +
                        "  FROM CBO_AR_TRX_MERGE " +
                        " WHERE DELETE_FLAG = 'N' " +
                        "   AND ETAX_ISSUE_ID = :etaxIssueId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("etaxIssueId", list.get(i).etaxIssueId);
                List<SalesTaxInvoiceDto> cboArMergeList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                for(int j=0; j<cboArMergeList.size(); j++){

                    StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM");
                    storedProcedureQuery.setParameter("p_customer_trx_id", cboArMergeList.get(j).customerTrxId.intValue());
                    storedProcedureQuery.execute();

                    String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");

                    if(!err_flag.equals("S")){
                        if("X".equals(endMsg)){
                            endMsg = "매출채권 Accounting 오류. 시스템 담당자에게 문의 바랍니다. CUSTOMER_TRX_ID : " + cboArMergeList.get(j).customerTrxId;
                        }else{
                            endMsg +=  ", " + cboArMergeList.get(j).customerTrxId;
                        }
                    }
                }
            }

            if(!"X".equals(endMsg)){
                result.put("Message", endMsg);
            }

            if(result.isEmpty()){
                result.put("Message", "발행되었습니다.");
            }

        }

        return result;
    }

    @Nullable @Transactional(propagation = Propagation.REQUIRES_NEW)
    Map<String, String> issueTaxInvoiceSub1(List<SalesTaxInvoiceDto> list, Map<String, String> result, List<SalesTaxInvoiceDto> corperInfo, BigDecimal newInterfaceBatchId) {
        Query query;
        StringBuilder sb;

        //여러건 for문
        for(int i = 0; i< list.size(); i++){
            //개인사용자
            if(list.get(i).byrComRegno.replaceAll("-", "").length() > 10){
                list.get(i).byrComRegno = list.get(i).byrComRegno.substring(list.get(i).byrComRegno.length() - 10, list.get(i).byrComRegno.length());
            }

            //발행여부 체크
            if(hasText(list.get(i).conversationId)){
                sb = new StringBuilder();

                sb.append(
                        "SELECT CONVERSATION_ID " +
                        "     , DTI_STATUS " +
                        "     , RETURN_CODE " +
                        "  FROM CBOTAX.XXSB_DTI_STATUS" +
                        " WHERE CONVERSATION_ID = :conversationId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("conversationId", list.get(i).conversationId);
                List<SalesTaxInvoiceDto> dtiStatusList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                if(dtiStatusList.size() != 1){
                    result.put("Message", "세금계산서 상태값 error");
                    return result;
                }else{
                    result.put("Message", "이미 발행한 건 입니다. 세금계산서 번호 : " + list.get(i).conversationId);
                    return result;
                }
            }

            if(!(("S".equals(list.get(i).dtiStatus) || !hasText(list.get(i).dtiStatus))
                    && ("37010".equals(list.get(i).returnCode) || "31079".equals(list.get(i).returnCode)  || !hasText(list.get(i).returnCode)))){
                result.put("Message", "발행 가능한 상태가 아닙니다.");
                return result;
            }

            //conversation id 채번
            String seq = cboArEtaxIssueRepository.getTaxSeq(util.getLoginCompCd());
            seq = seq.substring(seq.length()-4);

            String newConversationId = corperInfo.get(0).vatRegistrationNumNon
                                        + list.get(i).byrComRegno.replaceAll("-", "").replaceAll(" ", "")
                                        + list.get(i).dtiWdate.replaceAll("-", "")
                                        + seq
                                        + list.get(i).direction
                                        + "03";

            if(newConversationId.length() != 35){
                result.put("Message", "세금계산서ID 채번 오류");
                return result;
            }



            //Insert CBO_AR_ETAX_ISSUE
            CboArEtaxIssue inputCboArEtaxIssue = new CboArEtaxIssue();

            inputCboArEtaxIssue.etaxIssueId = new BigDecimal(list.get(i).etaxIssueId);
            inputCboArEtaxIssue.conversationId = newConversationId;
            inputCboArEtaxIssue.supbuyType = list.get(i).supbuyType;
            inputCboArEtaxIssue.direction = list.get(i).direction;
            inputCboArEtaxIssue.interfaceBatchId = newInterfaceBatchId;
            inputCboArEtaxIssue.dtiWdate = list.get(i).dtiWdate == null ? null :
                    LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                                     Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                                     Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
            inputCboArEtaxIssue.taxDemand = list.get(i).taxDemand;
            inputCboArEtaxIssue.itemName = list.get(i).itemName;
            inputCboArEtaxIssue.remark = list.get(i).remark;
            inputCboArEtaxIssue.byrTelNum = list.get(i).byrTelNum;
            inputCboArEtaxIssue.byrEmail = list.get(i).byrEmail;
            inputCboArEtaxIssue.issueEmpName = list.get(i).createEmpName;
            inputCboArEtaxIssue.issueEmpNo = list.get(i).createEmpNo;
            inputCboArEtaxIssue.issueEmpEmail = list.get(i).createEmpNo + "@iljin.co.kr";
            inputCboArEtaxIssue.issueEmpPhone = null;
            inputCboArEtaxIssue.createdPersonId = new BigDecimal(list.get(i).createdPersonId);
            inputCboArEtaxIssue.creationDate = LocalDateTime.now();
            inputCboArEtaxIssue.lastUpdatedPersonId = new BigDecimal(list.get(i).createdPersonId);
            inputCboArEtaxIssue.lastUpdateDate = LocalDateTime.now();
            inputCboArEtaxIssue.amendCode = list.get(i).amendCode;
            inputCboArEtaxIssue.oriIssueId = list.get(i).oriIssueId;
            inputCboArEtaxIssue.oriEtaxIssueId = list.get(i).oriEtaxIssueId;
            inputCboArEtaxIssue.oriDtiWdate = list.get(i).oriDtiWdate == null ? null :
                    LocalDateTime.of(Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(0, 4)),
                                     Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(4, 6)),
                                     Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
            inputCboArEtaxIssue.localLcOpenDate = list.get(i).localLcOpenDate == null ? null :
                    LocalDateTime.of(Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(0, 4)),
                                     Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(4, 6)),
                                     Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);

            cboArEtaxIssueRepository.save(inputCboArEtaxIssue);


            //Insert XXSB_DTI_MAIN
            XxsbDtiMain inputXxsbDtiMain = new XxsbDtiMain();

            inputXxsbDtiMain.conversationId = newConversationId;
            inputXxsbDtiMain.supbuyType = list.get(i).supbuyType;
            inputXxsbDtiMain.direction = list.get(i).direction;
            inputXxsbDtiMain.interfaceBatchId = newInterfaceBatchId;
            inputXxsbDtiMain.dtiWdate = list.get(i).dtiWdate == null ? null :
                    LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                                     Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                                     Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
            inputXxsbDtiMain.dtiType = list.get(i).dtiType;
            inputXxsbDtiMain.taxDemand = list.get(i).taxDemand;
            inputXxsbDtiMain.supComId = smartbillId;
            inputXxsbDtiMain.supComRegno = corperInfo.get(0).vatRegistrationNumNon;
            inputXxsbDtiMain.supRepName = corperInfo.get(0).representativeName;
            inputXxsbDtiMain.supComName = corperInfo.get(0).corporationName;
            inputXxsbDtiMain.supComType = corperInfo.get(0).custBusinessCondition;
            inputXxsbDtiMain.supComClassify = corperInfo.get(0).custBusinessType;
            inputXxsbDtiMain.supComAddr = corperInfo.get(0).address;
            inputXxsbDtiMain.supDeptName = list.get(i).createDeptCode;
            inputXxsbDtiMain.supEmpName = list.get(i).createEmpNo;
            inputXxsbDtiMain.supTelNum = null;
            inputXxsbDtiMain.supEmail = list.get(i).createEmpNo + "@iljin.co.kr";
            inputXxsbDtiMain.byrComId = "";
            inputXxsbDtiMain.byrComRegno = list.get(i).byrComRegno.replaceAll("-", "");
            inputXxsbDtiMain.byrRepName = list.get(i).byrRepName;
            inputXxsbDtiMain.byrComName = list.get(i).customerName;
            inputXxsbDtiMain.byrComType = list.get(i).byrComType;
            inputXxsbDtiMain.byrComClassify = list.get(i).byrComClassify;
            inputXxsbDtiMain.byrComAddr = list.get(i).byrComAddr;
            inputXxsbDtiMain.byrDeptName = "";
            inputXxsbDtiMain.byrEmpName = "";
            inputXxsbDtiMain.byrTelNum = list.get(i).byrTelNum;
            inputXxsbDtiMain.byrEmail = list.get(i).byrEmail;
            inputXxsbDtiMain.supAmount = list.get(i).supplyAmount;
            inputXxsbDtiMain.taxAmount = list.get(i).taxAmount;
            inputXxsbDtiMain.totalAmount = list.get(i).totalAmount;
            inputXxsbDtiMain.dttYn = "N";
            inputXxsbDtiMain.remark = list.get(i).remark;
            inputXxsbDtiMain.createdBy = list.get(i).createEmpNo;
            inputXxsbDtiMain.creationDate = LocalDateTime.now();
            inputXxsbDtiMain.amendCode = list.get(i).amendCode;
            inputXxsbDtiMain.byrBizplaceCode = list.get(i).byrBizplaceCode;

            xxsbDtiMainRepository.save(inputXxsbDtiMain);


            //Insert XXSB_DTI_ITEM
            List<SalesTaxInvoiceDto> insertXxsbDtiItem = salesTaxInvoiceRepository.getItem(Integer.valueOf(list.get(i).etaxIssueId));

            for(int j=0; j<insertXxsbDtiItem.size(); j++){
                XxsbDtiItem inputXxsbDtiItem = new XxsbDtiItem();

                inputXxsbDtiItem.conversationId = newConversationId;
                inputXxsbDtiItem.supbuyType = list.get(i).supbuyType;
                inputXxsbDtiItem.direction = list.get(i).direction;
                inputXxsbDtiItem.dtiLineNum = insertXxsbDtiItem.get(j).dtiLineNum;
                inputXxsbDtiItem.itemName = insertXxsbDtiItem.get(j).itemName;
                inputXxsbDtiItem.itemMd = list.get(i).dtiWdate == null ? null :
                        LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                                         Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                                         Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)),
                                                            0, 0, 0);
                inputXxsbDtiItem.unitPrice = insertXxsbDtiItem.get(j).unitPrice;
                inputXxsbDtiItem.itemQty = insertXxsbDtiItem.get(j).itemQty;
                inputXxsbDtiItem.supAmount = insertXxsbDtiItem.get(j).supplyAmount;
                inputXxsbDtiItem.taxAmount = insertXxsbDtiItem.get(j).taxAmount;
                inputXxsbDtiItem.remark = list.get(i).remark;
                inputXxsbDtiItem.createdBy = list.get(i).createEmpNo;
                inputXxsbDtiItem.creationDate = LocalDateTime.now();

                xxsbDtiItemRepository.save(inputXxsbDtiItem);
            }

            //Insert XXSB_DTI_STATUS
            XxsbDtiStatus inputXxsbDtiStatus = new XxsbDtiStatus();

            inputXxsbDtiStatus.conversationId = newConversationId;
            inputXxsbDtiStatus.supbuyType = list.get(i).supbuyType;
            inputXxsbDtiStatus.direction = list.get(i).direction;
            inputXxsbDtiStatus.dtiStatus = "S";

            xxsbDtiStatusRepository.save(inputXxsbDtiStatus);

            //Insert XXSB_DTI_INVOICE
            sb = new StringBuilder();

            sb.append(
                    "SELECT RCTA.TRX_NUMBER " +
                    "     , RCTA.CUSTOMER_TRX_ID " +
                    "  FROM CBO_AR_TRX_MERGE CATM " +
                    "     , AR.RA_CUSTOMER_TRX_ALL RCTA " +
                    " WHERE CATM.ETAX_ISSUE_ID = :etaxIssueId " +
                    "   AND CATM.CUSTOMER_TRX_ID = RCTA.CUSTOMER_TRX_ID " +
                    "   AND CATM.DELETE_FLAG = 'N' " +
                    " ORDER BY RCTA.CUSTOMER_TRX_ID "
            );

            query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("etaxIssueId", list.get(i).etaxIssueId);
            List<SalesTaxInvoiceDto> dtiInvoiceList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

            for(int j=0; j<dtiInvoiceList.size(); j++){
                XxsbDtiInvoice xxsbDtiInvoice = new XxsbDtiInvoice();

                xxsbDtiInvoice.conversationId = newConversationId;
                xxsbDtiInvoice.supbuyType = list.get(i).supbuyType;
                xxsbDtiInvoice.direction = list.get(i).direction;
                xxsbDtiInvoice.invoiceIdx = String.valueOf(j+1);
                xxsbDtiInvoice.invoiceId = dtiInvoiceList.get(j).customerTrxId;
                xxsbDtiInvoice.invoiceNum = dtiInvoiceList.get(j).trxNumber;

                xxsbDtiInvoiceRepository.save(xxsbDtiInvoice);
            }
        }
        return result;
    }


    @Override
    public Map<String, String> reIssueTaxInvoice(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        //회사 정보 조회
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                        "     , VAT_REGISTRATION_NUM_NON " +
                        "     , REPRESENTATIVE_NAME " +
                        "     , CUST_BUSINESS_CONDITION " +
                        "     , CUST_BUSINESS_TYPE " +
                        "     , CORPORATION_NAME " +
                        "     , ADDRESS " +
                        "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                        " WHERE ORG_ID = :orgId"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", list.get(0).orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{

            for(int i=0; i<list.size(); i++){
                if(list.get(i).direction.equals("1")){
                    result.put("Message", "역발행은 [역발행저장] 버튼으로 처리 하시기 바랍니다.");
                    return result;
                }
            }


            //interface batch id 채번
            sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_IF_BATCH_S.NEXTVAL AS NEW_INTERFACE_BATCH_ID, 'temp' AS temp" +
                            "  FROM DUAL "
            );

            query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newInterfaceBatchId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId;

            //여러건 for문
            for(int i=0; i<list.size(); i++){
                //개인사용자
                if(list.get(i).byrComRegno.replaceAll("-", "").length() > 10){
                    list.get(i).byrComRegno = list.get(i).byrComRegno.substring(list.get(i).byrComRegno.length() - 10, list.get(i).byrComRegno.length());
                }

                //발행여부 체크
                if(hasText(list.get(i).conversationId)){
                    sb = new StringBuilder();

                    sb.append(
                            "SELECT CONVERSATION_ID " +
                                    "     , DTI_STATUS " +
                                    "     , RETURN_CODE " +
                                    "  FROM CBOTAX.XXSB_DTI_STATUS" +
                                    " WHERE CONVERSATION_ID = :conversationId "
                    );

                    query = entityManager.createNativeQuery(sb.toString());
                    query.setParameter("conversationId", list.get(i).conversationId);

                    List<SalesTaxInvoiceDto> dtiStatusList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                    if(dtiStatusList.size() != 1){
                        result.put("Message", "세금계산서 상태값 error");
                        return result;
                    }
                }

                reIssueTaxInvoiceSub(list, newInterfaceBatchId, i);
            }

            //Smartbill API call
            String param = "?BATCH_ID=" + newInterfaceBatchId;
            param += "&ID=" + smartbillId;
            param += "&PASS=" + smartbillPwd;

            String connection_url = smartbillUrl + "/callSB_V3/XXSB_DTI_ARISSUE.asp";

            connection_url += param;

            URL smartbillSever = null;
            try {
                smartbillSever = new URL(connection_url);
                HttpURLConnection con = (HttpURLConnection) smartbillSever.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                Map map = con.getHeaderFields();
                int responseCode = con.getResponseCode();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            String endMsg = "X";

            //Accounting
            for(int i=0; i<list.size(); i++){
                sb = new StringBuilder();

                sb.append(
                        "SELECT ETAX_ISSUE_ID, CUSTOMER_TRX_ID " +
                                "  FROM CBO_AR_TRX_MERGE " +
                                " WHERE DELETE_FLAG = 'N' " +
                                "   AND ETAX_ISSUE_ID = :etaxIssueId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("etaxIssueId", list.get(i).etaxIssueId);
                List<SalesTaxInvoiceDto> cboArMergeList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                for(int j=0; j<cboArMergeList.size(); j++){

                    StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM");
                    storedProcedureQuery.setParameter("p_customer_trx_id", cboArMergeList.get(j).customerTrxId.intValue());
                    storedProcedureQuery.execute();

                    String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");

                    if(!err_flag.equals("S")){
                        if("X".equals(endMsg)){
                            endMsg = "매출채권 Accounting 오류. 시스템 담당자에게 문의 바랍니다. CUSTOMER_TRX_ID : " + cboArMergeList.get(j).customerTrxId;
                        }else{
                            endMsg +=  ", " + cboArMergeList.get(j).customerTrxId;
                        }
                    }
                }
            }

            if(!"X".equals(endMsg)){
                result.put("Message", endMsg);
            }

            if(result.isEmpty()){
                result.put("Message", "재발행되었습니다.");
            }
        }


        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void reIssueTaxInvoiceSub(List<SalesTaxInvoiceDto> list, BigDecimal newInterfaceBatchId, int i) {
        //update CBO_AR_ETAX_ISSUE
        cboArEtaxIssueRepository.updateInterfaceBatchIdAndLastUpdatedPersonIdAndLastUpdateDateByEtaxIssueIdAndConversationId(
                newInterfaceBatchId,
                new BigDecimal(list.get(i).createdPersonId),
                LocalDateTime.now(),
                new BigDecimal(list.get(i).etaxIssueId),
                list.get(i).conversationId
        );

        //update XXSB_DTI_MAIN
        xxsbDtiMainRepository.updateInterfaceBatchIdByConversationId(
                newInterfaceBatchId,
                list.get(i).conversationId
        );
    }

    @Override
    public Map<String, String> cancelIssueTaxInvoice(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        //회사 정보 조회
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                        "     , VAT_REGISTRATION_NUM_NON " +
                        "     , REPRESENTATIVE_NAME " +
                        "     , CUST_BUSINESS_CONDITION " +
                        "     , CUST_BUSINESS_TYPE " +
                        "     , CORPORATION_NAME " +
                        "     , ADDRESS " +
                        "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                        " WHERE ORG_ID = :orgId"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", list.get(0).orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{

            for(int i=0; i<list.size(); i++){
                if(list.get(i).direction.equals("1")){
                    result.put("Message", "역발행은 [역발행저장] 버튼으로 처리 하시기 바랍니다.");
                    return result;
                }
            }


            //interface batch id 채번
            sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_IF_BATCH_S.NEXTVAL AS NEW_INTERFACE_BATCH_ID, 'temp' AS temp" +
                            "  FROM DUAL "
            );

            query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newInterfaceBatchId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId;

            //Main 작업 프로시저
            result = cancelIssueTaxInvoiceSub(list, result, newInterfaceBatchId);
            if (!result.isEmpty()) return result;

            //Smartbill API call
            String param = "?BATCH_ID=" + newInterfaceBatchId;
            param += "&STATUS=O&SIGNAL=CANCELALL";
            param += "&ID=" + smartbillId;
            param += "&PASS=" + smartbillPwd;

            String connection_url = smartbillUrl + "/callSB_V3/XXSB_DTI_STATUS_CHANGE.asp";

            connection_url += param;

            URL smartbillSever = null;
            try {
                smartbillSever = new URL(connection_url);
                HttpURLConnection con = (HttpURLConnection) smartbillSever.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                Map map = con.getHeaderFields();
                int responseCode = con.getResponseCode();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

        return result;
    }

    @Nullable @Transactional(propagation = Propagation.REQUIRES_NEW)
    Map<String, String> cancelIssueTaxInvoiceSub(List<SalesTaxInvoiceDto> list, Map<String, String> result, BigDecimal newInterfaceBatchId) {
        Query query;
        StringBuilder sb;
        //여러건 for문
        for(int i = 0; i< list.size(); i++){
            //개인사용자
            if(list.get(i).byrComRegno.replaceAll("-", "").length() > 10){
                list.get(i).byrComRegno = list.get(i).byrComRegno.substring(list.get(i).byrComRegno.length() - 10, list.get(i).byrComRegno.length());
            }

            //발행여부 체크
            if(hasText(list.get(i).conversationId)){
                sb = new StringBuilder();

                sb.append(
                        "SELECT CONVERSATION_ID " +
                                "     , DTI_STATUS " +
                                "     , RETURN_CODE " +
                                "  FROM CBOTAX.XXSB_DTI_STATUS" +
                                " WHERE CONVERSATION_ID = :conversationId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("conversationId", list.get(i).conversationId);
                List<SalesTaxInvoiceDto> dtiStatusList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                if(dtiStatusList.size() != 1){
                    result.put("Message", "세금계산서 상태값 error");
                    return result;
                }
            }

            //update CBO_AR_ETAX_ISSUE
            cboArEtaxIssueRepository.updateInterfaceBatchIdAndLastUpdatedPersonIdAndLastUpdateDateByEtaxIssueIdAndConversationId(
                    newInterfaceBatchId,
                    new BigDecimal(list.get(i).createdPersonId),
                    LocalDateTime.now(),
                    new BigDecimal(list.get(i).etaxIssueId),
                    list.get(i).conversationId
            );

            //update XXSB_DTI_MAIN
            xxsbDtiMainRepository.updateInterfaceBatchIdByConversationId(
                    newInterfaceBatchId,
                    list.get(i).conversationId
            );
        }
        return result;
    }


    @Override
    public Map<String, String> emailReSend(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        //회사 정보 조회
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                        "     , VAT_REGISTRATION_NUM_NON " +
                        "     , REPRESENTATIVE_NAME " +
                        "     , CUST_BUSINESS_CONDITION " +
                        "     , CUST_BUSINESS_TYPE " +
                        "     , CORPORATION_NAME " +
                        "     , ADDRESS " +
                        "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                        " WHERE ORG_ID = :orgId"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", list.get(0).orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{

            for(int i=0; i<list.size(); i++){
                if(list.get(i).direction.equals("1")){
                    result.put("Message", "역발행은 [역발행저장] 버튼으로 처리 하시기 바랍니다.");
                    return result;
                }
            }


            //여러건 for문
            for(int i=0; i<list.size(); i++){
                //개인사용자
                if(list.get(i).byrComRegno.replaceAll("-", "").length() > 10){
                    list.get(i).byrComRegno = list.get(i).byrComRegno.substring(list.get(i).byrComRegno.length() - 10, list.get(i).byrComRegno.length());
                }

                //발행여부 체크
                if(hasText(list.get(i).conversationId)){
                    sb = new StringBuilder();

                    sb.append(
                            "SELECT CONVERSATION_ID " +
                                    "     , DTI_STATUS " +
                                    "     , RETURN_CODE " +
                                    "  FROM CBOTAX.XXSB_DTI_STATUS" +
                                    " WHERE CONVERSATION_ID = :conversationId "
                    );

                    query = entityManager.createNativeQuery(sb.toString());
                    query.setParameter("conversationId", list.get(i).conversationId);
                    List<SalesTaxInvoiceDto> dtiStatusList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                    if(dtiStatusList.size() != 1){
                        result.put("Message", "세금계산서 상태값 error");
                        return result;
                    }
                }

                if(!("I".equals(list.get(i).dtiStatus) || "C".equals(list.get(i).dtiStatus) || "V".equals(list.get(i).dtiStatus) || "N".equals(list.get(i).dtiStatus))){
                    result.put("Message", "메일 발송 가능 상태가 아닙니다.");
                    return result;
                }

                //transaction 반영을 위한 분리
                updateEmailMethod(list, i);

                //Smartbill API call
                String param = "?CONVERSATION_ID=" + list.get(i).conversationId;
                param += "&EMAIL=" + list.get(i).byrEmail;
                param += "&ID=" + smartbillId;
                param += "&PASS=" + smartbillPwd;
                param += "&STATUS=" + list.get(i).dtiStatus;

                String connection_url = smartbillUrl + "/callSB_V3/XXSB_DTI_SEND_EMAIL.asp";

                connection_url += param;

                URL smartbillSever = null;
                try {
                    smartbillSever = new URL(connection_url);
                    HttpURLConnection con = (HttpURLConnection) smartbillSever.openConnection();
                    con.setRequestMethod("GET");
                    con.connect();

                    Map map = con.getHeaderFields();
                    int responseCode = con.getResponseCode();
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (ProtocolException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }

        }

        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateEmailMethod(List<SalesTaxInvoiceDto> list, int i) {
        cboArEtaxIssueRepository.updateForEmailReSend(
                list.get(i).byrEmail, new BigDecimal(list.get(i).createdPersonId), LocalDateTime.now(), new BigDecimal(list.get(i).etaxIssueId), list.get(i).conversationId
        );
    }


    @Override
    public Map<String, String> reverseSave(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();

        //interface batch id 채번
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT CBOSLIP.CBO_AR_ETAX_IF_BATCH_S.NEXTVAL AS NEW_INTERFACE_BATCH_ID, 'temp' AS temp" +
                        "  FROM DUAL "
        );

        Query query = entityManager.createNativeQuery(sb.toString());

        BigDecimal newInterfaceBatchId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId;

        //회사 정보 조회
        sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                        "     , VAT_REGISTRATION_NUM_NON " +
                        "     , REPRESENTATIVE_NAME " +
                        "     , CUST_BUSINESS_CONDITION " +
                        "     , CUST_BUSINESS_TYPE " +
                        "     , CORPORATION_NAME " +
                        "     , ADDRESS " +
                        "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                        " WHERE ORG_ID = :orgId"
        );

        query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", list.get(0).orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{

            for(int i=0; i<list.size(); i++){
                if(!list.get(i).direction.equals("1")){
                    result.put("Message", "역발행만 저장 가능합니다.");
                    return result;
                }
            }


            //여러건 for문
            for(int i=0; i<list.size(); i++){
                //개인사용자
                if(list.get(i).byrComRegno.replaceAll("-", "").length() > 10){
                    list.get(i).byrComRegno = list.get(i).byrComRegno.substring(list.get(i).byrComRegno.length() - 10, list.get(i).byrComRegno.length());
                }

                String seq = cboArEtaxIssueRepository.getTaxSeq(util.getLoginCompCd());
                seq = seq.substring(seq.length()-4);

                String newConversationId = corperInfo.get(0).vatRegistrationNumNon
                        + list.get(i).byrComRegno.replaceAll("-", "").replaceAll(" ", "")
                        + list.get(i).dtiWdate.replaceAll("-", "")
                        + seq
                        + list.get(i).direction
                        + "03";

                if(newConversationId.length() != 35){
                    result.put("Message", "세금계산서ID 채번 오류");
                    return result;
                }


                //Insert CBO_AR_ETAX_ISSUE
                CboArEtaxIssue inputCboArEtaxIssue = new CboArEtaxIssue();

                inputCboArEtaxIssue.etaxIssueId = new BigDecimal(list.get(i).etaxIssueId);
                inputCboArEtaxIssue.conversationId = newConversationId;
                inputCboArEtaxIssue.supbuyType = list.get(i).supbuyType;
                inputCboArEtaxIssue.direction = list.get(i).direction;
                inputCboArEtaxIssue.interfaceBatchId = newInterfaceBatchId;
                inputCboArEtaxIssue.dtiWdate = list.get(i).dtiWdate == null ? null :
                        LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                                Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                                Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
                inputCboArEtaxIssue.taxDemand = list.get(i).taxDemand;
                inputCboArEtaxIssue.itemName = list.get(i).itemName;
                inputCboArEtaxIssue.remark = list.get(i).remark;
                inputCboArEtaxIssue.byrTelNum = list.get(i).byrTelNum;
                inputCboArEtaxIssue.byrEmail = list.get(i).byrEmail;
                inputCboArEtaxIssue.issueEmpName = list.get(i).createEmpName;
                inputCboArEtaxIssue.issueEmpNo = list.get(i).createEmpNo;
                inputCboArEtaxIssue.issueEmpEmail = list.get(i).createEmpNo + "@iljin.co.kr";
                inputCboArEtaxIssue.issueEmpPhone = null;
                inputCboArEtaxIssue.createdPersonId = new BigDecimal(list.get(i).createdPersonId);
                inputCboArEtaxIssue.creationDate = LocalDateTime.now();
                inputCboArEtaxIssue.lastUpdatedPersonId = new BigDecimal(list.get(i).createdPersonId);
                inputCboArEtaxIssue.lastUpdateDate = LocalDateTime.now();
                inputCboArEtaxIssue.amendCode = list.get(i).amendCode;
                inputCboArEtaxIssue.oriIssueId = list.get(i).oriIssueId;
                inputCboArEtaxIssue.oriEtaxIssueId = list.get(i).oriEtaxIssueId;
                inputCboArEtaxIssue.oriDtiWdate = list.get(i).oriDtiWdate == null ? null :
                        LocalDateTime.of(Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(0, 4)),
                                Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(4, 6)),
                                Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
                inputCboArEtaxIssue.localLcOpenDate = list.get(i).localLcOpenDate == null ? null :
                        LocalDateTime.of(Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(0, 4)),
                                Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(4, 6)),
                                Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);

                cboArEtaxIssueRepository.save(inputCboArEtaxIssue);
            }

            String endMsg = "X";

            //Accounting
            for(int i=0; i<list.size(); i++){
                sb = new StringBuilder();

                sb.append(
                        "SELECT ETAX_ISSUE_ID, CUSTOMER_TRX_ID " +
                                "  FROM CBO_AR_TRX_MERGE " +
                                " WHERE DELETE_FLAG = 'N' " +
                                "   AND ETAX_ISSUE_ID = :etaxIssueId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("etaxIssueId", list.get(i).etaxIssueId);
                List<SalesTaxInvoiceDto> cboArMergeList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                for(int j=0; j<cboArMergeList.size(); j++){

                    StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM");
                    storedProcedureQuery.setParameter("p_customer_trx_id", cboArMergeList.get(j).customerTrxId.intValue());
                    storedProcedureQuery.execute();

                    String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");

                    if(!err_flag.equals("S")){
                        if("X".equals(endMsg)){
                            endMsg = "매출채권 Accounting 오류. 시스템 담당자에게 문의 바랍니다. CUSTOMER_TRX_ID : " + cboArMergeList.get(j).customerTrxId;
                        }else{
                            endMsg +=  ", " + cboArMergeList.get(j).customerTrxId;
                        }
                    }
                }
            }

            if(!"X".equals(endMsg)){
                result.put("Message", endMsg);
            }

        }

        return result;
    }


    @Override
    public Map<String, String> reverseCancel(List<SalesTaxInvoiceDto> list){
        Map<String, String> result = new HashMap<>();


        for(int i=0; i<list.size(); i++){
            if(!list.get(i).direction.equals("1")){
                result.put("Message", "역발행만 취소 가능합니다.");
                return result;
            }
        }

        for(int i=0; i<list.size(); i++) {
            StringBuilder sb = new StringBuilder();

            sb.append(
                    "SELECT COUNT(*) AS CNT, 'temp' AS temp" +
                            "  FROM APPS.XXSB_DTI_INVOICE " +
                            " WHERE 1 = 1 " +
                            "   AND Invoice_Id in (SELECT CUSTOMER_TRX_ID FROM CBO_AR_TRX_MERGE WHERE ETAX_ISSUE_ID = :etaxIssueId ) "
            );

            Query query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("etaxIssueId", list.get(i).etaxIssueId);

            BigDecimal cnt = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId; //임시로 DTO 사용

            if(cnt.intValue() == 0){
                cboArEtaxIssueRepository.deleteByEtaxIssueId(new BigDecimal(list.get(i).etaxIssueId));
            }else{
                result.put("Message", list.get(i).etaxIssueId + " - 역발행 취소를 진행할 수 없는 상태입니다.");
                break;
            }

        }

        return result;
    }


    @Override
    public List<SalesTaxInvoiceDto> getModifyList(SalesTaxInvoiceDto salesTaxInvoiceDto){
        return salesTaxInvoiceRepository.getModifyList(salesTaxInvoiceDto);
    }

    @Override
    public Map<String, String> etaxModify(List<SalesTaxInvoiceDto> list) throws Exception{
        Map<String, String> result = new HashMap<>();

        //회사 정보 조회
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                        "     , VAT_REGISTRATION_NUM_NON " +
                        "     , REPRESENTATIVE_NAME " +
                        "     , CUST_BUSINESS_CONDITION " +
                        "     , CUST_BUSINESS_TYPE " +
                        "     , CORPORATION_NAME " +
                        "     , ADDRESS " +
                        "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                        " WHERE ORG_ID = :orgId"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", list.get(0).orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{

            for(int i=0; i<list.size(); i++){
                if(list.get(i).direction.equals("1")){
                    result.put("Message", "역발행은 [역발행저장] 버튼으로 처리 하시기 바랍니다.");
                    return result;
                }
            }


            //interface batch id 채번
            sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_IF_BATCH_S.NEXTVAL AS NEW_INTERFACE_BATCH_ID, 'temp' AS temp" +
                            "  FROM DUAL "
            );

            query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newInterfaceBatchId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId;

            //여러건 for문
            for(int i=0; i<list.size(); i++){
                //개인사용자
                if(list.get(i).byrComRegno.replaceAll("-", "").length() > 10){
                    list.get(i).byrComRegno = list.get(i).byrComRegno.substring(list.get(i).byrComRegno.length() - 10, list.get(i).byrComRegno.length());
                }

                //발행여부 체크
                if(hasText(list.get(i).conversationId)){
                    sb = new StringBuilder();

                    sb.append(
                            "SELECT CONVERSATION_ID " +
                                    "     , DTI_STATUS " +
                                    "     , RETURN_CODE " +
                                    "  FROM CBOTAX.XXSB_DTI_STATUS" +
                                    " WHERE CONVERSATION_ID = :conversationId "
                    );

                    query = entityManager.createNativeQuery(sb.toString());
                    query.setParameter("conversationId", list.get(i).conversationId);
                    List<SalesTaxInvoiceDto> dtiStatusList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                    if(dtiStatusList.size() != 1){
                        result.put("Message", "세금계산서 상태값 error");
                        return result;
                    }else{
                        result.put("Message", "이미 발행한 건 입니다. 세금계산서 번호 : " + list.get(i).conversationId);
                        return result;
                    }
                }

                if(!(("S".equals(list.get(i).dtiStatus) || !hasText(list.get(i).dtiStatus))
                        && ("37010".equals(list.get(i).returnCode) || "31079".equals(list.get(i).returnCode)  || !hasText(list.get(i).returnCode)))){
                    result.put("Message", "발행 가능한 상태가 아닙니다.");
                    return result;
                }

                //conversation id 채번
                String seq = cboArEtaxIssueRepository.getTaxSeq(util.getLoginCompCd());
                seq = seq.substring(seq.length()-4);

                String newConversationId = corperInfo.get(0).vatRegistrationNumNon
                        + list.get(i).byrComRegno.replaceAll("-", "").replaceAll(" ", "")
                        + list.get(i).dtiWdate.replaceAll("-", "")
                        + seq
                        + list.get(i).direction
                        + "03";

                if(newConversationId.length() != 35){
                    result.put("Message", "세금계산서ID 채번 오류");
                    return result;
                }

                //세금계산서 정보 조회
                List<SalesTaxInvoiceDto> modifyList = salesTaxInvoiceRepository.getModifyInfo(list.get(i).orgId, list.get(i).oriIssueId);

                //수정세금계산서 검증
                if(modifyList.size() != 1){
                    throw new Exception("원본세금계산서 정보가 잘못 됐습니다. approve_id : " + list.get(i).oriIssueId);
                }

                if(list.get(i).amendCode.equals("02")           //공급가액 변동동
                       || list.get(i).amendCode.equals("03")   //환입
                        || list.get(i).amendCode.equals("04")   //계약의 해제
                        || list.get(i).amendCode.equals("06")   //착오에의한 이중발급
                ){
                    if(!list.get(i).taxCode.equals(modifyList.get(0).taxCode)){
                        throw new Exception("원천 세금계산서와 수정세금계산서의 세금코드가 불일치 합니다.");
                    }
                }

                if(list.get(i).amendCode.equals("03")   //환입
                        || list.get(i).amendCode.equals("04")   //계약의 해제
                        || list.get(i).amendCode.equals("06")   //착오에의한 이중발급
                ){
                    if(list.get(i).totalAmount.intValue() > 0){
                        throw new Exception("수정코드가 [03.환입, 04.계약의 해제, 06.착오에의한 이중발급]일 경우 수정세금계산서의 금액이 음수여야 합니다.  원승인번호 : " + list.get(i).oriIssueId);
                    }
                }

                if(list.get(i).amendCode.equals("03")) {   //환입
                    if(list.get(i).totalAmount.intValue() + modifyList.get(0).totalAmount.intValue() < 0){
                        throw new Exception("수정코드가 [03.환입] 일 경우 원천세금계산서의 금액보다 많은 금액을 환입할 수 없습니다.");
                    }
                }

                if(list.get(i).amendCode.equals("04")   //계약의 해제
                        || list.get(i).amendCode.equals("06")   //착오에의한 이중발급
                ){
                    if((list.get(i).totalAmount.intValue() + modifyList.get(0).totalAmount.intValue()) != 0){
                        throw new Exception("수정코드가 [04.계약의 해제, 06.착오에의한 이중발급]일 경우 원천세금계산서의 금액과 수정세금계산서 금액의 절대값이 같아야 합니다. 원승인번호 : " + list.get(i).oriIssueId);
                    }
                }

                if(list.get(i).amendCode.equals("01")   //기재사항 착오정정
                        || list.get(i).amendCode.equals("05")   //내국신용장 사후개설
                        || list.get(i).amendCode.equals("06")   //착오에의한 이중발급
                ){
                    list.get(i).dtiWdate = list.get(i).oriDtiWdate.replaceAll("-", "");
                }

                list.get(i).remark = "[당초 승인번호 : " + list.get(i).oriIssueId + "] ";

                if(list.get(i).amendCode.equals("02")   //공급가액 변동
                        || list.get(i).amendCode.equals("03")   //환입
                        || list.get(i).amendCode.equals("04")   //계약의 해제
                ) {
                    list.get(i).remark += ", [당초 세금계산서 작성일 : " + list.get(i).oriDtiWdate + "]";
                }

                //transaction 반영을 위한 분리
                sb = etaxModifySub(list, corperInfo, newInterfaceBatchId, i, newConversationId);
            }


            //01.기재사항 착오정정, 05.내국신용장 사후개설
            ///validation check1
            sb = new StringBuilder();

            sb.append(
                    "SELECT COUNT(ORI_ISSUE_ID) AS CNT " +
                    "     , ORI_ISSUE_ID " +
                    "     , AMEND_CODE " +
                    "     , 'temp' AS TEMP " +
                    "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V " +
                    " WHERE INTERFACE_BATCH_ID = :newInterfaceBatchId " +
                    "   AND AMEND_CODE IN ('05','01') " +
                    " GROUP BY ORI_ISSUE_ID, AMEND_CODE "
            );

            query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("newInterfaceBatchId", newInterfaceBatchId);
            List<SalesTaxInvoiceDto> valCheck1 = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

            for(int i=0; i<valCheck1.size(); i++){
                if(valCheck1.get(i).count.intValue() != 2){
                    throw new Exception("수정코드가 [01.기재사항 착오정정, 05.내국신용장 사후개설]일 경우 동일한 원천세금계산서를 갖는 수정세금계산서가 2장 이여야 합니다. 원승인번호 : " + valCheck1.get(i).oriIssueId);
                }
            }

            ///validation check2
            sb = new StringBuilder();

            sb.append(
                    "SELECT ETAX_ISSUE_ID " +
                    "     , INTERFACE_BATCH_ID " +
                    "     , ORI_ISSUE_ID " +
                    "     , TOTAL_AMOUNT " +
                    "     , 'temp' AS TAX_CODE " +
                    "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V " +
                    " WHERE  INTERFACE_BATCH_ID = :newInterfaceBatchId " +
                    "   AND AMEND_CODE IN ('05','01') " +
                    "   AND TO_NUMBER(TOTAL_AMOUNT) < 0 " +
                    " ORDER BY ORI_ISSUE_ID, CONVERSATION_ID "
            );

            query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("newInterfaceBatchId", newInterfaceBatchId);
            List<SalesTaxInvoiceDto> valCheck2 = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

            for(int i=0; i<valCheck2.size(); i++){
                sb.append(
                        "SELECT TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE " +
                        "     , MV.TOTAL_AMOUNT " +
                        "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V MV " +
                        " WHERE MV.ORG_ID = :orgId " +
                        "   AND MV.APPROVE_ID = :oriIssueId " +
                        " UNION ALL " +
                        "SELECT TO_CHAR(MV.DTI_WDATE, 'YYYY-MM-DD') AS DTI_WDATE " +
                        "  FROM cbo_ar_etax_old_dti_main MV " +
                        " WHERE MV.ORG_ID = :orgId " +
                        " AND MV.APPROVE_ID = :oriIssueId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("orgId", list.get(0).orgId);
                query.setParameter("oriIssueId", valCheck2.get(i).oriIssueId);
                List<SalesTaxInvoiceDto> valCheck2Sub = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);


                if(valCheck2Sub.size() != 1){
                    throw new Exception("원본세금계산서 정보가 잘못 됐습니다. 원승인번호 : " + valCheck2.get(i).oriIssueId);
                }

                //(String trxNumber, BigDecimal customerTrxId) 임시로 DTO 사용 - 실제 데이터(DTI_WDATE, TOTAL_AMOUNT)
                if(valCheck2.get(i).totalAmount.intValue() + valCheck2Sub.get(i).customerTrxId.intValue() != 0){
                    throw new Exception("수정코드가 [01.기재사항 착오정정, 05.내국신용장 사후개설]일 경우 원천세금계산서의 금액과 수정세금계산서 금액의 절대값이 같아야 합니다. 원승인번호 : " + valCheck2.get(i).oriIssueId);
                }
            }

            if(valCheck1.size() != valCheck2.size()){
                throw new Exception("수정코드가 [01.기재사항 착오정정]일 경우 부(-)의 세금계산서 1장과 정확한 세금계산서 1장, [05.내국신용장 사후개설]일 경우 부(-)의 세금계산서 1장과 영세율 세금계산서 1장을 발행 해야 합니다.");
            }

            ///validation check3
            sb = new StringBuilder();

            sb.append(
                    "SELECT ETAX_ISSUE_ID " +
                    "     , INTERFACE_BATCH_ID " +
                    "     , ORI_ISSUE_ID " +
                    "     , TOTAL_AMOUNT " +
                    "     , TAX_CODE " +
                    "  FROM APPS.CBO_AR_ETAX_DTI_MAIN_V " +
                    " WHERE INTERFACE_BATCH_ID = :newInterfaceBatchId " +
                    "   AND AMEND_CODE IN ('05') " +
                    "   AND TO_NUMBER(TOTAL_AMOUNT) >= 0 " +
                    " ORDER BY ORI_ISSUE_ID, CONVERSATION_ID "
            );

            query = entityManager.createNativeQuery(sb.toString());
            query.setParameter("newInterfaceBatchId", newInterfaceBatchId);
            List<SalesTaxInvoiceDto> valCheck3 = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

            for(int i=0; i<valCheck3.size(); i++){
                if(valCheck3.get(i).taxCode.indexOf("로컬") < 0){
                    throw new Exception("[05.내국신용장 사후개설]일 경우 세금코드는 일반과 로컬이 존재해야합니다.");
                }
            }



            //Smartbill API call
            String param = "?BATCH_ID=" + newInterfaceBatchId;
            param += "&ID=" + smartbillId;
            param += "&PASS=" + smartbillPwd;

            String connection_url = smartbillUrl + "/callSB_V3/XXSB_DTI_ARISSUE.asp";

            connection_url += param;

            URL smartbillSever = new URL(connection_url);
            HttpURLConnection con = (HttpURLConnection) smartbillSever.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            Map map = con.getHeaderFields();
            int responseCode = con.getResponseCode();

            String endMsg = "X";

            //Accounting
            for(int i=0; i<list.size(); i++){
                sb = new StringBuilder();

                sb.append(
                        "SELECT ETAX_ISSUE_ID, CUSTOMER_TRX_ID " +
                                "  FROM CBO_AR_TRX_MERGE " +
                                " WHERE DELETE_FLAG = 'N' " +
                                "   AND ETAX_ISSUE_ID = :etaxIssueId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("etaxIssueId", list.get(i).etaxIssueId);
                List<SalesTaxInvoiceDto> cboArMergeList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                for(int j=0; j<cboArMergeList.size(); j++){

                    StoredProcedureQuery storedProcedureQuery = entityManager.createNamedStoredProcedureQuery("APPS.CBO_SP_SLIP_PKG.CREATE_ACCOUNT_OM");
                    storedProcedureQuery.setParameter("p_customer_trx_id", cboArMergeList.get(j).customerTrxId.intValue());
                    storedProcedureQuery.execute();

                    String err_flag = (String) storedProcedureQuery.getOutputParameterValue("x_err_flag");

                    if(!err_flag.equals("S")){
                        if("X".equals(endMsg)){
                            endMsg = "매출채권 Accounting 오류. 시스템 담당자에게 문의 바랍니다. CUSTOMER_TRX_ID : " + cboArMergeList.get(j).customerTrxId;
                        }else{
                            endMsg +=  ", " + cboArMergeList.get(j).customerTrxId;
                        }
                    }
                }
            }

            if(!"X".equals(endMsg)){
                result.put("Message", endMsg);
            }

            if(result.isEmpty()){
                result.put("Message", "수정발행 되었습니다.");
            }

        }

        return result;
    }

    @NotNull @Transactional(propagation = Propagation.REQUIRES_NEW)
    StringBuilder etaxModifySub(List<SalesTaxInvoiceDto> list, List<SalesTaxInvoiceDto> corperInfo, BigDecimal newInterfaceBatchId, int i, String newConversationId) {
        Query query;
        StringBuilder sb;
        //Insert CBO_AR_ETAX_ISSUE
        CboArEtaxIssue inputCboArEtaxIssue = new CboArEtaxIssue();

        inputCboArEtaxIssue.etaxIssueId = new BigDecimal(list.get(i).etaxIssueId);
        inputCboArEtaxIssue.conversationId = newConversationId;
        inputCboArEtaxIssue.supbuyType = list.get(i).supbuyType;
        inputCboArEtaxIssue.direction = list.get(i).direction;
        inputCboArEtaxIssue.interfaceBatchId = newInterfaceBatchId;
        inputCboArEtaxIssue.dtiWdate = list.get(i).dtiWdate == null ? null :
                LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                        Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                        Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
        inputCboArEtaxIssue.taxDemand = list.get(i).taxDemand;
        inputCboArEtaxIssue.itemName = list.get(i).itemName;
        inputCboArEtaxIssue.remark = list.get(i).remark;
        inputCboArEtaxIssue.byrTelNum = list.get(i).byrTelNum;
        inputCboArEtaxIssue.byrEmail = list.get(i).byrEmail;
        inputCboArEtaxIssue.issueEmpName = list.get(i).createEmpName;
        inputCboArEtaxIssue.issueEmpNo = list.get(i).createEmpNo;
        inputCboArEtaxIssue.issueEmpEmail = list.get(i).createEmpNo + "@iljin.co.kr";
        inputCboArEtaxIssue.issueEmpPhone = null;
        inputCboArEtaxIssue.createdPersonId = new BigDecimal(list.get(i).createdPersonId);
        inputCboArEtaxIssue.creationDate = LocalDateTime.now();
        inputCboArEtaxIssue.lastUpdatedPersonId = new BigDecimal(list.get(i).createdPersonId);
        inputCboArEtaxIssue.lastUpdateDate = LocalDateTime.now();
        inputCboArEtaxIssue.amendCode = list.get(i).amendCode;
        inputCboArEtaxIssue.oriIssueId = list.get(i).oriIssueId;
        inputCboArEtaxIssue.oriEtaxIssueId = list.get(i).oriEtaxIssueId;
        inputCboArEtaxIssue.oriDtiWdate = list.get(i).oriDtiWdate == null ? null :
                LocalDateTime.of(Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(0, 4)),
                        Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(4, 6)),
                        Integer.parseInt(list.get(i).oriDtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
        inputCboArEtaxIssue.localLcOpenDate = list.get(i).localLcOpenDate == null ? null :
                LocalDateTime.of(Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(0, 4)),
                        Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(4, 6)),
                        Integer.parseInt(list.get(i).localLcOpenDate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);

        cboArEtaxIssueRepository.save(inputCboArEtaxIssue);


        //Insert XXSB_DTI_MAIN
        XxsbDtiMain inputXxsbDtiMain = new XxsbDtiMain();

        inputXxsbDtiMain.conversationId = newConversationId;
        inputXxsbDtiMain.supbuyType = list.get(i).supbuyType;
        inputXxsbDtiMain.direction = list.get(i).direction;
        inputXxsbDtiMain.interfaceBatchId = newInterfaceBatchId;
        inputXxsbDtiMain.dtiWdate = list.get(i).dtiWdate == null ? null :
                LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                        Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                        Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)), 0, 0, 0);
        inputXxsbDtiMain.dtiType = list.get(i).dtiType;
        inputXxsbDtiMain.taxDemand = list.get(i).taxDemand;
        inputXxsbDtiMain.supComId = smartbillId;
        inputXxsbDtiMain.supComRegno = corperInfo.get(0).vatRegistrationNumNon;
        inputXxsbDtiMain.supRepName = corperInfo.get(0).representativeName;
        inputXxsbDtiMain.supComName = corperInfo.get(0).corporationName;
        inputXxsbDtiMain.supComType = corperInfo.get(0).custBusinessCondition;
        inputXxsbDtiMain.supComClassify = corperInfo.get(0).custBusinessType;
        inputXxsbDtiMain.supComAddr = corperInfo.get(0).address;
        inputXxsbDtiMain.supDeptName = list.get(i).createDeptCode;
        inputXxsbDtiMain.supEmpName = list.get(i).createEmpNo;
        inputXxsbDtiMain.supTelNum = null;
        inputXxsbDtiMain.supEmail = list.get(i).createEmpNo + "@iljin.co.kr";
        inputXxsbDtiMain.byrComId = "";
        inputXxsbDtiMain.byrComRegno = list.get(i).byrComRegno.replaceAll("-", "");
        inputXxsbDtiMain.byrRepName = list.get(i).byrRepName;
        inputXxsbDtiMain.byrComName = list.get(i).customerName;
        inputXxsbDtiMain.byrComType = list.get(i).byrComType;
        inputXxsbDtiMain.byrComClassify = list.get(i).byrComClassify;
        inputXxsbDtiMain.byrComAddr = list.get(i).byrComAddr;
        inputXxsbDtiMain.byrDeptName = "";
        inputXxsbDtiMain.byrEmpName = "";
        inputXxsbDtiMain.byrTelNum = list.get(i).byrTelNum;
        inputXxsbDtiMain.byrEmail = list.get(i).byrEmail;
        inputXxsbDtiMain.supAmount = list.get(i).supplyAmount;
        inputXxsbDtiMain.taxAmount = list.get(i).taxAmount;
        inputXxsbDtiMain.totalAmount = list.get(i).totalAmount;
        inputXxsbDtiMain.dttYn = "N";
        inputXxsbDtiMain.remark = list.get(i).remark;
        inputXxsbDtiMain.createdBy = list.get(i).createEmpNo;
        inputXxsbDtiMain.creationDate = LocalDateTime.now();
        inputXxsbDtiMain.amendCode = list.get(i).amendCode;
        inputXxsbDtiMain.oriIssueId = list.get(i).oriIssueId;
        inputXxsbDtiMain.byrBizplaceCode = list.get(i).byrBizplaceCode;

        xxsbDtiMainRepository.save(inputXxsbDtiMain);


        //Insert XXSB_DTI_ITEM
        List<SalesTaxInvoiceDto> insertXxsbDtiItem = salesTaxInvoiceRepository.getItem(Integer.valueOf(list.get(i).etaxIssueId));

        for(int j=0; j<insertXxsbDtiItem.size(); j++){
            XxsbDtiItem inputXxsbDtiItem = new XxsbDtiItem();

            inputXxsbDtiItem.conversationId = newConversationId;
            inputXxsbDtiItem.supbuyType = list.get(i).supbuyType;
            inputXxsbDtiItem.direction = list.get(i).direction;
            inputXxsbDtiItem.dtiLineNum = insertXxsbDtiItem.get(j).dtiLineNum;
            inputXxsbDtiItem.itemName = insertXxsbDtiItem.get(j).itemName;
            inputXxsbDtiItem.itemMd = list.get(i).dtiWdate == null ? null :
                    LocalDateTime.of(Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(0, 4)),
                            Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(4, 6)),
                            Integer.parseInt(list.get(i).dtiWdate.replaceAll("-", "").substring(6, 8)),
                            0, 0, 0);
            inputXxsbDtiItem.unitPrice = insertXxsbDtiItem.get(j).unitPrice;
            inputXxsbDtiItem.itemQty = insertXxsbDtiItem.get(j).itemQty;
            inputXxsbDtiItem.supAmount = insertXxsbDtiItem.get(j).supplyAmount;
            inputXxsbDtiItem.taxAmount = insertXxsbDtiItem.get(j).taxAmount;
            inputXxsbDtiItem.remark = list.get(i).remark;
            inputXxsbDtiItem.createdBy = list.get(i).createEmpNo;
            inputXxsbDtiItem.creationDate = LocalDateTime.now();

            xxsbDtiItemRepository.save(inputXxsbDtiItem);
        }

        //Insert XXSB_DTI_STATUS
        XxsbDtiStatus inputXxsbDtiStatus = new XxsbDtiStatus();

        inputXxsbDtiStatus.conversationId = newConversationId;
        inputXxsbDtiStatus.supbuyType = list.get(i).supbuyType;
        inputXxsbDtiStatus.direction = list.get(i).direction;
        inputXxsbDtiStatus.dtiStatus = "S";

        xxsbDtiStatusRepository.save(inputXxsbDtiStatus);

        //Insert XXSB_DTI_INVOICE
        sb = new StringBuilder();

        sb.append(
                "SELECT RCTA.TRX_NUMBER " +
                        "     , RCTA.CUSTOMER_TRX_ID " +
                        "  FROM CBO_AR_TRX_MERGE CATM " +
                        "     , AR.RA_CUSTOMER_TRX_ALL RCTA " +
                        " WHERE CATM.ETAX_ISSUE_ID = :etaxIssueId " +
                        "   AND CATM.CUSTOMER_TRX_ID = RCTA.CUSTOMER_TRX_ID " +
                        "   AND CATM.DELETE_FLAG = 'N' " +
                        " ORDER BY RCTA.CUSTOMER_TRX_ID "
        );

        query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("etaxIssueId", list.get(i).etaxIssueId);
        List<SalesTaxInvoiceDto> dtiInvoiceList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        for(int j=0; j<dtiInvoiceList.size(); j++){
            XxsbDtiInvoice xxsbDtiInvoice = new XxsbDtiInvoice();

            xxsbDtiInvoice.conversationId = newConversationId;
            xxsbDtiInvoice.supbuyType = list.get(i).supbuyType;
            xxsbDtiInvoice.direction = list.get(i).direction;
            xxsbDtiInvoice.invoiceIdx = String.valueOf(j+1);
            xxsbDtiInvoice.invoiceId = dtiInvoiceList.get(j).customerTrxId;
            xxsbDtiInvoice.invoiceNum = dtiInvoiceList.get(j).trxNumber;

            xxsbDtiInvoiceRepository.save(xxsbDtiInvoice);
        }
        return sb;
    }



    @Override
    public Map<String, String> preview(SalesTaxInvoiceDto salesTaxInvoiceDto){
        Map<String, String> result = new HashMap<>();

        //회사 정보 조회
        StringBuilder sb = new StringBuilder();

        sb.append(
                "SELECT VAT_REGISTRATION_NUM " +
                        "     , VAT_REGISTRATION_NUM_NON " +
                        "     , REPRESENTATIVE_NAME " +
                        "     , CUST_BUSINESS_CONDITION " +
                        "     , CUST_BUSINESS_TYPE " +
                        "     , CORPORATION_NAME " +
                        "     , ADDRESS " +
                        "  FROM APPS.CBO_AR_BUSINESS_INFO_V " +
                        " WHERE ORG_ID = :orgId"
        );

        Query query = entityManager.createNativeQuery(sb.toString());
        query.setParameter("orgId", salesTaxInvoiceDto.orgId);
        List<SalesTaxInvoiceDto> corperInfo = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

        if(corperInfo.size() != 1){
            result.put("Message", "법인정보 Error");
        }else{
            if(salesTaxInvoiceDto.direction.equals("1")){
                result.put("Message", "역발행은 [역발행저장] 버튼으로 처리 하시기 바랍니다.");
                return result;
            }

            //interface batch id 채번
            sb = new StringBuilder();

            sb.append(
                    "SELECT CBOSLIP.CBO_AR_ETAX_IF_BATCH_S.NEXTVAL AS NEW_INTERFACE_BATCH_ID, 'temp' AS temp" +
                            "  FROM DUAL "
            );

            query = entityManager.createNativeQuery(sb.toString());

            BigDecimal newInterfaceBatchId = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class).get(0).newInterfaceBatchId;


            //개인사용자
            if(salesTaxInvoiceDto.byrComRegno.replaceAll("-", "").length() > 10){
                salesTaxInvoiceDto.byrComRegno = salesTaxInvoiceDto.byrComRegno.substring(salesTaxInvoiceDto.byrComRegno.length() - 10);
            }

            //발행여부 체크
            if(hasText(salesTaxInvoiceDto.conversationId)){
                sb = new StringBuilder();

                sb.append(
                        "SELECT CONVERSATION_ID " +
                                "     , DTI_STATUS " +
                                "     , RETURN_CODE " +
                                "  FROM CBOTAX.XXSB_DTI_STATUS" +
                                " WHERE CONVERSATION_ID = :conversationId "
                );

                query = entityManager.createNativeQuery(sb.toString());
                query.setParameter("conversationId", salesTaxInvoiceDto.conversationId);
                List<SalesTaxInvoiceDto> dtiStatusList = com.iljin.apiServer.core.util.ResultMapperUtil.list(query, SalesTaxInvoiceDto.class);

                if(dtiStatusList.size() != 1){
                    result.put("Message", "세금계산서 상태값 error");
                    return result;
                }
            }

            //update CBO_AR_ETAX_ISSUE
            cboArEtaxIssueRepository.updateInterfaceBatchIdAndLastUpdatedPersonIdAndLastUpdateDateByEtaxIssueIdAndConversationId(
                    newInterfaceBatchId,
                    new BigDecimal(salesTaxInvoiceDto.createdPersonId),
                    LocalDateTime.now(),
                    new BigDecimal(salesTaxInvoiceDto.etaxIssueId),
                    salesTaxInvoiceDto.conversationId
            );

            //update XXSB_DTI_MAIN
            xxsbDtiMainRepository.updateInterfaceBatchIdByConversationId(
                    newInterfaceBatchId,
                    salesTaxInvoiceDto.conversationId
            );

            result.put("newInterfaceId", String.valueOf(newInterfaceBatchId));

        }

        return result;
    }

    @Override @Transactional
    public Map<String, String> modifyItem(SalesTaxInvoiceDto salesTaxInvoiceDto){
        Map<String, String> result = new HashMap<>();

        CboArEtaxIssueInfo inputCboArEtaxIssueInfo = new CboArEtaxIssueInfo();

        inputCboArEtaxIssueInfo.etaxIssueId = new BigDecimal(salesTaxInvoiceDto.etaxIssueId);
        inputCboArEtaxIssueInfo.remark = salesTaxInvoiceDto.remark;
        inputCboArEtaxIssueInfo.itemName = salesTaxInvoiceDto.itemName;
        inputCboArEtaxIssueInfo.itemQty = salesTaxInvoiceDto.itemQty;
        inputCboArEtaxIssueInfo.unitPrice = salesTaxInvoiceDto.unitPrice;
        inputCboArEtaxIssueInfo.createdPersonId = new BigDecimal(salesTaxInvoiceDto.createdPersonId);
        inputCboArEtaxIssueInfo.creationDate = LocalDateTime.now();
        inputCboArEtaxIssueInfo.lastUpdatedPersonId = new BigDecimal(salesTaxInvoiceDto.createdPersonId);
        inputCboArEtaxIssueInfo.lastUpdateDate = LocalDateTime.now();

        cboArEtaxIssueInfoRepository.save(inputCboArEtaxIssueInfo);

        result.put("Message", "수정 성공");

        return result;
    }

}
