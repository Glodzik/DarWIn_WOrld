package project.presenter;

import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import project.model.coordinates.Boundary;
import project.model.coordinates.Vector2D;
import project.model.map.RectangularMap;
import project.model.worldelements.WorldElement;
import project.model.worldelements.animals.Animal;
import project.model.worldelements.animals.AnimalComparator;
import project.model.worldelements.edibleelements.Antidote;
import project.model.worldelements.edibleelements.Plant;
import project.model.worldelements.edibleelements.Poison;

import java.util.HashMap;
import java.util.Map;

public final class MapDrafter {

    private static final int BORDER_WIDTH = 2;
    private static final float BORDER_OFFSET = (float) BORDER_WIDTH / 2;

    private static final String JUNGLE_BIOME = "/textures/biomes/grass_block_top_jungle.png";
    private static final String STEPPE_BIOME = "/textures/biomes/grass_block_top_steppe.png";
    private static final String ANIMAL_TEXTURE = "/textures/animal/animalUp.png";
    private static final String DEFAULT_PLANT = "/textures/plants/antidotes/grass.png";
    private static final String DEFAULT_POISON = "/textures/plants/poisons/lily.png";

    private final Map<String, Image> imageCache = new HashMap<>();
    private final Canvas canvas;
    private final int maxEnergy;

    public MapDrafter(Canvas canvas, int maxEnergy) {
        this.canvas = canvas;
        this.maxEnergy = maxEnergy;
    }

    public void drawMap(RectangularMap worldMap) {
        clearCanvas();

        Boundary boundary = worldMap.getMapBounds();
        Vector2D lowerLeft = boundary.lowerLeft();
        Vector2D upperRight = boundary.upperRight();
        Vector2D cellCount = upperRight.subtract(lowerLeft);

        int cellSize = calculateCellSize(upperRight.getX() - lowerLeft.getX() + 1, upperRight.getY() - lowerLeft.getY() + 1);
        int canvasWidth = ((int) cellCount.getX() + 2) * cellSize + BORDER_WIDTH;
        int canvasHeight = ((int) cellCount.getY() + 2) * cellSize + BORDER_WIDTH;
        int fontSize = Math.max(8, cellSize / 2);

        canvas.setWidth(canvasWidth);
        canvas.setHeight(canvasHeight);

        clearCanvas();

        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setStroke(Color.BLACK);
        graphics.setLineWidth(BORDER_WIDTH);

        Boundary jungleBounds = worldMap.getJungleBoundary();

        int yCellValue = upperRight.getY();
        for (int y = cellSize; y < canvasHeight - cellSize + 1; y += cellSize) {
            int xCellValue = lowerLeft.getX();
            for (int x = cellSize; x < canvasWidth - cellSize + 1; x += cellSize) {
                Vector2D currentPos = new Vector2D(xCellValue, yCellValue);

                drawJungle(currentPos, jungleBounds, graphics, x, y, cellSize);
                drawMapObjects(currentPos, graphics, x, cellSize, y, worldMap);

                xCellValue++;
            }
            yCellValue--;
        }

        drawGrid(graphics, canvasWidth, canvasHeight, cellSize);
        drawAxisLabels(graphics, fontSize, lowerLeft, upperRight, canvasWidth, canvasHeight, cellSize);
    }

    private void drawGrid(GraphicsContext graphics, int canvasWidth, int canvasHeight, int cellSize) {
        for (int x = 0; x < canvasWidth + 1; x += cellSize) {
            graphics.strokeLine(x + BORDER_OFFSET, 0, x + BORDER_OFFSET, canvasHeight);
        }
        for (int y = 0; y < canvasHeight + 1; y += cellSize) {
            graphics.strokeLine(0, y + BORDER_OFFSET, canvasWidth, y + BORDER_OFFSET);
        }
    }

    private void drawAxisLabels(GraphicsContext graphics, int fontSize, Vector2D lowerLeft, Vector2D upperRight,
                                int canvasWidth, int canvasHeight, int cellSize) {
        configureFont(graphics, fontSize, Color.BLACK);
        int xCellValue = lowerLeft.getX();
        graphics.fillText("y/x", cellSize / 2.0, cellSize / 2.0);

        for (int x = cellSize / 2 + cellSize; x < canvasWidth + 1; x += cellSize) {
            graphics.fillText(String.valueOf(xCellValue), x, cellSize / 2.0);
            xCellValue++;
        }

        int yCellValue = upperRight.getY();
        for (int y = cellSize / 2 + cellSize; y < canvasHeight + 1; y += cellSize) {
            graphics.fillText(String.valueOf(yCellValue), cellSize / 2.0, y);
            yCellValue--;
        }
    }

    private void drawMapObjects(Vector2D currentPos, GraphicsContext graphics, int x, int cellSize, int y, RectangularMap worldMap) {
        var animals = worldMap.getAnimalsAt(currentPos);
        if (!animals.isEmpty()) {
            animals.sort(AnimalComparator.getComparator());
            Image animalTexture = loadImage(ANIMAL_TEXTURE);
            if (animalTexture != null) {
                graphics.save();
                graphics.translate(x + BORDER_OFFSET + (cellSize - BORDER_WIDTH) / 2.0, y + BORDER_OFFSET + (cellSize - BORDER_WIDTH) / 2.0);
                graphics.rotate(animals.getFirst().getCurrDirection().rotateDegrees());
                double size = cellSize - BORDER_WIDTH;
                graphics.drawImage(animalTexture, -size / 2, -size / 2, size, size);
                graphics.restore();
            }
            drawEnergyBar(graphics, animals.getFirst(), x, y, cellSize);

        } else {
            WorldElement plant = worldMap.getPlantAt(currentPos);
            if (plant != null) {
                Image plantTexture = getTextureForElement(plant);
                if (plantTexture != null) {
                    graphics.drawImage(plantTexture, x, y, cellSize, cellSize);
                }
            }
        }
    }

    private void drawJungle(Vector2D currentPos, Boundary jungleBounds, GraphicsContext graphics, int x, int y, int cellSize) {
        boolean isInJungle = currentPos.follows(jungleBounds.lowerLeft()) && currentPos.precedes(jungleBounds.upperRight());
        Image biomeTexture = loadImage(isInJungle ? JUNGLE_BIOME : STEPPE_BIOME);
        if (biomeTexture != null) {
            graphics.drawImage(biomeTexture, x, y, cellSize, cellSize);
        }
    }

    private void drawEnergyBar(GraphicsContext graphics, Animal animal, int cellX, int cellY, int cellSize) {
        int barHeight = Math.max(2, cellSize / 10);

        double barY = cellY + cellSize - barHeight - BORDER_OFFSET;
        double barX = cellX + BORDER_OFFSET;
        double barWidth = cellSize - 2 - BORDER_WIDTH;

        double energyRatio = Math.min(animal.getEnergy() / (double) maxEnergy, 1.0);
        double filledWidth = barWidth * energyRatio;

        graphics.setFill(Color.WHITE);
        graphics.fillRect(barX, barY, barWidth, barHeight);

        Color barColor = energyRatio > 0.5 ? Color.GREEN : energyRatio > 0.25 ? Color.YELLOW : Color.RED;
        graphics.setFill(barColor);
        graphics.fillRect(barX, barY, filledWidth, barHeight);
    }

    private void clearCanvas() {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(Color.WHITE);
        graphics.fillRect(0, 0, canvas.getWidth() + 100, canvas.getHeight() + 100);
    }

    private int calculateCellSize(int mapWidth, int mapHeight) {
        int maxCanvasWidth = 800;
        int maxCanvasHeight = 600;

        int cellByWidth = maxCanvasWidth / (mapWidth + 2);
        int cellByHeight = maxCanvasHeight / (mapHeight + 2);

        int cellSize = Math.min(cellByWidth, cellByHeight);
        return Math.max(cellSize, 5);
    }

    private void configureFont(GraphicsContext graphics, int size, Color color) {
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.setFont(new Font("Montserrat", size));
        graphics.setFill(color);
    }

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
        } else if (element instanceof Antidote antidote) {
            return loadImage(antidote.getType().getTexturePath());
        } else if (element instanceof Poison poison) {
            return loadImage(poison.getType().getTexturePath());
        } else if (element instanceof Plant plant) {
            if (plant.getEnergy() > 0) {
                return loadImage(DEFAULT_PLANT);
            } else {
                return loadImage(DEFAULT_POISON);
            }
        }
        return null;
    }
}
