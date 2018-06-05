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

import analysis.*;
import solver.*;

/**
 * Class for Add/Modify Solver Libraries menu.
 * 
 * @author Murat
 * 
 */
public class SolverLib2 extends JDialog implements ActionListener,
		FocusListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

	private JComboBox combobox1_, combobox2_, combobox3_;

	private JRadioButton radiobutton1_, radiobutton2_;

	private JTabbedPane tabbedpane1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected SolverLib1 owner_;

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
	public SolverLib2(SolverLib1 owner, boolean add) {

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

		// build sub-panels
		JPanel panel4 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Options", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Options", Commons.gridbag_);
		JPanel panel8 = Commons.getPanel("Parameters", Commons.gridbag_);
		JPanel panel9 = new JPanel();
		panel9.setLayout(new BoxLayout(panel9, BoxLayout.Y_AXIS));

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Solver type :");
		JLabel label3 = new JLabel("Solver :");
		JLabel label4 = new JLabel("Preconditioner :");
		JLabel label5 = new JLabel("Storage :");
		JLabel label6 = new JLabel("Name :");
		JLabel label7 = new JLabel("Solver :");
		JLabel label8 = new JLabel("Storage :");
		JLabel label9 = new JLabel("Number of eigenvalues :");
		JLabel label10 = new JLabel("Convergence tolerance :");
		JLabel label11 = new JLabel("Maximum iterations :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield2_.setEditable(false);
		textfield4_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(219, 20));
		textfield3_.setPreferredSize(new Dimension(219, 20));
		textfield5_.setPreferredSize(new Dimension(134, 20));

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Conjugate gradients",
				"Conjugate gradients squared", "BiConjugate gradients",
				"BiConjugate gradients stabilized", "Quasi-minimal residual",
				"Generalized minimal residual", "Iterative refinement" };
		String types2[] = { "Incomplete Cholesky", "Incomplete LU",
				"Diagonal preconditioning" };
		String types3[] = { "Subspace iteration", "Direct" };
		combobox1_ = new JComboBox(types1);
		combobox2_ = new JComboBox(types2);
		combobox3_ = new JComboBox(types3);
		combobox1_.setMaximumRowCount(5);
		combobox2_.setMaximumRowCount(3);
		combobox3_.setMaximumRowCount(2);
		combobox1_.setPreferredSize(new Dimension(177, 23));
		combobox3_.setPreferredSize(new Dimension(208, 23));

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Iterative", true);
		radiobutton2_ = new JRadioButton("Direct", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// add components to sub-panels
		Commons.addComponent(panel4, label1, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label2, 0, 0, 1, 1);
		Commons.addComponent(panel5, label3, 1, 0, 1, 1);
		Commons.addComponent(panel5, label4, 2, 0, 1, 1);
		Commons.addComponent(panel5, label5, 3, 0, 1, 1);
		Commons.addComponent(panel5, radiobutton1_, 0, 1, 1, 1);
		Commons.addComponent(panel5, radiobutton2_, 0, 2, 1, 1);
		Commons.addComponent(panel5, combobox1_, 1, 1, 2, 1);
		Commons.addComponent(panel5, combobox2_, 2, 1, 2, 1);
		Commons.addComponent(panel5, textfield2_, 3, 1, 2, 1);
		Commons.addComponent(panel6, label6, 0, 0, 1, 1);
		Commons.addComponent(panel6, textfield3_, 0, 1, 1, 1);
		Commons.addComponent(panel7, label7, 0, 0, 1, 1);
		Commons.addComponent(panel7, label8, 1, 0, 1, 1);
		Commons.addComponent(panel7, combobox3_, 0, 1, 1, 1);
		Commons.addComponent(panel7, textfield4_, 1, 1, 1, 1);
		Commons.addComponent(panel8, label9, 0, 0, 1, 1);
		Commons.addComponent(panel8, label10, 1, 0, 1, 1);
		Commons.addComponent(panel8, label11, 2, 0, 1, 1);
		Commons.addComponent(panel8, textfield5_, 0, 1, 1, 1);
		Commons.addComponent(panel8, textfield6_, 1, 1, 1, 1);
		Commons.addComponent(panel8, textfield7_, 2, 1, 1, 1);
		panel9.add(Box.createRigidArea(new Dimension(0, 56)));

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel4, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel9, 2, 0, 1, 1);
		Commons.addComponent(panel2, panel6, 0, 0, 1, 1);
		Commons.addComponent(panel2, panel7, 1, 0, 1, 1);
		Commons.addComponent(panel2, panel8, 2, 0, 1, 1);
		panel3.add(button1);
		panel3.add(button2);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Linear System", panel1);
		tabbedpane1_.addTab("Eigen System", panel2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel3);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		radiobutton1_.addActionListener(this);
		radiobutton2_.addActionListener(this);
		combobox1_.addItemListener(this);
		combobox2_.addItemListener(this);
		combobox3_.addItemListener(this);
		textfield1_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield7_.addFocusListener(this);

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
		Solver selected = owner_.temporary_.get(index);

		// get type
		int type = selected.getType();

		// Solver0
		if (type == Solver.solver0_) {

			// get solver
			Solver0 solver = (Solver0) selected;

			// set tab
			tabbedpane1_.setSelectedIndex(solver.getProblemType());

			// set name
			textfield1_.setText(solver.getName());

			// set solver type
			radiobutton1_.setSelected(true);
			setCombos();

			// set solver
			combobox1_.setSelectedIndex(solver.getSolverType());

			// set preconditioner
			combobox2_.setSelectedIndex(solver.getPreconditionerType());

			// set storage
			setStorage(selected);

			// set default for other textfields
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
		}

		// Solver1
		else if (type == Solver.solver1_) {

			// get solver
			Solver1 solver = (Solver1) selected;

			// set tab
			tabbedpane1_.setSelectedIndex(solver.getProblemType());

			// set name
			textfield1_.setText(solver.getName());

			// set solver type
			radiobutton2_.setSelected(true);
			setCombos();

			// set solver
			combobox1_.setSelectedIndex(solver.getSolverType());

			// set storage
			setStorage(selected);

			// set default for other textfields
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
		}

		// Solver2
		else if (type == Solver.solver2_) {

			// get solver
			Solver2 solver = (Solver2) selected;

			// set tab
			tabbedpane1_.setSelectedIndex(solver.getProblemType());

			// set name
			textfield1_.setText(solver.getName());

			// set solver type
			radiobutton2_.setSelected(true);
			setCombos();

			// set solver
			combobox1_.setSelectedIndex(solver.getSolverType());

			// set storage
			setStorage(selected);

			// set default for other textfields
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
		}

		// Solver4
		else if (type == Solver.solver3_) {

			// get solver
			Solver3 solver = (Solver3) selected;

			// set tab
			tabbedpane1_.setSelectedIndex(solver.getProblemType());

			// set name
			textfield3_.setText(solver.getName());

			// set # of eigenvalues
			textfield5_.setText(Integer.toString(solver
					.getNumberOfRequiredEigenvalues()));

			// set convergence tolerance
			textfield6_.setText(owner_.owner_.formatter_.format(solver
					.getConvergenceTolerance()));

			// set maximum iterations
			textfield7_.setText(Integer.toString(solver
					.getMaxNumberOfIterations()));

			// set solver
			combobox3_.setSelectedIndex(solver.getSolverType());

			// set storage
			setStorage(selected);

			// set default for other textfields
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
		}

		// Solver5
		else if (type == Solver.solver4_) {

			// get solver
			Solver4 solver = (Solver4) selected;

			// set tab
			tabbedpane1_.setSelectedIndex(solver.getProblemType());

			// set name
			textfield3_.setText(solver.getName());

			// set # of eigenvalues
			textfield5_.setText(Integer.toString(solver
					.getNumberOfRequiredEigenvalues()));

			// set convergence tolerance
			textfield6_.setText(owner_.owner_.formatter_.format(solver
					.getConvergenceTolerance()));

			// disable maximum iterations
			textfield7_.setEnabled(false);

			// set solver
			combobox3_.setSelectedIndex(solver.getSolverType());

			// set storage
			setStorage(selected);

			// set default for other textfields
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
			setDefaultText(textfield7_);
		}
	}

	/**
	 * Sets default values for textfields.
	 * 
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultName = "Solver1";
		String defaultValue1 = Integer.toString(10);
		String defaultValue2 = owner_.owner_.formatter_
				.format(Math.pow(10, -6));
		String defaultValue3 = Integer.toString(16);
		String defaultValue4 = "Compressed row storage";
		String defaultValue5 = "Upper symmetrical banded 1D storage";

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue4);

		// set to textfield3
		else if (textfield.equals(textfield3_))
			textfield3_.setText(defaultName);

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultValue5);

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultValue1);

		// set to textfield6
		else if (textfield.equals(textfield6_))
			textfield6_.setText(defaultValue2);

		// set to textfield7
		else if (textfield.equals(textfield7_))
			textfield7_.setText(defaultValue3);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue4);
			textfield3_.setText(defaultName);
			textfield4_.setText(defaultValue5);
			textfield5_.setText(defaultValue1);
			textfield6_.setText(defaultValue2);
			textfield7_.setText(defaultValue3);
		}
	}

	/**
	 * Performs related action depending on the button clicked.
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

		// iterative button clicked
		else if (e.getSource().equals(radiobutton1_)) {
			setCombos();
			setStorage(getObject());
		}

		// direct button clicked
		else if (e.getSource().equals(radiobutton2_)) {
			setCombos();
			setStorage(getObject());
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
			textfield = textfield3_;

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield, 1)) {
				actionOkAddModify();
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield.getText().equals(selected)) {

				// check for incompatibility with any assigned library
				if (checkLibrary()) {
					actionOkAddModify();
					setVisible(false);
				}
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield, 1)) {

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
	 * @return True if modified solver is compatible, False vice versa.
	 */
	private boolean checkLibrary() {

		// get name of selected solver
		String item = owner_.list1_.getSelectedValue().toString();

		// get current tab
		int problemType = tabbedpane1_.getSelectedIndex();

		// check for analysis cases
		for (int i = 0; i < owner_.analysis_.size(); i++) {

			// get type of analysis case
			int type = owner_.analysis_.get(i).getType();

			// for linear static analysis
			if (type == Analysis.linearStatic_) {

				// get linear static analysis
				LinearStatic ls = (LinearStatic) owner_.analysis_.get(i);

				// check if name is same
				if (item.equals(ls.getSolver().getName())) {

					// problem type incompatible
					if (problemType != ls.getSolver().getProblemType()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"Incompatibility encountered with the assigned libraries!"
										+ "\nCannot modify solver.",
								"False data entry", 2);
						return false;
					}
				}
			}

			// for modal analysis
			else if (type == Analysis.modal_) {

				// get modal analysis
				Modal m = (Modal) owner_.analysis_.get(i);

				// check if name is same
				if (item.equals(m.getSolver().getName())) {

					// problem type incompatible
					if (problemType != m.getSolver().getProblemType()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"Incompatibility encountered with the assigned libraries!"
										+ "\nCannot modify solver.",
								"False data entry", 2);
						return false;
					}
				}
			}

			// for linear transient analysis
			else if (type == Analysis.linearTransient_) {

				// get linear transient analysis
				LinearTransient lt = (LinearTransient) owner_.analysis_.get(i);

				// check if name is same
				if (item.equals(lt.getSolver().getName())) {

					// problem type incompatible
					if (problemType != lt.getSolver().getProblemType()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"Incompatibility encountered with the assigned libraries!"
										+ "\nCannot modify solver.",
								"False data entry", 2);
						return false;
					}
				}
			}

			// for linear buckling analysis
			else if (type == Analysis.linearBuckling_) {

				// get linear buckling analysis
				LinearBuckling lb = (LinearBuckling) owner_.analysis_.get(i);

				// check if name is same
				if (item.equals(lb.getSolver().getName())) {

					// problem type incompatible
					if (problemType != lb.getSolver().getProblemType()) {

						// display message
						JOptionPane.showMessageDialog(this,
								"Incompatibility encountered with the assigned libraries!"
										+ "\nCannot modify solver.",
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

		// get object
		Solver object = getObject();

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(object.getName());
		}

		// modify button clicked
		else if (add_ == false) {

			// get name of selected solver
			String item = owner_.list1_.getSelectedValue().toString();

			// check for analysis cases
			for (int i = 0; i < owner_.analysis_.size(); i++) {

				// get type of analysis case
				int typ = owner_.analysis_.get(i).getType();

				// for linear static analysis
				if (typ == Analysis.linearStatic_) {

					// get linear static analysis
					LinearStatic ls = (LinearStatic) owner_.analysis_.get(i);

					// check if name is same
					if (item.equals(ls.getSolver().getName()))
						ls.setSolver(object);
				}

				// for modal analysis
				else if (typ == Analysis.modal_) {

					// get modal analysis
					Modal m = (Modal) owner_.analysis_.get(i);

					// check if name is same
					if (item.equals(m.getSolver().getName()))
						m.setSolver(object);
				}

				// for linear transient analysis
				else if (typ == Analysis.linearTransient_) {

					// get linear transient analysis
					LinearTransient m = (LinearTransient) owner_.analysis_
							.get(i);

					// check if name is same
					if (item.equals(m.getSolver().getName()))
						m.setSolver(object);
				}

				// for linear buckling analysis
				else if (typ == Analysis.linearBuckling_) {

					// get linear buckling analysis
					LinearBuckling m = (LinearBuckling) owner_.analysis_.get(i);

					// check if name is same
					if (item.equals(m.getSolver().getName()))
						m.setSolver(object);
				}
			}

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(object.getName(), index);
		}
	}

	/**
	 * Handles combobox events.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_))
			setStorage(getObject());

		// combobox2 event
		else if (event.getSource().equals(combobox2_))
			setStorage(getObject());

		// combobox3 event
		else if (event.getSource().equals(combobox3_)) {
			setStorage(getObject());

			// get selected index
			int index = combobox3_.getSelectedIndex();

			// Subspace
			if (index == GESolver.SubSpace_)
				textfield7_.setEnabled(true);

			// direct
			else if (index == GESolver.Direct_)
				textfield7_.setEnabled(false);
		}
	}

	/**
	 * Sets storage type depending on the selections made.
	 * 
	 * @param solver
	 *            Solver to be set
	 */
	private void setStorage(Solver solver) {

		// get tab
		int tab = tabbedpane1_.getSelectedIndex();
		JTextField text = null;
		if (tab == Solver.linearSystem_)
			text = textfield2_;
		else if (tab == Solver.eigenSystem_)
			text = textfield4_;

		// get storage
		int storage = solver.getStorageType();

		// set storage
		if (storage == Solver.CDS_)
			text.setText("Compressed diagonal storage");
		else if (storage == Solver.CRS_)
			text.setText("Compressed row storage");
		else if (storage == Solver.USPS_)
			text.setText("Upper symm. packed storage");
		else if (storage == Solver.USB1S_)
			text.setText("Upper symm. banded 1D storage");
		else if (storage == Solver.USB2S_)
			text.setText("Upper symm. banded 2D storage");
	}

	/**
	 * Creates and returns solver depending on the selections made.
	 * 
	 * @return Solver.
	 */
	private Solver getObject() {

		// get problem type
		int problemType = tabbedpane1_.getSelectedIndex();

		// linear system
		if (problemType == Solver.linearSystem_) {

			// get name
			String name = textfield1_.getText();

			// iterative solver
			if (radiobutton1_.isSelected()) {

				// get solver
				int solver = combobox1_.getSelectedIndex();

				// get preconditioner
				int precond = combobox2_.getSelectedIndex();

				// return object
				return new Solver0(name, solver, precond);
			}

			// direct solver
			else if (radiobutton2_.isSelected()) {

				// get solver
				int solver = combobox1_.getSelectedIndex();

				// COLSOL
				if (solver == LESolver.COLSOL_)
					return new Solver1(name);

				// GaussSymm
				else if (solver == LESolver.GaussSymm_)
					return new Solver2(name);
			}
		}

		// eigen system
		else if (problemType == Solver.eigenSystem_) {

			// get name
			String name = textfield3_.getText();

			// get solver
			int solver = combobox3_.getSelectedIndex();

			// get # of eigenvalues
			int nRoot = Integer.parseInt(textfield5_.getText());

			// get convergence tolerance
			double rTol = Double.parseDouble(textfield6_.getText());

			// SubSpace
			if (solver == GESolver.SubSpace_) {

				// get maximum iterations
				int maxit = Integer.parseInt(textfield7_.getText());

				// return object
				return new Solver3(name, nRoot, rTol, maxit);
			}

			// direct
			else if (solver == GESolver.Direct_) {

				// return object
				return new Solver4(name, nRoot, rTol);
			}
		}
		return null;
	}

	/**
	 * Sets solver type combobox for linear system solver.
	 * 
	 */
	private void setCombos() {

		// remove listeners
		combobox1_.removeItemListener(this);
		combobox2_.removeItemListener(this);

		// iterative
		if (radiobutton1_.isSelected()) {
			combobox1_.removeAllItems();
			String types[] = { "Conjugate gradients",
					"Conjugate gradients squared", "BiConjugate gradients",
					"BiConjugate gradients stabilized",
					"Quasi-minimal residual", "Generalized minimal residual",
					"Iterative refinement" };
			for (int i = 0; i < types.length; i++)
				combobox1_.addItem(types[i]);
			combobox2_.setEnabled(true);
		}

		// direct
		else if (radiobutton2_.isSelected()) {
			combobox1_.removeAllItems();
			String types[] = { "Active column solver",
					"Gauss elimination, symmetric" };
			for (int i = 0; i < types.length; i++)
				combobox1_.addItem(types[i]);
			combobox2_.setEnabled(false);
		}

		// add listeners
		combobox1_.addItemListener(this);
		combobox2_.addItemListener(this);
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
				if (tfield.equals(textfield5_))
					messageType = 2;
				else if (tfield.equals(textfield6_))
					messageType = 3;
				else if (tfield.equals(textfield7_))
					messageType = 4;

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
	 *            exists -> 1, Illegal value (non-integer, <=0 || >20) -> 2,
	 *            Illegal value (non-double, <=0) -> 3, Illegal value
	 *            (non-integer, <=0) -> 4).
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

		// Illegal value (non-integer, <=0 || >20)
		else if (messageType == 2) {

			// check for non-numeric values
			try {

				// convert text to integer value
				int value = Integer.parseInt(text);

				// check constraints
				if (value <= 0 || value > 20) {

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

		// Illegal value (non-double, <=0)
		else if (messageType == 3) {

			// check for non-numeric values
			try {

				// convert text to integer value
				double value = Double.parseDouble(text);

				// check constraints
				if (value <= 0.0) {

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

		// Illegal value (non-integer, <=0)
		else if (messageType == 4) {

			// check for non-numeric values
			try {

				// convert text to integer value
				int value = Integer.parseInt(text);

				// check constraints
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
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
