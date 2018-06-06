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
package dialogs.model;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;

import main.Commons;
// import main.ImageHandler;

/**
 * Class for Replicate Element Options Model menu.
 * 
 * @author Murat
 * 
 */
public class ReplicateElement2 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_, checkbox4_,
			checkbox5_, checkbox6_, checkbox7_;

	/** Mother dialog of this dialog. */
	private ReplicateElement1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public ReplicateElement2(ReplicateElement1 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Replicate Options", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Assignments", Commons.gridbag_);

		// build checkboxes
		checkbox1_ = new JCheckBox("Materials");
		checkbox2_ = new JCheckBox("Sections");
		checkbox3_ = new JCheckBox("Mechanical loads");
		checkbox4_ = new JCheckBox("Temperature loads");
		checkbox5_ = new JCheckBox("Springs");
		checkbox6_ = new JCheckBox("Masses");
		checkbox7_ = new JCheckBox("Local axes");

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, checkbox2_, 0, 1, 1, 1);
		Commons.addComponent(panel3, checkbox3_, 1, 0, 1, 1);
		Commons.addComponent(panel3, checkbox4_, 1, 1, 1, 1);
		Commons.addComponent(panel3, checkbox5_, 2, 0, 1, 1);
		Commons.addComponent(panel3, checkbox6_, 2, 1, 1, 1);
		Commons.addComponent(panel3, checkbox7_, 3, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button1);
		panel2.add(button2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);

		// initialize
		initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// set checkboxes
		checkbox1_.setSelected(owner_.options_[0]);
		checkbox2_.setSelected(owner_.options_[1]);
		checkbox3_.setSelected(owner_.options_[2]);
		checkbox4_.setSelected(owner_.options_[3]);
		checkbox5_.setSelected(owner_.options_[4]);
		checkbox6_.setSelected(owner_.options_[5]);
		checkbox7_.setSelected(owner_.options_[6]);
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

		// set checkbox selections to boolean array
		owner_.options_[0] = checkbox1_.isSelected();
		owner_.options_[1] = checkbox2_.isSelected();
		owner_.options_[2] = checkbox3_.isSelected();
		owner_.options_[3] = checkbox4_.isSelected();
		owner_.options_[4] = checkbox5_.isSelected();
		owner_.options_[5] = checkbox6_.isSelected();
		owner_.options_[6] = checkbox7_.isSelected();

		// set dialog unvisible
		setVisible(false);
	}
}
