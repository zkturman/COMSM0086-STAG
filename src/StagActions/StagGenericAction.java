package StagActions;

import StagEntities.StagLocation;
import StagEntities.StagPlayer;
import StagExceptions.StagException;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StagGenericAction's only job is to generate narration if an action is valid.
 */
public class StagGenericAction implements StagAction {

    private HashSet<String> triggerWords;
    private HashSet<String> subjectObjects;
    private HashSet<String> consumedObjects;
    private HashSet<String> producedObjects;
    private String narration;

    public StagGenericAction(){
    }

    public void setConsumedObjects(HashSet<String> consumedObjects) throws StagException {
        this.consumedObjects = consumedObjects;
    }

    public void setSubjectObjects(HashSet<String> subjectObjects) throws StagException {
        this.subjectObjects = subjectObjects;
    }

    public void setTriggerWords(HashSet<String> triggerWords) throws StagException {
        this.triggerWords = triggerWords;
    }

    public void setProducedObjects(HashSet<String> producedObjects) throws StagException {
        this.producedObjects = producedObjects;
    }

    public void setNarration(String narrationText) throws StagException {
        this.narration = narrationText;
    }

    public boolean isTriggerWord(String actionName){
        return triggerWords.contains(actionName);
    }

    public boolean canTryAction(String message){
        return (containsPhrase(message, triggerWords) && containsPhrase(message, subjectObjects));
    }

    public boolean containsPhrase(String message, HashSet<String> list){
        boolean found = false;
        for (String element : list){
            Matcher matchTrigger = createPattern(element).matcher(message);
            if (matchTrigger.find()){
                found = true;
            }
        }
        return found;
    }

    private Pattern createPattern(String trigger){
        Pattern pattern = Pattern.compile("(\\s+|^)" + trigger + "(\\s+|$)");
        return pattern;
    }

    public static void test() throws StagException {
        StagGenericAction testAction = new StagGenericAction();
        HashSet<String> triggers = new HashSet<>();
        triggers.add("test"); triggers.add("action");
        testAction.setTriggerWords(triggers);
        assert testAction.isTriggerWord("action");
        assert !testAction.isTriggerWord("acting");
        assert testAction.isTriggerWord("test");
        assert testAction.containsPhrase("this is a test", triggers);
        assert testAction.containsPhrase("test is a test", triggers);
        assert testAction.containsPhrase(" test is a test", triggers);
        assert testAction.containsPhrase("     test      is a ", triggers);
        assert testAction.containsPhrase("this is a test     ", triggers);
        assert testAction.containsPhrase("this is a test ", triggers);
        assert !testAction.containsPhrase("this is a ", triggers);

        //checks that subjects check works too
        HashSet<String> subjects = new HashSet<>();
        subjects.add("key"); subjects.add("sword");
        testAction.setSubjectObjects(subjects);
        assert testAction.canTryAction("test key");
        assert testAction.canTryAction(" test key");
        assert testAction.canTryAction("test key ");
        assert testAction.canTryAction("test     key");
        assert testAction.canTryAction("   test    key   ");
        assert testAction.canTryAction("please test key");
        assert testAction.canTryAction("please test a key");
        assert testAction.canTryAction("please test a key ");
        assert testAction.canTryAction(" please test a key");
        assert testAction.canTryAction("please test a key ");
    }
}
