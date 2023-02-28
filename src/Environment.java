import java.util.Random;

public class Environment {

    static final int minEnvironmentSize = 10;
    public final int environmentSize;
    char[][] environment;
    int maxPlantAmount = 10;

    public Environment() {
        this.environmentSize = 10;
        createEnvironment();
        generatePlants();
    }

    public Environment(int environmentSize) {
        this.environmentSize = environmentSize;
        createEnvironment();
        generatePlants();
    }

    public Environment(int environmentSize, int maxPlantAmount) {
        this.environmentSize = environmentSize;
        this.maxPlantAmount = maxPlantAmount;
        createEnvironment();
        generatePlants();
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

    void println() {
        System.out.printf("%-20s %d\n", "Enivornment size:", environmentSize);
        System.out.printf("%-20s %d\n", "Plant amount:", maxPlantAmount);
        for(int i = 0; i < environmentSize/2; ++i) {
            for (int j = 0; j < environmentSize; ++j) {
                System.out.print(environment[i][j]);
            }
            System.out.println();
        }
    }
}
