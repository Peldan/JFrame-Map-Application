import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DescribedPlaceForm extends JPanel {
	private JTextField nameField = new JTextField(20);
	private JTextField descField = new JTextField(20);
	private JPanel row1 = new JPanel();
	private JPanel row2 = new JPanel();
	
	public DescribedPlaceForm(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		row1.add(new JLabel("Namn: "));
		row1.add(nameField);
		row2.add(new JLabel("Beskrivning: "));
		row2.add(descField);
		add(row1);
		add(row2);
	}
	
	public String getName(){
		return nameField.getText();
	}
	
	public String getDesc(){
		return descField.getText();
	}
	
}
