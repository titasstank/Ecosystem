package Entity;

import Exceptions.EcosystemException;
import World.Environment;

public abstract class AbstractAnimal implements Movable, Cloneable {

    protected final Environment environment;
    protected int x, y;
    protected int speed;

    protected AbstractAnimal(Environment environment) {
        this.environment = environment;
    }

    public void move(String direction) throws EcosystemException {
        try {
            switch (direction) {
                case "up":
                    if(y >= speed) {
                        y -= speed;
                    }
                    break;
                case "down":
                    if(y + speed <= environment.environmentSize) {
                        y += speed;
                    }
                    break;
                case "left":
                    if(x >= speed) {
                        x -= speed;
                    }
                    break;
                case "right":
                    if(x + speed <= environment.environmentSize) {
                        x += speed;
                    }
            }
        } catch (Exception e) {
            System.out.println("Cant move!");
        }
    }

    public abstract void die();

    public abstract Object clone();

}
