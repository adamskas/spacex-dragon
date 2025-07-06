package pl.skasu.dragon.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import org.junit.jupiter.api.Test;
import pl.skasu.dragon.exception.MissionEndedException;
import pl.skasu.dragon.exception.RocketAlreadyAssignedException;
import pl.skasu.dragon.exception.RocketNotPartOfMissionException;

class MissionTest {

    @Test
    void constructor_shouldSetNameAndInitialStatus() {
        Mission mission = new Mission("Moon Landing");
        assertEquals("Moon Landing", mission.getName());
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertTrue(mission.getAssignedRockets().isEmpty());
    }

    @Test
    void constructor_shouldThrowNullPointerException_forNullName() {
        assertThrows(NullPointerException.class, () -> new Mission(null));
    }

    @Test
    void getName_shouldReturnCorrectName() {
        Mission mission = new Mission("Mars Odyssey");
        assertEquals("Mars Odyssey", mission.getName());
    }

    @Test
    void getStatus_shouldReturnCurrentStatus() {
        Mission mission = new Mission("Jupiter Flyby");
        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        mission.setStatus(MissionStatus.IN_PROGRESS);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
    }

    @Test
    void setStatus_shouldUpdateStatus() {
        Mission mission = new Mission("Saturn Probe");
        mission.setStatus(MissionStatus.ENDED);
        assertEquals(MissionStatus.ENDED, mission.getStatus());
    }

    @Test
    void setStatus_shouldThrowNullPointerException_forNullStatus() {
        Mission mission = new Mission("Venus Descent");
        assertThrows(NullPointerException.class, () -> mission.setStatus(null));
    }

    @Test
    void reevaluateStatus_shouldNotChangeStatus_whenMissionIsEnded() {
        Mission mission = new Mission("Endurance");
        mission.setStatus(MissionStatus.ENDED);

        mission.reevaluateStatus();

        assertEquals(MissionStatus.ENDED, mission.getStatus());
    }

    @Test
    void reevaluateStatus_shouldSetStatusToScheduled_whenNoRocketsAssigned() {
        Mission mission = new Mission("Discovery");
        mission.setStatus(MissionStatus.IN_PROGRESS);

        mission.reevaluateStatus();

        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
    }

    @Test
    void reevaluateStatus_shouldSetStatusToPending_whenRocketIsInRepair() throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Odyssey");
        Rocket rocket = new Rocket("Ares");
        rocket.setStatus(RocketStatus.IN_REPAIR);

        mission.assignRocket(rocket);

        assertEquals(MissionStatus.PENDING, mission.getStatus());
    }

    @Test
    void reevaluateStatus_shouldSetStatusToInProgress_whenRocketsAreReady() throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Voyager");
        Rocket rocket = new Rocket("Titan IIIE");

        mission.assignRocket(rocket);

        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }


    @Test
    void getAssignedRockets_shouldReturnUnmodifiableSet() {
        Mission mission = new Mission("Deep Space");
        Set<Rocket> rockets = mission.getAssignedRockets();
        Rocket rocket = new Rocket("Test");
        assertThrows(UnsupportedOperationException.class, () -> rockets.add(rocket));
    }

    @Test
    void canAssignRocket_shouldThrowNPE_whenRocketIsNull() {
        Mission mission = new Mission("Test Mission");
        assertThrows(NullPointerException.class, () -> mission.canAssignRocket(null));
    }

    @Test
    void canAssignRocket_shouldThrowMissionEndedException_whenMissionIsEnded() {
        Mission mission = new Mission("Test Mission");
        mission.setStatus(MissionStatus.ENDED);
        Rocket rocket = new Rocket("Test Rocket");
        assertThrows(MissionEndedException.class, () -> mission.canAssignRocket(rocket));
    }

    @Test
    void canAssignRocket_shouldNotThrow_whenRocketIsUnassignedAndMissionIsActive() {
        Mission mission = new Mission("Test Mission");
        mission.setStatus(MissionStatus.SCHEDULED);
        Rocket rocket = new Rocket("Test Rocket");
        assertDoesNotThrow(() -> mission.canAssignRocket(rocket));
    }

    @Test
    void canAssignRocket_shouldNotThrow_whenRocketIsAssignedToTheSameMission() throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Test Mission");
        Rocket rocket = new Rocket("Test Rocket");
        mission.assignRocket(rocket);
        assertDoesNotThrow(() -> mission.canAssignRocket(rocket));
    }

    @Test
    void canAssignRocket_shouldThrowRocketAlreadyAssignedException_whenRocketAssignedToDifferentMission() throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission1 = new Mission("Mission 1");
        Mission mission2 = new Mission("Mission 2");
        Rocket rocket = new Rocket("Test Rocket");
        mission1.assignRocket(rocket);

        assertThrows(RocketAlreadyAssignedException.class, () -> mission2.canAssignRocket(rocket));
    }


    @Test
    void assignRocket_shouldAssignRocketToMissionAndAssignToList()
        throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Orbital Station");
        Rocket rocket = new Rocket("Station Builder");
        mission.assignRocket(rocket);
        assertTrue(mission.hasRocket(rocket));
        assertEquals(1, mission.getNumberOfAssignedRockets());
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    void assignRocket_shouldThrowNullPointerException_whenNullRocketIsProvided() {
        Mission mission = new Mission("Null Rocket Mission");
        assertThrows(NullPointerException.class, () -> mission.assignRocket(null));
    }

    @Test
    void assignRocket_shouldThrowMissionEndedException_whenMissionAlreadyEnded() {
        Mission mission = new Mission("Failed Mission");
        mission.setStatus(MissionStatus.ENDED);
        Rocket rocket = new Rocket("Rescue Rocket");
        assertThrows(MissionEndedException.class, () -> mission.assignRocket(rocket));
    }

    @Test
    void removeRocket_shouldRemoveRocketFromMissionAndUpdateRocketStatus()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Rescue Mission");
        Rocket rocket = new Rocket("Rescue 1");
        mission.assignRocket(rocket);

        assertTrue(mission.hasRocket(rocket));
        assertEquals(1, mission.getNumberOfAssignedRockets());
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());

        mission.removeRocket(rocket);

        assertFalse(mission.hasRocket(rocket));
        assertEquals(0, mission.getNumberOfAssignedRockets());
        assertNull(rocket.getAssignedMission());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
    }

    @Test
    void removeRocket_shouldThrowNullPointerException_whenNullRocketIsProvided() {
        Mission mission = new Mission("Empty Mission");
        assertThrows(NullPointerException.class, () -> mission.removeRocket(null));
    }

    @Test
    void removeRocket_shouldThrowRocketNotPartOfMissionException_whenRocketIsUnassigned() {
        Mission mission = new Mission("Exploration Mission");
        Rocket rocket = new Rocket("Explorer");
        assertThrows(RocketNotPartOfMissionException.class, () -> mission.removeRocket(rocket));
    }


    @Test
    void hasRocket_shouldReturnTrue_forAssignedRocket()
        throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Monitoring Mission");
        Rocket rocket = new Rocket("Probe");
        mission.assignRocket(rocket);
        assertTrue(mission.hasRocket(rocket));
    }

    @Test
    void hasRocket_shouldReturnFalse_forNonAssignedRocket() {
        Mission mission = new Mission("Recon Mission");
        Rocket rocket = new Rocket("Scout");
        assertFalse(mission.hasRocket(rocket));
    }

    @Test
    void removeRocket_shouldSetStatusToScheduled_whenLastRocketRemoved()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Last Rocket Mission");
        Rocket rocket = new Rocket("Last Rocket");

        mission.assignRocket(rocket);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(1, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket);

        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertEquals(0, mission.getNumberOfAssignedRockets());
        assertNull(rocket.getAssignedMission());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
    }

    @Test
    void removeRocket_shouldSetStatusToInProgress_whenOtherRocketsRemain()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Multi-Rocket Mission");
        Rocket rocket1 = new Rocket("Rocket One");
        Rocket rocket2 = new Rocket("Rocket Two");

        mission.assignRocket(rocket1);
        mission.assignRocket(rocket2);
        assertEquals(2, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket1);

        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(1, mission.getNumberOfAssignedRockets());
        assertTrue(mission.hasRocket(rocket2));
        assertFalse(mission.hasRocket(rocket1));
    }

    @Test
    void removeRocket_shouldSetStatusToInProgress_whenOnlyInSpaceRocketRemains()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Mission");
        Rocket rocket1 = new Rocket("Rocket1");
        Rocket rocket2 = new Rocket("Rocket2");
        rocket2.setStatus(RocketStatus.IN_REPAIR);

        mission.assignRocket(rocket1);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        mission.assignRocket(rocket2);
        assertEquals(MissionStatus.PENDING, mission.getStatus());
        assertEquals(2, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket2);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(1, mission.getNumberOfAssignedRockets());
        assertTrue(mission.hasRocket(rocket1));
    }

    @Test
    void getNumberOfAssignedRockets_shouldReturnCorrectCount()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Colonization");
        assertEquals(0, mission.getNumberOfAssignedRockets());

        Rocket rocket1 = new Rocket("Colony Ship 1");
        mission.assignRocket(rocket1);
        assertEquals(1, mission.getNumberOfAssignedRockets());

        Rocket rocket2 = new Rocket("Colony Ship 2");
        mission.assignRocket(rocket2);
        assertEquals(2, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket1);
        assertEquals(1, mission.getNumberOfAssignedRockets());
    }

    @Test
    void equals_shouldReturnTrue_forSameName() {
        Mission mission1 = new Mission("Mission Alpha");
        Mission mission2 = new Mission("Mission Alpha");
        assertEquals(mission1, mission2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentName() {
        Mission mission1 = new Mission("Mission Alpha");
        Mission mission2 = new Mission("Mission Beta");
        assertNotEquals(mission1, mission2);
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        Mission mission = new Mission("Mission Gamma");
        assertNotEquals(null, mission);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentClass() {
        Mission mission = new Mission("Mission Delta");
        Object obj = new Object();
        assertNotEquals(mission, obj);
    }

    @Test
    void hashCode_shouldBeConsistentWithEquals() {
        Mission mission1 = new Mission("Mission Epsilon");
        Mission mission2 = new Mission("Mission Epsilon");
        Mission mission3 = new Mission("Mission Zeta");

        assertEquals(mission1.hashCode(), mission2.hashCode());
        assertNotEquals(mission1.hashCode(), mission3.hashCode());
    }

    @Test
    void toString_shouldReturnCorrectFormat()
        throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Solar Survey");
        assertEquals("Solar Survey - SCHEDULED - Dragons: 0", mission.toString());

        mission.assignRocket(new Rocket("Survey Probe 1"));
        assertEquals("Solar Survey - IN_PROGRESS - Dragons: 1", mission.toString());

        mission.setStatus(MissionStatus.ENDED);
        assertEquals("Solar Survey - ENDED - Dragons: 1", mission.toString());
    }
}