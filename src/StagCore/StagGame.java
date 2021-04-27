package StagCore;

import StagActions.StagAction;
import StagEntities.*;
import StagExceptions.StagException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.jar.JarEntry;

public class StagGame {

    private HashMap<String, StagLocation> gameLocations;
    private StagLocation startLocation;
    private StagLocation unplacedLocation;
    private StagPlayer[] players;
    private ArrayList<StagAction> customActions;
    private StagJSONParser actionGenerator;
    private StagGraphParser locationGenerator;

    public StagGame(){}

    public void generateActions(String filename) throws StagException {
        actionGenerator = new StagJSONParser(filename);
        this.customActions = actionGenerator.generateActions();
    }

    public void generateLocations(String filename) throws StagException {
        locationGenerator = new StagGraphParser(filename);
        this.unplacedLocation = gameLocations.get("unplaced");
        this.startLocation = locationGenerator.getFirstLocation();
    }

    public void setUnplaced(StagLocation unplaced) {
        this.unplacedLocation = unplaced;
    }

    public void setStartLocation(StagLocation startLocation) {
        this.startLocation = startLocation;
    }

}
