package StagActions;

import StagExceptions.StagException;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StagGenericAction's only job is to generate narration if an action is valid.
 */
public class StagGenericAction {

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

    public HashSet<String> getConsumedObjects(){
        return this.consumedObjects;
    }

    public void setSubjectObjects(HashSet<String> subjectObjects) throws StagException {
        this.subjectObjects = subjectObjects;
    }

    public HashSet<String> getSubjectObjects(){
        return this.subjectObjects;
    }
    public void setTriggerWords(HashSet<String> triggerWords) throws StagException {
        this.triggerWords = triggerWords;
    }

    public void setProducedObjects(HashSet<String> producedObjects) throws StagException {
        this.producedObjects = producedObjects;
    }

    public HashSet<String> getProducedObjects() {
        return producedObjects;
    }

    public void setNarration(String narrationText) throws StagException {
        this.narration = narrationText;
    }

    public String getNarration() {
        return this.narration;
    }

    public boolean canTryAction(String message){
        return (containsTrigger(message, triggerWords) && containsTrigger(message, subjectObjects));
    }

    public boolean containsTrigger(String message, HashSet<String> list){
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
//        assert testAction.isTriggerWord("action");
//        assert !testAction.isTriggerWord("acting");
//        assert testAction.isTriggerWord("test");
        assert testAction.containsTrigger("this is a test", triggers);
        assert testAction.containsTrigger("test is a test", triggers);
        assert testAction.containsTrigger(" test is a test", triggers);
        assert testAction.containsTrigger("     test      is a ", triggers);
        assert testAction.containsTrigger("this is a test     ", triggers);
        assert testAction.containsTrigger("this is a test ", triggers);
        assert !testAction.containsTrigger("this is a ", triggers);

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
        assert testAction.canTryAction("pleas test a sword");
    }
}
