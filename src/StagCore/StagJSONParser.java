package StagCore;

import StagActions.StagAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import StagExceptions.StagConfigReadException;
import StagExceptions.StagException;
import org.json.simple.*;
import org.json.simple.parser.*;

public class StagJSONParser {

    private String actionsFile;
    private JSONParser actionParser;

    public StagJSONParser(String filename) throws StagException {
        actionsFile = filename;
        actionParser = new JSONParser();
        try {
            FileReader reader = new FileReader(actionsFile);
            actionParser.parse(reader);
        }
        catch(IOException | ParseException exception){
            throw new StagConfigReadException();
        }
    }

    public ArrayList<StagAction> generateActions(){
        return null;
    }
}
