package project;

import project.model.*;
import project.model.WorldElements.Animal;

public class World {
    public static void main(String[] args) {
        Animal animal = new Animal();
        RectangularMap map = new RectangularMap(2, 2);

        map.place(animal);

        for(int i = 0; i < 100; i++) {
            map.move(animal);
        }
    }
}
