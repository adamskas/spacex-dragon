package pl.skasu.dragon.exception;

public class MissionAlreadyExistsException extends Exception {

    public MissionAlreadyExistsException(String missionName) {
        super("Mission '" + missionName + "' already exists.");
    }
}
