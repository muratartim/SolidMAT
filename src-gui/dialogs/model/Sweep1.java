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

import data.Group;
import element.Element;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
 * Class for Sweep Model menu.
 * 
 * @author Murat
 * 
 */
public class Sweep1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private JCheckBox checkbox1_, checkbox2_, checkbox3_;

	private JTextField textfield1_;

	/** Vector for storing nodes to be moved. */
	private Vector<Node> nodes1_ = new Vector<Node>();

	/** Vector for storing elements to be moved. */
	private Vector<Element> elements1_ = new Vector<Element>();

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
	public Sweep1(SolidMAT owner) {

		// build dialog, determine owner frame, give caption, make it modal
		super(owner.viewer_, "Sweep", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Tolerance :");

		// build text fields and set font
		textfield1_ = new JTextField(Double.toString(Math.pow(10, -8)));
		textfield1_.setEnabled(false);
		textfield1_.setPreferredSize(new Dimension(80, 20));

		// build checkboxes and set font
		checkbox1_ = new JCheckBox("Duplicate nodes");
		checkbox2_ = new JCheckBox("Duplicate elements");
		checkbox3_ = new JCheckBox("Unused nodes");

		// build buttons
		button1_ = new JButton("  OK  ");
		button2_ = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 3, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 3, 1, 1, 1);
		Commons.addComponent(panel3, checkbox1_, 0, 0, 2, 1);
		Commons.addComponent(panel3, checkbox2_, 1, 0, 2, 1);
		Commons.addComponent(panel3, checkbox3_, 2, 0, 2, 1);

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
		checkbox1_.addActionListener(this);

		// visualize
		Commons.visualize(this);
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

						// initialize message
						String message = null;

						// duplicate nodes
						progressor_
								.setStatusMessage("Sweeping duplicate nodes...");
						if (checkbox1_.isSelected())
							message = duplicateNodes(message);

						// duplicate elements
						progressor_
								.setStatusMessage("Sweeping duplicate elements...");
						if (checkbox2_.isSelected())
							message = duplicateElements(message);

						// unused nodes
						progressor_
								.setStatusMessage("Sweeping unused nodes...");
						if (checkbox3_.isSelected())
							message = unusedNodes(message);

						// draw
						progressor_.setStatusMessage("Drawing...");
						owner_.drawPre();

						// close progressor
						progressor_.close();

						// check message
						if (message != null) {

							// display message
							JOptionPane.showMessageDialog(Sweep1.this, message,
									"Sweep", JOptionPane.INFORMATION_MESSAGE);
						}

						// close dialog
						setVisible(false);
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

		// checkbox button clicked
		else if (e.getSource().equals(checkbox1_)) {

			// selected
			if (checkbox1_.isSelected())
				textfield1_.setEnabled(true);
			else
				textfield1_.setEnabled(false);
		}
	}

	/**
	 * Checks tolerance textfield before executing code.
	 * 
	 * @return True if no ptoblem with tolerance data, False vice versa.
	 */
	private boolean checkText() {

		// get the entered text
		String text = textfield1_.getText();

		// check for non-numeric values-zero or negative
		try {

			// convert text to double value
			double val = Double.parseDouble(text);

			// check zero or negative
			if (val <= 0.0) {

				// display message
				JOptionPane.showMessageDialog(this,
						"Tolerance should be greater than zero!",
						"False data entry", 2);
				return false;
			}
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(this, "Illegal value for tolerance!",
					"False data entry", 2);
			return false;
		}

		// entered value is correct
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
	 * Identifies and sweeps unused nodes.
	 * 
	 * @param message
	 *            The message to be displayed.
	 * @return The message to be displayed.
	 */
	private String unusedNodes(String message) {

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

		// remove unused nodes from structure and groups
		for (int i = 0; i < nodes1_.size(); i++) {
			int index = owner_.structure_.indexOfNode(nodes1_.get(i));
			owner_.structure_.removeNode(index);
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains node
				if (group.containsNode(nodes1_.get(i)))
					group.removeNode(nodes1_.get(i));
			}
		}

		// set message and return
		if (message != null)
			message += "\n" + nodes1_.size() + " unused nodes removed!";
		else if (message == null)
			message = nodes1_.size() + " unused nodes removed!";
		return message;
	}

	/**
	 * Identifies and sweeps duplicate elements.
	 * 
	 * @param message
	 *            The message to be displayed.
	 * @return The message to be displayed.
	 */
	private String duplicateElements(String message) {

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

		// remove duplicate elements from structure and groups
		for (int i = 0; i < elements1_.size(); i++) {
			int index = owner_.structure_.indexOfElement(elements1_.get(i));
			owner_.structure_.removeElement(index);
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains element
				if (group.containsElement(elements1_.get(i)))
					group.removeElement(elements1_.get(i));
			}
		}

		// set message and return
		if (message != null)
			message += "\n" + elements1_.size()
					+ " duplicate elements removed!";
		else if (message == null)
			message = elements1_.size() + " duplicate elements removed!";
		return message;
	}

	/**
	 * Identifies and sweeps duplicate nodes.
	 * 
	 * @param message
	 *            The message to be displayed.
	 * @return The message to be displayed.
	 */
	private String duplicateNodes(String message) {

		// get tolerance
		double tol = Double.parseDouble(textfield1_.getText());

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
				if (dif.l2Norm() <= tol) {
					if (nodes1_.contains(node2) == false) {

						// add to would be removed nodes list
						nodes1_.add(node2);

						// loop over elements
						for (int k = 0; k < owner_.structure_
								.getNumberOfElements(); k++) {

							// get nodes of element
							Node[] nodes = owner_.structure_.getElement(k)
									.getNodes();

							// loop over nodes of element
							for (int l = 0; l < nodes.length; l++) {

								// check if it contains the node
								if (nodes[l].equals(node2))
									nodes[l] = node1;
							}
						}
					}
				}
			}
		}

		// remove duplicate nodes from structure and groups
		for (int i = 0; i < nodes1_.size(); i++) {
			int index = owner_.structure_.indexOfNode(nodes1_.get(i));
			owner_.structure_.removeNode(index);
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains node
				if (group.containsNode(nodes1_.get(i)))
					group.removeNode(nodes1_.get(i));
			}
		}

		// set message and return
		message = nodes1_.size() + " duplicate nodes removed!";
		return message;
	}
}
