package AdventureModel;


import AdventureModel.NPCs.*;
import AdventureModel.PowerUps.PowerUp;
import AdventureModel.GameShop.GameShop;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Class AdventureGame.  Handles all the necessary tasks to run the Adventure game.
 */
public class AdventureGame implements Serializable {
    private final String directoryName; //An attribute to store the Introductory text of the game.
    private String helpText; //A variable to store the Help text of the game. This text is displayed when the user types "HELP" command.
    private final HashMap<Integer, Room> rooms; //A list of all the rooms in the game.
    private HashMap<String,String> synonyms = new HashMap<>(); //A HashMap to store synonyms of commands.
    private final String[] actionVerbs = {"QUIT","INVENTORY","TAKE","DROP","BUY","SELL","BEG","FIGHT","TYPE"}; //List of action verbs (other than motions) that exist in all games. Motion vary depending on the room and game.
    public Player player; //The Player of the game.
    public GameShop shop; //The shop for this game.

    /**
     * Adventure Game Constructor
     * __________________________
     * Initializes attributes
     *
     * @param name the name of the adventure
     */
    public AdventureGame(String name){
        this.synonyms = new HashMap<>();
        this.rooms = new HashMap<>();
        this.shop = new GameShop();
        this.directoryName = "Games/" + name; //all games files are in the Games directory!
        try {
            setUpGame();
        } catch (IOException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("An Error Occurred: " + e.getMessage());
        }
    }

    /**
     * Save the current state of the game to a file
     * 
     * @param file pointer to file to write to
     */
    public void saveModel(File file) {
        try {
            FileOutputStream outfile = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outfile);
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * setUpGame
     * __________________________
     *
     * @throws IOException in the case of a file I/O error
     */
    public void setUpGame() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        String directoryName = this.directoryName;
        AdventureLoader loader = new AdventureLoader(this, directoryName);
        loader.loadGame();

        // set up the player's current location
        this.player = new Player(this.rooms.get(1));
        this.shop.addPlayer(player);
    }

    /**
     * tokenize
     * __________________________
     *
     * @param input string from the command line
     * @return a string array of tokens that represents the command.
     */
    public String[] tokenize(String input){

        input = input.toUpperCase();
        String[] commandArray = input.split(" ");

        int i = 0;
        while (i < commandArray.length) {
            if(this.synonyms.containsKey(commandArray[i])){
                commandArray[i] = this.synonyms.get(commandArray[i]);
            }
            i++;
        }
        return commandArray;

    }

    /**
     * movePlayer
     * __________________________
     * Moves the player in the given direction, if possible.
     * Return false if the player wins or dies as a result of the move.
     *
     * @param direction the move command
     * @return false, if move results in death or a win (and game is over).  Else, true.
     */
    public boolean movePlayer(String direction) {
        return this.player.moveStrategy.move(direction, this.getPlayer(), this.rooms);
    }


    /**
     * interpretAction
     * Interpret the user's action.
     *
     * @param command String representation of the command.
     */
    public String interpretAction(String command) {

        String[] inputArray = tokenize(command); //look up synonyms
        PassageTable motionTable = this.player.getCurrentRoom().getMotionTable(); //where can we move?

        if (motionTable.optionExists(inputArray[0])) {
            if (!movePlayer(inputArray[0])) {
                if (this.player.getCurrentRoom().getMotionTable().getDirection().get(0).getDestinationRoom() == 0)
                    return "GAME OVER";
                else return "FORCED";
            } //something is up here! We are dead or we won.
            return null;
        } else if (Arrays.asList(this.actionVerbs).contains(inputArray[0])) {
            if (inputArray[0].equals("QUIT")) {
                return "GAME OVER";
            } //time to stop!
            else if (inputArray[0].equals("INVENTORY") && this.player.getInventory().size() == 0)
                return "INVENTORY IS EMPTY";
            else if (inputArray[0].equals("INVENTORY") && this.player.getInventory().size() > 0)
                return "THESE OBJECTS ARE IN YOUR INVENTORY:\n" + this.player.getInventory().toString();
            else if (inputArray[0].equals("TAKE") && inputArray.length < 2)
                return "THE TAKE COMMAND REQUIRES AN OBJECT";
            else if (inputArray[0].equals("DROP") && inputArray.length < 2)
                return "THE DROP COMMAND REQUIRES AN OBJECT";
            else if (inputArray[0].equals("TAKE") && inputArray.length == 2) {
                if (this.player.getCurrentRoom().checkIfObjectInRoom(inputArray[1])) {
                    this.player.takeObject(inputArray[1]);
                    return "YOU HAVE TAKEN:\n " + inputArray[1];
                } else {
                    return "THIS OBJECT IS NOT HERE:\n " + inputArray[1];
                }
            } else if (inputArray[0].equals("DROP") && inputArray.length == 2) {
                if (this.player.checkIfObjectInInventory(inputArray[1])) {
                    this.player.dropObject(inputArray[1]);
                    return "YOU HAVE DROPPED:\n " + inputArray[1];
                } else {
                    return "THIS OBJECT IS NOT IN YOUR INVENTORY:\n " + inputArray[1];
                }
            } else if (inputArray[0].equals("BEG")) {
                String[] listName = Arrays.copyOfRange(inputArray, 1, inputArray.length);
                String name = String.join(" ", listName);
                Room room = this.player.getCurrentRoom();
                for (NPC npc : room.NPCsInRoom) {
                    String[] full = npc.name.split(": ");
                    if (full[0].equalsIgnoreCase(name)) {
                        if (npc instanceof hostileNPC) {
                            return full[0] + " is hostile! You cannot beg for help!";
                        } else if (npc instanceof RewardNPC) {
                            if (npc.item != null) {
                                return full[0] + ":\n" + full[1] + "\n\n" + npc.getText() + "\n" + ((RewardNPC) npc).reward(this.player);
                            }
                            return full[0] + ":\n" + full[1] + "\n\n" + npc.getText() +  "\n" + full[0] + " has already given a Powerup.";

                        } else if (npc instanceof HealingNPC) {
                            String text = full[0] + ":\n" + full[1] + "\n\n" + npc.getText() + "\n" + ((HealingNPC) npc).healPlayer(this.player);
                            room.NPCsInRoom.remove(npc);
                            return text;
                        }
                    }
                }
                return "NPC is not in room!";
            } else if (inputArray[0].equalsIgnoreCase("type")){
                String[] listName = Arrays.copyOfRange(inputArray, 1, inputArray.length);
                String name = String.join(" ", listName);
                Room room = this.player.getCurrentRoom();
                for (NPC npc : room.NPCsInRoom) {
                    String[] full = npc.name.split(": ");
                    String nameClass = npc.getClass().getName().replace("AdventureModel.NPCs.", "");
                    if (full[0].equalsIgnoreCase(name)) {
                        if (npc.isHostile()){
                            return full[0]+ " is a "+ nameClass + " and is hostile!";
                        } else{
                            return full[0] + " is a " + nameClass + " and is friendly!";
                        }
                    }
                }
                return "NPC is not in room!";
            }else if (inputArray[0].equals("BUY") && inputArray.length == 2) {
                return shop.sellPowerUp(inputArray[1]);
            } else if (inputArray[0].equals("SELL") && inputArray.length == 2) {
                String objectName = inputArray[1];
                if (this.player.checkIfObjectInInventory(objectName)) {
                    AdventureObject object = this.player.getObject(objectName);
                    return this.shop.buyObject(object);
                }
                return "This object is not in your inventory!";
            }

        }
        return "INVALID COMMAND.";
    }



    /**
     * checkPlayerAlive
     * __________________________
     * Check if the player is still alive
     * If the player has health <= 0, player is dead and will be forced to move to the death room
     * and the game ends.
     * The inner if statement checking for rooms containing key 0 can be removed if the room.txt
     * is confirmed to have a death room as room 0.
     * @return true, if the player is alive or false, if the player's health <= 0
     */
    public boolean checkPlayerAlive(){
        if (this.player.getHealth() <= 0){
            if (this.rooms.containsKey(11)){
                Room deathRoom = this.rooms.get(11);
                this.player.setCurrentRoom(deathRoom);
                return false;
            }
        }
        return true;
    }

    /**
     * getDirectoryName
     * __________________________
     * Getter method for directory 
     * @return directoryName
     */
    public String getDirectoryName() {
        return this.directoryName;
    }

    /**
     * getInstructions
     * __________________________
     * Getter method for instructions 
     * @return helpText
     */
    public String getInstructions() {
        return helpText;
    }

    /**
     * getPlayer
     * __________________________
     * Getter method for Player 
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * getRooms
     * __________________________
     * Getter method for rooms 
     * @return map of key value pairs (integer to room)
     */
    public HashMap<Integer, Room> getRooms() {
        return this.rooms;
    }

    /**
     * getSynonyms
     * __________________________
     * Getter method for synonyms 
     * @return map of key value pairs (synonym to command)
     */
    public HashMap<String, String> getSynonyms() {
        return this.synonyms;
    }

    /**
     * setHelpText
     * __________________________
     * Setter method for helpText
     * @param help which is text to set
     */
    public void setHelpText(String help) {
        this.helpText = help;
    }


}
