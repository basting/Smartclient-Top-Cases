/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bgh.crm.top.cases.app;

import com.bgh.crm.top.cases.ViewTopCasesController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import jfxtras.labs.dialogs.DialogFX;

/**
 *
 * @author BASTING
 */
public class ViewTopCasesMain extends Application {
    
    private final int YES_BUTTON = 0;
    
    
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/com/bgh/crm/top/cases/ViewTopCases.fxml"));
            //final ViewTopCasesController controller = (ViewTopCasesController)loader.getController();
                        
            primaryStage.setTitle("Top Cases");
            javafx.stage.Screen screen = Screen.getPrimary();
            javafx.geometry.Rectangle2D rectangle2D = screen.getVisualBounds();
            primaryStage.setScene(new Scene(root, rectangle2D.getWidth()-50, rectangle2D.getHeight()-50));
            //primaryStage.initStyle(StageStyle.TRANSPARENT);
                        
            primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<Event>() {

                @Override
                public void handle(Event event) {
                    //controller.exitApp();
                    event.consume();
                    checkBeforeClosing();
                }
                
                
            });
            primaryStage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(ViewTopCasesMain.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
    