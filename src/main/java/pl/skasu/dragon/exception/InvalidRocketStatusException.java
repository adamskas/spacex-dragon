package pl.skasu.dragon.exception;

/**
 * Exception thrown when a rocket's status does not meet the required criteria for an operation.
 * This exception is used to indicate that the current status of a rocket is invalid
 * for the intended operation or process.
 */
public class InvalidRocketStatusException extends Exception {

    /**
     * Constructs an {@code InvalidRocketStatusException} with the specified rocket name,
     * current status, and required status. This exception is thrown when a rocket's status
     * does not match the required status for the intended operation.
     *
     * @param rocketName     The name of the rocket for which the status is invalid.
     * @param currentStatus  The current status of the rocket.
     * @param requiredStatus The status that is required but not met.
     */
    public InvalidRocketStatusException(String rocketName, String currentStatus, String requiredStatus) {
        super("Rocket '" + rocketName + "' has status '" + currentStatus + "', but '" + requiredStatus + "' was expected.");
    }
}
