package pl.skasu.dragon.model;

import java.util.Objects;
import pl.skasu.dragon.exception.InvalidRocketStatusException;
import pl.skasu.dragon.exception.RocketAlreadyAssignedException;

/**
 * Represents a rocket entity within the dragon mission control system. A rocket has a name, a
 * current status, and can be assigned to a mission.
 */
public class Rocket {

    /**
     * The name of the rocket. This is a unique, immutable identifier for the rocket that cannot be
     * null.
     */
    private final String name;
    private RocketStatus status;
    private Mission assignedMission;

    /**
     * Constructs a new Rocket instance with the specified name. Initially, the rocket is
     * {@code ON_GROUND} and not assigned to any mission.
     *
     * @param name The name of the rocket. Must not be null.
     * @throws NullPointerException if the provided name is null.
     */
    public Rocket(String name) {
        this.name = Objects.requireNonNull(name, "Rocket name cannot be null");
        this.status = RocketStatus.ON_GROUND;
        this.assignedMission = null;
    }

    /**
     * Returns the name of the rocket.
     *
     * @return The name of the rocket.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current status of the rocket.
     *
     * @return The current {@link RocketStatus} of the rocket.
     */
    public RocketStatus getStatus() {
        return status;
    }

    public void putInRepair() {
        this.status = RocketStatus.IN_REPAIR;

        if(assignedMission != null) {
            assignedMission.setStatus(MissionStatus.PENDING);
        }
    }

    public void completeRepair() throws InvalidRocketStatusException {
        if(status != RocketStatus.IN_REPAIR) {
            throw new InvalidRocketStatusException(name, status.toString(), RocketStatus.IN_REPAIR.toString());
        }

        if(assignedMission != null) {
            this.status = RocketStatus.IN_SPACE;
            this.assignedMission.reevaluateStatus();
        } else {
            this.status = RocketStatus.ON_GROUND;
        }

    }

    /**
     * Sets the status of the rocket.
     *
     * @param status The new {@link RocketStatus} for the rocket. Must not be null.
     * @throws NullPointerException if the provided status is null.
     */
    void setStatus(RocketStatus status) {
        this.status = Objects.requireNonNull(status, "Rocket status cannot be null");
    }

    /**
     * Returns the mission currently assigned to the rocket.
     *
     * @return The {@link Mission} assigned to the rocket, or {@code null} if no mission is
     * assigned.
     */
    public Mission getAssignedMission() {
        return assignedMission;
    }

    /**
     * Assigns the given mission to the rocket. If the rocket is already assigned to a mission, a
     * {@code RocketAlreadyAssignedException} is thrown. This method ensures that if the rocket's
     * current status is not {@code IN_REPAIR}, then it updates the rocket's status to
     * {@code IN_SPACE}.
     *
     * @param mission The mission to assign to the rocket. Must not be null.
     * @throws RocketAlreadyAssignedException if the rocket is already assigned to another mission.
     * @throws NullPointerException           if the provided mission is null.
     */
    void assignToMission(Mission mission) throws RocketAlreadyAssignedException {
        if (this.assignedMission != null) {
            throw new RocketAlreadyAssignedException(this.name, this.assignedMission.getName());
        }

        this.assignedMission = Objects.requireNonNull(mission, "Mission cannot be null");

        if (this.status != RocketStatus.IN_REPAIR) {
            this.status = RocketStatus.IN_SPACE;
        }
    }

    /**
     * Removes the rocket from its currently assigned mission and updates its status if necessary.
     * <br>
     * If the rocket is assigned to a mission, the assigned mission reference will be cleared by
     * setting it to {@code null}. Additionally, if the rocket's current status is {@code IN_SPACE},
     * it will be updated to {@code ON_GROUND}.
     * <br>
     * This method assumes that no other actions, such as notifying the mission or ensuring state
     * consistency, are required after the rocket is removed from the mission.
     */
    void removeFromMission() {
        this.assignedMission = null;

        if (this.status == RocketStatus.IN_SPACE) {
            this.status = RocketStatus.ON_GROUND;
        }
    }

    /**
     * Checks if the rocket is currently assigned to a mission.
     *
     * @return {@code true} if a mission is assigned, {@code false} otherwise.
     */
    public boolean isAssigned() {
        return assignedMission != null;
    }

    /**
     * Compares this rocket to the specified object. The result is true if and only if the argument
     * is not null and is a {@code Rocket} object that has the same name as this object.
     *
     * @param o The object to compare with.
     * @return {@code true} if the given object represents a {@code Rocket} equivalent to this
     * rocket, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rocket rocket = (Rocket) o;
        return Objects.equals(name, rocket.name);
    }

    /**
     * Returns a hash code for this rocket. The hash code is based on the rocket's name.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns a string representation of this rocket. The string includes the rocket name and its
     * status.
     *
     * @return A string representation of the rocket.
     */
    @Override
    public String toString() {
        return name + " - " + status;
    }
}