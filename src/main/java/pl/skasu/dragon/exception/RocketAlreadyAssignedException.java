package pl.skasu.dragon.exception;

/**
 * Exception thrown when a rocket is already assigned to a specific mission.
 * This exception is used to indicate that a rocket cannot be assigned to a mission
 * because it is already associated with that mission.
 */
public class RocketAlreadyAssignedException extends Exception {

    /**
     * Constructs a {@code RocketAlreadyAssignedException} with the specified rocket and mission names.
     * This exception is thrown when attempting to assign a rocket to a mission,
     * but the rocket is already assigned to that mission.
     *
     * @param rocketName  The name of the rocket that is already assigned.
     * @param missionName The name of the mission to which the rocket is assigned.
     */
    public RocketAlreadyAssignedException(String rocketName, String missionName) {
        super("Rocket '" + rocketName + "' is already assigned to mission '" + missionName + "'.");
    }
}
