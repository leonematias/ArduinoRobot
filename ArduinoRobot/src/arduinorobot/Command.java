package arduinorobot;

/**
 * All available commands that can be sent to the robot
 * 
 * @author matias.leone
 */
public enum Command {
    PING,
    LED,
    SPD,
    FORW,
    BACK,
    STOP,
    RLEFT,
    RRIGHT,
    DIST,
    GYRO
    ;
    
    private final static String OK_RESPONSE = "OK";
    
    /*
    private static void sendAndWaitOk(Server server, Command command) {
        CommandResponse response = server.sendBlockingCommand(command);
        if(!OK_RESPONSE.equals(response.getResponse()))
            throw new RuntimeException("Invalid ping response: " + response);
    }
    
    public static void send(Server server, Command command) {
        sendAndWaitOk(server, command);
    }
    
    public static int sendAndGetInt(Server server, Command command) {
        CommandResponse response = server.sendBlockingCommand(command);
        return response.getIntResponse(0);
    }
    
    public static float[] sendAndGet3Float(Server server, Command command) {
        CommandResponse response = server.sendBlockingCommand(command);
        return new float[]{
            response.getFloatResponse(0),
            response.getFloatResponse(1),
            response.getFloatResponse(2),
        };
    }
    */
    
    
    
    
    
    
}
