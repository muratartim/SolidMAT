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
package dialogs.library;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import analysis.LinearTransient;
import main.Commons;
// import main.ImageHandler;

/**
 * Class for Modify Time integration in Analysis Cases menu.
 * 
 * @author Murat
 * 
 */
public class AnalysisCase4 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_;

	private JRadioButton radiobutton1_, radiobutton2_;

	private JLabel label2_, label3_;

	/** Mother panel of this dialog. */
	private AnalysisCasePanel3 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Panel to be the owner of this dialog.
	 */
	public AnalysisCase4(AnalysisCasePanel3 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.owner_, "Integration Parameters", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Method and Parameters",
				Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Method :");
		label2_ = new JLabel("Alpha :");
		label3_ = new JLabel("Delta :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Newmark", true);
		radiobutton2_ = new JRadioButton("Wilson", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, label3_, 2, 0, 1, 1);
		Commons.addComponent(panel3, radiobutton1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 0, 2, 1, 1);
		Commons.addComponent(panel3, textfield1_, 1, 1, 2, 1);
		Commons.addComponent(panel3, textfield2_, 2, 1, 2, 1);

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
		radiobutton1_.addActionListener(this);
		radiobutton2_.addActionListener(this);

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

		// Newmark method selected
		if (owner_.method_ == LinearTransient.newmark_) {

			// set radiobutton and text options
			radiobutton1_.setSelected(true);
			textfield2_.setEnabled(true);
			label2_.setText("Alpha :");
			label3_.setVisible(true);

			// set texts
			textfield1_.setText(owner_.owner_.owner_.owner_.formatter_
					.format(owner_.integrationPar_[0]));
			textfield2_.setText(owner_.owner_.owner_.owner_.formatter_
					.format(owner_.integrationPar_[1]));
		}

		// Wilson method selected
		else if (owner_.method_ == LinearTransient.wilson_) {

			// set radiobutton and text options
			radiobutton2_.setSelected(true);
			textfield2_.setEnabled(false);
			label2_.setText("Theta :");
			label3_.setVisible(false);

			// set texts
			textfield1_.setText(owner_.owner_.owner_.owner_.formatter_
					.format(owner_.integrationPar_[0]));
		}
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

		// Newmark method
		else if (e.getSource().equals(radiobutton1_)) {

			// set textfield enabled
			textfield2_.setEnabled(true);
			label2_.setText("Alpha :");
			label3_.setVisible(true);

			// set default values
			textfield1_.setText("0.250");
			textfield2_.setText("0.500");
		}

		// Wilson method
		else if (e.getSource().equals(radiobutton2_)) {

			// set textfield enabled
			textfield2_.setEnabled(false);
			label2_.setText("Theta :");
			label3_.setVisible(false);

			// set default values
			textfield1_.setText("1.400");
			textfield2_.setText("");
		}
	}

	/**
	 * fill constants array of mother panel and sets dialog unvisible.
	 */
	private void actionOk() {

		// check textfields
		if (checkTexts()) {

			// set integration method of mother panel and set parameters
			if (radiobutton1_.isSelected()) {
				owner_.method_ = LinearTransient.newmark_;
				owner_.textfield5_.setText("Newmark");
				owner_.integrationPar_[0] = Double.parseDouble(textfield1_
						.getText());
				owner_.integrationPar_[1] = Double.parseDouble(textfield2_
						.getText());
			} else if (radiobutton2_.isSelected()) {
				owner_.method_ = LinearTransient.wilson_;
				owner_.textfield5_.setText("Wilson");
				owner_.integrationPar_[0] = Double.parseDouble(textfield1_
						.getText());
			}

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkTexts() {

		// Newmark method
		if (radiobutton1_.isSelected()) {

			// textfield2
			try {

				// convert text to double value
				double value = Double.parseDouble(textfield2_.getText());

				// check for < 0.5
				if (value < 0.5) {

					// display message
					JOptionPane.showMessageDialog(this, "Illegal value!",
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(this, "Illegal value!",
						"False data entry", 2);
				return false;
			}

			// textfield1
			try {

				// convert text to double value
				double value = Double.parseDouble(textfield1_.getText());
				double value1 = Double.parseDouble(textfield2_.getText());

				// check for < 0.25*(0.5+ro)^2
				if (value < 0.25 * Math.pow(0.5 + value1, 2.0)) {

					// display message
					JOptionPane.showMessageDialog(this, "Illegal value!",
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(this, "Illegal value!",
						"False data entry", 2);
				return false;
			}
		}

		// Wilson method
		else if (radiobutton2_.isSelected()) {

			// textfield1
			try {

				// convert text to double value
				double value = Double.parseDouble(textfield1_.getText());

				// check for < 1.37
				if (value < 1.37) {

					// display message
					JOptionPane.showMessageDialog(this, "Illegal value!",
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(this, "Illegal value!",
						"False data entry", 2);
				return false;
			}
		}

		// data is correct
		return true;
	}
}
