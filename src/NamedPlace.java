import java.awt.Color;
import java.awt.Graphics;

public class NamedPlace extends Place {
	
	private String type;

	
	public NamedPlace(Category category, String name, Position pos, String type) {
		super(category, name, pos, type);
		this.type = type;
	}

	@Override
	public String getType() {
		return type;
	}
	
}
