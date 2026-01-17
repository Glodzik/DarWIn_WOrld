package project.presenter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.EdibleElements.PlantParameters;

import java.io.IOException;

public class ConfigurationPresenter {
    @FXML
    private TextField mapWidthField;
    @FXML
    private TextField mapHeightField;
    @FXML
    private TextField startPlantsField;
    @FXML
    private TextField newPlantsEverydayField;
    @FXML
    private TextField startAnimalsField;
    @FXML
    private TextField startEnergyField;
    @FXML
    private TextField energyLossEveryDayField;
    @FXML
    private TextField energyLevelToBreedField;
    @FXML
    private TextField energyLossAfterBreedField;
    @FXML
    private TextField minMutationField;
    @FXML
    private TextField maxMutationField;
    @FXML
    private TextField genomeLengthField;
    @FXML
    private TextField eatingEnergyField;
    @FXML
    private TextField poisonPlantProbabilityField;
    @FXML
    private TextField poisonEnergyLossField;
    @FXML
    private TextField protectionGenomeLengthField;
    @FXML
    private CheckBox customPlantsCheckbox;

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

            Thread thread = new Thread(simulation);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private SimulationParameters getSimulationParameters() {
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        int mapHeight = Integer.parseInt(mapHeightField.getText());
        int startPlants = Integer.parseInt(startPlantsField.getText());
        int eatingEnergy = Integer.parseInt(eatingEnergyField.getText());
        int newPlantsEveryday = Integer.parseInt(newPlantsEverydayField.getText());
        int startAnimals = Integer.parseInt(startAnimalsField.getText());
        int startEnergy = Integer.parseInt(startEnergyField.getText());
        int energyLossEveryDay = Integer.parseInt(energyLossEveryDayField.getText());
        int energyLevelToBreed = Integer.parseInt(energyLevelToBreedField.getText());
        int energyLossAfterBreed = Integer.parseInt(energyLossAfterBreedField.getText());
        int minMutation = Integer.parseInt(minMutationField.getText());
        int maxMutation = Integer.parseInt(maxMutationField.getText());
        int genomeLength = Integer.parseInt(genomeLengthField.getText());
        int poisonPlantProbability = Integer.parseInt(poisonPlantProbabilityField.getText());
        int poisonEnergyLoss = Integer.parseInt(poisonEnergyLossField.getText());
        int protectionGenomeLength = Integer.parseInt(protectionGenomeLengthField.getText());
        boolean customPlants = customPlantsCheckbox.isSelected();

        AnimalParameters animalParameters = new AnimalParameters(startEnergy, energyLossEveryDay, energyLevelToBreed, energyLossAfterBreed, minMutation, maxMutation, genomeLength);
        PlantParameters plantParameters = new PlantParameters(eatingEnergy, poisonPlantProbability, poisonEnergyLoss);

        return new SimulationParameters
                (mapHeight, mapWidth, startPlants, newPlantsEveryday, startAnimals,
                        animalParameters, plantParameters, protectionGenomeLength, customPlants);
    }


}
