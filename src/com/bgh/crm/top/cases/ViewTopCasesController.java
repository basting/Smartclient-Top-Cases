/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.crm.top.cases;

import com.bgh.crm.top.cases.datamodel.CaseDetailsDataModel;
import com.bgh.crm.top.cases.datamodel.TopCasesDataModel;
import com.bgh.crm.top.cases.db.DBQueryHelper;
import com.bgh.crm.top.cases.db.model.CaseDetailsDt;
import com.bgh.crm.top.cases.db.model.TopCasesDt;
import com.bgh.crm.top.cases.util.CaseCondition;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javax.swing.event.DocumentEvent;
import jfxtras.labs.dialogs.DialogFX;
import jfxtras.labs.scene.control.CalendarTextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author BASTING
 */
public class ViewTopCasesController implements Initializable {
    
    private ArrayList<TopCasesDt> topCaseTypes;
    private ArrayList<CaseDetailsDt> caseList;
    private HashMap<TopCasesDataModel,ArrayList<CaseDetailsDataModel>> caseDetailsMap;
    
    //private static final String STAGE_TITLE = "XL Axiata Smart Client Top ";
    private static final String TOP = "Top ";
    private static final String CASES =  " Cases";
    private static final String COLON = ":";
    
    private static final String XLSX = ".xlsx";
    
    private static final String DT_FORMAT = "dd MMM yy HH:mm:ss";
    private final SimpleDateFormat dtFmt = new SimpleDateFormat(DT_FORMAT);
    
    private static final String DT_FORMAT_SHORT = "MMM dd, yyyy";
    private final SimpleDateFormat dtFmtShort = new SimpleDateFormat(DT_FORMAT_SHORT);
    
    private final static char TAB = '\t';
    private final static char NEW_LINE = '\n';
    
    private final String SUMMARY_DETAIL = "SUMM_DETAIL";
    private final String SUMMARY = "SUMM";
    
    private final int YES_BUTTON = 0;
    
    
    @FXML
    private AnchorPane pneBarChart;
    
    @FXML
    private RadioMenuItem radioMenuLoadSummaryDetails;
    
    @FXML
    private RadioMenuItem radioMenuLoadSummary;
    
    @FXML
    private RadioMenuItem radioMenuCondOpen;
    
    @FXML
    private RadioMenuItem radioMenuCondOpenClosed;
    
    @FXML
    private RadioMenuItem radioMenuTop10;
    
    @FXML
    private RadioMenuItem radioMenuTop15;
    
    @FXML
    private RadioMenuItem radioMenuTop20;
    
    @FXML
    private RadioMenuItem radioMenuTop25;
    
    @FXML
    private Label lblProgress;
    
    @FXML
    private Label lblLastLoaded;
    
    @FXML
    private Label lblErrorMessage;
    
    //@FXML
    //private MenuBar mnuBar;
    
    @FXML
    private MenuItem mnuItmExportToExcel;
    
    @FXML
    private Button btnRefresh;
    
    @FXML
    private ProgressBar pbLoading;
    
    @FXML
    private ProgressIndicator piLoading;
    
    private BarChart<Number,String> barChartTopCases;
    private NumberAxis xAxis;
    private CategoryAxis yAxis;
    
    private CalendarTextField picker;
    
    @FXML
    private AnchorPane pneTopCases;
    
    private TableView<TopCasesDataModel> tblTopCasesListing;
    
    private TableColumn<TopCasesDataModel,String> colCaseTypeLvl1;
    
    private TableColumn<TopCasesDataModel,String> colCaseTypeLvl2;
    
    private TableColumn<TopCasesDataModel,String> colCaseTypeLvl3;
    
    private TableColumn<TopCasesDataModel,Integer> colCount;
    
    @FXML
    private TableView<CaseDetailsDataModel> tblCaseDetailsListing;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colMsisdn;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colHistory;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colTitle;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colCaseId;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colCreated;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colStatus;
    
    @FXML
    private TableColumn<CaseDetailsDataModel,String> colCondition;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initTables();        
        rebuildGraphControl();
        initTableColumnsForCaseDetails();
        initRadioMenuItems();
        initCustomControls();
    }
    
    public void exitApp(){
        checkBeforeClosing();
    }
    
    public void loadData(){
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                //int j=-1;
                //int max = 3;
                updateProgress(-1, 1);
                updateMessage("Loading Top cases.. Please wait..");
                DBQueryHelper helper = new DBQueryHelper();
                //Date dt = new GregorianCalendar().getTime();
                //java.sql.Date dtSQL = new java.sql.Date(dt.getTime());
                
                java.util.Calendar cal = picker.getValue();
                Date dt = null;
                try{
                    if(cal == null){
                        cal = new GregorianCalendar();
                        picker.setValue(cal);
                    }
                    dt = cal.getTime();
                    
                    DateFormat dtftPicker = picker.getDateFormat();
                    
                    // checking whether format is correct or not
                    dtftPicker.format(dt);
                }catch(Exception e){
                    cal = new GregorianCalendar();
                    picker.setValue(cal);
                    dt = cal.getTime();
                    Logger.getLogger(ViewTopCasesController.class.getName()).log(Level.SEVERE, null, e);
                }
                
                java.sql.Date dtSQL = new java.sql.Date(dt.getTime());
                
                System.out.println(dtSQL);
                
                Toggle toggleTop = radioMenuTop10.getToggleGroup().getSelectedToggle();
                int selectedItemForCount = (Integer)toggleTop.getUserData();
                
                Toggle toggleCondition = radioMenuCondOpen.getToggleGroup().getSelectedToggle();
                CaseCondition selectedCondition = (CaseCondition)toggleCondition.getUserData();
                
                Toggle toggleLoadOption = radioMenuLoadSummaryDetails.getToggleGroup().getSelectedToggle();
                String loadOption = (String)toggleLoadOption.getUserData();
                
                if(topCaseTypes != null){
                    topCaseTypes.clear();
                }
                
                topCaseTypes = helper.getTopCaseTypes(dtSQL, selectedItemForCount,selectedCondition);
                
                if(topCaseTypes == null || topCaseTypes.isEmpty()){
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblErrorMessage.setText("No Data available for '"+dtFmtShort.format(picker.getValue().getTime())+"'");
                            reEnableControls();      
                            updateProgress(1, 1);
                        }
                    });
                    
                    return null;
                }
                
                if(caseList != null){
                    caseList.clear();
                }
                if(caseDetailsMap != null){
                    caseDetailsMap.clear();
                }
                
                if(loadOption.equalsIgnoreCase(SUMMARY_DETAIL)){
                    caseList = helper.getCaseList(topCaseTypes, dtSQL, selectedCondition);
                    caseDetailsMap = convertDetailsListIntoHashMap(caseList);
                }
                updateProgress(1, 1);
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        GregorianCalendar cal = new GregorianCalendar();
                        
                        lblLastLoaded.setText("Graph for '"+ dtFmtShort.format(picker.getValue().getTime()) +"' last loaded @ '"+dtFmt.format(cal.getTime())+ "'");
                        
                        rebuildGraphControl();
                        
                        barChartTopCases.getData().clear();
                        
                        btnRefresh.setText("Refresh");
                        
                        //System.out.println("start.begin1");
                        
                        //final NumberAxis
                        //        xAxis = new NumberAxis();
                        // final CategoryAxis
                        //        yAxis = new CategoryAxis();
                        //final BarChart<Number,String> bc =
                        //        barChartTopCases = new BarChart<Number,String>(xAxis,yAxis);
                        
                        xAxis.setLabel("Total Count");
                        //xAxis.setTickLabelRotation(90);
                        yAxis.setLabel("Case Types");
                        
                        int size = topCaseTypes.size();
                        //stage.setTitle(STAGE_TITLE+size+CASES);
                        barChartTopCases.setTitle(TOP+size+CASES);
                        
                        //System.out.println("start.begin2");
                        
                        XYChart.Series series = new XYChart.Series();
                        
                        series.setName(TOP+size+CASES);
                        ObservableList list= series.getData();
                        for (int i = size - 1; i >= 0; i--) {
                            TopCasesDt topCasesDt = topCaseTypes.get(i);
                            String caseTypeStr = new StringBuilder().append(topCasesDt.getCaseTypeLvl1())
                                    .append(COLON).append(topCasesDt.getCaseTypeLvl2())
                                    .append(COLON).append(topCasesDt.getCaseTypeLvl3()).toString();
                            XYChart.Data data = new XYChart.Data(topCasesDt.getCount(), caseTypeStr);
                            //XYChart.Data data = new XYChart.Data(caseTypeStr, topCasesDt.getCount());
                            list.add(data);
                        }
                        
                        /*FXCollections.sort(list, new Comparator<XYChart.Data>() {
                         *
                         * @Override
                         * public int compare(Data o1, Data o2) {
                         * if((Integer)o1.getXValue() > (Integer)o2.getXValue()){
                         * return 1;
                         * }else if((Integer)o1.getXValue() < (Integer)o2.getXValue()){
                         * return -1;
                         * }else{
                         * return 0;
                         * }
                         * }
                         * });*/
                        
                        //System.out.println("start.begin3");
                        
                        
                        barChartTopCases.getData().add(series);
                        
                        ObservableList<TopCasesDataModel> topCasesList = getDataForTopCasesTableView(topCaseTypes);
                        
                        
                        reBuildTopCasesTable();
                                
                        tblTopCasesListing.getItems().clear();
                        tblTopCasesListing.setItems(topCasesList);
                        
                        
                        //TableColumn<TopCasesDataModel, ?> sortOrder = tblTopCasesListing.getSortOrder().get(0);
                        // tblTopCasesListing.getSortOrder().clear();
                        // tblTopCasesListing.getSortOrder().add(sortOrder);
                        
                        //tblTopCasesListing.fireEvent(new TableColumn.CellEditEvent(tblTopCasesListing, new TablePosition(tblTopCasesListing, size, colCount), null, topCasesList));
                        
                        /*ObservableList<TopCasesDataModel> tableItems =  tblTopCasesListing.getItems();
                        tableItems.removeAll(tableItems);
                        
                        for (Iterator<TopCasesDataModel> it = topCasesList.iterator(); it.hasNext();) {
                            TopCasesDataModel topCasesDataModel = it.next();
                            tableItems.add(topCasesDataModel);
                        }*/
                        
                        //tblTopCasesListing.setItems(topCasesList);
                        
                        tblCaseDetailsListing.getItems().clear();
                        //initTableColumnsForTopCasesListing();
                        
                        /*tblTopCasesListing.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                         *
                         *
                         * @Override
                         * public void onChanged(Change<? extends Integer> c) {
                         * setDataOnCaseDetailsTable(c.getList());
                         * }
                         * });*/
                        
                        tblTopCasesListing.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number> () {
                            
                            @Override
                            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                                setDataOnCaseDetailsTable();
                            }
                            
                        });
                        
                        reEnableControls();                        
                        
                    }
                    
                    private ObservableList<TopCasesDataModel> getDataForTopCasesTableView(ArrayList<TopCasesDt> topCaseTypes) {
                        ArrayList<TopCasesDataModel> dataList = new ArrayList<TopCasesDataModel>();
                        
                        for (Iterator<TopCasesDt> it = topCaseTypes.iterator(); it.hasNext();) {
                            TopCasesDt topCasesDt = it.next();
                            String caseTypeLvl1 = topCasesDt.getCaseTypeLvl1();
                            String caseTypeLvl2 = topCasesDt.getCaseTypeLvl2();
                            String caseTypeLvl3 = topCasesDt.getCaseTypeLvl3();
                            int count = topCasesDt.getCount();
                            
                            TopCasesDataModel topCasesDataModel = new TopCasesDataModel(caseTypeLvl1, caseTypeLvl2, caseTypeLvl3, count);
                            dataList.add(topCasesDataModel);
                        }
                        
                        return FXCollections.observableArrayList(dataList);
                    }
                    
                });
                return null;
            }
            
            @Override
            protected void succeeded() {
                super.succeeded();
                updateMessage("Top Cases loaded successfully");
            }
            
            @Override
            protected void cancelled() {
                super.cancelled();
                updateMessage("Top Cases loading cancelled");
            }
            
            @Override
            protected void failed() {
                super.failed();
                updateMessage("Top Cases loading failed");
            }
        };
        
        lblErrorMessage.setText(null);
        btnRefresh.setDisable(true);
        picker.setDisable(true);
        //mnuBar.setDisable(true);
        lblProgress.textProperty().bind(task.messageProperty());
        pbLoading.progressProperty().bind(task.progressProperty());
        piLoading.progressProperty().bind(task.progressProperty());
        Thread t = new Thread(task);
        t.start();
        
        
    }
    
    private void reEnableControls() {
        btnRefresh.setDisable(false);
        //mnuBar.setDisable(false);
        mnuItmExportToExcel.setDisable(false);
        picker.setDisable(false);
    }
    
    private HashMap<TopCasesDataModel, ArrayList<CaseDetailsDataModel>> convertDetailsListIntoHashMap(ArrayList<CaseDetailsDt> caseList) {
        
        if(caseList == null){
            return null;
        }
        
        if(caseList.isEmpty()){
            return null;
        }
        
        HashMap<TopCasesDataModel, ArrayList<CaseDetailsDataModel>> caseDetailsDataMap = new HashMap<TopCasesDataModel, ArrayList<CaseDetailsDataModel>>();
        int lstSize = caseList.size();
        for(int i=0;i<lstSize;i++){
            CaseDetailsDt dtCurrent = caseList.get(i);
            String caseTypeLvl1 = dtCurrent.getCaseTypeLvl1();
            String caseTypeLvl2 = dtCurrent.getCaseTypeLvl2();
            String caseTypeLvl3 = dtCurrent.getCaseTypeLvl3();
            
            TopCasesDataModel dmNew = new TopCasesDataModel(caseTypeLvl1,caseTypeLvl2,caseTypeLvl3);
            
            String msisdn = dtCurrent.getMsisdn();
            String history = dtCurrent.getHistory();
            String title = dtCurrent.getTitle();
            String caseId = dtCurrent.getCaseId();
            String creationTime = dtFmt.format(dtCurrent.getCreationTime());
            String status = dtCurrent.getStatus();
            String condition = dtCurrent.getCondition();
            
            CaseDetailsDataModel caseDetailsDataModel = new CaseDetailsDataModel();
            caseDetailsDataModel.setMsisdn(msisdn);
            caseDetailsDataModel.setHistory(history);
            caseDetailsDataModel.setTitle(title);
            caseDetailsDataModel.setCaseId(caseId);
            caseDetailsDataModel.setCreationTime(creationTime);
            caseDetailsDataModel.setStatus(status);
            caseDetailsDataModel.setCondition(condition);
            caseDetailsDataModel.setCaseTypeLvl1(caseTypeLvl1);
            caseDetailsDataModel.setCaseTypeLvl2(caseTypeLvl2);
            caseDetailsDataModel.setCaseTypeLvl3(caseTypeLvl3);
            
            // Check if the key is present
            // If present, then check if its value(arraylist) is not null
            //  If not null, add the item to the list
            //  If null, create the ArrayList, add as its value and then add the item to it
            // If not present, create the key and add with its first value as ArrayList
            
            if(caseDetailsDataMap.containsKey(dmNew)){
                ArrayList<CaseDetailsDataModel> listCurrent = caseDetailsDataMap.get(dmNew);
                if(listCurrent == null){
                    listCurrent = new ArrayList<CaseDetailsDataModel>();
                }
                listCurrent.add(caseDetailsDataModel);
            }else{
                ArrayList<CaseDetailsDataModel> listCurrent = new ArrayList<CaseDetailsDataModel>();
                listCurrent.add(caseDetailsDataModel);
                caseDetailsDataMap.put(dmNew, listCurrent);
            }
            
        }
        
        return caseDetailsDataMap;
    }
    
    private void setDataOnCaseDetailsTable(){
        TableView.TableViewSelectionModel<TopCasesDataModel> selectionModel = tblTopCasesListing.getSelectionModel();
        if(selectionModel.getSelectedIndices().size() > 1){
            tblCaseDetailsListing.getItems().clear();
            return;
        }
        TopCasesDataModel model = selectionModel.getSelectedItem();
        
        if(caseDetailsMap == null){
            return;
        }
        
        ArrayList<CaseDetailsDataModel> listCurrent = caseDetailsMap.get(model);
        if(listCurrent == null){
            return;
        }
        
        ObservableList<CaseDetailsDataModel> observableListCurrent = FXCollections.observableArrayList(listCurrent);
        tblCaseDetailsListing.getItems().clear();
        //initTableColumnsForCaseDetails();
        tblCaseDetailsListing.setItems(observableListCurrent);
    }
    
    private void checkBeforeClosing(){
        DialogFX dialog = new DialogFX(DialogFX.Type.QUESTION);
        dialog.setTitleText("Exit Application");
        dialog.setMessage("Are you sure you want to exit the application?");
        int buttonClicked = dialog.showDialog();
        
        if (buttonClicked == YES_BUTTON){
            Platform.exit();
        }
    }
    
    private void initTableColumnsForTopCasesListing() {
        colCaseTypeLvl1.setCellValueFactory(new PropertyValueFactory<TopCasesDataModel,String>("caseTypeLvl1"));
        colCaseTypeLvl2.setCellValueFactory(new PropertyValueFactory<TopCasesDataModel,String>("caseTypeLvl2"));
        colCaseTypeLvl3.setCellValueFactory(new PropertyValueFactory<TopCasesDataModel,String>("caseTypeLvl3"));
        colCount.setCellValueFactory(new PropertyValueFactory<TopCasesDataModel,Integer>("count"));
    }
    
    private void initTableColumnsForCaseDetails() {
        colMsisdn.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("msisdn"));
        colHistory.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("history"));
        colTitle.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("title"));
        colCaseId.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("caseId"));
        colCreated.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("creationTime"));
        colStatus.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("status"));
        colCondition.setCellValueFactory(new PropertyValueFactory<CaseDetailsDataModel,String>("condition"));
    }
    
    private void initTables() {
        colHistory.setVisible(false);
        
        reBuildTopCasesTable();        
        
        //tblCaseDetailsListing.getSelectionModel().setCellSelectionEnabled(true); // to select individual cells
        tblCaseDetailsListing.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);        
    }
    
    private void initRadioMenuItems(){
        // Radio menu items for Top cases
        radioMenuTop10.setUserData(new Integer(10));
        radioMenuTop15.setUserData(new Integer(15));
        radioMenuTop20.setUserData(new Integer(20));
        radioMenuTop25.setUserData(new Integer(25));
        
        // Radio menu items for Case conditon - Open or Open+Closed
        radioMenuCondOpen.setUserData(CaseCondition.OPEN);
        radioMenuCondOpenClosed.setUserData(CaseCondition.OPEN_PLUS_CLOSED);
        
        radioMenuLoadSummaryDetails.setUserData(SUMMARY_DETAIL);
        radioMenuLoadSummary.setUserData(SUMMARY);
        
    }
    
    private void initCustomControls(){
        picker = new CalendarTextField();
        picker.setValue(new GregorianCalendar());
        
        picker.setLayoutX(556.0);
        picker.setLayoutY(40.0);
        picker.setPrefWidth(100.0);
        AnchorPane.setRightAnchor(picker, 20.0);
        AnchorPane.setTopAnchor(picker, 46.0);
        
        Label lblDate = new Label("Report Date: ");
        lblDate.setLabelFor(picker);
        AnchorPane.setRightAnchor(lblDate, 135.0);
        AnchorPane.setTopAnchor(lblDate, 48.0);
        
        pneBarChart.getChildren().addAll(picker, lblDate);
        
    }
    
    public void exportToExcel() {
        lblErrorMessage.setText(null);
        
        if(topCaseTypes == null){
            lblErrorMessage.setText("No Data available to export");
            return;
        }
        
        if(topCaseTypes.isEmpty()){
            lblErrorMessage.setText("No Data available to export");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        
        //Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("MS Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        
        //Show open file dialog
        java.io.File file = fileChooser.showOpenDialog(null);
        
        if(file == null){
            //lblErrorMessage.setText("Please select filename for export to excel");
            return;
        }
        
        String path = null;
        try {
            path = file.getCanonicalPath();
        } catch (IOException ex) {
            lblErrorMessage.setText(ex.getMessage());
            Logger.getLogger(ViewTopCasesController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(!path.endsWith(XLSX)){
            path = path + XLSX;
        }
        
        //writeDetailsToCSV(file);
        boolean success = createExcelFile(path, topCaseTypes, caseList);
        
        if(success){
            lblErrorMessage.setText("Excel file created successfully");
        }
    }
    
    
    /*private void writeDetailsToCSV(java.io.File outFileName) {
     *
     * Writer writer = null;
     * try {
     * writer = new BufferedWriter(new FileWriter(outFileName));
     * for (CaseDetailsDt caseDetails : caseList) {
     *
     * String text = caseDetails.getMsisdn() + "," +
     * caseDetails.getTitle() + "," +
     * caseDetails.getHistory() + "\n";
     * writer.write(text);
     * }
     * } catch (Exception ex) {
     * ex.printStackTrace();
     * }
     * finally {
     * try {
     * writer.flush();
     * writer.close();
     * } catch (IOException ex) {
     * Logger.getLogger(ViewTopCasesController.class.getName()).log(Level.SEVERE, null, ex);
     * }
     * }
     * }*/
    
    
    private boolean createExcelFile(String excelWorkBookPath, ArrayList<TopCasesDt> topCaseTypes, ArrayList<CaseDetailsDt> caseDetailsList) {
        System.out.println("Entry:createExcelFile");
        Workbook wb = new XSSFWorkbook();
        //CreationHelper createHelper = wb.getCreationHelper();
        
        //Sheet Number 1 for MPT Summary
        Sheet topCasesSummarySheet = wb.createSheet("Top cases");
        
        //	Cell style for header row
        CellStyle cs = wb.createCellStyle();
        Font f = wb.createFont();
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);
        f.setFontHeightInPoints((short) 12);
        cs.setFont(f);
        
        createTopCasesExcelContents(topCaseTypes,topCasesSummarySheet,cs);
        
        //Sheet Number 2 - All MPT requests
        if(caseDetailsList != null){
            if(!caseDetailsList.isEmpty()){
                Sheet caseDetailsSheet = wb.createSheet("Case Details");
                createCaseDetailsOnExcel(caseDetailsList,caseDetailsSheet,cs);
            }
        }
        
        //	Write the output to a file
        FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream(excelWorkBookPath);
            wb.write(fileOut);
            fileOut.close();
        } catch (FileNotFoundException e) {
            lblErrorMessage.setText(e.getMessage());
            return false;
        } catch (IOException e) {
            lblErrorMessage.setText(e.getMessage());
            return false;
        }
        System.out.println("Exit:createExcelFile");
        return true;
    }
    
    private void createCaseDetailsOnExcel(ArrayList<CaseDetailsDt> caseDetailsList, Sheet caseDetailsSheet, CellStyle style) {
        System.out.println("Entry:createCaseDetailsOnExcel");
        int size = caseDetailsList.size();
        //SimpleDateFormat dtFormat = new SimpleDateFormat(DT_FORMAT);
        Row rowHeader = caseDetailsSheet.createRow((short)0);
        rowHeader.setRowStyle(style);
        
        Cell cell1 = rowHeader.createCell(0);
        cell1.setCellValue("MSISDN");
        cell1.setCellStyle(style);
        
        Cell cell2 = rowHeader.createCell(1);
        cell2.setCellValue("Title");
        cell2.setCellStyle(style);
        
        Cell cell3= rowHeader.createCell(2);
        cell3.setCellValue("History");
        cell3.setCellStyle(style);
        
        Cell cell4= rowHeader.createCell(3);
        cell4.setCellValue("Case Id");
        cell4.setCellStyle(style);
        
        Cell cell5= rowHeader.createCell(4);
        cell5.setCellValue("Created");
        cell5.setCellStyle(style);
        
        Cell cell6 = rowHeader.createCell(5);
        cell6.setCellValue("Status");
        cell6.setCellStyle(style);
        
        Cell cell7= rowHeader.createCell(6);
        cell7.setCellValue("Condition");
        cell7.setCellStyle(style);
        
        Cell cell8= rowHeader.createCell(7);
        cell8.setCellValue("Type Level 1");
        cell8.setCellStyle(style);
        
        Cell cell9= rowHeader.createCell(8);
        cell9.setCellValue("Type Level 2");
        cell9.setCellStyle(style);
        
        Cell cell10 = rowHeader.createCell(9);
        cell10.setCellValue("Type Level 3");
        cell10.setCellStyle(style);
        
        for (int i=0;i<size;i++) {
            CaseDetailsDt dt = caseDetailsList.get(i);
            //	Create a row and put some cells in it. Rows are 0 based.
            Row row = caseDetailsSheet.createRow((short)i+1);
            row.createCell(0).setCellValue(dt.getMsisdn());
            row.createCell(1).setCellValue(dt.getTitle());
            row.createCell(2).setCellValue(dt.getHistory());
            row.createCell(3).setCellValue(dt.getCaseId());
            row.createCell(4).setCellValue(dtFmt.format(dt.getCreationTime()));
            row.createCell(5).setCellValue(dt.getStatus());
            row.createCell(6).setCellValue(dt.getCondition());
            row.createCell(7).setCellValue(dt.getCaseTypeLvl1());
            row.createCell(8).setCellValue(dt.getCaseTypeLvl2());
            row.createCell(9).setCellValue(dt.getCaseTypeLvl3());
        }
        System.out.println("Exit:createCaseDetailsOnExcel");
    }
    
    private void createTopCasesExcelContents(ArrayList<TopCasesDt> topCasesDtList, Sheet topCasesSummarySheet, CellStyle style) {
        System.out.println("Entry:createTopCasesExcelContents");
        int size = topCasesDtList.size();
        Row rowHeader = topCasesSummarySheet.createRow((short)0);
        rowHeader.setRowStyle(style);
        
        Cell cell1 = rowHeader.createCell(0);
        cell1.setCellValue("Type Level 1");
        cell1.setCellStyle(style);
        
        Cell cell2 = rowHeader.createCell(1);
        cell2.setCellValue("Type Level 2");
        cell2.setCellStyle(style);
        
        Cell cell3 = rowHeader.createCell(2);
        cell3.setCellValue("Type Level 3");
        cell3.setCellStyle(style);
        
        Cell cell4 = rowHeader.createCell(3);
        cell4.setCellValue("Count");
        cell4.setCellStyle(style);
        
        for (int i=0;i<size;i++) {
            TopCasesDt dt = topCasesDtList.get(i);
            //	Create a row and put some cells in it. Rows are 0 based.
            Row row = topCasesSummarySheet.createRow((short)i+1);
            //	Create a cell and put a value in it.
            row.createCell(0).setCellValue(dt.getCaseTypeLvl1());
            //	Or do it on one line.
            row.createCell(1).setCellValue(dt.getCaseTypeLvl2());
            row.createCell(2).setCellValue(dt.getCaseTypeLvl3());
            row.createCell(3).setCellValue(dt.getCount());
        }
        System.out.println("Exit:createTopCasesExcelContents");
    }
    
    private void rebuildGraphControl() {
        
        if(barChartTopCases != null){
            pneBarChart.getChildren().remove(barChartTopCases);
        }
        
        xAxis = new NumberAxis();
        yAxis = new CategoryAxis();
        barChartTopCases = new BarChart<Number,String>(xAxis,yAxis);
        barChartTopCases.setPrefHeight(389.0);
        barChartTopCases.setPrefWidth(798.0);
        AnchorPane.setLeftAnchor(barChartTopCases, 0.0);
        AnchorPane.setRightAnchor(barChartTopCases, 0.0);
        AnchorPane.setTopAnchor(barChartTopCases, 56.0);
        AnchorPane.setBottomAnchor(barChartTopCases, 0.0);
        pneBarChart.getChildren().add(barChartTopCases);
    }
    
    public void copySelectedTopCasesToClipboard(){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        
        TableView.TableViewSelectionModel<TopCasesDataModel> selectionModel = tblTopCasesListing.getSelectionModel();
        ObservableList<TopCasesDataModel> selectedList = selectionModel.getSelectedItems();
        int selectedSize = selectedList.size();
        
        if(selectedSize == 0){
            return;
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < selectedSize; i++) {
            TopCasesDataModel currItem = selectedList.get(i);
            builder.append(currItem.getCaseTypeLvl1());
            builder.append(TAB);
            builder.append(currItem.getCaseTypeLvl2());
            builder.append(TAB);
            builder.append(currItem.getCaseTypeLvl3());
            builder.append(TAB);
            builder.append(currItem.getCount());
            builder.append(NEW_LINE);
        }
        
        content.putString(builder.toString());
        clipboard.setContent(content);
    }
    
    
    public void copySelectedCasesDetailsToClipboard(){
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        
        TableView.TableViewSelectionModel<CaseDetailsDataModel> selectionModel = tblCaseDetailsListing.getSelectionModel();
        ObservableList<CaseDetailsDataModel> selectedList = selectionModel.getSelectedItems();
        int selectedSize = selectedList.size();
        
        if(selectedSize == 0){
            return;
        }
        
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < selectedSize; i++) {
            CaseDetailsDataModel currItem = selectedList.get(i);
            builder.append(currItem.getMsisdn());
            builder.append(TAB);
            builder.append(currItem.getTitle());
            builder.append(TAB);
            builder.append(currItem.getCaseId());
            builder.append(TAB);
            builder.append(currItem.getCreationTime());
            builder.append(TAB);
            builder.append(currItem.getStatus());
            builder.append(TAB);
            builder.append(currItem.getCondition());
            builder.append(NEW_LINE);
        }
        
        content.putString(builder.toString());
        clipboard.setContent(content);
    }

    private void reBuildTopCasesTable() {
        
         if(tblTopCasesListing != null){
            pneTopCases.getChildren().remove(tblTopCasesListing);            
        }
        
        // Summary table being built with code - start
        tblTopCasesListing = new TableView<TopCasesDataModel>();
        tblTopCasesListing.setPrefWidth(355.0);
        tblTopCasesListing.setPrefHeight(248.0);
        AnchorPane.setRightAnchor(tblTopCasesListing, 0.0);    
        AnchorPane.setBottomAnchor(tblTopCasesListing, 0.0);    
        AnchorPane.setTopAnchor(tblTopCasesListing, 0.0);    
        AnchorPane.setLeftAnchor(tblTopCasesListing, 0.0);    
              
        colCaseTypeLvl1 = new TableColumn<TopCasesDataModel, String>("Type Level 1");
        colCaseTypeLvl1.setEditable(false);
        colCaseTypeLvl1.setPrefWidth(100.0);
        
        colCaseTypeLvl2 = new TableColumn<TopCasesDataModel, String>("Type Level 2");
        colCaseTypeLvl2.setEditable(false);
        colCaseTypeLvl2.setPrefWidth(150.0);
        
        colCaseTypeLvl3 = new TableColumn<TopCasesDataModel, String>("Type Level 3");
        colCaseTypeLvl3.setEditable(false);
        colCaseTypeLvl3.setPrefWidth(150.0);
        
        colCount = new TableColumn<TopCasesDataModel, Integer>("Count");
        colCount.setEditable(false);
        colCount.setPrefWidth(75.0);
                
        tblTopCasesListing.getColumns().addAll(colCaseTypeLvl1,colCaseTypeLvl2,colCaseTypeLvl3,colCount);
        
        ContextMenu ctxMenu = new ContextMenu();
        MenuItem ctxMenuItm = new MenuItem("Copy");
        ctxMenuItm.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                copySelectedTopCasesToClipboard();
            }
        });
        ctxMenu.getItems().add(ctxMenuItm);
        
        tblTopCasesListing.setContextMenu(ctxMenu);
        
        tblTopCasesListing.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        pneTopCases.getChildren().addAll(tblTopCasesListing);
        // Summary table being built with code - end
        
        initTableColumnsForTopCasesListing();
    }
}
