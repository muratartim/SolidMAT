package dialogs.model;


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
import javax.swing.WindowConstants;

import node.Node;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DVec;

/**
 * Class for Add Node Model menu.
 * 
 * @author Murat
 * 
 */
public class AddNode1 extends JDialog implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	private JButton button1_, button2_;

	/** The progress monitor of processses that take place. */
	private Progressor progressor_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public AddNode1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Add Node", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Coordinates", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Node coordinates :");
		JLabel label2 = new JLabel("X coordinate :");
		JLabel label3 = new JLabel("Y coordinate :");
		JLabel label4 = new JLabel("Z coordinate :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(100, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, label3, 2, 0, 1, 1);
		Commons.addComponent(panel3, label4, 3, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel3, textfield3_, 2, 1, 1, 1);
		Commons.addComponent(panel3, textfield4_, 3, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		textfield1_.addFocusListener(this);
		textfield2_.addFocusListener(this);
		textfield3_.addFocusListener(this);
		textfield4_.addFocusListener(this);

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// set defaults values to textfields
		setDefaultText();
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					actionOk();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// cancel button clicked
		else if (e.getSource().equals(button2_)) {

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * Sets the dialog still for displaying progressor.
	 * 
	 * @param arg0
	 *            True if still, False if not.
	 */
	private void setStill(boolean arg0) {

		// still
		if (arg0) {

			// disable buttons
			button1_.setEnabled(false);
			button2_.setEnabled(false);

			// disable textfields
			textfield1_.setEnabled(false);
			textfield2_.setEnabled(false);
			textfield3_.setEnabled(false);
			textfield4_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);

			// disable textfields
			textfield1_.setEnabled(true);
			textfield2_.setEnabled(true);
			textfield3_.setEnabled(true);
			textfield4_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check node
		progressor_.setStatusMessage("Checking data...");
		if (checkNode()) {

			// get coordinates
			double x = Double.parseDouble(textfield2_.getText());
			double y = Double.parseDouble(textfield3_.getText());
			double z = Double.parseDouble(textfield4_.getText());

			// create node
			Node node = new Node(x, y, z);

			// add to structure
			owner_.structure_.addNode(node);

			// draw
			progressor_.setStatusMessage("Drawing...");
			owner_.drawPre();

			// close progressor
			progressor_.close();

			// close dialog
			setVisible(false);
		}

		// node exists
		else {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(AddNode1.this,
					"Node already exists!", "False data entry", 2);
		}
	}

	/**
	 * Checks whether any node of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if no node exits at the given coordinates, False vice versa.
	 */
	private boolean checkNode() {

		// get coordinates
		DVec pos = new DVec(3);
		pos.set(0, Double.parseDouble(textfield2_.getText()));
		pos.set(1, Double.parseDouble(textfield3_.getText()));
		pos.set(2, Double.parseDouble(textfield4_.getText()));

		// loop over nodes of structure
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++) {

			// get nodal position vector
			DVec pos1 = owner_.structure_.getNode(i).getPosition();

			// check coordinates
			if (pos.subtract(pos1).l2Norm() <= tolerance_)
				return false;
		}

		// no node exists at the same coordinates
		return true;
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// get source
				JTextField tfield = (JTextField) e.getSource();

				// check textfield
				if (checkText(tfield) == false)
					setDefaultText();
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

		// check textfield1
		if (textfield.equals(textfield1_)) {

			// eliminate spaces
			String elText = "";
			for (int i = 0; i < text.length(); i++) {
				char c = text.charAt(i);
				if (c != " ".charAt(0))
					elText += c;
			}

			// seperate components
			String[] comp = elText.split(",", 3);

			// check for non-numeric values
			try {

				// convert text to double value
				@SuppressWarnings("unused")
				double value;
				for (int i = 0; i < comp.length; i++)
					value = Double.parseDouble(comp[i]);

				// set other textfields
				textfield2_.setText(comp[0]);
				textfield3_.setText(comp[1]);
				textfield4_.setText(comp[2]);
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(AddNode1.this, "Illegal value!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// textfield2 lost focus
		else if (textfield.equals(textfield2_)) {

			// check for non-numeric values
			try {

				// convert text to double value
				double val1 = Double.parseDouble(text);

				// set value to textfield1
				double val2 = Double.parseDouble(textfield3_.getText());
				double val3 = Double.parseDouble(textfield4_.getText());
				textfield1_.setText(Double.toString(val1) + ","
						+ Double.toString(val2) + "," + Double.toString(val3));
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(AddNode1.this, "Illegal value!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// textfield3 lost focus
		else if (textfield.equals(textfield3_)) {

			// check for non-numeric values
			try {

				// convert text to double value
				double val2 = Double.parseDouble(text);

				// set value to textfield1
				double val1 = Double.parseDouble(textfield2_.getText());
				double val3 = Double.parseDouble(textfield4_.getText());
				textfield1_.setText(Double.toString(val1) + ","
						+ Double.toString(val2) + "," + Double.toString(val3));
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(AddNode1.this, "Illegal value!",
						"False data entry", 2);
				isCorrect = false;
			}
		}

		// textfield4 lost focus
		else if (textfield.equals(textfield4_)) {

			// check for non-numeric values
			try {

				// convert text to double value
				double val3 = Double.parseDouble(text);

				// set value to textfield1
				double val1 = Double.parseDouble(textfield2_.getText());
				double val2 = Double.parseDouble(textfield3_.getText());
				textfield1_.setText(Double.toString(val1) + ","
						+ Double.toString(val2) + "," + Double.toString(val3));
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(AddNode1.this, "Illegal value!",
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
	 */
	private void setDefaultText() {

		// The default values for textfields
		String defVal = Double.toString(0.0);

		// set to textfields
		textfield1_.setText(defVal + "," + defVal + "," + defVal);
		textfield2_.setText(defVal);
		textfield3_.setText(defVal);
		textfield4_.setText(defVal);
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
