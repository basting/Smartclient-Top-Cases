/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.crm.top.cases.datamodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author BASTING
 */
public class TopCasesDataModel {
    private final String EMPTY_STR = "";
    
    private final SimpleStringProperty caseTypeLvl1 = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty caseTypeLvl2 = new SimpleStringProperty(EMPTY_STR);
    private final SimpleStringProperty caseTypeLvl3 = new SimpleStringProperty(EMPTY_STR);
    private final SimpleIntegerProperty count = new SimpleIntegerProperty(0);

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof TopCasesDataModel)){
            return false;
        }
        
        TopCasesDataModel inputObj = (TopCasesDataModel)obj;
        //inputObj.caseTypeLvl1.get().equalsIgnoreCase(this.caseTypeLvl1.get());
        if(inputObj.caseTypeLvl1.get().equalsIgnoreCase(this.caseTypeLvl1.get()) &&
                inputObj.caseTypeLvl2.get().equalsIgnoreCase(this.caseTypeLvl2.get()) &&
                    inputObj.caseTypeLvl3.get().equalsIgnoreCase(this.caseTypeLvl3.get())){
            return true;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.caseTypeLvl1 != null ? this.caseTypeLvl1.get().hashCode() : 0);
        hash = 97 * hash + (this.caseTypeLvl2 != null ? this.caseTypeLvl2.get().hashCode() : 0);
        hash = 97 * hash + (this.caseTypeLvl3 != null ? this.caseTypeLvl3.get().hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return caseTypeLvl1.get() + ":" + caseTypeLvl2.get() + ":" + caseTypeLvl3.get();
    }
    
    
    
    public TopCasesDataModel(String caseTypeLvl1Local,String caseTypeLvl2Local,
            String caseTypeLvl3Local){
         setCaseTypeLvl1(caseTypeLvl1Local);
        setCaseTypeLvl2(caseTypeLvl2Local);
        setCaseTypeLvl3(caseTypeLvl3Local);
        setCount(0);
    }
    
    public TopCasesDataModel(String caseTypeLvl1Local,String caseTypeLvl2Local,
            String caseTypeLvl3Local,int countLocal){        
        setCaseTypeLvl1(caseTypeLvl1Local);
        setCaseTypeLvl2(caseTypeLvl2Local);
        setCaseTypeLvl3(caseTypeLvl3Local);
        setCount(countLocal);
    }
    
    
    public SimpleStringProperty caseTypeLvl1Property(){
        return caseTypeLvl1;
    }
    
    public SimpleStringProperty caseTypeLvl2Property(){
        return caseTypeLvl2;
    }
    
    public SimpleStringProperty caseTypeLvl3Property(){
        return caseTypeLvl3;
    }
    
    public SimpleIntegerProperty countProperty(){
        return count;
    } 
    
    public final void setCaseTypeLvl1(String caseTypeLvl1Local){
        caseTypeLvl1.set(caseTypeLvl1Local);
    } 
    
    public final void setCaseTypeLvl2(String caseTypeLvl2Local){
        caseTypeLvl2.set(caseTypeLvl2Local);
    } 
    
    public final void setCaseTypeLvl3(String caseTypeLvl3Local){
        caseTypeLvl3.set(caseTypeLvl3Local);
    } 
    
    public final void setCount(int countLocal){
        count.set(countLocal);
    }
    
    public String getCaseTypeLvl1(){
        return caseTypeLvl1.get();
    }
    
    public String getCaseTypeLvl2(){
        return caseTypeLvl2.get();
    }
   
    public String getCaseTypeLvl3(){
        return caseTypeLvl3.get();
    }
    
    public int getCount(){
        return count.get();
    }
}
