package arduinorobot;

import java.util.ArrayList;
import java.util.List;

/**
 * Main intelligence of the robot
 * 
 * @author matias.leone
 */
public class AI {

    private final Server server;
    private final MainUI mainUI;
    private volatile boolean running;

    
    public AI(Server server, MainUI mainUI) {
        this.server = server;
        this.mainUI = mainUI;
    }
    
    public void execute() {
        this.running = true;
        try {
            while(this.running) {
                aiLoop();
                
                mainUI.refresh();
                
                Thread.sleep(300);
            }
        } catch (Exception e) {
            this.running = false;
            throw new RuntimeException(e);
        }
    }
    
    public void stop() {
        this.running = false;
    }
    
    
   private void aiLoop() {
       int distance = Command.distance(server);
       if(distance > 20) {
           Command.forward(server, 1);
       } else {
           Command.leftSmall(server);
       }
       
   }
    
    
}
