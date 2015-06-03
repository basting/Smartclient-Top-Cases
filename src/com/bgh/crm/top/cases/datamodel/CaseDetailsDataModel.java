/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.crm.top.cases.datamodel;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BASTING
 */
public class CaseDetailsDataModel {
    
    private final String EMPTY_STR = "";
    
    private final SimpleStringProperty msisdn = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty caseTypeLvl1  = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty caseTypeLvl2  = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty caseTypeLvl3 = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty history = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty title = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty caseId = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty creationTime = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty status = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty condition = new SimpleStringProperty(EMPTY_STR);

    
    public String getCaseTypeLvl1() {
        return caseTypeLvl1.get();
    }

    public void setCaseTypeLvl1(String caseTypeLvl1) {
        this.caseTypeLvl1.set(caseTypeLvl1);
    }

    public String getCaseTypeLvl2() {
        return caseTypeLvl2.get();
    }

    public void setCaseTypeLvl2(String caseTypeLvl2) {
        this.caseTypeLvl2.set(caseTypeLvl2);
    }

    public String getCaseTypeLvl3() {
        return caseTypeLvl3.get();
    }

    public void setCaseTypeLvl3(String caseTypeLvl3) {
        this.caseTypeLvl3.set(caseTypeLvl3);
    }
    
    public String getMsisdn() {
        return msisdn.get();
    }
    
    public void setMsisdn(String msisdn) {
        this.msisdn.set(msisdn);
    }
    
    public String getHistory() {
        return history.get();
    }
    
    public void setHistory(String history) {
        this.history.set(history);
    }
    
    public String getTitle() {
        return title.get();
    }
    
    public void setTitle(String title) {
        this.title.set(title);
    }
    
    public String getCaseId() {
        return caseId.get();
    }
    
    public void setCaseId(String caseId) {
        this.caseId.set(caseId);
    }
    
    public String getCreationTime() {
        return creationTime.get();
    }
    
    public void setCreationTime(String creationTime) {
        this.creationTime.set(creationTime);
    }
    
    public String getStatus() {
        return status.get();
    }
    
    public void setStatus(String status) {
        this.status.set( status);
    }
    
    public String getCondition() {
        return condition.get();
    }
    
    public void setCondition(String condition) {
        this.condition.set(condition);
    }

}
