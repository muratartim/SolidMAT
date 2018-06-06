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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;

/**
 * Class for Modify Damping in Analysis Cases menu.
 * 
 * @author Murat
 * 
 */
public class AnalysisCase3 extends JDialog implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_;

	private JRadioButton radiobutton1_, radiobutton2_;

	/** Mother panel of this dialog. */
	protected AnalysisCasePanel3 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Panel to be the owner of this dialog.
	 */
	public AnalysisCase3(AnalysisCasePanel3 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.owner_, "Modify Damping Parameters", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Damping Coefficients",
				Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Specification :");
		JLabel label2 = new JLabel("Mass factor :");
		JLabel label3 = new JLabel("Stiffness factor :");
		JLabel label4 = new JLabel("First :");
		JLabel label5 = new JLabel("Second :");
		JLabel label6 = new JLabel("      Frequency");
		JLabel label7 = new JLabel("       Damping");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield3_.setEnabled(false);
		textfield4_.setEnabled(false);
		textfield5_.setEnabled(false);
		textfield6_.setEnabled(false);

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Direct", true);
		radiobutton2_ = new JRadioButton("By frequency", false);
		radiobutton1_.setPreferredSize(new Dimension(89, 22));

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, label3, 2, 0, 1, 1);
		Commons.addComponent(panel3, label6, 3, 1, 1, 1);
		Commons.addComponent(panel3, label7, 3, 2, 1, 1);
		Commons.addComponent(panel3, label4, 4, 0, 1, 1);
		Commons.addComponent(panel3, label5, 5, 0, 1, 1);
		Commons.addComponent(panel3, radiobutton1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 0, 2, 1, 1);
		Commons.addComponent(panel3, textfield1_, 1, 1, 2, 1);
		Commons.addComponent(panel3, textfield2_, 2, 1, 2, 1);
		Commons.addComponent(panel3, textfield3_, 4, 1, 1, 1);
		Commons.addComponent(panel3, textfield4_, 4, 2, 1, 1);
		Commons.addComponent(panel3, textfield5_, 5, 1, 1, 1);
		Commons.addComponent(panel3, textfield6_, 5, 2, 1, 1);

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
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);

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

		// set textfields
		textfield1_.setText(owner_.owner_.owner_.owner_.formatter_
				.format(owner_.proporCoeff_[0]));
		textfield2_.setText(owner_.owner_.owner_.owner_.formatter_
				.format(owner_.proporCoeff_[1]));
		setDefaultText(textfield3_);
		setDefaultText(textfield4_);
		setDefaultText(textfield5_);
		setDefaultText(textfield6_);
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

		// direct specification
		else if (e.getSource().equals(radiobutton1_)) {

			// set textfield editable
			textfield1_.setEditable(true);
			textfield2_.setEditable(true);
			textfield3_.setEnabled(false);
			textfield4_.setEnabled(false);
			textfield5_.setEnabled(false);
			textfield6_.setEnabled(false);
		}

		// Group radio button clicked
		else if (e.getSource().equals(radiobutton2_)) {

			// set textfield editable
			textfield1_.setEditable(false);
			textfield2_.setEditable(false);
			textfield3_.setEnabled(true);
			textfield4_.setEnabled(true);
			textfield5_.setEnabled(true);
			textfield6_.setEnabled(true);
		}
	}

	/**
	 * fill constants array of mother panel and sets dialog unvisible.
	 */
	private void actionOk() {

		// fill constants array of mother panel
		owner_.proporCoeff_[0] = Double.parseDouble(textfield1_.getText());
		owner_.proporCoeff_[1] = Double.parseDouble(textfield2_.getText());

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source and dependently set message type
				JTextField textfield = (JTextField) e.getSource();
				if (textfield.isEditable()) {
					int messageType = 5;
					if (textfield.equals(textfield3_)
							|| textfield.equals(textfield5_))
						messageType = 4;
					else if (textfield.equals(textfield4_)
							|| textfield.equals(textfield6_))
						messageType = 6;

					// check textfield for ivalid values
					if (owner_.owner_.checkText(textfield, messageType) == false) {
						setDefaultText(textfield);
					}

					// compute proportional factors
					if (textfield.equals(textfield3_)
							|| textfield.equals(textfield4_)
							|| textfield.equals(textfield5_)
							|| textfield.equals(textfield6_))
						computeFactors();
				}
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * Computes proportional mass and stiffness factors.
	 */
	private void computeFactors() {

		// get frequency and damping values
		double w1 = Double.parseDouble(textfield3_.getText());
		double w2 = Double.parseDouble(textfield5_.getText());
		double e1 = Double.parseDouble(textfield4_.getText());
		double e2 = Double.parseDouble(textfield6_.getText());

		// compute proportional constants
		double beta = 2.0 * (w1 * e1 - w2 * e2) / (w1 * w1 - w2 * w2);
		double alpha = 2.0 * w1 * e1 - beta * w1 * w1;

		// set to textfields
		textfield1_.setText(owner_.owner_.owner_.owner_.formatter_
				.format(alpha));
		textfield2_
				.setText(owner_.owner_.owner_.owner_.formatter_.format(beta));
	}

	/**
	 * Sets default values for textfields.
	 * 
	 * @param textfield
	 *            The textfield to be set. If null is given, sets all
	 *            textfields.
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultValue1 = owner_.owner_.owner_.owner_.formatter_
				.format(0.0);
		String defaultValue2 = owner_.owner_.owner_.owner_.formatter_
				.format(1.0);
		String defaultValue3 = owner_.owner_.owner_.owner_.formatter_
				.format(10.0);

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultValue1);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue1);

		// set to textfield3
		else if (textfield.equals(textfield3_))
			textfield3_.setText(defaultValue2);

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultValue1);

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultValue3);

		// set to textfield6
		else if (textfield.equals(textfield6_))
			textfield6_.setText(defaultValue1);

		// set to all
		else {
			textfield1_.setText(defaultValue1);
			textfield2_.setText(defaultValue1);
			textfield3_.setText(defaultValue2);
			textfield4_.setText(defaultValue1);
			textfield5_.setText(defaultValue3);
			textfield6_.setText(defaultValue1);
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
