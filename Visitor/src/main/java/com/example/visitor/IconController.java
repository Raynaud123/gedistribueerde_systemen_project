package com.example.visitor;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

public class IconController{

    @FXML
    private ImageView image;

    private TimerTask hourlyTask;
    private Stage stage;
    private Visitor visitor;
    private Timer timer;

    public IconController() {
    }


    public void initData(Visitor visitor, BufferedImage icon, Stage stage) {
        this.visitor = visitor;
        this.stage = stage;
        Image image_converted = SwingFXUtils.toFXImage(icon, null);
        image.setImage(image_converted);
        timer = new Timer ();
        hourlyTask = new TimerTask () {
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


    public void handleLogout() throws IOException {
        //Stop Tasks
        hourlyTask.cancel();
        timer.cancel();

        //Switch scenes
        FXMLLoader loader = new FXMLLoader(getClass().getResource("QRCode.fxml"));
        stage.getScene().setRoot(loader.load());

        QRCodeController controller = loader.getController();
        controller.initData(visitor, stage);
        stage.show();

    }

    public void flushCapsules() throws RemoteException {
        visitor.flushCapsules();
    }

}
