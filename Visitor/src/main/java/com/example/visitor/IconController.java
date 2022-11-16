package com.example.visitor;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class IconController{

    @FXML
    private HBox center_HBOX;
    @FXML
    private ImageView image;


    Visitor visitor;
    FXMLLoader loader;
    BufferedImage icon;

    public IconController() {
    }


    public void initData(Visitor visitor, FXMLLoader loader, BufferedImage icon) {
        this.visitor = visitor;
        this.loader = loader;
        Image image_converted = SwingFXUtils.toFXImage(icon, null);
        image.setImage(image_converted);
        Timer timer = new Timer ();
        TimerTask hourlyTask = new TimerTask () {
            @Override
            public void run () {
                try {
                    visitor.flushCapsules();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };
        //flushCapsules from visitor every Hour
        timer.schedule (hourlyTask, 0L, 1000*60*60);
    }
}
