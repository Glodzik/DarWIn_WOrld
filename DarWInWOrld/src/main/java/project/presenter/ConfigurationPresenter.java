package project.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.EdibleElements.PlantParameters;

import java.io.IOException;

public class ConfigurationPresenter {
    @FXML
    private Slider mapWidthField;
    @FXML
    private Slider mapHeightField;
    @FXML
    private Slider startPlantsField;
    @FXML
    private Slider newPlantsEverydayField;
    @FXML
    private Slider startAnimalsField;
    @FXML
    private Slider startEnergyField;
    @FXML
    private Slider energyLossEveryDayField;
    @FXML
    private Slider energyLevelToBreedField;
    @FXML
    private Slider energyLossAfterBreedField;
    @FXML
    private Slider minMutationField;
    @FXML
    private Slider maxMutationField;
    @FXML
    private Slider genomeLengthField;
    @FXML
    private Slider eatingEnergyField;
    @FXML
    private Slider poisonPlantProbabilityField;
    @FXML
    private Slider poisonEnergyLossField;
    @FXML
    private Slider protectionGenomeLengthField;
    @FXML
    private CheckBox customPlantsCheckbox;
    @FXML
    private CheckBox poisonPlantsVariantCheckbox;

    @FXML
    private Label mapWidthLabel;
    @FXML
    private Label mapHeightLabel;
    @FXML
    private Label startPlantsLabel;
    @FXML
    private Label eatingEnergyLabel;
    @FXML
    private Label newPlantsEverydayLabel;
    @FXML
    private Label startAnimalsLabel;
    @FXML
    private Label startEnergyLabel;
    @FXML
    private Label energyLossEveryDayLabel;
    @FXML
    private Label energyLevelToBreedLabel;
    @FXML
    private Label energyLossAfterBreedLabel;
    @FXML
    private Label minMutationLabel;
    @FXML
    private Label maxMutationLabel;
    @FXML
    private Label genomeLengthLabel;
    @FXML
    private Label poisonPlantProbabilityLabel;
    @FXML
    private Label poisonEnergyLossLabel;
    @FXML
    private Label protectionGenomeLengthLabel;

    @FXML
    public void initialize() {
        setupSliderListeners();
        setupCheckboxListeners();
    }

    private void setupSliderListeners() {
        mapWidthField.valueProperty().addListener((obs, oldVal, newVal) ->
                mapWidthLabel.setText(String.valueOf(newVal.intValue())));
        mapHeightField.valueProperty().addListener((obs, oldVal, newVal) ->
                mapHeightLabel.setText(String.valueOf(newVal.intValue())));
        startPlantsField.valueProperty().addListener((obs, oldVal, newVal) ->
                startPlantsLabel.setText(String.valueOf(newVal.intValue())));
        eatingEnergyField.valueProperty().addListener((obs, oldVal, newVal) ->
                eatingEnergyLabel.setText(String.valueOf(newVal.intValue())));
        newPlantsEverydayField.valueProperty().addListener((obs, oldVal, newVal) ->
                newPlantsEverydayLabel.setText(String.valueOf(newVal.intValue())));
        startAnimalsField.valueProperty().addListener((obs, oldVal, newVal) ->
                startAnimalsLabel.setText(String.valueOf(newVal.intValue())));
        startEnergyField.valueProperty().addListener((obs, oldVal, newVal) ->
                startEnergyLabel.setText(String.valueOf(newVal.intValue())));
        energyLossEveryDayField.valueProperty().addListener((obs, oldVal, newVal) ->
                energyLossEveryDayLabel.setText(String.valueOf(newVal.intValue())));
        energyLevelToBreedField.valueProperty().addListener((obs, oldVal, newVal) ->
                energyLevelToBreedLabel.setText(String.valueOf(newVal.intValue())));
        energyLossAfterBreedField.valueProperty().addListener((obs, oldVal, newVal) ->
                energyLossAfterBreedLabel.setText(String.valueOf(newVal.intValue())));
        minMutationField.valueProperty().addListener((obs, oldVal, newVal) ->
                minMutationLabel.setText(String.valueOf(newVal.intValue())));
        maxMutationField.valueProperty().addListener((obs, oldVal, newVal) ->
                maxMutationLabel.setText(String.valueOf(newVal.intValue())));
        genomeLengthField.valueProperty().addListener((obs, oldVal, newVal) ->
                genomeLengthLabel.setText(String.valueOf(newVal.intValue())));
        poisonPlantProbabilityField.valueProperty().addListener((obs, oldVal, newVal) ->
                poisonPlantProbabilityLabel.setText(String.valueOf(newVal.intValue())));
        poisonEnergyLossField.valueProperty().addListener((obs, oldVal, newVal) ->
                poisonEnergyLossLabel.setText(String.valueOf(newVal.intValue())));
        protectionGenomeLengthField.valueProperty().addListener((obs, oldVal, newVal) ->
                protectionGenomeLengthLabel.setText(String.valueOf(newVal.intValue())));
    }

    private void setupCheckboxListeners() {
        // Listener dla checkboxa trujących roślin
        poisonPlantsVariantCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            poisonPlantProbabilityField.setDisable(!newValue);
            poisonEnergyLossField.setDisable(!newValue);
            protectionGenomeLengthField.setDisable(!newValue);
            poisonPlantProbabilityLabel.setDisable(!newValue);
            poisonEnergyLossLabel.setDisable(!newValue);
            protectionGenomeLengthLabel.setDisable(!newValue);

            if (!newValue) {
                poisonPlantProbabilityField.setValue(0);
                poisonEnergyLossField.setValue(0);
                protectionGenomeLengthField.setValue(0);
            } else {
                poisonPlantProbabilityField.setValue(20);
                poisonEnergyLossField.setValue(20);
                protectionGenomeLengthField.setValue(10);
            }
        });
    }

    public void onSimulationStartClicked() {
        SimulationParameters parameters = getSimulationParameters();
        Simulation simulation = new Simulation(parameters);

        try {
            // Ładowanie FXML dla okna mapy
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulationMap.fxml"));
            BorderPane root = loader.load();

            // Pobranie presentera i przekazanie symulacji
            MapPresenter mapPresenter = loader.getController();
            mapPresenter.setSimulation(simulation);
            simulation.getWorldMap().addObserver(mapPresenter);

            // Stworzenie nowego okna
            Stage mapStage = new Stage();
            mapStage.setTitle("Symulacja");
            mapStage.setScene(new Scene(root));
            mapStage.show();

            mapStage.setOnCloseRequest(event -> {
                mapPresenter.cleanup();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SimulationParameters getSimulationParameters() {
        int mapWidth = (int) mapWidthField.getValue();
        int mapHeight = (int) mapHeightField.getValue();
        int startPlants = (int) startPlantsField.getValue();
        int eatingEnergy = (int) eatingEnergyField.getValue();
        int newPlantsEveryday = (int) newPlantsEverydayField.getValue();
        int startAnimals = (int) startAnimalsField.getValue();
        int startEnergy = (int) startEnergyField.getValue();
        int energyLossEveryDay = (int) energyLossEveryDayField.getValue();
        int energyLevelToBreed = (int) energyLevelToBreedField.getValue();
        int energyLossAfterBreed = (int) energyLossAfterBreedField.getValue();
        int minMutation = (int) minMutationField.getValue();
        int maxMutation = (int) maxMutationField.getValue();
        int genomeLength = (int) genomeLengthField.getValue();
        int poisonPlantProbability = (int) poisonPlantProbabilityField.getValue();
        int poisonEnergyLoss = (int) poisonEnergyLossField.getValue();
        int protectionGenomeLength = (int) protectionGenomeLengthField.getValue();
        boolean poisonPlantsVariant = poisonPlantsVariantCheckbox.isSelected();
        boolean customPlants = customPlantsCheckbox.isSelected();

        if(!poisonPlantsVariant) {
            poisonPlantProbability = 0;
            poisonEnergyLoss = 0;
        }

        AnimalParameters animalParameters = new AnimalParameters(startEnergy, energyLossEveryDay, energyLevelToBreed, energyLossAfterBreed, minMutation, maxMutation, genomeLength);
        PlantParameters plantParameters = new PlantParameters(eatingEnergy, poisonPlantProbability, poisonEnergyLoss);

        return new SimulationParameters
                (mapHeight, mapWidth, startPlants, newPlantsEveryday, startAnimals,
                        animalParameters, plantParameters, protectionGenomeLength, customPlants);
    }
}