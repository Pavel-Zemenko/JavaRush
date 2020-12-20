package javarush.spaceinvaders.gameobjects;

import javarush.spaceinvaders.Direction;
import javarush.spaceinvaders.ShapeMatrix;

public class Boss extends EnemyShip {
    private int frameCount = 0;

    public Boss(double x, double y) {
        super(x, y);
        setAnimatedView(true,
                ShapeMatrix.BOSS_ANIMATION_FIRST,
                ShapeMatrix.BOSS_ANIMATION_SECOND);
        score = 100;
    }

    @Override
    public void nextFrame() {
        if (++frameCount % 10 == 0 || !isAlive) {
            super.nextFrame();
        }
    }

    @Override
    public Bullet fire() {
        if (isAlive) {
            double x = (matrix == ShapeMatrix.BOSS_ANIMATION_FIRST) ? this.x + 6 : this.x;
            return new Bullet(x, y + height, Direction.DOWN);
        }
        return null;
    }

    @Override
    public void kill() {
        if (isAlive) {
            isAlive = false;
            setAnimatedView(false,
                    ShapeMatrix.KILL_BOSS_ANIMATION_FIRST,
                    ShapeMatrix.KILL_BOSS_ANIMATION_SECOND,
                    ShapeMatrix.KILL_BOSS_ANIMATION_THIRD);
        }
    }
}
