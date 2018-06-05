package dialogs.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup; // import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;

import visualize.PostVisualizer;

/**
 * Class for Display Deformed Shape menu.
 * 
 * @author Murat
 * 
 */
public class DisplayDeformedShape1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_;

	private JRadioButton radiobutton1_, radiobutton2_;

	private JTextField textfield1_;

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
	public DisplayDeformedShape1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Deformed Shape", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Multivalued", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Scaling", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Step :");

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Auto", true);
		radiobutton2_ = new JRadioButton("Scale factor :", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// build text fields and set font
		textfield1_ = new JTextField("1.0");
		textfield1_.setEnabled(false);
		textfield1_.setPreferredSize(new Dimension(80, 20));

		// build spinner
		SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel();
		spinnerModel1.setMinimum(0);
		spinnerModel1.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		spinner1_ = new JSpinner(spinnerModel1);
		spinner1_.setPreferredSize(new Dimension(146, 20));

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, spinner1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 1, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * If ok is clicked calls actionOk, if cancel clicked sets dialog unvisible,
	 * if other buttons clicked sets appropriate checkboxes.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					actionOk();
					return null;
				}
			};

			// display progressor and disable frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// cancel button clicked
		else if (e.getSource().equals(button2_)) {

			// set dialog unvisible
			setVisible(false);
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
	private void actionOk() {

		// check textfields
		progressor_.setStatusMessage("Checking data...");
		if (checkText()) {

			// get texts
			int step = (Integer) spinner1_.getValue();
			double factor = Double.parseDouble(textfield1_.getText());

			// set step number to structure
			progressor_.setStatusMessage("Setting step number...");
			owner_.structure_.setStep(owner_.path_, step);

			// set options to post-visualizer
			int option = PostVisualizer.noOption_;
			int[] comp = { 0, 0 };

			// auto scaling
			if (radiobutton1_.isSelected())
				owner_.postVis_.setScalingFactor(null);

			// scaling factor given
			else if (radiobutton2_.isSelected())
				owner_.postVis_.setScalingFactor(factor);

			// draw
			progressor_.setStatusMessage("Drawing...");
			owner_.drawPost(true, option, comp, step);

			// close progressor
			progressor_.close();

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * Checks textfields
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkText() {

		// get the entered text
		String text = textfield1_.getText();

		// check for non-numeric or negative values
		try {

			// convert text to double value
			double val = Double.parseDouble(text);
			int val1 = (Integer) spinner1_.getValue();

			// check for negative values
			if (val <= 0.0 || val1 < 0) {

				// close progressor and enable dialog
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, "Illegal values!",
						"False data entry", 2);
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
				return false;
			}
		} catch (Exception excep) {

			// close progressor and enable dialog
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(DisplayDeformedShape1.this,
					"Illegal values!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 event
		if (event.getSource().equals(radiobutton1_)) {

			// set textfields disabled
			textfield1_.setEnabled(false);
		}

		// radiobutton2 event
		else if (event.getSource().equals(radiobutton2_)) {

			// set textfield enabled
			textfield1_.setEnabled(true);
		}
	}
}
