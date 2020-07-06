package application;

import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.*;


public class MainApp extends Application {
    private Stage primaryStage;
    private BorderPane rootLayout;


    // called at start of application
    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        this.primaryStage.setTitle("Community Selection using Edge Betweenness Centrality");

        try {
            // Load root layout from fxml
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            // min height and width calculated from components in TextAppLayout
            primaryStage.setMinHeight(540);//430);
            primaryStage.setMinWidth(990);//334);
            primaryStage.show();


        } catch (Exception e) {
            e.printStackTrace();
        }
        showTextProApp();
    }

    /**
     * Gets the temporary graph image to display
     */

    public ImageView getImage() {
        try {
            String localDir = System.getProperty("user.dir");
            File img = new File(localDir+"/src/application/temp/Test.png");
            BufferedImage imagein = ImageIO.read(img);
            Image image = SwingFXUtils.toFXImage(imagein,null);
            ImageView imv = new ImageView(image);
            imv.setFitHeight(150);
            imv.setFitWidth(150);
            return imv;
        } catch (IOException e){
            System.out.print("  exception");
        }
        return null;
    }

    public void showImage() {
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/ImageLayout.fxml"));
            VBox page = (VBox) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Community Visualization by Color");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);

            ImageView imv = getImage();
            imv.setFitHeight(384);
            imv.setFitWidth(512);
            imv.setY(500);
            HBox picregion = new HBox();
            picregion.getChildren().add(imv);
            picregion.setAlignment(Pos.CENTER);
            page.getChildren().add(picregion);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set reference to stage in controller
            LoadFileDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            // Show the dialog and wait until the user closes it
            dialogStage.show();
        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }

    }
    /**
     * Shows the main TextApplication scene
     */
    public void showTextProApp() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/TextAppLayout.fxml"));
            HBox textProPage = (HBox) loader.load();
            rootLayout.setCenter(textProPage);

            // Connect controller and main app
            TextProController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // SHOW NEW STAGE METHODS

    /**
     * Shows dialog for user input error
     *
     * @param inErr - message to dispaly
     */
    public void showInputErrorDialog(String inErr) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Input Error");
        alert.setContentText(inErr);

        alert.showAndWait();
    }

    public void showLoadFileDialog(TextArea ta) {
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("view/LoadFileLayout.fxml"));
            VBox page = (VBox) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Load File");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set reference to stage in controller
            LoadFileDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // give controller reference to text area to load file into
            controller.setTextArea(ta);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }

    }

    // MAIN
    public static void main(String[] args) {
        launch(args);
    }

    public Stage getStage() {
        return this.primaryStage;
    }

}