package arduinorobot;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Response of a command
 * 
 * @author matias.leone
 */
public class CommandResponse {
    
    private static Pattern SEP_PATTERN = Pattern.compile("_");
    
    private Command command;
    private String[] responseParams;
    
    public void reset() {
        command = null;
        responseParams = new String[0];
    }
    
    public void fromMsg(String msg) {
        String[] split = SEP_PATTERN.split(msg);
        this.command = Command.valueOf(split[0]);
        this.responseParams = new String[split.length - 1];
        for (int i = 1; i < split.length; i++) {
            this.responseParams[i] = split[i];
        }
    }

    public Command getCommand() {
        return command;
    }

    public String getResponse(int index) {
        return responseParams[index];
    }
    
    public String getResponse() {
        return getResponse(0);
    }
    
    public int getIntResponse(int index) {
        return Integer.parseInt(getResponse(index));
    }
    
    public float getFloatResponse(int index) {
        return Float.parseFloat(getResponse(index));
    }

    @Override
    public String toString() {
        return command + " -> " + Arrays.toString(responseParams);
    }
    
    
    
}
