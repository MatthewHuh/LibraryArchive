package controller;

import POJO.CommonObjs;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;

public class HomeContentController {

    private CommonObjs commonObjs = CommonObjs.getInstance();

    public void handelSearch(ActionEvent actionEvent) {
        URL url = getClass().getResource("/view/Search.fxml");
        try{
            BorderPane vbox = (BorderPane) FXMLLoader.load(url);

            commonObjs.getBorderPane().setCenter(vbox);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
