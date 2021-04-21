package StagCore;

import StagEntities.StagLocation;
import StagExceptions.StagConfigReadException;
import StagExceptions.StagException;
import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class StagGraphParser  {

    private String entitiesFile;
    private Parser locationParser;
    ArrayList<Graph> locationGraphs;

    public StagGraphParser(String filename){
        entitiesFile = filename;
        locationParser = new Parser();
    }

    private void generateGraphs() throws StagException {
        try {
            FileReader reader = new FileReader(entitiesFile);
            locationParser.parse(reader);
        }
        catch (FileNotFoundException | ParseException exception){
            throw new StagConfigReadException();
        }
        locationGraphs = locationParser.getGraphs();
    }

    public static StagLocation generateLocations(){
        return null;
    }
}
