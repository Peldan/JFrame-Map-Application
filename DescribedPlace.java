import java.awt.Color;
import java.awt.Graphics;

public class DescribedPlace extends Place {
	private String description;
	private String type;
	
	public DescribedPlace(Category category, String name, Position pos, String description, String type) {
		super(category, name, pos, type);
		this.description = description;
		this.type = type;
	}
	
	protected void paintComponent(Graphics g){
		g.setColor(Color.RED);
		int[] vaff = {50, 60, 70};
		int[] fyff = {75, 85, 95};
		g.fillPolygon(vaff, fyff, 3);
	}
	
	public String getDesc(){
		return description;
	}

	@Override
	public String getType() {
		return type;
	}

	
}
