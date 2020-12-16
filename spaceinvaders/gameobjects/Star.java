package javarush.spaceinvaders.gameobjects;

import com.javarush.engine.cell.*;

import java.util.Objects;

public class Star extends GameObject {
    private static final String STAR_SIGN = "\u2605";

    public Star(double x, double y) {
        super(x, y);
    }

    public void draw(Game game) {
        game.setCellValueEx((int) x, (int) y, Color.NONE, STAR_SIGN, Color.WHITE, 100);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, STAR_SIGN);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Star)) return false;
        return Objects.equals(x, ((Star) obj).x) &&
                Objects.equals(y, ((Star) obj).y);
    }
}
