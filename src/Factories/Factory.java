package Factories;

import Entity.Animal;

public interface Factory {
    Animal createAnimal(String animal);
}
