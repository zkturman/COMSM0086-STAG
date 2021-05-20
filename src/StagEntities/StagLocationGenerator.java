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
            Node locationInfo = locationNode.get(0);
            String locationName = locationInfo.getId().getId();
            String locationDesc = locationInfo.getAttribute("description");
            ArrayList<Graph> locationItems = location.getSubgraphs();

            //add new location to hash map
            StagLocation locToAdd = new StagLocation(locationName);
            locToAdd.setDescription(locationDesc);
            createLocation(locToAdd, locationItems);
            gameLocations.put(locationName, locToAdd);
        }
        return gameLocations;
    }

    private void createLocation(StagLocation locationToUpdate, ArrayList<Graph> itemSettings) throws StagException {
        for (Graph itemList : itemSettings) {

            //get item type from node id. capitilise to convert to enum
            String itemType = itemList.getId().getId().toUpperCase();
            StagItem itemEnum = convertStringToType(itemType);

            ArrayList<Node> typedItems = itemList.getNodes(false);

            //helper class to set items in location
            StagItemLinker itemToAdd = new StagItemLinker(itemEnum, locationToUpdate);
            itemToAdd.setItemMap(generateItems(typedItems));
            itemToAdd.updateLocationItems();
        }
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
            throw new StagMalformedItemsException("Inappropriate entity type in entity file.");
        }
    }

    public String getFirstLocation(){
        //get first location settings
        Graph locationSettings = locationGraphs.get(0);

        //get name from first node in settings
        return locationSettings.getNodes(false).get(0).getId().getId();
    }

    public static void test(String entityFile) throws StagException {
        StagGraphParser testGraph = new StagGraphParser(entityFile);
        testGraph.generateGraphs();
        StagLocationGenerator testGenerator = new StagLocationGenerator();
        testGenerator.setLocationGraphs(testGraph.getLocationSettings());
        HashMap<String, StagLocation> testLocations = testGenerator.generateLocations();
        assert testLocations.size() == 4;
        assert testLocations.get("zzzz") == null;
        assert testLocations.get("start") != null;

        StagLocation testStart = testLocations.get("start");
        assert testStart.getArtefacts().get("potion") != null;
        assert testStart.getArtefacts().get("door") == null;
        assert testStart.getFurniture().get("door") != null;
    }
}
