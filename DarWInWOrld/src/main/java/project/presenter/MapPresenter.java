package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import project.model.Map.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.Simulation.Statistics.SimulationStatistics;
import project.model.Simulation.Statistics.SimulationStatisticsTracker;

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
    private Label basicPlantEnergyLabel;
    @FXML
    private HBox basicPoisonRow;

    // Custom plants energy labels - antidotes
    @FXML
    private Label bambooEnergyLabel;
    @FXML
    private Label carrotEnergyLabel;
    @FXML
    private Label parsleyEnergyLabel;
    @FXML
    private Label sunflowerEnergyLabel;

    // Custom plants energy labels - poisons
    @FXML
    private Label aloeEnergyLabel;
    @FXML
    private Label azaleaEnergyLabel;
    @FXML
    private Label oleanderEnergyLabel;
    @FXML
    private Label cisEnergyLabel;

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
        int plantEnergy = parameters.plantParameters().energy();
        int poisonEnergy = parameters.plantParameters().poisonEnergyLoss();

        if (parameters.customPlants()) {
            // Tryb customowych roślin - pokazuj rozbudowaną legendę
            basicLegend.setVisible(false);
            basicLegend.setManaged(false);
            customLegend.setVisible(true);
            customLegend.setManaged(true);

            // Skalowanie wartości energii dla antidotes
            // plantEnergy to wartość minimalna (Trawa), reszta rośnie proporcjonalnie (1x, 1.5x, 2x, 2.5x, 3x)
            grassEnergyLabel.setText("+%d".formatted(plantEnergy));
            bambooEnergyLabel.setText("+%d".formatted((int)(plantEnergy * 1.5)));
            carrotEnergyLabel.setText("+%d".formatted(plantEnergy * 2));
            parsleyEnergyLabel.setText("+%d".formatted((int)(plantEnergy * 2.5)));
            sunflowerEnergyLabel.setText("+%d".formatted(plantEnergy * 3));

            // Ukryj sekcję TRUJĄCE gdy brak trujących roślin
            if (!hasPoisons) {
                poisonLegendSection.setVisible(false);
                poisonLegendSection.setManaged(false);
            } else {
                // Skalowanie wartości energii dla poisons (1x, 2x, 3x, 4x, 6x)
                lilyEnergyLabel.setText("-%d".formatted(poisonEnergy));
                aloeEnergyLabel.setText("-%d".formatted((int)(poisonEnergy * 1.5)));
                azaleaEnergyLabel.setText("-%d".formatted(poisonEnergy * 2));
                oleanderEnergyLabel.setText("-%d".formatted((int)(poisonEnergy * 2.5)));
                cisEnergyLabel.setText("-%d".formatted(poisonEnergy * 3));
            }
        } else {
            // Tryb podstawowy - pokazuj uproszczoną legendę
            customLegend.setVisible(false);
            customLegend.setManaged(false);
            basicLegend.setVisible(true);
            basicLegend.setManaged(true);

            basicPlantEnergyLabel.setText("+%d".formatted(plantEnergy));
            if (hasPoisons) {
                basicPoisonEnergyLabel.setText("-%d".formatted(poisonEnergy));
            } else {
                // ukrywa trujące rośliny z legendy gdy są wyłączone
                basicPoisonRow.setVisible(false);
                basicPoisonRow.setManaged(false);
                basicPoisonEnergyLabel.setVisible(false);
                basicPoisonEnergyLabel.setManaged(false);
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
