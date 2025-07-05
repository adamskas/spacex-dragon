package pl.skasu.dragon.exception;

public class RocketAlreadyAssignedException extends Exception {
    public RocketAlreadyAssignedException(String rocketName, String missionId) {
        super("Rocket '" + rocketName + "' is already assigned to mission '" + missionId + "'.");
    }
}
