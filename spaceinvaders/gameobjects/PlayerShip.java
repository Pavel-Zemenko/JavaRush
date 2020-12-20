package javarush.spaceinvaders.gameobjects;

import javarush.spaceinvaders.Direction;
import javarush.spaceinvaders.ShapeMatrix;
import javarush.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {
    private Direction direction = Direction.UP;

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2.0,
                SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
        setStaticView(ShapeMatrix.PLAYER);
    }

    public void setDirection(Direction newDirection) {
        if (newDirection != Direction.DOWN) {
            direction = newDirection;
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void verifyHit(List<Bullet> bullets) {
        if (isAlive && !bullets.isEmpty()) {
            for (Bullet bullet : bullets) {
                if (bullet.isAlive && isCollision(bullet)) {
                    this.kill();
                    bullet.kill();
                }
            }
        }
    }

    @Override
    public void kill() {
        if (isAlive) {
            isAlive = false;
            setAnimatedView(false,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND,
                    ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD,
                    ShapeMatrix.DEAD_PLAYER);
        }
    }

    public void move() {
        if (isAlive) {
            switch (direction) {
                case LEFT  : x--;
                    break;
                case RIGHT : x++;
            }
            if (x < 0) x = 0;
            if (x + width > SpaceInvadersGame.WIDTH) x = SpaceInvadersGame.WIDTH - width;
        }
    }

    @Override
    public Bullet fire() {
        if (isAlive) {
            return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
        } else {
            return null;
        }
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
