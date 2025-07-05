package pl.skasu.dragon.exception;

import pl.skasu.dragon.model.RocketStatus;

public class InvalidRocketStatusException extends Exception {
    public InvalidRocketStatusException(String rocketName, String currentStatus, String requiredStatus) {
        super("Rocket '" + rocketName + "' has status '" + currentStatus + "', but '" + requiredStatus + "' was expected.");
    }
}
