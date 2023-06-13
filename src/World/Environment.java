package World;

import EcosystemManager.SimulationManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

public class Environment implements Cloneable, Serializable {

    public static final int minEnvironmentSize = 10;
    public final int environmentSize;
    public char[][] environment;
    private int maxPlantAmount;
    private int plantValue;
    private transient BufferedImage[] tiles;
    private boolean graphicsLoaded = false;

    public Environment() {
        this(10);
    }

    public Environment(int environmentSize) {
        this(environmentSize, 10);
    }

    public Environment(int environmentSize, int maxPlantAmount) {
        this.environmentSize = environmentSize;
        this.maxPlantAmount = maxPlantAmount;
        createEnvironment();
        generatePlants();
        getTileImage();
    }

    void createEnvironment() {
        environment = new char[environmentSize][environmentSize];
        for(int i = 0; i < environmentSize; ++i) {
            for (int j = 0; j < environmentSize; ++j) {
                if(i == 0 || i == environmentSize - 1 || j == 0 || j == environmentSize - 1) {
                    environment[i][j] = '#';
                }
                else {
                    environment[i][j] = ' ';
                }
            }
        }
    }

    void generatePlants() {
        Random rand = new Random();
        int row, col;
        for(int plantAmount = 0; plantAmount < maxPlantAmount;) {
            col = rand.nextInt(environmentSize - 2);
            row = rand.nextInt(environmentSize - 2);
            if(environment[row+1][col+1] != 'P') {
                environment[row+1][col+1] = 'P';
                plantAmount++;
            }
        }
    }


    void generatePlants(int newMaxPlantAmount) {
        if(newMaxPlantAmount != this.maxPlantAmount) {
            if(newMaxPlantAmount < this.maxPlantAmount) {
                createEnvironment();
                this.maxPlantAmount = 0;
            }
            Random rand = new Random();
            int row, col;
            for(int plantAmount = this.maxPlantAmount; plantAmount < newMaxPlantAmount;) {
                col = rand.nextInt(environmentSize-2);
                row = rand.nextInt(environmentSize/2 - 2);
                if(environment[row+1][col+1] != 'P') {
                    environment[row+1][col+1] = 'P';
                    plantAmount++;
                }
            }
            this.maxPlantAmount = newMaxPlantAmount;
        }
    }

    public int getMaxPlantAmount() {
        return maxPlantAmount;
    }

    public void setMaxPlantAmount(int maxPlantAmount) {
        generatePlants(maxPlantAmount);
    }

    public void println() {
//        System.out.printf("%-20s %d\n", "Environment size:", environmentSize);
//        System.out.printf("%-20s %d\n", "Plant amount:", maxPlantAmount);
        for(int i = 0; i < environmentSize; ++i) {
            for (int j = 0; j < environmentSize; ++j) {
                System.out.print(environment[i][j]);
            }
            System.out.println();
        }
    }

    public int getPlantValue() {
        return plantValue;
    }

    public void setPlantValue(int plantValue) {
        this.plantValue = plantValue;
    }

    public void setGraphicsUnloaded() {
        graphicsLoaded = false;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Environment clonedEnv = (Environment) super.clone();
        clonedEnv.environment = new char[this.environmentSize][this.environmentSize];
        for (int i = 0; i < this.environmentSize; i++) {
            for(int j = 0; j < this.environmentSize; j++) {
                clonedEnv.environment[i][j] = this.environment[i][j];
            }
        }
        return clonedEnv;
    }

    public void getTileImage(){
        tiles = new BufferedImage[12];
        try {
            tiles[0] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grass.png")));
            tiles[1] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grassWithFence.png")));
            tiles[2] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grassWithFenceTopLeft.png")));
            tiles[3] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grassWithFenceTopRight.png")));
            tiles[4] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grassWithFenceBottomRight.png")));
            tiles[5] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grassWithFenceBottomLeft.png")));
            tiles[6] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/grassWithFenceSide.png")));
            tiles[7] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/wolf.png")));
            tiles[8] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/rabbit.png")));
            tiles[9] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/plant.png")));
            tiles[10] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/wolfNoBG.png")));
            tiles[11] = ImageIO.read(Files.newInputStream(Paths.get("src/Graphics/rabbitNoBG.png")));

        } catch (IOException e) {
            e.printStackTrace();
        }
        graphicsLoaded = true;
    }

    public void drawAnimal(Graphics2D g2, String animal, int x0, int y0, int x, int y) throws InterruptedException {
        if(!graphicsLoaded)
            getTileImage();
        BufferedImage animalImage = null;
        switch (animal) {
            case "wolf":
                animalImage = tiles[10];
//                g2.drawImage(tiles[10], x * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                break;
            case "rabbit":
                animalImage = tiles[11];
//                g2.drawImage(tiles[11], x * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                break;
        }
        if(x0 != x) {
            for(int i = x0 * SimulationManager.tileSize; i <= x * SimulationManager.tileSize; ++i) {
                System.out.println("painting");
                drawInterval(g2, "x", i / SimulationManager.tileSize, y0);
                g2.drawImage(animalImage, i, y0 * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                Thread.sleep(50);
            }
        }
        if(y0 != y) {
            for(int i = y0 * SimulationManager.tileSize; i <= y * SimulationManager.tileSize; ++i) {
                drawInterval(g2, "y", x, i / SimulationManager.tileSize);
                g2.drawImage(animalImage, x, i * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                Thread.sleep(50);
            }
        }
    }

    public void drawInterval(Graphics2D g2, String axis, int x, int y) {
        if(!graphicsLoaded)
            getTileImage();
        switch (axis) {
            case "x":
                for(int i = x - 1; i <= x + 1; ++i) {
                    switch (environment[i][y]) {
                        case '#':
                            g2.drawImage(tiles[6], i * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        case 'P':
                            g2.drawImage(tiles[9], i * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        case 'W':
                            g2.drawImage(tiles[7], i * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        case 'R':
                            g2.drawImage(tiles[8], i * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        default:
                            g2.drawImage(tiles[0], i * SimulationManager.tileSize, y * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                    }
                }
                break;
            case "y":
                for(int i = y - 1; i <= y + 1; ++i) {
                    switch (environment[x][i]) {
                        case '#':
                            g2.drawImage(tiles[1], x * SimulationManager.tileSize, i * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        case 'P':
                            g2.drawImage(tiles[9], x * SimulationManager.tileSize, i * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        case 'W':
                            g2.drawImage(tiles[7], x * SimulationManager.tileSize, i * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        case 'R':
                            g2.drawImage(tiles[8], x * SimulationManager.tileSize, i * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                            break;
                        default:
                            g2.drawImage(tiles[0], x * SimulationManager.tileSize, i * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                    }
                }
        }
    }

    public void draw (Graphics2D g2) {
        if(!graphicsLoaded)
            getTileImage();
        for(int i = 0; i < environmentSize; ++i) {
            for (int j = 0; j < environmentSize; ++j) {
                switch (environment[i][j])
                {
                    case '#':
                        if(i == 0 && j == 0)
                            g2.drawImage(tiles[2], 0, 0, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        else if(i == environmentSize - 1 && j == 0)
                            g2.drawImage(tiles[3], i * SimulationManager.tileSize, 0, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        else if(i == environmentSize - 1 && j == environmentSize - 1)
                            g2.drawImage(tiles[4], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        else if(i == 0 && j == environmentSize - 1)
                            g2.drawImage(tiles[5], 0, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        else if(i == 0 || i == environmentSize - 1)
                            g2.drawImage(tiles[6], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        else {
                            g2.drawImage(tiles[1], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        }
                        break;
                    case ' ':
                        g2.drawImage(tiles[0], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        break;
                    case 'W':
                        g2.drawImage(tiles[7], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        break;
                    case 'R':
                        g2.drawImage(tiles[8], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                        break;
                    case 'P':
                        g2.drawImage(tiles[9], i * SimulationManager.tileSize, j * SimulationManager.tileSize, SimulationManager.tileSize, SimulationManager.tileSize, null);
                }
            }
        }
    }

}
