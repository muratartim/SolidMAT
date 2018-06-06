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
package dialogs.assign;

import data.Group;
import element.Element;
import element.ElementLibrary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel; // import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;

import boundary.ElementMechLoad;

/**
 * Class for Assign Area Mechanical Loads Assign menu.
 * 
 * @author Murat
 * 
 */
public class AAreaMechLoad1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton6_;

	private JComboBox combobox1_;

	private JList list1_;

	private DefaultListModel listModel1_;

	/** Vector for storing element ids to be moved. */
	private Vector<Integer> elementIds_ = new Vector<Integer>();

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public AAreaMechLoad1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Assign Area Mechanical Loads", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Areas", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Options", Commons.gridbag_);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(82, 20));

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(6);
		list1_.setFixedCellWidth(150);
		list1_.setFixedCellHeight(15);

		// build scroll panes and add lists to them
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// get groups
		String[] groups = getGroups();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Area IDs :", true);
		radiobutton2_ = new JRadioButton("Groups :", false);
		radiobutton6_ = new JRadioButton("All existing areas", false);
		radiobutton3_ = new JRadioButton("Add to existing loads", false);
		radiobutton4_ = new JRadioButton("Replace existing loads", true);
		radiobutton5_ = new JRadioButton("Delete existing loads", false);

		// check if there is any group
		if (groups.length == 0)
			radiobutton2_.setEnabled(false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		ButtonGroup buttongroup2 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton6_);
		buttongroup2.add(radiobutton3_);
		buttongroup2.add(radiobutton4_);
		buttongroup2.add(radiobutton5_);

		// build comboboxes
		combobox1_ = new JComboBox(groups);
		combobox1_.setMaximumRowCount(3);
		combobox1_.setEnabled(false);

		// build buttons
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton6_, 2, 0, 2, 1);
		Commons.addComponent(panel4, scrollpane1, 0, 0, 1, 1);
		Commons.addComponent(panel5, radiobutton3_, 0, 0, 1, 1);
		Commons.addComponent(panel5, radiobutton4_, 1, 0, 1, 1);
		Commons.addComponent(panel5, radiobutton5_, 2, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		panel2.add(button1);
		panel2.add(button2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);
		radiobutton6_.addItemListener(this);

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// set the input data vector to list
		Vector<ElementMechLoad> object = owner_.inputData_.getElementMechLoad();
		for (int i = 0; i < object.size(); i++)
			if (object.get(i).getType() == ElementMechLoad.area_)
				listModel1_.addElement(object.get(i).getName());
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getActionCommand() == "  OK  ") {

			// call actionOk
			actionOk();
		}

		// cancel button clicked
		else if (e.getActionCommand() == "Cancel") {

			// set dialog unvisible
			setVisible(false);
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
		else if (event.getSource().equals(radiobutton6_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check list selection
		if (checkSelection()) {

			// get library selection
			ElementMechLoad ml = null;
			if (radiobutton3_.isSelected() || radiobutton4_.isSelected()) {

				// get library selection
				String name = list1_.getSelectedValue().toString();

				// loop over loads
				for (int i = 0; i < owner_.inputData_.getElementMechLoad()
						.size(); i++) {
					if (name.equals(owner_.inputData_.getElementMechLoad().get(
							i).getName()))
						ml = owner_.inputData_.getElementMechLoad().get(i);
				}
			}

			// check elements
			if (checkIds()) {

				// check load
				if (checkLoad(ml)) {

					// loop over selected elements
					for (int i = 0; i < elementIds_.size(); i++) {

						// get element
						Element e = owner_.structure_.getElement(elementIds_
								.get(i));

						// create vector for element loads
						Vector<ElementMechLoad> loads = new Vector<ElementMechLoad>();

						// add option selected
						if (radiobutton3_.isSelected()) {

							// get element's loads
							if (e.getAllMechLoads() != null)
								loads = e.getAllMechLoads();

							// add selected load to vector
							loads.add(ml);
						}

						// replace option selected
						else if (radiobutton4_.isSelected()) {

							// add selected load to vector
							loads.add(ml);
						}

						// set vector to element
						e.setMechLoads(loads);
					}

					// close dialog
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Checks whether given load is compatible with selected elements.
	 * 
	 * @param ml
	 *            The load to be checked.
	 * @return True if the load is compatible, False if not.
	 */
	private boolean checkLoad(ElementMechLoad ml) {

		// check if any load given
		if (ml != null) {

			// get components and coordinate system of load
			int comp = ml.getComponent();
			int coord = ml.getCoordinateSystem();

			// loop over selected elements
			for (int i = 0; i < elementIds_.size(); i++) {

				// get element and type
				Element e = owner_.structure_.getElement(elementIds_.get(i));
				int type = e.getType();

				// check components of load with element's dofs
				int[] dof = e.getDofArray(coord);
				Vector<Integer> dofv = new Vector<Integer>();
				for (int j = 0; j < dof.length; j++)
					dofv.add(j, dof[j]);
				if (dofv.contains(comp) == false) {

					// display message
					JOptionPane.showMessageDialog(AAreaMechLoad1.this,
							"Illegal loading for element type: " + type,
							"False data entry", 2);
					return false;
				}
			}
		}

		// spring is compatible with selected elements
		return true;
	}

	/**
	 * Checks whether any item in the list is selected or not (For add and
	 * replace options).
	 * 
	 * @return True if selection is correct, False if not.
	 */
	private boolean checkSelection() {

		// check if any spring selected
		if (radiobutton3_.isSelected() || radiobutton4_.isSelected()) {
			if (list1_.isSelectionEmpty()) {

				// display message
				JOptionPane.showMessageDialog(AAreaMechLoad1.this,
						"No load selected!", "False data entry", 2);
				return false;
			}
		}

		// selection is correct
		return true;
	}

	/**
	 * Checks whether any element of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if the given element ids are valid for replicating, False
	 *         vice versa.
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

		// set element ids for all existing elements
		else if (radiobutton6_.isSelected())
			setIdsForAll();

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

				// convert text to integer value
				int val = Integer.parseInt(comp[i]);

				// check if negative
				if (val < 0) {
					JOptionPane.showMessageDialog(AAreaMechLoad1.this,
							"Illegal values!", "False data entry", 2);
					return false;
				}

				// get element
				Element e = owner_.structure_.getElement(val);

				// check if two dimensional
				if (e.getDimension() == ElementLibrary.twoDimensional_)
					elementIds_.add(val);
			}
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(AAreaMechLoad1.this,
					"Given areas do not exist!", "False data entry", 2);
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

			// check if two dimensional
			if (elements.get(i).getDimension() == ElementLibrary.twoDimensional_) {

				// get the index of group element
				int n = owner_.structure_.indexOfElement(elements.get(i));

				// add to values vector
				elementIds_.add(n);
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

			// check if one dimensional
			if (owner_.structure_.getElement(i).getDimension() == ElementLibrary.twoDimensional_)
				elementIds_.add(i);
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
