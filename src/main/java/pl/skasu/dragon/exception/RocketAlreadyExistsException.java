package pl.skasu.dragon.exception;

public class RocketAlreadyExistsException extends Exception {

    public RocketAlreadyExistsException(String rocketName) {
        super("Rocket '" + rocketName + "' already exists.");
    }
}
