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

import element.Element;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
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
 * Class for Edit Node Model menu.
 * 
 * @author Murat
 * 
 */
public class EditNode1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	/** The node to be edited. */
	private Node node_;

	/** The new coordinates of node. */
	private double[] coord_ = new double[3];

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
	public EditNode1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Edit Node", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Node", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("New Coordinates", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Node ID :");
		JLabel label2 = new JLabel("X coordinate :");
		JLabel label3 = new JLabel("Y coordinate :");
		JLabel label4 = new JLabel("Z coordinate :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(121, 20));
		textfield2_.setPreferredSize(new Dimension(100, 20));

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, label3, 1, 0, 1, 1);
		Commons.addComponent(panel4, label4, 2, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 0, 1, 1, 1);
		Commons.addComponent(panel4, textfield3_, 1, 1, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 1, 1, 1);

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

		// check node id, coordinates and connectivity
		progressor_.setStatusMessage("Checking data...");
		if (checkId() && checkCoordinates() && checkConnectivity()) {

			// set new position vector to the node
			node_.setPosition(new DVec(coord_));

			// draw
			progressor_.setStatusMessage("Drawing...");
			owner_.drawPre();

			// close progressor
			progressor_.close();

			// close dialog
			setVisible(false);
		}
	}

	/**
	 * Checks the given node id for existance.
	 * 
	 * @return True if node exists, False vice versa.
	 */
	private boolean checkId() {

		// get the entered text
		String text = textfield1_.getText();

		// check for non-numeric or negative values
		try {

			// convert text to integer value
			int id = Integer.parseInt(text);

			// check if negative
			if (id < 0) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(EditNode1.this,
						"Illegal node ID!", "False data entry", 2);
				return false;
			}

			// check if given node exists
			node_ = owner_.structure_.getNode(id);
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(EditNode1.this,
					"Given node does not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks structure for any node having the given coordinates.
	 * 
	 * @return True if no node is at the given coordinates, False vice versa.
	 */
	private boolean checkCoordinates() {

		// get textfields
		String x = textfield2_.getText();
		String y = textfield3_.getText();
		String z = textfield4_.getText();

		// check for non-numeric values
		try {

			// convert text to double value
			coord_[0] = Double.parseDouble(x);
			coord_[1] = Double.parseDouble(y);
			coord_[2] = Double.parseDouble(z);
			DVec pos = new DVec(3);
			pos.set(0, coord_[0]);
			pos.set(1, coord_[1]);
			pos.set(2, coord_[2]);

			// check structure for any node having the same coordinates
			for (int i = 0; i < owner_.structure_.getNumberOfNodes(); i++) {

				// get nodal position vector
				DVec pos1 = owner_.structure_.getNode(i).getPosition();

				// check coordinates
				if (pos.subtract(pos1).l2Norm() <= tolerance_) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(EditNode1.this,
							"There exists a node at the given coordinates!",
							"False data entry", 2);
					return false;
				}
			}
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(EditNode1.this,
					"Illegal coordinate values!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks whether given node is connected to any element.
	 * 
	 * @return True if it is not connected to any element, False vice versa.
	 */
	private boolean checkConnectivity() {

		// loop over elements
		for (int j = 0; j < owner_.structure_.getNumberOfElements(); j++) {

			// get element and its nodes
			Element e = owner_.structure_.getElement(j);
			Node[] nodes = e.getNodes();

			// loop over nodes of element
			for (int k = 0; k < nodes.length; k++) {

				// check if node is the same with element node
				if (nodes[k].equals(node_)) {

					// close progressor
					progressor_.close();
					setStill(false);

					// display message
					JOptionPane.showMessageDialog(EditNode1.this,
							"Node is connected to elements!",
							"False data entry", 2);
					return false;
				}
			}
		}

		// node is not connected to any element
		return true;
	}
}
