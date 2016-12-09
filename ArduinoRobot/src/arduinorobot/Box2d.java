package arduinorobot;

/**
 * Bounding Box 2D
 * 
 * @author matias.leone
 */
public class Box2d {
    
    public Vector2 min;
    public Vector2 max;
    
    public Box2d(Vector2 min, Vector2 max) {
        this.min = min;
        this.max = max;
    }
    
    public Box2d() {
        this(new Vector2(0, 0), new Vector2(0, 0));
    }
    
    public Box2d(float minX, float minY, float maxX, float maxY) {
        this(new Vector2(minX, minY), new Vector2(maxX, maxY));
    }
    
}
