/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.bgh.crm.top.cases.db.DBQueryHelper;
import com.bgh.crm.top.cases.db.model.CaseDetailsDt;
import com.bgh.crm.top.cases.db.model.TopCasesDt;
import com.bgh.crm.top.cases.util.CaseCondition;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

/**
 *
 * @author BASTING
 */
public class DisplayTopCases extends Application {
    
    private static final String STAGE_TITLE = "XL Axiata Smart Client Top ";
    private static final String TOP = "Top ";
    private static final String CASES =  " Cases";
    private static final String COLON = ":";
    
    private ArrayList<TopCasesDt> topCaseTypes;
    private ArrayList<CaseDetailsDt> caseList;
    
    BooleanProperty ready = new SimpleBooleanProperty(false);
    
    /*private void longStart() {
     * System.out.println("longStart begin");
     * 
     * //simulate long init in background
     * Task task = new Task<Void>() {
     * @Override
     * protected Void call() throws Exception {
     * System.out.println("longStart.Task.call begin");
     * int max = 4;
     * int i = 1;
     * 
     * notifyPreloader(new Preloader.ProgressNotification(-1.0f));
     * 
     * Thread.sleep(5000);
     * 
     * // Send progress to preloader
     * notifyPreloader(new Preloader.ProgressNotification(((double) i++)/max));
     * 
     * DBQueryHelper helper = new DBQueryHelper();
     * Date dt = new GregorianCalendar().getTime();
     * java.sql.Date dtSQL = new java.sql.Date(dt.getTime());
     * 
     * // Send progress to preloader
     * notifyPreloader(new Preloader.ProgressNotification(((double) i++)/max));
     * 
     * topCaseTypes = helper.getTopCaseTypes(dtSQL, 10);
     * 
     * // Send progress to preloader
     * notifyPreloader(new Preloader.ProgressNotification(((double) i++)/max));
     * 
     * caseList = helper.getCaseList(topCaseTypes, dtSQL);
     * 
     * // Send progress to preloader
     * notifyPreloader(new Preloader.ProgressNotification(((double) i++)/max));
     * 
     * System.out.println("done"+caseList.size() + ".."+topCaseTypes.size());
     * 
     * 
     * // After init is ready, the app is ready to be shown
     * // Do this before hiding the preloader stage to prevent the
     * // app from exiting prematurely
     * ready.setValue(Boolean.TRUE);
     * 
     * notifyPreloader(new Preloader.StateChangeNotification(
     * Preloader.StateChangeNotification.Type.BEFORE_START));
     * System.out.println("longStart.Task.call end");
     * return null;
     * }
     * };
     * System.out.println("longStart.new.Thread.start");
     * Thread th = new Thread(task);
     * th.start();
     * 
     * try {
     * System.out.println("longStart Thread join");
     * th.join();
     * } catch (InterruptedException ex) {
     * Logger.getLogger(DisplayTopCases.class.getName()).log(Level.SEVERE, null, ex);
     * }
     * System.out.println("longStart Thread join done");
     * }*/
    
    //@Override
    /*public void init() throws Exception {
     * System.out.println("init.begin");
     * super.init();
     * longStart();
     * System.out.println("init.end");
     * }*/
    
    
    
    @Override
    public void start(final Stage stage) {
        System.out.println("start.begin");
        
        DBQueryHelper helper = new DBQueryHelper();
        Date dt = new GregorianCalendar().getTime();
        java.sql.Date dtSQL = new java.sql.Date(dt.getTime());
        topCaseTypes = helper.getTopCaseTypes(dtSQL, 10, CaseCondition.OPEN);
        caseList = helper.getCaseList(topCaseTypes, dtSQL,  CaseCondition.OPEN);
        
        final NumberAxis xAxis = new NumberAxis();
        final CategoryAxis yAxis = new CategoryAxis();
        final BarChart<Number,String> bc =
                new BarChart<Number,String>(xAxis,yAxis);
        
        System.out.println("start.begin1");
        
        xAxis.setLabel("Total Count");
        //xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Case Types");
        
        int size = topCaseTypes.size();
        stage.setTitle(STAGE_TITLE+size+CASES);
        bc.setTitle(TOP+size+CASES);
        
        System.out.println("start.begin2");
        
        XYChart.Series series = new XYChart.Series();
        series.setName(TOP+size+CASES);
        ObservableList list= series.getData();
        for (int i = size - 1; i >= 0; i--) {
            TopCasesDt topCasesDt = topCaseTypes.get(i);
            String caseTypeStr = new StringBuilder().append(topCasesDt.getCaseTypeLvl1())
                    .append(COLON).append(topCasesDt.getCaseTypeLvl2())
                    .append(COLON).append(topCasesDt.getCaseTypeLvl3()).toString();
            XYChart.Data data = new XYChart.Data(topCasesDt.getCount(), caseTypeStr);
            list.add(data);
        }
        
        System.out.println("start.begin3");
        
        bc.getData().add(series);
        
        Scene scene  = new Scene(bc,800,600);
        //bc.getData().addAll(seriesArr);
        stage.setScene(scene);
        stage.show();
        
        System.out.println("start.end");
        
    }
    
    public static void main(String[] args) {
        new DisplayTopCases().showChart(args);
    }
    
    private void showChart(String[] args) {
        launch(args);
    }
}
