package pl.skasu.dragon.model;

import org.junit.jupiter.api.Test;

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
    void constructor_shouldThrowNullPointerExceptionForNullName() {
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
    void setStatus_shouldThrowNullPointerExceptionForNullStatus() {
        Rocket rocket = new Rocket("Phoenix");
        assertThrows(NullPointerException.class, () -> rocket.setStatus(null));
    }

    @Test
    void getAssignedMission_shouldReturnNullInitially() {
        Rocket rocket = new Rocket("Dragon");
        assertNull(rocket.getAssignedMission());
    }

    @Test
    void assignToMission_shouldAssignMissionAndUpdateStatus() {
        Rocket rocket = new Rocket("Orion");
        Mission mission = new Mission("Mars Exploration");
        rocket.assignToMission(mission);
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_SPACE, rocket.getStatus());
        assertTrue(rocket.isAssigned());
    }

    @Test
    void assignToMission_shouldNotChangeStatusIfInRepair() {
        Rocket rocket = new Rocket("Repaired Rocket");
        Mission mission = new Mission("Repair Mission");
        rocket.setStatus(RocketStatus.IN_REPAIR);
        rocket.assignToMission(mission);
        assertEquals(mission, rocket.getAssignedMission());
        assertEquals(RocketStatus.IN_REPAIR, rocket.getStatus());
    }

    @Test
    void assignToMission_shouldThrowNullPointerExceptionForNullMission() {
        Rocket rocket = new Rocket("Test Rocket");
        assertThrows(NullPointerException.class, () -> rocket.assignToMission(null));
    }

    @Test
    void isAssigned_shouldReturnTrueWhenAssigned() {
        Rocket rocket = new Rocket("Assigned Rocket");
        Mission mission = new Mission("Test Mission");
        rocket.assignToMission(mission);
        assertTrue(rocket.isAssigned());
    }

    @Test
    void isAssigned_shouldReturnFalseWhenNotAssigned() {
        Rocket rocket = new Rocket("Unassigned Rocket");
        assertFalse(rocket.isAssigned());
    }

    @Test
    void equals_shouldReturnTrueForSameName() {
        Rocket rocket1 = new Rocket("RocketA");
        Rocket rocket2 = new Rocket("RocketA");
        assertEquals(rocket1, rocket2);
    }

    @Test
    void equals_shouldReturnFalseForDifferentName() {
        Rocket rocket1 = new Rocket("RocketA");
        Rocket rocket2 = new Rocket("RocketB");
        assertNotEquals(rocket1, rocket2);
    }

    @Test
    void equals_shouldReturnFalseForNull() {
        Rocket rocket = new Rocket("RocketC");
        assertNotEquals(null, rocket);
    }

    @Test
    void equals_shouldReturnFalseForDifferentClass() {
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
}