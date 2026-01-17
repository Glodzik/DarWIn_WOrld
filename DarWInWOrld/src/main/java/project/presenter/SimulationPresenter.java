package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import project.model.Boundary;
import project.model.MapChangeListener;
import project.model.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Simulation.SimulationParameters;
import project.model.Vector2D;
import project.model.WorldElements.Animals.AnimalParameters;
import project.model.WorldElements.Animals.Genome;
import project.model.WorldElements.EdibleElements.PlantParameters;

public class SimulationPresenter implements MapChangeListener {
    private RectangularMap worldMap;

    private static final int CELL_SIZE = 45;
    private static final int BORDER_WIDTH = 2;
    private static final float BORDER_OFFSET = (float) BORDER_WIDTH / 2;
    private static final int FONT_SIZE = 25;

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

        int canvasWidth = ((int) cellCount.getX() + 2) * CELL_SIZE + BORDER_WIDTH;
        int canvasHeight = ((int) cellCount.getY() + 2) * CELL_SIZE + BORDER_WIDTH;

        mapCanvas.setWidth(canvasWidth);
        mapCanvas.setHeight(canvasHeight);

        // Clearing canvas
        clearCanvas();


        // Creating grid
        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(BORDER_WIDTH);

        for (int x = 0; x < mapCanvas.getWidth() + 1; x += CELL_SIZE) {
            graphics.strokeLine(x + BORDER_OFFSET, 0, x + BORDER_OFFSET, mapCanvas.getHeight());
        }
        for (int y = 0; y < mapCanvas.getHeight() + 1; y += CELL_SIZE) {
            graphics.strokeLine(0, y + BORDER_OFFSET, mapCanvas.getWidth(), y + BORDER_OFFSET);
        }


        // Filling cells
        configureFont(graphics, FONT_SIZE, Color.BLACK);
        int xCellValue = lowerLeft.getX();
        graphics.fillText("y/x", CELL_SIZE / 2, CELL_SIZE / 2);

        // X axis
        for (int x = CELL_SIZE/2 + CELL_SIZE; x < mapCanvas.getWidth() + 1; x += CELL_SIZE) {
            String text = String.valueOf((xCellValue));
            graphics.fillText(text, x, CELL_SIZE/2);
            xCellValue++;
        }

        // Y axis
        int yCellValue = upperRight.getY();
        for (int y = CELL_SIZE/2 + CELL_SIZE; y < mapCanvas.getHeight() + 1; y += CELL_SIZE) {
            String text = String.valueOf((yCellValue));
            graphics.fillText(text, CELL_SIZE/2, y);
            yCellValue--;
        }

        // Filling cells with objects on map
        yCellValue = upperRight.getY();
        graphics.setFill(Color.RED);
        for (int y = CELL_SIZE/2 + CELL_SIZE; y < mapCanvas.getHeight() + 1; y += CELL_SIZE) {
            xCellValue = lowerLeft.getX();
            for(int x = CELL_SIZE/2 + CELL_SIZE; x < mapCanvas.getWidth() + 1; x += CELL_SIZE) {
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
        AnimalParameters animalParameters = new AnimalParameters(100, 10, 80, 20, 1, 3, 8);
        SimulationParameters parameters = new SimulationParameters(9,10, 10,5,5,
                animalParameters, new PlantParameters(20, 40, -20), 8, true);
        Simulation simulation = new Simulation(parameters);
        setWorldMap(simulation.getWorldMap());
        worldMap.addObserver(this);

        Thread thread = new Thread(simulation);
        thread.start();
    }
}
