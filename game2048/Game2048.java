package javarush.game2048;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;

import java.util.Arrays;

public class Game2048 extends Game {
    private static final int SIDE = 4;

    private int[][] gameField = new int[SIDE][SIDE];
    private boolean isGameStopped = false;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
        drawScene();

    }

    private void createGame() {
        gameField = new int[SIDE][SIDE];
        setScore(score = 0);
        createNewNumber();
        createNewNumber();
    }

    private void drawScene() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);
            }
        }
    }

    private void createNewNumber() {
        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);

        if (getMaxTileValue() == 2048) {
            win();
            return;
        }

        if (gameField[y][x] == 0) {
            gameField[y][x] = (getRandomNumber(10) == 1) ? 4 : 2;
        } else
            createNewNumber();
    }

    private int getMaxTileValue() {
        int maxValue = 0;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] > maxValue) {
                    maxValue = gameField[y][x];
                }
            }
        }
        return maxValue;
    }

    private Color getColorByValue(int value) {
        switch (value) {
            case    0 : return Color.LAVENDER;
            case    2 : return Color.BEIGE;
            case    4 : return Color.AQUA;
            case    8 : return Color.YELLOWGREEN;
            case   16 : return Color.LIGHTPINK;
            case   32 : return Color.LIGHTGREEN;
            case   64 : return Color.LIGHTSALMON;
            case  128 : return Color.AQUAMARINE;
            case  256 : return Color.BISQUE;
            case  512 : return Color.BLANCHEDALMOND;
            case 1024 : return Color.OLIVE;
            case 2048 : return Color.VIOLET;
            default   : throw new IllegalArgumentException("Incorrect value: " + value);
        }
    }

    private void setCellColoredNumber(int x, int y, int value) {
        Color color = getColorByValue(value);
        String number = (value == 0) ? "" : String.valueOf(value);
        setCellValueEx(x, y, color, number);
    }

    private boolean compressRow(int[] row) {
        int[] tempRow = new int[SIDE];
        int counter = 0;
        for (int i = 0; i < SIDE; i++) {
            if (row[i] > 0) {
                tempRow[counter++] = row[i];
            }
        }
        if (!Arrays.equals(row, tempRow)) {
            System.arraycopy(tempRow, 0, row, 0, SIDE);
            return true;
        } else {
            return false;
        }
    }

    private boolean mergeRow(int[] row) {
        int[] tempRow = new int[SIDE];
        boolean isMerged = false;
        for (int i = 0; i < SIDE; i++) {
            if (i == SIDE - 1 || row[i] != row[i + 1]) {
                tempRow[i] = row[i];
            } else if (row[i] != 0) {
                score += tempRow[i] = row[i++] * 2;
                setScore(score);
                isMerged = true;
            }
        }
        if (isMerged) {
            System.arraycopy(tempRow, 0, row, 0, SIDE);
        }
        return isMerged;
    }

    private void moveLeft() {
        boolean isModified = false;
        for (int[] row : gameField) {
            if (compressRow(row) | mergeRow(row) | compressRow(row)) {
                isModified = true;
            }
        }
        if (isModified) {
            createNewNumber();
        }
    }

    private void moveRight() {
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
    }

    private void moveUp() {
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {
        int[][] tempMatrix = new int[SIDE][SIDE];
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                tempMatrix[y][x] = gameField[SIDE - 1 - x][y];
            }
        }
        for (int i = 0; i < SIDE; i++) {
            System.arraycopy(tempMatrix[i], 0, gameField[i], 0, SIDE);
        }
    }

    private boolean canUserMove() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0 ||
                        (x + 1 <= SIDE - 1) && gameField[y][x] == gameField[y][x + 1] ||
                        (y + 1 <= SIDE - 1) && gameField[y][x] == gameField[y + 1][x]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.NONE, "ХОДОВ НЕТ", Color.ORANGERED, 100);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.NONE, "ПОБЕДА!", Color.GREEN, 100);
    }

    @Override
    public void onKeyPress(Key key) {
        if (isGameStopped) {
            if (key == Key.SPACE) {
                isGameStopped = false;
                createGame();
                drawScene();
            }
            return;
        }

        if (!canUserMove()) {
            gameOver();
            return;
        }

        switch (key) {
            case LEFT  : moveLeft();
                break;
            case RIGHT : moveRight();
                break;
            case UP    : moveUp();
                break;
            case DOWN  : moveDown();
                break;
            default    : return;
        }
        drawScene();
    }
}
