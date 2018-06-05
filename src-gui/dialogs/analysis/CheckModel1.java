package dialogs.analysis;

import element.Element;
import element.ElementLibrary;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;

import node.Node;

import main.Commons; // import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DVec;

/**
 * Class for Check Model model menu.
 * 
 * @author Murat
 * 
 */
public class CheckModel1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextArea textArea1_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** Vector for storing nodes to be moved. */
	private Vector<Node> nodes1_ = new Vector<Node>();

	/** Vector for storing elements to be moved. */
	private Vector<Element> elements1_ = new Vector<Element>();

	/** Static variable for new lines in the text area. */
	private final static String newline_ = "\n";

	private JButton button1_, button2_;

	/** The progress monitor of processses that take place. */
	private Progressor progressor_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners, calls initialize and visualize.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public CheckModel1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Check Model", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build panels
		JPanel panel1 = Commons.getPanel("", Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.gridbag_);

		// build text area
		textArea1_ = new JTextArea(8, 40);
		textArea1_.setEditable(false);
		textArea1_.setLineWrap(true);
		textArea1_.setWrapStyleWord(true);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(textArea1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// build buttons
		button1_ = new JButton("Check");
		button2_ = new JButton("OK");

		// build labels
		JLabel label1 = new JLabel(" ");
		JLabel label2 = new JLabel(" ");
		JLabel label3 = new JLabel(" ");
		JLabel label4 = new JLabel(" ");
		JLabel label5 = new JLabel(" ");

		// add components to panels
		Commons.addComponent(panel1, scrollpane1, 0, 0, 1, 7);
		Commons.addComponent(panel1, label1, 0, 1, 1, 1);
		Commons.addComponent(panel1, label2, 1, 1, 1, 1);
		Commons.addComponent(panel1, label3, 2, 1, 1, 1);
		Commons.addComponent(panel1, label4, 3, 1, 1, 1);
		Commons.addComponent(panel1, label5, 4, 1, 1, 1);
		Commons.addComponent(panel1, button1_, 5, 1, 1, 1);
		Commons.addComponent(panel1, button2_, 6, 1, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel2, panel1, 0, 0, 1, 1);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * If add or modify button clicked, builds child dialog. If delete button
	 * clicked, calls actionDelete. If ok button clicked, calls actionOk. If
	 * cancel button clicked sets dialog unvisible.
	 */
	public void actionPerformed(ActionEvent e) {

		// check button clicked
		if (e.getSource().equals(button1_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					check();
					return null;
				}
			};

			// display progressor and still frame
			setStill(true);
			progressor_ = new Progressor(this);

			// start task
			worker.start();
		}

		// ok button clicked
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
	 * Checks model and writes information to text area.
	 * 
	 */
	private void check() {

		// clear text area
		textArea1_.setText("");

		// structure information
		textArea1_.append("---------------------------------------" + newline_);
		textArea1_.append("Structure Information" + newline_);
		textArea1_.append("---------------------------------------" + newline_);
		int nn = owner_.structure_.getNumberOfNodes();
		textArea1_.append("# of nodes : " + nn + newline_);
		int ne = owner_.structure_.getNumberOfElements();
		textArea1_.append("# of elements : " + ne + newline_);

		// meshing
		progressor_.setStatusMessage("Checking geometry and meshing...");
		textArea1_.append("---------------------------------------" + newline_);
		textArea1_.append("Geometry and Meshing" + newline_);
		textArea1_.append("---------------------------------------" + newline_);
		int dupn = duplicateNodes();
		textArea1_.append("# of duplicate nodes : " + dupn + newline_);
		if (dupn != 0) {

			// loop over duplicate nodes
			textArea1_.append("Node IDs : ");
			for (int i = 0; i < nodes1_.size(); i++) {

				// get index of node
				int index = owner_.structure_.indexOfNode(nodes1_.get(i));
				if (i != 0)
					textArea1_.append(", ");
				textArea1_.append(Integer.toString(index));
			}
		}
		int dupe = duplicateElements();
		textArea1_.append("# of duplicate elements : " + dupe + newline_);
		if (dupe != 0) {

			// loop over duplicate elements
			textArea1_.append("Element IDs : ");
			for (int i = 0; i < elements1_.size(); i++) {

				// get index of element
				int index = owner_.structure_.indexOfElement(elements1_.get(i));
				if (i != 0)
					textArea1_.append(", ");
				textArea1_.append(Integer.toString(index));
			}
		}
		int unn = unusedNodes();
		textArea1_.append("# of unused nodes : " + unn + newline_);
		if (unn != 0) {

			// loop over duplicate nodes
			textArea1_.append("Node IDs : ");
			for (int i = 0; i < nodes1_.size(); i++) {

				// get index of node
				int index = owner_.structure_.indexOfNode(nodes1_.get(i));
				if (i != 0)
					textArea1_.append(", ");
				textArea1_.append(Integer.toString(index));
			}
		}

		// assignments
		progressor_.setStatusMessage("Checking assignments...");
		textArea1_.append("---------------------------------------" + newline_);
		textArea1_.append("Assignments" + newline_);
		textArea1_.append("---------------------------------------" + newline_);
		int nuae1 = materialAssignments();
		textArea1_.append("# of elements without material : " + nuae1
				+ newline_);
		if (dupe != 0) {

			// loop over duplicate elements
			textArea1_.append("Element IDs : ");
			for (int i = 0; i < elements1_.size(); i++) {

				// get index of element
				int index = owner_.structure_.indexOfElement(elements1_.get(i));
				if (i != 0)
					textArea1_.append(", ");
				textArea1_.append(Integer.toString(index));
			}
		}
		int nuae2 = sectionAssignments();
		textArea1_
				.append("# of elements without section : " + nuae2 + newline_);
		if (dupe != 0) {

			// loop over duplicate elements
			textArea1_.append("Element IDs : ");
			for (int i = 0; i < elements1_.size(); i++) {

				// get index of element
				int index = owner_.structure_.indexOfElement(elements1_.get(i));
				if (i != 0)
					textArea1_.append(", ");
				textArea1_.append(Integer.toString(index));
			}
		}

		// close progressor
		progressor_.close();
		setStill(false);
	}

	/**
	 * Determines the number of duplicate nodes.
	 * 
	 * @return The number of duplicate nodes.
	 */
	private int duplicateNodes() {

		// loop over nodes
		for (int i = 0; i < owner_.structure_.getNumberOfNodes() - 1; i++) {

			// get position of base node
			Node node1 = owner_.structure_.getNode(i);
			DVec pos1 = node1.getPosition();

			// loop over other nodes
			for (int j = i + 1; j < owner_.structure_.getNumberOfNodes(); j++) {

				// get target node and its position
				Node node2 = owner_.structure_.getNode(j);
				DVec pos2 = node2.getPosition();

				// check positions
				DVec dif = pos1.subtract(pos2);
				if (dif.l2Norm() <= tolerance_) {
					if (nodes1_.contains(node2) == false) {

						// add to would be removed nodes list
						nodes1_.add(node2);
					}
				}
			}
		}

		// return number of duplicate nodes
		return nodes1_.size();
	}

	/**
	 * Determines the number of duplicate elements.
	 * 
	 * @return The number of duplicate elements.
	 */
	private int duplicateElements() {

		// loop over elements
		for (int i = 0; i < owner_.structure_.getNumberOfElements() - 1; i++) {

			// get type and nodes of base element
			int type1 = owner_.structure_.getElement(i).getType();
			Node[] nodes = owner_.structure_.getElement(i).getNodes();

			// loop over other elements
			for (int j = i + 1; j < owner_.structure_.getNumberOfElements(); j++) {

				// get target element and its type
				Element e = owner_.structure_.getElement(j);
				int type2 = e.getType();

				// check for same type
				if (type1 == type2) {

					// check for identical nodes
					int m = 0;
					for (int k = 0; k < nodes.length; k++) {
						for (int l = 0; l < nodes.length; l++) {
							if (nodes[k].equals(e.getNodes()[l]))
								m++;
						}
					}

					// identical nodes
					if (m == nodes.length) {
						if (elements1_.contains(e) == false) {

							// add to would be removed elements list
							elements1_.add(e);
						}
					}
				}
			}
		}

		// return number of duplicate elements
		return elements1_.size();
	}

	/**
	 * Determines the number of unused nodes.
	 * 
	 * @return The number of unused nodes.
	 */
	private int unusedNodes() {

		// loop over nodes
		nodes1_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++) {

			// get node to be checked
			Node node = owner_.structure_.getNode(i);
			int m = 0;

			// loop over elements
			for (int j = 0; j < owner_.structure_.getNumberOfElements(); j++) {

				// get element and its nodes
				Element e = owner_.structure_.getElement(j);
				Node[] nodes = e.getNodes();

				// loop over nodes of element
				for (int k = 0; k < nodes.length; k++) {

					// element contains node
					if (nodes[k].equals(node)) {
						m++;
						break;
					}
				}

				// check node's connection
				if (m > 0)
					break;
			}

			// check node's connection
			if (m == 0)
				nodes1_.add(node);
		}

		// return number of unused nodes
		return nodes1_.size();
	}

	/**
	 * Determines the number of elements having no material assigned.
	 * 
	 * @return The number of elements having no material assigned.
	 */
	private int materialAssignments() {

		// loop over elements
		elements1_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// check if it has material
			if (e.getMaterial() == null)
				elements1_.add(e);
		}

		// return unassigned elements
		return elements1_.size();
	}

	/**
	 * Determines the number of elements having no section assigned.
	 * 
	 * @return The number of elements having no section assigned.
	 */
	private int sectionAssignments() {

		// loop over elements
		elements1_.clear();
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// check dimension
			if (e.getDimension() != ElementLibrary.threeDimensional_) {

				// check if it has section
				if (e.getSection() == null)
					elements1_.add(e);
			}
		}

		// return unassigned elements
		return elements1_.size();
	}
}
