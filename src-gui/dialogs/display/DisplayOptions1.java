package dialogs.display;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;

/**
 * Class for Display Options1 Display menu.
 * 
 * @author Murat
 * 
 */
public class DisplayOptions1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_, checkbox4_,
			checkbox5_;

	private JButton button1_, button2_, button3_, button4_;

	/** Arrays storing the nodal and element visualization options. */
	protected double[] nodeVizOptions_, elementVizOptions_;

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
	public DisplayOptions1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Display Options", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Nodes", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Elements", Commons.gridbag_);

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("Visible");
		checkbox2_ = new JCheckBox("Labels");
		checkbox3_ = new JCheckBox("Visible");
		checkbox4_ = new JCheckBox("Labels");
		checkbox5_ = new JCheckBox("Extrude");

		// build buttons, set tooltiptext and set font
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");
		button3_ = new JButton("Visualization");
		button4_ = new JButton("Visualization");

		// add components to sub-panels
		Commons.addComponent(panel3, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, checkbox2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, button3_, 2, 0, 1, 1);
		Commons.addComponent(panel4, checkbox3_, 0, 0, 1, 1);
		Commons.addComponent(panel4, checkbox4_, 1, 0, 1, 1);
		Commons.addComponent(panel4, checkbox5_, 2, 0, 1, 1);
		Commons.addComponent(panel4, button4_, 3, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 0, 1, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);

		// call initialize
		initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get node-element display options of visualizer
		boolean[] nodeOpt = owner_.preVis_.getNodeOptions();
		boolean[] elementOpt = owner_.preVis_.getElementOptions();

		// get node-element visualization options of visualizer
		nodeVizOptions_ = owner_.preVis_.getNodeVizOptions();
		elementVizOptions_ = owner_.preVis_.getElementVizOptions();

		// set checkboxes
		checkbox1_.setSelected(nodeOpt[0]);
		checkbox2_.setSelected(nodeOpt[1]);
		checkbox3_.setSelected(elementOpt[0]);
		checkbox4_.setSelected(elementOpt[1]);
		checkbox5_.setSelected(elementOpt[2]);
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

		// visualization button clicked for nodes
		else if (e.getSource().equals(button3_)) {

			// build child dialog and set visible
			DisplayOptions2 dialog = new DisplayOptions2(this, true);
			dialog.setVisible(true);
		}

		// visualization button clicked for elements
		else if (e.getSource().equals(button4_)) {

			// build child dialog and set visible
			DisplayOptions2 dialog = new DisplayOptions2(this, false);
			dialog.setVisible(true);
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
			button3_.setEnabled(false);
			button4_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);
			button3_.setEnabled(true);
			button4_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// get node-element display options of visualizer
		boolean[] nodeOpt = owner_.preVis_.getNodeOptions();
		boolean[] elementOpt = owner_.preVis_.getElementOptions();

		// set display options
		nodeOpt[0] = checkbox1_.isSelected();
		nodeOpt[1] = checkbox2_.isSelected();
		elementOpt[0] = checkbox3_.isSelected();
		elementOpt[1] = checkbox4_.isSelected();
		elementOpt[2] = checkbox5_.isSelected();

		// set node-element visualization options
		owner_.preVis_.setNodeVizOptions(nodeVizOptions_);
		owner_.preVis_.setElementVizOptions(elementVizOptions_);

		// draw
		progressor_.setStatusMessage("Drawing...");
		owner_.drawPre();

		// close progressor
		progressor_.close();

		// set dialog unvisible
		setVisible(false);
	}
}
