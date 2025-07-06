package pl.skasu.dragon.exception;

/**
 * Exception thrown when the status of a mission does not meet the required criteria.
 * This exception is used to indicate that a mission is in an incorrect state for the intended operation.
 */
public class InvalidMissionStatusException extends Exception {

    /**
     * Constructs an {@code InvalidMissionStatusException} with the specified mission name,
     * current status, and required status. This exception is thrown when a mission's status
     * does not match the required status for the intended operation.
     *
     * @param missionName   The name of the mission for which the status is invalid.
     * @param currentStatus The current status of the mission.
     * @param requiredStatus The status that is required but not met.
     */
    public InvalidMissionStatusException(String missionName, String currentStatus, String requiredStatus) {
        super("Mission '" + missionName + "' has status '" + currentStatus + "', but '" + requiredStatus + "' was expected.");
    }
}
