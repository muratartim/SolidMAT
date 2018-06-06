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


import java.util.Vector;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import boundary.BoundaryCase;
import math.Function;

import analysis.*;
import solver.Solver;
import main.Commons;
// import main.ImageHandler;

/**
 * Class for Add/Modify Analysis Cases menu.
 * 
 * @author Murat
 * 
 */
public class AnalysisCase2 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JTabbedPane tabbedpane1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	protected boolean add_;

	/** Mother frame of this dialog. */
	protected AnalysisCase1 owner_;

	/** Child panel of this dialog. */
	private AnalysisCasePanel1 panel1_;

	/** Child panel of this dialog. */
	private AnalysisCasePanel2 panel2_;

	/** Child panel of this dialog. */
	private AnalysisCasePanel3 panel3_;

	/** Child panel of this dialog. */
	private AnalysisCasePanel4 panel4_;

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
	public AnalysisCase2(AnalysisCase1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		panel1_ = new AnalysisCasePanel1(this);
		panel2_ = new AnalysisCasePanel2(this);
		panel3_ = new AnalysisCasePanel3(this);
		panel4_ = new AnalysisCasePanel4(this);
		JPanel panel6 = Commons.getPanel(null, Commons.flow_);

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to main panels
		panel6.add(button1);
		panel6.add(button2);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Static", panel1_);
		tabbedpane1_.addTab("Modal", panel2_);
		tabbedpane1_.addTab("Transient", panel3_);
		tabbedpane1_.addTab("Buckling", panel4_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel6);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);

		// If add is clicked set default, if not initialize
		if (add_) {
			panel1_.setDefaultText(new JTextField());
			panel2_.setDefaultText(new JTextField());
			panel3_.setDefaultText(new JTextField());
			panel4_.setDefaultText(new JTextField());
		} else
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
		Analysis selected = owner_.temporary_.get(index);

		// get type
		int type = selected.getType();

		// set tab
		tabbedpane1_.setSelectedIndex(type);

		// for linear static analysis
		if (type == Analysis.linearStatic_) {
			panel1_.initialize(selected);
			panel2_.setDefaultText(panel2_.textfield1_);
			panel3_.setDefaultText(panel3_.textfield1_);
			panel3_.setDefaultText(panel3_.textfield2_);
			panel3_.setDefaultText(panel3_.textfield3_);
			panel3_.setDefaultText(panel3_.textfield4_);
			panel3_.setDefaultText(panel3_.textfield5_);
			panel3_.setDefaultText(panel3_.textfield6_);
			panel4_.setDefaultText(panel4_.textfield1_);
			panel4_.setDefaultText(panel4_.textfield4_);
		}

		// for modal analysis
		else if (type == Analysis.modal_) {
			panel2_.initialize(selected);
			panel1_.setDefaultText(panel1_.textfield1_);
			panel1_.setDefaultText(panel1_.textfield2_);
			panel3_.setDefaultText(panel3_.textfield1_);
			panel3_.setDefaultText(panel3_.textfield2_);
			panel3_.setDefaultText(panel3_.textfield3_);
			panel3_.setDefaultText(panel3_.textfield4_);
			panel3_.setDefaultText(panel3_.textfield5_);
			panel3_.setDefaultText(panel3_.textfield6_);
			panel4_.setDefaultText(panel4_.textfield1_);
			panel4_.setDefaultText(panel4_.textfield4_);
		}

		// for linear transient analysis
		else if (type == Analysis.linearTransient_) {
			panel3_.initialize(selected);
			panel1_.setDefaultText(panel1_.textfield1_);
			panel1_.setDefaultText(panel1_.textfield2_);
			panel2_.setDefaultText(panel2_.textfield1_);
			panel4_.setDefaultText(panel4_.textfield1_);
			panel4_.setDefaultText(panel4_.textfield4_);
		}

		// for linear buckling analysis
		else if (type == Analysis.linearBuckling_) {
			panel4_.initialize(selected);
			panel1_.setDefaultText(panel1_.textfield1_);
			panel1_.setDefaultText(panel1_.textfield2_);
			panel2_.setDefaultText(panel2_.textfield1_);
			panel3_.setDefaultText(panel3_.textfield1_);
			panel3_.setDefaultText(panel3_.textfield2_);
			panel3_.setDefaultText(panel3_.textfield3_);
			panel3_.setDefaultText(panel3_.textfield4_);
			panel3_.setDefaultText(panel3_.textfield5_);
			panel3_.setDefaultText(panel3_.textfield6_);
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
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// get type
		int type = tabbedpane1_.getSelectedIndex();

		// get name textfield to be checked
		JTextField textfield = null;

		// linear static analysis
		if (type == Analysis.linearStatic_)
			textfield = panel1_.textfield1_;

		// modal analysis
		else if (type == Analysis.modal_)
			textfield = panel2_.textfield1_;

		// linear dynamic analysis
		else if (type == Analysis.linearTransient_)
			textfield = panel3_.textfield1_;

		// linear buckling analysis
		else if (type == Analysis.linearBuckling_)
			textfield = panel4_.textfield1_;

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield, 1)) {

				// check if boundary case list is empty
				boolean check = true;

				// linear static analysis
				if (type == Analysis.linearStatic_
						&& panel1_.listModel1_.isEmpty()) {

					// display message
					JOptionPane.showMessageDialog(this,
							"No boundaries assigned!", "False data entry", 2);
					check = false;
				}

				// linear transient analysis
				else if (type == Analysis.linearTransient_) {

					// check if boundary case list is empty
					if (panel3_.listModel1_.isEmpty()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"No boundaries assigned!", "False data entry",
								2);
						check = false;
					}

					// check if incompatible load time function assigned
					if (check) {

						// get loading time function
						Function func = null;

						// get vector of all functions
						Vector<Function> allfunc = owner_.owner_.inputData_
								.getFunction();

						// get name of selected function
						String fName = panel3_.combobox4_.getSelectedItem()
								.toString();

						// loop over all functions
						for (int i = 0; i < allfunc.size(); i++) {

							// get name
							String funcName = allfunc.get(i).getName();

							// check if it is same
							if (funcName.equals(fName)) {
								func = allfunc.get(i);
								break;
							}
						}

						// get time data
						int n = Integer.parseInt(panel3_.textfield2_.getText());
						double sz = Double.parseDouble(panel3_.textfield3_
								.getText());

						// assign function to a trial analysis
						try {
							LinearTransient ld = new LinearTransient("trial");
							ld.setTimeParameters(n, sz);
							ld.setLoadTimeFunction(func);
						}

						// incompatible function
						catch (Exception e) {

							// display message
							JOptionPane.showMessageDialog(this, e
									.getLocalizedMessage(), "False data entry",
									2);
							check = false;
						}
					}
				}

				// linear buckling analysis
				else if (type == Analysis.linearBuckling_
						&& panel4_.listModel1_.isEmpty()) {

					// display message
					JOptionPane.showMessageDialog(this,
							"No boundaries assigned!", "False data entry", 2);
					check = false;
				}

				// proceed if everything is ok
				if (check) {
					actionOkAddModify(type);
					setVisible(false);
				}
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield.getText().equals(selected)) {

				// check if boundary case list is empty
				boolean check = true;

				// linear static analysis
				if (type == Analysis.linearStatic_
						&& panel1_.listModel1_.isEmpty()) {

					// display message
					JOptionPane.showMessageDialog(this,
							"No boundaries assigned!", "False data entry", 2);
					check = false;
				}

				// linear transient analysis
				else if (type == Analysis.linearTransient_) {

					// check if boundary case list is empty
					if (panel3_.listModel1_.isEmpty()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"No boundaries assigned!", "False data entry",
								2);
						check = false;
					}

					// check if incompatible load time function assigned
					if (check) {

						// get loading time function
						Function func = null;

						// get vector of all functions
						Vector<Function> allfunc = owner_.owner_.inputData_
								.getFunction();

						// get name of selected function
						String fName = panel3_.combobox4_.getSelectedItem()
								.toString();

						// loop over all functions
						for (int i = 0; i < allfunc.size(); i++) {

							// get name
							String funcName = allfunc.get(i).getName();

							// check if it is same
							if (funcName.equals(fName)) {
								func = allfunc.get(i);
								break;
							}
						}

						// get time data
						int n = Integer.parseInt(panel3_.textfield2_.getText());
						double sz = Double.parseDouble(panel3_.textfield3_
								.getText());

						// assign function to a trial analysis
						try {
							LinearTransient ld = new LinearTransient("trial");
							ld.setTimeParameters(n, sz);
							ld.setLoadTimeFunction(func);
						}

						// incompatible function
						catch (Exception e) {

							// display message
							JOptionPane.showMessageDialog(this, e
									.getLocalizedMessage(), "False data entry",
									2);
							check = false;
						}
					}
				}

				// linear buckling analysis
				else if (type == Analysis.linearBuckling_
						&& panel4_.listModel1_.isEmpty()) {

					// display message
					JOptionPane.showMessageDialog(this,
							"No boundaries assigned!", "False data entry", 2);
					check = false;
				}

				// proceed if everything's ok
				if (check) {
					actionOkAddModify(type);
					setVisible(false);
				}
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield, 1)) {

					// check if boundary case list is empty
					boolean check = true;

					// linear static analysis
					if (type == Analysis.linearStatic_
							&& panel1_.listModel1_.isEmpty()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"No boundaries assigned!", "False data entry",
								2);
						check = false;
					}

					// linear transient analysis
					else if (type == Analysis.linearTransient_) {

						// check if boundary case list is empty
						if (panel3_.listModel1_.isEmpty()) {

							// display message
							JOptionPane.showMessageDialog(this,
									"No boundaries assigned!",
									"False data entry", 2);
							check = false;
						}

						// check if incompatible load time function assigned
						if (check) {

							// get loading time function
							Function func = null;

							// get vector of all functions
							Vector<Function> allfunc = owner_.owner_.inputData_
									.getFunction();

							// get name of selected function
							String fName = panel3_.combobox4_.getSelectedItem()
									.toString();

							// loop over all functions
							for (int i = 0; i < allfunc.size(); i++) {

								// get name
								String funcName = allfunc.get(i).getName();

								// check if it is same
								if (funcName.equals(fName)) {
									func = allfunc.get(i);
									break;
								}
							}

							// get time data
							int n = Integer.parseInt(panel3_.textfield2_
									.getText());
							double sz = Double.parseDouble(panel3_.textfield3_
									.getText());

							// assign function to a trial analysis
							try {
								LinearTransient ld = new LinearTransient(
										"trial");
								ld.setTimeParameters(n, sz);
								ld.setLoadTimeFunction(func);
							}

							// incompatible function
							catch (Exception e) {

								// display message
								JOptionPane.showMessageDialog(this, e
										.getLocalizedMessage(),
										"False data entry", 2);
								check = false;
							}
						}
					}

					// linear buckling analysis
					else if (type == Analysis.linearBuckling_
							&& panel4_.listModel1_.isEmpty()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"No boundaries assigned!", "False data entry",
								2);
						check = false;
					}

					// proceed if everything's ok
					if (check) {
						actionOkAddModify(type);
						setVisible(false);
					}
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 * @param type
	 *            The type of object.
	 */
	private void actionOkAddModify(int type) {

		// initialize name and values
		Analysis object = null;
		String name = null;

		// for linear static analysis
		if (type == Analysis.linearStatic_) {

			// get name
			name = panel1_.textfield1_.getText();

			// get all solvers
			Vector<Solver> solvers = owner_.owner_.inputData_.getSolver();

			// get solver
			String sol = panel1_.combobox1_.getSelectedItem().toString();
			Solver solver = null;
			for (int i = 0; i < solvers.size(); i++) {
				if (sol.equals(solvers.get(i).getName())) {
					solver = solvers.get(i);
					break;
				}
			}

			// create boundaries vector and scales array
			Vector<BoundaryCase> bound = new Vector<BoundaryCase>();
			double[] scales = new double[panel1_.listModel2_.size()];

			// get vector of all boundary cases
			Vector<BoundaryCase> allBound = owner_.owner_.inputData_
					.getBoundaryCase();

			// loop over boundary case list
			for (int i = 0; i < panel1_.listModel1_.size(); i++) {

				// get name and factor of boundary
				String bName = panel1_.listModel1_.get(i).toString();
				double factor = Double.parseDouble(panel1_.listModel2_.get(i)
						.toString());

				// loop over all boundary cases
				for (int j = 0; j < allBound.size(); j++) {

					// get name
					String allName = allBound.get(j).getName();

					// check name
					if (bName.equals(allName)) {
						bound.add(allBound.get(j));
						scales[bound.indexOf(allBound.get(j))] = factor;
						break;
					}
				}
			}

			// create and set object
			LinearStatic ls = new LinearStatic(name);
			ls.setSolver(solver);
			ls.setBoundaries(bound, scales);
			object = ls;
		}

		// for modal analysis
		else if (type == Analysis.modal_) {

			// get name
			name = panel2_.textfield1_.getText();

			// get all solvers
			Vector<Solver> solvers = owner_.owner_.inputData_.getSolver();

			// get solver
			String sol = panel2_.combobox1_.getSelectedItem().toString();
			Solver solver = null;
			for (int i = 0; i < solvers.size(); i++) {
				if (sol.equals(solvers.get(i).getName())) {
					solver = solvers.get(i);
					break;
				}
			}

			// create boundaries vector
			Vector<BoundaryCase> bound = new Vector<BoundaryCase>();

			// get vector of all boundary cases
			Vector<BoundaryCase> allBound = owner_.owner_.inputData_
					.getBoundaryCase();

			// loop over boundary case list
			for (int i = 0; i < panel2_.listModel1_.size(); i++) {

				// get name of boundary
				String bName = panel2_.listModel1_.get(i).toString();

				// loop over all boundary cases
				for (int j = 0; j < allBound.size(); j++) {

					// get name
					String allName = allBound.get(j).getName();

					// check name
					if (bName.equals(allName)) {
						bound.add(allBound.get(j));
						break;
					}
				}
			}

			// create and set object
			Modal modal = new Modal(name);
			modal.setSolver(solver);
			double[] scales = new double[bound.size()];
			modal.setBoundaries(bound, scales);
			object = modal;
		}

		// for linear dynamic analysis
		else if (type == Analysis.linearTransient_) {

			// get name
			name = panel3_.textfield1_.getText();

			// get time data
			int n = Integer.parseInt(panel3_.textfield2_.getText());
			double sz = Double.parseDouble(panel3_.textfield3_.getText());

			// get damping info
			double[] damp = panel3_.proporCoeff_;

			// get integration data
			int method = panel3_.method_;
			double[] par = panel3_.integrationPar_;

			// get loading time function
			Function func = null;

			// get vector of all functions
			Vector<Function> allfunc = owner_.owner_.inputData_.getFunction();

			// get name of selected function
			String fName = panel3_.combobox4_.getSelectedItem().toString();

			// loop over all functions
			for (int i = 0; i < allfunc.size(); i++) {

				// get name
				String funcName = allfunc.get(i).getName();

				// check if it is same
				if (funcName.equals(fName)) {
					func = allfunc.get(i);
					break;
				}
			}

			// get all solvers
			Vector<Solver> solvers = owner_.owner_.inputData_.getSolver();

			// get solver
			String sol = panel3_.combobox2_.getSelectedItem().toString();
			Solver solver = null;
			for (int i = 0; i < solvers.size(); i++) {
				if (sol.equals(solvers.get(i).getName())) {
					solver = solvers.get(i);
					break;
				}
			}

			// create boundaries vector and scales array
			Vector<BoundaryCase> bound = new Vector<BoundaryCase>();
			double[] scales = new double[panel3_.listModel2_.size()];

			// get vector of all boundary cases
			Vector<BoundaryCase> allBound = owner_.owner_.inputData_
					.getBoundaryCase();

			// loop over boundary case list
			for (int i = 0; i < panel3_.listModel1_.size(); i++) {

				// get name and factor of boundary
				String bName = panel3_.listModel1_.get(i).toString();
				double factor = Double.parseDouble(panel3_.listModel2_.get(i)
						.toString());

				// loop over all boundary cases
				for (int j = 0; j < allBound.size(); j++) {

					// get name
					String allName = allBound.get(j).getName();

					// check name
					if (bName.equals(allName)) {
						bound.add(allBound.get(j));
						scales[bound.indexOf(allBound.get(j))] = factor;
						break;
					}
				}
			}

			// create and set object
			LinearTransient ld = new LinearTransient(name);
			ld.setTimeParameters(n, sz);
			ld.setSolver(solver);
			ld.setProportionalCoefficients(damp[0], damp[1]);
			ld.setIntegrationMethod(method);
			if (method == LinearTransient.newmark_)
				ld.setNewmarkParameters(par[0], par[1]);
			else if (method == LinearTransient.wilson_)
				ld.setWilsonParameter(par[0]);
			ld.setBoundaries(bound, scales);
			ld.setLoadTimeFunction(func);
			object = ld;
		}

		// for linear buckling analysis
		else if (type == Analysis.linearBuckling_) {

			// get name
			name = panel4_.textfield1_.getText();

			// get all solvers
			Vector<Solver> solvers = owner_.owner_.inputData_.getSolver();

			// get solver
			String sol = panel4_.combobox1_.getSelectedItem().toString();
			Solver solver = null;
			for (int i = 0; i < solvers.size(); i++) {
				if (sol.equals(solvers.get(i).getName())) {
					solver = solvers.get(i);
					break;
				}
			}

			// create boundaries vector and scales array
			Vector<BoundaryCase> bound = new Vector<BoundaryCase>();
			double[] scales = new double[panel4_.listModel2_.size()];

			// get vector of all boundary cases
			Vector<BoundaryCase> allBound = owner_.owner_.inputData_
					.getBoundaryCase();

			// loop over boundary case list
			for (int i = 0; i < panel4_.listModel1_.size(); i++) {

				// get name and factor of boundary
				String bName = panel4_.listModel1_.get(i).toString();
				double factor = Double.parseDouble(panel4_.listModel2_.get(i)
						.toString());

				// loop over all boundary cases
				for (int j = 0; j < allBound.size(); j++) {

					// get name
					String allName = allBound.get(j).getName();

					// check name
					if (bName.equals(allName)) {
						bound.add(allBound.get(j));
						scales[bound.indexOf(allBound.get(j))] = factor;
						break;
					}
				}
			}

			// create and set object
			LinearBuckling buckling = new LinearBuckling(name);
			buckling.setSolver(solver);
			buckling.setBoundaries(bound, scales);
			object = buckling;
		}

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
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield1
	 *            The textfield to be checked.
	 * @param messageType
	 *            The type of message to be displayed (No name given -> 0, Name
	 *            exists -> 1, Illegal value (non-numeric, zero) -> 2, Illegal
	 *            value (non-integer, zero, negative) -> 3, Illegal value
	 *            (non-numeric, zero, negative) -> 4, Illegal value
	 *            (non-numeric, negative) -> 5, Illegal value (non-numeric,
	 *            negative, >= 1) -> 6.
	 * @return True if the data entered is correct, False if not.
	 */
	protected boolean checkText(JTextField textfield1, int messageType) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield1.getText();

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

		// Illegal value (non-numeric, zero)
		else if (messageType == 2) {

			// check for non-numeric values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check for zero
				if (value == 0) {

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

		// Illegal value (non-integer, zero, negative)
		else if (messageType == 3) {

			// check for non-numeric values
			try {

				// convert text to double value
				int value = Integer.parseInt(text);

				// check for zero,negative
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

		// Illegal value (non-numeric, zero, negative)
		else if (messageType == 4) {

			// check for non-numeric values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check for zero,negative
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

		// Illegal value (non-numeric, negative)
		else if (messageType == 5) {

			// check for non-numeric values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check for zero
				if (value < 0) {

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

		// Illegal value (non-numeric, negative, >= 1)
		else if (messageType == 6) {

			// check for non-numeric values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check for zero
				if (value < 0 || value >= 1) {

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
}
