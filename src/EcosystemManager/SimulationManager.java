package EcosystemManager;

import Entity.Animal;
import Entity.Rabbit;
import Entity.Wolf;
import Exceptions.EcosystemException;
import Factories.AnimalFactory;
import World.Environment;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;

public class SimulationManager extends JPanel implements Runnable, Serializable {

    static final int originalTileSize = 16; // 8x8 tile
    public static final int scale = 2;

    public static final int tileSize = originalTileSize * scale; // 16x16 tile
//    public final int maxScreenCol = 16;
//    public final int maxScreenRow = 12;
    public int screenWidth;
    public int screenHeight;

    private Graphics2D g2;


    transient Thread simulationThread;
    private final int sleepTime = 250;
    private boolean paused = false;


    private Environment environment;
    private Wolf[] wolves;
    private Rabbit[] rabbits;
    int wolfAmount, rabbitAmount;

    public SimulationManager() {
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.setFocusable(true);
    }

    public void initiateSimulation(int wolfLimit){
        environment = new Environment(20, 100);
        Wolf testWolf = new Wolf(environment, 5, 5, 3, 5);
        Rabbit testRabbit = new Rabbit(environment, 15, 15, 3, 5, 0.01);
        AnimalFactory animalF = new AnimalFactory(testWolf, testRabbit);

        wolves = new Wolf[wolfLimit];
        environment.environment[5][5] = 'W';
        rabbits = new Rabbit[wolfLimit * 2];
        environment.environment[15][15] = 'R';
        for (int i = 0; i < 1; ++i) {
            wolves[wolfAmount++] = (Wolf) animalF.createAnimal("wolf");
            rabbits[rabbitAmount++] = (Rabbit) animalF.createAnimal("rabbit");
            rabbits[rabbitAmount++] = (Rabbit) animalF.createAnimal("rabbit");
        }

        screenWidth = tileSize * environment.environmentSize;
        screenHeight = tileSize * environment.environmentSize;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    }

    public void startSimulationThread() {
        simulationThread = new Thread(this);
        simulationThread.start();
    }

    @Override
    public void run() {
        repaint();
        while(true) {
            moveAnimals(rabbits, rabbitAmount);
            moveAnimals(wolves, wolfAmount);
            synchronized (this) {
                while (paused) {
                    try {
                        wait(); // Pauses the thread and releases the lock
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore interrupted status
                    }
                }
            }
        }
    }

    private void moveAnimals(Rabbit[] animalArray, int numberOfAnimals) {
        try {
            for(int i = 0; i < Math.min(numberOfAnimals, rabbitAmount) && animalArray[i].getHealth() > 0; ++i) {
                int currX = animalArray[i].getX(), currY = animalArray[i].getY();
                environment.environment[animalArray[i].getX()][animalArray[i].getY()] = ' ';
                if(searchForTile(animalArray[i], 'P')) {
                    animalArray[i].eatPlant();
                }
                else {
                    animalArray[i].move();
                }

                //Smooth waking
//                environment.drawAnimal(g2, "rabbit", currX, currY, animalArray[i].getX(), animalArray[i].getY());


                environment.environment[animalArray[i].getX()][animalArray[i].getY()] = 'R';
                repaint();
                Thread.sleep(sleepTime);
            }
        } catch (EcosystemException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void moveAnimals(Wolf[] animalArray, int numberOfAnimals) {
        try {
            for(int i = 0; i < Math.min(numberOfAnimals, wolfAmount) && animalArray[i].getHealth() > 0; ++i) {
                int currX = animalArray[i].getX(), currY = animalArray[i].getY();
                environment.environment[animalArray[i].getX()][animalArray[i].getY()] = ' ';
                if(searchForTile(animalArray[i], 'R')) {
                    try {
                        animalArray[i].eatAnimal(findRabbit(animalArray[i].getX(), animalArray[i].getY()));
                    } catch (EcosystemException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else {
                    animalArray[i].move();
                }

                //Smooth waking
//                environment.drawAnimal(g2, "wolf", currX, currY, animalArray[i].getX(), animalArray[i].getY());

                environment.environment[animalArray[i].getX()][animalArray[i].getY()] = 'W';
                repaint();
                Thread.sleep(sleepTime);
            }
        } catch (EcosystemException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private Rabbit findRabbit(int x, int y) throws EcosystemException {
        for(int i = 0; i < rabbitAmount; ++i) {
            if(rabbits[i].getX() == x && rabbits[i].getY() == y) {
                return rabbits[i];
            }
        }
        throw new EcosystemException("Rabbit does not exist in that location", null);
    }

    private boolean searchForTile(Animal animal, char lookingFor) throws EcosystemException {
        int currX = animal.getX(), currY = animal.getY();
        for(int i = 1; i < Math.min(animal.getSpeed(), animal.getY()); ++i) {
            if(this.environment.environment[currX][currY - i] == lookingFor) {
                animal.move("up", i);
                return true;
            }
        }
        for(int i = 1; i < Math.min(animal.getSpeed(), this.environment.environmentSize - 2 - animal.getY()); ++i) {
            if(this.environment.environment[currX][currY + i] == lookingFor) {
                animal.move("down", i);
                return true;
            }
        }
        for(int i = 1; i < Math.min(animal.getSpeed(), animal.getX()); ++i) {
            if(this.environment.environment[currX - i][currY] == lookingFor) {
                animal.move("left", i);
                return true;
            }
        }
        for(int i = 1; i < Math.min(animal.getSpeed(), this.environment.environmentSize - 2 - animal.getX()); ++i) {
            if(this.environment.environment[currX + i][currY] == lookingFor) {
                animal.move("right", i);
                return true;
            }
        }
        for(int i = 1; i < Math.min(animal.getSpeed(), animal.getY()); ++i) {
            for(int j = 1; j < Math.min(animal.getSpeed(), animal.getX()); ++j) {
                if(this.environment.environment[currX - j][currY - i] == lookingFor) {
                    animal.move("left", j);
                    return true;
                }
            }
            for(int j = 1; j < Math.min(animal.getSpeed(), this.environment.environmentSize - 2 - animal.getX()); ++j) {
                if(this.environment.environment[currX + j][currY - i] == lookingFor) {
                    animal.move("right", j);
                    return true;
                }
            }
        }
        for(int i = 1; i < Math.min(animal.getSpeed(), this.environment.environmentSize - animal.getY()); ++i) {
            for(int j = 1; j < Math.min(animal.getSpeed(), animal.getX()); ++j) {
                if(this.environment.environment[currX - j][currY + i] == lookingFor) {
                    animal.move("left", j);
                    return true;
                }
            }
            for(int j = 1; j < Math.min(animal.getSpeed(), this.environment.environmentSize - 2 - animal.getX()); ++j) {
                if(this.environment.environment[currX + j][currY + i] == lookingFor) {
                    animal.move("right", j);
                    return true;
                }
            }
        }
        return false;
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);

        /*Graphics2D */g2 = (Graphics2D)g;

        environment.draw(g2);

        //g2.dispose();
    }

    public int getEnvironmentSize() {
        return environment.environmentSize;
    }

    public void saveState() {
        try {
            Thread.sleep(5000);
            environment.setGraphicsUnloaded();
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\Titas\\Desktop\\folder\\VU\\1k. 2sem\\Objektinis\\Ecosystem\\saves\\");
            int result = fileChooser.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                java.io.File file = fileChooser.getSelectedFile();
                ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
//                this.pause();
//                while (Thread.currentThread().getState() != Thread.State.WAITING);
                out.writeObject(this);
//                this.resume();
                out.close();
                JOptionPane.showMessageDialog(this, "State saved successfully!");
            }
        } catch (NotSerializableException e) {
            JOptionPane.showMessageDialog(this, "Error saving state: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void pause() {
        paused = true;
    }

    public synchronized void resume() {
        paused = false;
        notify(); // Notifies the thread to resume execution
    }
}
