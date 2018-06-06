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
package dialogs.analysis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.Commons;

import main.SolidMAT;

/**
 * Class for Analysis Options Analyze menu.
 * 
 * @author Murat
 * 
 */
public class AnalysisOptions1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_, checkbox4_,
			checkbox5_, checkbox6_;

	private JButton button1_, button2_, button3_, button4_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public AnalysisOptions1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Analysis Options", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Available DOFs", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Fast DOFs", Commons.gridbag_);

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("Translation X", true);
		checkbox2_ = new JCheckBox("Translation Y", true);
		checkbox3_ = new JCheckBox("Translation Z", true);
		checkbox4_ = new JCheckBox("Rotation about X", true);
		checkbox5_ = new JCheckBox("Rotation about Y", true);
		checkbox6_ = new JCheckBox("Rotation about Z", true);

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("    3D     ");
		button2_ = new JButton(" XZ Plane  ");
		button3_ = new JButton(" XY Plane  ");
		button4_ = new JButton("Space Truss");
		JButton button5 = new JButton("  OK  ");
		JButton button6 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, checkbox2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, checkbox3_, 2, 0, 1, 1);
		Commons.addComponent(panel3, checkbox4_, 0, 1, 1, 1);
		Commons.addComponent(panel3, checkbox5_, 1, 1, 1, 1);
		Commons.addComponent(panel3, checkbox6_, 2, 1, 1, 1);
		Commons.addComponent(panel4, button1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, button2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, button3_, 1, 0, 1, 1);
		Commons.addComponent(panel4, button4_, 1, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button5);
		panel2.add(button6);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);
		button5.addActionListener(this);
		button6.addActionListener(this);

		// call initialize
		initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get available dofs of the structure
		int[] dofs = owner_.structure_.getAvailableDofs();

		// set checkboxes
		if (dofs[0] == -1)
			checkbox1_.setSelected(false);
		if (dofs[1] == -1)
			checkbox2_.setSelected(false);
		if (dofs[2] == -1)
			checkbox3_.setSelected(false);
		if (dofs[3] == -1)
			checkbox4_.setSelected(false);
		if (dofs[4] == -1)
			checkbox5_.setSelected(false);
		if (dofs[5] == -1)
			checkbox6_.setSelected(false);
	}

	/**
	 * If ok is clicked calls actionOk, if cancel clicked sets dialog unvisible,
	 * if other buttons clicked sets appropriate checkboxes.
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

		// 3D button clicked
		else if (e.getSource().equals(button1_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(true);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(true);
			checkbox5_.setSelected(true);
			checkbox6_.setSelected(true);
		}

		// XZ button clicked
		else if (e.getSource().equals(button2_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(false);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(true);
			checkbox6_.setSelected(false);
		}

		// XY button clicked
		else if (e.getSource().equals(button3_)) {

			// set checkboxes
			checkbox1_.setSelected(false);
			checkbox2_.setSelected(false);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(true);
			checkbox5_.setSelected(true);
			checkbox6_.setSelected(false);
		}

		// space truss button clicked
		else if (e.getSource().equals(button4_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(true);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(false);
			checkbox6_.setSelected(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// create array for storing available dofs
		int[] dofs = new int[6];
		if (checkbox1_.isSelected() == false)
			dofs[0] = -1;
		if (checkbox2_.isSelected() == false)
			dofs[1] = -1;
		if (checkbox3_.isSelected() == false)
			dofs[2] = -1;
		if (checkbox4_.isSelected() == false)
			dofs[3] = -1;
		if (checkbox5_.isSelected() == false)
			dofs[4] = -1;
		if (checkbox6_.isSelected() == false)
			dofs[5] = -1;

		// set available dofs to structure
		owner_.structure_.setAvailableDofs(dofs);

		// set dialog unvisible
		setVisible(false);
	}
}
