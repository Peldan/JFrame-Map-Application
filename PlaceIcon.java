import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.JComponent;

public class PlaceIcon extends JComponent {
	private Position placePosition;
	private Category cat;
	private int pointX, pointY;
	private int leftX, leftY;
	private int rightX, rightY;
	
	public PlaceIcon(Position pos, Category cat){
		this.cat = cat;
		this.placePosition = pos;
	}
	
	protected void paintComponent(Graphics g){
		fixPositions();
		super.paintComponent(g);
		if(cat.toString() == "Bus"){
			g.setColor(Color.RED);			
		} 
		if(cat.toString() == "Train"){
			g.setColor(Color.GREEN);
		}
		if(cat.toString() == "Underground"){
			g.setColor(Color.BLUE);
		}
		int[] x = {leftX, pointX, rightX};
		int[] y = {leftY, pointY, rightY};
	    Polygon triangle = new Polygon(x, y, 3);
	    g.fillPolygon(triangle);
	}
	
	private void fixPositions(){
		int placeX = placePosition.getX();
		int placeY = placePosition.getY();
		this.pointX = placeX;
		this.pointY = placeY;
		this.leftX = placeX - 15;
		this.leftY = pointY - 15;
		this.rightX = placeX + 15;
		this.rightY = pointY - 15;
	}

}
