package com.example.imagemanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ImageManagement extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        //this code is compatible with java sdk 17
        //Command design pattern is used in this application
        //main-view fxml is main GUI
        //FileChooser is used to upload the required images
        //Uploading images could be one or multiple.
        //Images are downloaded to same location from where images are uploaded.

        FXMLLoader fxmlLoader = new FXMLLoader(ImageManagement.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Image Management Tool");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}