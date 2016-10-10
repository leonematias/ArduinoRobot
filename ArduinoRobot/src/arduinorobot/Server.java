package arduinorobot;


/**
 *
 * @author Matias Leone
 */
public class Server implements BluetoothDeviceManager.MsgListener {
    
    private final static String ARDUINO_BLUETOOTH_DEVICE = "HC-06";
    
    private BluetoothDeviceManager bluetooth;
    
    public Server() {
        bluetooth = new BluetoothDeviceManager(ARDUINO_BLUETOOTH_DEVICE, this);
        bluetooth.connect();
    }

    public void sendMessage(String msg) {
        bluetooth.sendMessage(msg);
    }
    
    @Override
    public void messageReceived(String msg) {
        System.out.println(msg);
    }
    
}
