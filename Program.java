import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.awt.GridBagLayout;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Program extends JFrame {
	
	private MapPanel m;
	private JScrollPane mapScroll;
	private JPanel topPanel;
	private JPanel radioButtonPanel;
	private JPanel mapPanel;
	private JPanel rightPanel;
	private JPanel categoryPanel;
	private JButton newPlaceButton, searchPlace, hidePlace, removePlace, coordinates, hideCategory;
	private JRadioButton namedBox;
	private JRadioButton describedBox;
	private ButtonGroup group = new ButtonGroup();
	private JTextField searchField;
	private JList<Category> categoryArea;
	private JLabel categoryAreaTitle;
	private JMenuBar menuBar;
	private JMenu archiveMenu;
	private JMenuItem newMap, loadPlaces, save, exit;
	private HashMap<Category, List<Place>> placesByCategory = new HashMap<Category, List<Place>>(); 
	private HashMap<String, List<Place>> placesByName = new HashMap<String, List<Place>>(); 	
	private HashMap<Position, Place> placesByPosition = new HashMap<Position, Place>();
	private static ArrayList<Place> markedPlaces = new ArrayList<Place>();
	private Category bus = new Category("Bus");
	private Category train = new Category("Train");
	private Category underground = new Category("Underground");
	private Category none = new Category("None");
	private Category[] categories = {bus, underground, train};
	private boolean isSaved = true;
	
	
	Program() {
		super("Inlupp 2");
		setupTopPanelComponents();
		setupMapArea();
		setSize(1024, 1024);
		setLayout(new BorderLayout());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				if(!isSaved){
					int choice = JOptionPane.showConfirmDialog(null, "There are unsaved changes. Are you sure you want to exit?", "Warning", JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
					if(choice == JOptionPane.YES_OPTION){
						System.exit(0);
					} else {
						return;
					}
				} else {
					System.exit(0);
				}
			}
		});
		topPanel.add(newPlaceButton);
		radioButtonPanel.add(namedBox);
		radioButtonPanel.add(describedBox);
		topPanel.add(radioButtonPanel);
		topPanel.add(searchField);
		topPanel.add(searchPlace);
		topPanel.add(hidePlace);
		topPanel.add(removePlace);
		topPanel.add(coordinates);			
		setJMenuBar(menuBar);
		add(topPanel, BorderLayout.NORTH);
		add(rightPanel, BorderLayout.EAST);
		setVisible(true);
	}
	
	private void setupTopPanelComponents(){
		createMenu();
		topPanel = new JPanel();
		rightPanel = new JPanel();
		radioButtonPanel = new JPanel();
		categoryPanel = new JPanel();
		radioButtonPanel.setLayout(new BoxLayout(radioButtonPanel, BoxLayout.Y_AXIS));
		categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
		rightPanel.setLayout(new BorderLayout());
		newPlaceButton = new JButton("New");
		newPlaceButton.addActionListener(new NewPlace());
		searchPlace = new JButton("Search");
		hidePlace = new JButton("Hide");
		hidePlace.addActionListener(new HideMarked());
		removePlace = new JButton("Remove");
		removePlace.addActionListener(new RemoveMarked());
		coordinates = new JButton("Coordinates");
		coordinates.addActionListener(new CoordinateFinder());
		namedBox = new JRadioButton("Name");
		namedBox.setSelected(true);
		describedBox = new JRadioButton("Described");
		searchField = new JTextField(10);
		searchPlace.addActionListener(new SearchListener());
		group.add(describedBox);
		group.add(namedBox);
		coordinates.setEnabled(false);
		newPlaceButton.setEnabled(false);
		searchPlace.setEnabled(false);
		hidePlace.setEnabled(false);
		removePlace.setEnabled(false);
		searchField.setEnabled(false);
		namedBox.setEnabled(false);
		describedBox.setEnabled(false);
	}
	
	private void setupMapArea(){
		categoryAreaTitle = new JLabel("Categories");
		categoryArea = new JList<Category>(categories);
		categoryArea.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		categoryArea.addListSelectionListener(new ShowCategory());
		categoryPanel.setLayout(new BorderLayout());
		hideCategory = new JButton("Hide category");
		categoryArea.setFixedCellWidth(110);
		hideCategory.addActionListener(new HideCategory());
		categoryPanel.add(categoryAreaTitle, BorderLayout.NORTH);
		categoryPanel.add(categoryArea, BorderLayout.EAST);
		categoryPanel.add(hideCategory, BorderLayout.SOUTH);
		mapPanel = new JPanel();
		rightPanel.add(categoryPanel);
		hideCategory.setEnabled(false);
		categoryArea.setEnabled(false);
	}
	
	private void clearAllData() throws IOException{		
		placesByCategory.clear();
		placesByName.clear();
		placesByPosition.clear();
		addMapToFrame(m.getFile());
	}
	
	private void refreshMap(){
		mapPanel.remove(m);
		mapScroll.remove(mapPanel);
		remove(mapScroll); 
	}
	

	private void addMapToFrame(File chosenFile) throws IOException{
		if(m != null && mapScroll != null && mapPanel != null){
			refreshMap();
		}
		mapScroll = new JScrollPane(mapPanel);
		mapPanel.setLayout(new GridBagLayout());
		mapPanel.setPreferredSize(new Dimension(951, 793));
		mapScroll.setPreferredSize(new Dimension(951, 793));
		m = new MapPanel(chosenFile);
		mapPanel.add(m);
		add(mapScroll);
		coordinates.setEnabled(true);
		newPlaceButton.setEnabled(true);
		searchPlace.setEnabled(true);
		hidePlace.setEnabled(true);
		removePlace.setEnabled(true);
		searchField.setEnabled(true);
		namedBox.setEnabled(true);
		describedBox.setEnabled(true);
		hideCategory.setEnabled(true);
		categoryArea.setEnabled(true);
		loadPlaces.setEnabled(true);
		validate();	
	}
		
	
	private void createMenu() {
		menuBar = new JMenuBar();
		archiveMenu = new JMenu("Archive");
		menuBar.add(archiveMenu);
		newMap = new JMenuItem("New Map");
		newMap.addActionListener(new NewMap());
		loadPlaces = new JMenuItem("Load Places");
		loadPlaces.setEnabled(false);
		loadPlaces.addActionListener(new LoadPlacesFromFile());
		save = new JMenuItem("Save");
		save.addActionListener(new SavePlacesToFile());
		exit = new JMenuItem("Exit");
		exit.addActionListener(new ExitProgram());
		archiveMenu.add(newMap);
		archiveMenu.add(loadPlaces);
		archiveMenu.add(save);
		archiveMenu.add(exit);
	}
	
	public static void updateMarkedList(Place p, boolean marked) {
		if(marked){
			markedPlaces.add(p);
		}
		else{
			markedPlaces.remove(p);
		}
	}
	
	class LoadPlacesFromFile implements ActionListener {
		private JFileChooser fc = new JFileChooser();

		@Override
		public void actionPerformed(ActionEvent e) {
			int returnValue = fc.showOpenDialog(Program.this);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				if(!isSaved){
					int choice = JOptionPane.showConfirmDialog(null, "There are unsaved changes. Are you sure you want to load new places?", "Warning", JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
					if(choice == JOptionPane.YES_OPTION){
						try {
							clearAllData();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						return;
					}
				}
				if(!placesByCategory.isEmpty() || !placesByName.isEmpty() || !placesByPosition.isEmpty()){
					try {
						clearAllData();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				File chosenFile = fc.getSelectedFile();
				readFile(chosenFile);
			} else {
				return;
			}
		}
		
		private void readFile(File chosenFile){
			FileReader input;
			BufferedReader in;
			String line = null;
			try {
				input = new FileReader(chosenFile);
				in = new BufferedReader(input);
				while((line = in.readLine())!=null){
					addPlace(line);		
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			isSaved = true;
		}
		
		private void addPlace(String line){
			String type, x, y, name, cat;
			Category c;
			Position pos;
			NewPlace n = new NewPlace();			
			type = line.substring(0, line.indexOf(","));
			line = line.replaceAll((type + ","), "");
			cat = line.substring(0, line.indexOf(","));
			line = line.replaceAll((cat + ","), "");
			x = line.substring(0, line.indexOf(","));
			line = line.replaceFirst((x + ","), "");
			y = line.substring(0, line.indexOf(","));
			line = line.replaceAll((y + ","), "");
			pos = new Position(Integer.parseInt(x), Integer.parseInt(y));
			c = getCategory(cat);
			if(type.equals("Described")){
				name = line.substring(0, line.indexOf(","));
				line = line.replaceAll(name + ",", "");
				String desc = line.substring(0);
				DescribedPlace dp = new DescribedPlace(c, name, pos, desc, type);
				n.putToMap(c, dp, name, pos);
				n.addPlaceToMap(dp);
			} else {				
				name = line.substring(0);
				NamedPlace p = new NamedPlace(c, name, pos, type);
				n.putToMap(c, p, name, pos);
				n.addPlaceToMap(p);
			}
			m.repaint();
		}
		
		private Category getCategory(String cat){
			switch(cat){
				case "Bus":
					return bus;
				case "Train":
					return train;
				case "Underground":
					return underground;
				case "None":
					return none;
				default:
					return null;
			}
		}
	}
	
	class SavePlacesToFile implements ActionListener {
		private JFileChooser fc = new JFileChooser();
		@Override
		public void actionPerformed(ActionEvent e){		
			try {
				int returnValue = fc.showOpenDialog(Program.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File chosenFile = fc.getSelectedFile();
					FileWriter output = new FileWriter(chosenFile);
					PrintWriter out = new PrintWriter(output);
					Collection<List<Place>> places = placesByName.values();
					
					for(List<Place> placeList: places){
						for(Place p: placeList){
							if(p.getType().equals("Named")){
								out.println(p.getType() + "," + p.getCategory()  + "," + p.getPos() + "," + p.getName());
							}
							if(p.getType().equals("Described")){
								out.println(p.getType() + "," + p.getCategory()  + "," + p.getPos() + "," + p.getName() + "," + ((DescribedPlace)p).getDesc());
							}
						}
					}
					isSaved = true;
					out.close();
				} else {
					return;
				}
			} catch (IOException ex) {
				System.out.println("IOException " + ex.getMessage());
			}
		}

	}

	
	class ShowCategory implements ListSelectionListener {
		@Override		
		public void valueChanged(ListSelectionEvent e) {	
			if(categoryArea.getSelectedValue() == bus && placesByCategory.get(bus) != null){
				setPlacesVisible(bus);
			}
			if(categoryArea.getSelectedValue() == underground && placesByCategory.get(underground) != null){
				setPlacesVisible(underground);
			}
			if(categoryArea.getSelectedValue() == train && placesByCategory.get(train) != null){
				setPlacesVisible(train);
			}		
		}
		
		private void setPlacesVisible(Category cat){
			List<Place> places = placesByCategory.get(cat);
			for(Place p: places){
				p.getIcon().setVisible(true);
				p.getCategory().setHidden(false);
			}
		}
	}

	class NewMap implements ActionListener {
		private JFileChooser fc = new JFileChooser();
		public void actionPerformed(ActionEvent e) {
			int returnValue = fc.showOpenDialog(Program.this);		
			if(returnValue==JFileChooser.APPROVE_OPTION) {
				if(m != null && !isSaved){
					int choice = JOptionPane.showConfirmDialog(null, "There are unsaved changes. Are you sure you want to load a new map?", "Warning", JOptionPane.WARNING_MESSAGE,
							JOptionPane.YES_NO_CANCEL_OPTION);
					if(choice == JOptionPane.CANCEL_OPTION){
						return;
					} else {
						try {
							clearAllData();
						} catch (IOException e1) {
							System.out.println("IOException " + e1.getMessage());
						}
						loadMap();
					}
				} else {
					loadMap();
				}
			}
		}

		
		private void loadMap(){
			File chosenFile = fc.getSelectedFile();
			try {
				addMapToFrame(chosenFile);
			} catch (IOException e1) {
				System.out.println("IOException " + e1.getMessage());
			}
		}
	}
	
	
	class HideCategory implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {			
			List<Place> places = placesByCategory.get(categoryArea.getSelectedValue());
			for(Place p: places){
				p.getIcon().setVisible(false);
				p.getCategory().setHidden(true);		
			}	
		}
	}

	class NewPlace extends MouseAdapter implements ActionListener {
		private boolean active;
		@Override
		public void actionPerformed(ActionEvent e) {
			active = true;
			m.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					int x = e.getX();
					int y = e.getY();
					Position pos = new Position(x, y);
					if(active){
						if(namedBox.isSelected()){
							createNamedPlace(pos);
						}
						if(describedBox.isSelected()){
							createDescPlace(pos);
						}
					} else {
						e.consume();
					}
					setCursor(DEFAULT_CURSOR);
					active = false;
				}
				
			});
			setCursor(CROSSHAIR_CURSOR);
		}

		
		private void createDescPlace(Position pos) {
			DescribedPlaceForm form = new DescribedPlaceForm();
			int choice = JOptionPane.showConfirmDialog(Program.this, form, "New place with description", JOptionPane.OK_CANCEL_OPTION);
			String name = form.getName();
			String desc = form.getDesc();
			Category c;
			if(categoryArea.isSelectionEmpty()){
				c = none;
			} else {
				c = categoryArea.getSelectedValue();
			}
			if(choice == JOptionPane.OK_OPTION){
				if(inputAllowed(name) && inputAllowed(desc)){
					DescribedPlace dp = new DescribedPlace(c, name, pos, desc, "Described");
					isSaved = false;
					putToMap(c, dp, name, pos);
					addPlaceToMap(dp);					
				} else {
					JOptionPane.showMessageDialog(Program.this, "The place needs a name and a description!");
				}
			}
		}

		private void createNamedPlace(Position pos) {
			JPanel namedForm = new JPanel();
			JTextField nameField = new JTextField(20);
			namedForm.add(nameField);
			int choice = JOptionPane.showConfirmDialog(Program.this, namedForm, "Ny plats", JOptionPane.OK_CANCEL_OPTION);
			Category c;
			if(categoryArea.isSelectionEmpty()){
				c = none;
			} else {
				c = categoryArea.getSelectedValue();
			}
			if(choice == JOptionPane.OK_OPTION){
				if(inputAllowed(nameField.getText())){
					NamedPlace p = new NamedPlace(c, nameField.getText(), pos, "Named");
					isSaved = false;
					putToMap(c, p, nameField.getText(), pos);
					addPlaceToMap(p);					
				} else {
					JOptionPane.showMessageDialog(Program.this, "The place needs a name!");
				}
			}
		}
		
		private boolean inputAllowed(String input){
			if(input.trim().isEmpty()){
				return false;
			} else {
				return true;
			}
		}
		
		private void addPlaceToMap(Place p){
			m.add(p.getIcon(), new Integer(1));
			validate();
		}

		private void putToMap(Category c, Place p, String name, Position pos){
			if(placesByCategory.containsKey(c) && placesByCategory.get(c) != null){
				placesByCategory.get(c).add(p);
			} else {
				List<Place> list = new ArrayList<Place>();
				list.add(p);
				placesByCategory.put(c, list);
			}
			
			if(placesByName.containsKey(name) && placesByName.get(name) != null){
				placesByName.get(name).add(p);
			} else {
				List<Place> list = new ArrayList<Place>();
				list.add(p);
				placesByName.put(name, list);
			}
			
			if(placesByPosition.containsKey(pos) && placesByPosition.get(pos) != null){
				JOptionPane.showMessageDialog(Program.this, "This position is occupied");
			} else {
				placesByPosition.put(pos, p);
			}
		}
	}
	
	class HideMarked implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			List<Place> toHide = new ArrayList<Place>();
			toHide.addAll(markedPlaces);
			for(Place p: toHide){
				p.getIcon().setVisible(false);
				p.getIcon().markLocation(false);
			}
		}
	}
	
	class ExitProgram implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e){
			if(!isSaved){
				int choice = JOptionPane.showConfirmDialog(null, "There are unsaved changes. Are you sure you want to exit?", "Warning", JOptionPane.WARNING_MESSAGE,
						JOptionPane.YES_NO_CANCEL_OPTION);
				if(choice==JOptionPane.YES_OPTION){
					System.exit(0);
				} else {
					return;
				}
			} else {
				System.exit(0);
			}
		}
	}
	
	class CoordinateFinder implements ActionListener {		
		@Override
		public void actionPerformed(ActionEvent e){
			JPanel coordsPanel = new JPanel();
			coordsPanel.setLayout(new BoxLayout(coordsPanel, BoxLayout.Y_AXIS));
			JPanel row1 = new JPanel();
			JPanel row2 = new JPanel();
			JTextField xField = new JTextField(20);
			JTextField yField = new JTextField(20);
			row1.add(new JLabel("X: "));
			row1.add(xField);
			row2.add(new JLabel("Y: "));
			row2.add(yField);
			coordsPanel.add(row1);
			coordsPanel.add(row2);
			int choice = JOptionPane.showConfirmDialog(Program.this, coordsPanel, "Enter coordinates", JOptionPane.OK_CANCEL_OPTION);
			if(choice != JOptionPane.OK_OPTION) {
				return;
			}
			try {
				int x = Integer.parseInt(xField.getText().trim());
				int y = Integer.parseInt(yField.getText().trim());
				findPosition(new Position(x, y));
			} catch(NumberFormatException ex){
				JOptionPane.showMessageDialog(null, "Not a number!");
			}
		}
		
		private void findPosition(Position pos){
			Place foundPlace = null;
			if(markedPlaces != null){
				List<Place> toHide = new ArrayList<Place>();
				for(Place p: markedPlaces){
					toHide.add(p);
				}
				for(Place p: toHide){
					p.getIcon().markLocation(false);
					m.repaint();
				}
			}
			
			if(placesByPosition.get(pos)==null){
				JOptionPane.showMessageDialog(null, "No location found!");
			} else {
				foundPlace = placesByPosition.get(pos);
				foundPlace.getIcon().setVisible(true);
				foundPlace.getIcon().markLocation(true);
			}
			
		}
	}
	
	class RemoveMarked implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {		
			List<Place> toRemove = new ArrayList<Place>();
			toRemove.addAll(markedPlaces);
			for(Place p: toRemove){
				m.remove(p.getIcon());
				placesByName.get(p.getName()).remove(p);
				placesByPosition.remove(p);
				placesByCategory.get(p.getCategory()).remove(p);
				m.repaint();
			}
			if(!toRemove.isEmpty()){
				isSaved = false;
			}
		}
	}
	
		class SearchListener implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<Place> toChange = new ArrayList<Place>();
				toChange.addAll(markedPlaces);
				for (Place p : toChange) {
					p.getIcon().markLocation(false);
					m.repaint();
				}
				if (searchField.getText() != null) {
					List<Place> places = placesByName.get(searchField.getText().toString());
					if (places != null) {
						for (Place p : places) {
							p.getIcon().setVisible(true);
							p.getIcon().markLocation(true);
							m.repaint();
						}
					}
				}
			}

		}



	public static void main(String[] args) {
		new Program();
	}
}
