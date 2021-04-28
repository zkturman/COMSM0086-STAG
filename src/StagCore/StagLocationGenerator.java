package StagCore;

import StagEntities.StagEntity;
import StagEntities.StagItem;
import StagEntities.StagLocation;
import StagExceptions.StagException;
import com.alexmerz.graphviz.objects.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StagLocationGenerator {

    private ArrayList<Graph> locationGraphs;

    public StagLocationGenerator(StagGraphParser locationSettings) throws StagException {
        locationGraphs = locationSettings.getLocationSettings();
    }

    public HashMap<String, StagLocation> generateLocations(){
        HashMap<String, StagLocation> gameLocations = new HashMap<>();
        return null;
    }

    private StagLocation createLocation(){
        StagLocation locationToCreate = new StagLocation();
        //set name
        //set artefacts
        //set furniture
        //set characters
        return null;
    }

    private HashMap<String, StagItem> generateItems(){
        return null;
    }

    private StagItem createObject(String entityType){
        StagItem entityToCreate = new StagItem();
        return null;
    }

}
