package StagEntities;

import java.util.ArrayList;

public class StagLocation implements StagEntity{

    private ArrayList<StagLocation> neighborLocations;
    private StagCharacter[] characters;
    private StagObject[] objects;
}
