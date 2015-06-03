/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.crm.top.cases.db.model;

/**
 *
 * @author BASTING
 */
public class TopCasesDt {

   private String caseTypeLvl1;
   private String caseTypeLvl2;
   private String caseTypeLvl3;
   private int count;
    
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
   

   
    @Override
    public String toString() {
        return new StringBuilder().append(caseTypeLvl1).append(":")
                .append(caseTypeLvl2).append(":")
                .append(caseTypeLvl3).append(":")
                .append(count).toString();
    }
    
    
}
