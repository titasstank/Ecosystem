import java.util.Random;

public class Animal {

    private final Environment environment;

    private int x, y;
    private int speed;
    private int health;


    private static int animalCount = 0;
    //public static final int availableAnimalNameAmount = 4;
    //private static final String[] animalList = {"Squirrel", "Rabbit", "Wolf", "Bear"};

    public Animal() {
        this(null, (int)(Math.random() * Environment.minEnvironmentSize), (int)(Math.random() * Environment.minEnvironmentSize), 2, 10);
    }

    public Animal(Environment environment, int x, int y, int speed, int health) {
//        this.environment = environment;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.health = health;
        Animal.animalCount++;
    }

    public void move() { // Method to move in a random direction
        Random direction = new Random();
        try {
            switch (direction.nextInt(4)) {
                case 0:
                    if(y >= speed) {
                        y -= speed;
                    }
                    else {
                        y = 0;
                    }
                    break;
                case 1:
                    if(y + speed <= environment.environmentSize) {
                        y += speed;
                    }
                    else {
                        y = environment == null ? Environment.minEnvironmentSize : environment.environmentSize;
                    }
                    break;
                case 2:
                    if(x >= speed) {
                        x -= speed;
                    }
                    else {
                        x = 0;
                    }
                    break;
                case 3:
                    if(x + speed <= environment.environmentSize) {
                        x += speed;
                    }
                    else {
                        x = environment == null ? Environment.minEnvironmentSize : environment.environmentSize;
                    }
            }
        } catch (Exception e) {
            System.out.println("Cant move!");
        }
    }

    public void move(String direction) {
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

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

//    public static String[] getAnimalList() {
//        return animalList;
//    }

    public static int getAnimalCount() {
        return animalCount;
    }

    public void println() {
        System.out.println("Coordinates: (" + x + ", " + y + ")");
        System.out.println("Speed: " + speed);
        System.out.println("Health " + health + "\n");
    }

}
