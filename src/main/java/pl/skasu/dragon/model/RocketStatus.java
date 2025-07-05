package pl.skasu.dragon.model;

/**
 * Represents the possible operational statuses of a rocket. This enum is used to track and manage
 * the current state of a rocket.
 */
public enum RocketStatus {
    /**
     * The rocket is currently on the ground, not actively engaged in a mission.
     */
    ON_GROUND,
    /**
     * The rocket is currently in space and assigned to the mission.
     */
    IN_SPACE,
    /**
     * The rocket is undergoing repairs.
     */
    IN_REPAIR
}
