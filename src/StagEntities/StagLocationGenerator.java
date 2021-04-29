package StagEntities;

import StagCore.StagGraphParser;
import StagCore.StagUtility;
import StagExceptions.StagException;
import StagExceptions.StagMalformedItemsException;
import com.alexmerz.graphviz.objects.Graph;
import com.alexmerz.graphviz.objects.Node;
import java.util.*;

/**
 * The sole purpose of this class is to generate locations from the location subgraph of the graph file.
 */
public class StagLocationGenerator {

    private ArrayList<Graph> locationGraphs;

    public StagLocationGenerator() {}

    public void setLocationGraphs(ArrayList<Graph> locationGraphs) {
        this.locationGraphs = locationGraphs;
    }

    public HashMap<String, StagLocation> generateLocations() throws StagException {
        HashMap<String, StagLocation> gameLocations = new HashMap<>();

        for (Graph location : locationGraphs){
            ArrayList<Node> locationNode = location.getNodes(false);
            StagUtility.checkNull(locationNode);

            //location name in first node
            String locationName = locationNode.get(0).getId().getId();

            ArrayList<Graph> locationItems = location.getSubgraphs();
            StagLocation locToAdd = createLocation(locationName, locationItems);

            //add new location to hash map
            gameLocations.put(locationName, locToAdd);
        }
        return gameLocations;
    }

    private StagLocation createLocation(String locationName, ArrayList<Graph> itemSettings) throws StagException {
        StagLocation locationToCreate = new StagLocation(locationName);
        //set name
        for (Graph itemList : itemSettings) {

            //get item type from node id. capitilise to convert to enum
            String itemType = itemList.getId().getId().toUpperCase();
            StagItem itemEnum = convertStringToType(itemType);

            ArrayList<Node> typedItems = itemList.getNodes(false);

            //helper class to set items in location
            StagItemLinker itemToAdd = new StagItemLinker(itemEnum, locationToCreate);
            itemToAdd.setItemMap(generateItems(typedItems));
            itemToAdd.updateLocationItems();
        }
        return locationToCreate;
    }

    private HashMap<String, String> generateItems(ArrayList<Node> typedItems) throws StagException {
        StagUtility.checkNull(typedItems);
        HashMap<String, String> locItem = new HashMap<>();

        //add item and description to hash map
        for (Node discreetItem : typedItems){
            String itemName = discreetItem.getId().getId();
            String itemDescription = discreetItem.getAttribute("description");
            locItem.put(itemName, itemDescription);
        }
        return locItem;
    }

    private StagItem convertStringToType(String type) throws StagException {
        type = type.toUpperCase();
        try {
            return StagItem.valueOf(type);
        }
        catch (IllegalArgumentException iae){
            throw new StagMalformedItemsException();
        }
    }

    public static void test(){

    }
}
