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

// import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;

/**
 * Class for Display Prefernces Display menu.
 * 
 * @author Murat
 * 
 */
public class DisplayPreferences1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

	private JButton button1_, button2_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public DisplayPreferences1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Display Preferences", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Scaling Factors", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Radius of nodes :");
		JLabel label2 = new JLabel("Radius of lines :");
		JLabel label3 = new JLabel("Radius of arrows :");
		JLabel label4 = new JLabel("Length of arrows :");
		JLabel label5 = new JLabel("Text height :");
		JLabel label6 = new JLabel("Diagram height");
		JLabel label7 = new JLabel("Writing tolerance :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(100, 20));

		// build buttons and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, label3, 2, 0, 1, 1);
		Commons.addComponent(panel3, label4, 3, 0, 1, 1);
		Commons.addComponent(panel3, label5, 4, 0, 1, 1);
		Commons.addComponent(panel3, label6, 5, 0, 1, 1);
		Commons.addComponent(panel3, label7, 6, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel3, textfield3_, 2, 1, 1, 1);
		Commons.addComponent(panel3, textfield4_, 3, 1, 1, 1);
		Commons.addComponent(panel3, textfield5_, 4, 1, 1, 1);
		Commons.addComponent(panel3, textfield6_, 5, 1, 1, 1);
		Commons.addComponent(panel3, textfield7_, 6, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);

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

		// get scaling factors of pre/post visualizer
		double[] factors1 = owner_.preVis_.getScalingFactors();
		double[] factors2 = owner_.postVis_.getScalingFactors();

		// set textfields
		textfield1_.setText(owner_.formatter_.format(factors1[0]));
		textfield2_.setText(owner_.formatter_.format(factors1[1]));
		textfield3_.setText(owner_.formatter_.format(factors1[2]));
		textfield4_.setText(owner_.formatter_.format(factors1[3]));
		textfield5_.setText(owner_.formatter_.format(factors1[4]));
		textfield6_.setText(owner_.formatter_.format(factors2[2]));
		textfield7_.setText(owner_.formatter_.format(factors1[5]));
	}

	/**
	 * If ok is clicked calls actionOk, if cancel clicked sets dialog unvisible,
	 * if other buttons clicked sets appropriate checkboxes.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {
			if (checkTexts())
				actionOk();
		}

		// cancel button clicked
		else if (e.getSource().equals(button2_))
			setVisible(false);
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// initialize factors arrays
		double[] factors1 = new double[6];
		double[] factors2 = new double[3];

		// get textfields
		factors1[0] = Double.parseDouble(textfield1_.getText());
		factors2[0] = Double.parseDouble(textfield1_.getText());
		factors1[1] = Double.parseDouble(textfield2_.getText());
		factors2[1] = Double.parseDouble(textfield2_.getText());
		factors1[2] = Double.parseDouble(textfield3_.getText());
		factors1[3] = Double.parseDouble(textfield4_.getText());
		factors1[4] = Double.parseDouble(textfield5_.getText());
		factors2[2] = Double.parseDouble(textfield6_.getText());
		factors1[5] = Double.parseDouble(textfield7_.getText());

		// set to pre/post visualizers
		owner_.preVis_.setScalingFactors(factors1);
		owner_.postVis_.setScalingFactors(factors2);

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * Checks whether acceptable information has been entered to textfields.
	 * 
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkTexts() {

		// get the entered texts
		String[] texts = new String[7];
		texts[0] = textfield1_.getText();
		texts[1] = textfield2_.getText();
		texts[2] = textfield3_.getText();
		texts[3] = textfield4_.getText();
		texts[4] = textfield5_.getText();
		texts[5] = textfield6_.getText();
		texts[6] = textfield7_.getText();

		// check for non-numeric, negative and zero values
		try {

			// loop over texts
			for (int i = 0; i < texts.length; i++) {

				// convert text to double value
				double value = Double.parseDouble(texts[i]);

				// check if its negative or zero
				if (value <= 0) {

					// display message
					JOptionPane.showMessageDialog(this, "Illegal value!",
							"False data entry", 2);
					return false;
				}
			}
		}

		// illegal value entered
		catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(this, "Illegal value!",
					"False data entry", 2);
			return false;
		}

		// the data is correct
		return true;
	}
}
