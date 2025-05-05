package POJO;

import javafx.scene.layout.BorderPane;

public class CommonObjs {

    private static CommonObjs commonObjs = new CommonObjs();

    private BorderPane borderPane;

    public static CommonObjs getInstance() {
        return commonObjs;
    }

    public BorderPane getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(BorderPane borderPane) {
        this.borderPane = borderPane;
    }
}
