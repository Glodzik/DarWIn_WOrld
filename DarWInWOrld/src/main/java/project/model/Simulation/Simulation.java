package project.model.Simulation;

import project.model.RectangularMap;
import project.model.WorldElements.Animal;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private List<Animal> animals = new ArrayList<Animal>();
    private RectangularMap worldMap;

    public Simulation(SimulationParameters parameters) {
        worldMap = new RectangularMap(parameters.mapWidth(), parameters.mapHeight());

    }
}
