package dialogs.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;

/**
 * Class for Format Menu Numbers View menu.
 * 
 * @author Murat
 * 
 */
public class MenuFormat extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private JComboBox combobox1_;

	private JSpinner spinner1_;

	private JButton button1_, button2_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public MenuFormat(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Format Numbers", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Format :");
		JLabel label2 = new JLabel("Digits :");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Auto", "Real", "Scientific" };
		combobox1_ = new JComboBox(types1);
		combobox1_.setMaximumRowCount(3);
		combobox1_.setPreferredSize(new Dimension(100, 23));

		// build spinner
		SpinnerNumberModel spinnerModel1 = new SpinnerNumberModel();
		spinnerModel1.setMinimum(0);
		spinnerModel1.setMaximum(6);
		spinner1_ = new JSpinner(spinnerModel1);
		spinner1_.setEnabled(false);

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, spinner1_, 1, 1, 1, 1);

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
		combobox1_.addItemListener(this);

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

		// get scaling factors of pre/post visualizer
		String format = owner_.formatter_.getFormat();
		int digits = owner_.formatter_.getDigits();

		// automatic format
		if (format.equalsIgnoreCase("a")) {
			combobox1_.setSelectedIndex(0);
			spinner1_.setValue(4);
			spinner1_.setEnabled(false);
		}

		// user defined format
		else {
			if (format.equalsIgnoreCase("f"))
				combobox1_.setSelectedIndex(1);
			else
				combobox1_.setSelectedIndex(2);
			spinner1_.setValue(digits);
		}
	}

	/**
	 * If ok is clicked calls actionOk, if cancel clicked sets dialog unvisible,
	 * if other buttons clicked sets appropriate checkboxes.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_))
			actionOk();

		// cancel button clicked
		else if (e.getSource().equals(button2_))
			setVisible(false);
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// set format
		if (combobox1_.getSelectedIndex() == 0)
			owner_.formatter_.setFormat("a");
		else if (combobox1_.getSelectedIndex() == 1)
			owner_.formatter_.setFormat("f");
		else
			owner_.formatter_.setFormat("E");

		// set digits
		int digits = (Integer) spinner1_.getValue();
		owner_.formatter_.setDigits(digits);

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// automatic formatting
			if (combobox1_.getSelectedIndex() == 0) {
				spinner1_.setValue(4);
				spinner1_.setEnabled(false);
			}

			// user defined formatting
			else
				spinner1_.setEnabled(true);
		}
	}
}
