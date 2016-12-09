package arduinorobot;

import java.util.ArrayList;
import java.util.List;

/**
 * Main intelligence of the robot
 * 
 * @author matias.leone
 */
public class AI {
    
    private static final Vector2 ROBOT_SIZE = new Vector2(10, 20);
    private static final Vector2 WALL_SIZE = new Vector2(2, 10);
    private static final float MAX_DIST = 100;
    private static final float STEP_MOVEMENT = 5;
    
    
    private final Server server;
    private final Map2dPanel map;
    private Vector2 currentPos;
    private Vector2 currentDir;
    private DistanceResponse testDist;
    private final List<Box2d> walls;
    private boolean canMoveForward;
    private boolean canMoveBackward;
    private boolean canMoveLeft;
    private boolean canMoveRight;
    
    public AI(Server server, Map2dPanel map) {
        this.server = server;
        this.map = map;
        this.currentPos = new Vector2(0, 0);
        this.currentDir = new Vector2(0, 1);
        this.testDist = new DistanceResponse();
        this.walls = new ArrayList<>(100);
    }
    
    public void execute() {
        
        for (int i = 0; i < 10; i++) {
            
            measureDistances();
            Command.forward(server, 1);
            currentPos.Y += STEP_MOVEMENT;
            
            map.render();
        }
        
        
        
    }
    
    
    
    private void measureDistances() {
        //Get left, right and center distances
        DistanceResponse dist = Command.allDistances(server, testDist);
        
        float left = MAX_DIST;
        float right = MAX_DIST;
        float center = MAX_DIST;
        float back = MAX_DIST;
        /**
         * Possible directions
         * top: 0,1
         * left: 1,0
         * right: -1,0
         */
        if(currentDir.Y == 1) {
            left = dist.left;
            right = dist.right;
            center = dist.center;
        } else if(currentDir.Y == 0) {
            if(currentDir.X == 1) {
                left = dist.center;
                back = dist.left;
                center = dist.right;
            } else if(currentDir.Y == -1) {
                right = dist.center;
                back = dist.right;
                center = dist.left;
            }
        }
        
        if(left < MAX_DIST) addVerticalWall(currentPos.X - left, currentPos.Y);
        if(right < MAX_DIST) addVerticalWall(currentPos.X + right, currentPos.Y);
        if(center < MAX_DIST) addHorizontalWall(currentPos.X, currentPos.Y + center);
        if(back < MAX_DIST) addHorizontalWall(currentPos.X, currentPos.Y - back);
        
    }
    
    private void addVerticalWall(float centerX, float centerY) {
        float dx = WALL_SIZE.X / 2;
        float dy = WALL_SIZE.Y / 2;
        this.walls.add(new Box2d(centerX - dx, centerY - dy, centerX + dx, centerY + dy));
    }
    
    private void addHorizontalWall(float centerX, float centerY) {
        float dx = WALL_SIZE.Y / 2;
        float dy = WALL_SIZE.X / 2;
        this.walls.add(new Box2d(centerX - dx, centerY - dy, centerX + dx, centerY + dy));
    }
    
    
    
}
