package pl.skasu.dragon.repository;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.skasu.dragon.exception.InvalidMissionStatusException;
import pl.skasu.dragon.exception.InvalidRocketStatusException;
import pl.skasu.dragon.exception.MissionAlreadyExistsException;
import pl.skasu.dragon.exception.MissionEndedException;
import pl.skasu.dragon.exception.MissionNotFoundException;
import pl.skasu.dragon.exception.RocketAlreadyAssignedException;
import pl.skasu.dragon.exception.RocketAlreadyExistsException;
import pl.skasu.dragon.exception.RocketNotFoundException;

class SpaceXDragonRepositoryTest {

    private SpaceXDragonRepository repository;

    @BeforeEach
    void setUp() {
        repository = new SpaceXDragonRepository();
    }

    @Test
    void addRocket_shouldAddRocket() throws RocketAlreadyExistsException {
        String rocketName = repository.addRocket("Falcon 9");

        assertEquals("Falcon 9", rocketName);
    }

    @Test
    void addRocket_shouldThrowException_whenRocketAlreadyExists()
        throws RocketAlreadyExistsException {
        repository.addRocket("Falcon 9");

        RocketAlreadyExistsException exception = assertThrows(RocketAlreadyExistsException.class,
            () -> {
                repository.addRocket("Falcon 9");
            });

        assertEquals("Rocket 'Falcon 9' already exists.", exception.getMessage());
    }

    @Test
    void assignRocketToMission_shouldAssignRocket() throws Exception {
        repository.addRocket("Falcon 9");
        repository.addMission("Starlink-1");

        assertDoesNotThrow(() -> repository.assignRocketToMission("Falcon 9", "Starlink-1"));
    }

    @Test
    void assignRocketToMission_shouldThrowException_whenRocketNotFound()
        throws MissionAlreadyExistsException {
        repository.addMission("Starlink-1");

        RocketNotFoundException exception = assertThrows(RocketNotFoundException.class, () -> {
            repository.assignRocketToMission("Falcon 9", "Starlink-1");
        });

        assertEquals("Rocket 'Falcon 9' not found.", exception.getMessage());
    }

    @Test
    void assignRocketToMission_shouldThrowException_whenMissionNotFound()
        throws RocketAlreadyExistsException {
        repository.addRocket("Falcon 9");

        MissionNotFoundException exception = assertThrows(MissionNotFoundException.class, () -> {
            repository.assignRocketToMission("Falcon 9", "Starlink-1");
        });

        assertEquals("Mission 'Starlink-1' not found.", exception.getMessage());
    }

    @Test
    void shouldAssignRocketsToMission()
        throws RocketAlreadyExistsException, MissionAlreadyExistsException, RocketNotFoundException, MissionNotFoundException, RocketAlreadyAssignedException, MissionEndedException {
        String missionName = "Starlink-1";
        repository.addMission(missionName);

        String rocketName1 = "Falcon 9";
        String rocketName2 = "Falcon Heavy";
        repository.addRocket(rocketName1);
        repository.addRocket(rocketName2);

        assertDoesNotThrow(
            () -> repository.assignRocketsToMission(List.of(rocketName1, rocketName2),
                missionName));
    }

    @Test
    void assignRocketsToMission_shouldThrowMissionNotFoundException() {
        assertThrows(MissionNotFoundException.class, () -> {
            repository.assignRocketsToMission(List.of("Falcon 9"), "Non-existent Mission");
        });
    }

    @Test
    void assignRocketsToMission_shouldThrowRocketNotFoundException()
        throws MissionAlreadyExistsException {
        String missionName = "Starlink-1";
        repository.addMission(missionName);

        assertThrows(RocketNotFoundException.class, () -> {
            repository.assignRocketsToMission(List.of("Non-existent Rocket"), missionName);
        });
    }

    @Test
    void assignRocketsToMission_shouldThrowMissionEndedException()
        throws MissionAlreadyExistsException, RocketAlreadyExistsException, MissionNotFoundException, RocketAlreadyAssignedException, MissionEndedException, InvalidMissionStatusException, RocketNotFoundException {
        String missionName = "Starlink-1";
        repository.addMission(missionName);

        String rocketName = "Falcon 9";
        repository.addRocket(rocketName);

        repository.assignRocketToMission(rocketName, missionName);
        repository.endMission(missionName);

        assertThrows(MissionEndedException.class, () -> {
            repository.assignRocketsToMission(List.of(rocketName), missionName);
        });
    }

    @Test
    void assignRocketsToMission_shouldThrowRocketAlreadyAssignedException()
        throws MissionAlreadyExistsException, RocketAlreadyExistsException, MissionNotFoundException, RocketAlreadyAssignedException, MissionEndedException, RocketNotFoundException {
        String missionName1 = "Starlink-1";
        String missionName2 = "Starlink-2";
        repository.addMission(missionName1);
        repository.addMission(missionName2);

        String rocketName = "Falcon 9";
        repository.addRocket(rocketName);

        repository.assignRocketToMission(rocketName, missionName1);

        assertThrows(RocketAlreadyAssignedException.class, () -> {
            repository.assignRocketsToMission(List.of(rocketName), missionName2);
        });
    }

    @Test
    void assignRocketsToMission_shouldNotThrowExceptionForEmptyRocketList()
        throws MissionAlreadyExistsException {
        String missionName = "Starlink-1";
        repository.addMission(missionName);

        assertDoesNotThrow(() -> {
            repository.assignRocketsToMission(List.of(), missionName);
        });
    }


    @Test
    void putRocketIntoRepair_shouldPutRocketInRepair() throws Exception {
        repository.addRocket("Falcon 9");

        assertDoesNotThrow(() -> repository.putRocketIntoRepair("Falcon 9"));
    }

    @Test
    void putRocketIntoRepair_shouldThrowException_whenRocketNotFound() {
        RocketNotFoundException exception = assertThrows(RocketNotFoundException.class, () -> {
            repository.putRocketIntoRepair("Falcon 9");
        });

        assertEquals("Rocket 'Falcon 9' not found.", exception.getMessage());
    }

    @Test
    void completeRepairOfRocket_shouldCompleteRepair() throws Exception {
        repository.addRocket("Falcon 9");
        repository.putRocketIntoRepair("Falcon 9");

        assertDoesNotThrow(() -> repository.completeRepairOfRocket("Falcon 9"));
    }

    @Test
    void completeRepairOfRocket_shouldThrowException_whenRocketNotFound() {
        RocketNotFoundException exception = assertThrows(RocketNotFoundException.class, () -> {
            repository.completeRepairOfRocket("Falcon 9");
        });

        assertEquals("Rocket 'Falcon 9' not found.", exception.getMessage());
    }

    @Test
    void completeRepairOfRocket_shouldThrowException_whenRocketNotInRepair()
        throws RocketAlreadyExistsException {
        repository.addRocket("Falcon 9");

        assertThrows(InvalidRocketStatusException.class, () -> {
            repository.completeRepairOfRocket("Falcon 9");
        });
    }


    @Test
    void addMission_shouldAddMission() throws MissionAlreadyExistsException {
        String missionName = repository.addMission("Starlink-1");

        assertEquals("Starlink-1", missionName);
    }

    @Test
    void addMission_shouldThrowException_whenMissionAlreadyExists()
        throws MissionAlreadyExistsException {
        repository.addMission("Starlink-1");

        MissionAlreadyExistsException exception = assertThrows(MissionAlreadyExistsException.class,
            () -> {
                repository.addMission("Starlink-1");
            });

        assertEquals("Mission 'Starlink-1' already exists.", exception.getMessage());
    }

    @Test
    void endMission_shouldEndMission() throws Exception {
        repository.addMission("Starlink-1");
        repository.addRocket("Falcon 9");
        repository.assignRocketToMission("Falcon 9", "Starlink-1");

        assertDoesNotThrow(() -> repository.endMission("Starlink-1"));
    }

    @Test
    void endMission_shouldThrowException_whenMissionNotFound() {
        MissionNotFoundException exception = assertThrows(MissionNotFoundException.class, () -> {
            repository.endMission("Starlink-1");
        });

        assertEquals("Mission 'Starlink-1' not found.", exception.getMessage());
    }

    @Test
    void endMission_shouldThrowException_whenMissionHasNotYetStarted() throws Exception {
        repository.addMission("Starlink-1");

        InvalidMissionStatusException exception = assertThrows(InvalidMissionStatusException.class,
            () -> {
                repository.endMission("Starlink-1");
            });

        assertEquals("Mission 'Starlink-1' has status 'SCHEDULED', but 'IN_PROGRESS' was expected.",
            exception.getMessage());
    }

    @Test
    void endMission_shouldThrowException_whenMissionAlreadyEnded() throws Exception {
        repository.addMission("Starlink-1");
        repository.addRocket("Falcon 9");
        repository.assignRocketToMission("Falcon 9", "Starlink-1");
        repository.endMission("Starlink-1");

        InvalidMissionStatusException exception = assertThrows(InvalidMissionStatusException.class,
            () -> {
                repository.endMission("Starlink-1");
            });

        assertEquals("Mission 'Starlink-1' has status 'ENDED', but 'IN_PROGRESS' was expected.",
            exception.getMessage());
    }

    @Test
    void getSummary_shouldReturnCorrectSummary() throws Exception {
        repository.addMission("Mission B");
        repository.addMission("Mission A");
        repository.addRocket("Rocket 1");
        repository.addRocket("Rocket 2");
        repository.addRocket("Rocket 3");

        repository.assignRocketToMission("Rocket 1", "Mission A");
        repository.assignRocketToMission("Rocket 2", "Mission B");
        repository.assignRocketToMission("Rocket 3", "Mission B");

        String summary = repository.getSummary();

        String expectedSummary1 = """
            - Mission B - IN_PROGRESS - Dragons: 2
            \t- Rocket 3 - IN_SPACE
            \t- Rocket 2 - IN_SPACE
            - Mission A - IN_PROGRESS - Dragons: 1
            \t- Rocket 1 - IN_SPACE
            """;

        String expectedSummary2 = """
            - Mission B - IN_PROGRESS - Dragons: 2
            \t- Rocket 2 - IN_SPACE
            \t- Rocket 3 - IN_SPACE
            - Mission A - IN_PROGRESS - Dragons: 1
            \t- Rocket 1 - IN_SPACE
            """;

        assertTrue(expectedSummary1.equals(summary) || expectedSummary2.equals(summary));
    }

    @Test
    void getSummary_shouldReturnEmptyString_whenNoMissions() {
        String summary = repository.getSummary();

        assertTrue(summary.isEmpty());
    }
}