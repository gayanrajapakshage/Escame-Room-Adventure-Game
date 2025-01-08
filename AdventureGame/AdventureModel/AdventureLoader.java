package AdventureModel;

import AdventureModel.NPCs.Blacksmith;
import AdventureModel.NPCs.NPC;
import AdventureModel.PowerUps.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Class AdventureLoader. Loads an adventure from files.
 */
public class AdventureLoader {

    private AdventureGame game; //the game to return
    private String adventureName; //the name of the adventure

    /**
     * Adventure Loader Constructor
     * __________________________
     * Initializes attributes
     * @param game the game that is loaded
     * @param directoryName the directory in which game files live
     */
    public AdventureLoader(AdventureGame game, String directoryName) {
        this.game = game;
        this.adventureName = directoryName;
    }

     /**
     * Load game from directory
     */
    public void loadGame() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        parseRooms();
        parseObjects();
        parseSynonyms();
        parseNPCs();
        this.game.setHelpText(parseOtherFile("help"));
        this.game.shop.setWelcomeText(parseOtherFile("shop-welcome"));
    }

     /**
     * Parse Rooms File
     */
    private void parseRooms() throws IOException {

        int roomNumber;

        String roomFileName = this.adventureName + "/rooms.txt";
        BufferedReader buff = new BufferedReader(new FileReader(roomFileName));

        while (buff.ready()) {

            String currRoom = buff.readLine(); // first line is the number of a room

            roomNumber = Integer.parseInt(currRoom); //current room number

            // now need to get room name
            String roomName = buff.readLine();

            // now we need to get the description
            String roomDescription = "";
            String line = buff.readLine();
            while (!line.equals("-----")) {
                roomDescription += line + "\n";
                line = buff.readLine();
            }
            roomDescription += "\n";

            // now we make the room object
            Room room = new Room(roomName, roomNumber, roomDescription, adventureName);

            // now we make the motion table
            line = buff.readLine(); // reads the line after "-----"
            while (line != null && !line.equals("")) {
                String[] part = line.split(" \s+"); // have to use regex \\s+ as we don't know how many spaces are between the direction and the room number
                String direction = part[0];
                String dest = part[1];
                if (dest.contains("/")) {
                    String[] blockedPath = dest.split("/");
                    String dest_part = blockedPath[0];
                    String object = blockedPath[1];
                    Passage entry = new Passage(direction, dest_part, object);
                    room.getMotionTable().addDirection(entry);
                } else {
                    Passage entry = new Passage(direction, dest);
                    room.getMotionTable().addDirection(entry);
                }
                line = buff.readLine();
            }
            this.game.getRooms().put(room.getRoomNumber(), room);
        }

    }

     /**
     * Parse Objects File
     */
    public void parseObjects() throws IOException {

        String objectFileName = this.adventureName + "/objects.txt";
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName));

        while (buff.ready()) {
            String objectName = buff.readLine();
            String objectDescription = buff.readLine();
            String objectLocation = buff.readLine();
            String objectWorth = buff.readLine();
            String separator = buff.readLine();
            if (separator != null && !separator.isEmpty())
                System.out.println("Formatting Error!");
            int i = Integer.parseInt(objectLocation);
            Room location = this.game.getRooms().get(i);
            double worth = Double.parseDouble(objectWorth);
            AdventureObject object = new AdventureObject(objectName, objectDescription, location, worth);
            location.addGameObject(object);
        }

    }

    public void parseNPCs() throws IOException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        String objectFileName = this.adventureName + "/NPCs.txt";
        BufferedReader buff = new BufferedReader(new FileReader(objectFileName));

        while (buff.ready()){
            String classname = buff.readLine();
            Class<?> classType = Class.forName("AdventureModel.NPCs." + classname);
            String name = buff.readLine();
            double hp = Double.parseDouble(buff.readLine());
            Room room = this.game.getRooms().get(Integer.parseInt(buff.readLine()));
            double damage_or_healing = 0;
            if (!(classname.equals("Blacksmith") || classname.equals("Merchant"))){
                damage_or_healing = Double.parseDouble(buff.readLine());
            }

            String NPCtext = "";
            String line = buff.readLine();
            while (!line.equals("-----")) {
                NPCtext += line + "\n";
                line = buff.readLine();
            }

            line = buff.readLine();
            String NPCDeathText = "";
            while (line != null && !line.isEmpty()){
                NPCDeathText += line + "\n";
                line = buff.readLine();
            }

            NPC NewNPC;
            if (classname.equals("Blacksmith")){
                StrengthPotion potion = new StrengthPotion();
                Constructor<?> constructor = classType.getConstructor(String.class, double.class, StrengthPotion.class, Room.class, String.class, String.class);
                NewNPC = (NPC) constructor.newInstance(name, hp, potion, room, NPCtext, NPCDeathText);
            } else if (classname.equals("Merchant")){
                MasterKey masterKey = new MasterKey();
                Constructor<?> constructor = classType.getConstructor(String.class, double.class, MasterKey.class, Room.class, String.class, String.class);
                NewNPC = (NPC) constructor.newInstance(name, hp, masterKey, room, NPCtext, NPCDeathText);
            } else{
                HealthPotion healthPotion = new HealthPotion();
                Constructor<?> constructor = classType.getConstructor(String.class, double.class, PowerUp.class, Room.class, String.class, String.class, double.class);
                NewNPC = (NPC) constructor.newInstance(name, hp, healthPotion, room, NPCtext, NPCDeathText, damage_or_healing);
            }
            room.NPCsInRoom.add(NewNPC);
        }
    }

     /**
     * Parse Synonyms File
     */
    public void parseSynonyms() throws IOException {
        String synonymsFileName = this.adventureName + "/synonyms.txt";
        BufferedReader buff = new BufferedReader(new FileReader(synonymsFileName));
        String line = buff.readLine();
        while(line != null){
            String[] commandAndSynonym = line.split("=");
            String command1 = commandAndSynonym[0];
            String command2 = commandAndSynonym[1];
            this.game.getSynonyms().put(command1,command2);
            line = buff.readLine();
        }

    }

    /**
     * Parse Files other than Rooms, Objects and Synonyms
     *
     * @param fileName the file to parse
     */
    public String parseOtherFile(String fileName) throws IOException {
        String text = "";
        fileName = this.adventureName + "/" + fileName + ".txt";
        BufferedReader buff = new BufferedReader(new FileReader(fileName));
        String line = buff.readLine();
        while (line != null) { // while not EOF
            text += line+"\n";
            line = buff.readLine();
        }
        return text;
    }

}
