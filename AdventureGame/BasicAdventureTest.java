
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import AdventureModel.AdventureGame;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicAdventureTest {
    @Test
    void getCommandsTest() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String commands = game.player.getCurrentRoom().getCommands();
        assertEquals("WEST, UP, NORTH, IN, SOUTH, DOWN", commands);
    }

    @Test
    void getObjectString() throws IOException {
        AdventureGame game = new AdventureGame("TinyGame");
        String objects = game.player.getCurrentRoom().getObjectString();
        assertEquals("a copy of an illuminated manuscript", objects);
    }

    @Test
    void TestParseNPC() throws IOException, NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException{
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals(3, game.getRooms().get(1).NPCsInRoom.size());
        assertEquals(4, game.getRooms().get(2).NPCsInRoom.size());
        assertEquals(1, game.getRooms().get(3).NPCsInRoom.size());
        assertEquals(1, game.getRooms().get(4).NPCsInRoom.size());
        assertEquals(1, game.getRooms().get(5).NPCsInRoom.size());
        assertEquals(0, game.getRooms().get(6).NPCsInRoom.size());
    }

    @Test
    void test_getNPCString() throws IOException{
        AdventureGame game = new AdventureGame("TinyGame");
        assertEquals("Goddess Mary", game.getRooms().get(3).getNPCstring());
    }

}
