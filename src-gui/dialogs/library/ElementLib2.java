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
import main.DocumentHandler;
// import main.ImageHandler;

import element.ElementLibrary;

/**
 * Class for Add/Modify Element Libraries menu.
 * 
 * @author Murat
 * 
 */
public class ElementLib2 extends JDialog implements ActionListener,
		ItemListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JButton button1_;

	private JTextField textfield1_, textfield2_;

	private JComboBox combobox1_, combobox2_, combobox3_;

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	private ElementLib1 owner_;

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
	public ElementLib2(ElementLib1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Library", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Library", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Properties", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");
		JLabel label3 = new JLabel("Geometry :");
		JLabel label4 = new JLabel("Mechanics :");
		JLabel label5 = new JLabel("Interpolation :");
		JLabel label6 = new JLabel("Type :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield2_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(145, 20));

		// build buttons and set font
		button1_ = new JButton("Element Info...");
		JButton button2 = new JButton("  OK  ");
		JButton button3 = new JButton("Cancel");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Line", "Quad", "Tria", "Hexa", "Tetra" };
		String types2[] = { "Truss", "Thick beam", "Curved thick beam",
				"Planar truss", "Planar thick beam", "Thin beam" };
		String types3[] = { "Linear", "Quadratic", "Cubic" };
		combobox1_ = new JComboBox(types1);
		combobox2_ = new JComboBox(types2);
		combobox3_ = new JComboBox(types3);
		combobox1_.setMaximumRowCount(5);
		combobox2_.setMaximumRowCount(6);
		combobox3_.setMaximumRowCount(3);
		combobox1_.setPreferredSize(new Dimension(110, 22));

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label3, 0, 0, 1, 1);
		Commons.addComponent(panel4, label4, 1, 0, 1, 1);
		Commons.addComponent(panel4, label5, 2, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel4, combobox3_, 2, 1, 1, 1);
		Commons.addComponent(panel4, label6, 3, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 3, 1, 1, 1);
		Commons.addComponent(panel4, button1_, 4, 0, 2, 1);

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
		combobox1_.addItemListener(this);
		combobox2_.addItemListener(this);
		combobox3_.addItemListener(this);

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
		ElementLibrary selected = owner_.temporary_.get(index);

		// get name, type and properties
		String name = selected.getName();
		int geo = selected.getGeometry();
		int mech = selected.getMechanics();
		int interp = selected.getInterpolation();

		// set name
		textfield1_.setText(name);

		// set comboboxes
		combobox1_.setSelectedIndex(geo);
		combobox2_.setSelectedIndex(mech);
		combobox3_.setSelectedIndex(interp);
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
		else if (e.getSource().equals(button1_))
			openDocument();
	}

	/**
	 * Opens Element Library documentation (Volume B) for the selected element
	 * type.
	 * 
	 */
	private void openDocument() {

		// get name
		String name = textfield1_.getText();

		// get properties
		int geo = combobox1_.getSelectedIndex();
		int mech = combobox2_.getSelectedIndex();
		int interp = combobox3_.getSelectedIndex();

		// create object
		try {

			// create element library and get type
			ElementLibrary object = new ElementLibrary(name, geo, mech, interp);
			int type = object.getType();

			// initialize page number and option
			String page = null;
			int option = DocumentHandler.page_;

			// set page number
			switch (type) {
			case ElementLibrary.element0_:
				page = "7";
				break;
			case ElementLibrary.element1_:
				page = "10";
				break;
			case ElementLibrary.element2_:
				page = "13";
				break;
			case ElementLibrary.element3_:
				page = "16";
				break;
			case ElementLibrary.element4_:
				page = "20";
				break;
			case ElementLibrary.element5_:
				page = "24";
				break;
			case ElementLibrary.element6_:
				page = "28";
				break;
			case ElementLibrary.element7_:
				page = "32";
				break;
			case ElementLibrary.element8_:
				page = "36";
				break;
			case ElementLibrary.element9_:
				page = "40";
				break;
			case ElementLibrary.element10_:
				page = "44";
				break;
			case ElementLibrary.element11_:
				page = "48";
				break;
			case ElementLibrary.element12_:
				page = "52";
				break;
			case ElementLibrary.element13_:
				page = "56";
				break;
			case ElementLibrary.element14_:
				page = "61";
				break;
			case ElementLibrary.element15_:
				page = "66";
				break;
			case ElementLibrary.element16_:
				page = "71";
				break;
			case ElementLibrary.element17_:
				page = "76";
				break;
			case ElementLibrary.element18_:
				page = "81";
				break;
			case ElementLibrary.element19_:
				page = "86";
				break;
			case ElementLibrary.element20_:
				page = "91";
				break;
			case ElementLibrary.element21_:
				page = "96";
				break;
			case ElementLibrary.element22_:
				page = "101";
				break;
			case ElementLibrary.element23_:
				page = "105";
				break;
			case ElementLibrary.element24_:
				page = "109";
				break;
			case ElementLibrary.element25_:
				page = "114";
				break;
			case ElementLibrary.element26_:
				page = "119";
				break;
			case ElementLibrary.element27_:
				page = "124";
				break;
			case ElementLibrary.element28_:
				page = "129";
				break;
			case ElementLibrary.element29_:
				page = "134";
				break;
			case ElementLibrary.element30_:
				page = "139";
				break;
			case ElementLibrary.element31_:
				page = "143";
				break;
			case ElementLibrary.element32_:
				page = "177";
				break;
			case ElementLibrary.element33_:
				page = "183";
				break;
			case ElementLibrary.element34_:
				page = "189";
				break;
			case ElementLibrary.element35_:
				page = "195";
				break;
			case ElementLibrary.element36_:
				page = "203";
				break;
			case ElementLibrary.element37_:
				page = "211";
				break;
			}

			// open document
			DocumentHandler.openPDFDocument("VolumeB.pdf", option, page);
		}

		// invalid element type
		catch (Exception excep) {

			// get error message
			String message = excep.getLocalizedMessage();

			// display message
			JOptionPane.showMessageDialog(ElementLib2.this, message,
					"False data entry", 2);
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
			if (checkText(1)) {
				if (actionOkAddModify())
					setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if textfield is equal to selected item of list
			if (textfield1_.getText().equals(selected)) {
				if (actionOkAddModify())
					setVisible(false);
			} else {

				// check if textfield exists in list of mother dialog
				if (checkText(1)) {
					if (actionOkAddModify())
						setVisible(false);
				}
			}
		}
	}

	/**
	 * Sets type number according to selections made.
	 * 
	 */
	private void setType() {

		// get name
		String name = textfield1_.getText();

		// get properties
		int geo = combobox1_.getSelectedIndex();
		int mech = combobox2_.getSelectedIndex();
		int interp = combobox3_.getSelectedIndex();

		// create object
		try {
			ElementLibrary object = new ElementLibrary(name, geo, mech, interp);
			textfield2_.setText(Integer.toString(object.getType()));
		}

		// invalid element type
		catch (Exception excep) {
			textfield2_.setText("Invalid element type.");
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 */
	private boolean actionOkAddModify() {

		// get name
		String name = textfield1_.getText();

		// get properties
		int geo = combobox1_.getSelectedIndex();
		int mech = combobox2_.getSelectedIndex();
		int interp = combobox3_.getSelectedIndex();

		// create object
		try {

			ElementLibrary object = new ElementLibrary(name, geo, mech, interp);

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
			return true;
		} catch (Exception excep) {

			// get error message
			String message = excep.getLocalizedMessage();

			// display message
			JOptionPane.showMessageDialog(ElementLib2.this, message,
					"False data entry", 2);
			return false;
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

			// for the Line option
			if (index == ElementLibrary.line_) {
				String types2[] = { "Truss", "Thick beam", "Curved thick beam",
						"Planar truss", "Planar thick beam", "Thin beam" };
				String types3[] = { "Linear", "Quadratic", "Cubic" };
				combobox2_.removeAllItems();
				combobox3_.removeAllItems();
				combobox2_.addItem(types2[0]);
				combobox2_.addItem(types2[1]);
				combobox2_.addItem(types2[2]);
				combobox2_.addItem(types2[3]);
				combobox2_.addItem(types2[4]);
				combobox2_.addItem(types2[5]);
				combobox3_.addItem(types3[0]);
				combobox3_.addItem(types3[1]);
				combobox3_.addItem(types3[2]);
			}

			// for the Quad option
			else if (index == ElementLibrary.quad_) {
				String types2[] = { "Plane stress", "Plane strain", "Plate",
						"Shell", "Curved shell" };
				String types3[] = { "Bilinear", "Biquadratic", "Bicubic" };
				combobox2_.removeAllItems();
				combobox3_.removeAllItems();
				combobox2_.addItem(types2[0]);
				combobox2_.addItem(types2[1]);
				combobox2_.addItem(types2[2]);
				combobox2_.addItem(types2[3]);
				combobox2_.addItem(types2[4]);
				combobox3_.addItem(types3[0]);
				combobox3_.addItem(types3[1]);
				combobox3_.addItem(types3[2]);
			}

			// for the Tria option
			else if (index == ElementLibrary.tria_) {
				String types2[] = { "Plane stress", "Plane strain", "Plate",
						"Shell", "Curved shell" };
				String types3[] = { "Linear", "Quadratic" };
				combobox2_.removeAllItems();
				combobox3_.removeAllItems();
				combobox2_.addItem(types2[0]);
				combobox2_.addItem(types2[1]);
				combobox2_.addItem(types2[2]);
				combobox2_.addItem(types2[3]);
				combobox2_.addItem(types2[4]);
				combobox3_.addItem(types3[0]);
				combobox3_.addItem(types3[1]);
			}

			// for the Hexa option
			else if (index == ElementLibrary.hexa_) {
				String types2[] = { "Solid" };
				String types3[] = { "Trilinear", "Triquadratic" };
				combobox2_.removeAllItems();
				combobox3_.removeAllItems();
				combobox2_.addItem(types2[0]);
				combobox3_.addItem(types3[0]);
				combobox3_.addItem(types3[1]);
			}

			// for the Tetra option
			else if (index == ElementLibrary.tetra_) {
				String types2[] = { "Solid" };
				String types3[] = { "Trilinear", "Triquadratic" };
				combobox2_.removeAllItems();
				combobox3_.removeAllItems();
				combobox2_.addItem(types2[0]);
				combobox3_.addItem(types3[0]);
				combobox3_.addItem(types3[1]);
			}
		}

		// set type
		setType();
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// check textfield
				if (checkText(0) == false)
					setDefaultText(textfield1_);
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * If false data has been entered displays message on screen.
	 * 
	 * @param messageType
	 *            The type of message to be displayed (No name given -> 0, Name
	 *            exists -> 1).
	 * @return True if the data entered is correct, False if not.
	 */
	private boolean checkText(int messageType) {

		// boolean value for checking if data entered is correct or not
		boolean isCorrect = true;

		// get the entered text
		String text = textfield1_.getText();

		// No name given
		if (messageType == 0) {

			// check if no name given
			if (text.equals("")) {

				// display message
				JOptionPane.showMessageDialog(ElementLib2.this,
						"No name given!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(ElementLib2.this,
						"Name already exists!", "False data entry", 2);
				isCorrect = false;
			}
		}

		// the data is correct
		return isCorrect;
	}

	/**
	 * Sets default values for textfields.
	 * 
	 */
	private void setDefaultText(JTextField textfield) {

		// The default values for textfields
		String defaultName = "Element1";
		String defaultValue = "0";

		// set to textfield1
		if (textfield.equals(textfield1_))
			textfield1_.setText(defaultName);

		// set to textfield2
		else if (textfield.equals(textfield2_))
			textfield2_.setText(defaultValue);

		// set to all
		else {
			textfield1_.setText(defaultName);
			textfield2_.setText(defaultValue);
		}
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
