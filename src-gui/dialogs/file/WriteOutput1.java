package dialogs.file;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel; // import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import main.Commons;

import write.WriteManager;

/**
 * Class for Write Output Tables menu.
 * 
 * @author Murat
 * 
 */
public class WriteOutput1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_;

	private JButton button1_, button2_, button3_, button4_, button5_;

	private JRadioButton radiobutton1_, radiobutton2_;

	private JList list1_;

	private DefaultListModel listModel1_;

	private JTree tree1_;

	/** Vector for storing steps for writing. */
	private Vector<Integer> steps_ = new Vector<Integer>();

	/** The progress monitor of processses that take place. */
	private Progressor progressor_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public WriteOutput1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Write Output Tables", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Output File", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Stepping Info", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("File :");
		JLabel label2 = new JLabel("Stations (1D elements) :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField("3");
		textfield3_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(190, 20));
		textfield2_.setPreferredSize(new Dimension(174, 20));

		// build buttons
		button1_ = new JButton("Browse");
		button2_ = new JButton("Add");
		button3_ = new JButton("Remove");
		button4_ = new JButton("Write");
		button5_ = new JButton("Close");

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Direct :", true);
		radiobutton2_ = new JRadioButton("Interval :", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// build list
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// create root node and build tree
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(
				"Output Options");
		createNodes(top);
		tree1_ = new JTree(top);
		tree1_.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		// build scroll panes and add lists to them
		JScrollPane scrollpane2 = new JScrollPane(tree1_);

		// set scrollpane constants
		scrollpane2.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane2.setHorizontalScrollBarPolicy(horizontalConstant);

		// Create a split pane with the two scroll panes in it
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				scrollpane2, scrollpane1);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		splitPane.setPreferredSize(new Dimension(300, 200));

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 200);
		scrollpane2.setMinimumSize(minimumSize);
		scrollpane1.setMinimumSize(minimumSize);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button1_, 0, 2, 1, 1);
		Commons.addComponent(panel4, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel4, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel4, label2, 2, 0, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel5, splitPane, 0, 0, 5, 1);
		Commons.addComponent(panel5,
				Box.createRigidArea(new Dimension(40, 23)), 1, 0, 1, 1);
		Commons.addComponent(panel5, button2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, button3_, 1, 2, 1, 1);
		Commons.addComponent(panel5, button4_, 1, 3, 1, 1);
		Commons.addComponent(panel5,
				Box.createRigidArea(new Dimension(39, 23)), 1, 4, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		panel2.add(button5_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);
		button5_.addActionListener(this);
		radiobutton1_.addActionListener(this);
		radiobutton2_.addActionListener(this);

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Creates the nodes under the root node.
	 * 
	 * @param top
	 *            Root node.
	 */
	private void createNodes(DefaultMutableTreeNode top) {

		// initialize nodes
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode item1 = null;
		DefaultMutableTreeNode item2 = null;
		DefaultMutableTreeNode item3 = null;

		// create Structure category
		category = new DefaultMutableTreeNode("Structure");
		top.add(category);

		// add items of Structure category
		item1 = new DefaultMutableTreeNode("Structure Info");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Analysis Info");
		category.add(item1);

		// create Nodes category
		category = new DefaultMutableTreeNode("Nodes");
		top.add(category);

		// add items of Nodes category
		item1 = new DefaultMutableTreeNode("Position");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Nodal Springs");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Nodal Masses");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Nodal Local Axes");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Boundary Conditions");
		category.add(item1);
		item2 = new DefaultMutableTreeNode("Constraints");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Nodal Mechanical Loads");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Displacement Loads");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Initial Conditions");
		category.add(item1);
		item2 = new DefaultMutableTreeNode("Initial Displacements");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Initial Velocities");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Results");
		category.add(item1);
		item2 = new DefaultMutableTreeNode("Displacements");
		item1.add(item2);
		item3 = new DefaultMutableTreeNode("Global Displacements");
		item2.add(item3);
		item3 = new DefaultMutableTreeNode("Local Displacements");
		item2.add(item3);
		item2 = new DefaultMutableTreeNode("Reaction Forces");
		item1.add(item2);
		item3 = new DefaultMutableTreeNode("Global Reaction Forces");
		item2.add(item3);
		item3 = new DefaultMutableTreeNode("Local Reaction Forces");
		item2.add(item3);

		// create Elements category
		category = new DefaultMutableTreeNode("Elements");
		top.add(category);

		// add items of Elements category
		item1 = new DefaultMutableTreeNode("Element Info");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Material");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Section");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Element Springs");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Element Masses");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Element Local Axes");
		category.add(item1);
		item1 = new DefaultMutableTreeNode("Boundary Conditions");
		category.add(item1);
		item2 = new DefaultMutableTreeNode("Element Mechanical Loads");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Temperature Loads");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Results");
		category.add(item1);
		item2 = new DefaultMutableTreeNode("Displacements");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Elastic Strains");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Stresses");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Internal Forces");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Principle Strains");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Principle Stresses");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Mises Stress");
		item1.add(item2);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// browse button clicked
		if (e.getSource().equals(button1_))
			browse();

		// add button clicked
		else if (e.getSource().equals(button2_))
			add();

		// remove button clicked
		else if (e.getSource().equals(button3_))
			remove();

		// write button clicked
		else if (e.getSource().equals(button4_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					write();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// close button clicked
		else if (e.getSource().equals(button5_))
			setVisible(false);

		// direct radio button clicked
		else if (e.getSource().equals(radiobutton1_)) {

			// set textfields
			textfield2_.setEditable(true);
			textfield3_.setEditable(false);
		}

		// interval radio button clicked
		else if (e.getSource().equals(radiobutton2_)) {

			// set textfields
			textfield2_.setEditable(false);
			textfield3_.setEditable(true);
		}
	}

	/**
	 * Performs task for the write button.
	 * 
	 */
	private void write() {

		// item(s) are selected
		if (listModel1_.isEmpty() == false) {

			// check model
			progressor_.setStatusMessage("Checking model...");
			String message = owner_.structure_.checkModel(0);

			// problem occured with the model
			if (message != null) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message, "Error", 2);
			}

			// no problem with the model
			else {

				// write
				try {

					// get path
					String path = textfield1_.getText();

					// create write manager
					WriteManager writer = new WriteManager(owner_.structure_,
							path);

					// structure info
					if (listModel1_.contains("Structure Info")) {
						progressor_
								.setStatusMessage("Writing structure info...");
						writer.write(WriteManager.structureInfo_);
					}

					// analysis info
					if (listModel1_.contains("Analysis Info")) {
						progressor_
								.setStatusMessage("Writing analysis info...");
						writer.write(WriteManager.analysisInfo_);
					}

					// nodal position
					if (listModel1_.contains("Position")) {
						progressor_
								.setStatusMessage("Writing nodal positions...");
						writer.write(WriteManager.nodalPosInfo_);
					}

					// nodal springs
					if (listModel1_.contains("Nodal Springs")) {
						progressor_
								.setStatusMessage("Writing nodal springs...");
						writer.write(WriteManager.nodalSpringInfo_);
					}

					// nodal masses
					if (listModel1_.contains("Nodal Masses")) {
						progressor_.setStatusMessage("Writing nodal masses...");
						writer.write(WriteManager.nodalMassInfo_);
					}

					// nodal local axes
					if (listModel1_.contains("Nodal Local Axes")) {
						progressor_
								.setStatusMessage("Writing nodal local axes...");
						writer.write(WriteManager.nodalLocalAxisInfo_);
					}

					// constraints
					if (listModel1_.contains("Constraints")) {
						progressor_.setStatusMessage("Writing constraints...");
						writer.write(WriteManager.constraintInfo_);
					}

					// nodal mechanical loads
					if (listModel1_.contains("Nodal Mechanical Loads")) {
						progressor_
								.setStatusMessage("Writing nodal mechanical loads...");
						writer.write(WriteManager.nodalMechLoadInfo_);
					}

					// displacement loads
					if (listModel1_.contains("Displacement Loads")) {
						progressor_
								.setStatusMessage("Writing displacement loads...");
						writer.write(WriteManager.dispLoadInfo_);
					}

					// initial displacements
					if (listModel1_.contains("Initial Displacements")) {
						progressor_
								.setStatusMessage("Writing initial displacements...");
						writer.write(WriteManager.initialDispInfo_);
					}

					// initial velocities
					if (listModel1_.contains("Initial Velocities")) {
						progressor_
								.setStatusMessage("Writing initial velocities...");
						writer.write(WriteManager.initialVeloInfo_);
					}

					// element info
					if (listModel1_.contains("Element Info")) {
						progressor_.setStatusMessage("Writing element info...");
						writer.write(WriteManager.elementInfo_);
					}

					// material
					if (listModel1_.contains("Material")) {
						progressor_.setStatusMessage("Writing materials...");
						writer.write(WriteManager.materialInfo_);
					}
					// section
					if (listModel1_.contains("Section")) {
						progressor_.setStatusMessage("Writing sections...");
						writer.write(WriteManager.sectionInfo_);
					}

					// element springs
					if (listModel1_.contains("Element Springs")) {
						progressor_
								.setStatusMessage("Writing element springs...");
						writer.write(WriteManager.elementSpringInfo_);
					}

					// element masses
					if (listModel1_.contains("Element Masses")) {
						progressor_
								.setStatusMessage("Writing element masses...");
						writer.write(WriteManager.elementAdditionalMassInfo_);
					}

					// element local axes
					if (listModel1_.contains("Element Local Axes")) {
						progressor_
								.setStatusMessage("Writing element local axes...");
						writer.write(WriteManager.elementLocalAxisInfo_);
					}

					// element mechanical loads
					if (listModel1_.contains("Element Mechanical Loads")) {
						progressor_
								.setStatusMessage("Writing element mechanical loads...");
						writer.write(WriteManager.elementMechLoadInfo_);
					}

					// temperature loads
					if (listModel1_.contains("Temperature Loads")) {
						progressor_
								.setStatusMessage("Writing element temperature loads...");
						writer.write(WriteManager.tempLoadInfo_);
					}

					// check if any result options selected
					if (isResultOptionsEmpty() == false) {

						// check stepping info
						progressor_
								.setStatusMessage("Checking stepping info...");
						if (checkTexts()) {

							// set number of stations
							writer.setNumberOfOutputStations(Integer
									.parseInt(textfield4_.getText()));

							// loop over demanded steps
							for (int i = 0; i < steps_.size(); i++) {

								// set step number to structure
								progressor_
										.setStatusMessage("Setting step number..."
												+ steps_.get(i));
								owner_.structure_.setStep(owner_.path_, steps_
										.get(i));

								// write step info
								writer.writeStepInfo(steps_.get(i));

								// nodal global displacements
								if (listModel1_
										.contains("Global Displacements")) {
									progressor_
											.setStatusMessage("Writing global nodal displacements...");
									writer
											.write(WriteManager.globalNodeDispInfo_);
								}

								// nodal local displacements
								if (listModel1_.contains("Local Displacements")) {
									progressor_
											.setStatusMessage("Writing local nodal displacements...");
									writer
											.write(WriteManager.localNodeDispInfo_);
								}

								// global reaction forces
								if (listModel1_
										.contains("Global Reaction Forces")) {
									progressor_
											.setStatusMessage("Writing global reaction forces...");
									writer
											.write(WriteManager.globalReactionForceInfo_);
								}

								// local reaction forces
								if (listModel1_
										.contains("Local Reaction Forces")) {
									progressor_
											.setStatusMessage("Writing local reaction forces...");
									writer
											.write(WriteManager.localReactionForceInfo_);
								}

								// element displacements
								if (listModel1_.contains("Displacements")) {
									progressor_
											.setStatusMessage("Writing element displacements...");
									writer.write(WriteManager.elementDispInfo_);
								}

								// elastic strains
								if (listModel1_.contains("Elastic Strains")) {
									progressor_
											.setStatusMessage("Writing elastic strains...");
									writer
											.write(WriteManager.elasticStrainInfo_);
								}

								// stresses
								if (listModel1_.contains("Stresses")) {
									progressor_
											.setStatusMessage("Writing stresses...");
									writer.write(WriteManager.stressInfo_);
								}

								// internal forces
								if (listModel1_.contains("Internal Forces")) {
									progressor_
											.setStatusMessage("Writing internal forces...");
									writer
											.write(WriteManager.internalForceInfo_);
								}

								// principle strains
								if (listModel1_.contains("Principle Strains")) {
									progressor_
											.setStatusMessage("Writing principle strains...");
									writer
											.write(WriteManager.principleStrainInfo_);
								}

								// principle stresses
								if (listModel1_.contains("Principle Stresses")) {
									progressor_
											.setStatusMessage("Writing principle stresses...");
									writer
											.write(WriteManager.principleStressInfo_);
								}

								// misses stress
								if (listModel1_.contains("Mises Stress")) {
									progressor_
											.setStatusMessage("Writing mises stresses...");
									writer.write(WriteManager.misesStressInfo_);
								}
							}
						}
					}

					// close progressor
					progressor_.close();
					setStill(false);
				}

				// exception occurred during writing
				catch (Exception e) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							e.getLocalizedMessage(), "False data entry", 2);
				}
			}
		}

		// no item selected
		else {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "No output option selected!",
					"False data entry", 2);
		}
	}

	/**
	 * Checks whether result option selections is empty or not.
	 * 
	 * @return True if empty.
	 */
	private boolean isResultOptionsEmpty() {

		// nodal global displacements
		if (listModel1_.contains("Global Displacements"))
			return false;

		// nodal local displacements
		if (listModel1_.contains("Local Displacements"))
			return false;

		// global reaction forces
		if (listModel1_.contains("Global Reaction Forces"))
			return false;

		// local reaction forces
		if (listModel1_.contains("Local Reaction Forces"))
			return false;

		// element displacements
		if (listModel1_.contains("Displacements"))
			return false;

		// elastic strains
		if (listModel1_.contains("Elastic Strains"))
			return false;

		// stresses
		if (listModel1_.contains("Stresses"))
			return false;

		// internal forces
		if (listModel1_.contains("Internal Forces"))
			return false;

		// principle strains
		if (listModel1_.contains("Principle Strains"))
			return false;

		// principle stresses
		if (listModel1_.contains("Principle Stresses"))
			return false;

		// mises stress
		if (listModel1_.contains("Mises Stress"))
			return false;

		return true;
	}

	/**
	 * Returns True if enetered text fields are correct.
	 * 
	 * @return True if enetered text fields are correct.
	 */
	private boolean checkTexts() {

		// initialize error messages
		String err1 = "Illegal stepping info!";
		String err2 = "Illegal number of stations!";
		String err3 = "Illegal values!";
		String cap = "False data entry";

		// get the entered texts
		String text1 = null;
		if (radiobutton1_.isSelected())
			text1 = textfield2_.getText();
		else
			text1 = textfield3_.getText();
		String text2 = textfield4_.getText();

		// eliminate spaces
		String elText = "";
		for (int i = 0; i < text1.length(); i++) {
			char c = text1.charAt(i);
			if (c != " ".charAt(0))
				elText += c;
		}

		// check for non-numeric or negative values
		try {

			// convert number of stations to integer
			int stations = Integer.parseInt(text2);

			// check stations
			if (stations <= 1) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, err2, cap, 2);
				return false;
			}

			// seperate components
			String[] comp = elText.split(",");

			// get number of available steps
			int limit = owner_.structure_.getNumberOfSteps();

			// convert text to integer values
			steps_.clear();

			// direct
			if (radiobutton1_.isSelected()) {

				// loop over items
				for (int i = 0; i < comp.length; i++) {

					// set value
					steps_.add(Integer.parseInt(comp[i]));

					// check if negative
					if (steps_.get(i) < 0) {

						// close progressor and enable dialog
						progressor_.close();
						setStill(false);

						// display message
						JOptionPane.showMessageDialog(this, err1, cap, 2);
						return false;
					}

					// check step number
					if (steps_.get(i) >= limit) {

						// close progressor and enable dialog
						progressor_.close();
						setStill(false);

						// display message
						JOptionPane.showMessageDialog(this, err1, cap, 2);
						return false;
					}
				}
			}

			// interval
			else {

				// check number of items
				if (comp.length != 2) {

					// close progressor and enable dialog
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this, err1, cap, 2);
					return false;
				}

				// check bounds
				for (int i = 0; i < comp.length; i++) {
					int val = Integer.parseInt(comp[i]);

					// check if negative
					if (val < 0) {

						// close progressor and enable dialog
						progressor_.close();
						setStill(false);

						// display message
						JOptionPane.showMessageDialog(this, err1, cap, 2);
						return false;
					}

					// check if larger than limit
					if (val >= limit) {

						// close progressor and enable dialog
						progressor_.close();
						setStill(false);

						// display message
						JOptionPane.showMessageDialog(this, err1, cap, 2);
						return false;
					}
				}

				// get bounds
				int start = Integer.parseInt(comp[0]);
				int end = Integer.parseInt(comp[1]);

				// check for mutual suitibility
				if (start >= end) {

					// close progressor and enable dialog
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this, err1, cap, 2);
					return false;
				}

				// set values
				for (int i = start; i <= end; i++)
					steps_.add(i);
			}

		} catch (Exception excep) {

			// close progressor and enable dialog
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, err3, cap, 2);
			return false;
		}

		return true;
	}

	/**
	 * Performs task for the browse button.
	 * 
	 */
	private void browse() {

		// create file chooser
		JFileChooser chooser = new JFileChooser();

		// add custom file filter
		chooser.addChoosableFileFilter(new FFilter2());

		// disable the default (Accept All) file filter.
		chooser.setAcceptAllFileFilterUsed(false);

		// show file chooser
		int val = chooser.showDialog(this, "Save");

		// open approved
		if (val == JFileChooser.APPROVE_OPTION) {

			// get selected file's path
			String path = chooser.getSelectedFile().getAbsolutePath();

			// append extension if necessary
			String extension = ".txt";
			if (path.length() >= extension.length() + 1)
				if (extension.equalsIgnoreCase(path
						.substring(path.length() - 4)) == false)
					path += extension;

			// set path to textfield
			textfield1_.setText(path);
		}
	}

	/**
	 * Sets the dialog still for displaying progressor.
	 * 
	 * @param arg0
	 *            True if still, False if not.
	 */
	private void setStill(boolean arg0) {

		// still
		if (arg0) {

			// disable buttons
			button1_.setEnabled(false);
			button2_.setEnabled(false);
			button3_.setEnabled(false);
			button4_.setEnabled(false);
			button5_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);
			button3_.setEnabled(true);
			button4_.setEnabled(true);
			button5_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Removes selected item from list.
	 * 
	 */
	private void remove() {

		// check if any item selected in list
		if (list1_.isSelectionEmpty() == false) {

			// get the selection index from list
			int selected = list1_.getSelectedIndex();

			// remove it from list
			listModel1_.remove(selected);
		}
	}

	/**
	 * Adds selected item from tree to list.
	 * 
	 */
	private void add() {

		// check if any item selected
		if (tree1_.isSelectionEmpty() == false) {

			// get selected node
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1_
					.getLastSelectedPathComponent();

			// check if leaf
			if (node.isLeaf()) {

				// get name of node
				String name = node.getUserObject().toString();

				// check if list contains
				if (listModel1_.contains(name) == false) {

					// add to list
					listModel1_.addElement(name);
				}
			}
		}
	}
}
