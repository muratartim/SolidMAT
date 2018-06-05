package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import boundary.NodalMechLoad;

import node.Node;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DVec;

/**
 * Class for Display Table Node Results menu.
 * 
 * @author Murat
 * 
 */
public class DisplayTNodeResult1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

	private JComboBox combobox1_, combobox2_;

	private JLabel label5_, label6_, label7_, label8_, label9_, label10_;

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
	public DisplayTNodeResult1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Node Results", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Node", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Options", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Results", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Node ID :");
		JLabel label2 = new JLabel("Step :");
		JLabel label3 = new JLabel("Coordinate system :");
		JLabel label4 = new JLabel("Type :");
		label5_ = new JLabel("UX :");
		label6_ = new JLabel("UY :");
		label7_ = new JLabel("UZ :");
		label8_ = new JLabel("RX :");
		label9_ = new JLabel("RY :");
		label10_ = new JLabel("RZ :");
		label5_.setPreferredSize(new Dimension(21, 23));

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Global", "Local" };
		String types2[] = { "Displacements", "Reaction forces" };
		combobox1_ = new JComboBox(types1);
		combobox2_ = new JComboBox(types2);
		combobox1_.setMaximumRowCount(2);
		combobox2_.setMaximumRowCount(2);

		// build spinner
		SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel();
		spinnerModel1.setMinimum(0);
		spinnerModel1.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		spinner1_ = new JSpinner(spinnerModel1);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
		textfield4_.setEditable(false);
		textfield5_.setEditable(false);
		textfield6_.setEditable(false);
		textfield7_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(80, 20));
		textfield2_.setPreferredSize(new Dimension(176, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Show");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button2_, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, label4, 2, 0, 1, 1);
		Commons.addComponent(panel4, spinner1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, combobox2_, 2, 1, 1, 1);
		Commons.addComponent(panel5, label5_, 0, 0, 1, 1);
		Commons.addComponent(panel5, label6_, 1, 0, 1, 1);
		Commons.addComponent(panel5, label7_, 2, 0, 1, 1);
		Commons.addComponent(panel5, label8_, 3, 0, 1, 1);
		Commons.addComponent(panel5, label9_, 4, 0, 1, 1);
		Commons.addComponent(panel5, label10_, 5, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel5, textfield5_, 3, 1, 1, 1);
		Commons.addComponent(panel5, textfield6_, 4, 1, 1, 1);
		Commons.addComponent(panel5, textfield7_, 5, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		panel2.add(button1_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		combobox1_.addItemListener(this);
		combobox2_.addItemListener(this);

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// set dialog unvisible
			setVisible(false);
		}

		// show button clicked
		else if (e.getSource().equals(button2_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					showNode();
					return null;
				}
			};

			// display progressor and disable frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
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
	private void showNode() {

		// check step number
		progressor_.setStatusMessage("Checking data...");
		if (checkText()) {

			// get the entered text
			String text = textfield1_.getText();

			// check for non-numeric and negative values
			try {

				// convert text to integer value
				int value = Integer.parseInt(text);

				// check if given node exists
				Node node = owner_.structure_.getNode(value);

				// get demanded coordinate system
				int coord = combobox1_.getSelectedIndex();

				// initialize result vector
				DVec result = null;

				// get demanded step number
				int step = (Integer) spinner1_.getValue();

				// set step number to node
				progressor_.setStatusMessage("Setting step number...");
				owner_.structure_.setStepToNode(owner_.path_, node, step);

				// displacements
				if (combobox2_.getSelectedIndex() == 0)
					result = node.getUnknown(coord);

				// reaction forces
				else if (combobox2_.getSelectedIndex() == 1)
					result = node.getReactionForce(coord);

				// set textfields
				textfield2_.setText(owner_.formatter_.format(result.get(0)));
				textfield3_.setText(owner_.formatter_.format(result.get(1)));
				textfield4_.setText(owner_.formatter_.format(result.get(2)));
				textfield5_.setText(owner_.formatter_.format(result.get(3)));
				textfield6_.setText(owner_.formatter_.format(result.get(4)));
				textfield7_.setText(owner_.formatter_.format(result.get(5)));

				// close progressor
				progressor_.close();
				setStill(false);
			} catch (Exception excep) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Given node does not exist!", "False data entry", 2);

				// set textfields
				textfield2_.setText("");
				textfield3_.setText("");
				textfield4_.setText("");
				textfield5_.setText("");
				textfield6_.setText("");
				textfield7_.setText("");
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

			// get selected index
			int index = combobox1_.getSelectedIndex();

			// initialize label to be changed
			String label;

			// for the "Global" option
			if (index == NodalMechLoad.global_) {
				label = label5_.getText().replace('1', 'X');
				label5_.setText(label);
				label = label6_.getText().replace('2', 'Y');
				label6_.setText(label);
				label = label7_.getText().replace('3', 'Z');
				label7_.setText(label);
				label = label8_.getText().replace('1', 'X');
				label8_.setText(label);
				label = label9_.getText().replace('2', 'Y');
				label9_.setText(label);
				label = label10_.getText().replace('3', 'Z');
				label10_.setText(label);
			}

			// for the "Local" option
			else if (index == NodalMechLoad.local_) {
				label = label5_.getText().replace('X', '1');
				label5_.setText(label);
				label = label6_.getText().replace('Y', '2');
				label6_.setText(label);
				label = label7_.getText().replace('Z', '3');
				label7_.setText(label);
				label = label8_.getText().replace('X', '1');
				label8_.setText(label);
				label = label9_.getText().replace('Y', '2');
				label9_.setText(label);
				label = label10_.getText().replace('Z', '3');
				label10_.setText(label);
			}
		}

		// combobox2 event
		else if (event.getSource().equals(combobox2_)) {

			// get selected index
			int index = combobox2_.getSelectedIndex();

			// initialize label to be changed
			String label;

			// for the "Displacements" option
			if (index == 0) {
				label = label5_.getText().replace('F', 'U');
				label5_.setText(label);
				label = label6_.getText().replace('F', 'U');
				label6_.setText(label);
				label = label7_.getText().replace('F', 'U');
				label7_.setText(label);
				label = label8_.getText().replace('M', 'R');
				label8_.setText(label);
				label = label9_.getText().replace('M', 'R');
				label9_.setText(label);
				label = label10_.getText().replace('M', 'R');
				label10_.setText(label);
			}

			// for the "Reaction forces" option
			else if (index == 1) {
				label = label5_.getText().replace('U', 'F');
				label5_.setText(label);
				label = label6_.getText().replace('U', 'F');
				label6_.setText(label);
				label = label7_.getText().replace('U', 'F');
				label7_.setText(label);
				label = label8_.getText().replace('R', 'M');
				label8_.setText(label);
				label = label9_.getText().replace('R', 'M');
				label9_.setText(label);
				label = label10_.getText().replace('R', 'M');
				label10_.setText(label);
			}
		}
	}

	/**
	 * Cgecks entered textfields.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkText() {

		// check for non-numeric or negative values
		try {

			// convert text1 to integer value
			int val1 = (Integer) spinner1_.getValue();

			// check if negative
			if (val1 < 0) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane
						.showMessageDialog(this,
								"Illegal value for step number!",
								"False data entry", 2);

				// set textfields
				textfield2_.setText("");
				textfield3_.setText("");
				textfield4_.setText("");
				textfield5_.setText("");
				textfield6_.setText("");
				textfield7_.setText("");

				return false;
			}

			// check step number
			if (val1 >= owner_.structure_.getNumberOfSteps()) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane
						.showMessageDialog(this,
								"Illegal value for step number!",
								"False data entry", 2);

				// set textfields
				textfield2_.setText("");
				textfield3_.setText("");
				textfield4_.setText("");
				textfield5_.setText("");
				textfield6_.setText("");
				textfield7_.setText("");

				return false;
			}
		} catch (Exception excep) {

			// close progressor and enable dialog
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this,
					"Illegal value for step number!", "False data entry", 2);

			// set textfields
			textfield2_.setText("");
			textfield3_.setText("");
			textfield4_.setText("");
			textfield5_.setText("");
			textfield6_.setText("");
			textfield7_.setText("");

			return false;
		}

		// entered values are correct
		return true;
	}
}
