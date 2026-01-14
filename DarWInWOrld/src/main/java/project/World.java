package project;

import project.model.RectangularMap;
import project.model.Vector2D;
import project.model.WorldElements.Animal;
import project.model.WorldElements.EdibleElements.Antidote;
import project.model.WorldElements.EdibleElements.Plant;
import project.model.WorldElements.EdibleElements.Poison;
import project.model.WorldElements.EdibleElements.TypeOfPoison;
import project.model.WorldElements.WorldElement;

import java.util.List;
import java.util.Random;

public class World {
    public static void main(String[] args) {
        RectangularMap map = new RectangularMap(2, 2);

        Animal[] animals = new Animal[5];
        Plant[] plantArray = new Plant[10];


        for (int i = 0; i < animals.length; i++) {
            animals[i] = new Animal();
            map.place(animals[i]);
        }

        Random random = new Random();
        for (int i = 0; i < plantArray.length; i++) {
            int antidoteType = random.nextInt(5);
            plantArray[i] = new Antidote(new Vector2D(0, 0), antidoteType);
            map.placePlant(plantArray[i]);
        }

        for(int i = 0; i < 100; i++) {
            map.move(animals[i % animals.length]);
        }
    }
}
