package javarush.spaceinvaders.gameobjects;

import com.javarush.engine.cell.*;

public class GameObject {
    public int[][] matrix;
    public int width;
    public int height;
    public double x;
    public double y;

    public GameObject(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
        width = matrix[0].length;
        height = matrix.length;
    }

    public void draw(Game game) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = Color.values()[matrix[y][x]];
                game.setCellValueEx((int) this.x + x, (int) this.y + y, color, "");
            }
        }
    }
}
