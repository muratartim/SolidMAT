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


import java.util.Vector;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import node.Node;

import element.Element;
import element.Element2D;
import element.Element3;
import element.Element3D;
import element.Element5;
import element.ElementLibrary;
import main.Commons;

/**
 * Class for Check Model menu.
 * 
 * @author Murat
 * 
 */
public class Check1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private GridBagLayout layout1_;

	private GridBagConstraints constraints1_;

	private JTextField textfield1_;

	private JRadioButton radiobutton1_, radiobutton2_, radiobutton3_,
			radiobutton4_, radiobutton5_, radiobutton6_;

	private JButton button1_, button2_;

	private JLabel label1_;

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
	public Check1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Check", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		// build sub-panels
		JPanel panel3 = new JPanel();
		JPanel panel4 = new JPanel();

		// build layout managers and set layout managers to panels
		layout1_ = new GridBagLayout();
		FlowLayout layout2 = new FlowLayout();
		panel1.setLayout(layout1_);
		panel2.setLayout(layout2);
		panel3.setLayout(layout1_);
		panel4.setLayout(layout1_);

		// set border to panels
		panel3.setBorder(BorderFactory.createTitledBorder("Check"));
		panel4.setBorder(BorderFactory.createTitledBorder("Operation"));

		// build gridbag constraints, make components extend in both directions
		// and determine gaps between components
		constraints1_ = new GridBagConstraints();
		constraints1_.insets = new Insets(5, 5, 5, 5);
		layout2.setAlignment(FlowLayout.CENTER);
		layout2.setHgap(5);
		layout2.setVgap(5);

		// build labels
		label1_ = new JLabel("Treshold :");

		// build text fields and set font
		textfield1_ = new JTextField("0.5");
		textfield1_.setEnabled(false);

		// build radio buttons and set font
		radiobutton1_ = new JRadioButton("Upside down", true);
		radiobutton2_ = new JRadioButton("Inside out", false);
		radiobutton3_ = new JRadioButton("Zero volume", false);
		radiobutton4_ = new JRadioButton("Aspect ratio", false);
		radiobutton5_ = new JRadioButton("Display", true);
		radiobutton6_ = new JRadioButton("Remove", false);
		radiobutton5_.setPreferredSize(new Dimension(85, 20));
		radiobutton6_.setPreferredSize(new Dimension(85, 20));

		// build button groups
		ButtonGroup buttongroup1 = new ButtonGroup();
		ButtonGroup buttongroup2 = new ButtonGroup();
		buttongroup1.add(radiobutton1_);
		buttongroup1.add(radiobutton2_);
		buttongroup1.add(radiobutton3_);
		buttongroup1.add(radiobutton4_);
		buttongroup2.add(radiobutton5_);
		buttongroup2.add(radiobutton6_);

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		addComponent(panel3, radiobutton1_, 0, 0, 1, 1);
		addComponent(panel3, radiobutton2_, 0, 1, 1, 1);
		addComponent(panel3, radiobutton3_, 1, 0, 1, 1);
		addComponent(panel3, radiobutton4_, 1, 1, 1, 1);
		addComponent(panel3, label1_, 2, 0, 1, 1);
		addComponent(panel3, textfield1_, 2, 1, 1, 1);
		addComponent(panel4, radiobutton5_, 0, 0, 1, 1);
		addComponent(panel4, radiobutton6_, 0, 1, 1, 1);

		// add sub-panels to main panels
		addComponent(panel1, panel3, 0, 0, 1, 1);
		addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button1_);
		panel2.add(button2_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		radiobutton1_.addActionListener(this);
		radiobutton2_.addActionListener(this);
		radiobutton3_.addActionListener(this);
		radiobutton4_.addActionListener(this);
		radiobutton5_.addActionListener(this);
		radiobutton6_.addActionListener(this);

		// visualize
		Commons.visualize(this);
	}

	/**
	 * Sets gridbag layout constraints and adds components to layout.
	 * 
	 * @param component
	 *            Component to be added into layout.
	 * @param row
	 *            Y coordinate of the upper-left corner of the component.
	 * @param column
	 *            X coordinate of the upper-left corner of the component.
	 * @param width
	 *            Cell number captured by the component's width.
	 * @param height
	 *            Cell number captured by the component's height.
	 */
	private void addComponent(JPanel panel, Component component, int row,
			int column, int width, int height) {

		// set gridx and gridy
		constraints1_.gridx = column;
		constraints1_.gridy = row;

		// set gridwidth and gridheight
		constraints1_.gridwidth = width;
		constraints1_.gridheight = height;

		if (component.equals(label1_)) {
			constraints1_.fill = GridBagConstraints.NONE;
			constraints1_.anchor = GridBagConstraints.EAST;
		} else
			constraints1_.fill = GridBagConstraints.BOTH;

		// set constraints and add component to panel1
		layout1_.setConstraints(component, constraints1_);
		panel.add(component);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getSource().equals(button1_)) {

			// check text
			if (checkText()) {

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
		}

		// cancel button clicked
		else if (e.getSource().equals(button2_)) {

			// set dialog unvisible
			setVisible(false);
		}

		// radiobutton1 clicked
		else if (e.getSource().equals(radiobutton1_))
			textfield1_.setEnabled(false);

		// radiobutton2 clicked
		else if (e.getSource().equals(radiobutton2_))
			textfield1_.setEnabled(false);

		// radiobutton3 clicked
		else if (e.getSource().equals(radiobutton3_))
			textfield1_.setEnabled(false);

		// radiobutton4 clicked
		else if (e.getSource().equals(radiobutton4_))
			textfield1_.setEnabled(true);
	}

	/**
	 * Checks treshold textfield.
	 * 
	 * @return True if entered text is correct, False vice versa.
	 */
	private boolean checkText() {

		// textfield enabled
		if (textfield1_.isEnabled()) {

			// get the entered text
			String text = textfield1_.getText();

			// check for non-numeric values-zero or negative
			try {

				// convert text to double value
				double val = Double.parseDouble(text);

				// check if negative
				if (val < 0.0) {

					// display message
					JOptionPane.showMessageDialog(this,
							"Illegal value for treshold!", "False data entry",
							2);
					return false;
				}
			} catch (Exception excep) {

				// display message
				JOptionPane.showMessageDialog(this,
						"Illegal value for treshold!", "False data entry", 2);
				return false;
			}
		}
		return true;
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

		// display operation option is selected
		if (radiobutton5_.isSelected())
			display();

		// remove operation option is selected
		else if (radiobutton6_.isSelected())
			remove();
	}

	/**
	 * Performs display operation for the selected option.
	 * 
	 */
	private void display() {

		// get element display options of visualizer
		boolean[] elementOpt = owner_.preVis_.getElementOptions();

		// set display options
		for (int i = 3; i < elementOpt.length; i++)
			elementOpt[i] = false;

		// disable colored options
		owner_.preVis_.disableColoredOptions();

		// set demanded options
		elementOpt[12] = radiobutton1_.isSelected();
		elementOpt[13] = radiobutton2_.isSelected();
		elementOpt[14] = radiobutton3_.isSelected();
		elementOpt[15] = radiobutton4_.isSelected();

		// set treshold to visualizer
		owner_.preVis_.setTreshold(Double.parseDouble(textfield1_.getText()));

		// draw
		progressor_.setStatusMessage("Drawing...");
		owner_.drawPre();

		// close progressor
		progressor_.close();

		// set dialog unvisible
		setVisible(false);
	}

	/**
	 * Performs removal operation for the selected option.
	 * 
	 */
	private void remove() {

		// initialize message
		String message = null;

		// upside down option
		if (radiobutton1_.isSelected())
			message = removeUpsideDown();

		// inside out option
		else if (radiobutton2_.isSelected())
			message = removeInsideOut();

		// zero volume option
		else if (radiobutton3_.isSelected())
			message = removeZeroVolume();

		// aspect ratio option
		else if (radiobutton4_.isSelected())
			message = removeAspectRatio();

		// draw
		progressor_.setStatusMessage("Drawing...");
		owner_.drawPre();

		// close progressor
		progressor_.close();

		// display message
		JOptionPane.showMessageDialog(this, message, "Check",
				JOptionPane.INFORMATION_MESSAGE);

		// close dialog
		setVisible(false);
	}

	/**
	 * Removes elements with bad aspect ratio in the mesh.
	 * 
	 * @return Message indicating the number of removed elements.
	 */
	private String removeAspectRatio() {

		// start operation
		progressor_
				.setStatusMessage("Removing elements with bad aspect ratio...");

		// initialize vector for storing removal elements
		Vector<Element> elements = new Vector<Element>();

		// get threshold
		double val = Double.parseDouble(textfield1_.getText());

		// loop over elements
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// two dimensional element
			if (e.getDimension() == ElementLibrary.twoDimensional_) {

				// write process message
				progressor_.setStatusMessage("Checking element - " + i + "...");

				// get 2D element
				Element2D e2D = (Element2D) e;

				// get area of element
				double a = e2D.getArea();

				// get nodes of element
				Node[] nodes = e2D.getNodes();

				// quad element
				if (e2D.getGeometry() == Element2D.quadrangular_) {

					// compute edge lengths
					double d1 = nodes[1].getPosition().subtract(
							nodes[0].getPosition()).l2Norm();
					double d2 = nodes[2].getPosition().subtract(
							nodes[1].getPosition()).l2Norm();
					double d3 = nodes[3].getPosition().subtract(
							nodes[2].getPosition()).l2Norm();
					double d4 = nodes[0].getPosition().subtract(
							nodes[3].getPosition()).l2Norm();

					// compute perimeter
					double s = d1 + d2 + d3 + d4;

					// compute aspect ratio of element
					double arEl = s / a;

					// get maximum edge length
					double max = Math.max(Math.max(d1, d2), Math.max(d3, d4));

					// compute aspect ratio of perfect quad
					double arPer = 4.0 / max;

					// normalize element's aspect ratio
					arEl = arEl / arPer;

					// add to removal elements
					if (arEl > val + 1.0)
						elements.add(e);
				}

				// tria element
				else if (e2D.getGeometry() == Element2D.triangular_) {

					// compute edge lengths
					double d1 = nodes[1].getPosition().subtract(
							nodes[0].getPosition()).l2Norm();
					double d2 = nodes[2].getPosition().subtract(
							nodes[1].getPosition()).l2Norm();
					double d3 = nodes[2].getPosition().subtract(
							nodes[0].getPosition()).l2Norm();

					// compute perimeter
					double s = d1 + d2 + d3;

					// compute aspect ratio of element
					double arEl = s / a;

					// get maximum edge length
					double max = Math.max(Math.max(d1, d2), d3);

					// compute aspect ratio of perfect tria
					double arPer = 12.0 / (max * Math.sqrt(3));

					// normalize element's aspect ratio
					arEl = arEl / arPer;

					// add to removal elements
					if (arEl > val + 1.0)
						elements.add(e);
				}
			}

			// three dimensional element
			else if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// write process message
				progressor_.setStatusMessage("Checking element - " + i + "...");

				// get 3D element
				Element3D e3D = (Element3D) e;

				// get volume of element
				double v = e3D.getVolume();

				// get nodes of element
				Node[] nodes = e3D.getNodes();

				// hexa element
				if (e3D.getGeometry() == Element3D.hexahedral_) {

					// compute surface areas of all faces
					Element3 eQuad = new Element3(nodes[0], nodes[4], nodes[5],
							nodes[1]);
					double a1 = eQuad.getArea();
					eQuad = new Element3(nodes[1], nodes[5], nodes[6], nodes[2]);
					double a2 = eQuad.getArea();
					eQuad = new Element3(nodes[2], nodes[6], nodes[7], nodes[3]);
					double a3 = eQuad.getArea();
					eQuad = new Element3(nodes[3], nodes[7], nodes[4], nodes[0]);
					double a4 = eQuad.getArea();
					eQuad = new Element3(nodes[0], nodes[1], nodes[2], nodes[3]);
					double a5 = eQuad.getArea();
					eQuad = new Element3(nodes[4], nodes[5], nodes[6], nodes[7]);
					double a6 = eQuad.getArea();

					// compute total surface area
					double s = a1 + a2 + a3 + a4 + a5 + a6;

					// compute aspect ratio of element
					double arEl = s / v;

					// get maximum surface area
					double max = Math.max(Math.max(a1, a2), Math.max(a3, a4));
					max = Math.max(max, Math.max(a5, a6));

					// compute aspect ratio of perfect hexa
					double arPer = 6.0 / max;

					// normalize element's aspect ratio
					arEl = arEl / arPer;

					// add to removal elements
					if (arEl > val + 1.0)
						elements.add(e);
				}

				// tetra element
				else if (e3D.getGeometry() == Element3D.tetrahedral_) {

					// compute surface areas of all faces
					Element5 eTria = new Element5(nodes[0], nodes[1], nodes[3]);
					double a1 = eTria.getArea();
					eTria = new Element5(nodes[1], nodes[2], nodes[3]);
					double a2 = eTria.getArea();
					eTria = new Element5(nodes[2], nodes[0], nodes[3]);
					double a3 = eTria.getArea();
					eTria = new Element5(nodes[0], nodes[2], nodes[1]);
					double a4 = eTria.getArea();

					// compute total surface area
					double s = a1 + a2 + a3 + a4;

					// compute aspect ratio of element
					double arEl = s / v;

					// get maximum surface area
					double max = Math.max(Math.max(a1, a2), Math.max(a3, a4));

					// compute aspect ratio of perfect hexa
					double arPer = 18.0 / max;

					// normalize element's aspect ratio
					arEl = arEl / arPer;

					// add to removal elements
					if (arEl > val + 1.0)
						elements.add(e);
				}
			}
		}

		// loop over removal elements
		for (int i = 0; i < elements.size(); i++) {

			// get index of removal element
			int index = owner_.structure_.indexOfElement(elements.get(i));

			// remove element from structure
			owner_.structure_.removeElement(index);
		}

		// return message
		return elements.size() + " elements with bad aspect ratio removed!";
	}

	/**
	 * Removes three dimensional elements with zero volume in the mesh.
	 * 
	 * @return Message indicating the number of removed elements.
	 */
	private String removeZeroVolume() {

		// start operation
		progressor_.setStatusMessage("Removing elements with zero volume...");

		// initialize vector for storing removal elements
		Vector<Element> elements = new Vector<Element>();

		// loop over elements
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// three dimensional
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// write process message
				progressor_.setStatusMessage("Checking element - " + i + "...");

				// check volume
				if (e.getVolume() <= 0.0)
					elements.add(e);
			}
		}

		// loop over removal elements
		for (int i = 0; i < elements.size(); i++) {

			// get index of removal element
			int index = owner_.structure_.indexOfElement(elements.get(i));

			// remove element from structure
			owner_.structure_.removeElement(index);
		}

		// return message
		return elements.size() + " elements with zero volume removed!";
	}

	/**
	 * Removes inside out elements in the mesh. Inside out elements are
	 * three-dimensional elements with negative jacobian.
	 * 
	 * @return Message indicating the number of removed elements.
	 */
	private String removeInsideOut() {

		// start operation
		progressor_.setStatusMessage("Removing inside out elements...");

		// initialize vector for storing removal elements
		Vector<Element3D> elements = new Vector<Element3D>();

		// loop over elements
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// three dimensional
			if (e.getDimension() == ElementLibrary.threeDimensional_) {

				// write process message
				progressor_.setStatusMessage("Checking element - " + i + "...");

				// get three dimensional element
				Element3D e3D = (Element3D) e;

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

				// tetra geometry
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
				int m = 0;
				for (int j = 0; j < eps1.length; j++) {

					// compute the jacobian and determinant
					double jacDet = e3D.getJacobian(eps1[j], eps2[j], eps3[j])
							.determinant();

					// check if it is negative or zero
					if (jacDet <= 0.0)
						m++;
				}

				// add to removal elements
				if (m > 0)
					elements.add(e3D);
			}
		}

		// loop over removal elements
		for (int i = 0; i < elements.size(); i++) {

			// get index of removal element
			int index = owner_.structure_.indexOfElement(elements.get(i));

			// remove element from structure
			owner_.structure_.removeElement(index);
		}

		// return message
		return elements.size() + " inside out elements removed!";
	}

	/**
	 * Removes upside down elements in the mesh. Upside down elements are
	 * two-dimensional elements with negative jacobian.
	 * 
	 * @return Message indicating the number of removed elements.
	 */
	private String removeUpsideDown() {

		// start operation
		progressor_.setStatusMessage("Removing upside down elements...");

		// initialize vector for storing removal elements
		Vector<Element2D> elements = new Vector<Element2D>();

		// loop over elements
		for (int i = 0; i < owner_.structure_.getNumberOfElements(); i++) {

			// get element
			Element e = owner_.structure_.getElement(i);

			// two dimensional
			if (e.getDimension() == ElementLibrary.twoDimensional_) {

				// write process message
				progressor_.setStatusMessage("Checking element - " + i + "...");

				// get two dimensional element
				Element2D e2D = (Element2D) e;

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
				int m = 0;
				for (int j = 0; j < eps1.length; j++) {

					// compute the jacobian and determinant
					double jacDet = e2D.getJacobian(eps1[j], eps2[j])
							.determinant();

					// check if it is negative
					if (jacDet < 0.0)
						m++;
				}

				// add to removal elements
				if (m > 0)
					elements.add(e2D);
			}
		}

		// loop over removal elements
		for (int i = 0; i < elements.size(); i++) {

			// get index of removal element
			int index = owner_.structure_.indexOfElement(elements.get(i));

			// remove element from structure
			owner_.structure_.removeElement(index);
		}

		// return message
		return elements.size() + " upside down elements removed!";
	}
}
