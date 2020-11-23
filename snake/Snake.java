package javarush.snake;

import com.javarush.engine.cell.*;

import java.util.ArrayList;
import java.util.List;

public class Snake extends GameObject {
    private static final String HEAD_SIGN = "\uD83D\uDC7E";
    private static final String BODY_SIGN = "\u26AB";

    private List<GameObject> snakeParts = new ArrayList<>();
    private Direction direction = Direction.LEFT;
    public boolean isAlive = true;

    public Snake(int x, int y) {
        super(x, y);
        for (int i = 0; i < 3; i++) {
            snakeParts.add(new GameObject(x + i, y));
        }
    }

    public void draw(Game game) {
        Color snakeColor = (isAlive) ? Color.GREEN : Color.RED;
        for (int i = 0; i < snakeParts.size(); i++) {
            GameObject gameObject = snakeParts.get(i);
            String sign = (i == 0) ? HEAD_SIGN : BODY_SIGN;
            game.setCellValueEx(gameObject.x, gameObject.y, Color.NONE, sign, snakeColor, 75);
        }
    }

    public void setDirection(Direction direction) {
        GameObject head = snakeParts.get(0);
        GameObject body = snakeParts.get(1);
        switch (this.direction) {
            case UP    :
            case DOWN  : if (head.y == body.y) return;
                break;
            case LEFT  :
            case RIGHT : if (head.x == body.x) return;
        }
        switch (direction) {
            case UP    : if (this.direction == Direction.DOWN) return;
                break;
            case DOWN  : if (this.direction == Direction.UP) return;
                break;
            case LEFT  : if (this.direction == Direction.RIGHT) return;
                break;
            case RIGHT : if (this.direction == Direction.LEFT) return;
        }
        this.direction = direction;
    }

    public void move(Apple apple) {
        GameObject head = createNewHead();

        if (head.x >= 0 && head.x < SnakeGame.WIDTH
                && head.y >= 0 && head.y < SnakeGame.HEIGHT) {
            if (checkCollision(head)) {
                isAlive = false;
                return;
            } else {
                snakeParts.add(0, head);
            }

            if (head.x == apple.x && head.y == apple.y) {
                apple.isAlive = false;
            } else {
                removeTail();
            }

        } else {
            isAlive = false;
        }
    }

    public GameObject createNewHead() {
        GameObject head = snakeParts.get(0);
        int x = head.x;
        int y = head.y;
        switch (direction) {
            case UP    : y = head.y - 1;
                break;
            case DOWN  : y = head.y + 1;
                break;
            case RIGHT : x = head.x + 1;
                break;
            case LEFT  : x = head.x - 1;
        }
        return new GameObject(x, y);
    }

    public void removeTail() {
        snakeParts.remove(snakeParts.size() - 1);
    }

    public boolean checkCollision(GameObject object) {
        for (GameObject part : snakeParts) {
            if (object.x == part.x && object.y == part.y) {
                return true;
            }
        }
        return false;
    }

    public int getLength() {
        return snakeParts.size();
    }

}