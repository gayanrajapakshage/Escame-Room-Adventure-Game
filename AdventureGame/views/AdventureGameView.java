package views;

import AdventureModel.AdventureGame;
import AdventureModel.AdventureObject;
import AdventureModel.NPCs.*;
import AdventureModel.PlayerStatusObserver;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.Room;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.scene.AccessibleRole;

import java.io.File;
import java.util.Arrays;

/**
 * Class AdventureGameView.
 *
 * This is the Class that will visualize your model.
 * You are asked to demo your visualization via a Zoom
 * recording. Place a link to your recording below.
 *
 * ZOOM LINK: https://drive.google.com/file/d/1ZKPeHjYoWFRyoJANJPlPAT4keNwA_Hs7/view?usp=sharing
 * PASSWORD: N/A
 */
public class AdventureGameView {

    AdventureGame model; //model of the game
    Stage stage; //stage on which all is rendered
    Button saveButton, loadButton, helpButton, settingsButton, shopButton; //buttons
    Rectangle healthBar = new Rectangle(140, 11, Color.valueOf("#24dc5c"));
    Rectangle repBar = new Rectangle(140, 11, Color.valueOf("#d8cd31"));
    Boolean helpToggle = false; //is help on display?
    Boolean shopToggle = false; //whether shop is opened
    Boolean fightToggle = false; //whether player is in fight
    Boolean playerTurn = true; //whether it is the turn of player to attack
    GridPane gridPane = new GridPane(); //to hold images and buttons
    Label roomDescLabel = new Label(); //to hold room description and/or instructions
    Label fightInfoLabel = new Label(); //to display fight text
    VBox objectsInRoom = new VBox(); //to hold room items
    VBox objectsInInventory = new VBox(); //to hold inventory items
    VBox playerStatusView = new VBox(); //to display health, reputation, and coins
    PlayerStatusObserver playerObserver = new PlayerStatusObserver();
    ImageView roomImageView; //to hold room image
    TextField inputTextField; //for user input
    ScrollPane scO = new ScrollPane(); //to hold objectsInRoom VBox
    ScrollPane scI = new ScrollPane(); //to hold objectsInInventory VBox
    VBox roomPane = new VBox(); //to hold roomImageView and roomDescLabel
    VBox textEntry = new VBox(); //to hold the text area and submit button
    ScrollPane info = new ScrollPane(); //to hold shop text
    NPC npcInFight; // the npc player is in fight with
    int colourBlindMode = 0; //the current colour-blind mode: 0 = None, 1 = Deuteranomaly, 2 = Tritanomaly, 3 = Protanomaly
    double volumeValue = 0.5; //the current volume value
    Paint currBackgroundColour = Color.BLACK; //the current background colour

    public MediaPlayer mediaPlayer; //to play audio
    private boolean mediaPlaying; //to know if the audio is playing

    /**
     * Adventure Game View Constructor
     * __________________________
     * Initializes attributes
     */
    public AdventureGameView(AdventureGame model, Stage stage) {
        this.model = model;
        this.stage = stage;
        intiUI();
    }

    /**
     * Initialize the UI
     */
    public void intiUI() {

        // setting up the stage
        this.stage.setTitle("riazrami's Adventure Game");

        //Inventory + Room items
        objectsInInventory.setSpacing(10);
        objectsInInventory.setAlignment(Pos.TOP_CENTER);
        objectsInRoom.setSpacing(10);
        objectsInRoom.setAlignment(Pos.TOP_CENTER);

        // GridPane, anyone?
        gridPane.setPadding(new Insets(20));
        gridPane.setBackground(new Background(new BackgroundFill(
                Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0)
        )));

        //Three columns, three rows for the GridPane
        ColumnConstraints column1 = new ColumnConstraints(150);
        ColumnConstraints column2 = new ColumnConstraints(650);
        ColumnConstraints column3 = new ColumnConstraints(150);
        column3.setHgrow( Priority.SOMETIMES ); //let some columns grow to take any extra space
        column1.setHgrow( Priority.SOMETIMES );

        // Row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints( 550 );
        RowConstraints row3 = new RowConstraints();
        row1.setVgrow( Priority.SOMETIMES );
        row3.setVgrow( Priority.SOMETIMES );

        gridPane.getColumnConstraints().addAll( column1 , column2 , column1 );
        gridPane.getRowConstraints().addAll( row1 , row2 , row1 );

        // Set up player's status view
        String rep50_80Image = this.model.getDirectoryName() + "/status-images/rep50-80.png";
        Image rep50_80File = new Image(rep50_80Image);
        ImageView rep50_80IW = new ImageView(rep50_80File);
        rep50_80IW.setFitHeight(20);
        rep50_80IW.setFitWidth(20);
        String rep50Image = this.model.getDirectoryName() + "/status-images/rep50.png";
        Image rep50File = new Image(rep50Image);
        ImageView rep50IW = new ImageView(rep50File);
        rep50IW.setFitHeight(20);
        rep50IW.setFitWidth(20);
        String healthImage = this.model.getDirectoryName() + "/status-images/heart.png";
        Image healthFile = new Image(healthImage);
        ImageView heartIW = new ImageView(healthFile);
        heartIW.setFitHeight(20);
        heartIW.setFitWidth(20);
        Label healthLabel = new Label("HealthLabel", heartIW);
        healthLabel.setText("100/100");
        healthLabel.setId("healthLabel");
        healthLabel.setStyle("-fx-text-fill: white;");
        String repImage = this.model.getDirectoryName() + "/status-images/rep80.png";
        Image repFile = new Image(repImage);
        ImageView repIW = new ImageView(repFile);
        repIW.setFitHeight(20);
        repIW.setFitWidth(20);
        Label repLabel = new Label("repLabel", repIW);
        repLabel.setText("100/100");
        repLabel.setId("repLabel");
        repLabel.setStyle("-fx-text-fill: white;");
        healthBar.setId("healthBar");
        repBar.setId("repBar");
        String coinImage = this.model.getDirectoryName() + "/status-images/coin.png";
        Image coinFile = new Image(coinImage);
        ImageView coinIW = new ImageView(coinFile);
        coinIW.setFitHeight(20);
        coinIW.setFitWidth(20);
        Label coinLabel = new Label("coinLabel", coinIW);
        coinLabel.setId("coinLabel");
        coinLabel.setStyle("-fx-text-fill: white;");
        playerStatusView.getChildren().add(healthLabel);
        playerStatusView.getChildren().add(healthBar);
        playerStatusView.getChildren().add(repLabel);
        playerStatusView.getChildren().add(repBar);
        playerStatusView.getChildren().add(coinLabel);
        playerStatusView.setBackground(new Background(new BackgroundFill(Color.valueOf("#000000"),
                new CornerRadii(0),
                new Insets(0))));
        playerStatusView.setMaxWidth(160);
        playerStatusView.setMaxHeight(260);
        gridPane.add(playerStatusView, 0, 2,1 ,1);
        this.playerObserver = new PlayerStatusObserver(this.model.getPlayer(), this.playerStatusView,
                rep50_80IW, rep50IW, repIW);
        this.playerObserver.update();

        // Buttons
        saveButton = new Button("Save");
        saveButton.setId("Save");
        customizeButton(saveButton, 100, 50);
        makeButtonAccessible(saveButton, "Save Button", "This button saves the game.", "This button saves the game. Click it in order to save your current progress, so you can play more later.");
        addSaveEvent();

        loadButton = new Button("Load");
        loadButton.setId("Load");
        customizeButton(loadButton, 100, 50);
        makeButtonAccessible(loadButton, "Load Button", "This button loads a game from a file.", "This button loads the game from a file. Click it in order to load a game that you saved at a prior date.");
        addLoadEvent();

        helpButton = new Button("Instructions");
        helpButton.setId("Instructions");
        customizeButton(helpButton, 200, 50);
        makeButtonAccessible(helpButton, "Help Button", "This button gives game instructions.", "This button gives instructions on the game controls. Click it to learn how to play.");
        addInstructionEvent();

        settingsButton = new Button("Settings");
        settingsButton.setId("Settings");
        customizeButton(settingsButton, 100, 50);
        makeButtonAccessible(settingsButton, "Settings Button", "This button opens the game settings menu.", "This button opens the game settings menu. Click it to modify any of the settings in the game.");
        addSettingsEvent();

        shopButton = new Button("Shop");
        shopButton.setId("Shop");
        customizeButton(shopButton, 200, 50);
        makeButtonAccessible(shopButton, "Shop Button", "This button opens the shop", "This button opens the shop and allow player to purchase items");
        addShopEvent();
        VBox shopBox = new VBox();
        shopBox.getChildren().add(shopButton);
        gridPane.add(shopBox, 2,2);

        HBox topButtons = new HBox();
        topButtons.getChildren().addAll(saveButton, helpButton, loadButton, settingsButton);
        topButtons.setSpacing(10);
        topButtons.setAlignment(Pos.CENTER);

        inputTextField = new TextField();
        inputTextField.setFont(new Font("Arial", 16));
        inputTextField.setFocusTraversable(true);

        inputTextField.setAccessibleRole(AccessibleRole.TEXT_AREA);
        inputTextField.setAccessibleRoleDescription("Text Entry Box");
        inputTextField.setAccessibleText("Enter commands in this box.");
        inputTextField.setAccessibleHelp("This is the area in which you can enter commands you would like to play.  Enter a command and hit return to continue.");
        addTextHandlingEvent(); //attach an event to this input field

        //labels for inventory and room items
        Label objLabel =  new Label("Objects in Room");
        objLabel.setAlignment(Pos.CENTER);
        objLabel.setStyle("-fx-text-fill: white;");
        objLabel.setFont(new Font("Arial", 16));

        Label invLabel =  new Label("Your Inventory");
        invLabel.setAlignment(Pos.CENTER);
        invLabel.setStyle("-fx-text-fill: white;");
        invLabel.setFont(new Font("Arial", 16));

        //add all the widgets to the GridPane
        gridPane.add( objLabel, 0, 0, 1, 1 );  // Add label
        gridPane.add( topButtons, 1, 0, 1, 1 );  // Add buttons
        gridPane.add( invLabel, 2, 0, 1, 1 );  // Add label

        Label commandLabel = new Label("What would you like to do?");
        commandLabel.setStyle("-fx-text-fill: white;");
        commandLabel.setFont(new Font("Arial", 16));

        updateScene(""); //method displays an image and whatever text is supplied
        updateItems(); //update items shows inventory and objects in rooms

        // adding the text area and submit button to a VBox
        textEntry = new VBox();
        textEntry.setStyle("-fx-background-color: #000000;");
        textEntry.setPadding(new Insets(20, 20, 20, 20));
        textEntry.getChildren().addAll(commandLabel, inputTextField);
        textEntry.setSpacing(10);
        textEntry.setAlignment(Pos.CENTER);
        gridPane.add( textEntry, 1, 2, 1, 1 );

        // Render everything
        var scene = new Scene( gridPane ,  1000, 800);
        scene.setFill(Color.BLACK);
        this.stage.setScene(scene);
        this.stage.setResizable(false);
        this.stage.show();

    }


    /**
     * makeButtonAccessible
     * __________________________
     * For information about ARIA standards, see
     * https://www.w3.org/WAI/standards-guidelines/aria/
     *
     * @param inputButton the button to add screenreader hooks to
     * @param name ARIA name
     * @param shortString ARIA accessible text
     * @param longString ARIA accessible help text
     */
    public static void makeButtonAccessible(Button inputButton, String name, String shortString, String longString) {
        inputButton.setAccessibleRole(AccessibleRole.BUTTON);
        inputButton.setAccessibleRoleDescription(name);
        inputButton.setAccessibleText(shortString);
        inputButton.setAccessibleHelp(longString);
        inputButton.setFocusTraversable(true);
    }

    /**
     * customizeButton
     * __________________________
     *
     * @param inputButton the button to make stylish :)
     * @param w width
     * @param h height
     */
    private void customizeButton(Button inputButton, int w, int h) {
        inputButton.setPrefSize(w, h);
        inputButton.setFont(new Font("Arial", 16));
        inputButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
    }

    /**
     * changeColourBlindMode
     * __________________________
     * Change the colour-blind mode to the corresponding
     * setting on the screen each time the "Change
     * Colour-Blind Setting" button is pressed.
     *
     * Colours have been preselected to cater to three types
     * of colour-blindness including: Deuteranomaly, Tritanomaly,
     * and Protanomaly colour-blindness types.
     */
    public void changeColourBlindMode(){
        colourBlindMode = (colourBlindMode + 1) % 4;
        if (colourBlindMode == 1){
            saveButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
            loadButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
            helpButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
            settingsButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
            shopButton.setStyle("-fx-background-color: rgb(2, 81, 150);-fx-text-fill: rgb(253, 179, 56);");
            healthBar.setStyle("-fx-fill: rgb(2, 81, 150);");
            repBar.setStyle("-fx-fill: rgb(253, 179, 56);");
            for (int i = 0; i < objectsInRoom.getChildren().size(); i++){
                objectsInRoom.getChildren().get(i).setStyle("-fx-background-color: rgb(2, 81, 150);");
            }
            for (int i = 0; i < objectsInInventory.getChildren().size(); i++){
                objectsInInventory.getChildren().get(i).setStyle("-fx-background-color: rgb(2, 81, 150);");
            }
        }
        if (colourBlindMode == 2){
            saveButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
            loadButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
            helpButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
            settingsButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
            shopButton.setStyle("-fx-background-color: rgb(91,0,255);-fx-text-fill: rgb(230,0,0);");
            healthBar.setStyle("-fx-fill: rgb(91,0,255);");
            repBar.setStyle("-fx-fill: rgb(230,0,0);");
            for (int i = 0; i < objectsInRoom.getChildren().size(); i++){
                objectsInRoom.getChildren().get(i).setStyle("-fx-background-color: rgb(91,0,255);");
            }
            for (int i = 0; i < objectsInInventory.getChildren().size(); i++){
                objectsInInventory.getChildren().get(i).setStyle("-fx-background-color: rgb(91,0,255);");
            }
        }
        if (colourBlindMode == 3){
            saveButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
            loadButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
            helpButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
            settingsButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
            shopButton.setStyle("-fx-background-color: rgb(12, 100, 214);-fx-text-fill: rgb(181, 228, 71);");
            healthBar.setStyle("-fx-fill: rgb(12, 100, 214)");
            repBar.setStyle("-fx-fill: rgb(181, 228, 71);");
            for (int i = 0; i < objectsInRoom.getChildren().size(); i++){
                objectsInRoom.getChildren().get(i).setStyle("-fx-background-color: rgb(12, 100, 214);");
            }
            for (int i = 0; i < objectsInInventory.getChildren().size(); i++){
                objectsInInventory.getChildren().get(i).setStyle("-fx-background-color: rgb(12, 100, 214);");
            }
        }
        if (colourBlindMode == 0){
            customizeButton(saveButton, 100, 50);
            customizeButton(loadButton, 100, 50);
            customizeButton(helpButton, 200, 50);
            customizeButton(settingsButton, 100, 50);
            shopButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            healthBar.setStyle("-fx-fill: #24dc5c;");
            repBar.setStyle("-fx-fill: #d8cd31;");
            for (int i = 0; i < objectsInRoom.getChildren().size(); i++){
                objectsInRoom.getChildren().get(i).setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            }
            for (int i = 0; i < objectsInInventory.getChildren().size(); i++){
                objectsInInventory.getChildren().get(i).setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            }
        }
    }

    /**
     * changeVolume
     * __________________________
     * Change the volume of the game based on how far
     * the volume slider has been moved left or right
     * in the SettingsView class.
     *
     * The volumeValue attribute stores the value for the
     * current volume of the game.
     */
    public void changeVolume(){
        mediaPlayer.setVolume(volumeValue);
    }

    /**
     * changeBackgroundColour
     * __________________________
     * Change the background colour of the game to corresponding
     * colour in the currBackgroundColour attribute.
     */
    public void changeBackgroundColour(){
        gridPane.setBackground(new Background(new BackgroundFill(currBackgroundColour, new CornerRadii(0), new Insets(0))));
        playerStatusView.setBackground(new Background(new BackgroundFill(currBackgroundColour, new CornerRadii(0), new Insets(0))));
        if (scO != null){
            scO.setStyle("-fx-background: #" + currBackgroundColour.toString().substring(2, currBackgroundColour.toString().length()-2) + "; -fx-background-color:transparent;");
        }
        if (scI != null){
            scI.setStyle("-fx-background: #" + currBackgroundColour.toString().substring(2, currBackgroundColour.toString().length()-2) + "; -fx-background-color:transparent;");
        }
        if (roomPane != null){
            roomPane.setStyle("-fx-background-color: #" + currBackgroundColour.toString().substring(2, currBackgroundColour.toString().length()-2) + ";");
        }
        if (textEntry != null){
            textEntry.setStyle("-fx-background-color: #" + currBackgroundColour.toString().substring(2, currBackgroundColour.toString().length()-2) + ";");
        }
        if (info != null){
            info.setStyle("-fx-background: #" + currBackgroundColour.toString().substring(2, currBackgroundColour.toString().length()-2) + ";");
        }
    }

    /**
     * addTextHandlingEvent
     * __________________________
     * Add an event handler to the myTextField attribute 
     *
     * Your event handler should respond when users 
     * hits the ENTER or TAB KEY. If the user hits 
     * the ENTER Key, strip white space from the
     * input to myTextField and pass the stripped 
     * string to submitEvent for processing.
     *
     * If the user hits the TAB key, move the focus 
     * of the scene onto any other node in the scene 
     * graph by invoking requestFocus method.
     */
    private void addTextHandlingEvent() {
        inputTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String text = inputTextField.getText().trim();
                submitEvent(text);
                inputTextField.clear();
            } else if (event.getCode() == KeyCode.TAB) {
                this.roomDescLabel.requestFocus();
            }
        });
    }


    /**
     * submitEvent
     * __________________________
     *
     * @param text the command that needs to be processed
     */
    private void submitEvent(String text) {

        text = text.strip(); //get rid of white space
        String[] checkFight = text.split(" ");
        System.out.println(Arrays.toString(checkFight));
        stopArticulation(); //if speaking, stop

        if (text.equalsIgnoreCase("LOOK") || text.equalsIgnoreCase("L")) {
            updateScene(null);
            return;
        } else if (text.equalsIgnoreCase("HELP") || text.equalsIgnoreCase("H")) {
            showInstructions();
            return;
        } else if (text.equalsIgnoreCase("COMMANDS") || text.equalsIgnoreCase("C")) {
            showCommands(); //this is new!  We did not have this command in A1
            return;
        } else if (text.equalsIgnoreCase("SHOP") || text.equalsIgnoreCase("S")) {
            shopWelcome();
            if (!this.shopToggle){
                this.shopToggle = true;
            }
            return;
        }
        else if (text.equalsIgnoreCase("BALANCE")) {
            roomDescLabel.setText(Double.toString(this.model.getPlayer().getCoins()));
            return;
        } else if (checkFight[0].equalsIgnoreCase("BEG") || checkFight[0].equalsIgnoreCase("TYPE") ) {
            for (NPC npc : this.model.player.getCurrentRoom().NPCsInRoom) {
                String[] full = npc.name.split(": ");
                String[] bruh = Arrays.copyOfRange(checkFight, 1, checkFight.length);
                if (full[0].equalsIgnoreCase(String.join(" ", bruh))) {
                    setUpBeg(String.join(" ", Arrays.copyOfRange(checkFight, 1, checkFight.length)));
                    roomDescLabel.setText(this.model.interpretAction(text));
                    this.playerObserver.update();
                    return;
                }
            }

        }
        else if (checkFight[0].equalsIgnoreCase("FIGHT") || checkFight[0].equalsIgnoreCase("F")) {
            String[] listName = Arrays.copyOfRange(checkFight, 1, checkFight.length);
            String name = String.join(" ", listName);
            Room room = this.model.getPlayer().getCurrentRoom();
            this.npcInFight = null;
            for (NPC npc : room.NPCsInRoom) {
                String[] full = npc.name.split(": ");
                if (full[0].equalsIgnoreCase(name)) {
                     this.npcInFight = npc;
                     break;
                }
            }
            if (this.npcInFight == null) {
            updateScene("NPC is not in room!");
            return;
            }
            else {
                setUpFight();
                this.fightToggle = true;
                String fightText = "You start a fight with:\n[" + this.npcInFight.name + "]\n";
                if (this.npcInFight instanceof hostileNPC){
                    fightText +=  this.npcInFight.getText()+ "\n";
                }
                submitEvent(fightText);
                return;
            }

        } else if (this.fightToggle) {
            this.inputTextField.setDisable(true);
            StringBuilder builder = new StringBuilder(text);
            if (this.playerTurn){
                String npcText = this.npcInFight.fight(this.model.getPlayer());
                String fightText = "\n" + npcText;
                builder.append(fightText).append(this.npcInFight.getHealthText()).append("\n").append("======================================");
                this.playerTurn = false;
            } else{
                String playerText = this.model.getPlayer().fight(this.npcInFight);
                String healthLeft = this.model.getPlayer().getHealthText();
                String fightText = "\n" + playerText + "\n" + healthLeft;
                builder.append(fightText).append("\n").append("======================================");
                this.playerTurn = true;
            }
            if (this.npcInFight.hp <= 0){
                builder.append("\n").append(this.npcInFight.getDeathText()).append(this.npcInFight.name.split(":")[0]).append(" has been slain.\n");
                this.model.getPlayer().getCurrentRoom().removeNPC(this.npcInFight);
                PowerUp npcItem = this.npcInFight.item;
                if (npcItem != null){
                    npcItem.activate(this.model.getPlayer());
                    builder.append("You have obtained ").append(npcItem.getClass().getName().replace("AdventureModel.PowerUps.", "")).append(".\n");
                }
                if (npcInFight instanceof friendlyNPC){
                    this.model.getPlayer().gainRep(Double.max(-50, -this.model.player.getRep()));
                    builder.append("You attacked a friendly NPC! Your reputation is now ").append(this.model.player.getRep()).append(" points.");
                } else{
                    this.model.getPlayer().gainRep(Double.min(15, 100 - this.model.player.getRep()));
                    builder.append("You have defeated a hostile NPC! Your reputation is now ").append(this.model.player.getRep()).append(" points.");
                }
                this.fightInfoLabel.setText(builder.toString());
                this.model.getPlayer().getCurrentRoom().removeNPC(this.npcInFight);
                fightToggle = false;
                playerTurn = true;
                npcInFight = null;
                this.playerObserver.update();
                this.inputTextField.setDisable(false);
                return;
            }
            this.playerObserver.update();
            PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
            pause.setOnFinished(event -> {
                if (this.model.getPlayer().getHealth() > 0 && this.npcInFight.hp > 0){
                    this.fightInfoLabel.setText(builder.toString());
                    submitEvent(builder.toString());
                } else{
                    if (! this.model.checkPlayerAlive()){
                        this.inputTextField.setDisable(true);

                        updateScene("Your character is dead");
                        updateItems();
                        PauseTransition pauseDeath = new PauseTransition(Duration.seconds(5));
                        pauseDeath.setOnFinished(deathEvent -> {
                            Platform.exit();
                        });
                        pauseDeath.play();
                    }
                }
            });
            pause.play();
            return;
        }



        //try to move!
        String output = this.model.interpretAction(text); //process the command!
        System.out.println(output);

        if (output == null || (!output.equals("GAME OVER") && !output.equals("FORCED") && !output.equals("HELP"))) {
            updateScene(output);
            updateItems();
        } else if (output.equals("GAME OVER")) {
            updateScene("");
            updateItems();
            PauseTransition pause = new PauseTransition(Duration.seconds(10));
            pause.setOnFinished(event -> {
                Platform.exit();
            });
            pause.play();
        } else if (output.equals("FORCED")) {
            //write code here to handle "FORCED" events!
            //Your code will need to display the image in the
            //current room and pause, then transition to
            //the forced room.
            updateScene("");
            updateItems();
            this.inputTextField.setDisable(true);
                PauseTransition pause = new PauseTransition(Duration.seconds(6));

                pause.setOnFinished(event -> {
                    this.inputTextField.setDisable(false);
                    submitEvent(output);
                });
                pause.play();
        }
        /*
        Codes for interacting with NPCs or objects
         */
        this.playerObserver.update(); //update player's status on screen
    }


    /**
     * showCommands
     * __________________________
     *
     * update the text in the GUI (within roomDescLabel)
     * to show all the moves that are possible from the 
     * current room.
     */
    private void showCommands() {
        roomDescLabel.setText("These are all the valid moves you can make from here: " + this.model.getPlayer().getCurrentRoom().getCommands());
    }


    /**
     * updateScene
     * __________________________
     *
     * Show the current room, and print some text below it.
     * If the input parameter is not null, it will be displayed
     * below the image.
     * Otherwise, the current room description will be dispplayed
     * below the image.
     * 
     * @param textToDisplay the text to display below the image.
     */
    public void updateScene(String textToDisplay) {

        getRoomImage(); //get the image of the current room
        formatText(textToDisplay); //format the text to display
        roomDescLabel.setPrefWidth(500);
        roomDescLabel.setPrefHeight(500);
        roomDescLabel.setTextOverrun(OverrunStyle.CLIP);
        roomDescLabel.setWrapText(true);
        roomPane = new VBox(roomImageView,roomDescLabel);
        roomPane.setPadding(new Insets(10));
        roomPane.setAlignment(Pos.TOP_CENTER);
        roomPane.setStyle("-fx-background-color: #000000;");

        gridPane.add(roomPane, 1, 1);
        stage.sizeToScene();

        //update background colour
        changeBackgroundColour();

        //finally, articulate the description
        if (textToDisplay == null || textToDisplay.isBlank()) articulateRoomDescription();
    }

    /**
     * setUpFight
     * __________________________
     *
     * clear the node at (1, 1) and replace it with a scroll pane containing
     * the room image and a text area with scroll bar to update what happened
     * during a fight
     */
    private void setUpFight() {
        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
        ScrollPane fightPane = new ScrollPane();
        this.fightInfoLabel.setText("");

        fightInfoLabel.setPrefWidth(650);
        fightInfoLabel.setPrefHeight(3000);
        fightInfoLabel.setStyle("-fx-background-color: #000000;");
        fightInfoLabel.setTextFill(Color.valueOf("#FFFFFF"));
        fightInfoLabel.setAlignment(Pos.TOP_LEFT);
        fightInfoLabel.setFont(new Font("Arial", 16));

        String fileName = this.npcInFight.name.split(":")[0];
        Image npc = new Image(this.model.getDirectoryName() + "/NPC-images/" + fileName + ".jpg");
        ImageView npcView = new ImageView(npc);
        npcView.setFitWidth(300);
        npcView.setFitHeight(300);
        fightPane.setContent(npcView);//replace this roomImageView with NPC imageView
        fightPane.setContent(fightInfoLabel);
        fightPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        fightPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        VBox fightVbox = new VBox(npcView, fightPane);//replace this roomImageView with NPC imageView
        fightVbox.setPadding(new Insets(10));
        fightVbox.setAlignment(Pos.TOP_CENTER);
        fightVbox.setStyle("-fx-background-color: #000000;");
        gridPane.add(fightVbox, 1, 1);
    }

    private void setUpBeg(String npcName){
        try{
            Image npc = new Image(this.model.getDirectoryName() + "/NPC-images/" + npcName + ".jpg");
            this.roomImageView.setImage(npc);
            roomImageView.setFitWidth(300);
            roomImageView.setFitHeight(300);
        } catch (Exception ignore){

        }
    }


    /**
     * formatText
     * __________________________
     *
     * Format text for display.
     * 
     * @param textToDisplay the text to be formatted for display.
     */
    private void formatText(String textToDisplay) {
        if (textToDisplay == null || textToDisplay.isBlank()) {
            String roomDesc = this.model.getPlayer().getCurrentRoom().getRoomDescription() + "\n";
            String objectString = this.model.getPlayer().getCurrentRoom().getObjectString();
            String NPCstring = this.model.getPlayer().getCurrentRoom().getNPCstring();
            String text = roomDesc;
            if (objectString != null && !objectString.isEmpty()){
                text += "\nObjects in this room:\n" + objectString +"\n";
            }
            if (NPCstring != null && !NPCstring.isEmpty()){
                text += "\nNPCs in Room:" + "\n"+ NPCstring;
            }
            roomDescLabel.setText(text);
        } else roomDescLabel.setText(textToDisplay);
        roomDescLabel.setStyle("-fx-text-fill: white;");
        roomDescLabel.setFont(new Font("Arial", 16));
        roomDescLabel.setAlignment(Pos.CENTER);
    }

    /**
     * getRoomImage
     * __________________________
     *
     * Get the image for the current room and place 
     * it in the roomImageView 
     */
    private void getRoomImage() {

        int roomNumber = this.model.getPlayer().getCurrentRoom().getRoomNumber();
        String roomImage = this.model.getDirectoryName() + "/room-images/" + roomNumber + ".png";

        Image roomImageFile = new Image(roomImage);
        roomImageView = new ImageView(roomImageFile);
        roomImageView.setPreserveRatio(true);
        roomImageView.setFitWidth(400);
        roomImageView.setFitHeight(400);

        //set accessible text
        roomImageView.setAccessibleRole(AccessibleRole.IMAGE_VIEW);
        roomImageView.setAccessibleText(this.model.getPlayer().getCurrentRoom().getRoomDescription());
        roomImageView.setFocusTraversable(true);
    }

    /**
     * updateItems
     * __________________________
     *
     * This method is partially completed, but you are asked to finish it off.
     *
     * The method should populate the objectsInRoom and objectsInInventory Vboxes.
     * Each Vbox should contain a collection of nodes (Buttons, ImageViews, you can decide)
     * Each node represents a different object.
     * 
     * Images of each object are in the assets 
     * folders of the given adventure game.
     */
    public void updateItems() {

        //write some code here to add images of objects in a given room to the objectsInRoom Vbox
        //write some code here to add images of objects in a player's inventory room to the objectsInInventory Vbox
        //please use setAccessibleText to add "alt" descriptions to your images!
        //the path to the image of any is as follows:
        //this.model.getDirectoryName() + "/objectImages/" + objectName + ".jpg";

        scO = new ScrollPane(objectsInRoom);
        scO.setPadding(new Insets(10));
        scO.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        scO.setFitToWidth(true);
        gridPane.add(scO,0,1);

        scI = new ScrollPane(objectsInInventory);
        scI.setFitToWidth(true);
        scI.setStyle("-fx-background: #000000; -fx-background-color:transparent;");
        gridPane.add(scI,2,1);

        // clear both boxes before populating again
        objectsInRoom.getChildren().clear();
        objectsInInventory.getChildren().clear();


        for (AdventureObject object : model.getPlayer().getCurrentRoom().objectsInRoom) {
            Button objectButton = new Button();
            makeButtonAccessible(objectButton, object.getName(), ("This is the " + object.getName() + " object."), ("This is the " + object.getName() + " object. Click on it to add it to your inventory!"));
            ImageView image = new ImageView(model.getDirectoryName() + "/objectImages/" + object.getName() + ".jpg");
            image.setFitWidth(100);
            image.setPreserveRatio(true);
            objectButton.setGraphic(image);
            objectButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            objectButton.setOnAction(event -> {
                if (objectsInRoom.getChildren().contains(objectButton)){
                    this.model.getPlayer().takeObject(object.getName());
                    objectsInInventory.getChildren().add(objectButton);
                    objectsInRoom.getChildren().remove(objectButton);
                    updateScene("YOU HAVE TAKEN:\n " + object.getName());
                }
                else{
                    this.model.getPlayer().dropObject(object.getName());
                    objectsInInventory.getChildren().remove(objectButton);
                    objectsInRoom.getChildren().add(objectButton);
                    updateScene("YOU HAVE DROPPED:\n " + object.getName());
                }
            });
            objectsInRoom.getChildren().add(objectButton);
        }


        for (AdventureObject object : model.getPlayer().inventory) {
            Button objectButton = new Button();
            makeButtonAccessible(objectButton, object.getName(), ("This is the " + object.getName() + " object."), ("This is the " + object.getName() + " object. Click on it to add it to your inventory!"));
            ImageView image = new ImageView(model.getDirectoryName() + "/objectImages/" + object.getName() + ".jpg");
            image.setFitWidth(100);
            image.setPreserveRatio(true);
            objectButton.setGraphic(image);
            objectButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            objectButton.setOnAction(event -> {
                if (objectsInRoom.getChildren().contains(objectButton)){
                    this.model.getPlayer().takeObject(object.getName());
                    objectsInInventory.getChildren().add(objectButton);
                    objectsInRoom.getChildren().remove(objectButton);
                    updateScene("YOU HAVE TAKEN:\n " + object.getName());
                }
                else{
                    this.model.getPlayer().dropObject(object.getName());
                    objectsInInventory.getChildren().remove(objectButton);
                    objectsInRoom.getChildren().add(objectButton);
                    updateScene("YOU HAVE DROPPED:\n " + object.getName());
                }
            });
            objectsInInventory.getChildren().add(objectButton);
        }

        //Update colours for objects in room and inventory
        colourBlindMode -= 1;
        changeColourBlindMode();
        changeBackgroundColour();

    }

    /**
     * Show the game instructions.
     *
     * If helpToggle is FALSE:
     * -- display the help text in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- use whatever GUI elements to get the job done!
     * -- set the helpToggle to TRUE
     * -- REMOVE whatever nodes are within the cell beforehand!
     *
     * If helpToggle is TRUE:
     * -- redraw the room image in the CENTRE of the gridPane (i.e. within cell 1,1)
     * -- set the helpToggle to FALSE
     * -- Again, REMOVE whatever nodes are within the cell beforehand!
     */
    public void showInstructions() {
        if (!this.helpToggle){
            ScrollPane instructionPane = new ScrollPane();
            instructionPane.setMaxSize(700,700);
            String instructions = this.model.getInstructions();
            Label instructionsLabel = new Label(instructions);
            instructionsLabel.setFont(new Font("Arial", 13));
            instructionsLabel.setTextFill(Color.WHITE);
            instructionPane.setStyle("-fx-background: black; -fx-control-inner-background: black;");
            instructionsLabel.setWrapText(true);
            instructionPane.setContent(instructionsLabel);
            instructionPane.setPadding(new Insets(10));
            instructionPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            instructionPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
            gridPane.add(instructionPane, 1, 1);
            helpToggle = true;
        }
        else{
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
            updateScene("");
            helpToggle = false;
        }
    }


    /**
     * Show all the instructions related to the shop.
     */
    public void shopWelcome() {
        Text welcomeText = new Text(this.model.shop.welcomePlayer());
        welcomeText.setFont(new Font("Arial", 14));
        welcomeText.setFill(Color.WHITE);
        VBox textContainer = new VBox(welcomeText);
        textContainer.setPadding(new Insets(10));;
        info.setContent(textContainer);
        info.setFitToWidth(true);
        info.setStyle("-fx-background: #" + currBackgroundColour.toString().substring(2, currBackgroundColour.toString().length()-2) + ";");
        gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
        gridPane.add(info, 1, 1);
    }


    /**
     * This method handles the event related to the
     * help button.
     */
    public void addInstructionEvent() {
        helpButton.setOnAction(e -> {
            stopArticulation(); //if speaking, stop
            showInstructions();
        });
    }

    /**
     * This method handles the event related to the
     * save button.
     */
    public void addSaveEvent() {
        saveButton.setOnAction(e -> {
            gridPane.requestFocus();
            SaveView saveView = new SaveView(this);
        });
    }

    /**
     * This method handles the event related to the
     * load button.
     */
    public void addLoadEvent() {
        loadButton.setOnAction(e -> {
            gridPane.requestFocus();
            LoadView loadView = new LoadView(this);
        });
    }

    /**
     * This method handles the event related to the
     * settings button.
     */
    public void addSettingsEvent() {
        settingsButton.setOnAction(e -> {
            gridPane.requestFocus();
            SettingsView settingsView = new SettingsView(this);
        });
    }

    /**
     * This method handles the event related to the
     * shop button.
     */
    public void addShopEvent() {
        shopButton.setOnAction(e -> {
            if (!this.shopToggle){
                shopToggle = true;
                gridPane.requestFocus();
                submitEvent("SHOP");
            } else{
                gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node) == 1 && GridPane.getColumnIndex(node) == 1);
                updateScene("");
                shopToggle = false;
            }
        });
    }


    /**
     * This method articulates Room Descriptions
     */
    public void articulateRoomDescription() {
        String musicFile;
        String adventureName = this.model.getDirectoryName();
        String roomName = this.model.getPlayer().getCurrentRoom().getRoomName();

        if (!this.model.getPlayer().getCurrentRoom().getVisited()) musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-long.mp3" ;
        else musicFile = "./" + adventureName + "/sounds/" + roomName.toLowerCase() + "-short.mp3" ;
        musicFile = musicFile.replace(" ","-");

        Media sound = new Media(new File(musicFile).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.setVolume(volumeValue);
        mediaPlayer.play();
        mediaPlaying = true;
    }

    /**
     * This method stops articulations 
     * (useful when transitioning to a new room or loading a new game)
     */
    public void stopArticulation() {
        if (mediaPlaying) {
            mediaPlayer.stop(); //shush!
            mediaPlaying = false;
        }
    }
}
