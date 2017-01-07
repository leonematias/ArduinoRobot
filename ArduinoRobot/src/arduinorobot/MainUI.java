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
    private UIControlPanel controlsPanel;
    
    public static void main(String[] args) {
        new MainUI();
    }
    
    public MainUI() {
        frame = new JFrame("Arduino Robot");
        frame.setMinimumSize(new Dimension(WIN_WIDTH, WIN_HEIGHT));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        
        //2D map
        map = new Map2dPanel();
        frame.add(map.getComponent(), BorderLayout.CENTER);
        
        //Controls
        controlsPanel = new UIControlPanel(this);
        frame.add(controlsPanel, BorderLayout.WEST);
        
        //Log area
        logArea = new JTextArea(5, 100);
        logArea.setEditable(false);
        frame.add(new JScrollPane(logArea), BorderLayout.SOUTH);
        
        
        //Make visible
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
    
    public void refresh() {
        map.render();
        controlsPanel.repaint();
    }
    
    public void log(String msg) {
        logArea.append(msg);
        logArea.append("\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    public Map2dPanel getMap() {
        return map;
    }
    
    
    
}
