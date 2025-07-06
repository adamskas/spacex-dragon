package pl.skasu.dragon.exception;

/**
 * Exception thrown when attempting to add a rocket that already exists in the system.
 * This exception is used to indicate that a rocket with the specified name cannot
 * be created because it is already present in the repository.
 */
public class RocketAlreadyExistsException extends Exception {

    /**
     * Constructs a {@code RocketAlreadyExistsException} with the specified rocket name.
     * This exception is thrown when attempting to add a rocket that already exists in the system.
     *
     * @param rocketName The name of the rocket that already exists.
     */
    public RocketAlreadyExistsException(String rocketName) {
        super("Rocket '" + rocketName + "' already exists.");
    }
}
