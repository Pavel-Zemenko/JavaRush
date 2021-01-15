package javarush.racer;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import javarush.racer.road.RoadManager;

public class RacerGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int CENTER_X = WIDTH / 2;
    public static final int ROADSIDE_WIDTH = 14;
    private static final int RACE_GOAL_CARS_COUNT = 40;

    private RoadManager roadManager;
    private RoadMarking roadMarking;
    private PlayerCar player;
    private FinishLine finishLine;
    private ProgressBar progressBar;
    private boolean isGameStopped;
    private int score;

    @Override
    public void initialize() {
        showGrid(false);
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        setTurnTimer(40);
        roadManager = new RoadManager();
        roadMarking = new RoadMarking();
        player = new PlayerCar();
        finishLine = new FinishLine();
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        isGameStopped = false;
        score = 3500;
        drawScene();
    }

    private void drawScene() {
        drawField();
        roadMarking.draw(this);
        finishLine.draw(this);
        roadManager.draw(this);
        player.draw(this);
        progressBar.draw(this);
    }

    private void drawField() {
        Color color;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (x == CENTER_X) {
                    color = Color.WHITE;
                } else if (x >= ROADSIDE_WIDTH && x < (WIDTH - ROADSIDE_WIDTH)) {
                    color = Color.DIMGRAY;
                } else {
                    color = Color.GREEN;
                }
                setCellColor(x, y, color);
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            super.setCellColor(x, y, color);
        }
    }

    @Override
    public void onTurn(int step) {
        if (finishLine.isCrossed(player)) {
            win();
        } else {
            if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {
                finishLine.show();
            }
            if (roadManager.checkCrush(player)) {
                gameOver();
            } else {
                roadManager.generateNewRoadObjects(this);
                moveAll();
            }
        }
        setScore(score -= 5);
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case SPACE : if (isGameStopped) createGame();
                break;
            case LEFT  : player.setDirection(Direction.LEFT);
                break;
            case RIGHT : player.setDirection(Direction.RIGHT);
                break;
            case UP    : player.speed = 2;
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        switch (key) {
            case LEFT  : if (player.getDirection() == Direction.LEFT)
                player.setDirection(Direction.NONE);
                break;
            case RIGHT : if (player.getDirection() == Direction.RIGHT)
                player.setDirection(Direction.NONE);
                break;
            case UP    : player.speed = 1;
        }
    }

    private void moveAll() {
        roadMarking.move(player.speed);
        finishLine.move(player.speed);
        roadManager.move(player.speed);
        player.move();
        progressBar.move(roadManager.getPassedCarsCount());
    }

    private void gameOver() {
        isGameStopped = true;
        player.stop();
        showMessageDialog(Color.NONE, "GAME OVER", Color.RED, 100);
        stopTurnTimer();
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.NONE, "FINISH!", Color.WHITE, 100);
        stopTurnTimer();
    }
}
