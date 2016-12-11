package arduinorobot;

/**
 * Response of a command
 * 
 * @author matias.leone
 */
public class CommandResponse {
    
    private Command command;
    private String response;
    
    public void reset() {
        command = null;
        response = null;
    }
    
    public void fromMsg(String msg) {
        int idx = msg.indexOf('_');
        if(idx == -1 || msg.length() < 3)
            throw new RuntimeException("Invalid command response: " + msg);
        
        char commandCode = msg.charAt(0);
        this.command = Command.fromCode(commandCode);
        this.response = msg.substring(2);
        
        if(this.command == null)
            throw new RuntimeException("Invalid command code: " + msg);
    }

    public Command getCommand() {
        return command;
    }

    public String getResponse() {
        return response;
    }
    
    public int getIntResponse() {
        return Integer.parseInt(response);
    }

    @Override
    public String toString() {
        return command + " -> " + response;
    }
    
    
    
}
