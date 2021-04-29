package StagEntities;

import java.util.HashMap;

/**
 * The sole purpose of this class is to maintain the contents and connections for a game location.
 */
public class StagLocation{

    private String locationName;
    private HashMap<String, String> characters;
    private HashMap<String, String> artefacts;
    private HashMap<String, String> furniture;
    private HashMap<String, String> players;
    private HashMap<String, String> neighborLocations;

    public StagLocation(String name){
        locationName = name;
    }
    public void setArtefacts(HashMap<String, String> artefacts) {
        this.artefacts = artefacts;
    }

    public void setCharacters(HashMap<String, String> characters) {
        this.characters = characters;
    }

    public void setFurniture(HashMap<String, String> furniture) {
        this.furniture = furniture;
    }

    public void setPlayers(HashMap<String, String> players) {
        this.players = players;
    }

    public void setNeighborLocations(HashMap<String, String> neighborLocations) {
        this.neighborLocations = neighborLocations;
    }

    public static void test() {

    }
}
