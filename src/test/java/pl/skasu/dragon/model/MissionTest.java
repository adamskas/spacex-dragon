package pl.skasu.dragon.model;

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
    void constructor_shouldThrowNullPointerExceptionForNullName() {
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
    void setStatus_shouldThrowNullPointerExceptionForNullStatus() {
        Mission mission = new Mission("Venus Descent");
        assertThrows(NullPointerException.class, () -> mission.setStatus(null));
    }

    @Test
    void getAssignedRockets_shouldReturnUnmodifiableSet() {
        Mission mission = new Mission("Deep Space");
        Set<Rocket> rockets = mission.getAssignedRockets();
        Rocket rocket = new Rocket("Test");
        assertThrows(UnsupportedOperationException.class, () -> rockets.add(rocket));
    }

    @Test
    void addRocket_shouldAssignRocketToMissionAndAddToList()
        throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Orbital Station");
        Rocket rocket = new Rocket("Station Builder");
        mission.addRocket(rocket);
        assertTrue(mission.hasRocket(rocket));
        assertEquals(1, mission.getNumberOfAssignedRockets());
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    void addRocket_shouldThrowNullPointerExceptionForNullRocket() {
        Mission mission = new Mission("Null Rocket Mission");
        assertThrows(NullPointerException.class, () -> mission.addRocket(null));
    }

    @Test
    void addRocket_shouldThrowMissionEndedExceptionForEndedMission() {
        Mission mission = new Mission("Failed Mission");
        mission.setStatus(MissionStatus.ENDED);
        Rocket rocket = new Rocket("Rescue Rocket");
        assertThrows(MissionEndedException.class, () -> mission.addRocket(rocket));
    }

    @Test
    void removeRocket_shouldRemoveRocketFromMissionAndUpdateRocketStatus()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Rescue Mission");
        Rocket rocket = new Rocket("Rescue 1");
        mission.addRocket(rocket);

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
    void removeRocket_shouldThrowNullPointerExceptionForNullRocket() {
        Mission mission = new Mission("Empty Mission");
        assertThrows(NullPointerException.class, () -> mission.removeRocket(null));
    }

    @Test
    void removeRocket_shouldThrowRocketNotPartOfMissionExceptionForUnassignedRocket() {
        Mission mission = new Mission("Exploration Mission");
        Rocket rocket = new Rocket("Explorer");
        assertThrows(RocketNotPartOfMissionException.class, () -> mission.removeRocket(rocket));
    }


    @Test
    void hasRocket_shouldReturnTrueForAssignedRocket()
        throws RocketAlreadyAssignedException, MissionEndedException {
        Mission mission = new Mission("Monitoring Mission");
        Rocket rocket = new Rocket("Probe");
        mission.addRocket(rocket);
        assertTrue(mission.hasRocket(rocket));
    }

    @Test
    void hasRocket_shouldReturnFalseForNonAssignedRocket() {
        Mission mission = new Mission("Recon Mission");
        Rocket rocket = new Rocket("Scout");
        assertFalse(mission.hasRocket(rocket));
    }

    @Test
    void removeRocket_shouldSetStatusToScheduledWhenLastRocketRemoved()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Last Rocket Mission");
        Rocket rocket = new Rocket("Last Rocket");

        mission.addRocket(rocket);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(1, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket);

        assertEquals(MissionStatus.SCHEDULED, mission.getStatus());
        assertEquals(0, mission.getNumberOfAssignedRockets());
        assertNull(rocket.getAssignedMission());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
    }

    @Test
    void removeRocket_shouldSetStatusToInProgressWhenOtherRocketsRemain()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Multi-Rocket Mission");
        Rocket rocket1 = new Rocket("Rocket One");
        Rocket rocket2 = new Rocket("Rocket Two");

        mission.addRocket(rocket1);
        mission.addRocket(rocket2);
        assertEquals(2, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket1);

        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        assertEquals(1, mission.getNumberOfAssignedRockets());
        assertTrue(mission.hasRocket(rocket2));
        assertFalse(mission.hasRocket(rocket1));
    }

    @Test
    void removeRocket_shouldSetStatusToInProgressWhenOnlyInSpaceRocketRemains()
        throws RocketAlreadyAssignedException, MissionEndedException, RocketNotPartOfMissionException {
        Mission mission = new Mission("Mission");
        Rocket rocket1 = new Rocket("Rocket1");
        Rocket rocket2 = new Rocket("Rocket2");
        rocket2.setStatus(RocketStatus.IN_REPAIR);

        mission.addRocket(rocket1);
        assertEquals(MissionStatus.IN_PROGRESS, mission.getStatus());
        mission.addRocket(rocket2);
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
        mission.addRocket(rocket1);
        assertEquals(1, mission.getNumberOfAssignedRockets());

        Rocket rocket2 = new Rocket("Colony Ship 2");
        mission.addRocket(rocket2);
        assertEquals(2, mission.getNumberOfAssignedRockets());

        mission.removeRocket(rocket1);
        assertEquals(1, mission.getNumberOfAssignedRockets());
    }

    @Test
    void equals_shouldReturnTrueForSameName() {
        Mission mission1 = new Mission("Mission Alpha");
        Mission mission2 = new Mission("Mission Alpha");
        assertEquals(mission1, mission2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentName() {
        Mission mission1 = new Mission("Mission Alpha");
        Mission mission2 = new Mission("Mission Beta");
        assertNotEquals(mission1, mission2);
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Mission mission = new Mission("Mission Gamma");
        assertNotEquals(null, mission);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
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

        mission.addRocket(new Rocket("Survey Probe 1"));
        assertEquals("Solar Survey - IN_PROGRESS - Dragons: 1", mission.toString());

        mission.setStatus(MissionStatus.ENDED);
        assertEquals("Solar Survey - ENDED - Dragons: 1", mission.toString());
    }
}