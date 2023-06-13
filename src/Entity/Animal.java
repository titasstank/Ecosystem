package Entity;


import Exceptions.AnimalUnableToMoveException;
import Exceptions.EcosystemException;
import Exceptions.EnvironmentNotAssignedException;
import World.Environment;

import java.io.Serializable;
import java.util.Random;

public abstract class Animal implements Movable, Cloneable, Serializable {

    protected Environment environment;

    private int x, y;
    private int speed;
    protected boolean customSpeed = false;
    private int health;
    private final int maxHealth;
    private int caloricValue;

    public Animal() {
        this(null, (int)(Math.random() * Environment.minEnvironmentSize), (int)(Math.random() * Environment.minEnvironmentSize), 2, 10);
    }

    public Animal(Environment environment, int x, int y, int speed, int health) {
        this.environment = environment;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.maxHealth = this.health = health;
        this.caloricValue = health/2;
    }

    public void move() { // Method to move in a random direction
        Random direction = new Random();
        /*
            0 - up
            1 - down
            2 - left
            3 - right
         */
        try {
            switch (direction.nextInt(4)) {
                case 0:
                    if(y == 1) {
                        validateMoving("down", speed);
                        break;
                    }
                    if(y >= speed + 1) {
                        validateMoving("up", speed);
                    }
                    else {
                        validateMoving("up", y - 1);
                    }
                    break;
                case 1:
                    if(y == environment.environmentSize - 2) {
                        validateMoving("up", speed);
                        break;
                    }
                    if(y + speed < environment.environmentSize - 1) {
                        validateMoving("down", speed);
                    }
                    else {
                        validateMoving("down", environment.environmentSize - 2 - y);
                    }
                    break;
                case 2:
                    if(x == 1) {
                        validateMoving("right", speed);
                        break;
                    }
                    if(x >= speed + 1) {
                        validateMoving("left", speed);
                    }
                    else {
                        validateMoving("left", x - 1);
                    }
                    break;
                case 3:
                    if(x == environment.environmentSize - 2) {
                        validateMoving("left", speed);
                        break;
                    }
                    if(x + speed < environment.environmentSize - 1) {
                        validateMoving("right", speed);
                    }
                    else {
                        validateMoving("right", environment.environmentSize - 2 - x);
                    }
            }
        } catch (Exception e) {
            System.out.println("Cant move!");
            System.out.println(this.getClass().getSimpleName() + "( " + x + ", " + y + ")");
        }
    }

    public void validateMoving(String direction, int amount){
        switch (direction){
            case "up":
                for(int i = amount; i > 0; --i) {
                    if(environment.environment[x][y - i] == ' ' || environment.environment[x][y * i] == 'P') {
                        y -= i;
                    }
                }
                break;
            case "down":
                for(int i = amount; i > 0; --i) {
                    if(environment.environment[x][y + i] == ' ' || environment.environment[x][y + i] == 'P') {
                        y += i;
                    }
                }
                break;
            case "left":
                for(int i = amount; i > 0; --i) {
                    if(environment.environment[x - i][y] == ' ' || environment.environment[x - i][y] == 'P') {
                        x -= i;
                    }
                }
                break;
            case "right":
                for(int i = amount; i > 0; --i) {
                    if(environment.environment[x + i][y] == ' ' || environment.environment[x + i][y] == 'P') {
                        x += i;
                    }
                }
        }
    }

    public void move(String direction) throws EcosystemException {
//        try {
        if(this.environment == null){
            throw new EnvironmentNotAssignedException("Animal tried to move without environment set", this);
        }
            switch (direction) {
                case "up":
                    if(y > speed) {
                        y -= speed;
                    }
                    break;
                case "down":
                    if(y + speed >= environment.environmentSize) {
                        throw new AnimalUnableToMoveException("Animal cant move " + direction, this);
                    }
                    else {
                        y += speed;
                    }
                    break;
                case "left":
                    if(x > speed ) {
                        x -= speed;
                    }
                    break;
                case "right":
                    if(x + speed < environment.environmentSize) {
                        x += speed;
                }
            }
//        } catch (AnimalUnableToMoveException e) {
//            System.out.println(e.getMessage());
//        }
    }

    public void move(String direction, int amount) throws EcosystemException{
        if(amount <= this.speed) {
            int tempSpeed = this.speed;
            this.speed = amount;
            customSpeed = true;
            this.move(direction);
            this.speed = tempSpeed;
            customSpeed = false;
        }
        else {
            this.move(direction);
        }
    }

    public abstract Animal multiply();

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {

        this.health = Math.min(health, this.maxHealth);
    }

    public int getMaxHealth() {
        return maxHealth;
    }
    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCaloricValue() {
        return caloricValue;
    }

    public void setCaloricValue(int caloricValue) {
        this.caloricValue = caloricValue;
    }

    final public void die() {
        this.health = 0;
    }

    @Override
    public String toString() {
        return "This animal speed: " + this.getSpeed() +
                "\nThis animal max health: " + this.getMaxHealth() +
                "\nThis animal health: " + this.getHealth() +
                "\nThis animal caloric value: " + this.getCaloricValue() + "\n";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {

        // Deep cloning
//        if (this.environment != null) {
//            clonedAnimal.environment = (Environment) this.environment.clone();
//        }

        return super.clone();
    }
}
