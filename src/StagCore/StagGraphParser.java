package StagCore;

import StagEntities.StagLocation;
import StagExceptions.StagConfigReadException;
import StagExceptions.StagException;
import StagExceptions.StagMalformedLocationException;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class StagGraphParser  {

    private String entitiesFile;
    private Parser locationParser;
    ArrayList<Graph> locationConfig;
    ArrayList<Graph> locationSettings;
    ArrayList<Edge> pathSettings;

    public StagGraphParser(String filename) throws StagException {
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
            throw new StagConfigReadException();
        }
    }

    private void separateConfig() throws StagException {
        //one index has to be locations, and only one will return subgraphs
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
            throw new StagMalformedLocationException();
        }
    }

    private void checkNull(Object obj) throws StagException {
        if (obj == null){
            throw new StagMalformedLocationException();
        }
    }

    public static void test() throws StagException {
        StagGraphParser testParser = new StagGraphParser("src/basic-entities.dot");

    }

}
