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

    public void updateLocationItems() throws StagException {
        if (itemType.equals(StagItem.ARTEFACT)) location.setArtefacts(itemMap);
        if (itemType.equals(StagItem.CHARACTER)) location.setCharacters(itemMap);
        if (itemType.equals(StagItem.FURNITURE)) location.setFurniture(itemMap);
    }

    public static void test() {

    }
}
