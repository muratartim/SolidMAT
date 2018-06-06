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

import element.*;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import node.Node;

import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DMat;
import matrix.DVec;

/**
 * Class for Add Element Model menu.
 * 
 * @author Murat
 * 
 */
public class AddElement1 extends JDialog implements ActionListener,
		ListSelectionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_;

	private JList list1_;

	private JLabel label3_, label4_;

	private DefaultListModel listModel1_;

	/** The tolerance for checking identical nodes and surface equation. */
	private static final double tol1_ = Math.pow(10, -8), tol2_ = Math.pow(10,
			-4);

	/** Array for storing element node ids. */
	private int[] value_;

	/** Vector for storing the nodes of element. */
	private Vector<Node> nodes_ = new Vector<Node>();

	/** The element to be added to the structure. */
	private Element element_;

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
	public AddElement1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Add Element", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Type and Nodes", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Type :");
		JLabel label2 = new JLabel("Node IDs :");
		label3_ = new JLabel("Radius 1 :");
		label4_ = new JLabel("Radius 2 :");
		label3_.setVisible(false);
		label4_.setVisible(false);

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield2_.setEditable(false);
		textfield3_.setEditable(false);

		// build list model and list, set single selection mode,
		// visible row number, fixed width, fixed height
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list1_.setVisibleRowCount(6);
		list1_.setFixedCellWidth(120);
		list1_.setFixedCellHeight(15);

		// build scroll panes and add lists to them
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, label2, 4, 0, 1, 1);
		Commons.addComponent(panel3, label3_, 5, 0, 1, 1);
		Commons.addComponent(panel3, label4_, 6, 0, 1, 1);
		Commons.addComponent(panel3, scrollpane1, 0, 1, 1, 4);
		Commons.addComponent(panel3, textfield1_, 4, 1, 1, 1);
		Commons.addComponent(panel3, textfield2_, 5, 1, 1, 1);
		Commons.addComponent(panel3, textfield3_, 6, 1, 1, 1);

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
		list1_.addListSelectionListener(this);

		// call initialize
		initialize();

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// set the input data vector to list
		Vector<ElementLibrary> object = owner_.inputData_.getElementLibrary();
		for (int i = 0; i < object.size(); i++)
			listModel1_.addElement(object.get(i).getName());
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
	 * Sets textfields visible or not depending on the element type selected
	 * from the list.
	 */
	public void valueChanged(ListSelectionEvent event) {

		// list1 item selected
		if (event.getSource().equals(list1_)) {

			// get type of selected element type
			int index = list1_.getSelectedIndex();
			ElementLibrary eL = owner_.inputData_.getElementLibrary()
					.get(index);
			int type = eL.getType();

			// for element19-20-21
			if (type == ElementLibrary.element19_
					|| type == ElementLibrary.element20_
					|| type == ElementLibrary.element21_) {
				label3_.setVisible(true);
				label4_.setVisible(true);
				textfield2_.setText("1.0");
				textfield3_.setText("1.0");
				textfield2_.setEditable(true);
				textfield3_.setEditable(true);
			}

			// for element27-28-29
			else if (type == ElementLibrary.element27_
					|| type == ElementLibrary.element28_
					|| type == ElementLibrary.element29_) {
				label3_.setVisible(true);
				label4_.setVisible(false);
				textfield2_.setText("1.0");
				textfield3_.setText("");
				textfield2_.setEditable(true);
				textfield3_.setEditable(false);
			}

			// for other elements
			else {
				label3_.setVisible(false);
				label4_.setVisible(false);
				textfield2_.setText("");
				textfield3_.setText("");
				textfield2_.setEditable(false);
				textfield3_.setEditable(false);
			}
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// check if any selection made in the list
		if (list1_.isSelectionEmpty() == false) {

			// check entered values
			progressor_.setStatusMessage("Checking data...");
			if (checkText1() && checkText2() && checkElement()) {

				// add created element to structure
				owner_.structure_.addElement(element_);

				// draw
				progressor_.setStatusMessage("Drawing...");
				owner_.drawPre();

				// close progressor
				progressor_.close();

				// close dialog
				setVisible(false);
			}
		}

		// empty
		else {

			// close progressor
			progressor_.close();
			setStill(false);
		}
	}

	/**
	 * Checks textfield with the number of corner nodes for the selected element
	 * type.
	 * 
	 * @return True if given nodes are correct, False vise versa.
	 */
	private boolean checkText1() {

		// get the entered texts
		String text1 = textfield1_.getText();

		// eliminate spaces
		String elText1 = "";
		for (int i = 0; i < text1.length(); i++) {
			char c = text1.charAt(i);
			if (c != " ".charAt(0))
				elText1 += c;
		}

		// get geometry of selected element type
		int index = list1_.getSelectedIndex();
		ElementLibrary eL = owner_.inputData_.getElementLibrary().get(index);
		int geo = eL.getGeometry();
		int nn = 0;

		// for one dimensional elements
		if (geo == ElementLibrary.line_)
			nn = 2;

		// for quad elements
		else if (geo == ElementLibrary.quad_)
			nn = 4;

		// for tria elements
		else if (geo == ElementLibrary.tria_)
			nn = 3;

		// for hexa elements
		else if (geo == ElementLibrary.hexa_)
			nn = 8;

		// for tetra elements
		else if (geo == ElementLibrary.tetra_)
			nn = 4;

		// split the texts into number of nodes
		String[] comp1 = elText1.split(",", nn);
		int[] value1 = new int[nn];

		// check for non-numeric and negative values
		try {

			// convert text to integer value
			for (int i = 0; i < nn; i++) {
				value1[i] = Integer.parseInt(comp1[i]);

				// check if negative
				if (value1[i] < 0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(AddElement1.this,
							"Illegal values for element nodes!",
							"False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(AddElement1.this,
					"Illegal values for element nodes!", "False data entry", 2);
			return false;
		}

		// the data is correct
		value_ = value1;
		return true;
	}

	/**
	 * Checks whether given parameters for radii of curvatures are correct or
	 * not.
	 * 
	 * @return True if entered parameters are correct, Flase vice versa.
	 */
	private boolean checkText2() {

		// get type of selected element type
		int index = list1_.getSelectedIndex();
		ElementLibrary eL = owner_.inputData_.getElementLibrary().get(index);
		int type = eL.getType();

		// set error message to be displayed
		String message = "Illegal values for element parameters!";

		// for element19-20-21
		if (type == ElementLibrary.element19_
				|| type == ElementLibrary.element20_
				|| type == ElementLibrary.element21_) {

			// check for non-numeric and zero values
			try {

				// convert texts to double values
				double r1 = Double.parseDouble(textfield2_.getText());
				double r2 = Double.parseDouble(textfield3_.getText());

				// check if zero
				if (r1 == 0.0 || r2 == 0.0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(AddElement1.this, message,
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(AddElement1.this, message,
						"False data entry", 2);
				return false;
			}
		}

		// for element27-28-29
		else if (type == ElementLibrary.element27_
				|| type == ElementLibrary.element28_
				|| type == ElementLibrary.element29_) {

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
					JOptionPane.showMessageDialog(AddElement1.this, message,
							"False data entry", 2);
					return false;
				}
			} catch (Exception excep) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(AddElement1.this, message,
						"False data entry", 2);
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates demanded element and checks for negative-zero jacobian and
	 * existance.
	 * 
	 * @return True if element has been succesfully created, False vice versa.
	 */
	private boolean checkElement() {

		// check given corner nodes
		if (checkNodes() == false)
			return false;

		// create element
		createElement();

		// check jacobian of element
		if (checkJacobian() == false)
			return false;

		// check structure for identical elements
		if (checkIdenticalElements() == false)
			return false;

		// the data is correct
		return true;
	}

	/**
	 * Checks if given corner nodes exist or not and, checks whether given nodes
	 * form a plane (for quad elements).
	 * 
	 * @return True if nodes exist, False vice versa.
	 */
	private boolean checkNodes() {

		// clear nodes vector
		nodes_.clear();

		// check if given nodes exist
		try {
			for (int i = 0; i < value_.length; i++)
				nodes_.add(owner_.structure_.getNode(value_[i]));
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(AddElement1.this,
					"Given nodes do not exist!", "False data entry", 2);
			return false;
		}

		// get geometry of selected element type
		int index = list1_.getSelectedIndex();
		ElementLibrary eL = owner_.inputData_.getElementLibrary().get(index);
		int geo = eL.getGeometry();

		// check if corner nodes form a plane (for quad elements)
		if (geo == ElementLibrary.quad_) {

			// get position vectors of initial 3 nodes
			DVec pos1 = nodes_.get(0).getPosition();
			DVec pos2 = nodes_.get(1).getPosition();
			DVec pos3 = nodes_.get(2).getPosition();
			DVec pos4 = nodes_.get(3).getPosition();

			// compute the plane equation
			DMat plane = new DMat(4, 4);
			plane.set(0, 3, 1.0);
			plane.set(1, 3, 1.0);
			plane.set(2, 3, 1.0);
			plane.set(3, 3, 1.0);
			for (int i = 0; i < 3; i++) {
				plane.set(0, i, pos1.get(i));
				plane.set(1, i, pos2.get(i));
				plane.set(2, i, pos3.get(i));
				plane.set(3, i, pos4.get(i));
			}

			// check if nodes form a plane
			if (Math.abs(plane.determinant()) > tol2_) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(AddElement1.this,
						"Given nodes do not form a plane!", "False data entry",
						2);
				return false;
			}
		}

		// nodes exist
		return true;
	}

	/**
	 * Creates demanded element.
	 * 
	 */
	private void createElement() {

		// get the selected element type
		int index = list1_.getSelectedIndex();
		ElementLibrary eL = owner_.inputData_.getElementLibrary().get(index);
		int type = eL.getType();

		// create internal nodes
		createInternalNodes();

		// element0
		if (type == ElementLibrary.element0_)
			element_ = new Element0(nodes_.get(0), nodes_.get(1));

		// element1
		else if (type == ElementLibrary.element1_)
			element_ = new Element1(nodes_.get(0), nodes_.get(2), nodes_.get(1));

		// element2
		else if (type == ElementLibrary.element2_)
			element_ = new Element2(nodes_.get(0), nodes_.get(2),
					nodes_.get(3), nodes_.get(1));

		// element3
		else if (type == ElementLibrary.element3_)
			element_ = new Element3(nodes_.get(0), nodes_.get(1),
					nodes_.get(2), nodes_.get(3));

		// element4
		else if (type == ElementLibrary.element4_)
			element_ = new Element4(nodes_.get(0), nodes_.get(1),
					nodes_.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element5
		else if (type == ElementLibrary.element5_)
			element_ = new Element5(nodes_.get(0), nodes_.get(1), nodes_.get(2));

		// element6
		else if (type == ElementLibrary.element6_)
			element_ = new Element6(nodes_.get(0), nodes_.get(1),
					nodes_.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element7
		else if (type == ElementLibrary.element7_)
			element_ = new Element7(nodes_.get(0), nodes_.get(1),
					nodes_.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9),
					nodes_.get(10), nodes_.get(11));

		// element8
		else if (type == ElementLibrary.element8_)
			element_ = new Element8(nodes_.get(0), nodes_.get(1),
					nodes_.get(2), nodes_.get(3));

		// element9
		else if (type == ElementLibrary.element9_)
			element_ = new Element9(nodes_.get(0), nodes_.get(1),
					nodes_.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element10
		else if (type == ElementLibrary.element10_)
			element_ = new Element10(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9),
					nodes_.get(10), nodes_.get(11));

		// element11
		else if (type == ElementLibrary.element11_)
			element_ = new Element11(nodes_.get(0), nodes_.get(1), nodes_
					.get(2));

		// element12
		else if (type == ElementLibrary.element12_)
			element_ = new Element12(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element13
		else if (type == ElementLibrary.element13_)
			element_ = new Element13(nodes_.get(0), nodes_.get(1));

		// element14
		else if (type == ElementLibrary.element14_)
			element_ = new Element14(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element15
		else if (type == ElementLibrary.element15_)
			element_ = new Element15(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element16
		else if (type == ElementLibrary.element16_)
			element_ = new Element16(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element17
		else if (type == ElementLibrary.element17_)
			element_ = new Element17(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element18
		else if (type == ElementLibrary.element18_)
			element_ = new Element18(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element19
		else if (type == ElementLibrary.element19_) {
			element_ = new Element19(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));
			double r1 = Double.parseDouble(textfield2_.getText());
			double r2 = Double.parseDouble(textfield3_.getText());
			double[] param = { r1, r2 };
			element_.setParameters(param);
		}

		// element20
		else if (type == ElementLibrary.element20_) {
			element_ = new Element20(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));
			double r1 = Double.parseDouble(textfield2_.getText());
			double r2 = Double.parseDouble(textfield3_.getText());
			double[] param = { r1, r2 };
			element_.setParameters(param);
		}

		// element21
		else if (type == ElementLibrary.element21_) {
			element_ = new Element21(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));
			double r1 = Double.parseDouble(textfield2_.getText());
			double r2 = Double.parseDouble(textfield3_.getText());
			double[] param = { r1, r2 };
			element_.setParameters(param);
		}

		// element22
		else if (type == ElementLibrary.element22_)
			element_ = new Element22(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element23
		else if (type == ElementLibrary.element23_)
			element_ = new Element23(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9),
					nodes_.get(10), nodes_.get(11), nodes_.get(12), nodes_
							.get(13), nodes_.get(14), nodes_.get(15), nodes_
							.get(16), nodes_.get(17), nodes_.get(18), nodes_
							.get(19));

		// element24
		else if (type == ElementLibrary.element24_)
			element_ = new Element24(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element25
		else if (type == ElementLibrary.element25_)
			element_ = new Element25(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7));

		// element26
		else if (type == ElementLibrary.element26_)
			element_ = new Element26(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5));

		// element27
		else if (type == ElementLibrary.element27_) {
			element_ = new Element27(nodes_.get(0), nodes_.get(1));
			double r1 = Double.parseDouble(textfield2_.getText());
			double[] param = { r1 };
			element_.setParameters(param);
		}

		// element28
		else if (type == ElementLibrary.element28_) {
			element_ = new Element28(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));
			double r1 = Double.parseDouble(textfield2_.getText());
			double[] param = { r1 };
			element_.setParameters(param);
		}

		// element29
		else if (type == ElementLibrary.element29_) {
			element_ = new Element29(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));
			double r1 = Double.parseDouble(textfield2_.getText());
			double[] param = { r1 };
			element_.setParameters(param);
		}

		// element30
		else if (type == ElementLibrary.element30_)
			element_ = new Element30(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3));

		// element31
		else if (type == ElementLibrary.element31_)
			element_ = new Element31(nodes_.get(0), nodes_.get(1), nodes_
					.get(2), nodes_.get(3), nodes_.get(4), nodes_.get(5),
					nodes_.get(6), nodes_.get(7), nodes_.get(8), nodes_.get(9));

		// element32
		else if (type == ElementLibrary.element32_)
			element_ = new Element32(nodes_.get(0), nodes_.get(1));

		// element33
		else if (type == ElementLibrary.element33_)
			element_ = new Element33(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element34
		else if (type == ElementLibrary.element34_)
			element_ = new Element34(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element35
		else if (type == ElementLibrary.element35_)
			element_ = new Element35(nodes_.get(0), nodes_.get(1));

		// element36
		else if (type == ElementLibrary.element36_)
			element_ = new Element36(nodes_.get(0), nodes_.get(2), nodes_
					.get(1));

		// element37
		else if (type == ElementLibrary.element37_)
			element_ = new Element37(nodes_.get(0), nodes_.get(2), nodes_
					.get(3), nodes_.get(1));

		// element38
		else if (type == ElementLibrary.element38_)
			element_ = new Element38(nodes_.get(0), nodes_.get(1));
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
			if (pos.subtract(pos1).l2Norm() <= tol1_)
				return node;
		}

		// no node exists at the same coordinates
		return null;
	}

	/**
	 * Creates internal nodes of element.
	 * 
	 */
	private void createInternalNodes() {

		// get the selected element properties
		int index = list1_.getSelectedIndex();
		ElementLibrary eL = owner_.inputData_.getElementLibrary().get(index);
		int geo = eL.getGeometry();
		int interp = eL.getInterpolation();

		// create internal element nodes
		Node[] nodes = new Node[0];
		DVec[] posInt = new DVec[0];

		// one dimensional elements
		if (geo == ElementLibrary.line_) {

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

			// get corner nodes' position vectors
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

		// create internal nodes
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
	 * Checks the determinant of jacobian of element.
	 * 
	 * @return True if the determinant of jacobian is not zero or negative,
	 *         False vice versa.
	 */
	private boolean checkJacobian() {

		// set error message to display
		String message = "Negative or zero jacobian encountered for element!";

		// one dimensional element
		if (element_.getDimension() == ElementLibrary.oneDimensional_) {

			// get one dimensional element
			Element1D e1D = (Element1D) element_;

			// compute jacobian
			double jacDet = e1D.getDetJacobian();

			// check if it is negative or zero
			if (jacDet <= 0.0) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(AddElement1.this, message,
						"False data entry", 2);
				return false;
			}
		}

		// two dimensional element
		else if (element_.getDimension() == ElementLibrary.twoDimensional_) {

			// get two dimensional element
			Element2D e2D = (Element2D) element_;

			// initialize and compute natural coordinates
			double[] eps1 = null;
			double[] eps2 = null;

			// quad geometry
			if (e2D.getGeometry() == Element2D.quadrangular_) {
				eps1 = new double[4];
				eps2 = new double[4];
				eps1[0] = -1.0;
				eps1[1] = 1.0;
				eps1[2] = 1.0;
				eps1[3] = -1.0;
				eps2[0] = -1.0;
				eps2[1] = -1.0;
				eps2[2] = 1.0;
				eps2[3] = 1.0;
			}

			// triangular geometry
			else if (e2D.getGeometry() == Element2D.triangular_) {
				eps1 = new double[3];
				eps2 = new double[3];
				eps1[0] = 1.0;
				eps1[1] = 0.0;
				eps1[2] = 0.0;
				eps2[0] = 0.0;
				eps2[1] = 1.0;
				eps2[2] = 0.0;
			}

			// loop over corner points of element
			for (int i = 0; i < eps1.length; i++) {

				// compute the jacobian and determinant
				double jacDet = e2D.getJacobian(eps1[i], eps2[i]).determinant();

				// check if it is negative or zero
				if (jacDet <= 0.0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(AddElement1.this, message,
							"False data entry", 2);
					return false;
				}
			}
		}

		// three dimensional element
		else if (element_.getDimension() == ElementLibrary.threeDimensional_) {

			// get three dimensional element
			Element3D e3D = (Element3D) element_;

			// initialize and compute natural coordinates
			double[] eps1 = null;
			double[] eps2 = null;
			double[] eps3 = null;

			// hexa geometry
			if (e3D.getGeometry() == Element3D.hexahedral_) {
				eps1 = new double[8];
				eps2 = new double[8];
				eps3 = new double[8];
				eps1[0] = 1.0;
				eps2[0] = 1.0;
				eps3[0] = 1.0;
				eps1[1] = -1.0;
				eps2[1] = 1.0;
				eps3[1] = 1.0;
				eps1[2] = -1.0;
				eps2[2] = -1.0;
				eps3[2] = 1.0;
				eps1[3] = 1.0;
				eps2[3] = -1.0;
				eps3[3] = 1.0;
				eps1[4] = 1.0;
				eps2[4] = 1.0;
				eps3[4] = -1.0;
				eps1[5] = -1.0;
				eps2[5] = 1.0;
				eps3[5] = -1.0;
				eps1[6] = -1.0;
				eps2[6] = -1.0;
				eps3[6] = -1.0;
				eps1[7] = 1.0;
				eps2[7] = -1.0;
				eps3[7] = -1.0;
			}

			// tetrahedral geometry
			else if (e3D.getGeometry() == Element3D.tetrahedral_) {
				eps1 = new double[4];
				eps2 = new double[4];
				eps3 = new double[4];
				eps1[0] = 0.0;
				eps2[0] = 0.0;
				eps3[0] = 0.0;
				eps1[1] = 1.0;
				eps2[1] = 0.0;
				eps3[1] = 0.0;
				eps1[2] = 0.0;
				eps2[2] = 1.0;
				eps3[2] = 0.0;
				eps1[3] = 0.0;
				eps2[3] = 0.0;
				eps3[3] = 1.0;
			}

			// loop over corner points of element
			for (int i = 0; i < eps1.length; i++) {

				// compute the jacobian and determinant
				double jacDet = e3D.getJacobian(eps1[i], eps2[i], eps3[i])
						.determinant();

				// check if it is negative or zero
				if (jacDet <= 0.0) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(AddElement1.this, message,
							"False data entry", 2);
					return false;
				}
			}
		}

		// jacobian is positive
		return true;
	}

	/**
	 * Checks structure for the existance of identical elements. Identical
	 * elements are the elements having the same nodes and type.
	 * 
	 * @return True if no identical elements found, False vice versa.
	 */
	private boolean checkIdenticalElements() {

		// get type and nodes of created element
		int type1 = element_.getType();
		Node[] nodes = element_.getNodes();

		// loop over elements of structure
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get target element and its type
			Element e = owner_.structure_.getElement(i);
			int type2 = e.getType();

			// check for same type
			if (type1 == type2) {

				// check for identical nodes
				int m = 0;
				for (int j = 0; j < nodes.length; j++) {
					for (int k = 0; k < nodes.length; k++) {
						if (nodes[j].equals(e.getNodes()[k]))
							m++;
					}
				}

				// identical nodes
				if (m == nodes.length) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(AddElement1.this,
							"Element already exists!", "False data entry", 2);
					return false;
				}
			}
		}

		// element does not exist
		return true;
	}
}
