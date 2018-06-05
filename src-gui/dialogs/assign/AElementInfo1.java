package dialogs.assign;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup; // import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import data.Group;
import element.Element;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;

/**
 * Class for Element Info Assign menu.
 * 
 * @author Murat
 * 
 */
public class AElementInfo1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_;

	private JComboBox combobox1_;

	private JButton button1_, button2_;

	/** Vector for storing element ids to be moved. */
	protected Vector<Integer> elementIds_ = new Vector<Integer>();

	/** The owner frame of this dialog. */
	protected SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public AElementInfo1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Element Information", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Elements", Commons.gridbag_);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(82, 20));

		// get groups
		String[] groups = getGroups();

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Element IDs :", true);
		radiobutton2_ = new JRadioButton("Groups :", false);
		radiobutton3_ = new JRadioButton("All existing elements", false);

		// check if there is any group
		if (groups.length == 0)
			radiobutton2_.setEnabled(false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);

		// build comboboxes
		combobox1_ = new JComboBox(groups);
		combobox1_.setMaximumRowCount(3);
		combobox1_.setEnabled(false);

		// build buttons
		button1_ = new JButton("Show");
		button2_ = new JButton("Close");

		// add components to sub-panels
		Commons.addComponent(panel3, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton3_, 2, 0, 2, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// add sub-panels to main panel
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);
		radiobutton3_.addItemListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Performs related action.
	 */
	public void actionPerformed(ActionEvent e) {

		// show button clicked
		if (e.getSource().equals(button1_)) {

			// check elements
			if (checkIds()) {

				// create child dialog and set visible
				AElementInfo2 dialog = new AElementInfo2(this);
				dialog.setVisible(true);
			}
		}

		// close button clicked
		else if (e.getSource().equals(button2_))
			setVisible(false);
	}

	/**
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 event
		if (event.getSource().equals(radiobutton1_)) {

			// set textfields editable and combobox disabled
			textfield1_.setEditable(true);
			combobox1_.setEnabled(false);
		}

		// radiobutton2 event
		else if (event.getSource().equals(radiobutton2_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(true);
		}

		// All existing nodes button clicked
		else if (event.getSource().equals(radiobutton3_)) {

			// set textfield disabled and combobox enabled
			textfield1_.setEditable(false);
			combobox1_.setEnabled(false);
		}
	}

	/**
	 * Checks whether any element of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if the given element ids are valid for moving, False vice
	 *         versa.
	 */
	private boolean checkIds() {

		// set element ids from textfield
		if (radiobutton1_.isSelected()) {
			if (setIdsFromText() == false)
				return false;
		}

		// set element ids from groups combobox
		else if (radiobutton2_.isSelected())
			setIdsFromGroup();

		// set node ids for all elements
		else if (radiobutton3_.isSelected())
			setIdsForAll();

		try {

			// check if given elements exist
			for (int i = 0; i < elementIds_.size(); i++) {

				// get element
				owner_.structure_.getElement(elementIds_.get(i));
			}
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(this, "Given elements do not exist!",
					"False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks and sets division element ids from textfield.
	 * 
	 * @return True if entered text is correct, False vice versa.
	 */
	private boolean setIdsFromText() {

		String text = textfield1_.getText();

		// eliminate spaces
		String elText = "";
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c != " ".charAt(0))
				elText += c;
		}

		// check for non-numeric or negative values
		try {

			// seperate components
			String[] comp = elText.split(",");

			// convert text to integer value
			elementIds_.clear();
			for (int i = 0; i < comp.length; i++) {
				elementIds_.add(Integer.parseInt(comp[i]));

				// check if negative
				if (elementIds_.get(i) < 0) {

					// display message
					JOptionPane.showMessageDialog(this, "Illegal values!",
							"False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(this, "Given elements do not exist!",
					"False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Sets division element ids from groups combobox.
	 * 
	 */
	private void setIdsFromGroup() {

		// get the selected group
		int index = combobox1_.getSelectedIndex();
		Group group = owner_.inputData_.getGroup().get(index);

		// get elements of group
		Vector<Element> elements = group.getElements();

		// get indices of group elements
		elementIds_.clear();
		for (int i = 0; i < elements.size(); i++) {

			// get the index of group element
			int n = owner_.structure_.indexOfElement(elements.get(i));

			// add to values vector
			elementIds_.add(n);
		}
	}

	/**
	 * Sets mesh element ids for all elements.
	 * 
	 */
	private void setIdsForAll() {

		// get indices of all elements
		elementIds_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++)
			elementIds_.add(i);
	}

	/**
	 * Returns group names array.
	 * 
	 * @return The group names array.
	 */
	private String[] getGroups() {

		// initialize group names array
		int n = owner_.inputData_.getGroup().size();
		String[] groups = new String[n];

		// loop over groups
		for (int i = 0; i < n; i++) {

			// get name of group
			groups[i] = owner_.inputData_.getGroup().get(i).getName();
		}

		// return group names array
		return groups;
	}
}
