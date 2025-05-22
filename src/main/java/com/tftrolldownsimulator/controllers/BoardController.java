package com.tftrolldownsimulator.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;

public class BoardController {

  @FXML
  private Pane board;

  private static final double HEX_SIZE = 30; // Radius of the hexagon
  private static final double HEX_WIDTH = Math.sqrt(3) * 1.2 * HEX_SIZE;
  private static final double HEX_HEIGHT = 2 * 1.2 * HEX_SIZE;
  private static final double BASE_Y = 50;

  public void initialize() {
    createHexGrid(8, 4); // Example: 10 columns, 5 rows
  }

  private void createHexGrid(int cols, int rows) {
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        // Calculate x and y for each hexagon's center
        double x = col * HEX_WIDTH; // Horizontal spacing
        double y = row * HEX_HEIGHT * 0.75 + BASE_Y; // Vertical spacing

        if (row % 2 == 1) {
          x += HEX_WIDTH / 2;
        }

        Polygon hexagon = createHexagon(x, y);
        board.getChildren().add(hexagon);
      }
    }
  }

  private Polygon createHexagon(double x, double y) {
    Polygon hex = new Polygon();
    for (int i = 0; i < 6; i++) {
      double angle = Math.toRadians(60 * i);
      double px = x + HEX_SIZE * Math.cos(angle);
      double py = y + HEX_SIZE * Math.sin(angle);
      hex.getPoints().addAll(px, py);
    }
    hex.setRotate(30);
    hex.setStyle("-fx-fill: lightblue; -fx-stroke: black;");
    return hex;
  }
}
