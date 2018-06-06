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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;

import boundary.*;

import analysis.Analysis;

/**
 * Class for Boundary Case Library menu.
 * 
 * @author Murat
 * 
 */
public class BoundaryCase1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JList list1_;

	protected DefaultListModel listModel1_;

	/** Temporary vector used for storing objects. */
	protected Vector<BoundaryCase> temporary_ = new Vector<BoundaryCase>();

	/** Used for renewing prescribed displacements objects vector. */
	protected Vector<DispLoad> dispLoad_ = new Vector<DispLoad>();

	/** Used for renewing initial displacements objects vector. */
	protected Vector<InitialDisp> initialDisp_ = new Vector<InitialDisp>();

	/** Used for renewing initial velocities objects vector. */
	protected Vector<InitialVelo> initialVelo_ = new Vector<InitialVelo>();

	/** Used for renewing constraint objects vector. */
	protected Vector<Constraint> constraint_ = new Vector<Constraint>();

	/** Used for renewing nodal mechanical loads objects vector. */
	protected Vector<NodalMechLoad> nodalMechLoad_ = new Vector<NodalMechLoad>();

	/** Used for renewing element mechanical loads objects vector. */
	protected Vector<ElementMechLoad> elementMechLoad_ = new Vector<ElementMechLoad>();

	/** Used for renewing element temperature loads objects vector. */
	protected Vector<ElementTemp> elementTemp_ = new Vector<ElementTemp>();

	/** Used for renewing analysis cases objects vector. */
	protected Vector<Analysis> analysis_ = new Vector<Analysis>();

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public BoundaryCase1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Boundary Case Library", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Cases", Commons.gridbag_);

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(11);
		list1_.setFixedCellWidth(120);
		list1_.setFixedCellHeight(15);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// build buttons
		JButton button1 = new JButton("Add");
		JButton button2 = new JButton("Modify");
		JButton button3 = new JButton("Delete");
		JButton button4 = new JButton("  OK  ");
		JButton button5 = new JButton("Cancel");

		// add components to panels
		Commons.addComponent(panel3, scrollpane1, 0, 1, 1, 5);
		Commons.addComponent(panel3, button1, 0, 0, 1, 1);
		Commons.addComponent(panel3, button2, 1, 0, 1, 1);
		Commons.addComponent(panel3, button3, 2, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button4);
		panel2.add(button5);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		button5.addActionListener(this);

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// set the input data vector to temporary vector
		Vector<BoundaryCase> object1 = owner_.inputData_.getBoundaryCase();
		for (int i = 0; i < object1.size(); i++)
			temporary_.add(object1.get(i));

		// set prescribed displacement vector from input vector
		Vector<DispLoad> object3 = owner_.inputData_.getDispLoad();
		for (int i = 0; i < object3.size(); i++)
			dispLoad_.add(object3.get(i));

		// set element temperature load vector from input vector
		Vector<ElementTemp> object4 = owner_.inputData_.getElementTemp();
		for (int i = 0; i < object4.size(); i++)
			elementTemp_.add(object4.get(i));

		// set constraint vector from input vector
		Vector<Constraint> object5 = owner_.inputData_.getConstraint();
		for (int i = 0; i < object5.size(); i++)
			constraint_.add(object5.get(i));

		// set nodal mechanical load vector from input vector
		Vector<NodalMechLoad> object6 = owner_.inputData_.getNodalMechLoad();
		for (int i = 0; i < object6.size(); i++)
			nodalMechLoad_.add(object6.get(i));

		// set element mechanical load vector from input vector
		Vector<ElementMechLoad> object7 = owner_.inputData_
				.getElementMechLoad();
		for (int i = 0; i < object7.size(); i++)
			elementMechLoad_.add(object7.get(i));

		// set analysis case vector from input vector
		Vector<Analysis> object8 = owner_.inputData_.getAnalysis();
		for (int i = 0; i < object8.size(); i++)
			analysis_.add(object8.get(i));

		// set initial displacement vector from input vector
		Vector<InitialDisp> object9 = owner_.inputData_.getInitialDisp();
		for (int i = 0; i < object9.size(); i++)
			initialDisp_.add(object9.get(i));

		// set initial velocity vector from input vector
		Vector<InitialVelo> object10 = owner_.inputData_.getInitialVelo();
		for (int i = 0; i < object10.size(); i++)
			initialVelo_.add(object10.get(i));

		// copy names to list
		for (int i = 0; i < temporary_.size(); i++)
			listModel1_.addElement(temporary_.get(i).getName());
	}

	/**
	 * If add or modify button clicked, builds child dialog. If delete button
	 * clicked, calls actionDelete. If ok button clicked, calls actionOk. If
	 * cancel button clicked sets dialog unvisible.
	 */
	public void actionPerformed(ActionEvent e) {

		// add button clicked
		if (e.getActionCommand() == "Add") {

			// build child dialog and set visible
			BoundaryCase2 dialog = new BoundaryCase2(this, true);
			dialog.setVisible(true);
		}

		// modify button clicked
		else if (e.getActionCommand() == "Modify") {

			// check if any item selected in list
			if (list1_.isSelectionEmpty() == false) {

				// build child dialog and set visible
				BoundaryCase2 dialog = new BoundaryCase2(this, false);
				dialog.setVisible(true);
			}
		}

		// delete button clicked
		else if (e.getActionCommand() == "Delete") {

			// check if any item selected in list
			if (list1_.isSelectionEmpty() == false) {

				// call actionDelete
				actionDelete();
			}
		}

		// ok button clicked
		else if (e.getActionCommand() == "  OK  ") {

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
	 * Deletes selected list item from temporary vector and list.
	 */
	private void actionDelete() {

		// check if there is only one item
		if (listModel1_.size() != 1) {

			// check for assigned boundaries
			if (checkAssigned()) {

				// display confirmation message
				int confirm = JOptionPane.showConfirmDialog(BoundaryCase1.this,
						"Boundary case is assigned to other libraries."
								+ "\nDo you still want to delete?",
						"Data confirmation", JOptionPane.YES_NO_OPTION);

				// yes is chosen
				if (confirm == JOptionPane.YES_OPTION) {

					// delete assigned boundaries
					deleteAssigned();

					// get the selection index from list
					int selected = list1_.getSelectedIndex();

					// delete it from vector
					temporary_.remove(selected);

					// delete it from list
					listModel1_.remove(selected);
				}
			}

			// no assigned boundaries
			else {

				// get the selection index from list
				int selected = list1_.getSelectedIndex();

				// delete it from vector
				temporary_.remove(selected);

				// delete it from list
				listModel1_.remove(selected);
			}
		}

		// if there is only one item
		else if (listModel1_.size() == 1) {

			// display message
			JOptionPane.showMessageDialog(BoundaryCase1.this,
					"At least one case should exist!", "False data entry", 2);
		}
	}

	/**
	 * Sets temporary vector to input data vector. Sets dialog unvisible.
	 */
	private void actionOk() {

		// set prescribed displacement vector to input data vector
		owner_.inputData_.setDispLoad(dispLoad_);

		// set initial displacement vector to input data vector
		owner_.inputData_.setInitialDisp(initialDisp_);

		// set initial velocity vector to input data vector
		owner_.inputData_.setInitialVelo(initialVelo_);

		// set analysis case vector to input data vector
		owner_.inputData_.setAnalysis(analysis_);

		// set element temperature load vector to input data vector
		owner_.inputData_.setElementTemp(elementTemp_);

		// set constraint vector to input data vector
		owner_.inputData_.setConstraint(constraint_);

		// set nodal mechanical load vector to input data vector
		owner_.inputData_.setNodalMechLoad(nodalMechLoad_);

		// set element mechanical load vector to input data vector
		owner_.inputData_.setElementMechLoad(elementMechLoad_);

		// set temporary vector to input data vector
		owner_.inputData_.setBoundaryCase(temporary_);

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * Checks for assigned boundaries and analysis cases.
	 * 
	 * @return True if there is any assigned, False if not.
	 */
	private boolean checkAssigned() {

		// get selected item
		String item = list1_.getSelectedValue().toString();

		// check for prescribed displacements
		for (int i = 0; i < dispLoad_.size(); i++) {

			// get boundary case name
			String name = dispLoad_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for initial displacements
		for (int i = 0; i < initialDisp_.size(); i++) {

			// get boundary case name
			String name = initialDisp_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for initial velocities
		for (int i = 0; i < initialVelo_.size(); i++) {

			// get boundary case name
			String name = initialVelo_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for nodal mechanical loads
		for (int i = 0; i < nodalMechLoad_.size(); i++) {

			// get boundary case name
			String name = nodalMechLoad_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for element mechanical loads
		for (int i = 0; i < elementMechLoad_.size(); i++) {

			// get boundary case name
			String name = elementMechLoad_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for element temperature loads
		for (int i = 0; i < elementTemp_.size(); i++) {

			// get boundary case name
			String name = elementTemp_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for constraints
		for (int i = 0; i < constraint_.size(); i++) {

			// get boundary case name
			String name = constraint_.get(i).getBoundaryCase().getName();

			// return true if assigned
			if (name.equals(item))
				return true;
		}

		// check for analysis cases
		for (int i = 0; i < analysis_.size(); i++) {

			// get boundary cases vector
			Vector<BoundaryCase> bc = analysis_.get(i).getBoundaries();

			// loop over boundary cases array
			for (int j = 0; j < bc.size(); j++) {

				// return true if assigned
				if (bc.get(j).getName().equals(item))
					return true;
			}
		}

		// no boundaries assigned
		return false;
	}

	/**
	 * Removes assigned boundaries and analysis cases.
	 * 
	 */
	private void deleteAssigned() {

		// get selected item
		String item = list1_.getSelectedValue().toString();

		// delete assigned prescribed displacements
		Vector<DispLoad> dispTemp = new Vector<DispLoad>();
		for (int i = 0; i < dispLoad_.size(); i++) {
			String name = dispLoad_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				dispTemp.add(dispLoad_.get(i));
		}
		dispLoad_ = dispTemp;

		// delete assigned initial displacements
		Vector<InitialDisp> initdispTemp = new Vector<InitialDisp>();
		for (int i = 0; i < initialDisp_.size(); i++) {
			String name = initialDisp_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				initdispTemp.add(initialDisp_.get(i));
		}
		initialDisp_ = initdispTemp;

		// delete assigned initial velocities
		Vector<InitialVelo> initveloTemp = new Vector<InitialVelo>();
		for (int i = 0; i < initialVelo_.size(); i++) {
			String name = initialVelo_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				initveloTemp.add(initialVelo_.get(i));
		}
		initialVelo_ = initveloTemp;

		// delete assigned nodal mechanical loads
		Vector<NodalMechLoad> nodalMechTemp = new Vector<NodalMechLoad>();
		for (int i = 0; i < nodalMechLoad_.size(); i++) {
			String name = nodalMechLoad_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				nodalMechTemp.add(nodalMechLoad_.get(i));
		}
		nodalMechLoad_ = nodalMechTemp;

		// delete assigned element mechanical loads
		Vector<ElementMechLoad> elementMechTemp = new Vector<ElementMechLoad>();
		for (int i = 0; i < elementMechLoad_.size(); i++) {
			String name = elementMechLoad_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				elementMechTemp.add(elementMechLoad_.get(i));
		}
		elementMechLoad_ = elementMechTemp;

		// delete assigned element temperature loads
		Vector<ElementTemp> elementTemp = new Vector<ElementTemp>();
		for (int i = 0; i < elementTemp_.size(); i++) {
			String name = elementTemp_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				elementTemp.add(elementTemp_.get(i));
		}
		elementTemp_ = elementTemp;

		// delete assigned constraints
		Vector<Constraint> consTemp = new Vector<Constraint>();
		for (int i = 0; i < constraint_.size(); i++) {
			String name = constraint_.get(i).getBoundaryCase().getName();
			if (name.equals(item) == false)
				consTemp.add(constraint_.get(i));
		}
		constraint_ = consTemp;

		// delete assigned analysis cases
		Vector<Analysis> analyTemp = new Vector<Analysis>();
		for (int i = 0; i < analysis_.size(); i++) {

			// get boundary cases vector
			Vector<BoundaryCase> bc = analysis_.get(i).getBoundaries();

			// boolean value for checking
			boolean add = true;

			// loop over boundary cases array
			for (int j = 0; j < bc.size(); j++) {

				// return true if assigned
				if (bc.get(j).getName().equals(item)) {
					add = false;
					break;
				}
			}

			// add case
			if (add)
				analyTemp.add(analysis_.get(i));
		}
		analysis_ = analyTemp;
	}
}
