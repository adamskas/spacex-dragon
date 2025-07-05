package pl.skasu.dragon.exception;

public class RocketNotPartOfMissionException extends RuntimeException {

    public RocketNotPartOfMissionException(String rocketName, String missionName) {
        super("Rocket '" + rocketName + "' is not part of mission '" + missionName + "'.");
    }
}
