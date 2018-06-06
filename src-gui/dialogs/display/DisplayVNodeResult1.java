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
package dialogs.display;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.Dimension;

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

/**
 * Class for Display Visual Node Results menu.
 * 
 * @author Murat
 * 
 */
public class DisplayVNodeResult1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_;

	private JCheckBox checkbox1_;

	private JComboBox combobox1_, combobox2_, combobox3_;

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
	public DisplayVNodeResult1(SolidMAT owner) {

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
		JPanel panel3 = Commons.getPanel("Multivalued", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Step :");
		JLabel label2 = new JLabel("Type :");
		JLabel label3 = new JLabel("Coordinate system :");
		JLabel label4 = new JLabel("Component :");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Displacements", "Reaction forces" };
		String types2[] = { "Global", "Local" };
		String types3[] = { "Displacement x", "Displacement y",
				"Displacement z", "Rotation x", "Rotation y", "Rotation z",
				"Resultant displacement", "Resultant rotation" };
		combobox1_ = new JComboBox(types1);
		combobox2_ = new JComboBox(types2);
		combobox3_ = new JComboBox(types3);
		combobox1_.setMaximumRowCount(2);
		combobox2_.setMaximumRowCount(2);
		combobox3_.setMaximumRowCount(8);
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
		Commons.addComponent(panel4, label4, 2, 0, 1, 1);
		Commons.addComponent(panel4, combobox3_, 2, 1, 1, 1);
		Commons.addComponent(panel4, checkbox1_, 3, 0, 1, 1);
		Commons.addComponent(panel4, button3_, 3, 1, 1, 1);

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
		checkbox1_.addItemListener(this);
		combobox1_.addItemListener(this);
		combobox2_.addItemListener(this);

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

			// display progressor and still frame
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
			DisplayVNodeResult2 dialog = new DisplayVNodeResult2(this);
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
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// checkbox1 event
		if (event.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected())

				// set button3 disabled
				button3_.setEnabled(true);

			// not selected
			else if (checkbox1_.isSelected() == false)

				// set button3 disabled
				button3_.setEnabled(false);
		}

		// combobox1 / combobox2 event
		else if (event.getSource().equals(combobox1_)
				|| event.getSource().equals(combobox2_)) {

			// set component combobox
			combobox3_.removeAllItems();
			String[] array = getArray();
			for (int i = 0; i < array.length; i++)
				combobox3_.addItem(array[i]);
		}
	}

	/**
	 * Returns component array depending on the choices.
	 * 
	 * @return Component array.
	 */
	private String[] getArray() {

		// initialize array
		String array[] = new String[8];

		// get selected indexes
		int type = combobox1_.getSelectedIndex();
		int coor = combobox2_.getSelectedIndex();

		// displacements
		if (type == 0) {

			// global
			if (coor == 0) {
				array[0] = "Displacement x";
				array[1] = "Displacement y";
				array[2] = "Displacement z";
				array[3] = "Rotation x";
				array[4] = "Rotation y";
				array[5] = "Rotation z";
				array[6] = "Resultant displacement";
				array[7] = "Resultant rotation";
			}

			// global
			else if (coor == 1) {
				array[0] = "Displacement 1";
				array[1] = "Displacement 2";
				array[2] = "Displacement 3";
				array[3] = "Rotation 1";
				array[4] = "Rotation 2";
				array[5] = "Rotation 3";
				array[6] = "Resultant displacement";
				array[7] = "Resultant rotation";
			}
		}

		// reaction forces
		else if (type == 1) {

			// global
			if (coor == 0) {
				array[0] = "Reaction force x";
				array[1] = "Reaction force y";
				array[2] = "Reaction force z";
				array[3] = "Reaction moment x";
				array[4] = "Reaction moment y";
				array[5] = "Reaction moment z";
				array[6] = "Resultant reaction force";
				array[7] = "Resultant reaction moment";
			}

			// global
			else if (coor == 1) {
				array[0] = "Reaction force 1";
				array[1] = "Reaction force 2";
				array[2] = "Reaction force 3";
				array[3] = "Reaction moment 1";
				array[4] = "Reaction moment 2";
				array[5] = "Reaction moment 3";
				array[6] = "Resultant reaction force";
				array[7] = "Resultant reaction moment";
			}
		}

		// return array
		return array;
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

			// set step number to structure
			progressor_.setStatusMessage("Setting step number...");
			owner_.structure_.setStep(owner_.path_, step);

			// set result option
			int option = combobox1_.getSelectedIndex();

			// set component
			int[] comp = { combobox3_.getSelectedIndex(), 0 };

			// set coordinate system
			owner_.postVis_.setCoordinateSystem(combobox2_.getSelectedIndex());

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
						.showMessageDialog(DisplayVNodeResult1.this,
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
						.showMessageDialog(DisplayVNodeResult1.this,
								"Illegal value for step number!",
								"False data entry", 2);
				return false;
			}
		} catch (Exception excep) {

			// close progressor and enable dialog
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(DisplayVNodeResult1.this,
					"Illegal value for step number!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}
}
