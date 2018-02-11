import javax.swing.JLayeredPane;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import javax.imageio.ImageIO;

public class MapPanel extends JLayeredPane {
	
	private File file;
	private BufferedImage mapImg;
	
	public MapPanel(File file) throws IOException{
		this.setPreferredSize(new Dimension(951, 793));
		this.mapImg = ImageIO.read(file);
		this.file = file;
	}
	
	public File getFile(){
		return file;
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(mapImg, 0, 0, this);
	}
}
