import java.util.Random;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class BarChartSampleTop10Cases extends Application {
    
    private BarChart<Number,String> bc;
    final NumberAxis xAxis = new NumberAxis();
    final CategoryAxis yAxis = new CategoryAxis();
    
    @Override public void start(Stage stage) {
        stage.setTitle("Bar Chart Sample");
        
        Button btnRefresh = new Button("Load");
        btnRefresh.setOnAction(new EventHandler<ActionEvent>(){

            @Override
            public void handle(ActionEvent event) {
                drawBarChart();
            }
        });     
                
        Button btnClear = new Button("Clear");
        btnClear.setOnAction(new EventHandler<ActionEvent>() {

             @Override
            public void handle(ActionEvent event) {
                drawEmptyBarChart();
            }
        });
        
        bc = new BarChart<Number, String>(xAxis, yAxis);
        
        drawBarChart();
        
        VBox vbox = new VBox();
        vbox.setSpacing(15);
        vbox.getChildren().addAll(btnRefresh,btnClear,bc);
        
        Scene scene  = new Scene(vbox,800,600);
        stage.setScene(scene);
        stage.show();
        
    }
 
    public static void main(String[] args) {
        launch(args);
    }

    private void drawBarChart() {
        //bc = new BarChart<Number,String>(xAxis,yAxis);
        
        
        bc.getData().clear();
        
        bc.setTitle("Country Summary");
        xAxis.setLabel("Value");  
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Country");        
        
        String [] caseTypes = {"COMPLAINT:TARIFF:NOT APPLIED",
                                "COMPLAINT:RELOAD:PROGRAM",
                                "COMPLAINT:BLACKBERRY:LOWERCASE",
                                "COMPLAINT:GPRS CONNECTION:ACCESS INTERNET FAILED",
                                "COMPLAINT:NETWORK:UNSTABLE SIGNAL",
                                "COMPLAINT:BLACKBERRY:CAN'T/SLOW ACCESS BBM",
                                "COMPLAINT:AO:CAN'T REG/UNREG",
                                "COMPLAINT:VAS:LBS",
                                "PARENT CASE:BILLING:PROBLEM",
                                "COMPLAINT:NETWORK:CALL QUALITY"};
       
        XYChart.Series series = new XYChart.Series();
        series.setName("Top 10 cases");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            XYChart.Data data = new XYChart.Data(random.nextInt(50), caseTypes[i]);
            series.getData().add(data);
        }
        
        bc.getData().add(series);
    }
    
    private void drawEmptyBarChart() {
        //bc = new BarChart<Number,String>(xAxis,yAxis);
        
        
        bc.getData().clear();
        
        bc.setTitle("Country Summary");
        xAxis.setLabel("Value");  
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Country");        
        
        String [] caseTypes = {"COMPLAINT:TARIFF:NOT APPLIED",
                                "COMPLAINT:RELOAD:PROGRAM",
                                "COMPLAINT:BLACKBERRY:LOWERCASE",
                                "COMPLAINT:GPRS CONNECTION:ACCESS INTERNET FAILED",
                                "COMPLAINT:NETWORK:UNSTABLE SIGNAL",
                                "COMPLAINT:BLACKBERRY:CAN'T/SLOW ACCESS BBM",
                                "COMPLAINT:AO:CAN'T REG/UNREG",
                                "COMPLAINT:VAS:LBS",
                                "PARENT CASE:BILLING:PROBLEM",
                                "COMPLAINT:NETWORK:CALL QUALITY"};
       
        XYChart.Series series = new XYChart.Series();
        series.setName("Top 10 cases");
        for (int i = 0; i < 10; i++) {
            XYChart.Data data = new XYChart.Data(0, caseTypes[i]);
            series.getData().add(data);
        }
        
        bc.getData().add(series);
    }
    
}