package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import project.model.Boundary;
import project.model.MapChangeListener;
import project.model.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.Vector2D;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.EdibleElements.PlantParameters;

import java.io.IOException;

public class SimulationPresenter implements MapChangeListener {
    private RectangularMap worldMap;

    @FXML
    private TextField mapWidthField;
    @FXML
    private TextField mapHeigthField;
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
    private TextField genomeLenghtField;



    private static final int BORDER_WIDTH = 2;
    private static final float BORDER_OFFSET = (float) BORDER_WIDTH / 2;

    private int calculateCellSize(int mapWidth, int mapHeight) {
        int maxCanvasWidth = 800;  // maksymalna szerokość okna
        int maxCanvasHeight = 600; // maksymalna wysokość okna

        int cellByWidth = maxCanvasWidth / (mapWidth + 2);
        int cellByHeight = maxCanvasHeight / (mapHeight + 2);

        int cellSize = Math.min(cellByWidth, cellByHeight);
        return Math.max(cellSize, 5);
    }

    @FXML
    private Canvas mapCanvas;

    public void setWorldMap(RectangularMap worldMap) {
        this.worldMap = worldMap;
    }

    public void drawMap() {
        // Setting canvas height and width
        Boundary boundary = worldMap.getMapBounds();
        Vector2D lowerLeft = boundary.lowerLeft();
        Vector2D upperRight = boundary.upperRight();
        Vector2D cellCount = upperRight.subtract(lowerLeft);

        int cellSize = calculateCellSize(upperRight.getX() - lowerLeft.getX() + 1, upperRight.getY() - lowerLeft.getY() + 1);
        int canvasWidth = ((int) cellCount.getX() + 2) * cellSize + BORDER_WIDTH;
        int canvasHeight = ((int) cellCount.getY() + 2) * cellSize + BORDER_WIDTH;
        int fontSize = Math.max(8, cellSize / 2);

        mapCanvas.setWidth(canvasWidth);
        mapCanvas.setHeight(canvasHeight);

        // Clearing canvas
        clearCanvas();


        // Creating grid
        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(BORDER_WIDTH);

        for (int x = 0; x < mapCanvas.getWidth() + 1; x += cellSize) {
            graphics.strokeLine(x + BORDER_OFFSET, 0, x + BORDER_OFFSET, mapCanvas.getHeight());
        }
        for (int y = 0; y < mapCanvas.getHeight() + 1; y += cellSize) {
            graphics.strokeLine(0, y + BORDER_OFFSET, mapCanvas.getWidth(), y + BORDER_OFFSET);
        }


        // Filling cells
        configureFont(graphics, fontSize, Color.BLACK);
        int xCellValue = lowerLeft.getX();
        graphics.fillText("y/x", cellSize / 2, cellSize / 2);

        // X axis
        for (int x = cellSize / 2 + cellSize; x < mapCanvas.getWidth() + 1; x += cellSize) {
            String text = String.valueOf((xCellValue));
            graphics.fillText(text, x, cellSize / 2);
            xCellValue++;
        }

        // Y axis
        int yCellValue = upperRight.getY();
        for (int y = cellSize / 2 + cellSize; y < mapCanvas.getHeight() + 1; y += cellSize) {
            String text = String.valueOf((yCellValue));
            graphics.fillText(text, cellSize / 2, y);
            yCellValue--;
        }

        // Filling cells with objects on map
        yCellValue = upperRight.getY();
        graphics.setFill(Color.RED);
        for (int y = cellSize / 2 + cellSize; y < mapCanvas.getHeight() + 1; y += cellSize) {
            xCellValue = lowerLeft.getX();
            for (int x = cellSize / 2 + cellSize; x < mapCanvas.getWidth() + 1; x += cellSize) {
                if(worldMap.objectAt(new Vector2D(xCellValue, yCellValue)) != null) {
                    graphics.fillText(worldMap.objectAt(new Vector2D(xCellValue, yCellValue)).toString(), x, y);
                }
                xCellValue++;
            }
            yCellValue--;
        }

        //System.out.printf(worldMap.toString());
    }

    private void clearCanvas() {
        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    private void configureFont(GraphicsContext graphics, int size, Color black) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Arial", size));
        graphics.setFill(black);
    }

    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            System.out.println(message);
        });
    }

    public void onSimulationStartClicked() {
        int mapWidth = Integer.parseInt(mapWidthField.getText());
        int mapHeigth = Integer.parseInt(mapHeigthField.getText());
        int startPlants = Integer.parseInt(startPlantsField.getText());
        int newPlantsEveryday = Integer.parseInt(newPlantsEverydayField.getText());
        int startAnimals = Integer.parseInt(startAnimalsField.getText());
        int startEnergy = Integer.parseInt(startEnergyField.getText());
        int energyLossEveryDay = Integer.parseInt(energyLossEveryDayField.getText());
        int energyLevelToBreed = Integer.parseInt(energyLevelToBreedField.getText());
        int energyLossAfterBreed = Integer.parseInt(energyLossAfterBreedField.getText());
        int minMutation = Integer.parseInt(minMutationField.getText());
        int maxMutation = Integer.parseInt(maxMutationField.getText());
        int genomeLenght = Integer.parseInt(genomeLenghtField.getText());

        AnimalParameters animalParameters = new AnimalParameters(startEnergy, energyLossEveryDay, energyLevelToBreed, energyLossAfterBreed, minMutation, maxMutation, genomeLenght);
        SimulationParameters parameters = new SimulationParameters(mapHeigth, mapWidth, startPlants, newPlantsEveryday, startAnimals,
                animalParameters, new PlantParameters(20, 40, -20), 8, true);
        Simulation simulation = new Simulation(parameters);
        setWorldMap(simulation.getWorldMap());
        worldMap.addObserver(this);

        try {
            // Ładowanie FXML dla okna mapy
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getClassLoader().getResource("simulation_map_view.fxml"));
            BorderPane root = loader.load();

            // Pobranie presentera i przekazanie mapy
            SimulationPresenter mapPresenter = loader.getController();
            mapPresenter.setWorldMap(simulation.getWorldMap());
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
}
