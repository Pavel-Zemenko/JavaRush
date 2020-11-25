package javarush.minesweeper;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinesweeperGame extends Game {
    private static final int SIDE = 9;
    private static final String MINE = "\uD83D\uDCA3";
    private static final String FLAG = "\uD83D\uDEA9";

    private GameObject[][] gameField = new GameObject[SIDE][SIDE];
    private int countMinesOnField;
    private int countClosedTiles = SIDE * SIDE;
    private int countFlags;
    private int score;
    private boolean isGameStopped;

    @Override
    public void initialize() {
        setScreenSize(SIDE, SIDE);
        createGame();
    }

    private void createGame() {
        clean();
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;
                if (isMine) {
                    countMinesOnField++;
                }
                gameField[y][x] = new GameObject(x, y, isMine);
                setCellColor(x, y, Color.GREY);
            }
        }
        countMineNeighbors();
        countFlags = countMinesOnField;
    }

    private void restart() {
        isGameStopped = false;
        countMinesOnField = 0;
        countClosedTiles = SIDE * SIDE;
        setScore(score = 0);
        createGame();
    }

    private void clean() {
        for (int y = 0; y < gameField.length; y++) {
            for (int x = 0; x < gameField[y].length; x++) {
                setCellValue(x, y, "");
            }
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {
        List<GameObject> result = new ArrayList<>();
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {
                if (y < 0 || y >= SIDE) {
                    continue;
                }
                if (x < 0 || x >= SIDE) {
                    continue;
                }
                if (gameField[y][x] == gameObject) {
                    continue;
                }
                result.add(gameField[y][x]);
            }
        }
        return result;
    }

    private void countMineNeighbors() {
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject cell = gameField[y][x];
                if (!cell.isMine) {
                    cell.countMineNeighbors = getNeighbors(cell)
                            .stream()
                            .filter(e -> e.isMine)
                            .collect(Collectors.toList())
                            .size();
                }
            }
        }
    }

    private void openTile(int x, int y) {
        GameObject cell = gameField[y][x];

        if (isGameStopped || cell.isOpen || cell.isFlag) {
            return;
        }

        cell.isOpen = true;
        countClosedTiles--;

        if (cell.isMine) {
            setCellValueEx(x, y, Color.RED, MINE);
            gameOver();
        } else {
            setCellColor(x, y, Color.GREEN);
            setScore(score += 5);
            if (cell.countMineNeighbors == 0) {
                getNeighbors(cell)
                        .stream()
                        .filter(e -> !e.isOpen)
                        .forEach(e -> openTile(e.x, e.y));
                setCellValue(x, y, "");
            } else {
                setCellNumber(x, y, cell.countMineNeighbors);
            }
            if (countClosedTiles == countMinesOnField) {
                win();
            }
        }
    }

    private void markTile(int x, int y) {
        GameObject gameObject = gameField[y][x];

        if (isGameStopped || gameObject.isOpen || (!gameObject.isFlag && countFlags == 0))
            return;

        if (!gameObject.isFlag) {
            gameObject.isFlag = true;
            countFlags--;
            setCellValue(x, y, FLAG);
            setCellColor(x, y, Color.LIGHTGREEN);
        } else {
            gameObject.isFlag = false;
            countFlags++;
            setCellValue(x, y, "");
            setCellColor(x, y, Color.GREY);
        }
    }

    private void gameOver() {
        isGameStopped = true;
        showMessageDialog(Color.TRANSPARENT, "ПОТРАЧЕНО", Color.WHITE, 110);
    }

    private void win() {
        isGameStopped = true;
        showMessageDialog(Color.TRANSPARENT, "ПОБЕДА!", Color.RED, 120);
    }

    @Override
    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {
            restart();
        } else {
            openTile(x, y);
        }
    }

    @Override
    public void onMouseRightClick(int x, int y) {
        markTile(x, y);
    }
}