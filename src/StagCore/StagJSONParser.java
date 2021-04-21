package StagCore;

import StagActions.StagAction;
import java.util.ArrayList;
import org.json.simple.*;
import org.json.simple.parser.*;

public class StagJSONParser {

    private String actionsFile;
    private JSONParser actionParser;

    public StagJSONParser(String filename){
        actionsFile = filename;
        actionParser = new JSONParser();
    }

    public static ArrayList<StagAction> generateActions(){
        return null;
    }
}
