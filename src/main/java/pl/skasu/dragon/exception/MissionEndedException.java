package pl.skasu.dragon.exception;

public class MissionEndedException extends Exception {
    public MissionEndedException(String missionName) {
        super("Cannot modify mission '" + missionName + "'. It has 'ENDED' status.");
    }
}
