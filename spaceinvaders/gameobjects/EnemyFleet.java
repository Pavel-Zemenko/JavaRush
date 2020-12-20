package javarush.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import javarush.spaceinvaders.Direction;
import javarush.spaceinvaders.ShapeMatrix;
import javarush.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
        Boss boss = new Boss((double) STEP * COLUMNS_COUNT / 2
                - (double) ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5);
        ships.add(boss);
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

    public double getBottomBorder() {
        if (ships.isEmpty()) return 0;
        List<Double> list = new ArrayList<>();
        ships.stream()
                .max(Comparator.comparingDouble(o -> o.y + o.height))
                .ifPresent(enemyShip -> list.add(enemyShip.y + enemyShip.height));
        return list.get(0);
    }

    private double getSpeed() {
        return Math.min(2.0, (3.0 / ships.size()));
    }

    public int getShipsCount() {
        return ships.size();
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

    public Bullet fire(Game game) {
        if (ships.isEmpty() || game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) {
            return null;
        }
        int shipIndex = game.getRandomNumber(ships.size());
        return ships.get(shipIndex).fire();
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.isEmpty()) return 0;
        AtomicInteger sumScore = new AtomicInteger(0);
        ships.forEach(ship -> bullets.forEach(bullet -> {
            if (ship.isAlive && bullet.isAlive && bullet.isCollision(ship)) {
                ship.kill();
                bullet.kill();
                sumScore.addAndGet(ship.score);
            }
        }));
        return sumScore.get();
    }

    public void deleteHiddenShips() {
        ships.removeIf(ship -> !ship.isVisible());
    }

}
