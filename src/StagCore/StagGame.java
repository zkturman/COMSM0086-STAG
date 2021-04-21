package StagCore;

import StagActions.StagAction;
import StagEntities.*;
import java.util.ArrayList;

public class StagGame {

    private StagLocation unplaced;
    private StagLocation startLocation;
    private StagPlayer[] players;
    private ArrayList<StagAction> customActions;

    public void setUnplaced(StagLocation unplaced) {
        this.unplaced = unplaced;
    }

    public void setStartLocation(StagLocation startLocation) {
        this.startLocation = startLocation;
    }

    public void setCustomActions(ArrayList<StagAction> customActions) {
        this.customActions = customActions;
    }
}
