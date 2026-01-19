package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import project.model.Map.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.Statistics.SimulationStatistics;
import project.model.Statistics.SimulationStatisticsTracker;

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
    private Label grassEnergyLabel;

    @FXML
    private Label lilyEnergyLabel;

    @FXML
    private VBox poisonLegendSection;
    @FXML
    private VBox basicLegend;
    @FXML
    private VBox customLegend;
    @FXML
    private Label basicPoisonEnergyLabel;

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

        SimulationParameters parameters = simulation.getParameters();
        setupLegend(parameters);
    }

    private void setupLegend(SimulationParameters parameters) {
        boolean hasPoisons = parameters.plantParameters().poisonProbability() > 0;

        if (parameters.customPlants()) {
            // Tryb customowych roślin - pokazuj rozbudowaną legendę
            basicLegend.setVisible(false);
            basicLegend.setManaged(false);
            customLegend.setVisible(true);
            customLegend.setManaged(true);

            // Ukryj sekcję TRUJĄCE gdy brak trujących roślin
            if (!hasPoisons) {
                poisonLegendSection.setVisible(false);
                poisonLegendSection.setManaged(false);
            }
        } else {
            // Tryb podstawowy - pokazuj uproszczoną legendę
            customLegend.setVisible(false);
            customLegend.setManaged(false);
            basicLegend.setVisible(true);
            basicLegend.setManaged(true);

            grassEnergyLabel.setText("+%d".formatted(parameters.plantParameters().energy()));

            grassEnergyLabel.setText("+%d".formatted(parameters.plantParameters().energy()));
            if(parameters.plantParameters().poisonProbability() > 0) {
                lilyEnergyLabel.setText("-%d".formatted(parameters.plantParameters().poisonEnergyLoss()));
            } else {
                // ukrywa trujące rośliny z legendy gdy są wyłączone
                poisonLegendSection.setVisible(false);
                poisonLegendSection.setManaged(false);
            }
        }
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
        topGenotypeLabel.setText(formatGenome(stats.getMostPopularGenes()));
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

    private String formatGenome(int[] genes) {
        if (genes == null) return "-";
        StringBuilder sb = new StringBuilder();
        for (int gene : genes) {
            sb.append(gene);
        }
        return sb.toString();
    }
}
