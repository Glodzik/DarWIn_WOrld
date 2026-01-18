package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import project.model.Map.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Statistics.SimulationStatistics;
import project.model.Statistics.SimulationStatisticsTracker;

import java.util.Arrays;

public final class MapPresenter implements MapChangeListener {
    private RectangularMap worldMap;
    private Simulation simulation;
    private Thread simulationThread;
    private SimulationStatisticsTracker statisticsTracker;
    private MapDrafter mapDrafter;

    @FXML
    private Canvas mapCanvas;
    @FXML
    private Button toggleButton;
    @FXML
    private Label animalCountLabel;

    @FXML
    private Label deadAnimalCountLabel;

    @FXML
    private Label plantCountLabel;

    @FXML
    private Label freeFieldsLabel;

    @FXML
    private Label topGenotypeLabel;

    @FXML
    private Label avgEnergyLabel;

    @FXML
    private Label avgLifespanLabel;

    @FXML
    private Label avgChildrenLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private void initialize() {
        toggleButton.setOnAction(event -> toggleSimulation());
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = simulation.getWorldMap();
        this.statisticsTracker = simulation.getStatisticsTracker();
        this.mapDrafter = new MapDrafter(mapCanvas, simulation.getStartEnergy());

        mapDrafter.drawMap(worldMap);
        startSimulation();
    }


    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        Platform.runLater(() -> {
            mapDrafter.drawMap(worldMap);
            updateStats();
            System.out.println(message);
        });
    }

    private void updateStats() {
        int day = simulation.getDay();
        statisticsTracker.updateStats();
        SimulationStatistics stats = statisticsTracker.getStatistics();

        dayLabel.setText(String.valueOf(day));
        animalCountLabel.setText(String.valueOf(stats.getNumberOfAnimals()));
        deadAnimalCountLabel.setText(String.valueOf(stats.getDeadAnimals()));
        plantCountLabel.setText(String.valueOf(stats.getNumberOfPlants()));
        freeFieldsLabel.setText(String.valueOf(stats.getNumberOfNotOccupiedFields()));
        topGenotypeLabel.setText(Arrays.toString(stats.getMostPopularGenes()));
        avgChildrenLabel.setText(String.format("%.2f", stats.getAverageAmountOfChildren()));
        avgEnergyLabel.setText(String.format("%.2f", stats.getAverageEnergyLevel()));
        avgLifespanLabel.setText(String.format("%.2f", stats.getAverageLifespan()));
    }

    // turning simulation on/off

    private void toggleSimulation() {
        if (simulation == null) return;

        if (simulation.isRunning()) {
            stopSimulation();
            toggleButton.setText("START");
        } else {
            startSimulation();
            toggleButton.setText("STOP");
        }
    }

    private void startSimulation() {
        if (simulation == null) return;
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        simulation.start();
        simulationThread = new Thread(simulation);
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    private void stopSimulation() {
        if (simulation != null) {
            simulation.stop();
        }

        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
    }

    public void cleanup() {
        stopSimulation();
    }
}
