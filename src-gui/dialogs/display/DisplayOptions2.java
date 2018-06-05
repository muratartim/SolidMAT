package dialogs.display;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JSlider;

import main.Commons;
// import main.ImageHandler;

/**
 * Class for Display Options2 Display menu.
 * 
 * @author Murat
 * 
 */
public class DisplayOptions2 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JSlider slider1_;

	private JButton button3_;

	private Color color_;

	/**
	 * Used for determining if node or element button clicked from mother
	 * dialog.
	 */
	private boolean node_;

	/** The owner frame of this dialog. */
	private DisplayOptions1 owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 * @param node
	 *            Boolean value for determining if node or element button
	 *            clicked from mother dialog.
	 */
	public DisplayOptions2(DisplayOptions1 owner, boolean node) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Visualization Options", true);
		owner_ = owner;
		node_ = node;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Transparency and Color",
				Commons.gridbag_);

		// build slider, turn on labels at major tick marks.
		slider1_ = new JSlider();
		slider1_.setMajorTickSpacing(25);
		slider1_.setMinorTickSpacing(5);
		slider1_.setPaintTicks(true);
		slider1_.setPaintLabels(true);

		// build buttons
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");
		button3_ = new JButton("Color");

		// add components to sub-panels
		Commons.addComponent(panel3, slider1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, button3_, 1, 0, 1, 1);

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
		button3_.addActionListener(this);

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

		// create array for vizsualization options
		double[] vizOpt = new double[4];

		// dialog opened for node visualization
		if (node_)
			vizOpt = owner_.nodeVizOptions_;

		// dialog opened for element visualization
		else
			vizOpt = owner_.elementVizOptions_;

		// get color values and create color
		Double red = vizOpt[0] * 255.0;
		Double green = vizOpt[1] * 255.0;
		Double blue = vizOpt[2] * 255.0;
		color_ = new Color(red.intValue(), green.intValue(), blue.intValue());

		// set transparency
		Double val = 100.0 * vizOpt[3];
		slider1_.setValue(val.intValue());
	}

	/**
	 * If ok is clicked calls actionOk, if cancel clicked sets dialog unvisible,
	 * if other buttons clicked sets appropriate checkboxes.
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

		// color button clicked
		else if (e.getSource().equals(button3_)) {

			// show color chooser and get selected color
			Color c = JColorChooser.showDialog(this, "Choose Color", color_);

			// check if any selected
			if (c != null)
				color_ = c;
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// create array for storing visualization options
		double[] opt = new double[4];

		// get color properties
		opt[0] = color_.getRed() / 255.0;
		opt[1] = color_.getGreen() / 255.0;
		opt[2] = color_.getBlue() / 255.0;

		// get opacity
		opt[3] = slider1_.getValue() / 100.0;

		// dialog opened from node button
		if (node_)
			owner_.nodeVizOptions_ = opt;

		// dialog opened from element button
		else
			owner_.elementVizOptions_ = opt;

		// close dialog
		setVisible(false);
	}
}
