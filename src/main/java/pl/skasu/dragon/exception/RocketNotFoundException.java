package pl.skasu.dragon.exception;

/**
 * Exception thrown when a rocket with the specified name cannot be found in the system.
 * This exception is used to indicate that the requested rocket does not exist.
 */
public class RocketNotFoundException extends Exception {

    /**
     * Constructs a {@code RocketNotFoundException} with the specified rocket name.
     *
     * @param rocketName The name of the rocket that could not be found.
     */
    public RocketNotFoundException(String rocketName) {
        super("Rocket '" + rocketName + "' not found.");
    }
}
