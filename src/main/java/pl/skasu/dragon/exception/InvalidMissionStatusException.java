package pl.skasu.dragon.exception;

public class InvalidMissionStatusException extends Exception {
    public InvalidMissionStatusException(String missionName, String currentStatus, String requiredStatus) {
        super("Mission '" + missionName + "' has status '" + currentStatus + "', but '" + requiredStatus + "' was expected.");
    }
}
