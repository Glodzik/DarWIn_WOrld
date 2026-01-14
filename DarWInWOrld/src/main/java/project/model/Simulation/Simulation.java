package project.model.Simulation;

import project.model.RectangularMap;
import project.model.WorldElements.Animal;
import project.model.WorldElements.EdibleElements.Plant;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private List<Animal> animals = new ArrayList<Animal>();
    private List<Plant> plants = new ArrayList<Plant>();
    private RectangularMap worldMap;

    public Simulation(SimulationParameters parameters) {
        worldMap = new RectangularMap(parameters.mapWidth(), parameters.mapHeight());

    }
}
