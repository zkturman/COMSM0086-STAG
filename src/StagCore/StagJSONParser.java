package StagCore;

import StagActions.*;

import java.io.*;
import java.util.ArrayList;

import StagActions.StagGenericAction;
import StagExceptions.StagConfigReadException;
import StagExceptions.StagException;
import StagExceptions.StagMalformedActionError;
import org.json.simple.*;

/**
 * StagJSONParser's only job is to turn a json file into an ArrayList of StagActions.
 */
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

    public ArrayList<StagAction> generateActions() throws StagException {
        ArrayList<StagAction> actionList = new ArrayList<>();
        checkExpectedType(JSONArray.class, actionObject.get("actions"));
        JSONArray actionArray = (JSONArray) actionObject.get("actions");
        for(Object obj : actionArray){
            checkNull(obj);
            checkExpectedType(JSONObject.class, obj);
            StagGenericAction newAction = new StagGenericAction();
            newAction.setConsumedObjects(createArrayFromJSON(obj, "consumed"));
            newAction.setSubjectObjects(createArrayFromJSON(obj, "subjects"));
            newAction.setTriggerWords(createArrayFromJSON(obj, "triggers"));
            newAction.setProducedObjects(createArrayFromJSON(obj, "produced"));
            //narration is always a single string
            newAction.setNarration(returnStringFromKey(obj, "narration"));
        }
        return null;
    }

    private String returnStringFromKey(Object obj,String keyName) throws StagException{
        Object jsonString = ((JSONObject) obj).get(keyName);
        checkExpectedType(String.class, jsonString);
        return (String) jsonString;
    }

    private JSONArray returnArrayFromKey(Object obj, String keyName) throws StagException {
        Object jsonArray = ((JSONObject) obj).get(keyName);
        checkExpectedType(JSONArray.class, jsonArray);
        return (JSONArray) jsonArray;
    }

    private ArrayList<String> createArrayFromJSON(Object obj, String keyName) throws StagException{
        ArrayList<String> arrayToBuild = new ArrayList<>();
        JSONArray jsonArray = returnArrayFromKey(obj, keyName);
        //should be an array of strings
        for(Object jsonString: jsonArray){
            checkExpectedType(String.class, obj);
            arrayToBuild.add((String) obj);
        }
        return arrayToBuild;
    }

    private void checkExpectedType (Class<?> cl, Object obj) throws StagException{
        checkNull(obj);
        if (!cl.isInstance(obj)){
            throw new StagMalformedActionError();
        }
    }

    private void checkNull(Object valueToCheck) throws StagException {
        if (valueToCheck == null){
            throw new StagMalformedActionError();
        }
    }

}
