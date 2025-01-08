package views;

import AdventureModel.Settings.Commands.BackgroundColourCommand;
import AdventureModel.Settings.Commands.ColourBlindCommand;
import AdventureModel.Settings.Commands.VolumeCommand;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import AdventureModel.Settings.SettingsInvoker;

/**
 * Class SettingsView.
 *
 * This is the class that visualizes the settings menu window.
 */
public class SettingsView{

    private AdventureGameView adventureGameView;
    private Label settingsLabel = new Label(String.format("Game Settings Menu"));;
    private ListView<String> GameList;
    private String filename = null;
    private int colourBlindMode = 0;
    private Label noneLabel = new Label("None");
    private Label protanomalyLabel = new Label("Protanomaly");
    private Label deuteranomalyLabel = new Label("Deuteranomaly");
    private Label tritanomalyLabel = new Label("Tritanomaly");
    private Label volumeLabel = new Label("Volume");
    private Label backgroundColourLabel = new Label("Select Background Colour");
    private Slider volumeSlider = new Slider(0, 1, 0.5);
    private ColorPicker backgroundColourPicker = new ColorPicker();
    public SettingsInvoker settingsInvoker = new SettingsInvoker();
    private VBox settingsVbox = new VBox(20);

    private Button changeColourBlindMode = new Button("Change Colour Blind Setting");

    /**
     * Settings View Constructor
     * __________________________
     * Initializes attributes
     */
    public SettingsView(AdventureGameView adventureGameView){

        this.adventureGameView = adventureGameView;
        final Stage settings = new Stage();
        settings.initModality(Modality.APPLICATION_MODAL);
        settings.initOwner(adventureGameView.stage);

        settingsVbox.setPadding(new Insets(20, 20, 20, 20));
        settingsVbox.setStyle("-fx-background-color: #" + adventureGameView.currBackgroundColour.toString().substring(2, adventureGameView.currBackgroundColour.toString().length()-2) + ";");

        //Makes Button Accessible
        Button changeColourBlindSettingButton = new Button("Change Colour-Blind Setting");
        AdventureGameView.makeButtonAccessible(changeColourBlindSettingButton, "Change Colour-Blind Setting",
                "This button changes the current colour-blind settings.",
                "This button changes the colour-blind settings to the corresponding colour-blind type on the " +
                        "screen.");
        changeColourBlindSettingButton.setOnAction(e -> changeColourBlindSetting());

        volumeLabel.setFont(new Font(22));
        volumeLabel.setStyle("-fx-text-fill: #e8e6e3;");
        volumeSlider.setPrefWidth(584.0);
        volumeSlider.setValue(adventureGameView.volumeValue);
        adventureGameView.mediaPlayer.volumeProperty().bindBidirectional(volumeSlider.valueProperty());
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
                adventureGameView.volumeValue = newValue.doubleValue();
                changeVolumeSetting();
        });

        backgroundColourLabel.setFont(new Font(22));
        backgroundColourLabel.setStyle("-fx-text-fill: #e8e6e3;");
        backgroundColourPicker.setPrefWidth(418.0);
        backgroundColourPicker.setOnAction(e -> {
            adventureGameView.currBackgroundColour = backgroundColourPicker.getValue();
            changeBackgroundColourSetting();
        });
        backgroundColourPicker.setValue(Color.web(adventureGameView.currBackgroundColour.toString().substring(2, adventureGameView.currBackgroundColour.toString().length()-2)));

        colourBlindMode = adventureGameView.colourBlindMode;
        updateColourBlindLabels();

        //Container for colour mode texts
        HBox colourModeBox = new HBox(80, changeColourBlindSettingButton, noneLabel, deuteranomalyLabel, tritanomalyLabel, protanomalyLabel);
        colourModeBox.setAlignment(Pos.CENTER);

        //Container for volume slider and text
        HBox volumeBox = new HBox(80, volumeLabel, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER);

        //Container for background colour and text
        HBox backgroundColourBox = new HBox(80, backgroundColourLabel, backgroundColourPicker);
        backgroundColourBox.setAlignment(Pos.CENTER);

        settingsLabel.setId("Settings");
        settingsLabel.setStyle("-fx-text-fill: #e8e6e3;");
        settingsLabel.setFont(new Font(24));
        //Container for all settings menu contents
        VBox contentBox = new VBox(40, settingsLabel, colourModeBox, volumeBox, backgroundColourBox);
        contentBox.setAlignment(Pos.CENTER);
        settingsVbox.getChildren().add(contentBox);
        Scene settingsScene = new Scene(settingsVbox, 850, 550);
        settings.setScene(settingsScene);
        settings.show();
    }

    /**
     * changeColourBlindSetting
     * __________________________
     * Changes the colour-blind mode setting in the game.
     *
     * The colour-blind modes can be cycled through four options including modes for
     * Deuteranomaly, Tritanomaly, and Protanomaly colour-blindness types, while updating
     * the labels corresponding to each mode on the screen.
     */
    public void changeColourBlindSetting(){
        colourBlindMode = (colourBlindMode + 1) % 4;
        updateColourBlindLabels();
        settingsInvoker.setCommand(new ColourBlindCommand(adventureGameView));
        settingsInvoker.executeCommand();
    }

    /**
     * changeVolumeSetting
     * __________________________
     * Changes the volume of the game.
     * The further right the volume slider is moved the louder the game becomes
     * and vice versa.
     */
    public void changeVolumeSetting(){
        settingsInvoker.setCommand(new VolumeCommand(adventureGameView));
        settingsInvoker.executeCommand();
    }

    /**
     * changeBackgroundColourSetting
     * __________________________
     * Changes the background colour of the game based on the user's selection.
     */
    public void changeBackgroundColourSetting(){
        settingsVbox.setStyle("-fx-background-color: #" + adventureGameView.currBackgroundColour.toString().substring(2, adventureGameView.currBackgroundColour.toString().length()-2) + ";");
        settingsInvoker.setCommand(new BackgroundColourCommand(adventureGameView));
        settingsInvoker.executeCommand();
    }

    /**
     * updateColourBlindLabels
     * __________________________
     * Updates the labels in the settings menu corresponding to the current selected
     * colour-blind mode setting.
     */
    private void updateColourBlindLabels() {
        noneLabel.setStyle("-fx-text-fill: #e8e6e3;");
        protanomalyLabel.setStyle("-fx-text-fill: #e8e6e3;");
        deuteranomalyLabel.setStyle("-fx-text-fill: #e8e6e3;");
        tritanomalyLabel.setStyle("-fx-text-fill: #e8e6e3;");
        if (colourBlindMode == 1){
            noneLabel.setStyle("-fx-text-fill: #e8e6e3; -fx-background-color: transparent;");
            deuteranomalyLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        }
        if (colourBlindMode == 2){
            deuteranomalyLabel.setStyle("-fx-text-fill: #e8e6e3; -fx-background-color: transparent;");
            tritanomalyLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        }
        if (colourBlindMode == 3){
            tritanomalyLabel.setStyle("-fx-text-fill: #e8e6e3; -fx-background-color: transparent;");
            protanomalyLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        }
        if (colourBlindMode == 0){
            protanomalyLabel.setStyle("-fx-text-fill: #e8e6e3; -fx-background-color: transparent;");
            noneLabel.setStyle("-fx-text-fill: black; -fx-background-color: white;");
        }
    }

}