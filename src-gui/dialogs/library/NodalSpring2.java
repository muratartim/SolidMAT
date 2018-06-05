package dialogs.library;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;

import node.NodalSpring;

import main.Commons;
// import main.ImageHandler;
import matrix.DMat;

/**
 * Class for Add/Modify Nodal Springs menu.
 * 
 * @author Murat
 * 
 */
public class NodalSpring2 extends JDialog implements ActionListener,
		ItemListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JLabel label3_, label4_, label5_, label6_, label7_, label8_;

	private JButton button1_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected NodalSpring1 owner_;

	/** The spring stiffness matrix. */
	protected DMat stiffness_ = new DMat(6, 6);

	protected JComboBox combobox1_;

	protected JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

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
	public NodalSpring2(NodalSpring1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Library and Coordinate System",
				Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Spring Stiffness", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Coordinate system :");
		label3_ = new JLabel("Translation X :");
		label4_ = new JLabel("Translation Y :");
		label5_ = new JLabel("Translation Z :");
		label6_ = new JLabel("Rotation about X :");
		label7_ = new JLabel("Rotation about Y :");
		label8_ = new JLabel("Rotation about Z :");
		label3_.setPreferredSize(new Dimension(90, 20));

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(90, 20));
		textfield2_.setPreferredSize(new Dimension(90, 20));

		// build buttons and set font
		button1_ = new JButton("Advanced...");
		JButton button2 = new JButton("  OK  ");
		JButton button3 = new JButton("Cancel");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types[] = { "Global", "Local" };
		combobox1_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(2);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, label3_, 0, 0, 1, 1);
		Commons.addComponent(panel4, label4_, 1, 0, 1, 1);
		Commons.addComponent(panel4, label5_, 2, 0, 1, 1);
		Commons.addComponent(panel4, label6_, 3, 0, 1, 1);
		Commons.addComponent(panel4, label7_, 4, 0, 1, 1);
		Commons.addComponent(panel4, label8_, 5, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 1, 1, 1);
		Commons.addComponent(panel4, textfield5_, 3, 1, 1, 1);
		Commons.addComponent(panel4, textfield6_, 4, 1, 1, 1);
		Commons.addComponent(panel4, textfield7_, 5, 1, 1, 1);
		Commons.addComponent(panel4, button1_, 6, 0, 2, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button2);
		panel2.add(button3);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);
		textfield5_.addFocusListener(this);
		textfield6_.addFocusListener(this);
		textfield7_.addFocusListener(this);
		combobox1_.addItemListener(this);

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
		NodalSpring selected = owner_.temporary_.get(index);

		// get name, coordinate system, value
		String name = selected.getName();
		int coord = selected.getCoordinateSystem();
		stiffness_ = selected.getStiffness();

		// set name
		textfield1_.setText(name);

		// set coordinate system
		combobox1_.setSelectedIndex(coord);

		// set mass values
		textfield2_.setText(owner_.owner_.formatter_.format(stiffness_
				.get(0, 0)));
		textfield3_.setText(owner_.owner_.formatter_.format(stiffness_
				.get(1, 1)));
		textfield4_.setText(owner_.owner_.formatter_.format(stiffness_
				.get(2, 2)));
		textfield5_.setText(owner_.owner_.formatter_.format(stiffness_
				.get(3, 3)));
		textfield6_.setText(owner_.owner_.formatter_.format(stiffness_
				.get(4, 4)));
		textfield7_.setText(owner_.owner_.formatter_.format(stiffness_
				.get(5, 5)));
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

		// advanced button clicked
		else if (e.getSource().equals(button1_)) {

			// create child dialog and set visible
			NodalSpring3 dialog = new NodalSpring3(this);
			dialog.setVisible(true);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield1_, 1)) {

				// check stiffness matrix
				if (stiffness_.determinant() < 0) {

					// display message
					JOptionPane.showMessageDialog(NodalSpring2.this,
							"Stiffness matrix is not positive definite!",
							"False data entry", 2);
				} else {
					actionOkAddModify();
					setVisible(false);
				}
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield1_.getText().equals(selected)) {

				// check stiffness matrix
				if (stiffness_.determinant() < 0) {

					// display message
					JOptionPane.showMessageDialog(NodalSpring2.this,
							"Stiffness matrix is not positive definite!",
							"False data entry", 2);
				} else {
					actionOkAddModify();
					setVisible(false);
				}
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield1_, 1)) {

					// check stiffness matrix
					if (stiffness_.determinant() < 0) {

						// display message
						JOptionPane.showMessageDialog(NodalSpring2.this,
								"Stiffness matrix is not positive definite!",
								"False data entry", 2);
					} else {
						actionOkAddModify();
						setVisible(false);
					}
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 */
	private void actionOkAddModify() {

		// get name
		String name = textfield1_.getText();

		// get coordinate system
		int cs = combobox1_.getSelectedIndex();

		// create object
		NodalSpring object = new NodalSpring(name, cs, stiffness_);

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
	 * If the related checkbox is selected, sets default value to textfield and
	 * makes it editable. If the checkbox is deselected, clears textfield and
	 * makes it uneditable.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// get selected index
			int index = combobox1_.getSelectedIndex();

			// for the "Global" option
			if (index == NodalSpring.global_) {
				label3_.setText("Translation X :");
				label4_.setText("Translation Y :");
				label5_.setText("Translation Z :");
				label6_.setText("Rotation about X :");
				label7_.setText("Rotation about Y :");
				label8_.setText("Rotation about Z :");
			}

			// for the "Local" option
			else if (index == NodalSpring.local_) {
				label3_.setText("Translation 1 :");
				label4_.setText("Translation 2 :");
				label5_.setText("Translation 3 :");
				label6_.setText("Rotation about 1 :");
				label7_.setText("Rotation about 2 :");
				label8_.setText("Rotation about 3 :");
			}
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
				if (tfield.equals(textfield1_))
					messageType = 0;

				// check textfield
				if (checkText(tfield, messageType) == false) {
					setDefaultText(tfield);
				}

				// set matrix value ux
				double value = Double.parseDouble(tfield.getText());
				if (tfield.equals(textfield2_))
					stiffness_.set(0, 0, value);
				else if (tfield.equals(textfield3_))
					stiffness_.set(1, 1, value);
				else if (tfield.equals(textfield4_))
					stiffness_.set(2, 2, value);
				else if (tfield.equals(textfield5_))
					stiffness_.set(3, 3, value);
				else if (tfield.equals(textfield6_))
					stiffness_.set(4, 4, value);
				else if (tfield.equals(textfield7_))
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
				JOptionPane.showMessageDialog(NodalSpring2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(NodalSpring2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Illegal value
		else if (messageType == 2) {

			// check if it is editable
			if (textfield.isEditable()) {

				// check for non-numeric values
				try {

					// convert text to double value
					@SuppressWarnings("unused")
					double value = Double.parseDouble(text);
				} catch (Exception excep) {

					// display message
					JOptionPane.showMessageDialog(NodalSpring2.this,
							"Illegal value!", "False data entry", 2);
					isCorrect = false;
				}
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
		String defaultName = "Spring1";
		String defaultValue1 = owner_.owner_.formatter_.format(0.0);

		// set to textfields
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue1);
		else if (textfield.equals(textfield3_))
			textfield3_.setText(defaultValue1);
		else if (textfield.equals(textfield4_))
			textfield4_.setText(defaultValue1);
		else if (textfield.equals(textfield5_))
			textfield5_.setText(defaultValue1);
		else if (textfield.equals(textfield6_))
			textfield6_.setText(defaultValue1);
		else if (textfield.equals(textfield7_))
			textfield7_.setText(defaultValue1);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue1);
			textfield3_.setText(defaultValue1);
			textfield4_.setText(defaultValue1);
			textfield5_.setText(defaultValue1);
			textfield6_.setText(defaultValue1);
			textfield7_.setText(defaultValue1);
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
