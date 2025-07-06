package pl.skasu.dragon.exception;

/**
 * Exception thrown when an operation is attempted on a mission that has already ended.
 * This exception is used to indicate that a mission in the 'ENDED' status cannot be modified.
 */
public class MissionEndedException extends Exception {

    /**
     * Constructs a {@code MissionEndedException} with the specified mission name.
     * This exception is thrown when an operation is attempted on a mission that has already ended.
     *
     * @param missionName The name of the mission that cannot be modified because it has 'ENDED' status.
     */
    public MissionEndedException(String missionName) {
        super("Cannot modify mission '" + missionName + "'. It has 'ENDED' status.");
    }
}
