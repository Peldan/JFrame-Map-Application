import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class Place {
	private Category category;
	private String name;
	private Position pos;
	private PlaceIcon icon;
	private String type;

	public Place(Category category, String name, Position pos, String type) {
		this.category = category;
		this.name = name;
		this.pos = pos;
		this.type = type;
		this.icon = new PlaceIcon(pos, category);
	}
	
	public abstract String getType();
	
	public Category getCategory(){
		return category;
	}
	
	public PlaceIcon getIcon(){
		return icon;
	}
	
	public String getName(){
		return name;
	}

	public String toString(){
		return category + " " + name + " " + pos;
	}
	
	public Position getPos(){
		return pos;
	}
	
	class PlaceIcon extends JComponent {
		private Polygon triangle;
		private Category cat;
		private boolean marked;
		private int x;
		private int y;
		
		public PlaceIcon(Position pos, Category cat){
			this.cat = cat;
			this.x = pos.getX();
			this.y = pos.getY();
			addMouseListener(new IconListener());
			setBounds(x - 12, y - 24, 24, 24);
		    this.triangle = new Polygon(new int[] {0, 12, 24}, new int[] {0, 24, 0}, 3);
		}
		
		public Polygon getTriangle(){
			return triangle;
		}
		
		private void displayInfo() {
			if(type.equals("Named")){
				JOptionPane.showMessageDialog(null, Place.this.getName() + " <" +  Place.this.getPos() + "> ", "Platsinfo", JOptionPane.INFORMATION_MESSAGE, null);
			}
			else if(type.equals("Described")){			
				JPanel descPanel = new JPanel();
				descPanel.setLayout(new BoxLayout(descPanel, BoxLayout.Y_AXIS));
				JPanel row1 = new JPanel();
				JPanel row2 = new JPanel();
				JLabel nameLabel = new JLabel("Name: ");
				JTextField nameField = new JTextField(20);
				JLabel descLabel = new JLabel("Description: ");
				JTextField descField = new JTextField(20);
				descField.setText(((DescribedPlace)Place.this).getDesc());
				nameField.setText(Place.this.getName() + " <" + Place.this.getPos() + "> ");
				descField.setEditable(false);
				nameField.setEditable(false);
				row1.add(nameLabel);
				row1.add(nameField);
				row2.add(descLabel);
				row2.add(descField);
				descPanel.add(row1);
				descPanel.add(row2);
				JOptionPane.showMessageDialog(null, descPanel, "Plats", JOptionPane.INFORMATION_MESSAGE, null);
			}
		}
		
		public void markLocation(boolean marked){
			this.marked = marked;
			Program.updateMarkedList(Place.this, marked);
			repaint();
		}

		protected void paintComponent(Graphics g){
			super.paintComponent(g);
			if(cat.toString().equals("Bus")){
				g.setColor(Color.RED);			
			} 
			if(cat.toString().equals("Train")){
				g.setColor(Color.GREEN);
			}
			if(cat.toString().equals("Underground")){
				g.setColor(Color.BLUE);
			}
			g.fillPolygon(triangle);
			if(marked){
				g.setColor(Color.RED);
				g.drawRect(0, 0, 23, 23);
			}
		}
		
		
		class IconListener extends MouseAdapter {
			@Override
			public void mouseClicked(MouseEvent e){
				if(getTriangle().contains(e.getPoint())){
					if(e.getButton() == MouseEvent.BUTTON1){
						if(marked)
							markLocation(false);
						else
							markLocation(true);
					}
					if(e.getButton() == MouseEvent.BUTTON3){
						displayInfo();
					}
					
				}
			}
		}	
	}
	
}
