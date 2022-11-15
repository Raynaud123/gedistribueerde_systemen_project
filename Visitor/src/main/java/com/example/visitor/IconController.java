package com.example.visitor;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class IconController implements Initializable {

    Visitor visitor;
    FXMLLoader loader;
    BufferedImage icon;

    public IconController() {
    }


    public void initData(Visitor visitor, FXMLLoader loader, BufferedImage icon){
        this.visitor = visitor;
        this.loader = loader;
        this.icon = icon;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
