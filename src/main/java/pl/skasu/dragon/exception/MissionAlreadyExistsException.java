package pl.skasu.dragon.exception;

/**
 * Exception thrown when attempting to add a mission that already exists in the system.
 * This exception is used to indicate that a mission with the specified name cannot
 * be created because it is already present in the repository.
 */
public class MissionAlreadyExistsException extends Exception {

    /**
     * Constructs a {@code MissionAlreadyExistsException} with the specified mission name.
     * This exception is thrown when attempting to add a mission that already exists in the system.
     *
     * @param missionName The name of the mission that already exists.
     */
    public MissionAlreadyExistsException(String missionName) {
        super("Mission '" + missionName + "' already exists.");
    }
}
