package dialogs.model;

import data.Group;
import element.*;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
 * Class for Edit Element Model menu.
 * 
 * @author Murat
 * 
 */
public class EditElement1 extends JDialog implements ActionListener,
		FocusListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_;

	private JComboBox combobox1_, combobox2_;

	private JLabel label4_, label5_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** The element to be editted and the new element for replacement. */
	private Element element_, element1_;

	/** Vector for storing the nodes of element. */
	private Vector<Node> nodes_ = new Vector<Node>();

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
	public EditElement1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Edit Element", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Element", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Properties", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Element ID :");
		JLabel label2 = new JLabel("Mechanics :");
		JLabel label3 = new JLabel("Interpolation :");
		label4_ = new JLabel("Radius 1 :");
		label5_ = new JLabel("Radius 2 :");
		label4_.setVisible(false);
		label5_.setVisible(false);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
		textfield1_.setPreferredSize(new Dimension(110, 20));
		textfield2_.setPreferredSize(new Dimension(100, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// build list for combo boxes, build combo boxes and set maximum visible
		// row number. Then set font for items
		String types1[] = { "Truss", "Beam", "Curved beam", "Planar truss",
				"Planar beam" };
		String types2[] = { "Linear", "Quadratic", "Cubic" };
		combobox1_ = new JComboBox(types1);
		combobox2_ = new JComboBox(types2);
		combobox1_.setMaximumRowCount(3);
		combobox2_.setMaximumRowCount(3);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, label4_, 2, 0, 1, 1);
		Commons.addComponent(panel4, label5_, 3, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, combobox2_, 1, 1, 1, 1);
		Commons.addComponent(panel4, textfield2_, 2, 1, 1, 1);
		Commons.addComponent(panel4, textfield3_, 3, 1, 1, 1);

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
		textfield1_.addFocusListener(this);
		combobox1_.addItemListener(this);

		// visualize
		Commons.visualize(this);
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

			// disable texfields
			textfield1_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);

			// enable texfields
			textfield1_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check id
		progressor_.setStatusMessage("Checking data...");
		if (checkId() && checkText()) {

			// create element
			if (createElement()) {

				// draw
				progressor_.setStatusMessage("Drawing...");
				owner_.drawPre();

				// close progressor
				progressor_.close();

				// set dialog unvisible
				setVisible(false);
			}
		}
	}

	/**
	 * Creates and replaces the demanded element with the old one.
	 * 
	 */
	private boolean createElement() {

		// get combobox selections
		int mech = combobox1_.getSelectedIndex();
		int interp = combobox2_.getSelectedIndex();

		// get geometry of element
		int geo = 0;

		// two dimensional elements
		if (element_.getDimension() == ElementLibrary.twoDimensional_) {

			// get two dimensional element
			Element2D e2D = (Element2D) element_;

			// get geometry
			if (e2D.getGeometry() == Element2D.quadrangular_)
				geo = ElementLibrary.quad_;
			else if (e2D.getGeometry() == Element2D.triangular_)
				geo = ElementLibrary.tria_;
		}

		// three dimensional elements
		else if (element_.getDimension() == ElementLibrary.threeDimensional_) {

			// get three dimensional element
			Element3D e3D = (Element3D) element_;

			// get geometry
			if (e3D.getGeometry() == Element3D.hexahedral_)
				geo = ElementLibrary.hexa_;
			else if (e3D.getGeometry() == Element3D.tetrahedral_)
				geo = ElementLibrary.tetra_;
		}

		// get type of new element
		int type = 0;
		try {
			ElementLibrary lib = new ElementLibrary("new", geo, mech, interp);
			type = lib.getType();
		} catch (Exception excep) {

			// get error message
			String message = excep.getLocalizedMessage();

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(EditElement1.this, message,
					"False data entry", 2);
			return false;
		}

		// create internal nodes
		createInternalNodes(geo, interp);

		// element0
		if (type == ElementLibrary.element0_)
			element1_ = new Element0(nodes_.get(0), nodes_.get(1));

		// element1
		else if (type == ElementLibrary.element1_)
			element1_ = new Element1(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element2
		else if (type == ElementLibrary.element2_)
			element1_ = new Element2(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element3
		else if (type == ElementLibrary.element3_)
			element1_ = new Element3(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element4
		else if (type == ElementLibrary.element4_)
			element1_ = new Element4(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element5
		else if (type == ElementLibrary.element5_)
			element1_ = new Element5(nodes_.get(0), nodes_.get(1), nodes_
					.get(2));

		// element6
		else if (type == ElementLibrary.element6_)
			element1_ = new Element6(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element7
		else if (type == ElementLibrary.element7_)
			element1_ = new Element7(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9),
					nodes_.get(10), nodes_.get(11));

		// element8
		else if (type == ElementLibrary.element8_)
			element1_ = new Element8(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element9
		else if (type == ElementLibrary.element9_)
			element1_ = new Element9(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element10
		else if (type == ElementLibrary.element10_)
			element1_ = new Element10(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9),
					nodes_.get(10), nodes_.get(11));

		// element11
		else if (type == ElementLibrary.element11_)
			element1_ = new Element11(nodes_.get(0), nodes_.get(1), nodes_
					.get(2));

		// element12
		else if (type == ElementLibrary.element12_)
			element1_ = new Element12(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element13
		else if (type == ElementLibrary.element13_)
			element1_ = new Element13(nodes_.get(0), nodes_.get(1));

		// element14
		else if (type == ElementLibrary.element14_)
			element1_ = new Element14(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element15
		else if (type == ElementLibrary.element15_)
			element1_ = new Element15(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element16
		else if (type == ElementLibrary.element16_)
			element1_ = new Element16(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element17
		else if (type == ElementLibrary.element17_)
			element1_ = new Element17(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element18
		else if (type == ElementLibrary.element18_)
			element1_ = new Element18(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element19
		else if (type == ElementLibrary.element19_)
			element1_ = new Element19(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element20
		else if (type == ElementLibrary.element20_)
			element1_ = new Element20(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element21
		else if (type == ElementLibrary.element21_)
			element1_ = new Element21(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element22
		else if (type == ElementLibrary.element22_)
			element1_ = new Element22(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element23
		else if (type == ElementLibrary.element23_)
			element1_ = new Element23(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9),
					nodes_.get(10), nodes_.get(11), nodes_.get(12), nodes_
							.get(13), nodes_.get(14), nodes_.get(15), nodes_
							.get(16), nodes_.get(17), nodes_.get(18), nodes_
							.get(19));

		// element24
		else if (type == ElementLibrary.element24_)
			element1_ = new Element24(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element25
		else if (type == ElementLibrary.element25_)
			element1_ = new Element25(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element26
		else if (type == ElementLibrary.element26_)
			element1_ = new Element26(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element27
		else if (type == ElementLibrary.element27_)
			element1_ = new Element27(nodes_.get(0), nodes_.get(1));

		// element28
		else if (type == ElementLibrary.element28_)
			element1_ = new Element28(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element29
		else if (type == ElementLibrary.element29_)
			element1_ = new Element29(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element30
		else if (type == ElementLibrary.element30_)
			element1_ = new Element30(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element31
		else if (type == ElementLibrary.element31_)
			element1_ = new Element31(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9));

		// element32
		else if (type == ElementLibrary.element32_)
			element1_ = new Element32(nodes_.get(0), nodes_.get(1));

		// element33
		else if (type == ElementLibrary.element33_)
			element1_ = new Element33(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element34
		else if (type == ElementLibrary.element34_)
			element1_ = new Element34(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element35
		else if (type == ElementLibrary.element35_)
			element1_ = new Element35(nodes_.get(0), nodes_.get(1));

		// element36
		else if (type == ElementLibrary.element36_)
			element1_ = new Element36(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element37
		else if (type == ElementLibrary.element37_)
			element1_ = new Element37(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element38
		else if (type == ElementLibrary.element38_)
			element1_ = new Element38(nodes_.get(0), nodes_.get(1));

		// pass element properties to new element
		passElementProperties();

		// replace the old element in groups
		replaceInGroups();

		// replace the old element with the new one in the structure
		int index = owner_.structure_.indexOfElement(element_);
		owner_.structure_.setElement(index, element1_);

		// remove old element's nodes from structure and groups
		removeNodes();
		return true;
	}

	/**
	 * Removes nodes of old element which are not connected to any other element
	 * from structure and groups.
	 * 
	 */
	private void removeNodes() {

		// loop over nodes of old element
		for (int i = 0; i < element_.getNodes().length; i++) {

			// get node
			Node node = element_.getNodes()[i];

			// loop over elements of structure
			int m = 0;
			for (int j = 0; j < owner_.structure_.getNumberOfElements(); j++) {

				// get nodes of element
				Node[] nodes = owner_.structure_.getElement(j).getNodes();

				// loop over nodes of element
				for (int k = 0; k < nodes.length; k++) {
					if (node.equals(nodes[k])) {
						m++;
						break;
					}
				}
				if (m > 0)
					break;
			}

			// remove node if it is not connected
			if (m == 0) {

				// remove from structure
				int index = owner_.structure_.indexOfNode(node);
				owner_.structure_.removeNode(index);

				// remove from groups
				for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

					// get group
					Group group = owner_.inputData_.getGroup().get(j);

					// check if group contains node
					if (group.containsNode(node))
						group.removeNode(node);
				}
			}
		}
	}

	/**
	 * Passes element properties to newly created element.
	 * 
	 */
	private void passElementProperties() {

		// material
		if (element_.getMaterial() != null)
			element1_.setMaterial(element_.getMaterial());

		// sections
		try {
			if (element_.getSection() != null)
				element1_.setSection(element_.getSection());
		} catch (Exception excep) {
		}

		// mechanical loads
		try {
			if (element_.getAllMechLoads() != null)
				element1_.setMechLoads(element_.getAllMechLoads());
		} catch (Exception excep) {
		}

		// temperature loads
		element1_.setTempLoads(element_.getAllTempLoads());

		// springs
		try {
			if (element_.getSprings() != null)
				element1_.setSprings(element_.getSprings());
		} catch (Exception excep) {
		}

		// masses
		try {
			if (element_.getAdditionalMasses() != null)
				element1_.setMasses(element_.getAdditionalMasses());
		} catch (Exception excep) {
		}

		// local axes
		if (element_.getDimension() == ElementLibrary.oneDimensional_) {

			// get 1D elements
			Element1D e1D1 = (Element1D) element_;
			Element1D e1D2 = (Element1D) element1_;

			// set local axes
			if (e1D1.getLocalAxis() != null)
				e1D2.setLocalAxis(e1D1.getLocalAxis());
		}

		// radii of curvatures
		int typ = element1_.getType();

		// curved shells
		if (typ == ElementLibrary.element19_
				|| typ == ElementLibrary.element20_
				|| typ == ElementLibrary.element21_) {
			double r1 = Double.parseDouble(textfield2_.getText());
			double r2 = Double.parseDouble(textfield3_.getText());
			double[] param = { r1, r2 };
			element1_.setParameters(param);
		}

		// curved beams
		if (typ == ElementLibrary.element27_
				|| typ == ElementLibrary.element28_
				|| typ == ElementLibrary.element29_) {
			double r1 = Double.parseDouble(textfield2_.getText());
			double[] param = { r1 };
			element1_.setParameters(param);
		}
	}

	/**
	 * Replaces demanded element from related groups.
	 * 
	 */
	private void replaceInGroups() {

		// loop over groups
		for (int i = 0; i < owner_.inputData_.getGroup().size(); i++) {

			// get group
			Group group = owner_.inputData_.getGroup().get(i);

			// check if group contains element
			if (group.containsElement(element_)) {

				// replace the old element with the new one
				int index = group.indexOfElement(element_);
				group.setElement(index, element1_);
			}
		}
	}

	/**
	 * Creates internal nodes of element.
	 * 
	 */
	private void createInternalNodes(int geo, int interp) {

		// create internal element nodes
		Node[] nodes = new Node[0];
		DVec[] posInt = new DVec[0];

		// get element nodes
		Node[] elNodes = element_.getNodes();
		nodes_.clear();

		// one dimensional elements
		if (geo == ElementLibrary.line_) {

			// set end nodes
			nodes_.add(elNodes[0]);
			nodes_.add(elNodes[elNodes.length - 1]);

			// get end nodes' position vectors
			DVec posI = nodes_.get(0).getPosition();
			DVec posJ = nodes_.get(1).getPosition();
			DVec posDif = posJ.subtract(posI);

			// quadratic interpolation
			if (interp == ElementLibrary.quadratic_) {

				// compute positions of internal nodes
				posInt = new DVec[1];
				posInt[0] = posI.add(posJ).scale(0.5);
			}

			// cubic interpolation
			else if (interp == ElementLibrary.cubic_) {

				// compute positions of internal nodes
				posInt = new DVec[2];
				posInt[0] = posI.add(posDif.scale(1.0 / 3.0));
				posInt[1] = posI.add(posDif.scale(2.0 / 3.0));
			}
		}

		// quad elements
		else if (geo == ElementLibrary.quad_) {

			// set corner nodes
			nodes_.add(elNodes[0]);
			nodes_.add(elNodes[1]);
			nodes_.add(elNodes[2]);
			nodes_.add(elNodes[3]);

			// get corner nodes' position vectors
			DVec pos0 = nodes_.get(0).getPosition();
			DVec pos1 = nodes_.get(1).getPosition();
			DVec pos2 = nodes_.get(2).getPosition();
			DVec pos3 = nodes_.get(3).getPosition();

			// quadratic interpolation
			if (interp == ElementLibrary.quadratic_) {

				// compute positions of internal nodes
				posInt = new DVec[4];
				posInt[0] = pos0.add(pos1).scale(0.5);
				posInt[1] = pos1.add(pos2).scale(0.5);
				posInt[2] = pos2.add(pos3).scale(0.5);
				posInt[3] = pos3.add(pos0).scale(0.5);
			}

			// cubic interpolation
			else if (interp == ElementLibrary.cubic_) {

				// compute positions of internal nodes
				posInt = new DVec[8];
				posInt[0] = pos0.add(pos1.subtract(pos0).scale(1.0 / 3.0));
				posInt[1] = pos0.add(pos1.subtract(pos0).scale(2.0 / 3.0));
				posInt[2] = pos1.add(pos2.subtract(pos1).scale(1.0 / 3.0));
				posInt[3] = pos1.add(pos2.subtract(pos1).scale(2.0 / 3.0));
				posInt[4] = pos2.add(pos3.subtract(pos2).scale(1.0 / 3.0));
				posInt[5] = pos2.add(pos3.subtract(pos2).scale(2.0 / 3.0));
				posInt[6] = pos3.add(pos0.subtract(pos3).scale(1.0 / 3.0));
				posInt[7] = pos3.add(pos0.subtract(pos3).scale(2.0 / 3.0));
			}
		}

		// tria elements
		else if (geo == ElementLibrary.tria_) {

			// set corner nodes
			nodes_.add(elNodes[0]);
			nodes_.add(elNodes[1]);
			nodes_.add(elNodes[2]);

			// get corner nodes' position vectors
			DVec pos0 = nodes_.get(0).getPosition();
			DVec pos1 = nodes_.get(1).getPosition();
			DVec pos2 = nodes_.get(2).getPosition();

			// quadratic interpolation
			if (interp == ElementLibrary.quadratic_) {

				// compute positions of internal nodes
				posInt = new DVec[3];
				posInt[0] = pos0.add(pos1).scale(0.5);
				posInt[1] = pos1.add(pos2).scale(0.5);
				posInt[2] = pos2.add(pos0).scale(0.5);
			}
		}

		// hexa elements
		else if (geo == ElementLibrary.hexa_) {

			// set vertex nodes
			nodes_.add(elNodes[0]);
			nodes_.add(elNodes[1]);
			nodes_.add(elNodes[2]);
			nodes_.add(elNodes[3]);
			nodes_.add(elNodes[4]);
			nodes_.add(elNodes[5]);
			nodes_.add(elNodes[6]);
			nodes_.add(elNodes[7]);

			// get vertex nodes' position vectors
			DVec pos0 = nodes_.get(0).getPosition();
			DVec pos1 = nodes_.get(1).getPosition();
			DVec pos2 = nodes_.get(2).getPosition();
			DVec pos3 = nodes_.get(3).getPosition();
			DVec pos4 = nodes_.get(4).getPosition();
			DVec pos5 = nodes_.get(5).getPosition();
			DVec pos6 = nodes_.get(6).getPosition();
			DVec pos7 = nodes_.get(7).getPosition();

			// quadratic interpolation
			if (interp == ElementLibrary.quadratic_) {

				// compute positions of internal nodes
				posInt = new DVec[12];
				posInt[0] = pos0.add(pos1).scale(0.5);
				posInt[1] = pos1.add(pos2).scale(0.5);
				posInt[2] = pos2.add(pos3).scale(0.5);
				posInt[3] = pos3.add(pos0).scale(0.5);
				posInt[4] = pos4.add(pos5).scale(0.5);
				posInt[5] = pos5.add(pos6).scale(0.5);
				posInt[6] = pos6.add(pos7).scale(0.5);
				posInt[7] = pos7.add(pos4).scale(0.5);
				posInt[8] = pos4.add(pos0).scale(0.5);
				posInt[9] = pos5.add(pos1).scale(0.5);
				posInt[10] = pos6.add(pos2).scale(0.5);
				posInt[11] = pos7.add(pos3).scale(0.5);
			}
		}

		// tetra elements
		else if (geo == ElementLibrary.tetra_) {

			// set vertex nodes
			nodes_.add(elNodes[0]);
			nodes_.add(elNodes[1]);
			nodes_.add(elNodes[2]);
			nodes_.add(elNodes[3]);

			// get corner nodes' position vectors
			DVec pos0 = nodes_.get(0).getPosition();
			DVec pos1 = nodes_.get(1).getPosition();
			DVec pos2 = nodes_.get(2).getPosition();
			DVec pos3 = nodes_.get(3).getPosition();

			// quadratic interpolation
			if (interp == ElementLibrary.quadratic_) {

				// compute positions of internal nodes
				posInt = new DVec[6];
				posInt[0] = pos0.add(pos1).scale(0.5);
				posInt[1] = pos1.add(pos2).scale(0.5);
				posInt[2] = pos2.add(pos0).scale(0.5);
				posInt[3] = pos1.add(pos3).scale(0.5);
				posInt[4] = pos2.add(pos3).scale(0.5);
				posInt[5] = pos0.add(pos3).scale(0.5);
			}
		}

		// create internal nodes array
		nodes = new Node[posInt.length];

		// loop over internal nodes
		for (int i = 0; i < nodes.length; i++) {

			// check if there is any node at that coordinate
			nodes[i] = checkCoordinates(posInt[i]);

			// if there is no, create and add internal node to structure
			if (nodes[i] == null) {
				nodes[i] = new Node(posInt[i].get(0), posInt[i].get(1),
						posInt[i].get(2));

				// add node to structure
				owner_.structure_.addNode(nodes[i]);
			}

			// add to nodes vector
			nodes_.add(nodes[i]);
		}
	}

	/**
	 * Checks whether given coordinates are occupied by a node of the structure
	 * or not.
	 * 
	 * @param pos
	 *            The coordinates to be checked.
	 * @return True if no nodes are at the given coordinates, False vice versa.
	 */
	private Node checkCoordinates(DVec pos) {

		// loop over nodes of structure
		for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++) {

			// get nodal position vector
			Node node = owner_.structure_.getNode(i);
			DVec pos1 = node.getPosition();

			// check coordinates
			if (pos.subtract(pos1).l2Norm() <= tolerance_)
				return node;
		}

		// no node exists at the same coordinates
		return null;
	}

	/**
	 * If the related checkbox is selected, sets default value to textfield and
	 * makes it editable. If the checkbox is deselected, clears textfield and
	 * makes it uneditable.
	 */
	public void itemStateChanged(ItemEvent event) {

		// combobox1 event
		if (event.getSource().equals(combobox1_)) {

			// get selected item
			String item = combobox1_.getSelectedItem().toString();

			// for curved shell
			if (item == "Curved shell") {
				label4_.setVisible(true);
				label5_.setVisible(true);
				textfield2_.setText("1.0");
				textfield3_.setText("1.0");
				textfield2_.setEditable(true);
				textfield3_.setEditable(true);
			}

			// for curved beam
			else if (item == "Curved beam") {
				label4_.setVisible(true);
				label5_.setVisible(false);
				textfield2_.setText("1.0");
				textfield3_.setText("");
				textfield2_.setEditable(true);
				textfield3_.setEditable(false);
			}

			// for other items
			else {
				label4_.setVisible(false);
				label5_.setVisible(false);
				textfield2_.setText("");
				textfield3_.setText("");
				textfield2_.setEditable(false);
				textfield3_.setEditable(false);
			}
		}
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// check textfield
				if (checkId())
					setCombos();
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * Checks whether given parameters for radii of curvatures are correct or
	 * not.
	 * 
	 * @return True if entered parameters are correct, Flase vice versa.
	 */
	private boolean checkText() {

		// set error message to be displayed
		String message = "Illegal values for element parameters!";

		// textfield2
		if (textfield2_.isEditable()) {

			// check for non-numeric and zero values
			try {

				// convert texts to double values
				double r1 = Double.parseDouble(textfield2_.getText());

				// check if zero
				if (r1 == 0.0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(EditElement1.this, message,
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(EditElement1.this, message,
						"False data entry", 2);
				return false;
			}
		}

		// textfield3
		if (textfield3_.isEditable()) {

			// check for non-numeric and zero values
			try {

				// convert texts to double values
				double r2 = Double.parseDouble(textfield3_.getText());

				// check if zero
				if (r2 == 0.0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(EditElement1.this, message,
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(EditElement1.this, message,
						"False data entry", 2);
				return false;
			}
		}
		return true;
	}

	/**
	 * Checks wheter given element id exists in the structure or not.
	 * 
	 * @return True if element exists, False vice versa.
	 */
	private boolean checkId() {

		// get the entered text
		String text = textfield1_.getText();

		// check for non-numeric and negative values
		try {

			// convert text to integer value
			int value = Integer.parseInt(text);

			// check if given element exists
			element_ = owner_.structure_.getElement(value);
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(EditElement1.this,
					"Given element does not exist!", "False data entry", 2);
			return false;
		}

		// given element exists
		return true;
	}

	/**
	 * Sets lists for comboboxes depending on the element id given.
	 * 
	 */
	private void setCombos() {

		// get properties of element
		int dim = element_.getDimension();

		// initialize combobox arrays
		String[] type1 = null;
		String[] type2 = null;

		// one dimensional elements
		if (dim == ElementLibrary.oneDimensional_) {

			// set mechanics array
			type1 = new String[4];
			type1[0] = "Truss";
			type1[1] = "Beam";
			type1[2] = "Curved beam";
			type1[3] = "Planar truss";
			type1[4] = "Planar beam";

			// set interpolation array
			type2 = new String[3];
			type2[0] = "Linear";
			type2[1] = "Quadratic";
			type2[2] = "Cubic";
		}

		// two dimensional
		else if (dim == ElementLibrary.twoDimensional_) {

			// set mechanics array
			type1 = new String[5];
			type1[0] = "Plane Stress";
			type1[1] = "Plane Strain";
			type1[2] = "Plate";
			type1[3] = "Shell";
			type1[4] = "Curved shell";

			// get two dimensional element
			Element2D e2D = (Element2D) element_;

			// quad geometry
			if (e2D.getGeometry() == Element2D.quadrangular_) {

				// set interpolation array
				type2 = new String[3];
				type2[0] = "Bilinear";
				type2[1] = "Biquadratic";
				type2[2] = "Bicubic";
			}

			// tria geometry
			else if (e2D.getGeometry() == Element2D.triangular_) {

				// set interpolation array
				type2 = new String[2];
				type2[0] = "Linear";
				type2[1] = "Quadratic";
			}
		}

		// three dimensional
		else if (dim == ElementLibrary.threeDimensional_) {

			// set mechanics array
			type1 = new String[1];
			type1[0] = "Solid";

			// set interpolation array
			type2 = new String[2];
			type2[0] = "Trilinear";
			type2[1] = "Triquadratic";
		}

		// clear comboboxes
		combobox1_.removeItemListener(this);
		combobox1_.removeAllItems();
		combobox2_.removeAllItems();

		// set items to comboboxes
		for (int i = 0; i < type1.length; i++)
			combobox1_.addItem(type1[i]);
		for (int i = 0; i < type2.length; i++)
			combobox2_.addItem(type2[i]);
		combobox1_.addItemListener(this);

		// set additional labels and texts unvisible
		label4_.setVisible(false);
		label5_.setVisible(false);
		textfield2_.setText("");
		textfield3_.setText("");
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
