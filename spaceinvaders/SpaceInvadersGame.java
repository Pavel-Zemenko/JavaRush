package javarush.spaceinvaders;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import javarush.spaceinvaders.gameobjects.Bullet;
import javarush.spaceinvaders.gameobjects.EnemyFleet;
import javarush.spaceinvaders.gameobjects.PlayerShip;
import javarush.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;
    private static final int PLAYER_BULLETS_MAX = 1;

    private List<Star> stars;
    private List<Bullet> enemyBullets;
    private List<Bullet> playerBullets;
    private EnemyFleet enemyFleet;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        createStars();
        playerShip = new PlayerShip();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerBullets = new ArrayList<>();
        isGameStopped = false;
        animationsCount = 0;
        score = 0;
        drawScene();
        setTurnTimer(40);
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.NONE, "ПОБЕДА!", Color.GREEN, 100);
        } else {
            showMessageDialog(Color.NONE, "ВЫ ПРОИГРАЛИ", Color.RED, 80);
        }
    }

    private void stopGameWithDelay() {
        if (++animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    private void drawScene() {
        drawField();
        playerShip.draw(this);
        enemyFleet.draw(this);
        enemyBullets.forEach(bullet -> bullet.draw(this));
        playerBullets.forEach(bullet -> bullet.draw(this));
    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        stars.forEach(star -> star.draw(this));
    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            Star star = new Star(x, y);
            if (!stars.contains(star)) {
                stars.add(star);
            } else {
                i--;
            }
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            super.setCellValueEx(x, y, cellColor, value);
        }
    }

    @Override
    public void onTurn(int step) {
        setScore(score);
        moveSpaceObjects();
        check();
        Bullet bullet = enemyFleet.fire(this);
        if (bullet != null) enemyBullets.add(bullet);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case SPACE : if (isGameStopped) createGame();
                         else {
                             Bullet bullet = playerShip.fire();
                             if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX)
                                 playerBullets.add(bullet);
                         }
                break;
            case LEFT  : playerShip.setDirection(Direction.LEFT);
                break;
            case RIGHT : playerShip.setDirection(Direction.RIGHT);
                break;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key) {
            case LEFT  : if (playerShip.getDirection() == Direction.LEFT)
                playerShip.setDirection(Direction.UP);
                break;
            case RIGHT : if (playerShip.getDirection() == Direction.RIGHT)
                playerShip.setDirection(Direction.UP);
        }
    }

    private void moveSpaceObjects() {
        enemyFleet.move();
        enemyBullets.forEach(Bullet::move);
        playerBullets.forEach(Bullet::move);
        playerShip.move();
    }

    private void removeDeadBullets() {
        enemyBullets.removeIf(bullet -> !bullet.isAlive || (bullet.y >= HEIGHT - 1));
        playerBullets.removeIf(bullet -> !bullet.isAlive || (bullet.y + bullet.height < 0));
    }

    private void check() {
        if (!playerShip.isAlive)
            stopGameWithDelay();
        if (enemyFleet.getBottomBorder() >= playerShip.y)
            playerShip.kill();
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
        playerShip.verifyHit(enemyBullets);
        score += enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
    }

}
