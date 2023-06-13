package Entity;

import Exceptions.EcosystemException;
import World.Environment;

import java.io.Serializable;

public class Wolf extends Animal implements Carnivorous {

    private final int damage = 20;

    public Wolf() {
        this(null, (int)(Math.random() * Environment.minEnvironmentSize), (int)(Math.random() * Environment.minEnvironmentSize), 5, 25);
    }

    public Wolf(Environment environment, int x, int y, int speed, int health) {
        super(environment, x, y, speed, health);
    }

    @Override
    public void move (String direction) throws EcosystemException {
        if(!customSpeed) {
            int tempSpeed = this.getSpeed();
            double speedMultiplier = Math.max(1 + ((double) this.getMaxHealth() / (double) this.getHealth() - 1) * 0.125, 1.5);
            this.setSpeed((int) (tempSpeed * speedMultiplier));
            super.move(direction);
            this.setSpeed(tempSpeed);
        }
        else {
            super.move(direction);
        }
    }

    public boolean eatAnimal(Rabbit prey) {
        if(!prey.tryToJumpAway()) {
            return this.eatAnimal((Animal)prey);
        }
        else {
            prey.move();
            System.out.println("Evaded");
            return false;
        }
    }

    public boolean eatAnimal(Animal prey) {
        if (prey.getHealth() <= this.damage) {
            boolean canMultiply = this.getHealth() + prey.getCaloricValue() > this.getMaxHealth();
            this.setHealth(this.getHealth() + prey.getCaloricValue());
            prey.die();
            return canMultiply;
        } else {
            prey.setHealth(prey.getHealth() - this.damage);
            return false;
        }
    }

    public Animal multiply() {
        if (getHealth() >= getMaxHealth() * 0.9) {
            setHealth((int) (getMaxHealth() * 0.9));
            try {
                return (Animal) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Animal type : Carnivore" +
                "\nAnimal name: " + this.getClass().getSimpleName() +
                '\n' + super.toString();
    }
}
