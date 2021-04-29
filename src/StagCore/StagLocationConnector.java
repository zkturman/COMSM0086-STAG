package StagCore;

import StagEntities.StagLocation;
import com.alexmerz.graphviz.objects.Edge;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The sole purpose of this class is to add connections to existing locations
 */
public class StagLocationConnector {

    private ArrayList<Edge> locationPaths;
    private HashMap<String, StagLocation> gameLocations;

    public StagLocationConnector(){}

    public void setLocationPaths(ArrayList<Edge> locationPaths) {
        this.locationPaths = locationPaths;
    }

    public void setGameLocations(HashMap<String, StagLocation> gameLocations) {
        this.gameLocations = gameLocations;
    }

    public void generateConnections(){

    }
}
