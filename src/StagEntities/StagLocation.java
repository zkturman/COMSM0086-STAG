package StagEntities;

import java.util.HashMap;

/**
 * The sole purpose of this class is to maintain the contents and connections for a game location.
 */
public class StagLocation{

    private String locationName;
    private String description;
    private HashMap<String, String> characters;
    private HashMap<String, String> artefacts;
    private HashMap<String, String> furniture;
    private HashMap<String, StagPlayer> players;
    private HashMap<String, StagLocation> neighborLocations;

    public StagLocation(String name){
        locationName = name;
        neighborLocations = new HashMap<>();
        characters = new HashMap<>();
        artefacts = new HashMap<>();
        furniture = new HashMap<>();
        players = new HashMap<>();
    }

    public String getLocationName() {
        return locationName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setArtefacts(HashMap<String, String> artefacts) {
        this.artefacts = artefacts;
    }

    public HashMap<String, String> getArtefacts() {
        return artefacts;
    }

    public void setCharacters(HashMap<String, String> characters) {
        this.characters = characters;
    }

    public HashMap<String, String> getCharacters() {
        return characters;
    }

    public void setFurniture(HashMap<String, String> furniture) {
        this.furniture = furniture;
    }

    public HashMap<String, String> getFurniture() {
        return furniture;
    }

    public void setPlayers(HashMap<String, StagPlayer> players) {
        this.players = players;
    }

    public HashMap<String, StagPlayer> getPlayers() {
        return players;
    }

    public HashMap<String, StagLocation> getNeighborLocations() {
        return neighborLocations;
    }

    public void addNeighbor(StagLocation newNeighbor){
        String neighborName = newNeighbor.getLocationName();
        neighborLocations.put(neighborName, newNeighbor);
    }

    public static void test() {

    }
}
