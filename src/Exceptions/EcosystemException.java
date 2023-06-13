package Exceptions;

import Entity.Animal;

public class EcosystemException extends Exception{
    public Animal animal;
    public EcosystemException(String message, Animal animal){
        super(message);
        this.animal = animal;
    }

}
