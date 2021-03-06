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
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;

import data.Group;

import threeDim.MeshHexaElement;

import element.Element;
import element.Element3D;
import element.ElementLibrary;

/**
 * Class for Mesh Solid Model menu.
 * 
 * @author Murat
 * 
 */
public class MeshSolid1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_;

	private JComboBox combobox1_;

	/** The number of divisions. */
	private int m_, n_, k_;

	/** Vector for storing element ids to be meshed. */
	private Vector<Integer> elementIds_ = new Vector<Integer>();

	/** Vector for storing elements to be meshed. */
	private Vector<Element3D> elements1_ = new Vector<Element3D>();

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
	public MeshSolid1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Mesh Solids", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Solids", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Mesh", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Divisions in 1 :");
		JLabel label2 = new JLabel("Divisions in 2 :");
		JLabel label3 = new JLabel("Divisions in 3 :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(100, 20));
		textfield2_.setPreferredSize(new Dimension(105, 20));

		// get the groups
		String[] groups = getGroups();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Solid IDs :", true);
		radiobutton2_ = new JRadioButton("Groups :", false);
		radiobutton3_ = new JRadioButton("All existing solids", false);

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
		Commons.addComponent(panel4, label1, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 1, 0, 1, 1);
		Commons.addComponent(panel4, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel4, label3, 2, 0, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
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

		// Area ids radio button clicked
		else if (e.getActionCommand() == "Solid IDs :") {

			// set textfield editable and combobox disabled
			textfield1_.setEditable(true);
			combobox1_.setEnabled(false);
		}

		// Groups radio button clicked
		else if (e.getActionCommand() == "Groups :") {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(true);
		}

		// All existing areas button clicked
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

		// check element
		progressor_.setStatusMessage("Checking data...");
		if (checkIds()) {

			// check division number textfields
			if (checkDivisionNumbers()) {

				// mesh solid
				progressor_.setStatusMessage("Meshing...");
				meshInto();

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
	 * Meshes elements for mesh into option.
	 * 
	 */
	private void meshInto() {

		// create element meshers
		MeshHexaElement mesher1 = new MeshHexaElement(owner_.structure_,
				owner_.inputData_.getGroup());

		// loop over would be divided elements list
		for (int i = 0; i < elements1_.size(); i++) {

			// get element
			Element3D e = elements1_.get(i);

			// hexahedral element
			if (e.getGeometry() == Element3D.hexahedral_) {

				// mesh element
				mesher1.meshInto(e, m_, n_, k_);
			}
		}
	}

	/**
	 * Checks whether division numbers are correct or not.
	 * 
	 * @return True if they are correct, False vice versa.
	 */
	private boolean checkDivisionNumbers() {

		// get the entered texts
		String text1 = textfield2_.getText();
		String text2 = textfield3_.getText();
		String text3 = textfield4_.getText();

		// check for non-numeric values and constraints
		try {

			// convert texts to integer values
			m_ = Integer.parseInt(text1);
			n_ = Integer.parseInt(text2);
			k_ = Integer.parseInt(text3);

			// check if smaller than 1
			if (m_ < 1 || n_ < 1 || k_ < 1) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Division number should be at least 1!",
						"False data entry", 2);
				return false;
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane
					.showMessageDialog(this,
							"Illegal value for division number!",
							"False data entry", 2);
			return false;
		}

		// entered value is correct
		return true;
	}

	/**
	 * Checks whether any element of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if the given element ids are valid for meshing, False vice
	 *         versa.
	 */
	private boolean checkIds() {

		// set element ids from textfield
		if (radiobutton1_.isSelected()) {
			if (setIdsFromText() == false)
				return false;
		}

		// set element ids from groups combobox
		else if (radiobutton2_.isSelected())
			setIdsFromGroup();

		// set element ids for all existing solids
		else if (radiobutton3_.isSelected())
			setIdsForAll();

		// check for existance
		elements1_.clear();
		try {

			// check if given elements exist
			for (int i = 0; i < elementIds_.size(); i++) {

				// get element
				Element e = owner_.structure_.getElement(elementIds_.get(i));

				// check if three dimensional
				if (e.getDimension() == ElementLibrary.threeDimensional_) {

					// get three dimensional element
					Element3D e3D = (Element3D) e;

					// check if hexahedral
					if (e3D.getGeometry() == Element3D.hexahedral_) {

						// add to would be divided element list
						elements1_.add((e3D));
					}

					// not hexahedral
					else {

						// close progressor
						progressor_.close();
						setStill(false);

						// display message
						JOptionPane
								.showMessageDialog(
										this,
										"Incompatible element geometries for division!",
										"False data entry", 2);
						return false;
					}
				}

				// not three dimensional
				else {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this,
							"Incompatible element geometries for division!",
							"False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "Given elements do not exist!",
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
			elementIds_.clear();
			for (int i = 0; i < comp.length; i++) {
				elementIds_.add(Integer.parseInt(comp[i]));

				// check if negative
				if (elementIds_.get(i) < 0) {

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
			JOptionPane.showMessageDialog(this, "Given elements do not exist!",
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

		// get elements of group
		Vector<Element> elements = group.getElements();

		// get indices of group elements
		elementIds_.clear();
		for (int i = 0; i < elements.size(); i++) {

			// get element
			Element e = elements.get(i);

			// check if three dimensional
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// get three dimensional element
				Element3D e3D = (Element3D) e;

				// check if hexahedral
				if (e3D.getGeometry() == Element3D.hexahedral_) {

					// get the index of group element
					int n = owner_.structure_.indexOfElement(e3D);

					// add to values vector
					elementIds_.add(n);
				}
			}
		}
	}

	/**
	 * Sets mesh element ids for all elements.
	 * 
	 */
	private void setIdsForAll() {

		// get indices of all elements
		elementIds_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// check if three dimensional
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// get three dimensional element
				Element3D e3D = (Element3D) e;

				// check if hexahedral
				if (e3D.getGeometry() == Element3D.hexahedral_)
					elementIds_.add(i);
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
}
