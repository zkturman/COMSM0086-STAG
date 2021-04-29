package StagCore;

import StagActions.StagAction;
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
//    private StagPlayer[] players;
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
//        this.startLocation = locationGenerator.getFirstLocation();
    }

    public void setUnplaced(StagLocation unplaced) {
        this.unplacedLocation = unplaced;
    }

    public void setStartLocation(StagLocation startLocation) {
        this.startLocation = startLocation;
    }

    public static void test () {

    }
}
