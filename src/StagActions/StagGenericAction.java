package StagActions;

import StagEntities.StagEntity;

public class StagGenericAction implements StagAction {

    private String[] triggers;
    private StagEntity[] requiredObjects;
    private StagEntity[] consumedObjects;
    private StagEntity[] producedObjects;
}
