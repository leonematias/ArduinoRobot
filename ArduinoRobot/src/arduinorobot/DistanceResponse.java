package arduinorobot;

import java.util.regex.Pattern;

/**
 * Response of testing distance with servo
 * 
 * @author matias.leone
 */
public class DistanceResponse {
    
    private static final Pattern SPLIT = Pattern.compile("_");
    
    public int left;
    public int center;
    public int right;
    
    public void fromResponse(CommandResponse response) {
        String[] split = SPLIT.split(response.getResponse());
        if(split.length != 3)
            throw new RuntimeException("Invalid distance response: " + response);
        
        this.left = Integer.parseInt(split[0]);
        this.center = Integer.parseInt(split[1]);
        this.right = Integer.parseInt(split[2]);
    }
    
}
