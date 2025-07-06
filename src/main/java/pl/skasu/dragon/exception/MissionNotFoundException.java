package pl.skasu.dragon.exception;

/**
 * Exception thrown when a mission with the specified name cannot be found in the system.
 * This exception is used to indicate that the requested mission does not exist.
 */
public class MissionNotFoundException extends Exception {

    /**
     * Constructs a {@code MissionNotFoundException} with the specified mission name.
     *
     * @param missionName The name of the mission that could not be found.
     */
    public MissionNotFoundException(String missionName) {
        super("Mission '" + missionName + "' not found.");
    }
}
