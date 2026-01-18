package project.presenter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import project.model.Boundary;
import project.model.MapChangeListener;
import project.model.RectangularMap;
import project.model.Simulation.Simulation;
import project.model.Statistics.SimulationStatistics;
import project.model.Statistics.SimulationStatisticsTracker;
import project.model.Vector2D;
import project.model.WorldElements.Animals.Animal;
import project.model.WorldElements.Animals.AnimalComparator;
import project.model.WorldElements.EdibleElements.Antidote;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.Poison;
import project.model.WorldElements.WorldElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class MapPresenter implements MapChangeListener {
    private RectangularMap worldMap;
    private Simulation simulation;
    private Thread simulationThread;
    private SimulationStatisticsTracker statisticsTracker;

    @FXML
    private Canvas mapCanvas;
    @FXML
    private Button toggleButton;
    @FXML
    private Label animalCountLabel;

    @FXML
    private Label plantCountLabel;

    @FXML
    private Label freeFieldsLabel;

    @FXML
    private Label topGenotypeLabel;

    @FXML
    private Label avgEnergyLabel;

    @FXML
    private Label avgLifespanLabel;

    @FXML
    private Label avgChildrenLabel;
    @FXML
    private Label dayLabel;
    @FXML
    private void initialize() {
        toggleButton.setOnAction(event -> toggleSimulation());
    }

    private static final int BORDER_WIDTH = 2;
    private static final float BORDER_OFFSET = (float) BORDER_WIDTH / 2;

    // tekstury
    private static final String JUNGLE_BIOME = "/textures/biomes/grass_block_top_jungle.png";
    private static final String STEPPE_BIOME = "/textures/biomes/grass_block_top_steppe.png";
    private static final String ANIMAL_TEXTURE = "/textures/animal/animalUp.png";
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
        this.statisticsTracker = simulation.getStatisticsTracker();
        drawMap();
        startSimulation();
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

                drawJungle(currentPos, jungleBounds, graphics, x, y, cellSize);

                drawMapObjects(currentPos, graphics, x, cellSize, y);

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

    private void drawMapObjects(Vector2D currentPos, GraphicsContext graphics, int x, int cellSize, int y) {
        var animals = worldMap.getAnimalsAt(currentPos);
        if (!animals.isEmpty()) {
            animals.sort(AnimalComparator.getComparator());
            Image animalTexture = loadImage(ANIMAL_TEXTURE);
            if (animalTexture != null) {
                graphics.save();
                graphics.translate(x + BORDER_OFFSET + (cellSize - BORDER_WIDTH) / 2.0, y + BORDER_OFFSET + (cellSize - BORDER_WIDTH) / 2.0);
                graphics.rotate(animals.getFirst().getCurrDirection().rotateDegrees());
                double size = cellSize - BORDER_WIDTH;
                graphics.drawImage(animalTexture, -size/2, -size/2, size, size);
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
        double maxEnergy = simulation.getStartEnergy();

        double barY = cellY + cellSize - barHeight - BORDER_OFFSET;
        double barX = cellX + BORDER_OFFSET;
        double barWidth = cellSize - 2 - BORDER_WIDTH;

        double energyRatio = Math.min(animal.getEnergy() / maxEnergy, 1.0);
        double filledWidth = barWidth * energyRatio;

        graphics.setFill(Color.WHITE);
        graphics.fillRect(barX, barY, barWidth, barHeight);

        Color barColor = energyRatio > 0.5 ? Color.GREEN : energyRatio > 0.25 ? Color.YELLOW : Color.RED;
        graphics.setFill(barColor);
        graphics.fillRect(barX, barY, filledWidth, barHeight);
    }

    @Override
    public void mapChanged(RectangularMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            updateStats();
            System.out.println(message);
        });
    }

    private void updateStats() {
        int day = simulation.getDay();
        statisticsTracker.updateStats();
        SimulationStatistics stats = statisticsTracker.getStatistics();

        dayLabel.setText(String.valueOf(day));
        animalCountLabel.setText(String.valueOf(stats.getNumberOfAnimals()));
        plantCountLabel.setText(String.valueOf(stats.getNumberOfPlants()));
        freeFieldsLabel.setText(String.valueOf(stats.getNumberOfNotOccupiedFields()));
        topGenotypeLabel.setText(Arrays.toString(stats.getMostPopularGenes()));
        avgChildrenLabel.setText(String.format("%.2f", stats.getAverageAmountOfChildren()));
        avgEnergyLabel.setText(String.format("%.2f", stats.getAverageEnergyLevel()));
        avgLifespanLabel.setText(String.format("%.2f", stats.getAverageLifespan()));
    }

    // turning simulation on/off

    private void toggleSimulation() {
        if (simulation == null) return;

        if (simulation.isRunning()) {
            stopSimulation();
            toggleButton.setText("START");
        } else {
            startSimulation();
            toggleButton.setText("STOP");
        }
    }

    private void startSimulation() {
        if (simulation == null) return;
        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
        simulation.start();
        simulationThread = new Thread(simulation);
        simulationThread.setDaemon(true);
        simulationThread.start();
    }

    private void stopSimulation() {
        if (simulation != null) {
            simulation.stop();
        }

        if (simulationThread != null && simulationThread.isAlive()) {
            simulationThread.interrupt();
        }
    }

    public void cleanup() {
        stopSimulation();
    }
}
