package StagEntities;

import StagExceptions.StagException;
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
        for (Edge path : locationPaths){
            String fromLocKey = path.getSource().getNode().getId().getId();
            StagLocation fromLocation = gameLocations.get(fromLocKey);
            String toLocKey = path.getTarget().getNode().getId().getId();
            StagLocation toLocation = gameLocations.get(toLocKey);
            fromLocation.addNeighbor(toLocation);
        }
    }
}
