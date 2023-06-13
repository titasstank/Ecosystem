package Entity;

public interface Carnivorous extends Movable {

    public boolean eatAnimal(Rabbit prey);
    public boolean eatAnimal(Animal prey);

}
