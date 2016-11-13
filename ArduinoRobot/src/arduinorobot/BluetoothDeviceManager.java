package arduinorobot;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * Tool to connect with a given Bluetooth device
 * 
 * @author Matias Leone
 */
public class BluetoothDeviceManager {
    
    /**
     * Bluetooth service RFCOMM
     */
    private final static UUID[] UUID_SET = new UUID[]{new UUID(0x0003)};
    private final static String SERVICE_URL_PATTERN = "btspp://";
    
    private final String deviceName;
    private final MsgListener msgListener;
    private final LocalDevice localDevice;
    private final DiscoveryAgent agent;
    private RemoteDevice device;
    private String serviceUrl;
    private StreamConnection connection;
    private CountDownLatch countDownLatch;
    private InputStream deviceIn;
    private OutputStream deviceOut;
    private volatile boolean listenMessagesFlag;

    public BluetoothDeviceManager(String deviceName, MsgListener msgListener) {
        try {
            this.deviceName = deviceName;
            this.msgListener = msgListener;
            this.localDevice = LocalDevice.getLocalDevice();
            this.agent = localDevice.getDiscoveryAgent();
            
        } catch (Exception e) {
            throw new RuntimeException("Error initializing local device", e);
        }
    }
    
    /**
     * Connect to Bluetooth device
     */
    public void connect() {
        try {
            
            //Search device
            this.device = null;
            this.countDownLatch = new CountDownLatch(1);
            this.agent.startInquiry(DiscoveryAgent.GIAC, new DiscoverDeviceListener());
            this.countDownLatch.await();
            if(this.device == null) {
                throw new RuntimeException("Could not find device with name: " + deviceName);
            }
            
            //Search service
            this.serviceUrl = null;
            this.countDownLatch = new CountDownLatch(1);
            this.agent.searchServices(null, UUID_SET, this.device, new DiscoverServiceListener());
            this.countDownLatch.await();
            if(this.serviceUrl == null) {
                throw new RuntimeException("Could not find service with name RFCOMM");
            }
            
            //Establish connection
            this.connection = (StreamConnection) Connector.open(this.serviceUrl);
            this.deviceIn = this.connection.openInputStream();
            this.deviceOut = this.connection.openOutputStream();
            
            //Create thread to receive listen incomming messages
            this.listenMessagesFlag = true;
            Thread t = new Thread(new ReceiveMessageThread());
            t.start();
            
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to device: " + deviceName);
        }
    }
    
    /**
     * Send message to device
     */
    public void sendMessage(String message) {
        try {
            message += "\n";
            this.deviceOut.write(message.getBytes());
            
        } catch (Exception e) {
            throw new RuntimeException("Error sending message: " + message);
        }
    }
    
    public void disconnect() {
        try {
            this.listenMessagesFlag = false;
            if(this.deviceIn != null) {
                this.deviceIn.close();
            }
            if(this.deviceOut != null) {
                this.deviceOut.close();
            }
            if(this.connection != null) {
                this.connection.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error disconnecting device", e);
        }
    }
    
    /**
     * Listener to receive incomming messages
     */
    public static interface MsgListener {
        public void messageReceived(String msg);
    }
    
    /**
     * Listener to discover a device with a specific name
     */
    private class DiscoverDeviceListener implements DiscoveryListener {
        @Override
        public void deviceDiscovered(RemoteDevice deviceDiscovered, DeviceClass deviceClass) {
            try {
                String friendlyName = deviceDiscovered.getFriendlyName(false);
                if(friendlyName.equals(deviceName)) {
                    device = deviceDiscovered;
                    countDownLatch.countDown();
                }
            } catch (Exception e) {
                throw new RuntimeException("Error on device discovered", e);
            }
        }
        @Override
        public void servicesDiscovered(int i, ServiceRecord[] srs) {
        }
        @Override
        public void serviceSearchCompleted(int i, int i1) {
        }
        @Override
        public void inquiryCompleted(int i) {
        }
    }
    
    /**
     * Listener to discover a service
     */
    private class DiscoverServiceListener implements DiscoveryListener {
        @Override
        public void servicesDiscovered(int transID, ServiceRecord[] servRecords) {
            for (ServiceRecord servRecord : servRecords) {
                String url = servRecord.getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
                if (url != null && url.toLowerCase().startsWith(SERVICE_URL_PATTERN)) {
                    serviceUrl = url;
                    countDownLatch.countDown();
                    break;
                }
            }
        }
        @Override
        public void deviceDiscovered(RemoteDevice deviceDiscovered, DeviceClass deviceClass) {
        }
        @Override
        public void serviceSearchCompleted(int i, int i1) {
        }
        @Override
        public void inquiryCompleted(int i) {
        }
    }
    
    /**
     * Thread to receive messages
     */
    private class ReceiveMessageThread implements Runnable {
        @Override
        public void run() {
            try {
                byte buffer[] = new byte[1024];
                StringBuilder sb = new StringBuilder(100);
                
                while (listenMessagesFlag) {
                    
                    //Read all characters
                    int n = deviceIn.read(buffer);
                    if(n > 0) {
                        String str = new String(buffer, 0, n);
                        for (int i = 0; i < str.length(); i++) {
                            char c = str.charAt(i);
                            
                            //End of line?
                            if(c == '\n') {
                                //Notify listener
                                String message = sb.toString().trim();
                                sb.setLength(0);
                                msgListener.messageReceived(message);
                            } else {
                                //Accumulate characters
                                sb.append(c);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException("Error receiving message", e);
            }
        }
    }
}
