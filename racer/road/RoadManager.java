package javarush.racer.road;

import com.javarush.engine.cell.Game;
import javarush.racer.PlayerCar;
import javarush.racer.RacerGame;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;
    private static final int FOURTH_LANE_POSITION = 44;
    private static final int PLAYER_CAR_DISTANCE = 12;

    private List<RoadObject> items = new ArrayList<>();
    private int passedCarsCount = 0;

    public int getPassedCarsCount() {
        return passedCarsCount;
    }

    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {
        RoadObject roadObject;
        switch (type) {
            case THORN     : roadObject = new Thorn(x, y);
                break;
            case DRUNK_CAR : roadObject = new MovingCar(x, y);
                break;
            default        : roadObject = new Car(type, x, y);
        }
        return roadObject;
    }

    private void addRoadObject(RoadObjectType type, Game game) {
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject roadObject = createRoadObject(type, x, y);
        if (roadObject != null && isRoadSpaceFree(roadObject)) {
            items.add(roadObject);
        }
    }

    private void deletePassedItems() {
        for (int i = 0; i < items.size(); ) {
            RoadObject item = items.get(i);
            if (item.y >= RacerGame.HEIGHT) {
                items.remove(i);
                if (item.type != RoadObjectType.THORN) {
                    passedCarsCount++;
                    continue;
                }
            }
            i++;
        }
    }

    public void draw(Game game) {
        items.forEach(item -> item.draw(game));
    }

    public void move(int boost) {
        items.forEach(item -> item.move((item.speed + boost), items));
        deletePassedItems();
    }

    private boolean isRoadSpaceFree(RoadObject object) {
        for (RoadObject roadObject : items) {
            if (roadObject.isCollisionWithDistance(object, PLAYER_CAR_DISTANCE))
                return false;
        }
        return true;
    }

    private boolean isThornExists() {
        for (RoadObject roadObject : items) {
            if (roadObject.type == RoadObjectType.THORN)
                return true;
        }
        return false;
    }

    private boolean isMovingCarExists() {
        for (RoadObject roadObject : items) {
            if (roadObject.type == RoadObjectType.DRUNK_CAR)
                return true;
        }
        return false;
    }

    private void generateThorn(Game game) {
        if (game.getRandomNumber(100) < 10 && !isThornExists()) {
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    private void generateMovingCar(Game game) {
        if (game.getRandomNumber(100) < 10 & !isMovingCarExists()) {
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        }
    }

    private void generateRegularCar(Game game) {
        int carTypeNumber = game.getRandomNumber(4);
        if (game.getRandomNumber(100) < 30) {
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        }
    }

    public void generateNewRoadObjects(Game game) {
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    public boolean checkCrush(PlayerCar player) {
        for (RoadObject roadObject : items) {
            if (roadObject.isCollision(player))
                return true;
        }
        return false;
    }

}
