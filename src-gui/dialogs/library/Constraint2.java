package dialogs.library;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;

import boundary.BoundaryCase;
import boundary.Constraint;

/**
 * Class for Add/Modify Constraints menu.
 * 
 * @author Murat
 * 
 */
public class Constraint2 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_, checkbox4_,
			checkbox5_, checkbox6_;

	private JComboBox combobox1_, combobox2_;

	private JButton button1_, button2_, button3_, button4_, button5_, button6_,
			button7_, button8_, button9_, button10_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public Constraint2(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Constraints", true);
		owner_ = owner;

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Name and Case", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Restraints", Commons.gridbag_);
		JPanel panel6 = Commons.getPanel("Fast Restraints", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Current :");
		JLabel label2 = new JLabel("Name :");
		JLabel label3 = new JLabel("Boundary case :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(143, 20));

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("Translation 1   ");
		checkbox2_ = new JCheckBox("Translation 2   ");
		checkbox3_ = new JCheckBox("Translation 3   ");
		checkbox4_ = new JCheckBox("Rotation about 1");
		checkbox5_ = new JCheckBox("Rotation about 2");
		checkbox6_ = new JCheckBox("Rotation about 3");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		combobox1_ = new JComboBox(setCurrent());
		combobox1_.setMaximumRowCount(10);
		combobox1_.setPreferredSize(new Dimension(175, 20));
		combobox2_ = new JComboBox(setBoundaryCases());
		combobox2_.setMaximumRowCount(10);

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("Clamped");
		button2_ = new JButton("Pinned");
		button3_ = new JButton("Free");
		button4_ = new JButton("Roller 1");
		button5_ = new JButton("Roller 2");
		button6_ = new JButton("Roller 3");
		button7_ = new JButton("Add");
		button8_ = new JButton("Modify");
		button9_ = new JButton("Remove");
		button10_ = new JButton("Close");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel5, checkbox2_, 1, 0, 1, 1);
		Commons.addComponent(panel5, checkbox3_, 2, 0, 1, 1);
		Commons.addComponent(panel5, checkbox4_, 0, 1, 1, 1);
		Commons.addComponent(panel5, checkbox5_, 1, 1, 1, 1);
		Commons.addComponent(panel5, checkbox6_, 2, 1, 1, 1);
		Commons.addComponent(panel6, button1_, 0, 0, 1, 1);
		Commons.addComponent(panel6, button2_, 0, 1, 1, 1);
		Commons.addComponent(panel6, button3_, 0, 2, 1, 1);
		Commons.addComponent(panel6, button4_, 1, 0, 1, 1);
		Commons.addComponent(panel6, button5_, 1, 1, 1, 1);
		Commons.addComponent(panel6, button6_, 1, 2, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		Commons.addComponent(panel1, panel6, 3, 0, 1, 1);
		panel2.add(button7_);
		panel2.add(button8_);
		panel2.add(button9_);
		panel2.add(button10_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);
		button5_.addActionListener(this);
		button6_.addActionListener(this);
		button7_.addActionListener(this);
		button8_.addActionListener(this);
		button9_.addActionListener(this);
		button10_.addActionListener(this);
		combobox1_.addItemListener(this);

		// initialize dialog
		initialize();

		// visualize dialog
		Commons.visualize(this);
	}

	/**
	 * Initializes gui.
	 */
	private void initialize() {

		// there are items in library
		if (combobox1_.getItemCount() != 0) {

			// get first item in library
			Constraint c = owner_.inputData_.getConstraint().firstElement();

			// set constraint to gui
			setToGui(c);
		}

		// there is no item in library
		else
			setDefaultText();
	}

	@Override
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// there are items in combobox
			if (combobox1_.getItemCount() != 0) {

				// get selected name
				String name = (String) combobox1_.getSelectedItem();

				// get items vector
				Vector<Constraint> items = owner_.inputData_.getConstraint();

				// get selected item in library
				Constraint c = null;
				for (int i = 0; i < items.size(); i++) {

					// get item
					c = items.get(i);

					// stop if name is equal
					if (name.equalsIgnoreCase(items.get(i).getName()))
						break;
				}

				// set constraint to gui
				setToGui(c);
			}
		}
	}

	/**
	 * Sets properties of given constraint to gui.
	 * 
	 * @param c
	 *            Constraint to be set.
	 */
	private void setToGui(Constraint c) {

		// set name
		textfield1_.setText(c.getName());

		// set boundary case
		combobox2_.setSelectedItem(c.getBoundaryCase().getName());

		// set restraints
		boolean[] values = c.getConstraints();
		checkbox1_.setSelected(!values[0]);
		checkbox2_.setSelected(!values[1]);
		checkbox3_.setSelected(!values[2]);
		checkbox4_.setSelected(!values[3]);
		checkbox5_.setSelected(!values[4]);
		checkbox6_.setSelected(!values[5]);
	}

	/**
	 * Returns new constraint from properties in gui.
	 * 
	 * @return New constraint.
	 */
	private Constraint getFromGui() {

		// get name
		String name = textfield1_.getText();

		// get boundary case
		BoundaryCase bCase = null;

		// get selected case name
		String selected = combobox2_.getSelectedItem().toString();

		// get boundary cases
		Vector<BoundaryCase> cases = owner_.inputData_.getBoundaryCase();

		// loop over cases
		for (int i = 0; i < cases.size(); i++) {

			// get case
			bCase = owner_.inputData_.getBoundaryCase().get(i);

			// get name of case
			String caseName = bCase.getName();

			// selected equals case
			if (selected.equals(caseName))
				break;
		}

		// get restraints
		boolean[] restraints = { !checkbox1_.isSelected(),
				!checkbox2_.isSelected(), !checkbox3_.isSelected(),
				!checkbox4_.isSelected(), !checkbox5_.isSelected(),
				!checkbox6_.isSelected() };

		// return object
		return new Constraint(name, bCase, restraints);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		// clamped button clicked
		if (e.getSource().equals(button1_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(true);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(true);
			checkbox5_.setSelected(true);
			checkbox6_.setSelected(true);
		}

		// simple button clicked
		else if (e.getSource().equals(button2_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(true);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(false);
			checkbox6_.setSelected(false);
		}

		// free button clicked
		else if (e.getSource().equals(button3_)) {

			// set checkboxes
			checkbox1_.setSelected(false);
			checkbox2_.setSelected(false);
			checkbox3_.setSelected(false);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(false);
			checkbox6_.setSelected(false);
		}

		// roller x1 button clicked
		else if (e.getSource().equals(button4_)) {

			// set checkboxes
			checkbox1_.setSelected(false);
			checkbox2_.setSelected(true);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(false);
			checkbox6_.setSelected(false);
		}

		// roller x2 button clicked
		else if (e.getSource().equals(button5_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(false);
			checkbox3_.setSelected(true);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(false);
			checkbox6_.setSelected(false);
		}

		// roller x3 button clicked
		else if (e.getSource().equals(button6_)) {

			// set checkboxes
			checkbox1_.setSelected(true);
			checkbox2_.setSelected(true);
			checkbox3_.setSelected(false);
			checkbox4_.setSelected(false);
			checkbox5_.setSelected(false);
			checkbox6_.setSelected(false);
		}

		// add button clicked
		else if (e.getSource().equals(button7_)) {

			// check text
			if (checkText()) {

				// add new constraint to library
				owner_.inputData_.getConstraint().add(getFromGui());

				// add name of new constraint to current combo
				combobox1_.addItem(textfield1_.getText());

				// select new added item
				combobox1_.setSelectedItem(textfield1_.getText());
			}
		}

		// modify button clicked
		else if (e.getSource().equals(button8_)) {

			// there are no items in library
			if (combobox1_.getItemCount() == 0)
				JOptionPane.showMessageDialog(Constraint2.this,
						"There are no items to modify!", "False data entry", 2);

			// there are items
			else {

				// check text
				if (!textfield1_.getText().equals(
						combobox1_.getSelectedItem().toString())) {
					if (checkText()) {
						modify();
					}
				} else
					modify();
			}
		}

		// remove button clicked
		else if (e.getSource().equals(button9_)) {

			// there are no items in library
			if (combobox1_.getItemCount() == 0)
				JOptionPane.showMessageDialog(Constraint2.this,
						"There are no items to remove!", "False data entry", 2);

			// there are items
			else {

				// get selected name
				String selected = combobox1_.getSelectedItem().toString();

				// get items in library
				Vector<Constraint> items = owner_.inputData_.getConstraint();

				// loop over items in library
				for (int i = 0; i < items.size(); i++) {

					// get name of item
					if (selected.equals(items.get(i).getName())) {
						items.remove(i);
						break;
					}
				}

				// renew combo
				int index = combobox1_.getSelectedIndex();
				Vector<String> a1 = new Vector<String>();
				for (int i = 0; i < combobox1_.getItemCount(); i++)
					if (i != index)
						a1.add(combobox1_.getItemAt(i).toString());
				combobox1_.removeAllItems();
				for (int i = 0; i < a1.size(); i++)
					combobox1_.addItem(a1.get(i));
			}
		}

		// close button clicked
		else if (e.getSource().equals(button10_))
			setVisible(false);
	}

	private void modify() {

		// get selected name
		String selected = combobox1_.getSelectedItem().toString();

		// get items in library
		Vector<Constraint> items = owner_.inputData_.getConstraint();

		// initialize constraint
		Constraint c = null;

		// loop over items in library
		for (int i = 0; i < items.size(); i++) {

			// get name of item
			if (selected.equals(items.get(i).getName())) {
				c = items.get(i);
				break;
			}
		}

		// set name
		c.setName(textfield1_.getText());

		// set constraints
		boolean[] restraints = { !checkbox1_.isSelected(),
				!checkbox2_.isSelected(), !checkbox3_.isSelected(),
				!checkbox4_.isSelected(), !checkbox5_.isSelected(),
				!checkbox6_.isSelected() };
		c.setConstraints(restraints);

		// set boundary case
		BoundaryCase bCase = null;

		// get selected case name
		String scase = combobox2_.getSelectedItem().toString();

		// get boundary cases
		Vector<BoundaryCase> cases = owner_.inputData_.getBoundaryCase();

		// loop over cases
		for (int i = 0; i < cases.size(); i++) {

			// get case
			bCase = owner_.inputData_.getBoundaryCase().get(i);

			// get name of case
			String caseName = bCase.getName();

			// selected equals case
			if (scase.equals(caseName))
				break;
		}
		c.setBoundaryCase(bCase);

		// renew combo
		int index = combobox1_.getSelectedIndex();
		String[] a1 = new String[combobox1_.getItemCount()];
		for (int i = 0; i < a1.length; i++)
			a1[i] = combobox1_.getItemAt(i).toString();
		a1[index] = textfield1_.getText();
		combobox1_.removeAllItems();
		for (int i = 0; i < a1.length; i++)
			combobox1_.addItem(a1[i]);
		combobox1_.setSelectedItem(a1[index]);
	}

	/**
	 * Checks text fields.
	 * 
	 * @return True if text is acceptable, False vice versa.
	 */
	private boolean checkText() {

		// get text of textfield1
		String text = textfield1_.getText();

		// no name given
		if (text.equals("")) {

			// display message
			JOptionPane.showMessageDialog(Constraint2.this, "No name given!",
					"False data entry", 2);
			return false;
		}

		// check if name exists in list of combo
		// loop over combo items
		for (int i = 0; i < combobox1_.getItemCount(); i++) {

			// text equals item
			if (text.equalsIgnoreCase((String) combobox1_.getItemAt(i))) {

				// display message
				JOptionPane.showMessageDialog(Constraint2.this,
						"Name already exists!", "False data entry", 2);
				return false;
			}
		}

		// the data is correct
		return true;
	}

	/**
	 * Sets default values for textfield1.
	 * 
	 */
	private void setDefaultText() {

		// The default values for textfields
		String defaultName = "Constraint1";

		// set to textfield1
		textfield1_.setText(defaultName);
	}

	/**
	 * Returns an array containing the names of the currently defined items in
	 * the library.
	 * 
	 * @return Array of names.
	 */
	private String[] setCurrent() {

		// get length of current library
		int length = owner_.inputData_.getConstraint().size();

		// store names in an array
		String[] currentNames = new String[length];
		for (int i = 0; i < length; i++) {
			String name = owner_.inputData_.getConstraint().get(i).getName();
			currentNames[i] = name;
		}

		// return the array
		return currentNames;
	}

	/**
	 * Returns the boundary case names for boundary case combo list.
	 * 
	 * @return Boundary case names array.
	 */
	private String[] setBoundaryCases() {

		// get length of boundary cases input vector
		int length = owner_.inputData_.getBoundaryCase().size();

		// store them in an array
		String[] cases = new String[length];
		for (int i = 0; i < length; i++) {
			String name = owner_.inputData_.getBoundaryCase().get(i).getName();
			cases[i] = name;
		}

		// return the array
		return cases;
	}
}
