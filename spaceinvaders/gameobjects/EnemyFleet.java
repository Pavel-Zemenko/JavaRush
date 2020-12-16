package javarush.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;

    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet() {
        createShips();
    }

    private void createShips() {
        ships = new ArrayList<>();

        for (int row = 0; row < ROWS_COUNT; row++) {
            for (int col = 0; col < COLUMNS_COUNT; col++) {
                ships.add(new EnemyShip(col * STEP, row * STEP + 12));
            }
        }
    }

    public void draw(Game game) {
        ships.forEach(ship -> ship.draw(game));
    }

    private double getLeftBorder() {
        List<Double> list = new ArrayList<>();
        ships.stream()
                .min(Comparator.comparingDouble(o -> o.x))
                .ifPresent(enemyShip -> list.add(enemyShip.x));
        return list.get(0);
    }

    private double getRightBorder() {
        List<Double> list = new ArrayList<>();
        ships.stream()
                .max(Comparator.comparingDouble(o -> o.x + o.width))
                .ifPresent(enemyShip -> list.add(enemyShip.x + enemyShip.width));
        return list.get(0);
    }

    private double getSpeed() {
        return Math.min(2.0, (3.0 / ships.size()));
    }

    public void move() {
        if (!ships.isEmpty()) {
            double speed = getSpeed();

            if (direction == Direction.LEFT && getLeftBorder() < 0) {
                direction = Direction.RIGHT;
                ships.forEach(ship -> ship.move(Direction.DOWN, speed));
            }

            if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
                direction = Direction.LEFT;
                ships.forEach(ship -> ship.move(Direction.DOWN, speed));
            }

            ships.forEach(ship -> ship.move(direction, speed));
        }
    }

}
