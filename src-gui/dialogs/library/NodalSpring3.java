package dialogs.library;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import node.NodalSpring;

import main.Commons;
// import main.ImageHandler;
import matrix.DMat;

/**
 * Class for Coupled Springs menu.
 * 
 * @author Murat
 * 
 */
public class NodalSpring3 extends JDialog implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_, textfield11_, textfield12_, textfield13_,
			textfield14_, textfield15_, textfield16_, textfield17_,
			textfield18_, textfield19_, textfield20_, textfield21_,
			textfield22_, textfield23_, textfield24_, textfield25_,
			textfield26_, textfield27_, textfield28_, textfield29_,
			textfield30_, textfield31_, textfield32_, textfield33_,
			textfield34_, textfield35_, textfield36_;

	/** The mother dialog of this dialog. */
	protected NodalSpring2 owner_;

	/** The spring stiffness matrix. */
	private DMat stiffness_ = new DMat(6, 6);

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 */
	public NodalSpring3(NodalSpring2 owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Coupled Spring", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Upper Stiffness Matrix",
				Commons.gridbag_);

		// build labels
		String[] labels = { "        UX", "        UY", "        UZ",
				"        RX", "        RY", "        RZ" };
		if (owner_.combobox1_.getSelectedIndex() == NodalSpring.local_) {
			labels[0] = "        U1";
			labels[1] = "        U2";
			labels[2] = "        U3";
			labels[3] = "        R1";
			labels[4] = "        R2";
			labels[5] = "        R3";
		}
		JLabel label1 = new JLabel(labels[0]);
		JLabel label2 = new JLabel(labels[1]);
		JLabel label3 = new JLabel(labels[2]);
		JLabel label4 = new JLabel(labels[3]);
		JLabel label5 = new JLabel(labels[4]);
		JLabel label6 = new JLabel(labels[5]);
		JLabel label7 = new JLabel(labels[0]);
		JLabel label8 = new JLabel(labels[1]);
		JLabel label9 = new JLabel(labels[2]);
		JLabel label10 = new JLabel(labels[3]);
		JLabel label11 = new JLabel(labels[4]);
		JLabel label12 = new JLabel(labels[5]);

		// build text fields, set font and make them unedittable
		String[][] values = new String[6][6];
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				values[i][j] = owner_.owner_.owner_.formatter_
						.format(owner_.stiffness_.get(i, j));
				stiffness_.set(i, j, owner_.stiffness_.get(i, j));
			}
		}
		textfield1_ = new JTextField(values[0][0]);
		textfield2_ = new JTextField(values[0][1]);
		textfield3_ = new JTextField(values[0][2]);
		textfield4_ = new JTextField(values[0][3]);
		textfield5_ = new JTextField(values[0][4]);
		textfield6_ = new JTextField(values[0][5]);
		textfield7_ = new JTextField(values[1][0]);
		textfield8_ = new JTextField(values[1][1]);
		textfield9_ = new JTextField(values[1][2]);
		textfield10_ = new JTextField(values[1][3]);
		textfield11_ = new JTextField(values[1][4]);
		textfield12_ = new JTextField(values[1][5]);
		textfield13_ = new JTextField(values[2][0]);
		textfield14_ = new JTextField(values[2][1]);
		textfield15_ = new JTextField(values[2][2]);
		textfield16_ = new JTextField(values[2][3]);
		textfield17_ = new JTextField(values[2][4]);
		textfield18_ = new JTextField(values[2][5]);
		textfield19_ = new JTextField(values[3][0]);
		textfield20_ = new JTextField(values[3][1]);
		textfield21_ = new JTextField(values[3][2]);
		textfield22_ = new JTextField(values[3][3]);
		textfield23_ = new JTextField(values[3][4]);
		textfield24_ = new JTextField(values[3][5]);
		textfield25_ = new JTextField(values[4][0]);
		textfield26_ = new JTextField(values[4][1]);
		textfield27_ = new JTextField(values[4][2]);
		textfield28_ = new JTextField(values[4][3]);
		textfield29_ = new JTextField(values[4][4]);
		textfield30_ = new JTextField(values[4][5]);
		textfield31_ = new JTextField(values[5][0]);
		textfield32_ = new JTextField(values[5][1]);
		textfield33_ = new JTextField(values[5][2]);
		textfield34_ = new JTextField(values[5][3]);
		textfield35_ = new JTextField(values[5][4]);
		textfield36_ = new JTextField(values[5][5]);
		textfield7_.setEditable(false);
		textfield13_.setEditable(false);
		textfield14_.setEditable(false);
		textfield19_.setEditable(false);
		textfield20_.setEditable(false);
		textfield21_.setEditable(false);
		textfield25_.setEditable(false);
		textfield26_.setEditable(false);
		textfield27_.setEditable(false);
		textfield28_.setEditable(false);
		textfield31_.setEditable(false);
		textfield32_.setEditable(false);
		textfield33_.setEditable(false);
		textfield34_.setEditable(false);
		textfield35_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(60, 20));
		textfield2_.setPreferredSize(new Dimension(60, 20));
		textfield3_.setPreferredSize(new Dimension(60, 20));
		textfield4_.setPreferredSize(new Dimension(60, 20));
		textfield5_.setPreferredSize(new Dimension(60, 20));
		textfield6_.setPreferredSize(new Dimension(60, 20));

		// build buttons and give font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 1, 1, 1);
		Commons.addComponent(panel3, label2, 0, 2, 1, 1);
		Commons.addComponent(panel3, label3, 0, 3, 1, 1);
		Commons.addComponent(panel3, label4, 0, 4, 1, 1);
		Commons.addComponent(panel3, label5, 0, 5, 1, 1);
		Commons.addComponent(panel3, label6, 0, 6, 1, 1);

		Commons.addComponent(panel3, label7, 1, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 1, 1, 1, 1);
		Commons.addComponent(panel3, textfield2_, 1, 2, 1, 1);
		Commons.addComponent(panel3, textfield3_, 1, 3, 1, 1);
		Commons.addComponent(panel3, textfield4_, 1, 4, 1, 1);
		Commons.addComponent(panel3, textfield5_, 1, 5, 1, 1);
		Commons.addComponent(panel3, textfield6_, 1, 6, 1, 1);

		Commons.addComponent(panel3, label8, 2, 0, 1, 1);
		Commons.addComponent(panel3, textfield7_, 2, 1, 1, 1);
		Commons.addComponent(panel3, textfield8_, 2, 2, 1, 1);
		Commons.addComponent(panel3, textfield9_, 2, 3, 1, 1);
		Commons.addComponent(panel3, textfield10_, 2, 4, 1, 1);
		Commons.addComponent(panel3, textfield11_, 2, 5, 1, 1);
		Commons.addComponent(panel3, textfield12_, 2, 6, 1, 1);

		Commons.addComponent(panel3, label9, 3, 0, 1, 1);
		Commons.addComponent(panel3, textfield13_, 3, 1, 1, 1);
		Commons.addComponent(panel3, textfield14_, 3, 2, 1, 1);
		Commons.addComponent(panel3, textfield15_, 3, 3, 1, 1);
		Commons.addComponent(panel3, textfield16_, 3, 4, 1, 1);
		Commons.addComponent(panel3, textfield17_, 3, 5, 1, 1);
		Commons.addComponent(panel3, textfield18_, 3, 6, 1, 1);

		Commons.addComponent(panel3, label10, 4, 0, 1, 1);
		Commons.addComponent(panel3, textfield19_, 4, 1, 1, 1);
		Commons.addComponent(panel3, textfield20_, 4, 2, 1, 1);
		Commons.addComponent(panel3, textfield21_, 4, 3, 1, 1);
		Commons.addComponent(panel3, textfield22_, 4, 4, 1, 1);
		Commons.addComponent(panel3, textfield23_, 4, 5, 1, 1);
		Commons.addComponent(panel3, textfield24_, 4, 6, 1, 1);

		Commons.addComponent(panel3, label11, 5, 0, 1, 1);
		Commons.addComponent(panel3, textfield25_, 5, 1, 1, 1);
		Commons.addComponent(panel3, textfield26_, 5, 2, 1, 1);
		Commons.addComponent(panel3, textfield27_, 5, 3, 1, 1);
		Commons.addComponent(panel3, textfield28_, 5, 4, 1, 1);
		Commons.addComponent(panel3, textfield29_, 5, 5, 1, 1);
		Commons.addComponent(panel3, textfield30_, 5, 6, 1, 1);

		Commons.addComponent(panel3, label12, 6, 0, 1, 1);
		Commons.addComponent(panel3, textfield31_, 6, 1, 1, 1);
		Commons.addComponent(panel3, textfield32_, 6, 2, 1, 1);
		Commons.addComponent(panel3, textfield33_, 6, 3, 1, 1);
		Commons.addComponent(panel3, textfield34_, 6, 4, 1, 1);
		Commons.addComponent(panel3, textfield35_, 6, 5, 1, 1);
		Commons.addComponent(panel3, textfield36_, 6, 6, 1, 1);

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
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield8_.addFocusListener(this);
		textfield9_.addFocusListener(this);
		textfield10_.addFocusListener(this);
		textfield11_.addFocusListener(this);
		textfield12_.addFocusListener(this);
		textfield15_.addFocusListener(this);
		textfield16_.addFocusListener(this);
		textfield17_.addFocusListener(this);
		textfield18_.addFocusListener(this);
		textfield22_.addFocusListener(this);
		textfield23_.addFocusListener(this);
		textfield24_.addFocusListener(this);
		textfield29_.addFocusListener(this);
		textfield30_.addFocusListener(this);
		textfield36_.addFocusListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getActionCommand() == "  OK  ") {

			// stiffness matrix negative definite
			if (stiffness_.determinant() < 0) {

				// display message
				JOptionPane.showMessageDialog(NodalSpring3.this,
						"Stiffness matrix is not positive definite!",
						"False data entry", 2);
			}

			// stiffness matrix positive definite
			else {

				// set stiffness
				String[][] values = new String[6][6];
				for (int i = 0; i < 6; i++) {
					for (int j = 0; j < 6; j++) {
						values[i][j] = owner_.owner_.owner_.formatter_
								.format(stiffness_.get(i, j));
						owner_.stiffness_.set(i, j, stiffness_.get(i, j));
					}
				}

				// set mother dialog's textfields
				owner_.textfield2_.setText(values[0][0]);
				owner_.textfield3_.setText(values[1][1]);
				owner_.textfield4_.setText(values[2][2]);
				owner_.textfield5_.setText(values[3][3]);
				owner_.textfield6_.setText(values[4][4]);
				owner_.textfield7_.setText(values[5][5]);

				// set dialog unvisible
				setVisible(false);
			}
		}

		// cancel button clicked
		else if (e.getActionCommand() == "Cancel") {

			// set dialog unvisible
			setVisible(false);
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

				// check textfield
				if (checkText(tfield) == false) {
					setDefaultText(tfield);
				}

				// set matrix values
				double value = Double.parseDouble(tfield.getText());
				if (tfield.equals(textfield1_))
					stiffness_.set(0, 0, value);
				else if (tfield.equals(textfield2_)) {
					stiffness_.set(0, 1, value);
					textfield7_.setText(tfield.getText());
					stiffness_.set(1, 0, value);
				} else if (tfield.equals(textfield3_)) {
					stiffness_.set(0, 2, value);
					textfield13_.setText(tfield.getText());
					stiffness_.set(2, 0, value);
				} else if (tfield.equals(textfield4_)) {
					stiffness_.set(0, 3, value);
					textfield19_.setText(tfield.getText());
					stiffness_.set(3, 0, value);
				} else if (tfield.equals(textfield5_)) {
					stiffness_.set(0, 4, value);
					textfield25_.setText(tfield.getText());
					stiffness_.set(4, 0, value);
				} else if (tfield.equals(textfield6_)) {
					stiffness_.set(0, 5, value);
					textfield31_.setText(tfield.getText());
					stiffness_.set(5, 0, value);
				}

				else if (tfield.equals(textfield8_))
					stiffness_.set(1, 1, value);
				else if (tfield.equals(textfield9_)) {
					stiffness_.set(1, 2, value);
					textfield14_.setText(tfield.getText());
					stiffness_.set(2, 1, value);
				} else if (tfield.equals(textfield10_)) {
					stiffness_.set(1, 3, value);
					textfield20_.setText(tfield.getText());
					stiffness_.set(3, 1, value);
				} else if (tfield.equals(textfield11_)) {
					stiffness_.set(1, 4, value);
					textfield26_.setText(tfield.getText());
					stiffness_.set(4, 1, value);
				} else if (tfield.equals(textfield12_)) {
					stiffness_.set(1, 5, value);
					textfield32_.setText(tfield.getText());
					stiffness_.set(5, 1, value);
				}

				else if (tfield.equals(textfield15_))
					stiffness_.set(2, 2, value);
				else if (tfield.equals(textfield16_)) {
					stiffness_.set(2, 3, value);
					textfield21_.setText(tfield.getText());
					stiffness_.set(3, 2, value);
				} else if (tfield.equals(textfield17_)) {
					stiffness_.set(2, 4, value);
					textfield27_.setText(tfield.getText());
					stiffness_.set(4, 2, value);
				} else if (tfield.equals(textfield18_)) {
					stiffness_.set(2, 5, value);
					textfield33_.setText(tfield.getText());
					stiffness_.set(5, 2, value);
				}

				else if (tfield.equals(textfield22_))
					stiffness_.set(3, 3, value);
				else if (tfield.equals(textfield23_)) {
					stiffness_.set(3, 4, value);
					textfield28_.setText(tfield.getText());
					stiffness_.set(4, 3, value);
				} else if (tfield.equals(textfield24_)) {
					stiffness_.set(3, 5, value);
					textfield34_.setText(tfield.getText());
					stiffness_.set(5, 3, value);
				}

				else if (tfield.equals(textfield29_))
					stiffness_.set(4, 4, value);
				else if (tfield.equals(textfield30_)) {
					stiffness_.set(4, 5, value);
					textfield35_.setText(tfield.getText());
					stiffness_.set(5, 4, value);
				}

				else if (tfield.equals(textfield36_))
					stiffness_.set(5, 5, value);
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param textfield
	 *            The textfield that the false data has been entered.
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkText(JTextField textfield) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield.getText();

		// check if it is editable
		if (textfield.isEditable()) {

			// check for non-numeric values
			try {

				// convert text to double value
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text);
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(NodalSpring3.this,
						"Illegal value!", "False data entry", 2);
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

		// The default values for textfields
		String defaultValue1 = owner_.owner_.owner_.formatter_.format(0.0);

		// set to textfields
		textfield.setText(defaultValue1);
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
