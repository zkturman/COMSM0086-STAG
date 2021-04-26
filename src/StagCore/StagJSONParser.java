package StagCore;

import StagActions.StagAction;

import java.io.*;
import java.util.ArrayList;

import StagActions.StagGenericAction;
import StagExceptions.StagConfigReadException;
import StagExceptions.StagException;
import org.json.simple.*;

public class StagJSONParser {

    private String actionsFile;
    private JSONObject actionObject;

    public StagJSONParser(String filename) throws StagException {
        actionsFile = filename;
        try {
            FileReader reader = new FileReader(actionsFile);
            BufferedReader buffReader = new BufferedReader(reader);
            String jsonString = buildJSONString(buffReader);
            actionObject = (JSONObject) JSONValue.parse(jsonString);
            buffReader.close();
        }
        catch(IOException exception){
            throw new StagConfigReadException();
        }
    }

    private String buildJSONString(BufferedReader jsonReader) throws IOException {
        StringBuilder jsonString = new StringBuilder();
        String jsonLine = jsonReader.readLine();
        while(jsonLine != null){
            jsonString.append(jsonLine);
            jsonLine = jsonReader.readLine();
        }
        return jsonString.toString();
    }

    public ArrayList<StagAction> generateActions(){
        JSONArray actionArray = (JSONArray) actionObject.get("actions");
        for(Object obj : actionArray){
            StagAction newAction = new StagGenericAction();
            Object consumed = ((JSONObject) obj).get("consumed");
            System.out.println(consumed.toString());
            Object consumedObj = ((JSONArray) consumed).get(0);
            System.out.println(consumedObj.toString());
            Object subjects = ((JSONObject) obj).get("subjects");
            System.out.println(subjects.toString());
            Object subjectsObj = ((JSONArray) subjects).get(0);
            System.out.println(subjectsObj.toString());
            //narration is always a single string
            Object narration = ((JSONObject) obj).get("narration");
            System.out.println(narration.toString());
//            Object narrationObj = ((JSONArray) narration).get(0);
//            System.out.println(narrationObj.toString());
            Object triggers = ((JSONObject) obj).get("triggers");
            Object produced = ((JSONObject) obj).get("produced");

        }
        return null;
    }
}
