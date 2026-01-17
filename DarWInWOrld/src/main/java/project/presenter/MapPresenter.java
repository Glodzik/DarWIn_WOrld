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
import project.model.Vector2D;

public final class MapPresenter implements MapChangeListener {
    private RectangularMap worldMap;
    private Simulation simulation;

    @FXML
    private Canvas mapCanvas;

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

    private void configureFont(GraphicsContext graphics, int size, Color black) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Arial", size));
        graphics.setFill(black);
    }

    private void clearCanvas() {
        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, mapCanvas.getWidth(), mapCanvas.getHeight());
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.worldMap = simulation.getWorldMap();
        drawMap();
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
                if (worldMap.objectAt(new Vector2D(xCellValue, yCellValue)) != null) {
                    graphics.fillText(worldMap.objectAt(new Vector2D(xCellValue, yCellValue)).toString(), x, y);
                }
                xCellValue++;
            }
            yCellValue--;
        }
    }

    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            System.out.println(message);
        });
    }
}
