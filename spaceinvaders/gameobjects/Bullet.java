package javarush.spaceinvaders.gameobjects;


import com.javarush.games.spaceinvaders.*;

public class Bullet extends GameObject {
    public boolean isAlive = true;
    private int dy;

    public Bullet(double x, double y, Direction direction) {
        super(x, y);
        setMatrix(ShapeMatrix.BULLET);
        dy = (direction == Direction.UP) ? -1 : 1;
    }

    public void move () {
        y += dy;
    }

}
