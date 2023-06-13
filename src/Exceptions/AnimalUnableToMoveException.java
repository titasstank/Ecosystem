package Exceptions;

import Entity.Animal;

public class AnimalUnableToMoveException extends EcosystemException {
    public AnimalUnableToMoveException(String message, Animal animal) {
        super(message, animal);
//        printAnimalInformation();
    }
}
