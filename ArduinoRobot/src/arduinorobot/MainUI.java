package arduinorobot;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
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
    private JLabel statusLabel;
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
 
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect();
            }
        });
        controlsPanel.add(connectButton);
        
        statusLabel = new JLabel();
        statusLabel.setMinimumSize(new Dimension(200, 20));
        controlsPanel.add(statusLabel);
        
        frame.add(controlsPanel, BorderLayout.NORTH);
        frame.pack();
        
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenDim.width / 2 - WIN_WIDTH / 2, screenDim.height / 2 - WIN_HEIGHT / 2);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        frame.setVisible(true);
        
        
        //Init map graphics
        map.init();
    }
    
    private void connect() {
        server = new Server();
        statusLabel.setText("CONNECTED");
        
        ai = new AI(server, map);
        //ai.execute();
    }
    
}
