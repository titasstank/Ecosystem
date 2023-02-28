
public class Main {
//    public static void main(String[] args) {
//
//        Environment forest = new Environment(15);
//        forest.println();
//        System.out.println();
//
//        Environment jungle = new Environment(20, 30);
//        jungle.println();
//        System.out.println();
//        jungle.setMaxPlantAmount(40);
//        jungle.println();
//        System.out.println();
//
//        Environment desert = new Environment();
//        desert.setMaxPlantAmount(5);
//        desert.println();
//
//        System.out.println();
//        String[] animals = Animal.getAnimalList();
//        System.out.println("Animals:");
//        for(int i = 0; i < Animal.availableAnimalNameAmount; ++i) {
//            System.out.println(animals[i]);
//        }
//    }

    public static void main(String[] args) {

        Environment testEnvironment = new Environment(20, 10);
        Animal testAnimal1 = new Animal(testEnvironment, 3, 19, 2, 10);
        testAnimal1.move("down");
        testAnimal1.println();
        testAnimal1.move();
        testAnimal1.move();
        testAnimal1.move();
        testAnimal1.println();


        Animal testAnimal2 = new Animal();
        testAnimal2.println();

        Animal testAnimal3 = new Animal(null, 5, 5, 10, 20);
        testAnimal3.println();
        testAnimal3.setSpeed(1);
        testAnimal3.setHealth(5);
        System.out.println("New Speed: " + testAnimal3.getSpeed() + "\nNew Health: " + testAnimal3.getHealth());

        System.out.println("Total animals: " + Animal.getAnimalCount());
    }
}