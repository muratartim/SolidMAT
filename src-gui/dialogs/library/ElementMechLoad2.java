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

import javax.swing.Box;
import javax.swing.BoxLayout;
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
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;
import matrix.DVec;

import boundary.BoundaryCase;
import boundary.ElementMechLoad;

/**
 * Class for Add/Modify Element Mechanical Loads menu.
 * 
 * @author Murat
 * 
 */
public class ElementMechLoad2 extends JDialog implements ActionListener,
		ItemListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_;

	private JComboBox combobox1_, combobox2_, combobox3_, combobox4_,
			combobox5_, combobox6_, combobox7_, combobox8_, combobox9_,
			combobox10_;

	private JTabbedPane tabbedpane1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected ElementMechLoad1 owner_;

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
	public ElementMechLoad2(ElementMechLoad1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel3 = Commons.getPanel(null, Commons.flow_);
		JPanel panel12 = Commons.getPanel(null, Commons.gridbag_);

		// build sub-panels
		JPanel panel4 = Commons.getPanel("Library and Case", Commons.gridbag_);
		JPanel panel5 = Commons
				.getPanel("Type and Direction", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Distribution and Value",
				Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Library and Case", Commons.gridbag_);
		JPanel panel8 = Commons
				.getPanel("Type and Direction", Commons.gridbag_);
		JPanel panel9 = Commons.getPanel("Distribution and Value",
				Commons.gridbag_);
		JPanel panel10 = Commons.getPanel("Library and Case", Commons.gridbag_);
		JPanel panel11 = Commons.getPanel("Direction and Value",
				Commons.gridbag_);
		JPanel panel13 = new JPanel();
		panel13.setLayout(new BoxLayout(panel13, BoxLayout.Y_AXIS));

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Boundary case :");
		JLabel label3 = new JLabel("Load type :");
		JLabel label4 = new JLabel("Coordinate system :");
		JLabel label5 = new JLabel("Direction :");
		JLabel label6 = new JLabel("Load distribution :");
		JLabel label7 = new JLabel("Value(s) :");
		JLabel label8 = new JLabel("          a");
		JLabel label9 = new JLabel("          b");
		JLabel label10 = new JLabel("Name :");
		JLabel label11 = new JLabel("Boundary case :");
		JLabel label12 = new JLabel("Load type :");
		JLabel label13 = new JLabel("Coordinate system :");
		JLabel label14 = new JLabel("Direction :");
		JLabel label15 = new JLabel("Load distribution :");
		JLabel label16 = new JLabel("Value(s) :");
		JLabel label17 = new JLabel("      a");
		JLabel label18 = new JLabel("      b");
		JLabel label19 = new JLabel("      c");
		JLabel label20 = new JLabel("Name :");
		JLabel label21 = new JLabel("Boundary case :");
		JLabel label22 = new JLabel("Direction :");
		JLabel label23 = new JLabel("Value :");
		label22.setPreferredSize(new Dimension(78, 22));

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
		textfield3_.setEditable(false);
		textfield6_.setEditable(false);
		textfield7_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(161, 20));
		textfield2_.setPreferredSize(new Dimension(70, 20));
		textfield4_.setPreferredSize(new Dimension(161, 20));
		textfield5_.setPreferredSize(new Dimension(42, 20));
		textfield6_.setPreferredSize(new Dimension(42, 20));
		textfield8_.setPreferredSize(new Dimension(161, 20));
		textfield9_.setPreferredSize(new Dimension(161, 20));

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("Self weight");
		checkbox2_ = new JCheckBox("Self weight");
		checkbox3_ = new JCheckBox("Self weight");

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Forces", true);
		radiobutton2_ = new JRadioButton("Moments", false);
		radiobutton3_ = new JRadioButton("Forces", true);
		radiobutton4_ = new JRadioButton("Moments", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		ButtonGroup buttongroup2 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup2.add(radiobutton3_);
		buttongroup2.add(radiobutton4_);

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Global", "Local" };
		String types2[] = { "X", "Y", "Z" };
		String types3[] = { "Uniform", "Linear" };
		combobox1_ = new JComboBox(setBoundaryCases());
		combobox2_ = new JComboBox(types1);
		combobox3_ = new JComboBox(types2);
		combobox4_ = new JComboBox(types3);
		combobox5_ = new JComboBox(setBoundaryCases());
		combobox6_ = new JComboBox(types1);
		combobox7_ = new JComboBox(types2);
		combobox8_ = new JComboBox(types3);
		combobox9_ = new JComboBox(setBoundaryCases());
		combobox10_ = new JComboBox(types2);
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(2);
		combobox3_.setMaximumRowCount(3);
		combobox4_.setMaximumRowCount(2);
		combobox5_.setMaximumRowCount(3);
		combobox6_.setMaximumRowCount(2);
		combobox7_.setMaximumRowCount(3);
		combobox8_.setMaximumRowCount(2);
		combobox9_.setMaximumRowCount(3);
		combobox10_.setMaximumRowCount(3);
		combobox4_.setPreferredSize(new Dimension(147, 23));
		combobox8_.setPreferredSize(new Dimension(147, 23));

		// add components to sub-panels
		Commons.addComponent(panel4, label1, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 1, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, checkbox1_, 2, 0, 1, 1);
		Commons.addComponent(panel5, label3, 0, 0, 1, 1);
		Commons.addComponent(panel5, label4, 1, 0, 1, 1);
		Commons.addComponent(panel5, label5, 2, 0, 1, 1);
		Commons.addComponent(panel5, radiobutton1_, 0, 1, 1, 1);
		Commons.addComponent(panel5, radiobutton2_, 0, 2, 1, 1);
		Commons.addComponent(panel5, combobox2_, 1, 1, 2, 1);
		Commons.addComponent(panel5, combobox3_, 2, 1, 2, 1);
		Commons.addComponent(panel6, label6, 0, 0, 1, 1);
		Commons.addComponent(panel6, label7, 2, 0, 1, 1);
		Commons.addComponent(panel6, label8, 1, 1, 1, 1);
		Commons.addComponent(panel6, label9, 1, 2, 1, 1);
		Commons.addComponent(panel6, combobox4_, 0, 1, 2, 1);
		Commons.addComponent(panel6, textfield2_, 2, 1, 1, 1);
		Commons.addComponent(panel6, textfield3_, 2, 2, 1, 1);
		Commons.addComponent(panel7, label10, 0, 0, 1, 1);
		Commons.addComponent(panel7, textfield4_, 0, 1, 1, 1);
		Commons.addComponent(panel7, label11, 1, 0, 1, 1);
		Commons.addComponent(panel7, combobox5_, 1, 1, 1, 1);
		Commons.addComponent(panel7, checkbox2_, 2, 0, 1, 1);
		Commons.addComponent(panel8, label12, 0, 0, 1, 1);
		Commons.addComponent(panel8, label13, 1, 0, 1, 1);
		Commons.addComponent(panel8, label14, 2, 0, 1, 1);
		Commons.addComponent(panel8, radiobutton3_, 0, 1, 1, 1);
		Commons.addComponent(panel8, radiobutton4_, 0, 2, 1, 1);
		Commons.addComponent(panel8, combobox6_, 1, 1, 2, 1);
		Commons.addComponent(panel8, combobox7_, 2, 1, 2, 1);
		Commons.addComponent(panel9, label15, 0, 0, 1, 1);
		Commons.addComponent(panel9, label16, 2, 0, 1, 1);
		Commons.addComponent(panel9, label17, 1, 1, 1, 1);
		Commons.addComponent(panel9, label18, 1, 2, 1, 1);
		Commons.addComponent(panel9, label19, 1, 3, 1, 1);
		Commons.addComponent(panel9, combobox8_, 0, 1, 3, 1);
		Commons.addComponent(panel9, textfield5_, 2, 1, 1, 1);
		Commons.addComponent(panel9, textfield6_, 2, 2, 1, 1);
		Commons.addComponent(panel9, textfield7_, 2, 3, 1, 1);
		Commons.addComponent(panel10, label20, 0, 0, 1, 1);
		Commons.addComponent(panel10, textfield8_, 0, 1, 1, 1);
		Commons.addComponent(panel10, label21, 1, 0, 1, 1);
		Commons.addComponent(panel10, combobox9_, 1, 1, 1, 1);
		Commons.addComponent(panel10, checkbox3_, 2, 0, 1, 1);
		Commons.addComponent(panel11, label22, 0, 0, 1, 1);
		Commons.addComponent(panel11, combobox10_, 0, 1, 1, 1);
		Commons.addComponent(panel11, label23, 1, 0, 1, 1);
		Commons.addComponent(panel11, textfield9_, 1, 1, 1, 1);
		panel13.add(Box.createRigidArea(new Dimension(0, 152)));

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel4, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel6, 2, 0, 1, 1);
		Commons.addComponent(panel2, panel7, 0, 0, 1, 1);
		Commons.addComponent(panel2, panel8, 1, 0, 1, 1);
		Commons.addComponent(panel2, panel9, 2, 0, 1, 1);
		Commons.addComponent(panel12, panel10, 0, 0, 1, 1);
		Commons.addComponent(panel12, panel11, 1, 0, 1, 1);
		Commons.addComponent(panel12, panel13, 2, 0, 1, 1);
		panel3.add(button1);
		panel3.add(button2);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Lines", panel1);
		tabbedpane1_.addTab("Areas", panel2);
		tabbedpane1_.addTab("Solids", panel12);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel3);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		checkbox1_.addActionListener(this);
		checkbox2_.addActionListener(this);
		checkbox3_.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield7_.addFocusListener(this);
		textfield8_.addFocusListener(this);
		textfield9_.addFocusListener(this);
		combobox2_.addItemListener(this);
		combobox4_.addItemListener(this);
		combobox6_.addItemListener(this);
		combobox8_.addItemListener(this);

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
		ElementMechLoad selected = owner_.temporary_.get(index);

		// get properties of load
		String name = selected.getName();
		int type = selected.getType();
		String boundary = selected.getBoundaryCase().getName();
		int coord = selected.getCoordinateSystem();
		int dist = selected.getDegree();
		int comp = selected.getComponent();
		double[] values = selected.getLoadingValues().get1DArray();
		boolean isSelfWeight = selected.isSelfWeight();

		// set tab
		tabbedpane1_.setSelectedIndex(type);

		// for lines
		if (type == ElementMechLoad.line_) {

			// set name
			textfield1_.setText(name);

			// set boundary case
			combobox1_.setSelectedItem(boundary);

			// set self weight
			if (isSelfWeight)
				checkbox1_.doClick();

			// set load type
			if (comp < 3)
				radiobutton1_.setSelected(true);
			else
				radiobutton2_.setSelected(true);

			// set coordinate system
			combobox2_.setSelectedIndex(coord);

			// set direction
			combobox3_.setSelectedIndex(comp % 3);

			// set distribution
			combobox4_.setSelectedIndex(dist);

			// set values
			if (dist == 0)
				textfield2_.setText(owner_.owner_.formatter_.format(values[0]));
			else {
				textfield2_.setText(owner_.owner_.formatter_.format(values[0]));
				textfield3_.setText(owner_.owner_.formatter_.format(values[1]));
			}

			// set default for others
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
		}

		// for areas
		else if (type == ElementMechLoad.area_) {

			// set name
			textfield4_.setText(name);

			// set boundary case
			combobox5_.setSelectedItem(boundary);

			// set self weight
			if (isSelfWeight)
				checkbox2_.doClick();

			// set load type
			if (comp < 3)
				radiobutton3_.setSelected(true);
			else
				radiobutton4_.setSelected(true);

			// set coordinate system
			combobox6_.setSelectedIndex(coord);

			// set direction
			combobox7_.setSelectedIndex(comp % 3);

			// set distribution
			combobox8_.setSelectedIndex(dist);

			// set values
			if (dist == 0)
				textfield5_.setText(owner_.owner_.formatter_.format(values[0]));
			else {
				textfield5_.setText(owner_.owner_.formatter_.format(values[0]));
				textfield6_.setText(owner_.owner_.formatter_.format(values[1]));
				textfield7_.setText(owner_.owner_.formatter_.format(values[2]));
			}

			// set default for others
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
		}

		// for solids
		else if (type == ElementMechLoad.volume_) {

			// set name
			textfield8_.setText(name);

			// set boundary case
			combobox9_.setSelectedItem(boundary);

			// set self weight
			if (isSelfWeight)
				checkbox3_.doClick();

			// set direction
			combobox10_.setSelectedIndex(comp % 3);

			// set value
			textfield9_.setText(owner_.owner_.formatter_.format(values[0]));

			// set default for others
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
		}
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

		// self weight-1 selected
		else if (e.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected()) {

				// set load types
				radiobutton1_.setSelected(true);
				radiobutton1_.setEnabled(false);
				radiobutton2_.setEnabled(false);

				// set distribution
				combobox4_.setSelectedIndex(0);
				combobox4_.setEnabled(false);

				// set value
				textfield2_.setText("0.0");
				textfield2_.setEnabled(false);
			}

			// unselected
			else if (checkbox1_.isSelected() == false) {

				// set load types
				radiobutton1_.setEnabled(true);
				radiobutton2_.setEnabled(true);

				// set distribution
				combobox4_.setEnabled(true);

				// set value
				textfield2_.setEnabled(true);
			}
		}

		// self weight-2 selected
		else if (e.getSource().equals(checkbox2_)) {

			// selected
			if (checkbox2_.isSelected()) {

				// set load types
				radiobutton3_.setSelected(true);
				radiobutton3_.setEnabled(false);
				radiobutton4_.setEnabled(false);

				// set distribution
				combobox8_.setSelectedIndex(0);
				combobox8_.setEnabled(false);

				// set value
				textfield5_.setText("0.0");
				textfield5_.setEnabled(false);
			}

			// unselected
			else if (checkbox2_.isSelected() == false) {

				// set load types
				radiobutton3_.setEnabled(true);
				radiobutton4_.setEnabled(true);

				// set distribution
				combobox8_.setEnabled(true);

				// set value
				textfield5_.setEnabled(true);
			}
		}

		// self weight-3 selected
		else if (e.getSource().equals(checkbox3_)) {

			// selected
			if (checkbox3_.isSelected()) {

				// set value
				textfield9_.setText("0.0");
				textfield9_.setEnabled(false);
			}

			// unselected
			else if (checkbox3_.isSelected() == false) {

				// set value
				textfield9_.setEnabled(true);
			}
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// get type
		int type = tabbedpane1_.getSelectedIndex();

		// get name textfield to be checked
		JTextField textfield = new JTextField();
		if (type == 0)
			textfield = textfield1_;
		else if (type == 1)
			textfield = textfield4_;
		else if (type == 2)
			textfield = textfield8_;

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield, 1)) {
				actionOkAddModify(type);
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield.getText().equals(selected)) {
				actionOkAddModify(type);
				setVisible(false);
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield, 1)) {
					actionOkAddModify(type);
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 * @param type
	 *            The type of object (line -> 0, area -> 1, solid -> 2).
	 */
	private void actionOkAddModify(int type) {

		// initialize name and values
		String name = null;
		BoundaryCase bCase = null;
		int coord = 0;
		int comp = 0;
		int dist = 0;
		double[] values = new double[0];
		boolean isSelfWeight = false;

		// for lines
		if (type == ElementMechLoad.line_) {

			// get name
			name = textfield1_.getText();

			// get boundary case
			String boundary = combobox1_.getSelectedItem().toString();
			int length = owner_.owner_.inputData_.getBoundaryCase().size();
			for (int i = 0; i < length; i++) {
				bCase = owner_.owner_.inputData_.getBoundaryCase().get(i);
				String caseName = bCase.getName();
				if (boundary.equals(caseName))
					break;
			}

			// get self weight
			isSelfWeight = checkbox1_.isSelected();

			// get load type
			int loadType = 0;
			if (radiobutton2_.isSelected())
				loadType = 1;

			// get coordinate system
			coord = combobox2_.getSelectedIndex();

			// get direction
			int direction = combobox3_.getSelectedIndex();
			comp = 3 * loadType + direction;

			// get distribution
			dist = combobox4_.getSelectedIndex();

			// get values
			values = new double[2];
			values[0] = Double.parseDouble(textfield2_.getText());
			values[1] = values[0];
			if (dist == 1)
				values[1] = Double.parseDouble(textfield3_.getText());
		}

		// for areas
		else if (type == ElementMechLoad.area_) {

			// get name
			name = textfield4_.getText();

			// get boundary case
			String boundary = combobox5_.getSelectedItem().toString();
			int length = owner_.owner_.inputData_.getBoundaryCase().size();
			for (int i = 0; i < length; i++) {
				bCase = owner_.owner_.inputData_.getBoundaryCase().get(i);
				String caseName = bCase.getName();
				if (boundary.equals(caseName))
					break;
			}

			// get self weight
			isSelfWeight = checkbox2_.isSelected();

			// get load type
			int loadType = 0;
			if (radiobutton4_.isSelected())
				loadType = 1;

			// get coordinate system
			coord = combobox6_.getSelectedIndex();

			// get direction
			int direction = combobox7_.getSelectedIndex();
			comp = 3 * loadType + direction;

			// get distribution
			dist = combobox8_.getSelectedIndex();

			// get values
			values = new double[3];
			values[0] = Double.parseDouble(textfield5_.getText());
			values[1] = values[0];
			values[2] = values[0];
			if (dist == 1) {
				values[1] = Double.parseDouble(textfield6_.getText());
				values[2] = Double.parseDouble(textfield7_.getText());
			}
		}

		// for solids
		else if (type == ElementMechLoad.volume_) {

			// get name
			name = textfield8_.getText();

			// get boundary case
			String boundary = combobox9_.getSelectedItem().toString();
			int length = owner_.owner_.inputData_.getBoundaryCase().size();
			for (int i = 0; i < length; i++) {
				bCase = owner_.owner_.inputData_.getBoundaryCase().get(i);
				String caseName = bCase.getName();
				if (boundary.equals(caseName))
					break;
			}

			// get self weight
			isSelfWeight = checkbox3_.isSelected();

			// get direction
			comp = combobox10_.getSelectedIndex();

			// get value
			values = new double[1];
			values[0] = Double.parseDouble(textfield9_.getText());
		}

		// create object
		ElementMechLoad object = new ElementMechLoad(name, bCase, type, comp,
				new DVec(values));
		object.setCoordinateSystem(coord);
		object.setAsSelfWeight(isSelfWeight);

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
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox2 event
		if (event.getSource().equals(combobox2_)) {

			// get selected index
			int index = combobox2_.getSelectedIndex();

			// set items of direction combobox
			if (index == ElementMechLoad.global_) {
				String[] item = { "X", "Y", "Z" };
				combobox3_.removeAllItems();
				combobox3_.addItem(item[0]);
				combobox3_.addItem(item[1]);
				combobox3_.addItem(item[2]);
			}

			else {
				String[] item = { "1", "2", "3" };
				combobox3_.removeAllItems();
				combobox3_.addItem(item[0]);
				combobox3_.addItem(item[1]);
				combobox3_.addItem(item[2]);
			}
		}

		// combobox6 event
		else if (event.getSource().equals(combobox6_)) {

			// get selected index
			int index = combobox6_.getSelectedIndex();

			// set items of direction combobox
			if (index == 0) {
				String[] item = { "X", "Y", "Z" };
				combobox7_.removeAllItems();
				combobox7_.addItem(item[0]);
				combobox7_.addItem(item[1]);
				combobox7_.addItem(item[2]);
			}

			else {
				String[] item = { "1", "2", "3" };
				combobox7_.removeAllItems();
				combobox7_.addItem(item[0]);
				combobox7_.addItem(item[1]);
				combobox7_.addItem(item[2]);
			}
		}

		// combobox4 event
		else if (event.getSource().equals(combobox4_)) {

			// get selected index
			int index = combobox4_.getSelectedIndex();

			// set texts
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);

			// set distribution
			if (index == 0)
				textfield3_.setEditable(false);
			else
				textfield3_.setEditable(true);
		}

		// combobox8 event
		else if (event.getSource().equals(combobox8_)) {

			// get selected index
			int index = combobox8_.getSelectedIndex();

			// set texts
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);

			// set distribution
			if (index == 0) {
				textfield6_.setEditable(false);
				textfield7_.setEditable(false);
			} else {
				textfield6_.setEditable(true);
				textfield7_.setEditable(true);
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
				if (tfield.equals(textfield1_) || tfield.equals(textfield4_)
						|| tfield.equals(textfield8_))
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
				JOptionPane.showMessageDialog(ElementMechLoad2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(ElementMechLoad2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value
		else if (messageType == 2) {

			// check if it is editable
			if (textfield.isEditable()) {

				// check for non-numeric values
				try {

					// convert text to double value
					@SuppressWarnings("unused")
					double value = Double.parseDouble(text);
				} catch (Exception excep) {

					// display message
					JOptionPane.showMessageDialog(ElementMechLoad2.this,
							"Illegal value!", "False data entry", 2);
					isCorrect = false;
				}
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
		String defaultName = "Load1";
		String defaultValue1 = owner_.owner_.formatter_.format(0.0);
		String defaultValue2 = "";

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultName);

		// set to textfield8
		else if (textfield.equals(textfield8_))
			textfield8_.setText(defaultName);

		// set to textfield9
		else if (textfield.equals(textfield9_))
			textfield9_.setText(defaultValue1);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue1);

		// set to textfield3
		else if (textfield.equals(textfield3_)) {
			int index = combobox4_.getSelectedIndex();
			if (index == 0)
				textfield3_.setText(defaultValue2);
			else
				textfield3_.setText(defaultValue1);
		}

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultValue1);

		// set to textfield6
		else if (textfield.equals(textfield6_)) {
			int index = combobox8_.getSelectedIndex();
			if (index == 0)
				textfield6_.setText(defaultValue2);
			else
				textfield6_.setText(defaultValue1);
		}

		// set to textfield7
		else if (textfield.equals(textfield7_)) {
			int index = combobox8_.getSelectedIndex();
			if (index == 0)
				textfield7_.setText(defaultValue2);
			else
				textfield7_.setText(defaultValue1);
		}

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield4_.setText(defaultName);
			textfield8_.setText(defaultName);
			textfield2_.setText(defaultValue1);
			textfield9_.setText(defaultValue1);
			textfield3_.setText(defaultValue2);
			textfield5_.setText(defaultValue1);
			textfield6_.setText(defaultValue2);
			textfield7_.setText(defaultValue2);
		}
	}

	/**
	 * Returns the boundary case names for boundary case combo list.
	 * 
	 * @return Boundary case names array.
	 */
	private String[] setBoundaryCases() {

		// get length of boundary cases input vector
		int length = owner_.owner_.inputData_.getBoundaryCase().size();

		// store them in an array
		String[] cases = new String[length];
		for (int i = 0; i < length; i++) {
			String name = owner_.owner_.inputData_.getBoundaryCase().get(i)
					.getName();
			cases[i] = name;
		}

		// return the array
		return cases;
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
