package Entity;

import Exceptions.EcosystemException;

public interface Movable {

    public void move();
    public void move(String direction) throws EcosystemException;
//    public void move(String direction, int amount);

    public int getX();
    public int getY();
}
