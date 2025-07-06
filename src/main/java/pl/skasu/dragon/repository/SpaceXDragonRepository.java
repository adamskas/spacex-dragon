package pl.skasu.dragon.repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import pl.skasu.dragon.exception.InvalidMissionStatusException;
import pl.skasu.dragon.exception.InvalidRocketStatusException;
import pl.skasu.dragon.exception.MissionAlreadyExistsException;
import pl.skasu.dragon.exception.MissionEndedException;
import pl.skasu.dragon.exception.MissionNotFoundException;
import pl.skasu.dragon.exception.RocketAlreadyAssignedException;
import pl.skasu.dragon.exception.RocketAlreadyExistsException;
import pl.skasu.dragon.exception.RocketNotFoundException;
import pl.skasu.dragon.model.Mission;
import pl.skasu.dragon.model.MissionStatus;
import pl.skasu.dragon.model.Rocket;

/**
 * SpaceXDragonRepository is responsible for managing and maintaining a repository of rockets and
 * missions. It provides functionality to add new rockets or missions, retrieve and manipulate their
 * state, and generate a summary of existing rocket and mission data.
 */
public class SpaceXDragonRepository {

    private final Map<String, Rocket> rockets;
    private final Map<String, Mission> missions;

    public SpaceXDragonRepository() {
        this.rockets = new HashMap<>();
        this.missions = new HashMap<>();
    }

    /**
     * Retrieves a rocket by its name.
     *
     * @param name The name of the rocket.
     * @return The Rocket object.
     * @throws RocketNotFoundException If no rocket with the given name is found.
     */
    private Rocket getRocket(String name) throws RocketNotFoundException {
        return Optional.ofNullable(rockets.get(name))
            .orElseThrow(() -> new RocketNotFoundException(name));
    }

    /**
     * Retrieves a mission by its name.
     *
     * @param name The name of the mission.
     * @return The Mission object.
     * @throws MissionNotFoundException If no mission with the given name is found.
     */
    private Mission getMission(String name) throws MissionNotFoundException {
        return Optional.ofNullable(missions.get(name))
            .orElseThrow(() -> new MissionNotFoundException(name));
    }

    /**
     * Adds a new rocket to the repository.
     *
     * @param name The name of the rocket.
     * @return Name of the newly created Rocket.
     */
    public String addRocket(String name) throws RocketAlreadyExistsException {
        if (rockets.containsKey(name)) {
            throw new RocketAlreadyExistsException(name);
        }

        rockets.put(name, new Rocket(name));
        return name;
    }

    /**
     * Puts the specified rocket into repair status. If the rocket is assigned to a mission, the
     * mission's status is updated to "PENDING."
     *
     * @param rocketName The name of the rocket to put into repair. Must not be null.
     * @throws RocketNotFoundException If no rocket with the given name is found.
     */
    public void putRocketIntoRepair(String rocketName) throws RocketNotFoundException {
        Rocket rocket = getRocket(rocketName);
        rocket.putInRepair();
    }

    /**
     * Completes the repair process for the specified rocket. This method marks the repair process
     * of a rocket as complete, provided the rocket exists in the system and its current status
     * allows for the operation.
     *
     * @param rocketName The name of the rocket whose repair is to be completed. Must not be null.
     * @throws RocketNotFoundException      If no rocket with the specified name exists in the
     *                                      repository.
     * @throws InvalidRocketStatusException If the rocket is not in a status that allows the
     *                                      completion of repair.
     */
    public void completeRepairOfRocket(String rocketName)
        throws RocketNotFoundException, InvalidRocketStatusException {
        Rocket rocket = getRocket(rocketName);
        rocket.completeRepair();
    }

    /**
     * Adds a new mission to the repository.
     *
     * @param name The name of the mission.
     * @return Name of the newly created Mission.
     */
    public String addMission(String name) throws MissionAlreadyExistsException {
        if (missions.containsKey(name)) {
            throw new MissionAlreadyExistsException(name);
        }

        missions.put(name, new Mission(name));
        return name;
    }

    /**
     * Assigns a rocket to a mission based on their respective names. This method associates a
     * specified rocket with a specified mission, provided that the rocket and mission exist, and
     * the assignment does not conflict with the current state or configuration of the rocket or
     * mission.
     *
     * @param rocketName  The name of the rocket to be assigned.
     * @param missionName The name of the mission to which the rocket is assigned.
     * @throws RocketNotFoundException        If no rocket with the specified name exists.
     * @throws MissionNotFoundException       If no mission with the specified name exists.
     * @throws RocketAlreadyAssignedException If the rocket is already assigned to a mission.
     * @throws MissionEndedException          If the mission has already ended and modifications are
     *                                        not allowed.
     */
    public void assignRocketToMission(String rocketName, String missionName)
        throws RocketNotFoundException, MissionNotFoundException, RocketAlreadyAssignedException, MissionEndedException {
        Rocket rocket = getRocket(rocketName);
        Mission mission = getMission(missionName);

        mission.assignRocket(rocket);
    }

    /**
     * Assigns multiple rockets to a specified mission based on their respective names. This method
     * associates all specified rockets with a specified mission, provided that the rockets and mission
     * exist, and the assignment does not conflict with the current state or configuration of the rockets
     * or mission.
     *
     * @param rocketNames  A list of rocket names to be assigned to the mission.
     * @param missionName  The name of the mission to which the rockets are assigned.
     * @throws RocketNotFoundException        If a rocket with any of the specified names does not exist.
     * @throws MissionNotFoundException       If the specified mission does not exist.
     * @throws RocketAlreadyAssignedException If any of the rockets is already assigned to another mission.
     * @throws MissionEndedException          If the mission has already ended and modifications are not allowed.
     */
    public void assignRocketsToMission(List<String> rocketNames, String missionName)
        throws RocketNotFoundException, MissionNotFoundException, RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = getMission(missionName);

        if (mission.getStatus() == MissionStatus.ENDED) {
            throw new MissionEndedException(mission.getName());
        }

        List<Rocket> rocketsToAssign = new ArrayList<>();
        for (String rocketName : rocketNames) {
            Rocket rocket = getRocket(rocketName);
            rocketsToAssign.add(rocket);
            mission.canAssignRocket(rocket);
        }

        for (Rocket rocket : rocketsToAssign) {
            mission.assignRocket(rocket);
        }
    }

    /**
     * Ends the mission with the specified name by marking it as complete.
     *
     * @param name The name of the mission to be ended. Must not be null or empty.
     * @throws MissionNotFoundException      If no mission with the specified name exists.
     * @throws InvalidMissionStatusException If the mission is in a status that does not allow it to
     *                                       be ended.
     */
    public void endMission(String name)
        throws MissionNotFoundException, InvalidMissionStatusException {
        Mission mission = getMission(name);
        mission.complete();
    }

    /**
     * Generates a summary of all missions and rockets in the repository.
     * <p>
     * The summary includes: - All missions and the rockets assigned to each mission.
     *
     * @return A string representation of the missions with their assigned rockets.
     */
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        missions.values().stream()
            .sorted(Comparator.comparingInt(Mission::getNumberOfAssignedRockets).reversed()
                .thenComparing(Mission::getName, Comparator.reverseOrder()))
            .forEach(mission -> {
                sb.append("- ").append(mission).append("\n");
                mission.getAssignedRockets()
                    .stream().sorted(Comparator.comparing(Rocket::getName))
                    .forEach(r -> sb.append("\t- ").append(r).append("\n"));
            });

        return sb.toString();
    }
}
