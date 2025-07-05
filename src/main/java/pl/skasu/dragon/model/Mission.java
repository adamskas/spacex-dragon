package pl.skasu.dragon.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import pl.skasu.dragon.exception.RocketNotPartOfMissionException;

/**
 * Represents a mission within the dragon project, encapsulating its name, current status, and the
 * set of rockets assigned to it. A mission progresses through various statuses and can have rockets
 * assigned or unassigned, with certain restrictions based on its current status.
 */
public class Mission {

    /**
     * The name of the mission. This is a unique, immutable identifier for the mission that cannot
     * be null.
     */
    private final String name;
    private MissionStatus status;
    private final Set<Rocket> assignedRockets;

    /**
     * Constructs a new Mission with a specified name. The mission's status is initialized to
     * {@code MissionStatus.SCHEDULED}, and it starts with no rockets assigned.
     *
     * @param name The name of the mission. Cannot be null.
     * @throws NullPointerException if the provided name is null.
     */
    public Mission(String name) {
        this.name = Objects.requireNonNull(name, "Mission name cannot be null");
        this.status = MissionStatus.SCHEDULED;
        this.assignedRockets = new HashSet<>();
    }

    /**
     * Returns the name of this mission.
     *
     * @return The name of the mission.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the current status of this mission.
     *
     * @return The current {@code MissionStatus} of the mission.
     */
    public MissionStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of this mission.
     *
     * @param status The new {@code MissionStatus} to set for the mission. Cannot be null.
     * @throws NullPointerException if the provided status is null.
     */
    public void setStatus(MissionStatus status) {
        this.status = Objects.requireNonNull(status, "Mission status cannot be null");
    }

    /**
     * Returns an unmodifiable set of rockets currently assigned to this mission. Any attempts to
     * modify the returned set will result in an {@code UnsupportedOperationException}.
     *
     * @return An unmodifiable {@code Set} of {@code Rocket} objects assigned to this mission.
     */
    public Set<Rocket> getAssignedRockets() {
        return Collections.unmodifiableSet(assignedRockets);
    }

    /**
     * Assigns a rocket to this mission. A rocket cannot be assigned if the mission's status is
     * {@code MissionStatus.ENDED}.
     *
     * @param rocket The {@code Rocket} to be assigned. Cannot be null.
     * @throws NullPointerException  if the provided rocket is null.
     * @throws IllegalStateException if an attempt is made to assign a rocket to a mission that has
     *                               ended.
     */
    public void addRocket(Rocket rocket) {
        Objects.requireNonNull(rocket, "Rocket cannot be null");

        if (status == MissionStatus.ENDED) {
            throw new IllegalStateException("Cannot assign rockets to an 'ENDED' mission.");
        }

        rocket.assignToMission(this);

        this.assignedRockets.add(rocket);
    }

    /**
     * Removes a rocket from the mission. If the rocket is not part of the mission, an
     * exception is thrown. Once removed, the rocket will no longer be associated with this
     * mission.
     *
     * @param rocket The {@code Rocket} to be removed from the mission. Must not be null.
     * @throws NullPointerException if the provided rocket is null.
     * @throws RocketNotPartOfMissionException if the specified rocket is not currently
     *         assigned to this mission.
     */
    public void removeRocket(Rocket rocket) {
        Objects.requireNonNull(rocket, "Rocket cannot be null");
        if(!this.assignedRockets.contains(rocket)) {
            throw new RocketNotPartOfMissionException(rocket.getName(), name);
        }

        this.assignedRockets.remove(rocket);
        rocket.removeFromMission();
    }

    /**
     * Checks if a specific rocket is assigned to this mission.
     *
     * @param rocket The {@code Rocket} to check for assignment.
     * @return {@code true} if the rocket is assigned to this mission, {@code false} otherwise.
     */
    public boolean hasRocket(Rocket rocket) {
        return assignedRockets.contains(rocket);
    }

    /**
     * Returns the number of rockets currently assigned to this mission.
     *
     * @return The count of assigned rockets.
     */
    public int getNumberOfAssignedRockets() {
        return assignedRockets.size();
    }

    /**
     * Compares this mission to the specified object. The result is true if and only if the argument
     * is not null and is a {@code Mission} object that has the same name as this object.
     *
     * @param o The object to compare with.
     * @return {@code true} if the given object represents a {@code Mission} equivalent to this
     * mission, {@code false} otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Mission mission = (Mission) o;
        return Objects.equals(name, mission.name);
    }

    /**
     * Returns a hash code for this mission. The hash code is based on the mission's name.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns a string representation of this mission. The string includes the mission name, its
     * status, and the number of assigned rockets.
     *
     * @return A string representation of the mission.
     */
    @Override
    public String toString() {
        return name + " - " + status + " - Dragons:" + assignedRockets.size();
    }
}