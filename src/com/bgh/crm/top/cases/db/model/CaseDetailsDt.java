/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.crm.top.cases.db.model;

import java.util.Date;

/**
 *
 * @author BASTING
 */
public class CaseDetailsDt {
    
    private String msisdn;
    private String caseTypeLvl1;
    private String caseTypeLvl2;
    private String caseTypeLvl3;
    private String history;
    private String title;
    private String caseId;
    private Date   creationTime;
    private String status;
    private String condition;

    @Override
    public String toString() {
        return caseId;
    }
    public String getCaseTypeLvl1() {
        return caseTypeLvl1;
    }

    public void setCaseTypeLvl1(String caseTypeLvl1) {
        this.caseTypeLvl1 = caseTypeLvl1;
    }

    public String getCaseTypeLvl2() {
        return caseTypeLvl2;
    }

    public void setCaseTypeLvl2(String caseTypeLvl2) {
        this.caseTypeLvl2 = caseTypeLvl2;
    }

    public String getCaseTypeLvl3() {
        return caseTypeLvl3;
    }

    public void setCaseTypeLvl3(String caseTypeLvl3) {
        this.caseTypeLvl3 = caseTypeLvl3;
    }
    
    public String getMsisdn() {
        return msisdn;
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }
    
    public String getHistory() {
        return history;
    }
    
    public void setHistory(String history) {
        this.history = history;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getCaseId() {
        return caseId;
    }
    
    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
    
    public Date getCreationTime() {
        return creationTime;
    }
    
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }

}
