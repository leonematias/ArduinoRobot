package arduinorobot;

/**
 * All available commands that can be sent to the robot
 * 
 * @author matias.leone
 */
public enum Command {
    PING('a'),
    STEP_FORWARD('b'),
    STEP_BACKWARD('c'),
    ROTATE_LEFT_90('d'),
    ROTATE_RIGHT_90('e'),
    TEST_DISTANCE('f'),
    DISTANCE_SENSOR_LEFT('g'),
    DISTANCE_SENSOR_RIGHT('h'),
    DISTANCE_SENSOR_CENTER('i'),
    LED_ON('j'),
    LED_OFF('k'),
    TEST_3_DISTANCES('l'),
    TWO_STEPS_FORWARD('m'),
    THREE_STEPS_FORWARD('n'),
    TWO_STEPS_BACKWARD('o'),
    THREE_STEPS_BACKWARD('p'),
    ROTATE_LEFT_180('q'),
    SET_SLOW_SPEED('r'),
    SET_MID_SPEED('s'),
    SET_FAST_SPEED('t'),
    STOP('u'),
    ROTATE_LEFT_SMALL('v'),
    ROTATE_RIGHT_SMALL('w'),
    ;
    
    private final static String OK_RESPONSE = "OK";
    
    public final char code;
    
    Command(char code) {
        this.code = code;
    }
    
    public static Command fromCode(char code) {
        for (Command c : values()) {
            if(c.code == code)
                return c;
        }
        return null;
    }
    
    private static void sendAndWaitOk(Server server, Command command) {
        CommandResponse response = server.sendBlockingCommand(command);
        if(!OK_RESPONSE.equals(response.getResponse()))
            throw new RuntimeException("Invalid ping response: " + response);
    }
    
    public static void ping(Server server) {
        sendAndWaitOk(server, PING);
    }
    
    public static void forward(Server server, int steps) {
        Command c = null;
        if(steps == 1) c = STEP_FORWARD;
        else if(steps == 2) c = TWO_STEPS_FORWARD;
        else if(steps == 3) c = THREE_STEPS_FORWARD;
        
        if(c != null)
            sendAndWaitOk(server, c);
    }
    
    public static void backward(Server server, int steps) {
        Command c = null;
        if(steps == 1) c = STEP_BACKWARD;
        else if(steps == 2) c = TWO_STEPS_BACKWARD;
        else if(steps == 3) c = THREE_STEPS_BACKWARD;
        
        if(c != null)
            sendAndWaitOk(server, c);
    }
    
    public static void left(Server server) {
        sendAndWaitOk(server, ROTATE_LEFT_90);
    }
    
    public static void leftSmall(Server server) {
        sendAndWaitOk(server, ROTATE_LEFT_SMALL);
    }
    
    public static void right(Server server) {
        sendAndWaitOk(server, ROTATE_RIGHT_90);
    }
    
    public static void rightSmall(Server server) {
        sendAndWaitOk(server, ROTATE_RIGHT_SMALL);
    }
    
    public static void rotate180(Server server) {
        sendAndWaitOk(server, ROTATE_LEFT_180);
    }
    
    public static int distance(Server server) {
        CommandResponse response = server.sendBlockingCommand(TEST_DISTANCE);
        return response.getIntResponse();
    }
    
    public static DistanceResponse allDistances(Server server, DistanceResponse out) {
        CommandResponse response = server.sendBlockingCommand(TEST_3_DISTANCES);
        out.fromResponse(response);
        return out;
    }
    
    public static void led(Server server, boolean on) {
        sendAndWaitOk(server, on ? LED_ON : LED_OFF);
    }

    @Override
    public String toString() {
        return name() + "(" + code + ")";
    }
    
    
    
    
}
