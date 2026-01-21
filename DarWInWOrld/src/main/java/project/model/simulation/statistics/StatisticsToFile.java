package project.model.simulation.statistics;

import project.model.worldelements.animals.Genome;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StatisticsToFile {
    private PrintWriter statsWriter;
    private String statsFileName;

    public void initializeStatsFile() throws IOException {
        Path statsDir = Paths.get("simulationstats");
        if (!Files.exists(statsDir)) {
            Files.createDirectories(statsDir);
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        statsFileName = String.format("simulationstats/simulation_stats_%s.csv", timestamp);
        statsWriter = new PrintWriter(new FileWriter(statsFileName, true));
    }

    private void writeStatsHeader() {
        String header = "Dzień,Liczba zwierząt,Liczba roślin,Wolne pola,Zmarłe zwierzęta,Śr. energia,Śr. potomstwo,Śr. długość życia,Najczęstszy genom";
        statsWriter.println(header);
    }

    public void saveDailyStats(int dayNumber, SimulationStatistics stats) {
        if (statsWriter == null) {
            System.err.println("Błąd: Plik CSV nie został zainicjalizowany!");
            return;
        }

        try {
            writeStatsHeader();

            String statsLine = dayNumber + "," +
                    stats.getNumberOfAnimals() + "," +
                    stats.getNumberOfPlants() + "," +
                    stats.getNumberOfNotOccupiedFields() + "," +
                    stats.getDeadAnimals() + "," +
                    String.format("%.2f", stats.getAverageEnergyLevel()) + "," +
                    String.format("%.2f", stats.getAverageAmountOfChildren()) + "," +
                    String.format("%.2f", stats.getAverageLifespan()) + "," +
                    Genome.formatGenome(stats.getMostPopularGenes());

            statsWriter.println(statsLine);
            statsWriter.flush();

        } catch (Exception e) {
            System.err.println("Błąd podczas zapisu do CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeStatsFile() {
        if (statsWriter != null) {
            statsWriter.close();
            System.out.printf("Plik ze statystykami: %s został zapisany%n", statsFileName);
        }
    }
}
