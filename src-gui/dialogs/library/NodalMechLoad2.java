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
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Commons;
// import main.ImageHandler;
import matrix.DVec;

import boundary.BoundaryCase;
import boundary.NodalMechLoad;

/**
 * Class for Add/Modify Nodal Mechanical Loads menu.
 * 
 * @author Murat
 * 
 */
public class NodalMechLoad2 extends JDialog implements ActionListener,
		ItemListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

	private JComboBox combobox1_, combobox2_;

	private JLabel label4_, label5_, label6_, label7_, label8_, label9_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	protected NodalMechLoad1 owner_;

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
	public NodalMechLoad2(NodalMechLoad1 owner, boolean add) {

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
		JPanel panel3 = Commons.getPanel("Library and Case", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Loads", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label2 = new JLabel("Boundary case :");
		JLabel label3 = new JLabel("Coordinate system :");
		label4_ = new JLabel("Force X :");
		label5_ = new JLabel("Force Y :");
		label6_ = new JLabel("Force Z :");
		label7_ = new JLabel("Moment about X :");
		label8_ = new JLabel("Moment about Y :");
		label9_ = new JLabel("Moment about Z :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types[] = { "Global", "Local" };
		combobox1_ = new JComboBox(setBoundaryCases());
		combobox2_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(2);
		combobox1_.setPreferredSize(new Dimension(119, 23));
		combobox2_.setPreferredSize(new Dimension(100, 23));

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel4, label3, 0, 0, 1, 1);
		Commons.addComponent(panel4, label4_, 1, 0, 1, 1);
		Commons.addComponent(panel4, label5_, 2, 0, 1, 1);
		Commons.addComponent(panel4, label6_, 3, 0, 1, 1);
		Commons.addComponent(panel4, label7_, 4, 0, 1, 1);
		Commons.addComponent(panel4, label8_, 5, 0, 1, 1);
		Commons.addComponent(panel4, label9_, 6, 0, 1, 1);
		Commons.addComponent(panel4, combobox2_, 0, 1, 2, 1);
		Commons.addComponent(panel4, textfield2_, 1, 1, 2, 1);
		Commons.addComponent(panel4, textfield3_, 2, 1, 2, 1);
		Commons.addComponent(panel4, textfield4_, 3, 1, 2, 1);
		Commons.addComponent(panel4, textfield5_, 4, 1, 2, 1);
		Commons.addComponent(panel4, textfield6_, 5, 1, 2, 1);
		Commons.addComponent(panel4, textfield7_, 6, 1, 2, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
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
		textfield7_.addFocusListener(this);
		combobox2_.addItemListener(this);

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
		NodalMechLoad selected = owner_.temporary_.get(index);

		// / get name, boundary case, coordinate system, load
		String name = selected.getName();
		String boundary = selected.getBoundaryCase().getName();
		int coordinateSystem = selected.getCoordinateSystem();
		double[] load = selected.getComponents().get1DArray();

		// set name
		textfield1_.setText(name);

		// set boundary case
		combobox1_.setSelectedItem(boundary);

		// set coordinate system
		combobox2_.setSelectedIndex(coordinateSystem);

		// set loading values
		textfield2_.setText(owner_.owner_.formatter_.format(load[0]));
		textfield3_.setText(owner_.owner_.formatter_.format(load[1]));
		textfield4_.setText(owner_.owner_.formatter_.format(load[2]));
		textfield5_.setText(owner_.owner_.formatter_.format(load[3]));
		textfield6_.setText(owner_.owner_.formatter_.format(load[4]));
		textfield7_.setText(owner_.owner_.formatter_.format(load[5]));
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

		// add button clicked from the mother dialog
		if (add_) {

			// check if textfield exists in list of mother dialog
			if (checkText(textfield1_, 1)) {
				actionOkAddModify();
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield1_.getText().equals(selected)) {
				actionOkAddModify();
				setVisible(false);
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(textfield1_, 1)) {
					actionOkAddModify();
					setVisible(false);
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

		// get boundary case
		BoundaryCase bCase = null;
		String boundary = combobox1_.getSelectedItem().toString();
		int length = owner_.owner_.inputData_.getBoundaryCase().size();
		for (int i = 0; i < length; i++) {
			bCase = owner_.owner_.inputData_.getBoundaryCase().get(i);
			String caseName = bCase.getName();
			if (boundary.equals(caseName))
				break;
		}

		// get coordinate system
		int cs = combobox2_.getSelectedIndex();

		// get loading values
		double[] values = new double[6];
		values[0] = Double.parseDouble(textfield2_.getText());
		values[1] = Double.parseDouble(textfield3_.getText());
		values[2] = Double.parseDouble(textfield4_.getText());
		values[3] = Double.parseDouble(textfield5_.getText());
		values[4] = Double.parseDouble(textfield6_.getText());
		values[5] = Double.parseDouble(textfield7_.getText());

		// create object
		NodalMechLoad object = new NodalMechLoad(name, bCase, new DVec(values));
		object.setCoordinateSystem(cs);

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

		// combobox2 event
		if (event.getSource().equals(combobox2_)) {

			// get selected index
			int index = combobox2_.getSelectedIndex();

			// for the "Global" option
			if (index == NodalMechLoad.global_) {
				label4_.setText("Force X :");
				label5_.setText("Force Y :");
				label6_.setText("Force Z :");
				label7_.setText("Moment about X :");
				label8_.setText("Moment about Y :");
				label9_.setText("Moment about Z :");
			}

			// for the "Local" option
			else if (index == NodalMechLoad.local_) {
				label4_.setText("Force 1 :");
				label5_.setText("Force 2 :");
				label6_.setText("Force 3 :");
				label7_.setText("Moment about 1 :");
				label8_.setText("Moment about 2 :");
				label9_.setText("Moment about 3 :");
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
				JOptionPane.showMessageDialog(NodalMechLoad2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(NodalMechLoad2.this,
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
					JOptionPane.showMessageDialog(NodalMechLoad2.this,
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
		String defaultName = "Load1";
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
	 * Returns the boundary case names for boundary case combo list.
	 * 
	 * @return Boundary case names array.
	 */
	private String[] setBoundaryCases() {

		// get length of boundary cases input vector
		int length = owner_.owner_.inputData_.getBoundaryCase().size();

		// store them in an array
		String[] cases = new String[length];
		for (int i = 0; i < length; i++) {
			String name = owner_.owner_.inputData_.getBoundaryCase().get(i)
					.getName();
			cases[i] = name;
		}

		// return the array
		return cases;
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
