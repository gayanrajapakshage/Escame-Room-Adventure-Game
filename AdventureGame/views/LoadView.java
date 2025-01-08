package views;

import AdventureModel.AdventureGame;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;


/**
 * Class LoadView.
 *
 * Loads Serialized adventure games.
 */
public class LoadView {

    private AdventureGameView adventureGameView;
    private Label selectGameLabel;
    private Button selectGameButton;
    private Button closeWindowButton;
    VBox dialogVbox = new VBox(20);

    private ListView<String> GameList;
    private String filename = null;

    public LoadView(AdventureGameView adventureGameView){

        //note that the buttons in this view are not accessible!!
        this.adventureGameView = adventureGameView;
        selectGameLabel = new Label(String.format(""));

        GameList = new ListView<>(); //to hold all the file names

        final Stage dialog = new Stage(); //dialogue box
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(adventureGameView.stage);

        dialogVbox.setPadding(new Insets(20, 20, 20, 20));
        dialogVbox.setStyle("-fx-background-color: #121212;");
        selectGameLabel.setId("CurrentGame"); // DO NOT MODIFY ID
        GameList.setId("GameList");  // DO NOT MODIFY ID
        GameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        getFiles(GameList); //get files for file selector
        selectGameButton = new Button("Change Game");
        selectGameButton.setId("ChangeGame"); // DO NOT MODIFY ID
        AdventureGameView.makeButtonAccessible(selectGameButton, "select game", "This is the button to select a game", "Use this button to indicate a game file you would like to load.");

        closeWindowButton = new Button("Close Window");
        closeWindowButton.setId("closeWindowButton"); // DO NOT MODIFY ID
        closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        closeWindowButton.setPrefSize(200, 50);
        closeWindowButton.setFont(new Font(16));
        closeWindowButton.setOnAction(e -> dialog.close());
        AdventureGameView.makeButtonAccessible(closeWindowButton, "close window", "This is a button to close the load game window", "Use this button to close the load game window.");

        //on selection, do something
        selectGameButton.setOnAction(e -> {
            try {
                selectGame(selectGameLabel, GameList);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        VBox selectGameBox = new VBox(10, selectGameLabel, GameList, selectGameButton);

        // Default styles which can be modified
        GameList.setPrefHeight(100);
        selectGameLabel.setStyle("-fx-text-fill: #e8e6e3");
        selectGameLabel.setFont(new Font(16));
        selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectGameButton.setPrefSize(200, 50);
        selectGameButton.setFont(new Font(16));
        selectGameBox.setAlignment(Pos.CENTER);
        updateColourBlind();
        updateBackgroundColour();
        dialogVbox.getChildren().add(selectGameBox);
        Scene dialogScene = new Scene(dialogVbox, 400, 400);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    /**
     * Get Files to display in the on screen ListView
     * Populate the listView attribute with .ser file names
     * Files will be located in the Games/Saved directory
     *
     * @param listView the ListView containing all the .ser files in the Games/Saved directory.
     */
    private void getFiles(ListView<String> listView) {
        File folder = new File("./Games/Saved");
        File[] files = folder.listFiles();
        if (files != null){
            for (File file: files){
                if (file.getName().endsWith(".ser")){
                    listView.getItems().add(file.getName());
                }
            }
        }
    }

    /**
     * Select the Game
     * Try to load a game from the Games/Saved
     * If successful, stop any articulation and put the name of the loaded file in the selectGameLabel.
     * If unsuccessful, stop any articulation and start an entirely new game from scratch.
     * In this case, change the selectGameLabel to indicate a new game has been loaded.
     *
     * @param selectGameLabel the label to use to print errors and or successes to the user.
     * @param GameList the ListView to populate
     */
    private void selectGame(Label selectGameLabel, ListView<String> GameList) throws IOException {
        //saved games will be in the Games/Saved folder!
        String chosenGame = "./Games/Saved/" + GameList.getSelectionModel().getSelectedItem();
        try {
            this.adventureGameView.model = loadGame(chosenGame);
            this.adventureGameView.stopArticulation();
            this.adventureGameView.updateScene(null);
            this.adventureGameView.updateItems();
            selectGameLabel.setText("Selected Game: " + GameList.getSelectionModel().getSelectedItem());
        }
        catch (ClassNotFoundException | IOException e){
            String adventureName = this.adventureGameView.model.getDirectoryName().split("/", 2)[1];
            this.adventureGameView.model = new AdventureGame(adventureName);
            this.adventureGameView.stopArticulation();
            this.adventureGameView.updateScene(null);
            this.adventureGameView.updateItems();
            selectGameLabel.setText("ERROR: Game not found. New game started!");
        }
    }

    /**
     * Load the Game from a file
     *
     * @param GameFile file to load
     * @return loaded Tetris Model
     */
    public AdventureGame loadGame(String GameFile) throws IOException, ClassNotFoundException {
        // Reading the object from a file
        FileInputStream file = null;
        ObjectInputStream in = null;
        try {
            file = new FileInputStream(GameFile);
            in = new ObjectInputStream(file);
            return (AdventureGame) in.readObject();
        } finally {
            if (in != null) {
                in.close();
                file.close();
            }
        }
    }

    /**
     * updateColourBlind
     * __________________________
     * Updates the colour-blind settings within the load window of the game.
     */
    private void updateColourBlind(){
        if (adventureGameView.colourBlindMode == 1){
            selectGameButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
            closeWindowButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
        }
        if (adventureGameView.colourBlindMode == 2){
            selectGameButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
            closeWindowButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
        }
        if (adventureGameView.colourBlindMode == 3){
            selectGameButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
            closeWindowButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
        }
        if (adventureGameView.colourBlindMode == 0){
            selectGameButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            closeWindowButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        }
    }

    /**
     * updateBackgroundColour
     * __________________________
     * Updates the background colour within the load window of the game.
     */
    private void updateBackgroundColour(){
        dialogVbox.setStyle("-fx-background-color: #" + adventureGameView.currBackgroundColour.toString().substring(2, adventureGameView.currBackgroundColour.toString().length()-2) + ";");
        System.out.println(adventureGameView.currBackgroundColour);
    }

}
