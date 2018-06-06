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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import data.Group;

import element.Element;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DMat;
import matrix.DVec;
import node.Node;

/**
 * Class for Move Nodes Model menu.
 * 
 * @author Murat
 * 
 */
public class MoveNode1 extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_, textfield11_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton6_;

	private JComboBox combobox1_, combobox2_;

	private JTabbedPane tabbedpane1_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** Vector for storing node ids to be moved. */
	private Vector<Integer> nodeIds_ = new Vector<Integer>();

	/** Vector for storing nodes to be moved. */
	private Vector<Node> nodes1_ = new Vector<Node>();

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
	public MoveNode1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Move Nodes", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);
		JPanel panel3 = Commons.getPanel(null, Commons.gridbag_);

		// build sub-panels
		JPanel panel4 = Commons.getPanel("Nodes", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Move Nodes by", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Nodes", Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Move Nodes by", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Delta X :");
		JLabel label2 = new JLabel("Delta Y :");
		JLabel label3 = new JLabel("Delta Z :");
		JLabel label4 = new JLabel("Centroid :");
		JLabel label5 = new JLabel("Rotations :");
		JLabel label6 = new JLabel("      X");
		JLabel label7 = new JLabel("      Y");
		JLabel label8 = new JLabel("      Z");
		label6.setPreferredSize(new Dimension(42, 19));

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
		textfield11_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(116, 20));
		textfield2_.setPreferredSize(new Dimension(158, 20));
		textfield5_.setPreferredSize(new Dimension(116, 20));
		textfield6_.setPreferredSize(new Dimension(42, 20));
		textfield7_.setPreferredSize(new Dimension(42, 20));
		textfield8_.setPreferredSize(new Dimension(42, 20));

		// get the groups
		String[] groups = getGroups();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Node IDs :", true);
		radiobutton2_ = new JRadioButton("Groups :", false);
		radiobutton5_ = new JRadioButton("All existing nodes", false);
		radiobutton3_ = new JRadioButton("Node IDs :", true);
		radiobutton4_ = new JRadioButton("Groups :", false);
		radiobutton6_ = new JRadioButton("All existing nodes", false);

		// check if there is any group
		if (groups.length == 0) {
			radiobutton2_.setEnabled(false);
			radiobutton4_.setEnabled(false);
		}

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		ButtonGroup buttongroup2 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton5_);
		buttongroup2.add(radiobutton3_);
		buttongroup2.add(radiobutton4_);
		buttongroup2.add(radiobutton6_);

		// build comboboxes
		combobox1_ = new JComboBox(groups);
		combobox2_ = new JComboBox(groups);
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(3);
		combobox1_.setEnabled(false);
		combobox2_.setEnabled(false);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel4, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton5_, 2, 0, 2, 1);
		Commons.addComponent(panel5, label1, 0, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label2, 1, 0, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel5, label3, 2, 0, 1, 1);
		Commons.addComponent(panel5, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton3_, 0, 0, 1, 1);
		Commons.addComponent(panel6, textfield5_, 0, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton4_, 1, 0, 1, 1);
		Commons.addComponent(panel6, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton6_, 2, 0, 2, 1);
		Commons.addComponent(panel7, label6, 0, 1, 1, 1);
		Commons.addComponent(panel7, label7, 0, 2, 1, 1);
		Commons.addComponent(panel7, label8, 0, 3, 1, 1);
		Commons.addComponent(panel7, label4, 1, 0, 1, 1);
		Commons.addComponent(panel7, label5, 2, 0, 1, 1);
		Commons.addComponent(panel7, textfield6_, 1, 1, 1, 1);
		Commons.addComponent(panel7, textfield7_, 1, 2, 1, 1);
		Commons.addComponent(panel7, textfield8_, 1, 3, 1, 1);
		Commons.addComponent(panel7, textfield9_, 2, 1, 1, 1);
		Commons.addComponent(panel7, textfield10_, 2, 2, 1, 1);
		Commons.addComponent(panel7, textfield11_, 2, 3, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel4, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 1, 0, 1, 1);
		Commons.addComponent(panel3, panel6, 0, 0, 1, 1);
		Commons.addComponent(panel3, panel7, 1, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Linear", panel1);
		tabbedpane1_.addTab("Radial", panel3);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);
		radiobutton3_.addItemListener(this);
		radiobutton4_.addItemListener(this);
		radiobutton5_.addItemListener(this);
		radiobutton6_.addItemListener(this);

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

		// radiobutton3 event
		else if (event.getSource().equals(radiobutton3_)) {

			// set textfields editable and combobox disabled
			textfield5_.setEditable(true);
			combobox2_.setEnabled(false);
		}

		// radiobutton4 event
		else if (event.getSource().equals(radiobutton4_)) {

			// set textfield disabled and combobox enabled
			textfield5_.setEditable(false);
			combobox2_.setEnabled(true);
		}

		// radiobutton5 event
		else if (event.getSource().equals(radiobutton5_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(false);
		}

		// radiobutton6 event
		else if (event.getSource().equals(radiobutton6_)) {

			// set textfield disabled and combobox enabled
			textfield5_.setEditable(false);
			combobox2_.setEnabled(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check node ids
		progressor_.setStatusMessage("Checking data...");
		if (checkIds()) {

			// check texts
			if (checkTexts()) {

				// linear option selected
				if (tabbedpane1_.getSelectedIndex() == 0) {

					// get entered values
					double val1 = Double.parseDouble(textfield2_.getText());
					double val2 = Double.parseDouble(textfield3_.getText());
					double val3 = Double.parseDouble(textfield4_.getText());

					// move nodes
					progressor_.setStatusMessage("Moving nodes...");
					if (Math.abs(val1) + Math.abs(val2) + Math.abs(val3) != 0.0)
						moveNodes(val1, val2, val3);

					// draw
					progressor_.setStatusMessage("Drawing...");
					owner_.drawPre();

					// close progressor
					progressor_.close();

					// close dialog
					setVisible(false);
				}

				// radial option is selected
				else if (tabbedpane1_.getSelectedIndex() == 1) {

					// get entered values
					double val1 = Double.parseDouble(textfield6_.getText());
					double val2 = Double.parseDouble(textfield7_.getText());
					double val3 = Double.parseDouble(textfield8_.getText());
					double val4 = Double.parseDouble(textfield9_.getText());
					double val5 = Double.parseDouble(textfield10_.getText());
					double val6 = Double.parseDouble(textfield11_.getText());

					// move nodes
					progressor_.setStatusMessage("Moving nodes...");
					double val7 = Math.abs(val1) + Math.abs(val2)
							+ Math.abs(val3);
					double val8 = Math.abs(val4) + Math.abs(val5)
							+ Math.abs(val6);
					if (val7 != 0.0 || val8 != 0.0)
						rotateNodes(val1, val2, val3, val4, val5, val6);

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
	}

	/**
	 * Moves nodes by the given coordinate changes.
	 * 
	 * @param val1
	 *            Delta X value.
	 * @param val2
	 *            Delta Y value.
	 * @param val3
	 *            Delta Z value.
	 */
	private void moveNodes(double val1, double val2, double val3) {

		// create vector from change of coordinates
		DVec delta = new DVec(3);
		delta.set(0, val1);
		delta.set(1, val2);
		delta.set(2, val3);

		// loop over would be moved nodes list
		for (int i = 0; i < nodes1_.size(); i++) {

			// get node
			Node node = nodes1_.get(i);

			// create node
			createNode(node, delta, null);
		}
	}

	/**
	 * Rotates nodes by the given centroid and rotation angles.
	 * 
	 * @param val1
	 *            X coordinate of centroid.
	 * @param val2
	 *            Y coordinate of centroid.
	 * @param val3
	 *            Z coordinate of centroid.
	 * @param val4
	 *            Rotation about X axis.
	 * @param val5
	 *            Rotation about Y axis.
	 * @param val6
	 *            Rotation about Z axis.
	 */
	private void rotateNodes(double val1, double val2, double val3,
			double val4, double val5, double val6) {

		// create position vector of centroid
		DVec centroid = new DVec(3);
		centroid.set(0, val1);
		centroid.set(1, val2);
		centroid.set(2, val3);

		// create transformation matrix from rotation angles
		DMat tr = new DMat(val4, val5, val6, DMat.xyz_);

		// loop over would be rotated nodes list
		for (int i = 0; i < nodes1_.size(); i++) {

			// get node
			Node node = nodes1_.get(i);

			// create node
			createNode(node, centroid, tr);
		}
	}

	/**
	 * Sets new coordinates to node.
	 * 
	 * @param nod
	 *            The node to be moved.
	 * @param delta
	 *            Vector storing the change of coordinates.
	 * @param tr
	 *            The transformation matrix used for rotating nodes.
	 */
	private void createNode(Node nod, DVec delta, DMat tr) {

		// create vector for storing position of new node
		DVec pos = new DVec(3);

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// compute new position vector
			pos = nod.getPosition().add(delta);
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// compute new position vector
			pos = nod.getPosition().subtract(delta);
			pos = pos.transform(tr, DMat.toGlobal_);
			pos = pos.add(delta);
			for (int j = 0; j < 3; j++)
				if (Math.abs(pos.get(j)) <= tolerance_)
					pos.set(j, 0.0);
		}

		// check if there is any node at that coordinate
		if (checkCoordinates(pos)) {

			// set new position vector to the node
			nod.setPosition(pos);
		}
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
	 * Sets ids from texts or groups, checks whether given ids point any node in
	 * the structure, and finally checks whether those nodes are connected to
	 * elements or not.
	 * 
	 * @return True if the given node ids are valid for moving, False vice
	 *         versa.
	 */
	private boolean checkIds() {

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// set node ids from textfield
			if (radiobutton1_.isSelected()) {
				if (setIdsFromText() == false)
					return false;
			}

			// set node ids from groups combobox
			else if (radiobutton2_.isSelected())
				setIdsFromGroup();

			// set node ids for all nodes
			else if (radiobutton5_.isSelected())
				setIdsForAll();
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// set node ids from textfield
			if (radiobutton3_.isSelected()) {
				if (setIdsFromText() == false)
					return false;
			}

			// set node ids from groups combobox
			else if (radiobutton4_.isSelected())
				setIdsFromGroup();

			// set node ids for all nodes
			else if (radiobutton6_.isSelected())
				setIdsForAll();
		}

		// check for existance and connectivity
		nodes1_.clear();
		try {

			// check if given nodes exist
			for (int i = 0; i < nodeIds_.size(); i++) {

				// get node
				Node node = owner_.structure_.getNode(nodeIds_.get(i));

				// check for connectivity
				if (checkConnectivity(node)) {

					// add to would be moved node list
					nodes1_.add(node);
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
	 * Checks whether given node is connected to any element.
	 * 
	 * @return True if it is not connected to any element, False vice versa.
	 */
	private boolean checkConnectivity(Node node) {

		// loop over elements
		for (int j = 0; j < owner_.structure_.getNumberOfElements(); j++) {

			// get element and its nodes
			Element e = owner_.structure_.getElement(j);
			Node[] nodes = e.getNodes();

			// loop over nodes of element
			for (int k = 0; k < nodes.length; k++) {

				// check if node is the same with element node
				if (nodes[k].equals(node)) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							"Node is connected to elements!",
							"False data entry", 2);
					return false;
				}
			}
		}

		// node is not connected to any element
		return true;
	}

	/**
	 * Checks textfields for illegal values and sets them to ids vector.
	 * 
	 * @return True if entered texts are correct, False vice versa.
	 */
	private boolean setIdsFromText() {

		String text = null;

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// get the entered text
			text = textfield1_.getText();
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get the entered text
			text = textfield5_.getText();
		}

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
	 * Sets node ids from groups to ids vector.
	 * 
	 */
	private void setIdsFromGroup() {

		int index = 0;

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// get the selected group
			index = combobox1_.getSelectedIndex();
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get the selected group
			index = combobox2_.getSelectedIndex();
		}
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
	 * Checks whether the delta textfields are correct or not.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkTexts() {

		// linear option selected
		if (tabbedpane1_.getSelectedIndex() == 0) {

			// get the entered texts
			String text1 = textfield2_.getText();
			String text2 = textfield3_.getText();
			String text3 = textfield4_.getText();

			// check for non-numeric values
			try {

				// convert texts to double values
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text1);
				value = Double.parseDouble(text2);
				value = Double.parseDouble(text3);
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, "Illegal values!",
						"False data entry", 2);
				return false;
			}
		}

		// radial option is selected
		else if (tabbedpane1_.getSelectedIndex() == 1) {

			// get the entered texts
			String text1 = textfield6_.getText();
			String text2 = textfield7_.getText();
			String text3 = textfield8_.getText();
			String text4 = textfield9_.getText();
			String text5 = textfield10_.getText();
			String text6 = textfield11_.getText();

			// check for non-numeric values
			try {

				// convert texts to integer values
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text1);
				value = Double.parseDouble(text2);
				value = Double.parseDouble(text3);
				value = Double.parseDouble(text4);
				value = Double.parseDouble(text5);
				value = Double.parseDouble(text6);
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, "Illegal values!",
						"False data entry", 2);
				return false;
			}
		}

		// entered values are correct
		return true;
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
