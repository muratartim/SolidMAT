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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import math.GraphPlot2D;
import matrix.DVec;
import node.Node;
import boundary.NodalMechLoad;
import analysis.Analysis;

/**
 * Class for Display Node History Plot menu.
 * 
 * @author Murat
 * 
 */
public class DisplayNodePlot1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JSpinner spinner1_, spinner2_;

	private JTextField textfield1_;

	private JComboBox combobox1_, combobox2_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton6_;

	/** Plotter of this dialog. */
	protected GraphPlot2D graph_ = new GraphPlot2D();

	private JButton button1_, button2_, button3_;

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
	public DisplayNodePlot1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Node History Plot", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Node and Options", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Stepping Info", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Result Options", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Component", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Node ID :");
		JLabel label2 = new JLabel("Initial step :");
		JLabel label3 = new JLabel("Final step :");
		JLabel label4 = new JLabel("Coordinate system :");
		JLabel label5 = new JLabel("Type :");

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
		SpinnerNumberModel spinnerModel2 = new SpinnerNumberModel();
		spinnerModel2.setMinimum(0);
		spinnerModel2.setMaximum(owner_.structure_.getNumberOfSteps() - 1);
		spinner1_ = new JSpinner(spinnerModel1);
		spinner2_ = new JSpinner(spinnerModel2);
		spinner1_.setPreferredSize(new Dimension(139, 20));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(80, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Options");
		button3_ = new JButton("Cancel");

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("UX", true);
		radiobutton2_ = new JRadioButton("UY", false);
		radiobutton3_ = new JRadioButton("UZ", false);
		radiobutton4_ = new JRadioButton("RX", false);
		radiobutton5_ = new JRadioButton("RY", false);
		radiobutton6_ = new JRadioButton("RZ", false);
		radiobutton1_.setPreferredSize(new Dimension(50, 23));
		radiobutton2_.setPreferredSize(new Dimension(50, 23));
		radiobutton3_.setPreferredSize(new Dimension(50, 23));

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);
		buttongroup1.add(radiobutton4_);
		buttongroup1.add(radiobutton5_);
		buttongroup1.add(radiobutton6_);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button2_, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, spinner1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, spinner2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, label4, 0, 0, 1, 1);
		Commons.addComponent(panel5, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label5, 1, 0, 1, 1);
		Commons.addComponent(panel5, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel6, radiobutton2_, 0, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton3_, 0, 2, 1, 1);
		Commons.addComponent(panel6, radiobutton4_, 1, 0, 1, 1);
		Commons.addComponent(panel6, radiobutton5_, 1, 1, 1, 1);
		Commons.addComponent(panel6, radiobutton6_, 1, 2, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		Commons.addComponent(panel1, panel6, 3, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button3_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
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

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					plotNode();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// options button clicked
		else if (e.getSource().equals(button2_)) {

			// create and set dialog visible
			DisplayNodePlot2 dialog = new DisplayNodePlot2(this);
			dialog.setVisible(true);
		}

		// cancel button clicked
		else if (e.getSource().equals(button3_)) {

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
			button3_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);
			button3_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void plotNode() {

		// check stepping numbers
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

				// get step numbers
				int step1 = (Integer) spinner1_.getValue();
				int step2 = (Integer) spinner2_.getValue();

				// get demanded coordinate system
				int coord = combobox1_.getSelectedIndex();

				// get demanded component
				int comp = 0;
				if (radiobutton1_.isSelected())
					comp = 0;
				else if (radiobutton2_.isSelected())
					comp = 1;
				else if (radiobutton3_.isSelected())
					comp = 2;
				else if (radiobutton4_.isSelected())
					comp = 3;
				else if (radiobutton5_.isSelected())
					comp = 4;
				else if (radiobutton6_.isSelected())
					comp = 5;

				// get analysis type
				int type = (Integer) owner_.structure_.getAnalysisInfo().get(1);

				// initialize x and y value vectors
				DVec xVal = new DVec(step2 - step1 + 1);
				DVec yVal = new DVec(step2 - step1 + 1);

				// set x and y labels
				String xLabel = getXLabel(type);
				String yLabel = getYLabel();

				// initialize vector index
				int i = 0;

				// loop over demanded steps
				progressor_.setStatusMessage("Forming graph data...");
				for (int j = step1; j <= step2; j++) {

					// set x values
					setXValues(type, xVal, i, j);

					// set y values
					setYValues(node, coord, comp, yVal, i, j);

					// renew result index
					i++;
				}

				// set x and y values to plotter
				graph_.setValues(xVal.get1DArray(), yVal.get1DArray());

				// set labels to plotter
				graph_.setLabels(xLabel, yLabel);

				// plot
				progressor_.setStatusMessage("Plotting...");
				graph_.plot(owner_.viewer_);

				// close progressor
				progressor_.close();

				// set dialog unvisible
				setVisible(false);
			}

			// node does not exist
			catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Given node does not exist!", "False data entry", 2);
			}
		}
	}

	/**
	 * Sets y values vector.
	 * 
	 * @param node
	 *            The node subjected for computation.
	 * @param coord
	 *            Demanded coordinate system.
	 * @param comp
	 *            The selected component of the result.
	 * @param yVal
	 *            Y values vector to be set.
	 * @param i
	 *            Index for setting the value.
	 * @param j
	 *            Index for the step number.
	 */
	private void setYValues(Node node, int coord, int comp, DVec yVal, int i,
			int j) {

		// set step number to node
		owner_.structure_.setStepToNode(owner_.path_, node, j);

		// displacements
		if (combobox2_.getSelectedIndex() == 0)
			yVal.set(i, node.getUnknown(coord).get(comp));

		// reaction forces
		else if (combobox2_.getSelectedIndex() == 1)
			yVal.set(i, node.getReactionForce(coord).get(comp));
	}

	/**
	 * Sets x values vector depending on the analysis type.
	 * 
	 * @param type
	 *            The type of analysis.
	 * @param xVal
	 *            X values vector to be set.
	 * @param i
	 *            Index for setting the value.
	 * @param j
	 *            Index for the step number.
	 */
	private void setXValues(int type, DVec xVal, int i, int j) {

		// linear dynamic analysis
		if (type == Analysis.linearTransient_) {

			// get time step size
			double dt = (Double) owner_.structure_.getAnalysisInfo().get(6);

			// set value
			xVal.set(i, j * dt + dt);
		}

		// modal analysis
		else if (type == Analysis.modal_) {

			// set value
			xVal.set(i, j);
		}
	}

	/**
	 * Returns label for the x values vector depending on the analysis type.
	 * 
	 * @param type
	 *            The type of analysis.
	 * @return The label for the x values vector.
	 */
	private String getXLabel(int type) {

		// linear dynamic analysis
		if (type == Analysis.linearTransient_)
			return "Time";

		// modal analysis
		else if (type == Analysis.modal_)
			return "Mode";
		return null;
	}

	/**
	 * Returns label for the y values vector.
	 * 
	 * @return Label for the y values vector.
	 */
	private String getYLabel() {

		// get text of selected radiobutton
		if (radiobutton1_.isSelected())
			return radiobutton1_.getText();
		else if (radiobutton2_.isSelected())
			return radiobutton2_.getText();
		else if (radiobutton3_.isSelected())
			return radiobutton3_.getText();
		else if (radiobutton4_.isSelected())
			return radiobutton4_.getText();
		else if (radiobutton5_.isSelected())
			return radiobutton5_.getText();
		else if (radiobutton6_.isSelected())
			return radiobutton6_.getText();
		return null;
	}

	/**
	 * Checks entered textfields.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkText() {

		// set error message
		String message = "Illegal values for step numbers!";

		// check for non-numeric or negative values
		try {

			// convert texts to integer values
			int val1 = (Integer) spinner1_.getValue();
			int val2 = (Integer) spinner2_.getValue();

			// check if negative
			if (val1 < 0 || val2 <= 0) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message,
						"False data entry", 2);
				return false;
			}

			// check mutual suitability
			if (val2 <= val1) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message,
						"False data entry", 2);
				return false;
			}

			// check if they are out of limit
			int limit = owner_.structure_.getNumberOfSteps();
			if (val1 >= limit || val2 >= limit) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, message,
						"False data entry", 2);
				return false;
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, message, "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
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
				label = radiobutton1_.getText().replace('1', 'X');
				radiobutton1_.setText(label);
				label = radiobutton2_.getText().replace('2', 'Y');
				radiobutton2_.setText(label);
				label = radiobutton3_.getText().replace('3', 'Z');
				radiobutton3_.setText(label);
				label = radiobutton4_.getText().replace('1', 'X');
				radiobutton4_.setText(label);
				label = radiobutton5_.getText().replace('2', 'Y');
				radiobutton5_.setText(label);
				label = radiobutton6_.getText().replace('3', 'Z');
				radiobutton6_.setText(label);
			}

			// for the "Local" option
			else if (index == NodalMechLoad.local_) {
				label = radiobutton1_.getText().replace('X', '1');
				radiobutton1_.setText(label);
				label = radiobutton2_.getText().replace('Y', '2');
				radiobutton2_.setText(label);
				label = radiobutton3_.getText().replace('Z', '3');
				radiobutton3_.setText(label);
				label = radiobutton4_.getText().replace('X', '1');
				radiobutton4_.setText(label);
				label = radiobutton5_.getText().replace('Y', '2');
				radiobutton5_.setText(label);
				label = radiobutton6_.getText().replace('Z', '3');
				radiobutton6_.setText(label);
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
				label = radiobutton1_.getText().replace('F', 'U');
				radiobutton1_.setText(label);
				label = radiobutton2_.getText().replace('F', 'U');
				radiobutton2_.setText(label);
				label = radiobutton3_.getText().replace('F', 'U');
				radiobutton3_.setText(label);
				label = radiobutton4_.getText().replace('M', 'R');
				radiobutton4_.setText(label);
				label = radiobutton5_.getText().replace('M', 'R');
				radiobutton5_.setText(label);
				label = radiobutton6_.getText().replace('M', 'R');
				radiobutton6_.setText(label);
			}

			// for the "Reaction forces" option
			else if (index == 1) {
				label = radiobutton1_.getText().replace('U', 'F');
				radiobutton1_.setText(label);
				label = radiobutton2_.getText().replace('U', 'F');
				radiobutton2_.setText(label);
				label = radiobutton3_.getText().replace('U', 'F');
				radiobutton3_.setText(label);
				label = radiobutton4_.getText().replace('R', 'M');
				radiobutton4_.setText(label);
				label = radiobutton5_.getText().replace('R', 'M');
				radiobutton5_.setText(label);
				label = radiobutton6_.getText().replace('R', 'M');
				radiobutton6_.setText(label);
			}
		}
	}
}
