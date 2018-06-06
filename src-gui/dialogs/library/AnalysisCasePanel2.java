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

import javax.swing.Box;
import javax.swing.BoxLayout;
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

import solver.Solver;

import analysis.Analysis;
import analysis.Modal;
import boundary.BoundaryCase;
import main.Commons;

/**
 * Class for Modal Analysis Cases panel.
 * 
 * @author Murat
 * 
 */
public class AnalysisCasePanel2 extends JPanel implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	protected JTextField textfield1_;

	private JButton button1_, button2_, button3_;

	protected JComboBox combobox1_, combobox2_;

	protected JList list1_;

	protected DefaultListModel listModel1_;

	/** Mother dialog of this panel. */
	private AnalysisCase2 owner_;

	/**
	 * Builds panel, builds components, calls addComponent, sets layout and sets
	 * up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this panel.
	 */
	public AnalysisCasePanel2(AnalysisCase2 owner) {

		// build panel
		super();
		owner_ = owner;

		// build sub-panels
		JPanel panel1 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel2 = Commons.getPanel("Solver", Commons.gridbag_);
		JPanel panel3 = Commons.getPanel("Boundary Cases", Commons.gridbag_);
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BoxLayout(panel4, BoxLayout.Y_AXIS));
		setLayout(new GridBagLayout());

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Solver :");
		JLabel label3 = new JLabel("Boundaries :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(295, 20));

		// build buttons and set font
		button1_ = new JButton("Add");
		button2_ = new JButton("Modify");
		button3_ = new JButton("Delete");

		// build list for combo boxes
		combobox2_ = new JComboBox(setBoundaryCases());
		combobox1_ = new JComboBox(setSolvers());
		combobox2_.setMaximumRowCount(3);
		combobox1_.setMaximumRowCount(3);
		combobox1_.setPreferredSize(new Dimension(292, 22));

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(5);
		list1_.setFixedCellWidth(245);
		list1_.setFixedCellHeight(15);

		// build scroll panes and add lists to them
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// add components to sub-panels
		Commons.addComponent(panel1, label1, 0, 0, 1, 1);
		Commons.addComponent(panel1, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel2, label2, 0, 0, 1, 1);
		Commons.addComponent(panel2, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, label3, 0, 0, 1, 1);
		Commons.addComponent(panel3, combobox2_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button1_, 1, 0, 1, 1);
		Commons.addComponent(panel3, button2_, 2, 0, 1, 1);
		Commons.addComponent(panel3, button3_, 3, 0, 1, 1);
		Commons.addComponent(panel3, scrollpane1, 1, 1, 1, 3);
		panel4.add(Box.createRigidArea(new Dimension(0, 252)));

		// add sub-panels to main panels
		Commons.addComponent(this, panel1, 0, 0, 1, 1);
		Commons.addComponent(this, panel2, 1, 0, 1, 1);
		Commons.addComponent(this, panel3, 2, 0, 1, 1);
		Commons.addComponent(this, panel4, 3, 0, 1, 1);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		textfield1_.addFocusListener(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	protected void initialize(Analysis selected) {

		// get name
		String name = selected.getName();

		// get modal analysis
		Modal m = (Modal) selected;

		// get solver
		String solver = m.getSolver().getName();

		// boundaries
		Vector<BoundaryCase> boundaries = m.getBoundaries();

		// set name
		textfield1_.setText(name);

		// set solver
		combobox1_.setSelectedItem(solver);

		// set boundaries
		for (int i = 0; i < boundaries.size(); i++)
			listModel1_.addElement(boundaries.get(i).getName());
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// button1_ clicked
		if (e.getSource().equals(button1_)) {

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
	}

	/**
	 * Adds selected boundary combobox item and its scaling factor to list1 and
	 * list2.
	 */
	private void actionAdd() {

		// get selected boundary combobox item of tab1
		String boundary = combobox2_.getSelectedItem().toString();

		// add it and its scaling factor to list1 and list2
		if (listModel1_.contains(boundary) == false)
			listModel1_.addElement(boundary);
	}

	/**
	 * Modifies selected list1 item and its scaling factor from boundary
	 * combobox.
	 */
	private void actionModify() {

		// check if any item selected in list1
		if (list1_.isSelectionEmpty() == false) {

			// get selected boundary combobox item
			String boundary = combobox2_.getSelectedItem().toString();

			// get selected item and index of list1
			int index = list1_.getSelectedIndex();
			String selected = listModel1_.getElementAt(index).toString();

			// selected item is different with the boundary
			if (selected.equals(boundary) == false)
				if (listModel1_.contains(boundary) == false)
					listModel1_.setElementAt(boundary, index);
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
				int messageType = 0;

				// check textfield
				if (owner_.checkText(textfield, messageType) == false)
					setDefaultText(textfield);
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

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to all
		else {
			textfield1_.setText(defaultName);
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
			if (solver.getProblemType() == Solver.eigenSystem_)
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
