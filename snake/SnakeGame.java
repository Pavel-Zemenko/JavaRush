package javarush.snake;

import com.javarush.engine.cell.*;

public class SnakeGame extends Game {
    public static final int WIDTH = 15;
    public static final int HEIGHT = 15;
    private static final int GOAL = 28;

    private Snake snake;
    private Apple apple;
    private int turnDelay;
    private int score;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        isGameStopped = false;
        setTurnTimer(turnDelay = 300);
        setScore(score = 0);
        snake = new Snake(WIDTH / 2, HEIGHT / 2);
        createNewApple();
        drawScene();
    }

    private void drawScene() {
        for (int y = 0; y < WIDTH; y++) {
            for (int x = 0; x < HEIGHT; x++) {
                setCellValueEx(x, y, Color.DARKSEAGREEN, "");
            }
        }
        snake.draw(this);
        apple.draw(this);
    }

    private void createNewApple() {
        do {
            apple = new Apple(getRandomNumber(WIDTH), getRandomNumber(HEIGHT));
        } while (snake.checkCollision(apple));
    }

    private void gameOver() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "ПОТРАЧЕНО", Color.WHITE, 100);
    }

    private void win() {
        stopTurnTimer();
        isGameStopped = true;
        showMessageDialog(Color.NONE, "ПОБЕДА!", Color.WHITE, 100);
    }

    @Override
    public void onTurn(int step) {
        snake.move(apple);
        if (!apple.isAlive) {
            setScore(score += 5);
            setTurnTimer(turnDelay -= 10);
            createNewApple();
        }
        if (!snake.isAlive) gameOver();
        if (snake.getLength() > GOAL) win();
        drawScene();
    }

    @Override
    public void onKeyPress(Key key) {
        switch (key) {
            case SPACE : if (isGameStopped) createGame();
                break;
            case LEFT  : snake.setDirection(Direction.LEFT);
                break;
            case RIGHT : snake.setDirection(Direction.RIGHT);
                break;
            case UP    : snake.setDirection(Direction.UP);
                break;
            case DOWN  : snake.setDirection(Direction.DOWN);
        }
    }
}
