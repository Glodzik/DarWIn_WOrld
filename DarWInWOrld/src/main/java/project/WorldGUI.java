package project;

import javafx.application.Application;
import javafx.scene.text.Font;
import project.model.simulation.SimulationApp;

public final class WorldGUI {
    public static void main(String[] args) {
        Font.loadFont(WorldGUI.class.getResourceAsStream("resources/font/Montserrat-Bold.ttf"), 42);
        Application.launch(SimulationApp.class, args);
    }
}
