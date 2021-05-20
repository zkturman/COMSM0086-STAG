package StagCore;

import StagExceptions.*;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The sole purpose of this class is to separate the locations and paths from a given DOT file
 */
public class StagGraphParser  {

    private final String entitiesFile;
    private final Parser locationParser;
    private ArrayList<Graph> locationConfig;
    private ArrayList<Graph> locationSettings;
    private ArrayList<Edge> pathSettings;

    public StagGraphParser(String filename) {
        entitiesFile = filename;
        locationParser = new Parser();
    }

    public ArrayList<Graph> getLocationSettings() {
        return locationSettings;
    }

    public ArrayList<Edge> getPathSettings() {
        return pathSettings;
    }

    public void generateGraphs() throws StagException {
        parseGraphs();

        ArrayList<Graph> parentGraph = locationParser.getGraphs();
        //should always be size one
        verifyListSize(parentGraph, 1);

        locationConfig = parentGraph.get(0).getSubgraphs();
        //check that subgraphs is length 2 --> locations and paths
        verifyListSize(locationConfig, 2);
        separateConfig();
    }

    private void parseGraphs() throws StagException {
        try {
            FileReader reader = new FileReader(entitiesFile);
            locationParser.parse(reader);
            reader.close();
        }
        catch (IOException | ParseException exception){
            throw new StagConfigReadException("Could not parse entity file.");
        }
    }

    private void separateConfig() throws StagException {
        //one index has to be locations, and only one will return subgraphs
        //TODO do we need to check the id is location and path
        locationSettings = locationConfig.get(0).getSubgraphs();
        pathSettings = locationConfig.get(1).getEdges();

        if (locationSettings == null){
            locationSettings = locationConfig.get(1).getSubgraphs();
            pathSettings = locationConfig.get(0).getEdges();
        }
        checkNull(locationSettings);
        checkNull(pathSettings);
    }

    private void verifyListSize(ArrayList<?> list, int size) throws StagException{
        checkNull(list);
        if (list.size() != size){
            throw new StagMalformedLocationException("Entity file should only have a location and path node.");
        }
    }

    private void checkNull(Object obj) throws StagException {
        if (obj == null){
            throw new StagMalformedLocationException("Unexpected null graphs in entity file.");
        }
    }

    public static void test() throws StagException {
        StagGraphParser testParser = new StagGraphParser("src/basic-entities.dot");
        testParser.generateGraphs();
        assert testParser.getLocationSettings().size() == 4;
        assert testParser.getPathSettings().size() == 3;
    }

}
