package com.bgh.crm.top.cases.db;

import com.bgh.crm.top.cases.db.model.CaseDetailsDt;
import com.bgh.crm.top.cases.db.model.TopCasesDt;
import com.bgh.crm.top.cases.util.CaseCondition;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBQueryHelper {
    private Connection jdbcConn;
    
    //private static final String S_QUOTE = "'";
    private static final String OR = " OR ";
    private static final String EMPTY = "";
    private static final String OPENING_BRACKET = " ( ";
    private static final String CLOSING_BRACKET = " ) ";
    private static final String CASE_TYPE_LVL_1 = " s_case_type_lvl1 ";
    private static final String CASE_TYPE_LVL_2 = " s_case_type_lvl2 ";
    private static final String CASE_TYPE_LVL_3 = " s_case_type_lvl3 ";
    private static final String EQUALS = " = ";
    private static final String AND = " AND ";
    private static final String QN_MARK = " ? ";
    
    private final String ALL_CASE_DETAILS_P1 = new StringBuilder().append(" SELECT case1.x_msisdn MSISDN, case1.s_case_type_lvl1, ")
            .append(" case1.s_case_type_lvl2, case1.s_case_type_lvl3, ")
            .append(" case1.case_history, case1.title, ")
            .append(" case1.id_number, case1.creation_time, ")
            .append(" status.title status, condition.title condition ")
            .append(" FROM table_case case1, table_gbst_elm status, table_condition condition ")
            .append(" WHERE      TRUNC (creation_time) = ? ")
            .append(" AND case1.casests2gbst_elm = status.objid ")
            .append(" AND case1.case_state2condition = condition.objid ").toString();
    
    private final String ALL_CASE_DETAILS_P2 = " AND condition.s_title != 'CLOSED' ";
    
    private final String ALL_CASE_DETAILS_P3 = " AND (";
    
    
    private final String TOP_CASE_QUERY_P1 = new StringBuilder().append(" SELECT * ")
            .append(" FROM (SELECT /*+parallel(case1,2) */ ")
            .append(" case1.s_case_type_lvl1, ")
            .append(" case1.s_case_type_lvl2, ")
            .append(" case1.s_case_type_lvl3, ")
            .append(" COUNT (1) ")
            .append(" FROM table_case case1, table_condition condition ")
            .append(" WHERE TRUNC (creation_time) = ? ")
            .append(" AND case1.case_state2condition = condition.objid ")
            .toString();
    
    private final String TOP_CASE_QUERY_P2 = " AND condition.s_title != 'CLOSED' ";
    
    private final String TOP_CASE_QUERY_P3 = new StringBuilder()
            .append(" GROUP BY s_case_type_lvl1, s_case_type_lvl2, s_case_type_lvl3 ")
            .append(" ORDER BY 4 DESC) ")
            .append(" WHERE ROWNUM <= ? " ).toString();
    
    public ArrayList<TopCasesDt> getTopCaseTypes(Date dt, int rowcount, CaseCondition selectedCondition) {
        jdbcConn = getJDBCConnection();
        
        ArrayList<TopCasesDt> resultTopCaseList = new ArrayList<TopCasesDt>();
        
        PreparedStatement pstmt = null;
        ResultSet resultSet =null;
        
        String finalQuery = TOP_CASE_QUERY_P1;
        if(selectedCondition.equals(CaseCondition.OPEN)){
            finalQuery = finalQuery + TOP_CASE_QUERY_P2;
        }
        finalQuery = finalQuery + TOP_CASE_QUERY_P3;
        
        try {
            pstmt = jdbcConn.prepareStatement(finalQuery);
            System.out.println("--SQL: "+finalQuery);
            pstmt.setDate(1, dt);
            pstmt.setInt(2, rowcount);
            resultSet = pstmt.executeQuery();
            
            while(resultSet.next()) {
                
                String caseTypeLvl1 = resultSet.getString(1);
                String caseTypeLvl2 = resultSet.getString(2);
                String caseTypeLvl3 = resultSet.getString(3);
                int count = resultSet.getInt(4);
                
                TopCasesDt topCasesDt = new TopCasesDt();
                
                if(caseTypeLvl1 != null){
                    topCasesDt.setCaseTypeLvl1(caseTypeLvl1);
                }
                
                if(caseTypeLvl2 != null){
                    topCasesDt.setCaseTypeLvl2(caseTypeLvl2);
                }
                
                if(caseTypeLvl3 != null){
                    topCasesDt.setCaseTypeLvl3(caseTypeLvl3);
                }
                
                topCasesDt.setCount(count);
                
                resultTopCaseList.add(topCasesDt);
            }
            System.out.println("Exit:getTopCaseTypes");
            return resultTopCaseList;
            
        } catch (SQLException e) {
            System.out.println("Error in getTopCaseTypes : "+e.getMessage());
            System.out.println(e);
            java.util.logging.Logger.getLogger(DBQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
            //e.printStackTrace();
        }finally{
            try {
                if(resultSet != null){
                    resultSet.close();
                }
                if(pstmt != null){
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error while closing prepared stmt in getTopCaseTypes : "+e);
                java.util.logging.Logger.getLogger(DBQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                //e.printStackTrace();
            }
        }
        System.out.println("Exit:getTopCaseTypes with null");
        return null;
    }
    
    public ArrayList<CaseDetailsDt> getCaseList(ArrayList<TopCasesDt> topCaseTypeList, Date dt, CaseCondition selectedCondition){
        
        if(topCaseTypeList == null){
            return null;
        }
        
        if(topCaseTypeList.isEmpty()){
            return null;
        }
        
        jdbcConn = getJDBCConnection();
        
        String filter = getCaseTypeLevelFilter(topCaseTypeList);
        
        if(EMPTY.equals(filter)){
           return null;  
        }
        
        String finalQuery = ALL_CASE_DETAILS_P1;
        if(selectedCondition.equals(CaseCondition.OPEN)){
            finalQuery = finalQuery + ALL_CASE_DETAILS_P2;
        }
        finalQuery = finalQuery + ALL_CASE_DETAILS_P3;
        
        
        finalQuery = finalQuery + filter;
        
        ArrayList<CaseDetailsDt> caseList = getCaseList(jdbcConn,finalQuery,dt,topCaseTypeList);
        
        return caseList;
    }
    
    private ArrayList<CaseDetailsDt> getCaseList(Connection jdbcConn, String caseListQuery, Date dt, ArrayList<TopCasesDt> topCaseTypeList) {
        System.out.println("Entry:getCaseList");
        PreparedStatement pstmt = null;
        ResultSet resultSet =null;
        ArrayList<CaseDetailsDt> caseList = new ArrayList<CaseDetailsDt>();
        try {
            pstmt = jdbcConn.prepareStatement(caseListQuery);
            System.out.println("--SQL: "+caseListQuery);
            pstmt.setDate(1, dt);
            int startIdx = 2;
            if(!setParams(pstmt,topCaseTypeList,startIdx)){
                return caseList;
            }
            resultSet = pstmt.executeQuery();
            
            while(resultSet.next()) {
                String msisdn = resultSet.getString(1);
                String caseTypeLvl1 = resultSet.getString(2);
                String caseTypeLvl2 = resultSet.getString(3);
                String caseTypeLvl3 = resultSet.getString(4);
                String history = resultSet.getString(5);
                String title = resultSet.getString(6);
                String caseId = resultSet.getString(7);
                Timestamp creationTime = resultSet.getTimestamp(8);
                String status = resultSet.getString(9);
                String condition = resultSet.getString(10);
                
                CaseDetailsDt caseQueryDt = new CaseDetailsDt();
                
                if(msisdn != null){
                    caseQueryDt.setMsisdn(msisdn);
                }
                if(caseTypeLvl1 != null){
                    caseQueryDt.setCaseTypeLvl1(caseTypeLvl1);
                }
                if(caseTypeLvl2 != null){
                    caseQueryDt.setCaseTypeLvl2(caseTypeLvl2);
                }
                if(caseTypeLvl3 != null){
                    caseQueryDt.setCaseTypeLvl3(caseTypeLvl3);
                }
                if(history != null){
                    caseQueryDt.setHistory(history);
                }
                if(title != null){
                    caseQueryDt.setTitle(title);
                }
                if(caseId != null){
                    caseQueryDt.setCaseId(caseId);
                }
                if(creationTime != null){
                    caseQueryDt.setCreationTime(new java.util.Date(creationTime.getTime()));
                }
                if(status != null){
                    caseQueryDt.setStatus(status);
                }
                if(condition != null){
                    caseQueryDt.setCondition(condition);
                }
                caseList.add(caseQueryDt);
            }
            System.out.println("Exit:getCaseList");
            return caseList;
            
        } catch (SQLException e) {
            System.out.println("Error in getCaseList : "+e.getMessage());
            System.out.println(e);
            java.util.logging.Logger.getLogger(DBQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);            
        }finally{
            try {
                if(resultSet != null){
                    resultSet.close();
                }
                if(pstmt != null){
                    pstmt.close();
                }
            } catch (SQLException e) {
                System.out.println("Error while closing prepared stmt in getCaseList : "+e);
                java.util.logging.Logger.getLogger(DBQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);              
            }
        }
        System.out.println("Exit:getCaseList with null");
        return null;
    }
    
    private boolean setParams(PreparedStatement pstmt, ArrayList<TopCasesDt> topCaseTypeList, int startIdx) {
        
        for (Iterator<TopCasesDt> it = topCaseTypeList.iterator(); it.hasNext();) {
            try {
                TopCasesDt topCasesDt = it.next();
                pstmt.setString(startIdx++, topCasesDt.getCaseTypeLvl1());
                pstmt.setString(startIdx++, topCasesDt.getCaseTypeLvl2());
                pstmt.setString(startIdx++, topCasesDt.getCaseTypeLvl3());
            } catch (SQLException ex) {
                Logger.getLogger(DBQueryHelper.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }        
        return true;
    }
    
    private String getCaseTypeLevelFilter(ArrayList<TopCasesDt> topCaseTypeList) {
    
        int topCaseTypeSize = topCaseTypeList.size();
        StringBuilder builder = new StringBuilder();
        for(int i=0;i<topCaseTypeSize;i++){
            TopCasesDt caseDt = topCaseTypeList.get(i);
            
            if(i != 0){
                builder.append(OR);
            }
        
            //(s_case_type_lvl1 = 'COMPLAINT' AND s_case_type_lvl2 = 'TARIFF' AND s_case_type_lvl3 = 'NOT APPLIED') or
            
            builder.append(OPENING_BRACKET);
            builder.append(CASE_TYPE_LVL_1);
            builder.append(EQUALS);
            //builder.append(S_QUOTE);
            //builder.append(caseDt.getCaseTypeLvl1());
            builder.append(QN_MARK);
            //builder.append(S_QUOTE);
            builder.append(AND);
            builder.append(CASE_TYPE_LVL_2);
            builder.append(EQUALS);
            //builder.append(S_QUOTE);
            //builder.append(caseDt.getCaseTypeLvl2());
            builder.append(QN_MARK);
            //builder.append(S_QUOTE);
            builder.append(AND);
            builder.append(CASE_TYPE_LVL_3);
            builder.append(EQUALS);
            //builder.append(S_QUOTE);
            //builder.append(caseDt.getCaseTypeLvl3());
            builder.append(QN_MARK);
            //builder.append(S_QUOTE);
            builder.append(CLOSING_BRACKET);            
        }
        
        builder.append(CLOSING_BRACKET);
        
        return builder.toString().trim();
    }
    
    private Connection getJDBCConnection() {
        if(jdbcConn != null){
            return jdbcConn;
        }
        
        ResourceBundle bundle = ResourceBundle.getBundle("dbconfig");
        String jdbcHost = bundle.getString("db_jdbc_host").trim();
        String jdbcPort = bundle.getString("db_jdbc_port").trim();
        String jdbcSID = bundle.getString("db_jdbc_sid").trim();
        String username = bundle.getString("db_username").trim();
        String password = bundle.getString("db_password").trim();
        
        
        System.out.println("Entry:getJDBCConnection");
        //Connection conn = null;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(e);
            return null;
        }
        String url = new StringBuilder().append("jdbc:oracle:thin:@").append(jdbcHost).
                append(":").append(jdbcPort).append(":").append(jdbcSID).toString();
        
        try {
            jdbcConn = DriverManager.getConnection(url,username, password);
            jdbcConn.setAutoCommit(false);
            CallableStatement stmt = jdbcConn.prepareCall("{call dbms_output.disable()}");
            stmt.execute();
            System.out.println("Dbms output disabled..");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e);
            return null;
        }
        System.out.println("Exit:getJDBCConnection");
        return jdbcConn;
        
    }
    
    public void closeJdbcConnection() {
        if(jdbcConn != null){
            try {
                jdbcConn.close();
                System.out.println("JDBC Connection closed successfully");
            } catch (SQLException e) {
                System.err.println("error in closeConnection: "+e);
                java.util.logging.Logger.getLogger(DBQueryHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, e);                
            }
        }
    }

}

