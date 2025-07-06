package pl.skasu.dragon.exception;

/**
 * Exception thrown when an operation is attempted involving a rocket that is not part
 * of the specified mission. This exception is used to indicate a mismatch between the
 * rocket and the mission it is expected to belong to.
 */
public class RocketNotPartOfMissionException extends Exception {

    /**
     * Exception thrown when a rocket is not part of the specified mission.
     * This exception is used to indicate that an operation involving a rocket
     * cannot proceed because the rocket is not currently assigned to the given mission.
     *
     * @param rocketName  The name of the rocket that is not part of the mission.
     * @param missionName The name of the mission that does not include the specified rocket.
     */
    public RocketNotPartOfMissionException(String rocketName, String missionName) {
        super("Rocket '" + rocketName + "' is not part of mission '" + missionName + "'.");
    }
}
