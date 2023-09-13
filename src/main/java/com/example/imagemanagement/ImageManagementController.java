package com.example.imagemanagement;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

public class ImageManagementController {

    @FXML
    TilePane tilepane;


    static int choiceCount = 0;
    static  int downlaodCount = 0;
    int count =0;
    private int nRows = 5;  //no of row for tile pane
    private int nCols = 5;  // no of column for tile pane

    private static final double ELEMENT_SIZE = 100;
    private static final double GAP = ELEMENT_SIZE / 10;

     Window primaryStage;
    final FileChooser fileChooser = new FileChooser();
    List<File> list1 = new ArrayList<>();

    //To upload images and show thumnails of images as well details.
    public void uploadPicture(ActionEvent ae){

        //only these image formats are allowed to upload
        configureFileChooser(fileChooser);

        list1 = fileChooser.showOpenMultipleDialog(primaryStage);

        //creates a tilepane (5*5 tiles). Each image and its details are fit into 1 tile of tilepane
        createElements();

    }

    //To change format of images and download images.
    public void convertF(ActionEvent ae) throws IOException {

        ChoiceBox choiceBox = new ChoiceBox<>();
        Button download = new Button();
        HBox ac = new HBox();
        Label msg = new Label();
        choiceBox.getItems().add("JPEG");
        choiceBox.getItems().add("PNG");
        choiceBox.getItems().add("BMP");
        choiceBox.getItems().add("GIF");

        final String[] formatname = new String[1];
        choiceBox.setOnAction((event) -> {

            String value = (String) choiceBox.getValue();
            formatname[0] = value;
            final String[] inputFile = new String[1];

            //to download images
            download.setOnAction( new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent actionEvent) {
                    for(File file : list1) {
                        inputFile[0] = "/" + file.getAbsolutePath();
                        String name = inputFile[0].split("\\.")[0];
                        String outputFile = "/" + name + "." + formatname[0];
                        try {
                            BufferedImage image = ImageIO.read(file);
                            File output = new File(outputFile);
                            ImageIO.write(image, formatname[0], output);
                            msg.setText("images downloaded.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    downlaodCount = 1;
                    choiceCount = 1;
                }

            });

        });
        choiceBox.setValue("FORMAT TYPES");
        download.setText("Download");
        msg.setAlignment(Pos.BASELINE_CENTER);
        ac.getChildren().add(choiceBox);
        ac.getChildren().add(download);
        ac.getChildren().add(msg);
        ac.setAlignment(Pos.CENTER);
        ac.setSpacing(50);
        Scene scene2 = new Scene(ac, 600, 400);
        Stage stage2 = new Stage();
        stage2.setTitle("Details");
        stage2.setScene(scene2);
        stage2.show();
        if(downlaodCount >=1){
            download.setDisable(true);
        }
        if(choiceCount >=1){
            choiceBox.setDisable(true);
            stage2.close();
        }
    }

    //creates a tilepane (5*5 tiles). Each image and its details are fit into 1 tile of tilepane
    private void createElements() {
        tilepane.setPrefColumns(nCols);
        tilepane.setPrefRows(nRows);
        tilepane.setHgap(GAP);
        tilepane.setVgap(GAP);
        tilepane.getChildren().clear();
        tilepane.setAlignment(Pos.CENTER);
        for (int i = 0; i < nCols; i++) {
            for (int j = 0; j < nRows; j++) {

                //To show image thumnails ad details of images
                tilepane.getChildren().add(createPage(count));
                count++;

            }
        }
    }

    //To show image thumnails ad details of images
    public VBox createPage(int index) {

        ImageView imageView = new ImageView();
        Label h1 = new Label();
        Label w1 = new Label();
        Label details = new Label();
        Label gps = new Label();
        Label format = new Label();

        if(list1.size()>index){
            File file = list1.get(index);
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                Image image = new Image(file.toURI().toString());
                imageView.setImage(image);
                imageView.setFitWidth(ELEMENT_SIZE);
                imageView.setFitHeight(ELEMENT_SIZE);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);

                ImageInputStream iis = ImageIO.createImageInputStream(file);
                Iterator<ImageReader> imageReaders = ImageIO.getImageReaders(iis);

                while (imageReaders.hasNext()) {
                    ImageReader reader = (ImageReader) imageReaders.next();
                    format.setText("Format :"+reader.getFormatName());

                }
                javaxt.io.Image img = new javaxt.io.Image(file);
                java.util.HashMap<Integer, Object> exif = img.getExifTags();


                //Image Orientation
                int orientation = 0;
                String desc = "";
                try{
                    orientation = (Integer) exif.get(0x0112);
                    desc = "";
                    switch (orientation) {
                        case 1: desc = "Top, left side"; break;
                        case 2: desc = "Top, right side"; break;
                        case 3: desc = "Bottom, right side"; break;
                        case 4: desc = "Bottom, left side"; break;
                        case 5: desc = "Left side, top"; break;
                        case 6: desc = "Right side, top"; break;
                        case 7: desc = "Right side, bottom"; break;
                        case 8: desc = "Left side, bottom"; break;
                    }

                }
                catch(Exception e){
                }


                //Print GPS Information
                double[] coord = img.getGPSCoordinate();
                if (coord!=null){
                    gps.setText("GPS XCoordinate: " + coord[0] + "\nGPS YCoordinate: " + coord[0]);
                }
                else{
                    gps.setText("GPS XCoordinate: null" + "\nGPS YCoordinate: null");
                }

                h1.setText("height = " + String.valueOf(image.getHeight()));
                w1.setText("width = " + String.valueOf(image.getWidth()));
                details.setText("Date: " + exif.get(0x0132)+
                        "\nCamera: " + exif.get(0x0110) +
                        "\nManufacturer: " + exif.get(0x010F)+
                        "\nResolution: " + exif.get(0x011A)+" * "+exif.get(0x011B) +
                        "\nFocal Length: " + exif.get(0x920A)+
                        "\nExposure Time: " + exif.get(0x829A)+
                        "\nOrientation: " + orientation + " -- " + desc);


            } catch (IOException ex) {

            }
        }

        VBox pageBox = new VBox();
        pageBox.getChildren().add(imageView);
        pageBox.getChildren().add(format);
        pageBox.getChildren().add(h1);
        pageBox.getChildren().add(w1);
        pageBox.getChildren().add(details);
        pageBox.getChildren().add(gps);

        //imageView = null;
        return pageBox;
    }


    //only these image formats are allowed to upload
    private void configureFileChooser(FileChooser fileChooser) {
        fileChooser.setTitle("View Pictures");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("GIF", "*.gif")

        );
    }

}