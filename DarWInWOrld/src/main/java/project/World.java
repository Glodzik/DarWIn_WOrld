package project;

import project.model.*;

public class World {
    public static void main(String[] args) {
        Animal animal = new Animal();
        RectangularMap map = new RectangularMap(20, 20);

        animal.move();
        System.out.println("Animal: " + animal.getPosition() + " " + animal.getCurrDirection());
        animal.move();
        System.out.println("Animal: " + animal.getPosition() + " " + animal.getCurrDirection());
        animal.move();
        System.out.println("Animal: " + animal.getPosition() + " " + animal.getCurrDirection());

        map.place(animal);
        map.move(animal);
        map.move(animal);
        map.move(animal);
    }
}
