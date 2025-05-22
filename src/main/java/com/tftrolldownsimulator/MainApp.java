package com.tftrolldownsimulator;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApp extends Application {

  public static void main(String[] args) {
    Application.launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    primaryStage.setTitle("TFT Rolldown Simulator");
    Font.loadFont(getClass().getResource("/fonts/BeaufortforLOL-Regular.otf").toExternalForm(), 12);
    Font.loadFont(getClass().getResource("/fonts/BeaufortforLOL-Bold.otf").toExternalForm(), 12);
    Font.loadFont(getClass().getResource("/fonts/Spiegel-Regular.otf").toExternalForm(), 12);

    Scene mainScene = new Scene(FXMLLoader.load(getClass().getResource("/views/main-view.fxml")), 1280, 720);

    primaryStage.setScene(mainScene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }
}
