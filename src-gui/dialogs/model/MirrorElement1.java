/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dialogs.model;


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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import data.Group;

import node.Node;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DMat;
import matrix.DVec;

import element.*;

/**
 * Class for Mirror Elements Model menu.
 * 
 * @author Murat
 * 
 */
public class MirrorElement1 extends JDialog implements ActionListener,
		ItemListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_, textfield8_, textfield9_,
			textfield10_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_;

	private JComboBox combobox1_;

	private JCheckBox checkbox1_;

	private JButton button1_, button2_, button3_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** Vector for storing element ids to be moved. */
	private Vector<Integer> elementIds_ = new Vector<Integer>();

	/** Vector for storing elements to be moved. */
	private Vector<Element> elements1_ = new Vector<Element>();

	/**
	 * Array storing the replicate options. The sequence is; material, section,
	 * mechanial loads, temperature loads, springs, masses, local axes.
	 */
	protected boolean[] options_ = { true, true, true, true, true, true, true };

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
	public MirrorElement1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Mirror Elements", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Elements", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Mirror Options", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Mirror Plane", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("     X");
		JLabel label2 = new JLabel("     Y");
		JLabel label3 = new JLabel("     Z");
		JLabel label4 = new JLabel("Point 1 :");
		JLabel label5 = new JLabel("Point 2 :");
		JLabel label6 = new JLabel("Point 3 :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield8_ = new JTextField();
		textfield9_ = new JTextField();
		textfield10_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(103, 20));
		textfield2_.setPreferredSize(new Dimension(40, 20));
		textfield3_.setPreferredSize(new Dimension(40, 20));
		textfield4_.setPreferredSize(new Dimension(40, 20));

		// get the groups
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

		// build checkboxes
		checkbox1_ = new JCheckBox("Pass assignments");
		checkbox1_.setSelected(true);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");
		button3_ = new JButton("Options");

		// add components to sub-panels
		Commons.addComponent(panel3, radiobutton1_, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton2_, 1, 0, 1, 1);
		Commons.addComponent(panel3, combobox1_, 1, 1, 1, 1);
		Commons.addComponent(panel3, radiobutton3_, 2, 0, 2, 1);
		Commons.addComponent(panel4, checkbox1_, 0, 0, 1, 1);
		Commons.addComponent(panel4, button3_, 0, 1, 1, 1);
		Commons.addComponent(panel5, label1, 0, 1, 1, 1);
		Commons.addComponent(panel5, label2, 0, 2, 1, 1);
		Commons.addComponent(panel5, label3, 0, 3, 1, 1);
		Commons.addComponent(panel5, label4, 1, 0, 1, 1);
		Commons.addComponent(panel5, label5, 2, 0, 1, 1);
		Commons.addComponent(panel5, label6, 3, 0, 1, 1);
		Commons.addComponent(panel5, textfield2_, 1, 1, 1, 1);
		Commons.addComponent(panel5, textfield3_, 1, 2, 1, 1);
		Commons.addComponent(panel5, textfield4_, 1, 3, 1, 1);
		Commons.addComponent(panel5, textfield5_, 2, 1, 1, 1);
		Commons.addComponent(panel5, textfield6_, 2, 2, 1, 1);
		Commons.addComponent(panel5, textfield7_, 2, 3, 1, 1);
		Commons.addComponent(panel5, textfield8_, 3, 1, 1, 1);
		Commons.addComponent(panel5, textfield9_, 3, 2, 1, 1);
		Commons.addComponent(panel5, textfield10_, 3, 3, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
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
		radiobutton1_.addItemListener(this);
		radiobutton2_.addItemListener(this);
		radiobutton3_.addItemListener(this);
		checkbox1_.addItemListener(this);

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

		// options buttons clicked
		else if (e.getSource().equals(button3_)) {

			// create dialog and set visible
			MirrorElement2 dialog = new MirrorElement2(this);
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

		// checkbox1 event
		else if (event.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected())

				// set button3 disabled
				button3_.setEnabled(true);

			// not selected
			else if (checkbox1_.isSelected() == false)

				// set button3 disabled
				button3_.setEnabled(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check element
		progressor_.setStatusMessage("Checking data...");
		if (checkIds()) {

			// check texts
			if (checkTexts()) {

				// check if options clicked
				if (checkbox1_.isSelected() == false)
					for (int i = 0; i < options_.length; i++)
						options_[i] = false;

				// get entered values
				double[] point1 = new double[3];
				double[] point2 = new double[3];
				double[] point3 = new double[3];
				point1[0] = Double.parseDouble(textfield2_.getText());
				point1[1] = Double.parseDouble(textfield3_.getText());
				point1[2] = Double.parseDouble(textfield4_.getText());
				point2[0] = Double.parseDouble(textfield5_.getText());
				point2[1] = Double.parseDouble(textfield6_.getText());
				point2[2] = Double.parseDouble(textfield7_.getText());
				point3[0] = Double.parseDouble(textfield8_.getText());
				point3[1] = Double.parseDouble(textfield9_.getText());
				point3[2] = Double.parseDouble(textfield10_.getText());
				DVec p1 = new DVec(point1);
				DVec p2 = new DVec(point2);
				DVec p3 = new DVec(point3);

				// mirror elements
				progressor_.setStatusMessage("Mirroring elements...");
				mirrorElements(p1, p2, p3);

				// draw
				progressor_.setStatusMessage("Drawing...");
				owner_.drawPre();

				// close progressor
				progressor_.close();

				// close dialog
				setVisible(false);
			}
		}
	}

	/**
	 * Mirrors elements by the given plane points.
	 * 
	 * @param p1
	 *            Position vector of first node.
	 * @param p2
	 *            Position vector of second node.
	 * @param p3
	 *            Position vector of third node.
	 */
	private void mirrorElements(DVec p1, DVec p2, DVec p3) {

		// compute plane constants
		DMat mat = new DMat(3, 3);
		for (int i = 0; i < 3; i++) {
			mat.set(0, i, p1.get(i));
			mat.set(1, i, p2.get(i));
			mat.set(2, i, p3.get(i));
		}
		DMat mat1 = mat.scale(1.0);
		DMat mat2 = mat.scale(1.0);
		DMat mat3 = mat.scale(1.0);
		for (int i = 0; i < 3; i++) {
			mat1.set(i, 0, 1.0);
			mat2.set(i, 1, 1.0);
			mat3.set(i, 2, 1.0);
		}
		double a = mat.determinant();
		double a1 = mat1.determinant();
		double a2 = mat2.determinant();
		double a3 = mat3.determinant();

		// loop over would be moved elements list
		for (int i = 0; i < elements1_.size(); i++) {

			// get element and its nodes
			Element e = elements1_.get(i);
			Node[] nodes = e.getNodes();

			// create nodes
			nodes = createNodes(nodes, a, a1, a2, a3);

			// create new element
			createElement(nodes, e);
		}
	}

	/**
	 * Creates and replaces the demanded element with the old one.
	 * 
	 */
	private void createElement(Node[] nod, Element e) {

		// get type of element
		int type = e.getType();

		// initialize replicate element
		Element elm = null;

		// element0
		if (type == ElementLibrary.element0_)
			elm = new Element0(nod[1], nod[0]);

		// element1
		else if (type == ElementLibrary.element1_)
			elm = new Element1(nod[2], nod[1], nod[0]);

		// element2
		else if (type == ElementLibrary.element2_)
			elm = new Element2(nod[3], nod[2], nod[1], nod[0]);

		// element3
		else if (type == ElementLibrary.element3_)
			elm = new Element3(nod[3], nod[2], nod[1], nod[0]);

		// element4
		else if (type == ElementLibrary.element4_)
			elm = new Element4(nod[3], nod[2], nod[1], nod[0], nod[6], nod[5],
					nod[4], nod[7]);

		// element5
		else if (type == ElementLibrary.element5_)
			elm = new Element5(nod[2], nod[1], nod[0]);

		// element6
		else if (type == ElementLibrary.element6_)
			elm = new Element6(nod[2], nod[1], nod[0], nod[4], nod[3], nod[5]);

		// element7
		else if (type == ElementLibrary.element7_)
			elm = new Element7(nod[3], nod[2], nod[1], nod[0], nod[9], nod[8],
					nod[7], nod[6], nod[5], nod[4], nod[11], nod[10]);

		// element8
		else if (type == ElementLibrary.element8_)
			elm = new Element8(nod[3], nod[2], nod[1], nod[0]);

		// element9
		else if (type == ElementLibrary.element9_)
			elm = new Element9(nod[3], nod[2], nod[1], nod[0], nod[6], nod[5],
					nod[4], nod[7]);

		// element10
		else if (type == ElementLibrary.element10_)
			elm = new Element10(nod[3], nod[2], nod[1], nod[0], nod[9], nod[8],
					nod[7], nod[6], nod[5], nod[4], nod[11], nod[10]);

		// element11
		else if (type == ElementLibrary.element11_)
			elm = new Element11(nod[2], nod[1], nod[0]);

		// element12
		else if (type == ElementLibrary.element12_)
			elm = new Element12(nod[2], nod[1], nod[0], nod[4], nod[3], nod[5]);

		// element13
		else if (type == ElementLibrary.element13_)
			elm = new Element13(nod[1], nod[0]);

		// element14
		else if (type == ElementLibrary.element14_)
			elm = new Element14(nod[2], nod[1], nod[0]);

		// element15
		else if (type == ElementLibrary.element15_)
			elm = new Element15(nod[3], nod[2], nod[1], nod[0]);

		// element16
		else if (type == ElementLibrary.element16_)
			elm = new Element16(nod[3], nod[2], nod[1], nod[0]);

		// element17
		else if (type == ElementLibrary.element17_)
			elm = new Element17(nod[3], nod[2], nod[1], nod[0], nod[6], nod[5],
					nod[4], nod[7]);

		// element18
		else if (type == ElementLibrary.element18_)
			elm = new Element18(nod[2], nod[1], nod[0], nod[4], nod[3], nod[5]);

		// element19
		else if (type == ElementLibrary.element19_)
			elm = new Element19(nod[3], nod[2], nod[1], nod[0]);

		// element20
		else if (type == ElementLibrary.element20_)
			elm = new Element20(nod[3], nod[2], nod[1], nod[0], nod[6], nod[5],
					nod[4], nod[7]);

		// element21
		else if (type == ElementLibrary.element21_)
			elm = new Element21(nod[2], nod[1], nod[0], nod[4], nod[3], nod[5]);

		// element22
		else if (type == ElementLibrary.element22_)
			elm = new Element22(nod[3], nod[2], nod[1], nod[0], nod[7], nod[6],
					nod[5], nod[4]);

		// element23
		else if (type == ElementLibrary.element23_)
			elm = new Element23(nod[3], nod[2], nod[1], nod[0], nod[7], nod[6],
					nod[5], nod[4], nod[10], nod[9], nod[8], nod[11], nod[14],
					nod[13], nod[12], nod[15], nod[19], nod[18], nod[17],
					nod[16]);

		// element24
		else if (type == ElementLibrary.element24_)
			elm = new Element24(nod[3], nod[2], nod[1], nod[0]);

		// element25
		else if (type == ElementLibrary.element25_)
			elm = new Element25(nod[3], nod[2], nod[1], nod[0], nod[6], nod[5],
					nod[4], nod[7]);

		// element26
		else if (type == ElementLibrary.element26_)
			elm = new Element26(nod[2], nod[1], nod[0], nod[4], nod[3], nod[5]);

		// element27
		else if (type == ElementLibrary.element27_)
			elm = new Element27(nod[1], nod[0]);

		// element28
		else if (type == ElementLibrary.element28_)
			elm = new Element28(nod[2], nod[1], nod[0]);

		// element29
		else if (type == ElementLibrary.element29_)
			elm = new Element15(nod[3], nod[2], nod[1], nod[0]);

		// element30
		else if (type == ElementLibrary.element30_)
			elm = new Element30(nod[2], nod[1], nod[0], nod[3]);

		// element31
		else if (type == ElementLibrary.element31_)
			elm = new Element31(nod[2], nod[1], nod[0], nod[3], nod[5], nod[4],
					nod[6], nod[7], nod[9], nod[8]);

		// element32
		else if (type == ElementLibrary.element32_)
			elm = new Element32(nod[1], nod[0]);

		// element33
		else if (type == ElementLibrary.element33_)
			elm = new Element33(nod[2], nod[1], nod[0]);

		// element34
		else if (type == ElementLibrary.element34_)
			elm = new Element34(nod[3], nod[2], nod[1], nod[0]);

		// element35
		else if (type == ElementLibrary.element35_)
			elm = new Element35(nod[1], nod[0]);

		// element36
		else if (type == ElementLibrary.element36_)
			elm = new Element36(nod[2], nod[1], nod[0]);

		// element37
		else if (type == ElementLibrary.element37_)
			elm = new Element37(nod[3], nod[2], nod[1], nod[0]);

		// element38
		else if (type == ElementLibrary.element38_)
			elm = new Element38(nod[1], nod[0]);

		// pass element properties to new element
		passElementProperties(e, elm);

		// add new element to structure
		owner_.structure_.addElement(elm);

		// add new element to groups
		addToGroups(e, elm);
	}

	/**
	 * Adds newly created element to original element's groups.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The new element.
	 */
	private void addToGroups(Element e1, Element e2) {

		// loop over groups
		for (int i = 0; i < owner_.inputData_.getGroup().size(); i++) {

			// get group
			Group group = owner_.inputData_.getGroup().get(i);

			// check if group contains original element
			if (group.containsElement(e1)) {

				// add new element to the group
				group.addElement(e2);
			}
		}
	}

	/**
	 * Passes element properties to newly created element.
	 * 
	 * @param e1
	 *            The original element.
	 * @param e2
	 *            The replicate element.
	 */
	private void passElementProperties(Element e1, Element e2) {

		// material
		if (options_[0])
			if (e1.getMaterial() != null)
				e2.setMaterial(e1.getMaterial());

		// section
		if (options_[1])
			if (e1.getSection() != null)
				e2.setSection(e1.getSection());

		// mechanical loads
		if (options_[2])
			try {
				if (e1.getAllMechLoads() != null)
					e2.setMechLoads(e1.getAllMechLoads());
			} catch (Exception excep) {
			}

		// temperature loads
		if (options_[3])
			e2.setTempLoads(e1.getAllTempLoads());

		// springs
		if (options_[4])
			try {
				if (e1.getSprings() != null)
					e2.setSprings(e1.getSprings());
			} catch (Exception excep) {
			}

		// masses
		if (options_[5])
			try {
				if (e1.getAdditionalMasses() != null)
					e2.setMasses(e1.getAdditionalMasses());
			} catch (Exception excep) {
			}

		// local axes
		if (options_[6])
			if (e1.getDimension() == ElementLibrary.oneDimensional_) {

				// get 1D elements
				Element1D e1D1 = (Element1D) e1;
				Element1D e1D2 = (Element1D) e2;

				// set local axes
				if (e1D1.getLocalAxis() != null)
					e1D2.setLocalAxis(e1D1.getLocalAxis());
			}

		// radii of curvatures
		int typ = e1.getType();
		if (typ == ElementLibrary.element19_
				|| typ == ElementLibrary.element20_
				|| typ == ElementLibrary.element21_
				|| typ == ElementLibrary.element27_
				|| typ == ElementLibrary.element28_
				|| typ == ElementLibrary.element29_)
			e2.setParameters(e1.getParameters());
	}

	/**
	 * Creates new element nodes and returns them.
	 * 
	 * @param nod
	 *            The nodes to be moved.
	 * @param a
	 *            First constant of mirror plane.
	 * @param a1
	 *            Second constant of mirror plane.
	 * @param a2
	 *            Third constant of mirror plane.
	 * @param a3
	 *            Fourth constant of mirror plane.
	 * @return The array storing the newly created element nodes.
	 */
	private Node[] createNodes(Node[] nod, double a, double a1, double a2,
			double a3) {

		// create vector for storing positions of element nodes
		DVec[] pos = new DVec[nod.length];

		// compute position vectors of new nodes
		for (int i = 0; i < pos.length; i++) {
			pos[i] = nod[i].getPosition();
			double x0 = pos[i].get(0);
			double y0 = pos[i].get(1);
			double z0 = pos[i].get(2);
			DVec pos1 = new DVec(3);
			pos1.set(0, a1);
			pos1.set(1, a2);
			pos1.set(2, a3);
			double b = 2.0 * (a1 * x0 + a2 * y0 + a3 * z0 - a)
					/ (a1 * a1 + a2 * a2 + a3 * a3);
			pos[i] = pos[i].subtract(pos1.scale(b));
		}

		// loop over nodes
		Node[] nodes = new Node[pos.length];
		for (int i = 0; i < nodes.length; i++) {

			// check if there is any node at that coordinate
			nodes[i] = checkCoordinates(pos[i]);

			// if there is no, create and add node to structure
			if (nodes[i] == null) {
				nodes[i] = new Node(pos[i].get(0), pos[i].get(1), pos[i].get(2));

				// add node to structure
				owner_.structure_.addNode(nodes[i]);
			}
		}

		// return nodes array
		return nodes;
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

			// get nodal position vector and length
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
	 * Checks whether the delta textfields are correct or not.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkTexts() {

		// get the entered texts
		String text1 = textfield2_.getText();
		String text2 = textfield3_.getText();
		String text3 = textfield4_.getText();
		String text4 = textfield5_.getText();
		String text5 = textfield6_.getText();
		String text6 = textfield7_.getText();
		String text7 = textfield8_.getText();
		String text8 = textfield9_.getText();
		String text9 = textfield10_.getText();

		// check for non-numeric values
		try {

			// convert texts to double values
			double[] point1 = new double[3];
			double[] point2 = new double[3];
			double[] point3 = new double[3];
			point1[0] = Double.parseDouble(text1);
			point1[1] = Double.parseDouble(text2);
			point1[2] = Double.parseDouble(text3);
			point2[0] = Double.parseDouble(text4);
			point2[1] = Double.parseDouble(text5);
			point2[2] = Double.parseDouble(text6);
			point3[0] = Double.parseDouble(text7);
			point3[1] = Double.parseDouble(text8);
			point3[2] = Double.parseDouble(text9);

			// check if they form a plane
			DVec p1 = new DVec(point1);
			DVec p2 = new DVec(point2);
			DVec p3 = new DVec(point3);
			if (p1.subtract(p2).l2Norm() <= tolerance_
					|| p1.subtract(p3).l2Norm() <= tolerance_
					|| p2.subtract(p3).l2Norm() <= tolerance_) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this,
						"Illegal values for mirror plane!", "False data entry",
						2);
				return false;
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this,
					"Illegal values for coordinate changes!",
					"False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
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

		// set element ids for all existing elements
		else if (radiobutton3_.isSelected())
			setIdsForAll();

		// check for existance
		elements1_.clear();
		try {

			// check if given elements exist
			for (int i = 0; i < elementIds_.size(); i++) {

				// get element
				Element e = owner_.structure_.getElement(elementIds_.get(i));

				// add to would be moved element list
				elements1_.add(e);
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

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
	 * Checks and sets division element ids from textfield.
	 * 
	 * @return True if entered text is correct, False vice versa.
	 */
	private boolean setIdsFromText() {

		// get the entered text
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

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(this, "Illegal values!",
							"False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "Given elements do not exist!",
					"False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
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
