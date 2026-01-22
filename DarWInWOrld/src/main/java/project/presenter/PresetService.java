package project.presenter;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class PresetService {
    private static final Map<String, ConfigurationData> PRESETS = Map.of(
            "Domyślny", new ConfigurationData(
                    15, 15,
                    10, 10, 10,
                    100, 10,
                    80, 30,
                    5, 5,
                    10, 20,
                    20, 20, 10,
                    true, false
            ),
            "Krótka", new ConfigurationData(
                    8, 8,
                    5, 5, 15,
                    60, 15,
                    70, 35,
                    2, 5,
                    10, 15,
                    30, 25, 8,
                    true, false
            ),
            "Średnia", new ConfigurationData(
                    15, 15,
                    15, 12, 12,
                    100, 10,
                    80, 30,
                    2, 5,
                    10, 20,
                    15, 20, 10,
                    true, false
            ),
            "Długa", new ConfigurationData(
                    20, 20,
                    30, 20, 10,
                    150, 5,
                    60, 25,
                    1, 3,
                    10, 30,
                    10, 15, 10,
                    true, false
            ), "Bez trucizn", new ConfigurationData(
                    15, 15,
                    15, 10, 10,
                    100, 10,
                    80, 30,
                    2, 5,
                    10, 20,
                    0, 0, 0,
                    false, false
            ),
            "Długa - customowe rośliny", new ConfigurationData(
                    20, 20,
                    40, 25, 10,
                    150, 5,
                    60, 25,
                    1, 3,
                    10, 35,
                    20, 20, 10,
                    true, true
            )
    );

    public List<String> getPresetNames() {
        return new ArrayList<>(PRESETS.keySet());
    }

    public void saveToFile(ConfigurationData data, File file) throws IOException {
        Properties props = new Properties();
        props.setProperty("mapHeight", String.valueOf(data.mapHeight()));
        props.setProperty("mapWidth", String.valueOf(data.mapWidth()));
        props.setProperty("startPlants", String.valueOf(data.startPlants()));
        props.setProperty("newPlantsEveryday", String.valueOf(data.newPlantsEveryday()));
        props.setProperty("startAnimals", String.valueOf(data.startAnimals()));
        props.setProperty("startEnergy", String.valueOf(data.startEnergy()));
        props.setProperty("energyLossEveryDay", String.valueOf(data.energyLossEveryDay()));
        props.setProperty("energyLevelToBreed", String.valueOf(data.energyLevelToBreed()));
        props.setProperty("energyLossAfterBreed", String.valueOf(data.energyLossAfterBreed()));
        props.setProperty("minMutation", String.valueOf(data.minMutation()));
        props.setProperty("maxMutation", String.valueOf(data.maxMutation()));
        props.setProperty("genomeLength", String.valueOf(data.genomeLength()));
        props.setProperty("eatingEnergy", String.valueOf(data.eatingEnergy()));
        props.setProperty("poisonProbability", String.valueOf(data.poisonProbability()));
        props.setProperty("poisonEnergyLoss", String.valueOf(data.poisonEnergyLoss()));
        props.setProperty("protectionLength", String.valueOf(data.protectionLength()));
        props.setProperty("poisonPlantVariant", String.valueOf(data.poisonPlantVariant()));
        props.setProperty("customPlants", String.valueOf(data.customPlants()));

        try (FileOutputStream out = new FileOutputStream(file)) {
            props.store(out, "Darwin World");
        }
    }

    public ConfigurationData loadFromFile(File file) throws IOException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        }

        return new ConfigurationData(
                Integer.parseInt(props.getProperty("mapHeight")),
                Integer.parseInt(props.getProperty("mapWidth")),
                Integer.parseInt(props.getProperty("startPlants")),
                Integer.parseInt(props.getProperty("newPlantsEveryday")),
                Integer.parseInt(props.getProperty("startAnimals")),
                Integer.parseInt(props.getProperty("startEnergy")),
                Integer.parseInt(props.getProperty("energyLossEveryDay")),
                Integer.parseInt(props.getProperty("energyLevelToBreed")),
                Integer.parseInt(props.getProperty("energyLossAfterBreed")),
                Integer.parseInt(props.getProperty("minMutation")),
                Integer.parseInt(props.getProperty("maxMutation")),
                Integer.parseInt(props.getProperty("genomeLength")),
                Integer.parseInt(props.getProperty("eatingEnergy")),
                Integer.parseInt(props.getProperty("poisonProbability")),
                Integer.parseInt(props.getProperty("poisonEnergyLoss")),
                Integer.parseInt(props.getProperty("protectionLength")),
                Boolean.parseBoolean(props.getProperty("poisonPlantVariant")),
                Boolean.parseBoolean(props.getProperty("customPlants"))
        );
    }

    public ConfigurationData getPreset(String name) {
        return PRESETS.get(name);
    }
}


