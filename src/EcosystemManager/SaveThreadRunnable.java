package EcosystemManager;

public class SaveThreadRunnable implements Runnable {
    private final SimulationManager sManager;

    public SaveThreadRunnable(SimulationManager sManager) {
        this.sManager = sManager;
    }

    @Override
    public void run() {
        sManager.saveState();
    }
}
