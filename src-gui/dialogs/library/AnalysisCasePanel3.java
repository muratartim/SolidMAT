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

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import main.Commons;

import solver.Solver;

import analysis.Analysis;
import analysis.LinearTransient;
import boundary.BoundaryCase;

/**
 * Class for Linear Dynamic Analysis Cases panel.
 * 
 * @author Murat
 * 
 */
public class AnalysisCasePanel3 extends JPanel implements ActionListener,
		FocusListener, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	protected JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_;

	private JButton button1_, button2_, button3_, button4_, button5_;

	protected JComboBox combobox1_, combobox2_, combobox4_;

	private JList list1_, list2_;

	protected DefaultListModel listModel1_, listModel2_;

	/** Mass and stiffness proportional coefficients of analysis. */
	protected double[] proporCoeff_ = new double[2];

	/** Integration parameters of analysis. */
	protected double[] integrationPar_ = { 0.25, 0.5 };

	/** Integration method of analysis. */
	protected int method_ = LinearTransient.newmark_;

	/** Mother dialog of this panel. */
	protected AnalysisCase2 owner_;

	/**
	 * Builds panel, builds components, calls addComponent, sets layout and sets
	 * up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this panel.
	 */
	public AnalysisCasePanel3(AnalysisCase2 owner) {

		// build panel
		super();
		owner_ = owner;

		// build sub-panels
		JPanel panel1 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel2 = Commons.getPanel("Time Step Data", Commons.gridbag_);
		JPanel panel3 = Commons
				.getPanel("Solution Procedure", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Boundary Cases", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Solver", Commons.gridbag_);
		setLayout(new GridBagLayout());

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Number of time steps :");
		JLabel label3 = new JLabel("Time step size :");
		JLabel label4 = new JLabel("Damping :");
		JLabel label5 = new JLabel("Time integration :");
		JLabel label9 = new JLabel("Load time function :");
		JLabel label6 = new JLabel("Boundaries :");
		JLabel label7 = new JLabel("                      Case");
		JLabel label8 = new JLabel("      Scale");
		JLabel label10 = new JLabel("Solver :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield4_.setEditable(false);
		textfield5_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(295, 20));
		textfield2_.setPreferredSize(new Dimension(220, 20));
		textfield4_.setPreferredSize(new Dimension(150, 20));

		// build buttons and set font
		button1_ = new JButton("Modify");
		button2_ = new JButton("Modify");
		button3_ = new JButton("Add");
		button4_ = new JButton("Modify");
		button5_ = new JButton("Delete");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		combobox1_ = new JComboBox(setBoundaryCases());
		combobox2_ = new JComboBox(setSolvers());
		combobox4_ = new JComboBox(setFunctions());
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(3);
		combobox4_.setMaximumRowCount(3);
		combobox2_.setPreferredSize(new Dimension(292, 22));
		combobox4_.setPreferredSize(new Dimension(234, 22));

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		listModel2_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list2_ = new JList(listModel2_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list2_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(5);
		list2_.setVisibleRowCount(5);
		list1_.setFixedCellWidth(155);
		list2_.setFixedCellWidth(50);
		list1_.setFixedCellHeight(15);
		list2_.setFixedCellHeight(15);

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

		// add components to sub-panels
		Commons.addComponent(panel1, label1, 0, 0, 1, 1);
		Commons.addComponent(panel1, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel2, label2, 0, 0, 1, 1);
		Commons.addComponent(panel2, label3, 1, 0, 1, 1);
		Commons.addComponent(panel2, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel2, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel3, label9, 0, 0, 1, 1);
		Commons.addComponent(panel3, label4, 1, 0, 1, 1);
		Commons.addComponent(panel3, label5, 2, 0, 1, 1);
		Commons.addComponent(panel3, combobox4_, 0, 1, 2, 1);
		Commons.addComponent(panel3, textfield4_, 1, 1, 1, 1);
		Commons.addComponent(panel3, textfield5_, 2, 1, 1, 1);
		Commons.addComponent(panel3, button1_, 1, 2, 1, 1);
		Commons.addComponent(panel3, button2_, 2, 2, 1, 1);
		Commons.addComponent(panel4, label6, 1, 0, 1, 1);
		Commons.addComponent(panel4, label7, 0, 1, 1, 1);
		Commons.addComponent(panel4, label8, 0, 2, 1, 1);
		Commons.addComponent(panel4, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, textfield6_, 1, 2, 1, 1);
		Commons.addComponent(panel4, button3_, 2, 0, 1, 1);
		Commons.addComponent(panel4, button4_, 3, 0, 1, 1);
		Commons.addComponent(panel4, button5_, 4, 0, 1, 1);
		Commons.addComponent(panel4, scrollpane1, 2, 1, 1, 3);
		Commons.addComponent(panel4, scrollpane2, 2, 2, 1, 3);
		Commons.addComponent(panel5, label10, 0, 0, 1, 1);
		Commons.addComponent(panel5, combobox2_, 0, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(this, panel1, 0, 0, 1, 1);
		Commons.addComponent(this, panel2, 1, 0, 1, 1);
		Commons.addComponent(this, panel3, 2, 0, 1, 1);
		Commons.addComponent(this, panel5, 3, 0, 1, 1);
		Commons.addComponent(this, panel4, 4, 0, 1, 1);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);
		button5_.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		list1_.addListSelectionListener(this);
		list2_.addListSelectionListener(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	protected void initialize(Analysis selected) {

		// get name
		String name = selected.getName();

		// get linear dynamic analysis
		LinearTransient ld = (LinearTransient) selected;

		// get time data
		int n = ld.getNumberOfTimeSteps();
		double size = ld.getTimeStepSize();

		// get time function
		String fName = ld.getLoadTimeFunction().getName();

		// get damping parameters
		proporCoeff_ = ld.getProportionalCoefficients();

		// get integration data
		method_ = ld.getIntegrationMethod();
		if (method_ == LinearTransient.newmark_)
			integrationPar_ = ld.getNewmarkParameters();
		else if (method_ == LinearTransient.wilson_)
			integrationPar_[0] = ld.getWilsonParameter();

		// get solver
		String solver = ld.getSolver().getName();

		// get boundaries, scales
		Vector<BoundaryCase> boundaries = ld.getBoundaries();
		double[] scales = ld.getBoundaryScales();

		// set name
		textfield1_.setText(name);

		// set time data
		textfield2_.setText(Integer.toString(n));
		textfield3_.setText(owner_.owner_.owner_.formatter_.format(size));

		// set load time function
		combobox4_.setSelectedItem(fName);

		// set solution procedure
		setDefaultText(textfield4_);
		if (method_ == LinearTransient.newmark_)
			textfield5_.setText("Newmark");
		else if (method_ == LinearTransient.wilson_)
			textfield5_.setText("Wilson");

		// set solver
		combobox2_.setSelectedItem(solver);

		// set boundaries
		for (int i = 0; i < boundaries.size(); i++)
			listModel1_.addElement(boundaries.get(i).getName());

		// set scales
		for (int i = 0; i < scales.length; i++)
			listModel2_.addElement(owner_.owner_.owner_.formatter_
					.format(scales[i]));

		// set default values for others
		setDefaultText(textfield6_);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// button1_ clicked
		if (e.getSource().equals(button1_)) {

			// build child dialog and set visible
			AnalysisCase3 dialog = new AnalysisCase3(this);
			dialog.setVisible(true);
		}

		// button2_ clicked
		else if (e.getSource().equals(button2_)) {

			// build child dialog and set visible
			AnalysisCase4 dialog = new AnalysisCase4(this);
			dialog.setVisible(true);
		}

		// button3_ clicked
		else if (e.getSource().equals(button3_)) {

			// call actionAdd
			actionAdd();
		}

		// button4_ clicked
		else if (e.getSource().equals(button4_)) {

			// call actionModify
			actionModify();
		}

		// button5_ clicked
		else if (e.getSource().equals(button5_)) {

			// call actionDelete
			actionDelete();
		}
	}

	/**
	 * Adds selected boundary combobox item and its scaling factor to list1 and
	 * list2.
	 */
	private void actionAdd() {

		// get selected boundary combobox item of tab1
		String boundary = combobox1_.getSelectedItem().toString();

		// add it and its scaling factor to list1 and list2
		if (listModel1_.contains(boundary) == false) {
			listModel1_.addElement(boundary);
			listModel2_.addElement(textfield6_.getText());
		}
	}

	/**
	 * Modifies selected list1 item and its scaling factor from boundary
	 * combobox.
	 */
	private void actionModify() {

		// check if any item selected in list1
		if (list1_.isSelectionEmpty() == false) {

			// get selected boundary combobox item
			String boundary = combobox1_.getSelectedItem().toString();

			// get selected item and index of list1
			int index = list1_.getSelectedIndex();
			String selected = listModel1_.getElementAt(index).toString();

			// selected item is same with the boundary
			if (selected.equals(boundary))
				listModel2_.setElementAt(textfield6_.getText(), index);

			// selected item is different from boundary
			else {
				if (listModel1_.contains(boundary) == false) {
					listModel1_.setElementAt(boundary, index);
					listModel2_.setElementAt(textfield6_.getText(), index);
				}
			}
		}
	}

	/**
	 * Deletes selected list1 item and its scaling factor.
	 */
	private void actionDelete() {

		// check if any item selected in list1
		if (list1_.isSelectionEmpty() == false) {

			// get selected index of list1
			int index = list1_.getSelectedIndex();

			// delete selected item from list1 and list2
			listModel1_.removeElementAt(index);
			listModel2_.removeElementAt(index);
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
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source and dependently set message type
				JTextField textfield = (JTextField) e.getSource();
				if (textfield.isEditable()) {
					int messageType = 2;
					if (textfield.equals(textfield1_))
						messageType = 0;
					else if (textfield.equals(textfield2_))
						messageType = 3;
					else if (textfield.equals(textfield3_))
						messageType = 4;

					// check textfield
					if (owner_.checkText(textfield, messageType) == false) {
						setDefaultText(textfield);
					}
				}
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * Sets default values for textfields.
	 * 
	 * @param textfield
	 *            The textfield to be set. If null is given, sets all
	 *            textfields.
	 */
	protected void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultName = "ACase1";
		String defaultValue1 = Integer.toString(10);
		String defaultValue2 = owner_.owner_.owner_.formatter_.format(0.1);
		String defaultValue3 = owner_.owner_.owner_.formatter_.format(1.0);
		String defaultValue4 = "Proportional";
		String defaultValue5 = "Newmark";

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue1);

		// set to textfield3
		else if (textfield.equals(textfield3_))
			textfield3_.setText(defaultValue2);

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultValue4);

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultValue5);

		// set to textfield6
		else if (textfield.equals(textfield6_))
			textfield6_.setText(defaultValue3);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue1);
			textfield3_.setText(defaultValue2);
			textfield4_.setText(defaultValue4);
			textfield5_.setText(defaultValue5);
			textfield6_.setText(defaultValue3);
		}
	}

	/**
	 * Returns the boundary case names for boundary case combo list.
	 * 
	 * @return Boundary case names array.
	 */
	private String[] setBoundaryCases() {

		// get length of boundary cases input vector
		int length = owner_.owner_.owner_.inputData_.getBoundaryCase().size();

		// store them in an array
		String[] cases = new String[length];
		for (int i = 0; i < length; i++) {
			String name = owner_.owner_.owner_.inputData_.getBoundaryCase()
					.get(i).getName();
			cases[i] = name;
		}

		// return the array
		return cases;
	}

	/**
	 * Returns the function names for functions combo list.
	 * 
	 * @return Function names array.
	 */
	private String[] setFunctions() {

		// get length of functions input vector
		int length = owner_.owner_.owner_.inputData_.getFunction().size();

		// store them in an array
		String[] cases = new String[length];
		for (int i = 0; i < length; i++) {
			String name = owner_.owner_.owner_.inputData_.getFunction().get(i)
					.getName();
			cases[i] = name;
		}

		// return the array
		return cases;
	}

	/**
	 * Returns the solver names for solvers combo list.
	 * 
	 * @return Solver names array.
	 */
	private String[] setSolvers() {

		// initialize vector for storing solvers
		Vector<String> sol = new Vector<String>();

		// get length of solvers input vector
		int length = owner_.owner_.owner_.inputData_.getSolver().size();

		// loop over solvers
		for (int i = 0; i < length; i++) {
			Solver solver = owner_.owner_.owner_.inputData_.getSolver().get(i);
			if (solver.getProblemType() == Solver.linearSystem_)
				sol.add(owner_.owner_.owner_.inputData_.getSolver().get(i)
						.getName());
		}

		// pass vector to array
		String[] solvers = new String[sol.size()];
		for (int i = 0; i < solvers.length; i++)
			solvers[i] = sol.get(i);

		// return the array
		return solvers;
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
