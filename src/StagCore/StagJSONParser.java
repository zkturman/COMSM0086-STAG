package StagCore;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import StagActions.StagGenericAction;
import StagExceptions.StagConfigReadException;
import StagExceptions.StagException;
import StagExceptions.StagMalformedActionException;
import org.json.simple.*;

/**
 * StagJSONParser's only job is to turn a json file into an ArrayList of StagActions.
 */
public class StagJSONParser {

    private final JSONObject actionObject;

    public StagJSONParser(String filename) throws StagException {
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader buffReader = new BufferedReader(reader);
            String jsonString = buildJSONString(buffReader);
            actionObject = (JSONObject) JSONValue.parse(jsonString);
            buffReader.close();
        }
        catch(IOException exception){
            throw new StagConfigReadException("Could not parse action file.");
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

    public ArrayList<StagGenericAction> generateActions() throws StagException {
        ArrayList<StagGenericAction> actionList = new ArrayList<>();
        checkExpectedType(JSONArray.class, actionObject.get("actions"));
        JSONArray actionArray = (JSONArray) actionObject.get("actions");
        for(Object obj : actionArray){
            checkNull(obj);
            checkExpectedType(JSONObject.class, obj);
            StagGenericAction newAction = new StagGenericAction();
            newAction.setConsumedObjects(createSetFromJSON(obj, "consumed"));
            newAction.setSubjectObjects(createSetFromJSON(obj, "subjects"));
            newAction.setTriggerWords(createSetFromJSON(obj, "triggers"));
            newAction.setProducedObjects(createSetFromJSON(obj, "produced"));
            //narration is always a single string
            newAction.setNarration(returnStringFromKey(obj, "narration"));
            actionList.add(newAction);
        }
        return actionList;
    }

    @SuppressWarnings("SameParameterValue")
    private String returnStringFromKey(Object obj, String keyName) throws StagException{
        Object jsonString = ((JSONObject) obj).get(keyName);
        checkExpectedType(String.class, jsonString);
        return (String) jsonString;
    }

    private JSONArray returnArrayFromKey(Object obj, String keyName) throws StagException {
        Object jsonArray = ((JSONObject) obj).get(keyName);
        checkExpectedType(JSONArray.class, jsonArray);
        return (JSONArray) jsonArray;
    }

    private HashSet<String> createSetFromJSON(Object obj, String keyName) throws StagException{
        HashSet<String> setToBuild = new HashSet<>();
        JSONArray jsonArray = returnArrayFromKey(obj, keyName);
        //should be an array of strings
        for(Object jsonString: jsonArray){
            checkExpectedType(String.class, jsonString);
            setToBuild.add((String) jsonString);
        }
        return setToBuild;
    }

    private void checkExpectedType (Class<?> cl, Object obj) throws StagException{
        checkNull(obj);
        if (!cl.isInstance(obj)){
            throw new StagMalformedActionException("Error casting object in activity file.");
        }
    }

    private void checkNull(Object valueToCheck) throws StagException {
        if (valueToCheck == null){
            throw new StagMalformedActionException("Null object in activity file.");
        }
    }

    public static void test() throws StagException {
        StagJSONParser testParser = new StagJSONParser("src/basic-actions.json");
        ArrayList<StagGenericAction> testArray = testParser.generateActions();
        assert testArray.size() == 4;
    }
}
