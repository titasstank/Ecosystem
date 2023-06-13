package Exceptions;

import Entity.Animal;

public class EnvironmentNotAssignedException extends EcosystemException{
    public EnvironmentNotAssignedException(String message, Animal animal){
        super(message, animal);
//        printAnimalInformation();
    }
}
