package project.presenter;


import project.model.simulation.SimulationParameters;
import project.model.worldelements.animals.AnimalParameters;
import project.model.worldelements.edibleelements.PlantParameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class PresetService {
    private static final Map<String, SimulationParameters> PRESETS = Map.of(
            "Domyślny", new SimulationParameters(
                    15, 15,
                    10, 10, 10,
                    new AnimalParameters(100, 10, 80, 30, 5, 5, 10),
                    new PlantParameters(20, 20, 20),
                    10, false, false
            ),
            "Krótka", new SimulationParameters(
                    8, 8,
                    5, 5, 15,
                    new AnimalParameters(60, 15, 70, 35, 2, 5, 10),
                    new PlantParameters(15, 30, 25),
                    8, false, false
            ),
            "Długa", new SimulationParameters(
                    20, 20,
                    30, 20, 10,
                    new AnimalParameters(150, 5, 60, 25, 1, 3, 10),
                    new PlantParameters(30, 10, 15),
                    10, false, false

            ),

            "Z truciznami", new SimulationParameters(
                    15, 15,
                    15, 10, 10,
                    new AnimalParameters(100, 10, 80, 30, 2, 5, 10),
                    new PlantParameters(20, 0, 0),
                    0, true, false
            ),
            "Długa - customowe rośliny", new SimulationParameters(
                    20, 20,
                    40, 25, 10,
                    new AnimalParameters(150, 5, 60, 25, 1, 3, 10),
                    new PlantParameters(35, 20, 20),
                    10, true, true
            )
    );

    public List<String> getPresetNames() {
        return new ArrayList<>(PRESETS.keySet());
    }

    public void saveToFile(SimulationParameters data, File file) throws IOException {
        Properties props = new Properties();
        props.setProperty("mapHeight", String.valueOf(data.mapHeight()));
        props.setProperty("mapWidth", String.valueOf(data.mapWidth()));
        props.setProperty("startPlants", String.valueOf(data.startPlants()));
        props.setProperty("newPlantsEveryday", String.valueOf(data.newPlantsEveryday()));
        props.setProperty("startAnimals", String.valueOf(data.startAnimals()));
        props.setProperty("startEnergy", String.valueOf(data.animalParameters().startEnergy()));
        props.setProperty("energyLossEveryDay", String.valueOf(data.animalParameters().energyLossEveryDay()));
        props.setProperty("energyLevelToBreed", String.valueOf(data.animalParameters().energyLevelToBreed()));
        props.setProperty("energyLossAfterBreed", String.valueOf(data.animalParameters().energyLossAfterBreed()));
        props.setProperty("minMutation", String.valueOf(data.animalParameters().minMutation()));
        props.setProperty("maxMutation", String.valueOf(data.animalParameters().maxMutation()));
        props.setProperty("genomeLength", String.valueOf(data.animalParameters().genomLength()));
        props.setProperty("eatingEnergy", String.valueOf(data.plantParameters().energy()));
        props.setProperty("poisonProbability", String.valueOf(data.plantParameters().poisonProbability()));
        props.setProperty("poisonEnergyLoss", String.valueOf(data.plantParameters().poisonEnergyLoss()));
        props.setProperty("protectionLength", String.valueOf(data.protectionGenomLength()));
        // Inference for backward compatibility or future use
        boolean poisonPlantVariant = data.plantParameters().poisonProbability() > 0;
        props.setProperty("poisonPlantVariant", String.valueOf(poisonPlantVariant));
        props.setProperty("customPlants", String.valueOf(data.customPlants()));

        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "Darwin World");
        }
    }

    public SimulationParameters loadFromFile(File file) throws IOException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        }

        return new SimulationParameters(
                Integer.parseInt(props.getProperty("mapHeight")),
                Integer.parseInt(props.getProperty("mapWidth")),
                Integer.parseInt(props.getProperty("startPlants")),
                Integer.parseInt(props.getProperty("newPlantsEveryday")),
                Integer.parseInt(props.getProperty("startAnimals")),
                new AnimalParameters(
                        Integer.parseInt(props.getProperty("startEnergy")),
                        Integer.parseInt(props.getProperty("energyLossEveryDay")),
                        Integer.parseInt(props.getProperty("energyLevelToBreed")),
                        Integer.parseInt(props.getProperty("energyLossAfterBreed")),
                        Integer.parseInt(props.getProperty("minMutation")),
                        Integer.parseInt(props.getProperty("maxMutation")),
                        Integer.parseInt(props.getProperty("genomeLength"))
                ),
                new PlantParameters(
                        Integer.parseInt(props.getProperty("eatingEnergy")),
                        Integer.parseInt(props.getProperty("poisonProbability")),
                        Integer.parseInt(props.getProperty("poisonEnergyLoss"))
                ),
                Integer.parseInt(props.getProperty("protectionLength")),
                Boolean.parseBoolean(props.getProperty("poisonPlantsVariant")),
                Boolean.parseBoolean(props.getProperty("customPlants"))
        );
    }

    public SimulationParameters getPreset(String name) {
        return PRESETS.get(name);
    }
}


