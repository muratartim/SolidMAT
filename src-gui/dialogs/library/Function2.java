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


import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultListModel;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import dialogs.file.FFilter2;

import analysis.Analysis;
import analysis.LinearTransient;

import main.Commons;
// import main.ImageHandler;
import math.Function;

/**
 * Class for Add/Modify Functions menu.
 * 
 * @author Murat
 * 
 */
public class Function2 extends JDialog implements ActionListener, ItemListener,
		FocusListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_;

	private JLabel label4_, label5_, label6_, label7_;

	private JButton button1_, button2_, button3_, button4_;

	private JComboBox combobox1_;

	private JList list1_, list2_;

	private DefaultListModel listModel1_, listModel2_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected Function1 owner_;

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
	public Function2(Function1 owner, boolean add) {

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
		JPanel panel3 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Type", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Parameters", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Values", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Type :");
		JLabel label3 = new JLabel("Function :");
		label4_ = new JLabel("A :");
		label5_ = new JLabel("B :");
		label6_ = new JLabel("C :");
		label7_ = new JLabel("D :");
		JLabel label8 = new JLabel("         x");
		JLabel label9 = new JLabel("       F(x)");
		label6_.setVisible(false);
		label7_.setVisible(false);
		label1.setPreferredSize(new Dimension(56, 23));
		label3.setPreferredSize(new Dimension(56, 23));
		label4_.setPreferredSize(new Dimension(56, 23));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield8_ = new JTextField();
		textfield2_.setEditable(false);
		textfield2_.setText("F(x)=Ax+B");
		textfield5_.setEnabled(false);
		textfield6_.setEnabled(false);
		textfield7_.setEnabled(false);
		textfield8_.setEnabled(false);
		textfield1_.setPreferredSize(new Dimension(163, 20));
		textfield2_.setPreferredSize(new Dimension(163, 20));
		textfield3_.setPreferredSize(new Dimension(163, 20));

		// build buttons and set font
		button1_ = new JButton("Add");
		button2_ = new JButton("Modify");
		button3_ = new JButton("Delete");
		button4_ = new JButton("Import");
		JButton button5 = new JButton("  OK  ");
		JButton button6 = new JButton("Cancel");
		button1_.setEnabled(false);
		button2_.setEnabled(false);
		button3_.setEnabled(false);
		button4_.setEnabled(false);

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Linear", "Quadratic", "Cubic", "Sine", "Cosine",
				"Tangent", "Exp", "Log", "Log10", "User defined" };
		combobox1_ = new JComboBox(types1);
		combobox1_.setMaximumRowCount(6);

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		listModel2_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list2_ = new JList(listModel2_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list2_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(6);
		list2_.setVisibleRowCount(6);
		list1_.setFixedCellWidth(50);
		list2_.setFixedCellWidth(50);
		list1_.setFixedCellHeight(14);
		list2_.setFixedCellHeight(14);

		// build scroll panes and add lists to them
		JScrollPane scrollpane1 = new JScrollPane(list1_);
		JScrollPane scrollpane2 = new JScrollPane(list2_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);
		scrollpane2.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane2.setHorizontalScrollBarPolicy(horizontalConstant);
		scrollpane1.setEnabled(false);
		scrollpane2.setEnabled(false);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, label4_, 0, 0, 1, 1);
		Commons.addComponent(panel5, label5_, 1, 0, 1, 1);
		Commons.addComponent(panel5, label6_, 2, 0, 1, 1);
		Commons.addComponent(panel5, label7_, 3, 0, 1, 1);
		Commons.addComponent(panel5, textfield3_, 0, 1, 1, 1);
		Commons.addComponent(panel5, textfield4_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield5_, 2, 1, 1, 1);
		Commons.addComponent(panel5, textfield6_, 3, 1, 1, 1);
		Commons.addComponent(panel6, label8, 0, 1, 1, 1);
		Commons.addComponent(panel6, label9, 0, 2, 1, 1);
		Commons.addComponent(panel6, button4_, 1, 0, 1, 1);
		Commons.addComponent(panel6, button1_, 2, 0, 1, 1);
		Commons.addComponent(panel6, button2_, 3, 0, 1, 1);
		Commons.addComponent(panel6, button3_, 4, 0, 1, 1);
		Commons.addComponent(panel6, textfield7_, 1, 1, 1, 1);
		Commons.addComponent(panel6, textfield8_, 1, 2, 1, 1);
		Commons.addComponent(panel6, scrollpane1, 2, 1, 1, 3);
		Commons.addComponent(panel6, scrollpane2, 2, 2, 1, 3);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		Commons.addComponent(panel1, panel6, 3, 0, 1, 1);
		panel2.add(button5);
		panel2.add(button6);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield7_.addFocusListener(this);
		textfield8_.addFocusListener(this);
		combobox1_.addItemListener(this);
		list1_.addListSelectionListener(this);
		list2_.addListSelectionListener(this);

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
		Function selected = owner_.temporary_.get(index);

		// get name, type
		String name = selected.getName();
		int type = selected.getType();

		// set name
		textfield1_.setText(name);

		// set type
		combobox1_.setSelectedIndex(type);

		// user defined function
		if (type == Function.userDefined_) {

			// get x and y values
			double[] xVal = selected.getXValues();
			double[] yVal = selected.getYValues();

			// set values
			for (int i = 0; i < xVal.length; i++) {
				listModel1_
						.addElement(owner_.owner_.formatter_.format(xVal[i]));
				listModel2_
						.addElement(owner_.owner_.formatter_.format(yVal[i]));
			}
		}

		// built-in function
		else {

			// get parameters
			double[] param = selected.getParameters();

			// linear-sine-cosine-tangent
			if (type == Function.linear_ || type == Function.sine_
					|| type == Function.cosine_ || type == Function.tangent_) {
				textfield3_.setText(owner_.owner_.formatter_.format(param[0]));
				textfield4_.setText(owner_.owner_.formatter_.format(param[1]));
			}

			// quadratic
			else if (type == Function.quadratic_) {
				textfield3_.setText(owner_.owner_.formatter_.format(param[0]));
				textfield4_.setText(owner_.owner_.formatter_.format(param[1]));
				textfield5_.setText(owner_.owner_.formatter_.format(param[2]));
			}

			// cubic
			else if (type == Function.cubic_) {
				textfield3_.setText(owner_.owner_.formatter_.format(param[0]));
				textfield4_.setText(owner_.owner_.formatter_.format(param[1]));
				textfield5_.setText(owner_.owner_.formatter_.format(param[2]));
				textfield6_.setText(owner_.owner_.formatter_.format(param[3]));
			}
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

		// button1_ clicked
		else if (e.getSource().equals(button1_)) {

			// call actionAdd
			actionAdd();
		}

		// button2_ clicked
		else if (e.getSource().equals(button2_)) {

			// call actionModify
			actionModify();
		}

		// button3_ clicked
		else if (e.getSource().equals(button3_)) {

			// call actionDelete
			actionDelete();
		}

		// button4_ clicked
		else if (e.getSource().equals(button4_)) {

			// call actionImport
			actionImport();
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

				// check for incompatibility with any assigned library
				if (checkLibrary()) {
					actionOkAddModify();
					setVisible(false);
				}
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield1_, 1)) {

					// check for incompatibility with any assigned library
					if (checkLibrary()) {
						actionOkAddModify();
						setVisible(false);
					}
				}
			}
		}
	}

	/**
	 * Checks whether there is any incompatibility with the assigned libraries.
	 * 
	 * @return True if modified function is compatible, False vice versa.
	 */
	private boolean checkLibrary() {

		// get name
		String name = textfield1_.getText();

		// get type
		int type = combobox1_.getSelectedIndex();

		// get parameters
		double[] param = new double[4];

		// linear-sine-cosine-tangent
		if (type == Function.linear_ || type == Function.sine_
				|| type == Function.cosine_ || type == Function.tangent_) {
			param[0] = Double.parseDouble(textfield3_.getText());
			param[1] = Double.parseDouble(textfield4_.getText());
		}

		// quadratic
		else if (type == Function.quadratic_) {
			param[0] = Double.parseDouble(textfield3_.getText());
			param[1] = Double.parseDouble(textfield4_.getText());
			param[2] = Double.parseDouble(textfield5_.getText());
		}

		// cubic
		else if (type == Function.cubic_) {
			param[0] = Double.parseDouble(textfield3_.getText());
			param[1] = Double.parseDouble(textfield4_.getText());
			param[2] = Double.parseDouble(textfield5_.getText());
			param[3] = Double.parseDouble(textfield6_.getText());
		}

		// get values
		double[] xValues = new double[listModel1_.size()];
		double[] yValues = new double[listModel2_.size()];
		for (int i = 0; i < xValues.length; i++) {
			xValues[i] = Double.parseDouble(listModel1_.get(i).toString());
			yValues[i] = Double.parseDouble(listModel2_.get(i).toString());
		}

		// create object
		Function object = new Function(name, type);
		object.setParameters(param);
		object.setValues(xValues, yValues);

		// get name of selected function
		String item = owner_.list1_.getSelectedValue().toString();

		// check for analysis cases
		for (int i = 0; i < owner_.analysis_.size(); i++) {

			// get type of analysis case
			int typ = owner_.analysis_.get(i).getType();

			// for dynamic analysis
			if (typ == Analysis.linearTransient_) {

				// get linear dynamic analysis
				LinearTransient ls = (LinearTransient) owner_.analysis_.get(i);

				// get load time function
				String func = ls.getLoadTimeFunction().getName();

				// check if name is same
				if (item.equals(func)) {

					// get time data
					int n = ls.getNumberOfTimeSteps();
					double dt = ls.getTimeStepSize();

					// create dummy analysis,
					try {
						LinearTransient ld = new LinearTransient("dummy");
						ld.setTimeParameters(n, dt);
						ld.setLoadTimeFunction(object);
					}

					// incompatible function for analysis
					catch (Exception e) {

						// display message
						JOptionPane.showMessageDialog(this,
								"Incompatibility encountered with the assigned libraries!"
										+ "\nCannot modify function.",
								"False data entry", 2);
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 */
	private void actionOkAddModify() {

		// get name
		String name = textfield1_.getText();

		// get type
		int type = combobox1_.getSelectedIndex();

		// get parameters
		double[] param = new double[4];

		// linear-sine-cosine-tangent
		if (type == Function.linear_ || type == Function.sine_
				|| type == Function.cosine_ || type == Function.tangent_) {
			param[0] = Double.parseDouble(textfield3_.getText());
			param[1] = Double.parseDouble(textfield4_.getText());
		}

		// quadratic
		else if (type == Function.quadratic_) {
			param[0] = Double.parseDouble(textfield3_.getText());
			param[1] = Double.parseDouble(textfield4_.getText());
			param[2] = Double.parseDouble(textfield5_.getText());
		}

		// cubic
		else if (type == Function.cubic_) {
			param[0] = Double.parseDouble(textfield3_.getText());
			param[1] = Double.parseDouble(textfield4_.getText());
			param[2] = Double.parseDouble(textfield5_.getText());
			param[3] = Double.parseDouble(textfield6_.getText());
		}

		// get values
		double[] xValues = new double[listModel1_.size()];
		double[] yValues = new double[listModel2_.size()];
		for (int i = 0; i < xValues.length; i++) {
			xValues[i] = Double.parseDouble(listModel1_.get(i).toString());
			yValues[i] = Double.parseDouble(listModel2_.get(i).toString());
		}

		// create object
		Function object = new Function(name, type);
		object.setParameters(param);
		object.setValues(xValues, yValues);

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(name);
		}

		// modify button clicked
		else if (add_ == false) {

			// get name of selected function
			String item = owner_.list1_.getSelectedValue().toString();

			// check for analysis cases
			for (int i = 0; i < owner_.analysis_.size(); i++) {

				// get type of analysis case
				int typ = owner_.analysis_.get(i).getType();

				// for dynamic analysis
				if (typ == Analysis.linearTransient_) {

					// get linear dynamic analysis
					LinearTransient ls = (LinearTransient) owner_.analysis_
							.get(i);

					// get load time function
					String func = ls.getLoadTimeFunction().getName();

					// check if name is same
					if (item.equals(func))
						ls.setLoadTimeFunction(object);
				}
			}

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(name, index);
		}
	}

	/**
	 * Adds selected boundary combobox item and its scaling factor to list1 and
	 * list2.
	 */
	private void actionAdd() {

		// check if textfield enabled
		if (textfield7_.isEnabled()) {

			// get entered value from textfield
			String value = textfield7_.getText();

			// add it and its function value to list1 and list2
			if (listModel1_.contains(value) == false) {
				listModel1_.addElement(value);
				listModel2_.addElement(textfield8_.getText());
			}

			// sort lists
			sort();
		}
	}

	/**
	 * Modifies selected list1 item and its scaling factor from boundary
	 * combobox.
	 */
	private void actionModify() {

		// check if textfield enabled
		if (textfield7_.isEnabled()) {

			// check if any item selected in list1
			if (list1_.isSelectionEmpty() == false) {

				// get entered value from textfield
				String value = textfield7_.getText();

				// get selected item and index of list1
				int index = list1_.getSelectedIndex();
				String selected = listModel1_.getElementAt(index).toString();

				// selected item is same with the value
				if (selected.equals(value))
					listModel2_.setElementAt(textfield8_.getText(), index);

				// selected item is different from value
				else {
					if (listModel1_.contains(value) == false) {
						listModel1_.setElementAt(value, index);
						listModel2_.setElementAt(textfield8_.getText(), index);
					}
				}

				// sort lists
				sort();
			}
		}
	}

	/**
	 * Deletes selected list1 item and its scaling factor.
	 */
	private void actionDelete() {

		// check if textfield enabled
		if (textfield7_.isEnabled()) {

			// check if any item selected in list1
			if (list1_.isSelectionEmpty() == false) {

				// get selected index of list1
				int index = list1_.getSelectedIndex();

				// delete selected item from list1 and list2
				listModel1_.removeElementAt(index);
				listModel2_.removeElementAt(index);
			}
		}
	}

	/**
	 * Performs task for importing function from text file.
	 * 
	 */
	private void actionImport() {

		// create file chooser
		JFileChooser chooser = new JFileChooser();

		// add custom file filter
		chooser.addChoosableFileFilter(new FFilter2());

		// disable the default (Accept All) file filter.
		chooser.setAcceptAllFileFilterUsed(false);

		// show file chooser
		int val = chooser.showDialog(this, "Import");

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

			// read file
			readFile(path);
		}
	}

	/**
	 * Reads text file and imports values to user-defined lists.
	 * 
	 * @param path
	 *            The path of text file to be read.
	 */
	private void readFile(String path) {

		// initialize reader
		BufferedReader breader = null;

		try {

			// create input file
			File file = new File(path);

			// create file reader
			FileReader reader = new FileReader(file);

			// create buffered reader
			breader = new BufferedReader(reader);

			// read header and space
			String line = breader.readLine();
			line = breader.readLine();

			// read lines
			line = breader.readLine();
			while (line != null) {

				// eliminate spaces
				String elText = "";
				for (int i = 0; i < line.length(); i++) {
					char c = line.charAt(i);
					if (c != " ".charAt(0))
						elText += c;
				}

				// seperate components
				String[] comp = elText.split(",", 2);

				// convert text to double value and add line to lists
				double value1, value2;

				// convert text to double value
				value1 = Double.parseDouble(comp[0]);
				value2 = Double.parseDouble(comp[1]);

				// add line to lists
				if (listModel1_.contains(owner_.owner_.formatter_
						.format(value1)) == false) {
					listModel1_.addElement(owner_.owner_.formatter_
							.format(value1));
					listModel2_.addElement(owner_.owner_.formatter_
							.format(value2));
				}

				// read line
				line = breader.readLine();
			}

			// sort lists
			sort();
		}

		// exception occured during creation of file
		catch (Exception excep) {

			// clear lists
			listModel1_.clear();
			listModel2_.clear();

			// display message
			JOptionPane.showMessageDialog(this,
					"Exception occurred during reading file!", "Exception", 2);
		}

		// close reader
		finally {

			// check if the reader is null
			if (breader != null) {
				try {

					// close
					breader.close();
				} catch (IOException io) {
				}
			}
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

			// set textfields
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
			setDefaultText(textfield8_);

			// get selected index
			int index = combobox1_.getSelectedIndex();

			// linear
			if (index == Function.linear_) {

				// set textfields enabled
				textfield3_.setEnabled(true);
				textfield4_.setEnabled(true);
				textfield5_.setEnabled(false);
				textfield6_.setEnabled(false);
				textfield7_.setEnabled(false);
				textfield8_.setEnabled(false);

				// set labels
				label4_.setText("A :");
				label5_.setText("B :");

				// set labels visible
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(false);
				label7_.setVisible(false);

				// clear lists
				listModel1_.clear();
				listModel2_.clear();

				// disable user-buttons
				button1_.setEnabled(false);
				button2_.setEnabled(false);
				button3_.setEnabled(false);
				button4_.setEnabled(false);
			}

			// quadratic
			else if (index == Function.quadratic_) {

				// set textfields enabled
				textfield3_.setEnabled(true);
				textfield4_.setEnabled(true);
				textfield5_.setEnabled(true);
				textfield6_.setEnabled(false);
				textfield7_.setEnabled(false);
				textfield8_.setEnabled(false);

				// set labels
				label4_.setText("A :");
				label5_.setText("B :");
				label6_.setText("C :");

				// set labels visible
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(false);

				// clear lists
				listModel1_.clear();
				listModel2_.clear();

				// disable user-buttons
				button1_.setEnabled(false);
				button2_.setEnabled(false);
				button3_.setEnabled(false);
				button4_.setEnabled(false);
			}

			// cubic
			else if (index == Function.cubic_) {

				// set textfields enabled
				textfield3_.setEnabled(true);
				textfield4_.setEnabled(true);
				textfield5_.setEnabled(true);
				textfield6_.setEnabled(true);
				textfield7_.setEnabled(false);
				textfield8_.setEnabled(false);

				// set labels
				label4_.setText("A :");
				label5_.setText("B :");
				label6_.setText("C :");
				label7_.setText("D :");

				// set labels visible
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(true);
				label7_.setVisible(true);

				// clear lists
				listModel1_.clear();
				listModel2_.clear();

				// disable user-buttons
				button1_.setEnabled(false);
				button2_.setEnabled(false);
				button3_.setEnabled(false);
				button4_.setEnabled(false);
			}

			// sine-cosine-tangent
			else if (index == Function.sine_ || index == Function.cosine_
					|| index == Function.tangent_) {

				// set textfields enabled
				textfield3_.setEnabled(true);
				textfield4_.setEnabled(true);
				textfield5_.setEnabled(false);
				textfield6_.setEnabled(false);
				textfield7_.setEnabled(false);
				textfield8_.setEnabled(false);

				// set labels
				label4_.setText("Period :");
				label5_.setText("Amplitude :");

				// set labels visible
				label4_.setVisible(true);
				label5_.setVisible(true);
				label6_.setVisible(false);
				label7_.setVisible(false);

				// clear lists
				listModel1_.clear();
				listModel2_.clear();

				// disable user-buttons
				button1_.setEnabled(false);
				button2_.setEnabled(false);
				button3_.setEnabled(false);
				button4_.setEnabled(false);
			}

			// exp-log-log10
			else if (index == Function.exp_ || index == Function.log_
					|| index == Function.log10_) {

				// set textfields enabled
				textfield3_.setEnabled(false);
				textfield4_.setEnabled(false);
				textfield5_.setEnabled(false);
				textfield6_.setEnabled(false);
				textfield7_.setEnabled(false);
				textfield8_.setEnabled(false);

				// set labels
				label4_.setText("");

				// set labels visible
				label4_.setVisible(true);
				label5_.setVisible(false);
				label6_.setVisible(false);
				label7_.setVisible(false);

				// clear lists
				listModel1_.clear();
				listModel2_.clear();

				// disable user-buttons
				button1_.setEnabled(false);
				button2_.setEnabled(false);
				button3_.setEnabled(false);
				button4_.setEnabled(false);
			}

			// user defined
			else if (index == Function.userDefined_) {

				// set textfields enabled
				textfield3_.setEnabled(false);
				textfield4_.setEnabled(false);
				textfield5_.setEnabled(false);
				textfield6_.setEnabled(false);
				textfield7_.setEnabled(true);
				textfield8_.setEnabled(true);

				// set labels
				label4_.setText("");

				// set labels visible
				label4_.setVisible(true);
				label5_.setVisible(false);
				label6_.setVisible(false);
				label7_.setVisible(false);

				// disable user-buttons
				button1_.setEnabled(true);
				button2_.setEnabled(true);
				button3_.setEnabled(true);
				button4_.setEnabled(true);
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
				int messageType = 0;
				if (tfield.equals(textfield5_) || tfield.equals(textfield6_)
						|| tfield.equals(textfield7_)
						|| tfield.equals(textfield8_))
					messageType = 2;
				else if (tfield.equals(textfield3_)
						|| tfield.equals(textfield4_)) {
					int index = combobox1_.getSelectedIndex();
					if (index == Function.sine_ || index == Function.cosine_
							|| index == Function.tangent_)
						messageType = 3;
					else
						messageType = 2;
				}

				// check textfield
				if (checkText(tfield, messageType) == false) {
					setDefaultText(tfield);
				}
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * Makes dependent selections between lists.
	 */
	public void valueChanged(ListSelectionEvent event) {

		// list1 item selected
		if (event.getSource().equals(list1_)) {

			// get selected index
			int index = list1_.getSelectedIndex();

			// select same indexed item of scaling list
			list2_.setSelectedIndex(index);
		}

		// list2 item selected
		else if (event.getSource().equals(list2_)) {

			// get selected index
			int index = list2_.getSelectedIndex();

			// select same indexed item of name list
			list1_.setSelectedIndex(index);
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield
	 *            The textfield that the false data has been entered.
	 * @param messageType
	 *            The type of message to be displayed (No name given -> 0, Name
	 *            exists -> 1, Illegal value (non-numeric) -> 2, Illegal value
	 *            (non-numeric, <= 0) -> 3).
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
				JOptionPane.showMessageDialog(this, "No name given!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(this, "Name already exists!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value (non-numeric)
		else if (messageType == 2) {

			// check for non-numeric values
			try {

				// convert text to double value
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text);
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(this, "Illegal value!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value (non-numeric, <= 0)
		else if (messageType == 3) {

			// check for non-numeric values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check for <= 0
				if (value <= 0) {

					// display message
					JOptionPane.showMessageDialog(this, "Illegal value!",
							"False data entry", 2);
					isCorrect = false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(this, "Illegal value!",
						"False data entry", 2);
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

		// get selected index of combo
		int index = combobox1_.getSelectedIndex();

		// The default values for textfields
		String defaultName = "Func1";
		String defaultValue1 = "F(x)=Ax+B";
		String defaultValue2 = "F(x)=Ax^2+Bx+C";
		String defaultValue3 = "F(x)=Ax^3+Bx^2+Cx+D";
		String defaultValue4 = "F(x)=sin(x)";
		String defaultValue5 = "F(x)=cos(x)";
		String defaultValue6 = "F(x)=tan(x)";
		String defaultValue7 = "F(x)=e^x";
		String defaultValue8 = "F(x)=log(x)";
		String defaultValue9 = "F(x)=log10(x)";
		String defaultValue10 = "";
		String defaultValue11 = owner_.owner_.formatter_.format(0.0);
		String defaultValue12 = owner_.owner_.formatter_.format(1.0);

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_)) {
			if (index == Function.linear_)
				textfield2_.setText(defaultValue1);
			else if (index == Function.quadratic_)
				textfield2_.setText(defaultValue2);
			else if (index == Function.cubic_)
				textfield2_.setText(defaultValue3);
			else if (index == Function.sine_)
				textfield2_.setText(defaultValue4);
			else if (index == Function.cosine_)
				textfield2_.setText(defaultValue5);
			else if (index == Function.tangent_)
				textfield2_.setText(defaultValue6);
			else if (index == Function.exp_)
				textfield2_.setText(defaultValue7);
			else if (index == Function.log_)
				textfield2_.setText(defaultValue8);
			else if (index == Function.log10_)
				textfield2_.setText(defaultValue9);
			else if (index == Function.userDefined_)
				textfield2_.setText(defaultValue10);
		}

		// set to textfield3
		else if (textfield.equals(textfield3_)) {
			if (index == Function.linear_ || index == Function.quadratic_
					|| index == Function.cubic_)
				textfield3_.setText(defaultValue11);
			else if (index == Function.sine_ || index == Function.cosine_
					|| index == Function.tangent_)
				textfield3_.setText(defaultValue12);
			else
				textfield3_.setText(defaultValue10);
		}

		// set to textfield4
		else if (textfield.equals(textfield4_)) {
			if (index == Function.linear_ || index == Function.quadratic_
					|| index == Function.cubic_)
				textfield4_.setText(defaultValue11);
			else if (index == Function.sine_ || index == Function.cosine_
					|| index == Function.tangent_)
				textfield4_.setText(defaultValue12);
			else
				textfield4_.setText(defaultValue10);
		}

		// set to textfield5
		else if (textfield.equals(textfield5_)) {
			if (index == Function.quadratic_ || index == Function.cubic_)
				textfield5_.setText(defaultValue11);
			else
				textfield5_.setText(defaultValue10);
		}

		// set to textfield6
		else if (textfield.equals(textfield6_)) {
			if (index == Function.cubic_)
				textfield6_.setText(defaultValue11);
			else
				textfield6_.setText(defaultValue10);
		}

		// set to textfield7
		else if (textfield.equals(textfield7_)) {
			if (index == Function.userDefined_)
				textfield7_.setText(defaultValue11);
			else
				textfield7_.setText(defaultValue10);
		}

		// set to textfield8
		else if (textfield.equals(textfield8_)) {
			if (index == Function.userDefined_)
				textfield8_.setText(defaultValue11);
			else
				textfield8_.setText(defaultValue10);
		}

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue1);
			textfield3_.setText(defaultValue11);
			textfield4_.setText(defaultValue11);
			textfield5_.setText(defaultValue10);
			textfield6_.setText(defaultValue10);
			textfield7_.setText(defaultValue10);
			textfield8_.setText(defaultValue10);
		}
	}

	/**
	 * Sorts list1 in increasing order. List2 is ordered dependently to list1.
	 * 
	 */
	private void sort() {

		// create arrays for storing elements of lists
		double[] val1 = new double[listModel1_.size()];
		double[] val2 = new double[listModel2_.size()];

		// fill arrays from lists
		for (int i = 0; i < val1.length; i++) {
			val1[i] = Double.parseDouble(listModel1_.get(i).toString());
			val2[i] = Double.parseDouble(listModel2_.get(i).toString());
		}

		// loop over array for pivot
		for (int i = 0; i < val1.length - 1; i++) {

			// loop over remaining elements
			for (int j = i + 1; j < val1.length; j++) {

				// compare pivot
				if (val1[i] > val1[j]) {

					// exchange positions of array-1
					double temp1 = val1[i];
					val1[i] = val1[j];
					val1[j] = temp1;

					// exchange positions of array-2
					double temp2 = val2[i];
					val2[i] = val2[j];
					val2[j] = temp2;
				}
			}
		}

		// clear lists
		listModel1_.clear();
		listModel2_.clear();

		// fill lists from arrays
		for (int i = 0; i < val1.length; i++) {
			listModel1_.add(i, owner_.owner_.formatter_.format(val1[i]));
			listModel2_.add(i, owner_.owner_.formatter_.format(val2[i]));
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
