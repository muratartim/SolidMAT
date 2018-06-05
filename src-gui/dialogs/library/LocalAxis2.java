package dialogs.library;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;
import node.LocalAxis;

/**
 * Class for Add/Modify Local Axes menu.
 * 
 * @author Murat
 * 
 */
public class LocalAxis2 extends JDialog implements ActionListener,
		FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_;

	private JTabbedPane tabbedpane1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected LocalAxis1 owner_;

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
	public LocalAxis2(LocalAxis1 owner, boolean add) {

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
		JPanel panel5 = Commons.getPanel("Rotations", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel7 = Commons.getPanel("Rotations", Commons.gridbag_);
		JPanel panel8 = new JPanel();
		panel8.setLayout(new BoxLayout(panel8, BoxLayout.Y_AXIS));

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Rotation about X :");
		JLabel label3 = new JLabel("Rotation about Y' :");
		JLabel label4 = new JLabel("Rotation about Z'' :");
		JLabel label5 = new JLabel("Name :");
		JLabel label6 = new JLabel("Axial rotation :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(118, 20));
		textfield2_.setPreferredSize(new Dimension(60, 20));
		textfield6_.setPreferredSize(new Dimension(81, 20));
		textfield5_.setPreferredSize(new Dimension(118, 20));

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel4, label1, 0, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label2, 0, 0, 1, 1);
		Commons.addComponent(panel5, label3, 1, 0, 1, 1);
		Commons.addComponent(panel5, label4, 2, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel6, label5, 0, 0, 1, 1);
		Commons.addComponent(panel6, textfield5_, 0, 1, 1, 1);
		Commons.addComponent(panel7, label6, 0, 0, 1, 1);
		Commons.addComponent(panel7, textfield6_, 0, 1, 1, 1);
		panel8.add(Box.createRigidArea(new Dimension(0, 50)));

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel4, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 1, 0, 1, 1);
		Commons.addComponent(panel2, panel6, 0, 0, 1, 1);
		Commons.addComponent(panel2, panel7, 1, 0, 1, 1);
		Commons.addComponent(panel2, panel8, 2, 0, 1, 1);
		panel3.add(button1);
		panel3.add(button2);

		// build tabbedpane and set font
		tabbedpane1_ = new JTabbedPane();

		// add panels to tabbedpane
		tabbedpane1_.addTab("Points", panel1);
		tabbedpane1_.addTab("Lines", panel2);

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
		LocalAxis selected = owner_.temporary_.get(index);

		// get name, type and values of selected object
		String name = selected.getName();
		int type = selected.getType();
		double[] values = selected.getValues();

		// set tab
		tabbedpane1_.setSelectedIndex(type);

		// set textfields for points
		if (type == 0) {
			textfield1_.setText(name);
			textfield2_.setText(owner_.owner_.formatter_.format(values[0]));
			textfield3_.setText(owner_.owner_.formatter_.format(values[1]));
			textfield4_.setText(owner_.owner_.formatter_.format(values[2]));
			setDefaultText(textfield5_);
			setDefaultText(textfield6_);
		}

		// set textfields for lines
		else if (type == 1) {
			setDefaultText(textfield1_);
			setDefaultText(textfield2_);
			setDefaultText(textfield3_);
			setDefaultText(textfield4_);
			textfield5_.setText(name);
			textfield6_.setText(owner_.owner_.formatter_.format(values[0]));
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
		if (type == 0)
			textfield = textfield1_;
		else if (type == 1)
			textfield = textfield5_;

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
	 *            The type of object (point -> 0, line -> 1).
	 */
	private void actionOkAddModify(int type) {

		// initialize name and values
		String name = null;
		double[] values = null;

		// for points
		if (type == 0) {

			// get name and values
			name = textfield1_.getText();
			values = new double[3];
			values[0] = Double.parseDouble(textfield2_.getText());
			values[1] = Double.parseDouble(textfield3_.getText());
			values[2] = Double.parseDouble(textfield4_.getText());
		}

		// for lines
		else if (type == 1) {

			// get name and values
			name = textfield5_.getText();
			values = new double[1];
			values[0] = Double.parseDouble(textfield6_.getText());
		}

		// create object
		LocalAxis object = new LocalAxis(name, type, values);

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
				if (tfield.equals(textfield1_) || tfield.equals(textfield5_))
					messageType = 0;

				// check textfield
				if (checkText(tfield, messageType) == false) {
					setDefaultText(tfield);
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
	 *            exists -> 1, Illegal value -> 2).
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
				JOptionPane.showMessageDialog(LocalAxis2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(LocalAxis2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value
		else if (messageType == 2) {

			// check for non-numeric values
			try {

				// convert text to double value
				@SuppressWarnings("unused")
				double value = Double.parseDouble(text);
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(LocalAxis2.this,
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
		String defaultName = "Axis1";
		String defaultValue = owner_.owner_.formatter_.format(0.0);

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue);

		// set to textfield3
		else if (textfield.equals(textfield3_))
			textfield3_.setText(defaultValue);

		// set to textfield4
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultValue);

		// set to textfield5
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultName);

		// set to textfield6
		else if (textfield.equals(textfield6_))
			textfield6_.setText(defaultValue);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue);
			textfield3_.setText(defaultValue);
			textfield4_.setText(defaultValue);
			textfield5_.setText(defaultName);
			textfield6_.setText(defaultValue);
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
