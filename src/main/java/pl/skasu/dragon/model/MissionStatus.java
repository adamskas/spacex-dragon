package pl.skasu.dragon.model;

/**
 * Represents the possible statuses of a mission. This enum is used to track and manage the current
 * state of a mission.
 */
public enum MissionStatus {
    /**
     * The mission has been planned and scheduled but has not yet started.
     */
    SCHEDULED,
    /**
     * The mission is awaiting repair of at least one assigned rocket.
     */
    PENDING,
    /**
     * The mission is currently active and underway.
     */
    IN_PROGRESS,
    /**
     * The mission has concluded.
     */
    ENDED
}
