package javarush.moonlander;

import com.javarush.engine.cell.Game;

import java.util.List;

public class RocketFire extends GameObject {
    private List<int[][]> frames;
    private int frameIndex;
    private boolean isVisible;

    public RocketFire(List<int[][]> frames) {
        super(0, 0, frames.get(0));
        this.frames = frames;
        frameIndex = 0;
        isVisible = false;
    }

    private void nextFrame() {
        if (++frameIndex >= frames.size()) {
            frameIndex = 0;
        }
        matrix = frames.get(frameIndex);
    }

    @Override
    public void draw(Game game) {
        if (isVisible) {
            nextFrame();
            super.draw(game);
        }
    }

    public void show() {
        isVisible = true;
    }

    public void hide() {
        isVisible = false;
    }
}
