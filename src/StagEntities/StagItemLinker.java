package StagEntities;

import StagExceptions.StagException;
import java.util.HashMap;

/**
 * The sole purpose of this class is to add a list of items to a location
 */
public class StagItemLinker {
    private final StagItem itemType;
    private final StagLocation location;
    private HashMap<String, String> itemMap;

    public StagItemLinker(StagItem type, StagLocation location){
        itemType = type;
        this.location = location;
    }

    public void setItemMap(HashMap<String, String> itemMap) {
        this.itemMap = itemMap;
    }

    public void updateLocationItems(){
        if (itemType.equals(StagItem.ARTEFACTS)) location.setArtefacts(itemMap);
        if (itemType.equals(StagItem.CHARACTERS)) location.setCharacters(itemMap);
        if (itemType.equals(StagItem.FURNITURE)) location.setFurniture(itemMap);
    }

    public static void test() throws StagException {
        StagLocation testLoc = new StagLocation("test");
        StagItemLinker testLinker = new StagItemLinker(StagItem.ARTEFACTS, testLoc);
        HashMap<String, String> testItemSet = new HashMap<>();
        testItemSet.put("artefact",  "this is a description");
        testLinker.setItemMap(testItemSet);
        testLinker.updateLocationItems();
        HashMap<String, String> testArtefacts = testLoc.getArtefacts();
        assert testArtefacts.get("artefact").equals("this is a description");
    }
}
