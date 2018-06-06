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

import javax.swing.BoxLayout;
import javax.swing.Box;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import material.*;
import main.Commons;
// import main.ImageHandler;

/**
 * Class for Add/Modify Materials menu.
 * 
 * @author Murat
 * 
 */
public class Material2 extends JDialog implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_, textfield11_, textfield12_, textfield13_,
			textfield14_, textfield15_, textfield16_, textfield17_,
			textfield18_, textfield19_, textfield20_, textfield21_,
			textfield22_;

	private JTabbedPane tabbedpane1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected Material1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 * @param add
	 *            Used for determining if add or modify button clicked from
	 *            mother dialog.
	 */
	public Material2(Material1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel3 = new JPanel();

		// build sub-panels
		JPanel panel4 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Properties", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Properties", Commons.gridbag_);
		JPanel panel8 = new JPanel();
		panel8.setLayout(new BoxLayout(panel8, BoxLayout.Y_AXIS));

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Mass per unit volume :");
		JLabel label3 = new JLabel("Weight per unit volume :");
		JLabel label4 = new JLabel("Modulus of elasticity :");
		JLabel label5 = new JLabel("Poisson's ratio :");
		JLabel label6 = new JLabel("Coeff. of thermal expansion :");
		JLabel label7 = new JLabel("Shear modulus :");
		JLabel label8 = new JLabel("Name :");
		JLabel label9 = new JLabel("Mass per unit volume :");
		JLabel label10 = new JLabel("Weight per unit volume :");
		JLabel label11 = new JLabel("Modulus of elasticity 1 :");
		JLabel label12 = new JLabel("Modulus of elasticity 2 :");
		JLabel label13 = new JLabel("Modulus of elasticity 3 :");
		JLabel label14 = new JLabel("Poisson's ratio 1-2 :");
		JLabel label15 = new JLabel("Poisson's ratio 1-3 :");
		JLabel label16 = new JLabel("Poisson's ratio 2-3 :");
		JLabel label17 = new JLabel("Coeff. of thermal expansion 1 :");
		JLabel label18 = new JLabel("Coeff. of thermal expansion 2 :");
		JLabel label19 = new JLabel("Coeff. of thermal expansion 3 :");
		JLabel label20 = new JLabel("Shear modulus 1-2 :");
		JLabel label21 = new JLabel("Shear modulus 1-3 :");
		JLabel label22 = new JLabel("Shear modulus 2-3 :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield8_ = new JTextField();
		textfield9_ = new JTextField();
		textfield10_ = new JTextField();
		textfield11_ = new JTextField();
		textfield12_ = new JTextField();
		textfield13_ = new JTextField();
		textfield14_ = new JTextField();
		textfield15_ = new JTextField();
		textfield16_ = new JTextField();
		textfield17_ = new JTextField();
		textfield18_ = new JTextField();
		textfield19_ = new JTextField();
		textfield20_ = new JTextField();
		textfield21_ = new JTextField();
		textfield22_ = new JTextField();
		textfield7_.setEditable(false);
		textfield19_.setPreferredSize(new Dimension(65, 20));
		textfield8_.setPreferredSize(new Dimension(182, 20));
		textfield1_.setPreferredSize(new Dimension(182, 20));
		textfield2_.setPreferredSize(new Dimension(74, 20));

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel4, label1, 0, 0, 1, 1);
		Commons.addComponent(panel5, label2, 0, 0, 1, 1);
		Commons.addComponent(panel5, label3, 1, 0, 1, 1);
		Commons.addComponent(panel5, label4, 2, 0, 1, 1);
		Commons.addComponent(panel5, label5, 3, 0, 1, 1);
		Commons.addComponent(panel5, label6, 4, 0, 1, 1);
		Commons.addComponent(panel5, label7, 5, 0, 1, 1);
		Commons.addComponent(panel6, label8, 0, 0, 1, 1);
		Commons.addComponent(panel7, label9, 0, 0, 1, 1);
		Commons.addComponent(panel7, label10, 1, 0, 1, 1);
		Commons.addComponent(panel7, label11, 2, 0, 1, 1);
		Commons.addComponent(panel7, label12, 3, 0, 1, 1);
		Commons.addComponent(panel7, label13, 4, 0, 1, 1);
		Commons.addComponent(panel7, label14, 5, 0, 1, 1);
		Commons.addComponent(panel7, label15, 6, 0, 1, 1);
		Commons.addComponent(panel7, label16, 7, 0, 1, 1);
		Commons.addComponent(panel7, label17, 8, 0, 1, 1);
		Commons.addComponent(panel7, label18, 9, 0, 1, 1);
		Commons.addComponent(panel7, label19, 10, 0, 1, 1);
		Commons.addComponent(panel7, label20, 11, 0, 1, 1);
		Commons.addComponent(panel7, label21, 12, 0, 1, 1);
		Commons.addComponent(panel7, label22, 13, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel5, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel5, textfield5_, 3, 1, 1, 1);
		Commons.addComponent(panel5, textfield6_, 4, 1, 1, 1);
		Commons.addComponent(panel5, textfield7_, 5, 1, 1, 1);
		Commons.addComponent(panel6, textfield8_, 0, 1, 1, 1);
		Commons.addComponent(panel7, textfield9_, 0, 1, 1, 1);
		Commons.addComponent(panel7, textfield10_, 1, 1, 1, 1);
		Commons.addComponent(panel7, textfield11_, 2, 1, 1, 1);
		Commons.addComponent(panel7, textfield12_, 3, 1, 1, 1);
		Commons.addComponent(panel7, textfield13_, 4, 1, 1, 1);
		Commons.addComponent(panel7, textfield14_, 5, 1, 1, 1);
		Commons.addComponent(panel7, textfield15_, 6, 1, 1, 1);
		Commons.addComponent(panel7, textfield16_, 7, 1, 1, 1);
		Commons.addComponent(panel7, textfield17_, 8, 1, 1, 1);
		Commons.addComponent(panel7, textfield18_, 9, 1, 1, 1);
		Commons.addComponent(panel7, textfield19_, 10, 1, 1, 1);
		Commons.addComponent(panel7, textfield20_, 11, 1, 1, 1);
		Commons.addComponent(panel7, textfield21_, 12, 1, 1, 1);
		Commons.addComponent(panel7, textfield22_, 13, 1, 1, 1);
		panel8.add(Box.createRigidArea(new Dimension(0, 230)));

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel4, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel8, 2, 0, 1, 1);
		Commons.addComponent(panel2, panel6, 0, 0, 1, 1);
		Commons.addComponent(panel2, panel7, 1, 0, 1, 1);
		panel3.add(button1);
		panel3.add(button2);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Isotropic", panel1);
		tabbedpane1_.addTab("Orthotropic", panel2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", tabbedpane1_);
		getContentPane().add("South", panel3);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield7_.addFocusListener(this);
		textfield8_.addFocusListener(this);
		textfield9_.addFocusListener(this);
		textfield10_.addFocusListener(this);
		textfield11_.addFocusListener(this);
		textfield12_.addFocusListener(this);
		textfield13_.addFocusListener(this);
		textfield14_.addFocusListener(this);
		textfield15_.addFocusListener(this);
		textfield16_.addFocusListener(this);
		textfield17_.addFocusListener(this);
		textfield18_.addFocusListener(this);
		textfield19_.addFocusListener(this);
		textfield20_.addFocusListener(this);
		textfield21_.addFocusListener(this);
		textfield22_.addFocusListener(this);

		// If add is clicked set default, if not initialize
		if (add_)
			setDefaultText(new JTextField());
		else
			initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get index of selected item in list of mother dialog
		int index = owner_.list1_.getSelectedIndex();

		// get the selected object
		Material selected = owner_.temporary_.get(index);

		// get name, type, mass/volume, weight/volume
		String name = selected.getName();
		int type = selected.getType();
		double m = selected.getVolumeMass();
		double w = selected.getVolumeWeight();

		// set tab
		tabbedpane1_.setSelectedIndex(type);

		// for isotropic
		if (type == Material.isotropic_) {

			// get isotropic material
			Isotropic mat = (Isotropic) selected;

			// set name
			textfield1_.setText(name);

			// set values
			textfield2_.setText(owner_.owner_.formatter_.format(m));
			textfield3_.setText(owner_.owner_.formatter_.format(w));
			textfield4_.setText(owner_.owner_.formatter_.format(mat
					.getElasticModulus()));
			textfield5_.setText(owner_.owner_.formatter_.format(mat
					.getPoisson()));
			textfield6_.setText(owner_.owner_.formatter_.format(mat
					.getThermalExpansion()));
			textfield7_.setText(owner_.owner_.formatter_.format(mat
					.getShearModulus()));

			// set default for others
			setDefaultText(textfield8_);
			setDefaultText(textfield9_);
			setDefaultText(textfield10_);
			setDefaultText(textfield11_);
			setDefaultText(textfield12_);
			setDefaultText(textfield13_);
			setDefaultText(textfield14_);
			setDefaultText(textfield15_);
			setDefaultText(textfield16_);
			setDefaultText(textfield17_);
			setDefaultText(textfield18_);
			setDefaultText(textfield19_);
			setDefaultText(textfield20_);
			setDefaultText(textfield21_);
			setDefaultText(textfield22_);
		}

		// for orthotropic
		else if (type == Material.orthotropic_) {

			// get orthotropic material
			Orthotropic mat = (Orthotropic) selected;

			// set name
			textfield8_.setText(name);

			// set values
			textfield9_.setText(owner_.owner_.formatter_.format(m));
			textfield10_.setText(owner_.owner_.formatter_.format(w));
			textfield11_.setText(owner_.owner_.formatter_.format(mat
					.getElasticModulus()[0]));
			textfield12_.setText(owner_.owner_.formatter_.format(mat
					.getElasticModulus()[1]));
			textfield13_.setText(owner_.owner_.formatter_.format(mat
					.getElasticModulus()[2]));
			textfield14_.setText(owner_.owner_.formatter_.format(mat
					.getPoisson()[0]));
			textfield15_.setText(owner_.owner_.formatter_.format(mat
					.getPoisson()[1]));
			textfield16_.setText(owner_.owner_.formatter_.format(mat
					.getPoisson()[2]));
			textfield17_.setText(owner_.owner_.formatter_.format(mat
					.getThermalExpansion()[0]));
			textfield18_.setText(owner_.owner_.formatter_.format(mat
					.getThermalExpansion()[1]));
			textfield19_.setText(owner_.owner_.formatter_.format(mat
					.getThermalExpansion()[2]));
			textfield20_.setText(owner_.owner_.formatter_.format(mat
					.getShearModulus()[0]));
			textfield21_.setText(owner_.owner_.formatter_.format(mat
					.getShearModulus()[1]));
			textfield22_.setText(owner_.owner_.formatter_.format(mat
					.getShearModulus()[2]));

			// set default for others
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
			setDefaultText(textfield7_);
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
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// get type
		int type = tabbedpane1_.getSelectedIndex();

		// get name textfield to be checked
		JTextField textfield = new JTextField();
		if (type == Material.isotropic_)
			textfield = textfield1_;
		else if (type == Material.orthotropic_)
			textfield = textfield8_;

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield, 1)) {
				actionOkAddModify(type);
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield.getText().equals(selected)) {
				actionOkAddModify(type);
				setVisible(false);
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield, 1)) {
					actionOkAddModify(type);
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 * @param type
	 *            The type of object.
	 */
	private void actionOkAddModify(int type) {

		// initialize input object
		Material object = null;
		String name = null;

		// for isotropic
		if (type == Material.isotropic_) {

			// get values
			name = textfield1_.getText();
			double m = Double.parseDouble(textfield2_.getText());
			double w = Double.parseDouble(textfield3_.getText());
			double e = Double.parseDouble(textfield4_.getText());
			double nu = Double.parseDouble(textfield5_.getText());
			double t = Double.parseDouble(textfield6_.getText());

			// set object
			Isotropic mat = new Isotropic(name, e, nu);
			mat.setThermalExpansion(t);
			mat.setVolumeMass(m);
			mat.setVolumeWeight(w);
			object = mat;
		}

		// for orthotropic
		else if (type == Material.orthotropic_) {

			// get values
			name = textfield8_.getText();
			double m = Double.parseDouble(textfield9_.getText());
			double w = Double.parseDouble(textfield10_.getText());
			double[] e = new double[3];
			e[0] = Double.parseDouble(textfield11_.getText());
			e[1] = Double.parseDouble(textfield12_.getText());
			e[2] = Double.parseDouble(textfield13_.getText());
			double[] nu = new double[3];
			nu[0] = Double.parseDouble(textfield14_.getText());
			nu[1] = Double.parseDouble(textfield15_.getText());
			nu[2] = Double.parseDouble(textfield16_.getText());
			double[] t = new double[3];
			t[0] = Double.parseDouble(textfield17_.getText());
			t[1] = Double.parseDouble(textfield18_.getText());
			t[2] = Double.parseDouble(textfield19_.getText());
			double[] sm = new double[3];
			sm[0] = Double.parseDouble(textfield20_.getText());
			sm[1] = Double.parseDouble(textfield21_.getText());
			sm[2] = Double.parseDouble(textfield22_.getText());

			// set object
			Orthotropic mat = new Orthotropic(name, e, nu, sm);
			mat.setThermalExpansion(t);
			mat.setVolumeMass(m);
			mat.setVolumeWeight(w);
			object = mat;
		}

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(name);
		}

		// modify button clicked
		else if (add_ == false) {

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(name, index);
		}
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source and dependently set message type
				JTextField tfield = (JTextField) e.getSource();
				int messageType = 2;
				if (tfield.equals(textfield1_) || tfield.equals(textfield8_))
					messageType = 0;
				if (tfield.equals(textfield5_))
					messageType = 3;

				// check textfield
				if (checkText(tfield, messageType) == false)
					setDefaultText(tfield);

				// compute shear modulus
				if (tfield.equals(textfield4_) || tfield.equals(textfield5_)) {
					double ym = Double.parseDouble(textfield4_.getText());
					double nu = Double.parseDouble(textfield5_.getText());
					double g = ym / (2.0 * (nu + 1.0));
					textfield7_.setText(owner_.owner_.formatter_.format(g));
				}
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield
	 *            The textfield that the false data has been entered.
	 * @param messageType
	 *            The type of message to be displayed (No name given -> 0, Name
	 *            exists -> 1, Illegal value -> 2, Illegal value (poisson's
	 *            ratio) -> 3).
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkText(JTextField textfield, int messageType) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield.getText();

		// No name given
		if (messageType == 0) {

			// check if no name given
			if (text.equals("")) {

				// display message
				JOptionPane.showMessageDialog(Material2.this, "No name given!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(Material2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value
		else if (messageType == 2) {

			// check for non-numeric, negative and zero values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check if its negative or zero
				if (value <= 0) {

					// display message
					JOptionPane.showMessageDialog(Material2.this,
							"Illegal value!", "False data entry", 2);
					isCorrect = false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(Material2.this, "Illegal value!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value (poisson's ratio)
		else if (messageType == 3) {

			// check for non-numeric, negative and zero values
			try {

				// convert text to double value
				double value = Double.parseDouble(text);

				// check if its negative, zero or larger than 0.5
				if (value <= 0 || value >= 0.5) {

					// display message
					JOptionPane.showMessageDialog(Material2.this,
							"Illegal value!", "False data entry", 2);
					isCorrect = false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(Material2.this, "Illegal value!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// the data is correct
		return isCorrect;
	}

	/**
	 * Sets default values for textfields.
	 * 
	 * @param textfield
	 *            The textfield to be set. If null is given, sets all
	 *            textfields.
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields (KN-m-C)
		String defaultName = "Material1";
		double mass = 7.849;
		double weight = 76.9729;
		double[] e = { 2.00E+08, 2.00E+08, 2.00E+08 };
		double[] nu = { 0.3, 0.3, 0.3 };
		double[] t = { 1.17E-05, 1.17E-05, 1.17E-05 };
		double[] g = { 76923077.0, 76923077.0, 76923077.0 };

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(owner_.owner_.formatter_.format(mass));

		// set to textfield3
		else if (textfield.equals(textfield3_))
			textfield3_.setText(owner_.owner_.formatter_.format(weight));

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(owner_.owner_.formatter_.format(e[0]));

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(owner_.owner_.formatter_.format(nu[0]));

		// set to textfield6
		else if (textfield.equals(textfield6_))
			textfield6_.setText(owner_.owner_.formatter_.format(t[0]));

		// set to textfield7
		else if (textfield.equals(textfield7_))
			textfield7_.setText(owner_.owner_.formatter_.format(g[0]));

		// set to textfield8
		else if (textfield.equals(textfield8_))
			textfield8_.setText(defaultName);

		// set to textfield9
		else if (textfield.equals(textfield9_))
			textfield9_.setText(owner_.owner_.formatter_.format(mass));

		// set to textfield10
		else if (textfield.equals(textfield10_))
			textfield10_.setText(owner_.owner_.formatter_.format(weight));

		// set to textfield11
		else if (textfield.equals(textfield11_))
			textfield11_.setText(owner_.owner_.formatter_.format(e[0]));

		// set to textfield12
		else if (textfield.equals(textfield12_))
			textfield12_.setText(owner_.owner_.formatter_.format(e[1]));

		// set to textfield13
		else if (textfield.equals(textfield13_))
			textfield13_.setText(owner_.owner_.formatter_.format(e[2]));

		// set to textfield14
		else if (textfield.equals(textfield14_))
			textfield14_.setText(owner_.owner_.formatter_.format(nu[0]));

		// set to textfield15
		else if (textfield.equals(textfield15_))
			textfield15_.setText(owner_.owner_.formatter_.format(nu[1]));

		// set to textfield16
		else if (textfield.equals(textfield16_))
			textfield16_.setText(owner_.owner_.formatter_.format(nu[2]));

		// set to textfield17
		else if (textfield.equals(textfield17_))
			textfield17_.setText(owner_.owner_.formatter_.format(t[0]));

		// set to textfield18
		else if (textfield.equals(textfield18_))
			textfield18_.setText(owner_.owner_.formatter_.format(t[1]));

		// set to textfield19
		else if (textfield.equals(textfield19_))
			textfield19_.setText(owner_.owner_.formatter_.format(t[2]));

		// set to textfield20
		else if (textfield.equals(textfield20_))
			textfield20_.setText(owner_.owner_.formatter_.format(g[0]));

		// set to textfield21
		else if (textfield.equals(textfield21_))
			textfield21_.setText(owner_.owner_.formatter_.format(g[1]));

		// set to textfield22
		else if (textfield.equals(textfield22_))
			textfield22_.setText(owner_.owner_.formatter_.format(g[2]));

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(owner_.owner_.formatter_.format(mass));
			textfield3_.setText(owner_.owner_.formatter_.format(weight));
			textfield4_.setText(owner_.owner_.formatter_.format(e[0]));
			textfield5_.setText(owner_.owner_.formatter_.format(nu[0]));
			textfield6_.setText(owner_.owner_.formatter_.format(t[0]));
			textfield7_.setText(owner_.owner_.formatter_.format(g[0]));
			textfield8_.setText(defaultName);
			textfield9_.setText(owner_.owner_.formatter_.format(mass));
			textfield10_.setText(owner_.owner_.formatter_.format(weight));
			textfield11_.setText(owner_.owner_.formatter_.format(e[0]));
			textfield12_.setText(owner_.owner_.formatter_.format(e[1]));
			textfield13_.setText(owner_.owner_.formatter_.format(e[2]));
			textfield14_.setText(owner_.owner_.formatter_.format(nu[0]));
			textfield15_.setText(owner_.owner_.formatter_.format(nu[1]));
			textfield16_.setText(owner_.owner_.formatter_.format(nu[2]));
			textfield17_.setText(owner_.owner_.formatter_.format(t[0]));
			textfield18_.setText(owner_.owner_.formatter_.format(t[1]));
			textfield19_.setText(owner_.owner_.formatter_.format(t[2]));
			textfield20_.setText(owner_.owner_.formatter_.format(g[0]));
			textfield21_.setText(owner_.owner_.formatter_.format(g[1]));
			textfield22_.setText(owner_.owner_.formatter_.format(g[2]));
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
