package arduinorobot;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * Main UI
 * 
 * @author matias.leone
 */
public class MainUI {
    
    private final static int WIN_WIDTH = 1200;
    private final static int WIN_HEIGHT = 720;
    
    private JFrame frame;
    private JTextArea logArea;
    private JComboBox<Command> inputCommand;
    private JButton sendCommandButton;
    private JButton connectButton;
    
    private Map2dPanel map;
    private Server server;
    private AI ai;
    
    public static void main(String[] args) {
        new MainUI();
    }
    
    public MainUI() {
        frame = new JFrame("Gravity");
        frame.setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        map = new Map2dPanel();
        frame.add(map.getComponent(), BorderLayout.CENTER);
 
        
        //Controls
        JPanel controlsPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(controlsPanel, BoxLayout.PAGE_AXIS);
        controlsPanel.setLayout(boxLayout);
        //controlsPanel.setMaximumSize(new Dimension(-1, 50));
        
        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        connectButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(connectButton);
        
        
        inputCommand = new JComboBox<>(Command.values());
        inputCommand.setMaximumSize(new Dimension(250, 25));
        inputCommand.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(inputCommand);
        sendCommandButton = new JButton("Send command");
        sendCommandButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendCommand((Command)inputCommand.getSelectedItem());
            }
        });
        sendCommandButton.setEnabled(false);
        sendCommandButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlsPanel.add(sendCommandButton);
        
        controlsPanel.add(Box.createVerticalGlue());
        frame.add(controlsPanel, BorderLayout.WEST);
        
        logArea = new JTextArea(5, 100);
        logArea.setEditable(false);
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        
        frame.pack();
        
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        //frame.setLocation(screenDim.width / 2 - WIN_WIDTH / 2, screenDim.height / 2 - WIN_HEIGHT / 2);
        frame.setLocation(0, 0);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        frame.setVisible(true);
        
        
        
        //Init map graphics
        map.init();
        map.render();
    }
    
    private void connect() {
        server = new Server();
        log("CONNECTED");
        connectButton.setEnabled(false);
        sendCommandButton.setEnabled(true);
        
        ai = new AI(server, map);
        //ai.execute();
    }
    
    private void sendCommand(Command command) {
        log("Sending command: " + command);
        CommandResponse response = server.sendBlockingCommand(command);
        log("Response: " + response);
    }
    
    private void log(String msg) {
        logArea.append(msg);
        logArea.append("\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }
    
}
