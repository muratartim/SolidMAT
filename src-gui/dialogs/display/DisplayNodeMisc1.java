package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;

import boundary.BoundaryCase;

/**
 * Class for Display Nodal Assignments menu.
 * 
 * @author Murat
 * 
 */
public class DisplayNodeMisc1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JComboBox combobox1_, combobox2_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_;

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
	public DisplayNodeMisc1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Show Nodal Assignments", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Options", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Assignment Type", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Boundary case :");
		JLabel label2 = new JLabel("Coordinate system :");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types[] = { "Global", "Local" };
		combobox1_ = new JComboBox(setBoundaryCases());
		combobox2_ = new JComboBox(types);
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(2);
		combobox2_.setEnabled(false);
		combobox1_.setPreferredSize(new Dimension(100, 23));

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Constraints", true);
		radiobutton2_ = new JRadioButton("Springs", false);
		radiobutton3_ = new JRadioButton("Masses", false);
		radiobutton4_ = new JRadioButton("Local axes", false);
		radiobutton5_ = new JRadioButton("Groups", false);

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);
		buttongroup1.add(radiobutton4_);
		buttongroup1.add(radiobutton5_);

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, radiobutton2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton3_, 1, 0, 1, 1);
		Commons.addComponent(panel4, radiobutton4_, 1, 1, 1, 1);
		Commons.addComponent(panel4, radiobutton5_, 2, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

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
		radiobutton4_.addItemListener(this);
		radiobutton5_.addItemListener(this);

		// call visualize
		Commons.visualize(this);
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

	/**
	 * If ok is clicked calls actionOk, if cancel clicked sets dialog unvisible,
	 * if other buttons clicked sets appropriate checkboxes.
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

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// set boundary case to visualizer
		int index = combobox1_.getSelectedIndex();
		Vector<BoundaryCase> bound = new Vector<BoundaryCase>();
		bound.add(owner_.inputData_.getBoundaryCase().get(index));
		owner_.preVis_.setBoundaryCases(bound);

		// set coordinate system to visualizer
		owner_.preVis_.setCoordinateSystem(combobox2_.getSelectedIndex());

		// get node display options of visualizer
		boolean[] nodeOpt = owner_.preVis_.getNodeOptions();

		// set display options
		for (int i = 2; i < nodeOpt.length; i++)
			nodeOpt[i] = false;

		// disable colored options
		owner_.preVis_.disableColoredOptions();

		// set demanded options
		nodeOpt[2] = radiobutton1_.isSelected();
		nodeOpt[3] = radiobutton2_.isSelected();
		nodeOpt[6] = radiobutton3_.isSelected();
		nodeOpt[7] = radiobutton4_.isSelected();
		nodeOpt[10] = radiobutton5_.isSelected();

		// draw
		progressor_.setStatusMessage("Drawing...");
		owner_.drawPre();

		// close progressor
		progressor_.close();

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * If load distribution combobox items are selected, related textfields are
	 * arranged.
	 */
	public void itemStateChanged(ItemEvent event) {

		// radiobutton1 selected
		if (radiobutton1_.isSelected()) {
			combobox1_.setEnabled(true);
			combobox2_.setEnabled(false);
		}

		// radiobutton2 selected
		else if (radiobutton2_.isSelected()) {
			combobox1_.setEnabled(false);
			combobox2_.setEnabled(true);
		}

		// radiobutton3 selected
		else if (radiobutton3_.isSelected()) {
			combobox1_.setEnabled(false);
			combobox2_.setEnabled(true);
		}

		// radiobutton4 selected
		else if (radiobutton4_.isSelected()) {
			combobox1_.setEnabled(false);
			combobox2_.setEnabled(false);
		}

		// radiobutton5 selected
		else if (radiobutton5_.isSelected()) {
			combobox1_.setEnabled(false);
			combobox2_.setEnabled(false);
		}
	}
}
