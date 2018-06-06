/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dialogs.model;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import data.Group;

import node.Node;
import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DMat;
import matrix.DVec;

/**
 * Class for Mirror Nodes Model menu.
 * 
 * @author Murat
 * 
 */
public class MirrorNode1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_;

	private JComboBox combobox1_;

	private JCheckBox checkbox1_;

	private JButton button1_, button2_, button3_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** Vector for storing node ids to be . */
	private Vector<Integer> nodeIds_ = new Vector<Integer>();

	/** Vector for storing nodes to be mirrored. */
	private Vector<Node> nodes1_ = new Vector<Node>();

	/**
	 * Array storing the mirror options. The sequence is; constraint, local
	 * axis, mechanial loads, displacement loads, initial displacements, initial
	 * velocities, springs, masses.
	 */
	protected boolean[] options_ = { true, true, true, true, true, true, true,
			true };

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
	public MirrorNode1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Mirror Nodes", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Nodes", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Mirror Options", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Mirror Plane", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("     X");
		JLabel label2 = new JLabel("     Y");
		JLabel label3 = new JLabel("     Z");
		JLabel label4 = new JLabel("Point 1 :");
		JLabel label5 = new JLabel("Point 2 :");
		JLabel label6 = new JLabel("Point 3 :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield8_ = new JTextField();
		textfield9_ = new JTextField();
		textfield10_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(103, 20));
		textfield2_.setPreferredSize(new Dimension(40, 20));
		textfield3_.setPreferredSize(new Dimension(40, 20));
		textfield4_.setPreferredSize(new Dimension(40, 20));

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

		// build checkboxes
		checkbox1_ = new JCheckBox("Pass assignments");
		checkbox1_.setSelected(true);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");
		button3_ = new JButton("Options");

		// add components to sub-panels
		Commons.addComponent(panel3, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton3_, 2, 0, 2, 1);
		Commons.addComponent(panel4, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, button3_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label1, 0, 1, 1, 1);
		Commons.addComponent(panel5, label2, 0, 2, 1, 1);
		Commons.addComponent(panel5, label3, 0, 3, 1, 1);
		Commons.addComponent(panel5, label4, 1, 0, 1, 1);
		Commons.addComponent(panel5, label5, 2, 0, 1, 1);
		Commons.addComponent(panel5, label6, 3, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 2, 1, 1);
		Commons.addComponent(panel5, textfield4_, 1, 3, 1, 1);
		Commons.addComponent(panel5, textfield5_, 2, 1, 1, 1);
		Commons.addComponent(panel5, textfield6_, 2, 2, 1, 1);
		Commons.addComponent(panel5, textfield7_, 2, 3, 1, 1);
		Commons.addComponent(panel5, textfield8_, 3, 1, 1, 1);
		Commons.addComponent(panel5, textfield9_, 3, 2, 1, 1);
		Commons.addComponent(panel5, textfield10_, 3, 3, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);
		radiobutton3_.addItemListener(this);
		checkbox1_.addItemListener(this);

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

		// options buttons clicked
		else if (e.getSource().equals(button3_)) {

			// create dialog and set visible
			MirrorNode2 dialog = new MirrorNode2(this);
			dialog.setVisible(true);
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
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 event
		if (event.getSource().equals(radiobutton1_)) {

			// set textfields editable and combobox disabled
			textfield1_.setEditable(true);
			combobox1_.setEnabled(false);
		}

		// radiobutton2 event
		else if (event.getSource().equals(radiobutton2_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(true);
		}

		// All existing nodes button clicked
		else if (event.getSource().equals(radiobutton3_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(false);
		}

		// checkbox1 event
		else if (event.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected())

				// set button3 disabled
				button3_.setEnabled(true);

			// not selected
			else if (checkbox1_.isSelected() == false)

				// set button3 disabled
				button3_.setEnabled(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check nodes
		progressor_.setStatusMessage("Checking data...");
		if (checkIds()) {

			// check texts
			if (checkTexts()) {

				// check if options clicked
				if (checkbox1_.isSelected() == false)
					for (int i = 0; i < options_.length; i++)
						options_[i] = false;

				// get entered values
				double[] point1 = new double[3];
				double[] point2 = new double[3];
				double[] point3 = new double[3];
				point1[0] = Double.parseDouble(textfield2_.getText());
				point1[1] = Double.parseDouble(textfield3_.getText());
				point1[2] = Double.parseDouble(textfield4_.getText());
				point2[0] = Double.parseDouble(textfield5_.getText());
				point2[1] = Double.parseDouble(textfield6_.getText());
				point2[2] = Double.parseDouble(textfield7_.getText());
				point3[0] = Double.parseDouble(textfield8_.getText());
				point3[1] = Double.parseDouble(textfield9_.getText());
				point3[2] = Double.parseDouble(textfield10_.getText());
				DVec p1 = new DVec(point1);
				DVec p2 = new DVec(point2);
				DVec p3 = new DVec(point3);

				// mirror nodes
				progressor_.setStatusMessage("Mirroring nodes...");
				mirrorNodes(p1, p2, p3);

				// draw
				progressor_.setStatusMessage("Drawing...");
				owner_.drawPre();

				// close progressor
				progressor_.close();

				// close dialog
				setVisible(false);
			}
		}
	}

	/**
	 * Mirrors nodes by the given plane points.
	 * 
	 * @param p1
	 *            Position vector of first node.
	 * @param p2
	 *            Position vector of second node.
	 * @param p3
	 *            Position vector of third node.
	 */
	private void mirrorNodes(DVec p1, DVec p2, DVec p3) {

		// compute plane constants
		DMat mat = new DMat(3, 3);
		for (int i = 0; i < 3; i++) {
			mat.set(0, i, p1.get(i));
			mat.set(1, i, p2.get(i));
			mat.set(2, i, p3.get(i));
		}
		DMat mat1 = mat.scale(1.0);
		DMat mat2 = mat.scale(1.0);
		DMat mat3 = mat.scale(1.0);
		for (int i = 0; i < 3; i++) {
			mat1.set(i, 0, 1.0);
			mat2.set(i, 1, 1.0);
			mat3.set(i, 2, 1.0);
		}
		double a = mat.determinant();
		double a1 = mat1.determinant();
		double a2 = mat2.determinant();
		double a3 = mat3.determinant();

		// loop over would be mirrored nodes list
		for (int i = 0; i < nodes1_.size(); i++) {

			// get node
			Node node = nodes1_.get(i);

			// create node
			createNode(node, a, a1, a2, a3);
		}
	}

	/**
	 * Creates new nodes.
	 * 
	 * @param nod
	 *            The node to be mirrored.
	 * @param a
	 *            First constant of mirror plane.
	 * @param a1
	 *            Second constant of mirror plane.
	 * @param a2
	 *            Third constant of mirror plane.
	 * @param a3
	 *            Fourth constant of mirror plane.
	 */
	private void createNode(Node nod, double a, double a1, double a2, double a3) {

		// compute position vector of new node
		DVec pos = nod.getPosition();
		double x0 = pos.get(0);
		double y0 = pos.get(1);
		double z0 = pos.get(2);
		DVec pos1 = new DVec(3);
		pos1.set(0, a1);
		pos1.set(1, a2);
		pos1.set(2, a3);
		double b = 2.0 * (a1 * x0 + a2 * y0 + a3 * z0 - a)
				/ (a1 * a1 + a2 * a2 + a3 * a3);
		pos = pos.subtract(pos1.scale(b));

		// check if there is any node at that coordinate
		if (checkCoordinates(pos)) {

			// create new node
			Node node = new Node(pos.get(0), pos.get(1), pos.get(2));

			// node properties to new node
			passNodeProperties(nod, node);

			// add new node to groups
			addToGroups(nod, node);

			// add node to structure
			owner_.structure_.addNode(node);
		}
	}

	/**
	 * Adds newly created node to original node's groups.
	 * 
	 * @param n1
	 *            The original node.
	 * @param n2
	 *            The new node.
	 */
	private void addToGroups(Node n1, Node n2) {

		// loop over groups
		for (int i = 0; i < owner_.inputData_.getGroup().size(); i++) {

			// get group
			Group group = owner_.inputData_.getGroup().get(i);

			// check if group contains original node
			if (group.containsNode(n1)) {

				// add new node to the group
				group.addNode(n2);
			}
		}
	}

	/**
	 * Passes node properties to newly created node.
	 * 
	 * @param n1
	 *            The original node.
	 * @param n2
	 *            The new node.
	 */
	private void passNodeProperties(Node n1, Node n2) {

		// constraint
		if (options_[0])
			if (n1.getAppliedConstraint() != null)
				n2.setConstraint(n1.getAppliedConstraint());

		// local axis
		if (options_[1])
			if (n1.getLocalAxis() != null)
				n2.setLocalAxis(n1.getLocalAxis());

		// mechanical loads
		if (options_[2])
			n2.setMechLoads(n1.getAllMechLoads());

		// displacement loads
		if (options_[3])
			n2.setDispLoads(n1.getAllDispLoads());

		// initial displacements
		if (options_[4])
			n2.setInitialDisp(n1.getAllInitialDisp());

		// initial velocities
		if (options_[5])
			n2.setInitialVelo(n1.getAllInitialVelo());

		// springs
		if (options_[6])
			n2.setSprings(n1.getSprings());

		// masses
		if (options_[7])
			n2.setMasses(n1.getMasses());
	}

	/**
	 * Checks whether given coordinates are occupied by a node of the structure
	 * or not.
	 * 
	 * @param pos
	 *            The coordinates to be checked.
	 * @return True if no nodes are at the given coordinates, False vice versa.
	 */
	private boolean checkCoordinates(DVec pos) {

		// loop over nodes of structure
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++) {

			// get nodal position vector and length
			Node node = owner_.structure_.getNode(i);
			DVec pos1 = node.getPosition();

			// check coordinates
			if (pos.subtract(pos1).l2Norm() <= tolerance_)
				return false;
		}

		// no node exists at the same coordinates
		return true;
	}

	/**
	 * Checks whether the delta textfields are correct or not.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkTexts() {

		// get the entered texts
		String text1 = textfield2_.getText();
		String text2 = textfield3_.getText();
		String text3 = textfield4_.getText();
		String text4 = textfield5_.getText();
		String text5 = textfield6_.getText();
		String text6 = textfield7_.getText();
		String text7 = textfield8_.getText();
		String text8 = textfield9_.getText();
		String text9 = textfield10_.getText();

		// check for non-numeric values
		try {

			// convert texts to double values
			double[] point1 = new double[3];
			double[] point2 = new double[3];
			double[] point3 = new double[3];
			point1[0] = Double.parseDouble(text1);
			point1[1] = Double.parseDouble(text2);
			point1[2] = Double.parseDouble(text3);
			point2[0] = Double.parseDouble(text4);
			point2[1] = Double.parseDouble(text5);
			point2[2] = Double.parseDouble(text6);
			point3[0] = Double.parseDouble(text7);
			point3[1] = Double.parseDouble(text8);
			point3[2] = Double.parseDouble(text9);

			// check if they form a plane
			DVec p1 = new DVec(point1);
			DVec p2 = new DVec(point2);
			DVec p3 = new DVec(point3);
			if (p1.subtract(p2).l2Norm() <= tolerance_
					|| p1.subtract(p3).l2Norm() <= tolerance_
					|| p2.subtract(p3).l2Norm() <= tolerance_) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Illegal values for mirror plane!", "False data entry",
						2);
				return false;
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this,
					"Illegal values for mirror plane!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks whether any node of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if the given node ids are valid for replicating, False vice
	 *         versa.
	 */
	private boolean checkIds() {

		// set node ids from textfield
		if (radiobutton1_.isSelected()) {
			if (setIdsFromText() == false)
				return false;
		}

		// set node ids from groups combobox
		else if (radiobutton2_.isSelected())
			setIdsFromGroup();

		// set element ids for all existing nodes
		else if (radiobutton3_.isSelected())
			setIdsForAll();

		// check for existance
		nodes1_.clear();
		try {

			// check if given nodes exist
			for (int i = 0; i < nodeIds_.size(); i++) {

				// get node
				Node node = owner_.structure_.getNode(nodeIds_.get(i));

				// add to would be moved node list
				nodes1_.add(node);
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "Given nodes do not exist!",
					"False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks and sets division element ids from textfield.
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
			nodeIds_.clear();
			for (int i = 0; i < comp.length; i++) {
				nodeIds_.add(Integer.parseInt(comp[i]));

				// check if negative
				if (nodeIds_.get(i) < 0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this, "Illegal values!",
							"False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "Given nodes do not exist!",
					"False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Sets division element ids from groups combobox.
	 * 
	 */
	private void setIdsFromGroup() {

		// get the selected group
		int index = combobox1_.getSelectedIndex();
		Group group = owner_.inputData_.getGroup().get(index);

		// get nodes of group
		Vector<Node> nodes = group.getNodes();

		// get indices of group nodes
		nodeIds_.clear();
		for (int i = 0; i < nodes.size(); i++) {

			// get the index of group node
			int n = owner_.structure_.indexOfNode(nodes.get(i));

			// add to values vector
			nodeIds_.add(n);
		}
	}

	/**
	 * Sets mesh element ids for all elements.
	 * 
	 */
	private void setIdsForAll() {

		// get indices of all elements
		nodeIds_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++)
			nodeIds_.add(i);
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
}
