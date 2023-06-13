package Entity;

import Exceptions.EcosystemException;
import World.Environment;

public class Rabbit extends Animal implements Jumpable {

    final private double chanceToDodge;

    public Rabbit() {
        this(null, (int)(Math.random() * Environment.minEnvironmentSize), (int)(Math.random() * Environment.minEnvironmentSize), 10, 15, 0.15);
    }

    public Rabbit(Environment environment, int x, int y, int speed, int health, double chanceToDodge) {
        super(environment, x, y, speed, health);
        this.chanceToDodge = chanceToDodge;
    }

    @Override
    public void move(String direction) throws EcosystemException {
        super.move(direction);
        if(Math.random() >= 0.5) {
            super.move(direction);
        }
    }

    public void eatPlant() {
        if(environment.environment[this.getY()][this.getX()] == 'P') {
            this.setHealth(Math.max(this.getHealth() + environment.getPlantValue(), this.getMaxHealth()));
            environment.environment[this.getY()][this.getX()] = 'R';
        }
    }

    public Animal multiply() {
        if (getHealth() >= getMaxHealth() * 0.8) {
            setHealth((int) (getMaxHealth() * 0.8));
            try {
                return (Animal) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

    public boolean tryToJumpAway() {
        return Math.random() <= chanceToDodge;
    }

    @Override
    public String toString() {
        return "Animal type : Herbivore" +
                "\nAnimal name: " + this.getClass().getSimpleName() +
                '\n' + super.toString();
    }

//    public double getChanceToDodge() {
//        return chanceToDodge;
//    }
}
