package StagCore;

import StagActions.StagAction;
import StagActions.StagGenericAction;
import StagEntities.*;
import StagExceptions.StagException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The sole purpose of this class is to contain a game's current configuration
 */
public class StagGame {

    private HashMap<String, StagLocation> gameLocations;
    private StagLocation startLocation;
    private StagLocation unplacedLocation;
    private HashMap<String, StagPlayer> players;
    private ArrayList<StagAction> customActions;
    private StagJSONParser actionGenerator;
    private StagGraphParser locationInfo;

    public StagGame(){}

    public void generateActions(String filename) throws StagException {
        actionGenerator = new StagJSONParser(filename);
        this.customActions = actionGenerator.generateActions();
    }

    public void generateLocations(String filename) throws StagException {
        locationInfo = new StagGraphParser(filename);
        locationInfo.generateGraphs();

        //build map of locations
        StagLocationGenerator locationGenerator = new StagLocationGenerator();
        locationGenerator.setLocationGraphs(locationInfo.getLocationSettings());
        gameLocations = locationGenerator.generateLocations();

        //build connections between locations
        StagLocationConnector locationConnector = new StagLocationConnector();
        locationConnector.setLocationPaths(locationInfo.getPathSettings());
        locationConnector.setGameLocations(gameLocations);
        locationConnector.generateConnections();

        this.unplacedLocation = gameLocations.get("unplaced");
        this.startLocation = gameLocations.get(locationGenerator.getFirstLocation());
    }

    public ArrayList<StagAction> getCustomActions() {
        return customActions;
    }

    public HashMap<String, StagLocation> getGameLocations() {
        return gameLocations;
    }

    public StagLocation getStartLocation() {
        return startLocation;
    }

    public StagLocation getUnplacedLocation() {
        return unplacedLocation;
    }

    public static void test () throws StagException {
        StagGame game = new StagGame();

        //test actions
        game.generateActions("src/basic-actions.json");
        ArrayList<StagAction> customActions = game.getCustomActions();
        assert customActions.size() == 4;
        StagGenericAction testAction = (StagGenericAction) customActions.get(0);
        assert testAction.isTriggerWord("open");
        assert !testAction.isTriggerWord("zzzz");

        //test locations
        game.generateLocations("src/basic-entities.dot");
        HashMap<String, StagLocation> gameLocations = game.getGameLocations();
        assert gameLocations.size() == 4;
        assert gameLocations.get("start") != null;
        assert gameLocations.get("zzzz") == null;
        assert gameLocations.get("") == null;
        StagLocation startLocation = game.getStartLocation();
        StagLocation unplaced = game.getUnplacedLocation();
        assert gameLocations.get("start") == startLocation;
        assert gameLocations.get("start") != unplaced;
        assert gameLocations.get("unplaced") == unplaced;

        //test connections

    }
}
