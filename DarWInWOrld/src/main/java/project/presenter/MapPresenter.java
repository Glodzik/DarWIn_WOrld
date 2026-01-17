package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import project.model.Boundary;
import project.model.MapChangeListener;
import project.model.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Vector2D;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.EdibleElements.Antidote;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.Poison;
import project.model.WorldElements.WorldElement;

import java.util.HashMap;
import java.util.Map;

public final class MapPresenter implements MapChangeListener {
    private RectangularMap worldMap;
    private Simulation simulation;

    @FXML
    private Canvas mapCanvas;

    private static final int BORDER_WIDTH = 2;
    private static final float BORDER_OFFSET = (float) BORDER_WIDTH / 2;

    // tekstury
    private static final String JUNGLE_BIOME = "/textures/biomes/grass_block_top_jungle.png";
    private static final String STEPPE_BIOME = "/textures/biomes/grass_block_top_steppe.png";
    private static final String ANIMAL_TEXTURE = "/textures/animal/animalDown.png";
    private static final String DEFAULT_PLANT = "/textures/plants/antidotes/grass.png";
    private static final String DEFAULT_POISON = "/textures/plants/poisons/aloe.png";

    private final Map<String, Image> imageCache = new HashMap<>();

    private Image loadImage(String path) {
        return imageCache.computeIfAbsent(path, p -> {
            try {
                return new Image(getClass().getResourceAsStream(p));
            } catch (Exception e) {
                System.err.println("Failed to load image: " + p);
                return null;
            }
        });
    }

    private Image getTextureForElement(WorldElement element) {
        if (element instanceof Animal) {
            return loadImage(ANIMAL_TEXTURE);
        }
        else if (element instanceof Antidote antidote) {
            return loadImage(antidote.getType().getTexturePath());
        } else if (element instanceof Poison poison) {
            return loadImage(poison.getType().getTexturePath());
        } else if(element instanceof Plant plant) {
            if(plant.getEnergy() > 0) {
                return loadImage(DEFAULT_PLANT);
            } else {
                return loadImage(DEFAULT_POISON);
            }
        }
        return null;
    }


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

        GraphicsContext graphics = mapCanvas.getGraphicsContext2D();
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(BORDER_WIDTH);

        // Get jungle bounds for biome rendering
        Boundary jungleBounds = worldMap.getJungleBoundary();

        // Draw biome backgrounds and objects
        int yCellValue = upperRight.getY();
        for (int y = cellSize; y < canvasHeight - cellSize + 1; y += cellSize) {
            int xCellValue = lowerLeft.getX();
            for (int x = cellSize; x < canvasWidth - cellSize + 1; x += cellSize) {
                Vector2D currentPos = new Vector2D(xCellValue, yCellValue);

                // Draw biome texture as background
                boolean isInJungle = currentPos.follows(jungleBounds.lowerLeft()) && currentPos.precedes(jungleBounds.upperRight());
                Image biomeTexture = loadImage(isInJungle ? JUNGLE_BIOME : STEPPE_BIOME);
                if (biomeTexture != null) {
                    graphics.drawImage(biomeTexture, x + BORDER_OFFSET, y + BORDER_OFFSET, cellSize - BORDER_WIDTH, cellSize - BORDER_WIDTH);
                }

                // Draw objects on top
                WorldElement plant = worldMap.getPlantAt(currentPos);
                if (plant != null) {
                    Image plantTexture = getTextureForElement(plant);
                    if (plantTexture != null) {
                        graphics.drawImage(plantTexture, x + BORDER_OFFSET, y + BORDER_OFFSET, cellSize - BORDER_WIDTH, cellSize - BORDER_WIDTH);
                    }
                }

                var animals = worldMap.getAnimalsAt(currentPos);
                if (!animals.isEmpty()) {
                    Image animalTexture = loadImage(ANIMAL_TEXTURE);
                    if (animalTexture != null) {
                        graphics.drawImage(animalTexture, x + BORDER_OFFSET, y + BORDER_OFFSET, cellSize - BORDER_WIDTH, cellSize - BORDER_WIDTH);
                    }
                }

                xCellValue++;
            }
            yCellValue--;
        }

        // Draw grid lines
        for (int x = 0; x < canvasWidth + 1; x += cellSize) {
            graphics.strokeLine(x + BORDER_OFFSET, 0, x + BORDER_OFFSET, canvasHeight);
        }
        for (int y = 0; y < canvasHeight + 1; y += cellSize) {
            graphics.strokeLine(0, y + BORDER_OFFSET, canvasWidth, y + BORDER_OFFSET);
        }


        // Filling cells
        configureFont(graphics, fontSize, Color.BLACK);
        int xCellValue = lowerLeft.getX();
        graphics.fillText("y/x", cellSize / 2, cellSize / 2);

        // X axis
        for (int x = cellSize / 2 + cellSize; x < canvasWidth + 1; x += cellSize) {
            String text = String.valueOf(xCellValue);
            graphics.fillText(text, x, cellSize / 2);
            xCellValue++;
        }

        // Y axis
        yCellValue = upperRight.getY();
        for (int y = cellSize / 2 + cellSize; y < canvasHeight + 1; y += cellSize) {
            String text = String.valueOf(yCellValue);
            graphics.fillText(text, cellSize / 2, y);
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
