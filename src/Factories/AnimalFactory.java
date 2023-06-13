package Factories;

import Entity.Animal;
import Entity.Rabbit;
import Entity.Wolf;

public class AnimalFactory implements Factory {

    private final Wolf wolfPrototype;
    private final Rabbit rabbitPrototype;

    public AnimalFactory(Wolf wolfPrototype, Rabbit rabbitPrototype) {
        this.wolfPrototype = wolfPrototype;
        this.rabbitPrototype = rabbitPrototype;
    }

    public Animal createAnimal(String animal) {
        try {
            if (animal.equalsIgnoreCase("wolf")) {
                return (Animal) wolfPrototype.clone();
            } else if (animal.equalsIgnoreCase("rabbit")) {
                return (Animal) rabbitPrototype.clone();
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
