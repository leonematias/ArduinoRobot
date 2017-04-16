package arduinorobot;

import java.util.List;
import java.util.concurrent.Semaphore;


/**
 * Connection between robot and server
 * 
 * @author Matias Leone
 */
public class Server implements BluetoothDeviceManager.MsgListener {
    
    private final static String ARDUINO_BLUETOOTH_DEVICE = "HC-06";
    
    private final BluetoothDeviceManager bluetooth;
    private final Semaphore responseSemaphore;
    private final CommandResponse commandResponse;
    
    public Server() {
        responseSemaphore = new Semaphore(0);
        commandResponse = new CommandResponse();
        bluetooth = new BluetoothDeviceManager(ARDUINO_BLUETOOTH_DEVICE, this);
        bluetooth.connect();
    }

    public void sendMessage(String msg) {
        bluetooth.sendMessage(msg);
    }
    
    @Override
    public void messageReceived(String msg) {
        System.out.println(msg);
        commandResponse.fromMsg(msg);
        responseSemaphore.release();
    }
    
    
    
    /**
     * Send command and wait until we get a response
     */
    public CommandResponse sendBlockingCommand(Command command, List<String> params) {
        try {
            commandResponse.reset();
            bluetooth.sendMessage(buildCommandString(command, params));
            responseSemaphore.acquire();
            if(commandResponse.getCommand() == null)
                throw new RuntimeException("Empty response");
            return commandResponse;
            
        } catch (Exception e) {
            throw new RuntimeException("Error sending command: " + command.name(), e);
        }
        
    }
    
    private String buildCommandString(Command command, List<String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(command.name());
        sb.append(";");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i));
            sb.append(";");
        }
        return sb.toString();
    }
    
}
