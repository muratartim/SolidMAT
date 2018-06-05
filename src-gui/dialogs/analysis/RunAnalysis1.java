package dialogs.analysis;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel; // import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;

import analysis.Analysis;

/**
 * Class for Run analysis menu.
 * 
 * @author Murat
 * 
 */
public class RunAnalysis1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	protected JList list1_;

	protected DefaultListModel listModel1_;

	/** The owner frame of this dialog. */
	protected SolidMAT owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners, calls initialize and visualize.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public RunAnalysis1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Run Analysis", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Cases", Commons.gridbag_);

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(11);
		list1_.setFixedCellWidth(200);
		list1_.setFixedCellHeight(15);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// build buttons
		JButton button1 = new JButton("Run");
		JButton button2 = new JButton("Close");

		// add components to panels
		Commons.addComponent(panel3, scrollpane1, 0, 1, 1, 1);

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

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// copy names to list
		Vector<Analysis> object = owner_.inputData_.getAnalysis();
		for (int i = 0; i < object.size(); i++) {
			listModel1_.addElement(object.get(i).getName());
		}
	}

	/**
	 * Performs related action.
	 */
	public void actionPerformed(ActionEvent e) {

		// store action command in a string
		String command = e.getActionCommand();

		// run button clicked
		if (command.equalsIgnoreCase("Run")) {

			// check if any item selected in list
			if (list1_.isSelectionEmpty() == false) {

				// Schedule a job for the event-dispatching thread:
				// creating and showing related GUI.
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						// get selected analysis
						int index = list1_.getSelectedIndex();
						Analysis a = owner_.inputData_.getAnalysis().get(index);

						// set structure to analysis
						a.setStructure(owner_.structure_);

						// set path to analysis
						a.setPath(owner_.path_);

						// linear static analysis
						if (a.getType() == Analysis.linearStatic_) {
							LSProgress1 run = new LSProgress1(
									RunAnalysis1.this, a);
							run.setVisible(true);
						}

						// modal analysis
						else if (a.getType() == Analysis.modal_) {
							MProgress1 run = new MProgress1(RunAnalysis1.this,
									a);
							run.setVisible(true);
						}

						// linear dynamic analysis
						else if (a.getType() == Analysis.linearTransient_) {
							LTProgress1 run = new LTProgress1(
									RunAnalysis1.this, a);
							run.setVisible(true);
						}

						// linear buckling analysis
						else if (a.getType() == Analysis.linearBuckling_) {
							LBProgress1 run = new LBProgress1(
									RunAnalysis1.this, a);
							run.setVisible(true);
						}
					}
				});
			}
		}

		// close button clicked
		else if (command.equalsIgnoreCase("Close"))
			setVisible(false);
	}
}
