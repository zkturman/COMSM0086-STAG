package StagActions;

import StagExceptions.StagException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StagGenericAction's only job is to contain the template for a custom action.
 */
public class StagGenericAction {

    private HashSet<String> triggerWords;
    private HashSet<String> subjectObjects;
    private HashSet<String> consumedObjects;
    private HashSet<String> producedObjects;
    private String narration;

    public StagGenericAction(){
    }

    public void setConsumedObjects(HashSet<String> consumedObjects){
        this.consumedObjects = consumedObjects;
    }

    public HashSet<String> getConsumedObjects(){
        return this.consumedObjects;
    }

    public void setSubjectObjects(HashSet<String> subjectObjects){
        this.subjectObjects = subjectObjects;
    }

    public HashSet<String> getSubjectObjects(){
        return this.subjectObjects;
    }

    public void setTriggerWords(HashSet<String> triggerWords){
        this.triggerWords = new HashSet<>();
        //put all triggers in lower case because commands converted to lower case
        for(String trigger : triggerWords){
            this.triggerWords.add(trigger.toLowerCase());
        }
    }

    public void setProducedObjects(HashSet<String> producedObjects){
        this.producedObjects = producedObjects;
    }

    public HashSet<String> getProducedObjects() {
        return producedObjects;
    }

    public void setNarration(String narrationText){
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
        return Pattern.compile("(\\s+|^)" + trigger + "(\\s+|$)");
    }

    public static void test() throws StagException {
        StagGenericAction testAction = new StagGenericAction();
        HashSet<String> triggers = new HashSet<>();
        triggers.add("test"); triggers.add("action");
        testAction.setTriggerWords(triggers);
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
