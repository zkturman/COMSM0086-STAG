package StagActions;

import StagExceptions.StagException;

import java.util.ArrayList;

/**
 * StagGenericAction's only job is to generate narration if an action is valid.
 */
public class StagGenericAction implements StagAction {

    private ArrayList<String> triggerWords;
    private ArrayList<String> subjectObjects;
    private ArrayList<String> consumedObjects;
    private ArrayList<String> producedObjects;
    private String narration;

    public StagGenericAction(){
        triggerWords = new ArrayList<>();
    }

    public void setConsumedObjects(ArrayList<String> consumedObjects) throws StagException {
        this.consumedObjects = consumedObjects;
    }

    public void setSubjectObjects(ArrayList<String> subjectObjects) throws StagException {
        this.subjectObjects = subjectObjects;
    }

    public void setTriggerWords(ArrayList<String> triggerWords) throws StagException {
        this.triggerWords = triggerWords;
    }

    public void setProducedObjects(ArrayList<String> producedObjects) throws StagException {
        this.producedObjects = producedObjects;
    }

    public void setNarration(String narrationText) throws StagException {
        this.narration = narrationText;
    }

    public boolean isTriggerWord(String actionName){
        return triggerWords.contains(actionName);
    }

    public static void test() throws StagException {
        StagGenericAction testAction = new StagGenericAction();
        ArrayList<String> triggers = new ArrayList<>();
        triggers.add("test"); triggers.add("action");
        testAction.setTriggerWords(triggers);
        assert testAction.isTriggerWord("action");
        assert !testAction.isTriggerWord("acting");
        assert testAction.isTriggerWord("test");
    }
}
