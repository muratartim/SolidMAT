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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;

/**
 * Class for Display Element Assignments menu.
 * 
 * @author Murat
 * 
 */
public class DisplayElementMisc1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JComboBox combobox1_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton7_, radiobutton8_;

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
	public DisplayElementMisc1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Show Element Assignments", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Options", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Assignment Type", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Coordinate system :");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types[] = { "Global", "Local" };
		combobox1_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(2);
		combobox1_.setEnabled(false);
		combobox1_.setPreferredSize(new Dimension(100, 23));

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Materials", true);
		radiobutton2_ = new JRadioButton("Sections", false);
		radiobutton3_ = new JRadioButton("Springs", false);
		radiobutton4_ = new JRadioButton("Masses", false);
		radiobutton5_ = new JRadioButton("Local axes", false);
		radiobutton7_ = new JRadioButton("Types", false);
		radiobutton8_ = new JRadioButton("Groups", false);
		radiobutton1_.setPreferredSize(new Dimension(80, 23));
		radiobutton2_.setPreferredSize(new Dimension(80, 23));

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);
		buttongroup1.add(radiobutton4_);
		buttongroup1.add(radiobutton5_);
		buttongroup1.add(radiobutton7_);
		buttongroup1.add(radiobutton8_);

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, radiobutton2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton3_, 1, 0, 1, 1);
		Commons.addComponent(panel4, radiobutton4_, 1, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton5_, 2, 0, 1, 1);
		Commons.addComponent(panel4, radiobutton7_, 2, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton8_, 3, 0, 1, 1);

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
		radiobutton3_.addItemListener(this);
		radiobutton4_.addItemListener(this);
		radiobutton5_.addItemListener(this);
		radiobutton7_.addItemListener(this);
		radiobutton8_.addItemListener(this);

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

		// set coordinate system to visualizer
		owner_.preVis_.setCoordinateSystem(combobox1_.getSelectedIndex());

		// get element display options of visualizer
		boolean[] elementOpt = owner_.preVis_.getElementOptions();

		// set display options
		for (int i = 3; i < elementOpt.length; i++)
			elementOpt[i] = false;

		// disable colored options
		owner_.preVis_.disableColoredOptions();

		// set demanded options
		elementOpt[5] = radiobutton4_.isSelected();
		elementOpt[6] = radiobutton1_.isSelected();
		elementOpt[7] = radiobutton2_.isSelected();
		elementOpt[8] = radiobutton3_.isSelected();
		elementOpt[9] = radiobutton5_.isSelected();
		elementOpt[10] = radiobutton7_.isSelected();
		elementOpt[11] = radiobutton8_.isSelected();

		// draw
		progressor_.setStatusMessage("Drawing...");
		owner_.drawPre();

		// close progressor
		progressor_.close();

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 selected
		if (radiobutton1_.isSelected())
			combobox1_.setEnabled(false);

		// radiobutton2 selected
		else if (radiobutton2_.isSelected())
			combobox1_.setEnabled(false);

		// radiobutton3 selected
		else if (radiobutton3_.isSelected())
			combobox1_.setEnabled(true);

		// radiobutton4 selected
		else if (radiobutton4_.isSelected())
			combobox1_.setEnabled(true);

		// radiobutton5 selected
		else if (radiobutton5_.isSelected())
			combobox1_.setEnabled(false);

		// radiobutton7 selected
		else if (radiobutton7_.isSelected())
			combobox1_.setEnabled(false);

		// radiobutton8 selected
		else if (radiobutton8_.isSelected())
			combobox1_.setEnabled(false);
	}
}
