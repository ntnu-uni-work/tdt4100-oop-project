package com.tftrolldownsimulator.controllers;

import java.util.List;

import com.tftrolldownsimulator.models.BoardUnit;
import com.tftrolldownsimulator.models.Keybinding;
import com.tftrolldownsimulator.models.Tactician;
import com.tftrolldownsimulator.models.Unit;
import com.tftrolldownsimulator.persistence.Keybinds;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainController {
  @FXML
  ImageView rerollImage;

  @FXML
  ImageView buyXPImage;

  @FXML
  Button buyXPButton;

  @FXML
  Button rerollButton;

  @FXML
  BorderPane root;

  @FXML
  private Text xpProgressLabel, keybindsLabel, levelLabel, goldLabel, odds1CostLabel, odds2CostLabel, odds3CostLabel,
      odds4CostLabel,
      odds5CostLabel;

  @FXML
  HBox benchContainer, shopContainer;

  @FXML
  VBox keybindsContainer;

  @FXML
  AnchorPane shopUnitContainer1, shopUnitContainer2, shopUnitContainer3, shopUnitContainer4, shopUnitContainer5;

  @FXML
  ImageView shopUnitImage1, shopUnitImage2, shopUnitImage3, shopUnitImage4, shopUnitImage5, shopUnitCostImage1,
      shopUnitCostImage2, shopUnitCostImage3, shopUnitCostImage4, shopUnitCostImage5, shopUnitTrait1Image1,
      shopUnitTrait2Image1, shopUnitTrait3Image1, shopUnitTrait1Image2, shopUnitTrait2Image2, shopUnitTrait3Image2,
      shopUnitTrait1Image3, shopUnitTrait2Image3, shopUnitTrait3Image3, shopUnitTrait1Image4, shopUnitTrait2Image4,
      shopUnitTrait3Image4, shopUnitTrait1Image5, shopUnitTrait2Image5, shopUnitTrait3Image5;

  @FXML
  Text shopUnitName1, shopUnitName2, shopUnitName3, shopUnitName4, shopUnitName5, shopUnitTrait1Label1,
      shopUnitTrait2Label1, shopUnitTrait3Label1, shopUnitTrait1Label2, shopUnitTrait2Label2, shopUnitTrait3Label2,
      shopUnitTrait1Label3, shopUnitTrait2Label3, shopUnitTrait3Label3, shopUnitTrait1Label4, shopUnitTrait2Label4,
      shopUnitTrait3Label4, shopUnitTrait1Label5, shopUnitTrait2Label5, shopUnitTrait3Label5, shopUnitCostLabel1,
      shopUnitCostLabel2, shopUnitCostLabel3, shopUnitCostLabel4, shopUnitCostLabel5;

  @FXML
  HBox shopUnitTrait1Container1, shopUnitTrait2Container1, shopUnitTrait1Container2, shopUnitTrait2Container2,
      shopUnitTrait1Container3, shopUnitTrait2Container3, shopUnitTrait1Container4, shopUnitTrait2Container4,
      shopUnitTrait1Container5, shopUnitTrait2Container5;

  private StringProperty xpProgressText = new SimpleStringProperty();
  private StringProperty levelText = new SimpleStringProperty();
  private StringProperty goldText = new SimpleStringProperty();
  private StringProperty odds1CostText = new SimpleStringProperty();
  private StringProperty odds2CostText = new SimpleStringProperty();
  private StringProperty odds3CostText = new SimpleStringProperty();
  private StringProperty odds4CostText = new SimpleStringProperty();
  private StringProperty odds5CostText = new SimpleStringProperty();

  private Tactician tactician;

  private Keybinds keybinds;

  @FXML
  public void initialize() {
    tactician = new Tactician();
    tactician.setGold();

    keybinds = new Keybinds(true);

    update();

    xpProgressLabel.textProperty().bind(xpProgressText);
    levelLabel.textProperty().bind(levelText);
    goldLabel.textProperty().bind(goldText);
    odds1CostLabel.textProperty().bind(odds1CostText);
    odds2CostLabel.textProperty().bind(odds2CostText);
    odds3CostLabel.textProperty().bind(odds3CostText);
    odds4CostLabel.textProperty().bind(odds4CostText);
    odds5CostLabel.textProperty().bind(odds5CostText);

    enableBuyXP();
    enableReroll();
  }

  private void update() {
    xpProgressText.set(tactician.getXPProgress());
    levelText.set(String.format("Lvl. %d", tactician.getLevel()));
    goldText.set(String.valueOf(tactician.getGold()));

    List<Double> odds = tactician.getShopOdds();

    odds1CostText.set(String.format("%.0f%%%n", odds.get(0) * 100));
    odds2CostText.set(String.format("%.0f%%%n", odds.get(1) * 100));
    odds3CostText.set(String.format("%.0f%%%n", odds.get(2) * 100));
    odds4CostText.set(String.format("%.0f%%%n", odds.get(3) * 100));
    odds5CostText.set(String.format("%.0f%%%n", odds.get(4) * 100));

    if (!tactician.canBuyXP()) {
      disableBuyXP();
    }

    if (!tactician.canReroll()) {
      disableReroll();
    }

    BoardUnit[] bench = tactician.getBenchUnits();
    List<Node> benchElements = benchContainer.getChildren();

    for (int i = 0; i < bench.length; i++) {
      BoardUnit unit = bench[i];
      AnchorPane benchElement = (AnchorPane) ((AnchorPane) benchElements.get(i)).getChildren().get(0);

      if (unit == null) {
        benchElement.setVisible(false);
        continue;
      }

      ImageView benchImage = (ImageView) benchElement.getChildren().get(0);
      benchImage.setImage(new Image("/assets/units/icons/" + unit.getIcon()));

      Text benchLabel = (Text) ((HBox) benchElement.getChildren().get(1)).getChildren().get(0);
      benchLabel.setText(unit.getName());

      ImageView benchStarLevel = (ImageView) ((HBox) benchElement.getChildren().get(2)).getChildren().get(0);
      benchStarLevel
          .setImage(new Image(
              "/assets/stars/" + unit.getStarLevel() + "-star" + (unit.getStarLevel() == 1 ? "" : "s") + ".png"));
      benchElement.setVisible(true);
    }

    updateShop();

    List<Node> keybindElements = keybindsContainer.getChildren();

    for (int i = 0; i < keybindElements.size(); i++) {
      Text keybindLabelElement = (Text) ((HBox) keybindElements.get(i)).getChildren().get(0);
      Button keybindElement = (Button) ((HBox) keybindElements.get(i)).getChildren().get(1);

      Keybinding keybind = Keybinding.fromLabel(keybindLabelElement.getText());
      keybindElement.setText(keybinds.getState().get(keybind).toString());
    }

    keybindsLabel.setText("");
  }

  private void updateShop() {
    Unit[] shopUnits = tactician.getShopUnits();

    if (shopUnits[0] == null) {
      shopUnitContainer1.setVisible(false);
    } else {
      shopUnitImage1.setImage(new Image("/assets/units/covers/" + shopUnits[0].getIcon()));
      shopUnitName1.setText(shopUnits[0].getName());
      shopUnitCostImage1.setImage(new Image("/assets/shopoverlays/" + shopUnits[0].getCost() + "-cost"
          + (tactician.getGold() < shopUnits[0].getCost() ? "-disabled" : "") + ".png"));
      shopUnitCostLabel1.setText(String.valueOf(shopUnits[0].getCost()));
      if (shopUnits[0].getTraits().size() == 1) {
        shopUnitTrait1Container1.setVisible(false);
        shopUnitTrait2Container1.setVisible(false);
        shopUnitTrait3Image1.setImage(new Image("/assets/traiticons/" + shopUnits[0].getTraits().get(0).getLogo()));
        shopUnitTrait3Label1.setText(shopUnits[0].getTraits().get(0).getValue());
      } else if (shopUnits[0].getTraits().size() == 2) {
        shopUnitTrait1Container1.setVisible(false);
        shopUnitTrait2Image1.setImage(new Image("/assets/traiticons/" + shopUnits[0].getTraits().get(0).getLogo()));
        shopUnitTrait2Label1.setText(shopUnits[0].getTraits().get(0).getValue());
        shopUnitTrait3Image1.setImage(new Image("/assets/traiticons/" + shopUnits[0].getTraits().get(1).getLogo()));
        shopUnitTrait3Label1.setText(shopUnits[0].getTraits().get(1).getValue());
        shopUnitTrait2Container1.setVisible(true);
      } else if (shopUnits[0].getTraits().size() == 3) {
        shopUnitTrait1Image1.setImage(new Image("/assets/traiticons/" + shopUnits[0].getTraits().get(0).getLogo()));
        shopUnitTrait1Label1.setText(shopUnits[0].getTraits().get(0).getValue());
        shopUnitTrait2Image1.setImage(new Image("/assets/traiticons/" + shopUnits[0].getTraits().get(1).getLogo()));
        shopUnitTrait2Label1.setText(shopUnits[0].getTraits().get(1).getValue());
        shopUnitTrait3Image1.setImage(new Image("/assets/traiticons/" + shopUnits[0].getTraits().get(2).getLogo()));
        shopUnitTrait3Label1.setText(shopUnits[0].getTraits().get(2).getValue());
        shopUnitTrait1Container1.setVisible(true);
        shopUnitTrait2Container1.setVisible(true);
      }
      shopUnitContainer1.setVisible(true);
    }

    if (shopUnits[1] == null) {
      shopUnitContainer2.setVisible(false);
    } else {
      shopUnitImage2.setImage(new Image("/assets/units/covers/" + shopUnits[1].getIcon()));
      shopUnitName2.setText(shopUnits[1].getName());
      shopUnitCostImage2.setImage(new Image("/assets/shopoverlays/" + shopUnits[1].getCost() + "-cost"
          + (tactician.getGold() < shopUnits[1].getCost() ? "-disabled" : "") + ".png"));
      shopUnitCostLabel2.setText(String.valueOf(shopUnits[1].getCost()));
      if (shopUnits[1].getTraits().size() == 1) {
        shopUnitTrait1Container2.setVisible(false);
        shopUnitTrait2Container2.setVisible(false);
        shopUnitTrait3Image2.setImage(new Image("/assets/traiticons/" + shopUnits[1].getTraits().get(0).getLogo()));
        shopUnitTrait3Label2.setText(shopUnits[1].getTraits().get(0).getValue());
      } else if (shopUnits[1].getTraits().size() == 2) {
        shopUnitTrait1Container2.setVisible(false);
        shopUnitTrait2Image2.setImage(new Image("/assets/traiticons/" + shopUnits[1].getTraits().get(0).getLogo()));
        shopUnitTrait2Label2.setText(shopUnits[1].getTraits().get(0).getValue());
        shopUnitTrait3Image2.setImage(new Image("/assets/traiticons/" + shopUnits[1].getTraits().get(1).getLogo()));
        shopUnitTrait3Label2.setText(shopUnits[1].getTraits().get(1).getValue());
        shopUnitTrait2Container2.setVisible(true);
      } else if (shopUnits[1].getTraits().size() == 3) {
        shopUnitTrait1Image2.setImage(new Image("/assets/traiticons/" + shopUnits[1].getTraits().get(0).getLogo()));
        shopUnitTrait1Label2.setText(shopUnits[1].getTraits().get(0).getValue());
        shopUnitTrait2Image2.setImage(new Image("/assets/traiticons/" + shopUnits[1].getTraits().get(1).getLogo()));
        shopUnitTrait2Label2.setText(shopUnits[1].getTraits().get(1).getValue());
        shopUnitTrait3Image2.setImage(new Image("/assets/traiticons/" + shopUnits[1].getTraits().get(2).getLogo()));
        shopUnitTrait3Label2.setText(shopUnits[1].getTraits().get(2).getValue());
        shopUnitTrait1Container2.setVisible(true);
        shopUnitTrait2Container2.setVisible(true);
      }
      shopUnitContainer2.setVisible(true);
    }

    if (shopUnits[2] == null) {
      shopUnitContainer3.setVisible(false);
    } else {
      shopUnitImage3.setImage(new Image("/assets/units/covers/" + shopUnits[2].getIcon()));
      shopUnitName3.setText(shopUnits[2].getName());
      shopUnitCostImage3.setImage(new Image("/assets/shopoverlays/" + shopUnits[2].getCost() + "-cost"
          + (tactician.getGold() < shopUnits[2].getCost() ? "-disabled" : "") + ".png"));
      shopUnitCostLabel3.setText(String.valueOf(shopUnits[2].getCost()));
      if (shopUnits[2].getTraits().size() == 1) {
        shopUnitTrait1Container3.setVisible(false);
        shopUnitTrait2Container3.setVisible(false);
        shopUnitTrait3Image3.setImage(new Image("/assets/traiticons/" + shopUnits[2].getTraits().get(0).getLogo()));
        shopUnitTrait3Label3.setText(shopUnits[2].getTraits().get(0).getValue());
      } else if (shopUnits[2].getTraits().size() == 2) {
        shopUnitTrait1Container3.setVisible(false);
        shopUnitTrait2Image3.setImage(new Image("/assets/traiticons/" + shopUnits[2].getTraits().get(0).getLogo()));
        shopUnitTrait2Label3.setText(shopUnits[2].getTraits().get(0).getValue());
        shopUnitTrait3Image3.setImage(new Image("/assets/traiticons/" + shopUnits[2].getTraits().get(1).getLogo()));
        shopUnitTrait3Label3.setText(shopUnits[2].getTraits().get(1).getValue());
        shopUnitTrait2Container3.setVisible(true);
      } else if (shopUnits[2].getTraits().size() == 3) {
        shopUnitTrait1Image3.setImage(new Image("/assets/traiticons/" + shopUnits[2].getTraits().get(0).getLogo()));
        shopUnitTrait1Label3.setText(shopUnits[2].getTraits().get(0).getValue());
        shopUnitTrait2Image3.setImage(new Image("/assets/traiticons/" + shopUnits[2].getTraits().get(1).getLogo()));
        shopUnitTrait2Label3.setText(shopUnits[2].getTraits().get(1).getValue());
        shopUnitTrait3Image3.setImage(new Image("/assets/traiticons/" + shopUnits[2].getTraits().get(2).getLogo()));
        shopUnitTrait3Label3.setText(shopUnits[2].getTraits().get(2).getValue());
        shopUnitTrait1Container3.setVisible(true);
        shopUnitTrait2Container3.setVisible(true);
      }
      shopUnitContainer3.setVisible(true);
    }

    if (shopUnits[3] == null) {
      shopUnitContainer4.setVisible(false);
    } else {
      shopUnitImage4.setImage(new Image("/assets/units/covers/" + shopUnits[3].getIcon()));
      shopUnitName4.setText(shopUnits[3].getName());
      shopUnitCostImage4.setImage(new Image("/assets/shopoverlays/" + shopUnits[3].getCost() + "-cost"
          + (tactician.getGold() < shopUnits[3].getCost() ? "-disabled" : "") + ".png"));
      shopUnitCostLabel4.setText(String.valueOf(shopUnits[3].getCost()));
      if (shopUnits[3].getTraits().size() == 1) {
        shopUnitTrait1Container4.setVisible(false);
        shopUnitTrait2Container4.setVisible(false);
        shopUnitTrait3Image4.setImage(new Image("/assets/traiticons/" + shopUnits[3].getTraits().get(0).getLogo()));
        shopUnitTrait3Label4.setText(shopUnits[3].getTraits().get(0).getValue());
      } else if (shopUnits[3].getTraits().size() == 2) {
        shopUnitTrait1Container4.setVisible(false);
        shopUnitTrait2Image4.setImage(new Image("/assets/traiticons/" + shopUnits[3].getTraits().get(0).getLogo()));
        shopUnitTrait2Label4.setText(shopUnits[3].getTraits().get(0).getValue());
        shopUnitTrait3Image4.setImage(new Image("/assets/traiticons/" + shopUnits[3].getTraits().get(1).getLogo()));
        shopUnitTrait3Label4.setText(shopUnits[3].getTraits().get(1).getValue());
        shopUnitTrait2Container4.setVisible(true);
      } else if (shopUnits[3].getTraits().size() == 3) {
        shopUnitTrait1Image4.setImage(new Image("/assets/traiticons/" + shopUnits[3].getTraits().get(0).getLogo()));
        shopUnitTrait1Label4.setText(shopUnits[3].getTraits().get(0).getValue());
        shopUnitTrait2Image4.setImage(new Image("/assets/traiticons/" + shopUnits[3].getTraits().get(1).getLogo()));
        shopUnitTrait2Label4.setText(shopUnits[3].getTraits().get(1).getValue());
        shopUnitTrait3Image4.setImage(new Image("/assets/traiticons/" + shopUnits[3].getTraits().get(2).getLogo()));
        shopUnitTrait3Label4.setText(shopUnits[3].getTraits().get(2).getValue());
        shopUnitTrait1Container4.setVisible(true);
        shopUnitTrait2Container4.setVisible(true);
      }
      shopUnitContainer4.setVisible(true);
    }

    if (shopUnits[4] == null) {
      shopUnitContainer5.setVisible(false);
    } else {
      shopUnitImage5.setImage(new Image("/assets/units/covers/" + shopUnits[4].getIcon()));
      shopUnitName5.setText(shopUnits[4].getName());
      shopUnitCostImage5.setImage(new Image("/assets/shopoverlays/" + shopUnits[4].getCost() + "-cost"
          + (tactician.getGold() < shopUnits[4].getCost() ? "-disabled" : "") + ".png"));
      shopUnitCostLabel5.setText(String.valueOf(shopUnits[4].getCost()));
      if (shopUnits[4].getTraits().size() == 1) {
        shopUnitTrait1Container5.setVisible(false);
        shopUnitTrait2Container5.setVisible(false);
        shopUnitTrait3Image5.setImage(new Image("/assets/traiticons/" + shopUnits[4].getTraits().get(0).getLogo()));
        shopUnitTrait3Label5.setText(shopUnits[4].getTraits().get(0).getValue());
      } else if (shopUnits[4].getTraits().size() == 2) {
        shopUnitTrait1Container5.setVisible(false);
        shopUnitTrait2Image5.setImage(new Image("/assets/traiticons/" + shopUnits[4].getTraits().get(0).getLogo()));
        shopUnitTrait2Label5.setText(shopUnits[4].getTraits().get(0).getValue());
        shopUnitTrait3Image5.setImage(new Image("/assets/traiticons/" + shopUnits[4].getTraits().get(1).getLogo()));
        shopUnitTrait3Label5.setText(shopUnits[4].getTraits().get(1).getValue());
        shopUnitTrait2Container5.setVisible(true);
      } else if (shopUnits[4].getTraits().size() == 3) {
        shopUnitTrait1Image5.setImage(new Image("/assets/traiticons/" + shopUnits[4].getTraits().get(0).getLogo()));
        shopUnitTrait1Label5.setText(shopUnits[4].getTraits().get(0).getValue());
        shopUnitTrait2Image5.setImage(new Image("/assets/traiticons/" + shopUnits[4].getTraits().get(1).getLogo()));
        shopUnitTrait2Label5.setText(shopUnits[4].getTraits().get(1).getValue());
        shopUnitTrait3Image5.setImage(new Image("/assets/traiticons/" + shopUnits[4].getTraits().get(2).getLogo()));
        shopUnitTrait3Label5.setText(shopUnits[4].getTraits().get(2).getValue());
        shopUnitTrait1Container5.setVisible(true);
        shopUnitTrait2Container5.setVisible(true);
      }
      shopUnitContainer5.setVisible(true);
    }
  }

  public void onKeybindButtonClicked(ActionEvent event) {
    // var prevCallback = root.getOnKeyPressed();

    String prevKeybind = ((Button) event.getSource()).getText();

    Keybinding keybind = keybinds.getReverseState().get(KeyCode.valueOf(prevKeybind));

    keybindsLabel.setText("Press a key to change the key binding: " + keybind.getLabel());

    root.setOnKeyPressed(setterEvent -> {
      try {
        keybinds.setKeybinding(keybind, setterEvent.getCode());
      } catch (IllegalStateException err) {
        // root.setOnKeyPressed(prevCallback);
        root.setOnKeyPressed(e -> onKeyPressed(e));
        keybindsLabel.setText("Setting keybind failed: " + err.getMessage());
        return;
      }
      keybindsLabel.setText("");
      // root.setOnKeyPressed(prevCallback);
      root.setOnKeyPressed(e -> onKeyPressed(e));
      update();
    });
  }

  public void onKeyPressed(KeyEvent event) {
    if (event.getCode() == keybinds.getState().get(Keybinding.REROLL)) {
      rerollShop();
    } else if (event.getCode() == keybinds.getState().get(Keybinding.BUY_XP)) {
      buyXP();
    }
    event.consume();
  }

  public void onBenchUnitDragged(MouseEvent event) {
    AnchorPane source = (AnchorPane) event.getSource();

    String index = source.getUserData().toString();

    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);

    ClipboardContent content = new ClipboardContent();
    content.putString(index);
    db.setContent(content);

    WritableImage snapshot = source.snapshot(null, null);
    db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);

    event.consume();
  }

  public void onShopDragOver(DragEvent event) {
    if (event.getDragboard().hasString()) {
      event.acceptTransferModes(TransferMode.MOVE);
    }

    event.consume();
  }

  public void onBenchDragOver(DragEvent event) {
    if (event.getDragboard().hasString()) {
      event.acceptTransferModes(TransferMode.MOVE);
    }

    event.consume();
  }

  public void onShopDragDropped(DragEvent event) {
    Dragboard db = event.getDragboard();

    if (db.hasString()) {
      String data = db.getString();

      tactician.sellUnit(Integer.valueOf(data));
      event.setDropCompleted(true);
      update();
    } else {
      event.setDropCompleted(false);
    }

    event.consume();
  }

  public void onBenchDragDropped(DragEvent event) {
    Dragboard db = event.getDragboard();

    if (db.hasString()) {
      String data = db.getString();
      AnchorPane dest = (AnchorPane) event.getGestureTarget();

      tactician.moveUnitToBench(Integer.valueOf(data.toString()), Integer.valueOf(dest.getUserData().toString()));
      event.setDropCompleted(true);
      update();
    } else {
      event.setDropCompleted(false);
    }

    event.consume();
  }

  public void buyUnit(MouseEvent event) {
    Button button = (Button) event.getSource();
    int index = Integer.valueOf(button.getUserData().toString());

    tactician.buyUnit(index);

    update();

    event.consume();
  }

  private void disableBuyXP() {
    buyXPImage.setImage(new Image("/assets/actions/buyxp/disabled.png"));
    buyXPButton.setDisable(true);
  }

  private void disableReroll() {
    rerollImage.setImage(new Image("/assets/actions/reroll/disabled.png"));
    rerollButton.setDisable(true);
  }

  private void enableBuyXP() {
    buyXPImage.setImage(new Image("/assets/actions/buyxp/normal.png"));
    buyXPButton.setDisable(false);
  }

  private void enableReroll() {
    rerollImage.setImage(new Image("/assets/actions/reroll/normal.png"));
    rerollButton.setDisable(false);
  }

  public void buyXP() {
    tactician.buyXP();
    update();
  }

  public void hoverBuyXP() {
    buyXPImage.setImage(new Image("/assets/actions/buyxp/hover.png"));
  }

  public void exitedBuyXP() {
    buyXPImage.setImage(new Image("/assets/actions/buyxp/normal.png"));
  }

  public void pressedBuyXP() {
    buyXPImage.setImage(new Image("/assets/actions/buyxp/active.png"));
  }

  public void releasedBuyXP() {
    buyXPImage.setImage(new Image("/assets/actions/buyxp/hover.png"));
  }

  public void rerollShop() {
    tactician.rerollShop();
    update();
  }

  public void hoverReroll() {
    rerollImage.setImage(new Image("/assets/actions/reroll/hover.png"));
  }

  public void exitedReroll() {
    rerollImage.setImage(new Image("/assets/actions/reroll/normal.png"));
  }

  public void pressedReroll() {
    rerollImage.setImage(new Image("/assets/actions/reroll/active.png"));
  }

  public void releasedReroll() {
    rerollImage.setImage(new Image("/assets/actions/reroll/hover.png"));
  }
}
