package com.example.visitor;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ResourceBundle;

public class IconController {

    @FXML
    private HBox center_HBOX;
    @FXML
    private ImageView image;


    Visitor visitor;
    FXMLLoader loader;
    BufferedImage icon;

    public IconController() {
    }


    public void initData(Visitor visitor, FXMLLoader loader, BufferedImage icon){
        this.visitor = visitor;
        this.loader = loader;
        Image image_converted = SwingFXUtils.toFXImage(icon, null);
        image.setImage(image_converted);
    }

//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//        Image image_converted = SwingFXUtils.toFXImage(icon, null);
//        image.setImage(image_converted);
//    }
}
