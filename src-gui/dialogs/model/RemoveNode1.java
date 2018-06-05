package dialogs.model;

import data.Group;
import element.Element;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import node.Node;

/**
 * Class for Remove Node Model menu.
 * 
 * @author Murat
 * 
 */
public class RemoveNode1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_;

	private JComboBox combobox1_;

	/** Vector for storing node ids. */
	private Vector<Integer> values_ = new Vector<Integer>();

	private JButton button1_, button2_;

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
	public RemoveNode1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Remove Nodes", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Nodes", Commons.gridbag_);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(100, 20));

		// get the groups
		String[] groups = getGroups();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Node IDs :", true);
		radiobutton2_ = new JRadioButton("Groups :", false);
		radiobutton3_ = new JRadioButton("All existing nodes", false);

		// check if there is any group
		if (groups.length == 0)
			radiobutton2_.setEnabled(false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);

		// build comboboxes
		combobox1_ = new JComboBox(groups);
		combobox1_.setMaximumRowCount(3);
		combobox1_.setEnabled(false);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton3_, 2, 0, 2, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		radiobutton1_.addActionListener(this);
		radiobutton2_.addActionListener(this);
		radiobutton3_.addActionListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					actionOk();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// cancel button clicked
		else if (e.getSource().equals(button2_)) {

			// set dialog unvisible
			setVisible(false);
		}

		// Node ids radio button clicked
		else if (e.getActionCommand() == "Node IDs :") {

			// set textfield editable and combobox disabled
			textfield1_.setEditable(true);
			combobox1_.setEnabled(false);
		}

		// Group radio button clicked
		else if (e.getActionCommand() == "Groups :") {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(true);
		}

		// All existing nodes button clicked
		else if (e.getSource().equals(radiobutton3_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(false);
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

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check node
		progressor_.setStatusMessage("Checking data...");
		if (checkNode()) {

			// sort removal nodes array
			sort();

			// remove demanded nodes from groups
			removeFromGroups();

			// remove demanded nodes from structure
			for (int i = 0; i < values_.size(); i++)
				owner_.structure_.removeNode(values_.get(i));

			// draw
			progressor_.setStatusMessage("Drawing...");
			owner_.drawPre();

			// close progressor
			progressor_.close();

			// close dialog
			setVisible(false);
		}
	}

	/**
	 * Checks whether any node of the structure has the given id or not. And
	 * also checks for other invalid values entered.
	 * 
	 * @return True if the given node ids are valid for removal, False vice
	 *         versa.
	 */
	private boolean checkNode() {

		// set node ids from textfield
		if (radiobutton1_.isSelected()) {
			if (setIdsFromText() == false)
				return false;
		}

		// set node ids from groups combobox
		else if (radiobutton2_.isSelected())
			setIdsFromGroup();

		// set node ids for all existing nodes
		else if (radiobutton3_.isSelected())
			setIdsForAll();

		// check for existance and connectivity
		try {

			// check if given nodes exist
			for (int i = 0; i < values_.size(); i++)
				owner_.structure_.getNode(values_.get(i));

			// loop over given nodes
			for (int i = 0; i < values_.size(); i++) {

				// get node to be checked
				Node node = owner_.structure_.getNode(values_.get(i));

				// loop over elements
				for (int j = 0; j < owner_.structure_.getNumberOfElements(); j++) {

					// get element and its nodes
					Element e = owner_.structure_.getElement(j);
					Node[] nodes = e.getNodes();

					// loop over nodes of element
					for (int k = 0; k < nodes.length; k++) {

						// check if element node is the same with the given node
						if (nodes[k].equals(node)) {

							// close progressor
							progressor_.close();
							setStill(false);

							// display message
							JOptionPane.showMessageDialog(RemoveNode1.this,
									"Nodes are connected to elements!",
									"False data entry", 2);
							return false;
						}
					}
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(RemoveNode1.this,
					"Given nodes do not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Sorts vector in decresing order.
	 * 
	 */
	private void sort() {

		// eliminate duplicate values
		for (int i = 0; i < values_.size() - 1; i++) {
			for (int j = i + 1; j < values_.size(); j++) {
				int k = values_.get(i);
				int l = values_.get(j);
				if (k == l)
					values_.remove(j);
			}
		}

		// sort items of vector with decreasing order
		for (int i = 0; i < values_.size() - 1; i++) {
			for (int j = i + 1; j < values_.size(); j++) {
				int k = values_.get(i);
				int l = values_.get(j);
				if (k < l) {
					int m = k;
					values_.setElementAt(values_.get(j), i);
					values_.setElementAt(m, j);
				}
			}
		}
	}

	/**
	 * Removes demanded nodes from related groups.
	 * 
	 */
	private void removeFromGroups() {

		// loop over removal node ids
		for (int i = 0; i < values_.size(); i++) {

			// get node
			Node node = owner_.structure_.getNode(values_.get(i));

			// loop over groups
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains node
				if (group.containsNode(node))
					group.removeNode(node);
			}
		}
	}

	/**
	 * Returns group names array.
	 * 
	 * @return The group names array.
	 */
	private String[] getGroups() {

		// initialize group names array
		int n = owner_.inputData_.getGroup().size();
		String[] groups = new String[n];

		// loop over groups
		for (int i = 0; i < n; i++) {

			// get name of group
			groups[i] = owner_.inputData_.getGroup().get(i).getName();
		}

		// return group names array
		return groups;
	}

	/**
	 * Checks and sets removal node ids from textfield.
	 * 
	 * @return True if entered text is correct, False vice versa.
	 */
	private boolean setIdsFromText() {

		// get the entered text
		String text = textfield1_.getText();

		// eliminate spaces
		String elText = "";
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != " ".charAt(0))
				elText += c;
		}

		// check for non-numeric or negative values
		try {

			// seperate components
			String[] comp = elText.split(",");

			// convert text to integer value
			values_.clear();
			for (int i = 0; i < comp.length; i++) {
				values_.add(Integer.parseInt(comp[i]));

				// check if negative
				if (values_.get(i) < 0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(RemoveNode1.this,
							"Illegal values!", "False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(RemoveNode1.this,
					"Given nodes do not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Sets removal node ids from groups combobox.
	 * 
	 */
	private void setIdsFromGroup() {

		// get the selected group
		int index = combobox1_.getSelectedIndex();
		Group group = owner_.inputData_.getGroup().get(index);

		// get nodes of group
		Vector<Node> nodes = group.getNodes();

		// get indices of group nodes
		values_.clear();
		for (int i = 0; i < nodes.size(); i++) {

			// get the index of group node
			int n = owner_.structure_.indexOfNode(nodes.get(i));

			// add to values vector
			values_.add(n);
		}
	}

	/**
	 * Sets removal node ids for all nodes.
	 * 
	 */
	private void setIdsForAll() {

		// get indices of all nodes
		values_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++)
			values_.add(i);
	}
}
