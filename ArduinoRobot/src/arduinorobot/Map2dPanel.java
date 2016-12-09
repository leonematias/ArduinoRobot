package arduinorobot;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Render 2D map of robot
 * 
 * @author matias.leone
 */
public class Map2dPanel {
    
    private Canvas renderPanel;
    private BufferedImage renderImg;
    private Graphics2D renderG;
    private Dimension graphDim;
    
    public Map2dPanel() {
        this.renderPanel = new Canvas();
    }
    
    public Component getComponent() {
        return renderPanel;
    }
    
    public void init() {
        graphDim = renderPanel.getSize();
        renderImg = (BufferedImage)renderPanel.createImage(graphDim.width, graphDim.height);
        renderG = renderImg.createGraphics();
    }

    
    public void render() {
        //Clean
        renderG.setColor(Color.BLACK);
        renderG.fillRect(0, 0, graphDim.width, graphDim.height);
        
        doRender();
        
        //Draw buffer
        renderPanel.getGraphics().drawImage(renderImg, 0, 0, null);
    }
    
    private void doRender() {
        renderG.setColor(Color.RED);
        renderG.fillRect(200, 200, 50, 50);
    }
    
}
