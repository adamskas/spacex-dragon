package pl.skasu.dragon.model;

import org.junit.jupiter.api.Test;
import pl.skasu.dragon.exception.RocketAlreadyAssignedException;

import static org.junit.jupiter.api.Assertions.*;

class RocketTest {

    @Test
    void constructor_shouldSetNameAndInitialStatus() {
        Rocket rocket = new Rocket("Apollo 11");
        assertEquals("Apollo 11", rocket.getName());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        assertNull(rocket.getAssignedMission());
        assertFalse(rocket.isAssigned());
    }

    @Test
    void constructor_shouldThrowNullPointerException_forNullName() {
        assertThrows(NullPointerException.class, () -> new Rocket(null));
    }

    @Test
    void getName_shouldReturnCorrectName() {
        Rocket rocket = new Rocket("Falcon 9");
        assertEquals("Falcon 9", rocket.getName());
    }

    @Test
    void getStatus_shouldReturnCurrentStatus() {
        Rocket rocket = new Rocket("Starship");
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        rocket.setStatus(RocketStatus.IN_SPACE);
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    void setStatus_shouldUpdateStatus() {
        Rocket rocket = new Rocket("Voyager");
        rocket.setStatus(RocketStatus.IN_SPACE);
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
    }

    @Test
    void setStatus_shouldThrowNullPointerException_forNullStatus() {
        Rocket rocket = new Rocket("Phoenix");
        assertThrows(NullPointerException.class, () -> rocket.setStatus(null));
    }

    @Test
    void getAssignedMission_shouldReturnNullInitially() {
        Rocket rocket = new Rocket("Dragon");
        assertNull(rocket.getAssignedMission());
    }

    @Test
    void canBeAssignedToMission_shouldThrowNPE_whenMissionIsNull() {
        Rocket rocket = new Rocket("Test Rocket");

        assertThrows(NullPointerException.class, () -> rocket.canBeAssignedToMission(null));
    }

    @Test
    void canBeAssignedToMission_shouldNotThrow_whenRocketIsNotAssigned() {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission = new Mission("Test Mission");

        assertDoesNotThrow(() -> rocket.canBeAssignedToMission(mission));
    }

    @Test
    void canBeAssignedToMission_shouldNotThrow_whenRocketIsAssignedToSameMission() throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission = new Mission("Test Mission");

        rocket.assignToMission(mission);

        assertDoesNotThrow(() -> rocket.canBeAssignedToMission(mission));
    }

    @Test
    void canBeAssignedToMission_shouldThrowRocketAlreadyAssignedException_whenAssignedToDifferentMission() throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission1 = new Mission("Mission 1");
        Mission mission2 = new Mission("Mission 2");
        rocket.assignToMission(mission1);

        assertThrows(RocketAlreadyAssignedException.class, () -> rocket.canBeAssignedToMission(mission2));
    }


    @Test
    void assignToMission_shouldAssignMissionAndUpdateStatus()
        throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Orion");
        Mission mission = new Mission("Mars Exploration");
        rocket.assignToMission(mission);
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
        assertTrue(rocket.isAssigned());
    }

    @Test
    void assignToMission_shouldNotChangeStatus_whenInRepair() throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Repaired Rocket");
        Mission mission = new Mission("Repair Mission");
        rocket.setStatus(RocketStatus.IN_REPAIR);
        rocket.assignToMission(mission);
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());
    }

    @Test
    void assignToMission_shouldNotThrow_whenRocketAlreadyAssignedToTheSameMission()
        throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission = new Mission("Test Mission");
        rocket.assignToMission(mission);
        assertDoesNotThrow(() -> rocket.assignToMission(mission));
        assertTrue(rocket.isAssigned());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
        assertEquals(mission, rocket.getAssignedMission());
    }

    @Test
    void assignToMission_shouldThrowRocketAlreadyAssignedException_whenRocketIsAlreadyAssigned()
        throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission1 = new Mission("Test Mission");
        Mission mission2 = new Mission("Another Test Mission");
        rocket.assignToMission(mission1);
        assertTrue(rocket.isAssigned());
        assertThrows(RocketAlreadyAssignedException.class, () -> rocket.assignToMission(mission2));
    }

    @Test
    void assignToMission_shouldThrowNullPointerException_forNullMission() {
        Rocket rocket = new Rocket("Test Rocket");
        assertThrows(NullPointerException.class, () -> rocket.assignToMission(null));
    }

    @Test
    void isAssigned_shouldReturnTrue_whenAssigned() throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Assigned Rocket");
        Mission mission = new Mission("Test Mission");
        rocket.assignToMission(mission);
        assertTrue(rocket.isAssigned());
    }

    @Test
    void isAssigned_shouldReturnFalse_whenNotAssigned() {
        Rocket rocket = new Rocket("Unassigned Rocket");
        assertFalse(rocket.isAssigned());
    }

    @Test
    void isAssigned_shouldReturnFalse_whenAfterRemovalFromMission()
        throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Removed Rocket");
        Mission mission = new Mission("Test Mission");
        rocket.assignToMission(mission);
        assertTrue(rocket.isAssigned());
        rocket.removeFromMission();
        assertFalse(rocket.isAssigned());
    }

    @Test
    void equals_shouldReturnTrue_forSameName() {
        Rocket rocket1 = new Rocket("RocketA");
        Rocket rocket2 = new Rocket("RocketA");
        assertEquals(rocket1, rocket2);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentName() {
        Rocket rocket1 = new Rocket("RocketA");
        Rocket rocket2 = new Rocket("RocketB");
        assertNotEquals(rocket1, rocket2);
    }

    @Test
    void equals_shouldReturnFalse_forNull() {
        Rocket rocket = new Rocket("RocketC");
        assertNotEquals(null, rocket);
    }

    @Test
    void equals_shouldReturnFalse_forDifferentClass() {
        Rocket rocket = new Rocket("RocketD");
        Object obj = new Object();
        assertNotEquals(rocket, obj);
    }

    @Test
    void hashCode_shouldBeConsistentWithEquals() {
        Rocket rocket1 = new Rocket("RocketE");
        Rocket rocket2 = new Rocket("RocketE");
        Rocket rocket3 = new Rocket("RocketF");

        assertEquals(rocket1.hashCode(), rocket2.hashCode());
        assertNotEquals(rocket1.hashCode(), rocket3.hashCode());
    }

    @Test
    void toString_shouldReturnCorrectFormat() {
        Rocket rocket = new Rocket("Explorer");
        assertEquals("Explorer - ON_GROUND", rocket.toString());

        rocket.setStatus(RocketStatus.IN_SPACE);
        assertEquals("Explorer - IN_SPACE", rocket.toString());
    }

    @Test
    void removeFromMission_shouldUpdateRocketStateCorrectly()
        throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission = new Mission("Test Mission");
        
        rocket.assignToMission(mission);
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
        assertTrue(rocket.isAssigned());
        
        rocket.removeFromMission();
        assertNull(rocket.getAssignedMission());
        assertEquals(RocketStatus.ON_GROUND, rocket.getStatus());
        assertFalse(rocket.isAssigned());
    }

    @Test
    void removeFromMission_shouldNotUpdateRocketState_whenRocketIsInRepair()
        throws RocketAlreadyAssignedException {
        Rocket rocket = new Rocket("Test Rocket");
        Mission mission = new Mission("Test Mission");
        rocket = new Rocket("Repair Rocket");
        mission = new Mission("Repair Mission");
        rocket.setStatus(RocketStatus.IN_REPAIR);
        rocket.assignToMission(mission);
        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());

        rocket.removeFromMission();
        assertNull(rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());
        assertFalse(rocket.isAssigned());
    }
}