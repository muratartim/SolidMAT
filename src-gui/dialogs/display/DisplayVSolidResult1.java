package dialogs.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;

import visualize.PostVisualizer;

import element.Element;

/**
 * Class for Display Solid Results menu.
 * 
 * @author Murat
 * 
 */
public class DisplayVSolidResult1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_;

	private JCheckBox checkbox1_;

	private JComboBox combobox1_, combobox2_;

	private JButton button1_, button2_, button3_;

	/** The progress monitor of processses that take place. */
	private Progressor progressor_;

	/** Scaling factor of drawing. */
	protected Double scale_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public DisplayVSolidResult1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Solid Results", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Multivalued", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Step :");
		JLabel label2 = new JLabel("Type :");
		JLabel label3 = new JLabel("Component :");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Displacements", "Elastic strains", "Stresses" };
		String types2[] = { "Displacement 1", "Displacement 2",
				"Displacement 3", "Rotation 1", "Rotation 2", "Rotation 3",
				"Resultant displacement", "Resultant rotation" };
		combobox1_ = new JComboBox(types1);
		combobox1_.setMaximumRowCount(4);
		combobox2_ = new JComboBox(types2);
		combobox2_.setMaximumRowCount(12);
		combobox1_.setPreferredSize(new Dimension(160, 20));

		// build spinner
		SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel();
		spinnerModel1.setMinimum(0);
		spinnerModel1.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		spinner1_ = new JSpinner(spinnerModel1);
		spinner1_.setPreferredSize(new Dimension(190, 20));

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("Deformed shape");

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");
		button3_ = new JButton("Options");
		button3_.setEnabled(false);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, spinner1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel4, checkbox1_, 2, 0, 1, 1);
		Commons.addComponent(panel4, button3_, 2, 1, 1, 1);

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
		button3_.addActionListener(this);
		combobox1_.addItemListener(this);
		checkbox1_.addItemListener(this);

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

		// options button clicked
		else if (e.getSource().equals(button3_)) {

			// create dialog and set visible
			DisplayVSolidResult2 dialog = new DisplayVSolidResult2(this);
			dialog.setVisible(true);
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

			// get step number
			int step = (Integer) spinner1_.getValue();

			// set step number to structure
			progressor_.setStatusMessage("Setting step number...");
			owner_.structure_.setStep(owner_.path_, step);

			// get result type
			int option = 0;
			int[] comp = { 0, 0 };

			// element displacements
			if (combobox1_.getSelectedIndex() == 0) {

				// set option
				option = PostVisualizer.elementDisp3D_;

				// set component
				comp[0] = combobox2_.getSelectedIndex();
			}

			// elastic strains
			else if (combobox1_.getSelectedIndex() == 1) {

				// set result option
				option = PostVisualizer.elasticStrains3D_;

				// set component
				int index = combobox2_.getSelectedIndex();
				if (index == 0) {
					comp[0] = 0;
					comp[1] = 0;
				} else if (index == 1) {
					comp[0] = 1;
					comp[1] = 1;
				} else if (index == 2) {
					comp[0] = 2;
					comp[1] = 2;
				} else if (index == 3) {
					comp[0] = 0;
					comp[1] = 1;
				} else if (index == 4) {
					comp[0] = 0;
					comp[1] = 2;
				} else if (index == 5) {
					comp[0] = 1;
					comp[1] = 2;
				} else if (index == 6)
					comp[0] = 3;
				else if (index == 7)
					comp[0] = 4;
				else if (index == 8) {
					option = PostVisualizer.principalStrains3D_;
					comp[0] = Element.minPrincipal_;
				} else if (index == 9) {
					option = PostVisualizer.principalStrains3D_;
					comp[0] = Element.midPrincipal_;
				} else if (index == 10) {
					option = PostVisualizer.principalStrains3D_;
					comp[0] = Element.maxPrincipal_;
				}
			}

			// stresses
			else if (combobox1_.getSelectedIndex() == 2) {

				// set result option
				option = PostVisualizer.stresses3D_;

				// set component
				int index = combobox2_.getSelectedIndex();
				if (index == 0) {
					comp[0] = 0;
					comp[1] = 0;
				} else if (index == 1) {
					comp[0] = 1;
					comp[1] = 1;
				} else if (index == 2) {
					comp[0] = 2;
					comp[1] = 2;
				} else if (index == 3) {
					comp[0] = 0;
					comp[1] = 1;
				} else if (index == 4) {
					comp[0] = 0;
					comp[1] = 2;
				} else if (index == 5) {
					comp[0] = 1;
					comp[1] = 2;
				} else if (index == 6)
					comp[0] = 3;
				else if (index == 7)
					comp[0] = 4;
				else if (index == 8) {
					option = PostVisualizer.principalStresses3D_;
					comp[0] = Element.minPrincipal_;
				} else if (index == 9) {
					option = PostVisualizer.principalStresses3D_;
					comp[0] = Element.midPrincipal_;
				} else if (index == 10) {
					option = PostVisualizer.principalStresses3D_;
					comp[0] = Element.maxPrincipal_;
				} else if (index == 11)
					option = PostVisualizer.misesStress3D_;
			}

			// auto scaling
			owner_.postVis_.setScalingFactor(scale_);

			// draw
			progressor_.setStatusMessage("Drawing...");
			owner_.drawPost(checkbox1_.isSelected(), option, comp, step);

			// close progressor
			progressor_.close();

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * Checks entered textfields.
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
			JOptionPane.showMessageDialog(this,
					"Illegal value for step number!", "False data entry", 2);
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

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// set combobox2
			combobox2_.removeAllItems();
			String[] array = getArray();
			for (int i = 0; i < array.length; i++)
				combobox2_.addItem(array[i]);
		}

		// checkbox1 event
		else if (event.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected())

				// set button3 disabled
				button3_.setEnabled(true);

			// not selected
			else if (checkbox1_.isSelected() == false)

				// set button3 disabled
				button3_.setEnabled(false);
		}
	}

	/**
	 * Returns component array depending on the choices.
	 * 
	 * @return Component array.
	 */
	private String[] getArray() {

		// initialize array
		String array[] = null;

		// get selected indexes
		int type = combobox1_.getSelectedIndex();

		// displacements
		if (type == 0) {
			array = new String[8];
			array[0] = "Displacement 1";
			array[1] = "Displacement 2";
			array[2] = "Displacement 3";
			array[3] = "Rotation 1";
			array[4] = "Rotation 2";
			array[5] = "Rotation 3";
			array[6] = "Resultant displacement";
			array[7] = "Resultant rotation";
		}

		// elastic strains
		else if (type == 1) {
			array = new String[11];
			array[0] = "Normal strain 11";
			array[1] = "Normal strain 22";
			array[2] = "Normal strain 33";
			array[3] = "Shear strain 12";
			array[4] = "Shear strain 13";
			array[5] = "Shear strain 23";
			array[6] = "Resultant normal strain";
			array[7] = "Resultant shear strain";
			array[8] = "Min principal strain";
			array[9] = "Mid principal strain";
			array[10] = "Max principal strain";
		}

		// stresses
		else if (type == 2) {
			array = new String[12];
			array[0] = "Normal stress 11";
			array[1] = "Normal stress 22";
			array[2] = "Normal stress 33";
			array[3] = "Shear stress 12";
			array[4] = "Shear stress 13";
			array[5] = "Shear stress 23";
			array[6] = "Resultant normal stress";
			array[7] = "Resultant shear stress";
			array[8] = "Min principal stress";
			array[9] = "Mid principal stress";
			array[10] = "Max principal stress";
			array[11] = "Mises stress";
		}

		// return array
		return array;
	}
}
