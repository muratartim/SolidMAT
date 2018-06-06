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
package dialogs.library;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;

import element.ElementMass;

/**
 * Class for Add/Modify Element Masses menu.
 * 
 * @author Murat
 * 
 */
public class ElementMass2 extends JDialog implements ActionListener,
		FocusListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_;

	private JComboBox combobox1_, combobox2_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected ElementMass1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 * @param add
	 *            Used for determining if add or modify button clicked from
	 *            mother dialog.
	 */
	public ElementMass2(ElementMass1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Library and Case", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Value", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Coordinate system :");
		JLabel label3 = new JLabel("Component :");
		JLabel label4 = new JLabel("Mass :");
		label3.setPreferredSize(new Dimension(97, 23));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(80, 20));
		textfield2_.setPreferredSize(new Dimension(80, 20));

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// build list for combo boxes
		String[] types1 = { "Global", "Local" };
		String[] types2 = { "UX", "UY", "UZ", "RX", "RY", "RZ" };
		combobox1_ = new JComboBox(types1);
		combobox2_ = new JComboBox(types2);
		combobox1_.setMaximumRowCount(2);
		combobox2_.setMaximumRowCount(6);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, label3, 0, 0, 1, 1);
		Commons.addComponent(panel4, combobox2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label4, 1, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 1, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button1);
		panel2.add(button2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		combobox1_.addItemListener(this);

		// If add is clicked set default, if not initialize
		if (add_)
			setDefaultText(new JTextField());
		else
			initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get index of selected item in list of mother dialog
		int index = owner_.list1_.getSelectedIndex();

		// get the selected object
		ElementMass selected = owner_.temporary_.get(index);

		// get properties of mass
		String name = selected.getName();
		int coord = selected.getCoordinateSystem();
		int comp = selected.getComponent();
		double value = selected.getValue();

		// set name
		textfield1_.setText(name);

		// set coordinate system
		combobox1_.setSelectedIndex(coord);

		// set component
		combobox2_.setSelectedIndex(comp);

		// set value
		textfield2_.setText(owner_.owner_.formatter_.format(value));
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
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield1_, 1)) {
				actionOkAddModify();
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield1_.getText().equals(selected)) {
				actionOkAddModify();
				setVisible(false);
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield1_, 1)) {
					actionOkAddModify();
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 */
	private void actionOkAddModify() {

		// get name
		String name = textfield1_.getText();

		// get coordinate system
		int coord = combobox1_.getSelectedIndex();

		// get component
		int comp = combobox2_.getSelectedIndex();

		// get value
		double value = Double.parseDouble(textfield2_.getText());

		// create object
		ElementMass object = new ElementMass(name, comp, coord, value);

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(name);
		}

		// modify button clicked
		else if (add_ == false) {

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(name, index);
		}
	}

	/**
	 * If the related checkbox is selected, sets default value to textfield and
	 * makes it editable. If the checkbox is deselected, clears textfield and
	 * makes it uneditable.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// get selected index
			int index = combobox1_.getSelectedIndex();

			// for the "Global" option
			if (index == ElementMass.global_) {
				String[] item = { "UX", "UY", "UZ", "RX", "RY", "RZ" };
				combobox2_.removeAllItems();
				combobox2_.addItem(item[0]);
				combobox2_.addItem(item[1]);
				combobox2_.addItem(item[2]);
				combobox2_.addItem(item[3]);
				combobox2_.addItem(item[4]);
				combobox2_.addItem(item[5]);
			}

			// for the "Local" option
			else if (index == ElementMass.local_) {
				String[] item = { "U1", "U2", "U3", "R1", "R2", "R3" };
				combobox2_.removeAllItems();
				combobox2_.addItem(item[0]);
				combobox2_.addItem(item[1]);
				combobox2_.addItem(item[2]);
				combobox2_.addItem(item[3]);
				combobox2_.addItem(item[4]);
				combobox2_.addItem(item[5]);
			}
		}
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source and dependently set message type
				JTextField tfield = (JTextField) e.getSource();
				int messageType = 2;
				if (tfield.equals(textfield1_))
					messageType = 0;

				// check textfield
				if (checkText(tfield, messageType) == false) {
					setDefaultText(tfield);
				}
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield
	 *            The textfield that the false data has been entered.
	 * @param messageType
	 *            The type of message to be displayed (No name given -> 0, Name
	 *            exists -> 1, Illegal value -> 2).
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkText(JTextField textfield, int messageType) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield.getText();

		// No name given
		if (messageType == 0) {

			// check if no name given
			if (text.equals("")) {

				// display message
				JOptionPane.showMessageDialog(ElementMass2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(ElementMass2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value
		else if (messageType == 2) {

			// check for non-numeric, negative
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check if its negative
				if (value < 0) {

					// display message
					JOptionPane.showMessageDialog(ElementMass2.this,
							"Illegal value!", "False data entry", 2);
					isCorrect = false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(ElementMass2.this,
						"Illegal value!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// the data is correct
		return isCorrect;
	}

	/**
	 * Sets default values for textfields.
	 * 
	 * @param textfield
	 *            The textfield to be set. If null is given, sets all
	 *            textfields.
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultName = "Mass1";
		String defaultValue1 = owner_.owner_.formatter_.format(0.0);

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue1);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue1);
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
