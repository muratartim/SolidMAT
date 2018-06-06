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
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Vector;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import data.Group;
import dialogs.file.FFilter1;
import dialogs.file.FView1;

import analysis.Structure;
import element.Element;
import node.Node;
import main.Commons;
// import main.ImageHandler;
import main.SolidMAT;
import main.Progressor;
import main.SwingWorker;
import matrix.DVec;

/**
 * Class for Import Model menu.
 * 
 * @author Murat
 * 
 */
public class ImportModel1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	/** The tolerance for searching activities. */
	private static final double tolerance_ = Math.pow(10, -8);

	private JTextField textfield1_, textfield2_, textfield3_, textfield4_,
			textfield5_, textfield6_, textfield7_;

	private JButton button1_, button2_, button3_;

	private JComboBox combobox1_;

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
	public ImportModel1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Import Sub-Model", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Sub-Model File", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Options", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("File :");
		JLabel label2 = new JLabel("Collect under :");
		JLabel label3 = new JLabel("Base point (Current) :");
		JLabel label4 = new JLabel("Base point (Sub) :");
		JLabel label5 = new JLabel("     X");
		JLabel label6 = new JLabel("     Y");
		JLabel label7 = new JLabel("     Z");

		// build text fields
		textfield1_ = new JTextField();
		textfield2_ = new JTextField();
		textfield3_ = new JTextField();
		textfield4_ = new JTextField();
		textfield5_ = new JTextField();
		textfield6_ = new JTextField();
		textfield7_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(141, 20));
		textfield2_.setPreferredSize(new Dimension(40, 20));
		textfield3_.setPreferredSize(new Dimension(40, 20));
		textfield4_.setPreferredSize(new Dimension(40, 20));

		// build buttons
		button1_ = new JButton("Browse");
		button2_ = new JButton("  OK  ");
		button3_ = new JButton("Cancel");

		// get groups
		String[] groups = getGroups();

		// build comboboxes
		combobox1_ = new JComboBox(groups);
		combobox1_.setMaximumRowCount(3);
		if (groups.length == 0)
			combobox1_.setEnabled(false);

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel3, button1_, 0, 2, 1, 1);
		Commons.addComponent(panel4, label2, 0, 0, 1, 1);
		Commons.addComponent(panel4, combobox1_, 0, 1, 3, 1);
		Commons.addComponent(panel4, label5, 1, 1, 1, 1);
		Commons.addComponent(panel4, label6, 1, 2, 1, 1);
		Commons.addComponent(panel4, label7, 1, 3, 1, 1);
		Commons.addComponent(panel4, label3, 2, 0, 1, 1);
		Commons.addComponent(panel4, label4, 3, 0, 1, 1);
		Commons.addComponent(panel4, textfield2_, 2, 1, 1, 1);
		Commons.addComponent(panel4, textfield3_, 2, 2, 1, 1);
		Commons.addComponent(panel4, textfield4_, 2, 3, 1, 1);
		Commons.addComponent(panel4, textfield5_, 3, 1, 1, 1);
		Commons.addComponent(panel4, textfield6_, 3, 2, 1, 1);
		Commons.addComponent(panel4, textfield7_, 3, 3, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		panel2.add(button2_);
		panel2.add(button3_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// browse button clicked
		if (e.getSource().equals(button1_))
			browse();

		// ok button clicked
		else if (e.getSource().equals(button2_)) {

			// initialize thread for the task to be performed
			final SwingWorker worker = new SwingWorker() {
				public Object construct() {
					importModel();
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
		else if (e.getSource().equals(button3_))
			setVisible(false);
	}

	/**
	 * Performs task for ok button.
	 * 
	 */
	private void importModel() {

		// check if any group available
		if (combobox1_.isEnabled() == false) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this, "No group available!",
					"False data entry", 2);
		}

		// group available
		else {

			// check texts
			progressor_.setStatusMessage("Checking data...");
			if (checkTexts()) {

				// read model
				progressor_.setStatusMessage("Reading structure...");
				Structure slave = readModel();
				if (slave != null) {

					// check models
					if (checkModels(slave)) {

						// import model
						progressor_.setStatusMessage("Importing model...");
						importModel1(slave);

						// sweep duplicate nodes from master model
						progressor_
								.setStatusMessage("Sweeping duplicate nodes...");
						duplicateNodes();

						// sweep duplicate elements from master model
						progressor_
								.setStatusMessage("Sweeping duplicate elements...");
						duplicateElements();

						// sweep unused nodes from master model
						progressor_
								.setStatusMessage("Sweeping unused nodes...");
						unusedNodes();

						// draw
						progressor_.setStatusMessage("Drawing...");
						owner_.drawPre();

						// close progressor and dialog
						progressor_.close();
						setVisible(false);
					}
				}
			}
		}
	}

	/**
	 * Identifies and sweeps unused nodes.
	 * 
	 */
	private void unusedNodes() {

		// initialize would be removed nodes vector
		Vector<Node> nodes1 = new Vector<Node>();

		// loop over nodes
		nodes1.clear();
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
				nodes1.add(node);
		}

		// remove unused nodes from structure and groups
		for (int i = 0; i < nodes1.size(); i++) {
			int index = owner_.structure_.indexOfNode(nodes1.get(i));
			owner_.structure_.removeNode(index);
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains node
				if (group.containsNode(nodes1.get(i)))
					group.removeNode(nodes1.get(i));
			}
		}
	}

	/**
	 * Identifies and sweeps duplicate elements.
	 * 
	 */
	private void duplicateElements() {

		// initialize would be removed elements vector
		Vector<Element> elements1 = new Vector<Element>();

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
						if (elements1.contains(e) == false) {

							// add to would be removed elements list
							elements1.add(e);
						}
					}
				}
			}
		}

		// remove duplicate elements from structure and groups
		for (int i = 0; i < elements1.size(); i++) {
			int index = owner_.structure_.indexOfElement(elements1.get(i));
			owner_.structure_.removeElement(index);
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains element
				if (group.containsElement(elements1.get(i)))
					group.removeElement(elements1.get(i));
			}
		}
	}

	/**
	 * Identifies and sweeps duplicate nodes.
	 * 
	 */
	private void duplicateNodes() {

		// initialize would be removed nodes vector
		Vector<Node> nodes1 = new Vector<Node>();

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
					if (nodes1.contains(node2) == false) {

						// add to would be removed nodes list
						nodes1.add(node2);

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
		for (int i = 0; i < nodes1.size(); i++) {
			int index = owner_.structure_.indexOfNode(nodes1.get(i));
			owner_.structure_.removeNode(index);
			for (int j = 0; j < owner_.inputData_.getGroup().size(); j++) {

				// get group
				Group group = owner_.inputData_.getGroup().get(j);

				// check if group contains node
				if (group.containsNode(nodes1.get(i)))
					group.removeNode(nodes1.get(i));
			}
		}
	}

	/**
	 * Imports elements and nodes of slave to master model.
	 * 
	 * @param slave
	 *            Slave model.
	 */
	private void importModel1(Structure slave) {

		// get selected group
		int index = combobox1_.getSelectedIndex();
		Group collect = owner_.inputData_.getGroup().get(index);

		// get base points
		DVec p1 = new DVec(3);
		DVec p2 = new DVec(3);
		p1.set(0, Double.parseDouble(textfield2_.getText()));
		p1.set(1, Double.parseDouble(textfield3_.getText()));
		p1.set(2, Double.parseDouble(textfield4_.getText()));
		p2.set(0, Double.parseDouble(textfield5_.getText()));
		p2.set(1, Double.parseDouble(textfield6_.getText()));
		p2.set(2, Double.parseDouble(textfield7_.getText()));
		p1 = p1.subtract(p2);

		// loop over nodes of slave model
		for (int i = 0; i < slave.getNumberOfNodes(); i++) {

			// get node
			Node node = slave.getNode(i);

			// set new position
			node.setPosition(p1.add(node.getPosition()));

			// add to master model
			owner_.structure_.addNode(node);

			// add to group
			collect.addNode(node);
		}

		// loop over elements of slave model
		for (int i = 0; i < slave.getNumberOfElements(); i++) {

			// get element
			Element e = slave.getElement(i);

			// add to master model
			owner_.structure_.addElement(e);

			// add to group
			collect.addElement(e);
		}
	}

	/**
	 * Checks both slave and master structures.
	 * 
	 * @param slave
	 *            Slave structure.
	 * @return True if no problem, False if problem occured.
	 */
	private boolean checkModels(Structure slave) {

		// check slave model
		progressor_.setStatusMessage("Checking imported model...");
		for (int i = 1; i < 5; i++) {
			String msg = slave.checkModel(i);

			// problem ocurred
			if (msg != null) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, msg, "Error", 2);
				return false;
			}
		}

		// check master model
		progressor_.setStatusMessage("Checking current model...");
		for (int i = 2; i < 5; i++) {
			String msg = owner_.structure_.checkModel(i);

			// problem ocurred
			if (msg != null) {

				// close progressor
				progressor_.close();
				setStill(false);

				// display message
				JOptionPane.showMessageDialog(this, msg, "Error", 2);
				return false;
			}
		}

		// no problem ocurred
		return true;
	}

	/**
	 * Reads and returns slave model.
	 * 
	 * @return Slave model.
	 */
	private Structure readModel() {

		// set error messages
		String err1 = "File not found!";
		String err2 = "Cannot read file!";
		String err3 = "Cannot process file!";

		// initialize structure
		Structure s = null;

		// initialize input stream
		ObjectInputStream in = null;

		// read file
		try {

			// get the path
			String path = textfield1_.getText();

			// create input stream
			in = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(path)));

			// read input data
			in.readObject();

			// read structure
			s = (Structure) in.readObject();
		}

		// file not found
		catch (FileNotFoundException e) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(owner_.viewer_, err1, "Exception", 2);
			return null;
		}

		// cannot read file
		catch (IOException e) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(owner_.viewer_, err2, "Exception", 2);
			return null;
		}

		// cannot process file
		catch (ClassNotFoundException e) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(owner_.viewer_, err3, "Exception", 2);
			return null;
		}

		// cannot process file
		catch (ClassCastException e) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(owner_.viewer_, err3, "Exception", 2);
			return null;
		}

		// close input stream
		finally {

			// check if the input stream is null
			if (in != null) {
				try {

					// close
					in.close();
				} catch (IOException io) {
				}
			}
		}

		// return structure
		return s;
	}

	/**
	 * Checks whether the delta textfields are correct or not.
	 * 
	 * @return True if they are correct, False if not.
	 */
	private boolean checkTexts() {

		// check for non-numeric values
		try {

			// convert texts to double values
			double[] values = new double[6];
			values[0] = Double.parseDouble(textfield2_.getText());
			values[1] = Double.parseDouble(textfield3_.getText());
			values[2] = Double.parseDouble(textfield4_.getText());
			values[3] = Double.parseDouble(textfield5_.getText());
			values[4] = Double.parseDouble(textfield6_.getText());
			values[5] = Double.parseDouble(textfield7_.getText());
		} catch (Exception excep) {

			// close progressor
			progressor_.close();
			setStill(false);

			// display message
			JOptionPane.showMessageDialog(this,
					"Illegal values for base points!", "False data entry", 2);
			return false;
		}

		// entered values are correct
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
			button3_.setEnabled(false);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		}

		// activate
		else {

			// enable buttons
			button1_.setEnabled(true);
			button2_.setEnabled(true);
			button3_.setEnabled(true);

			// set window close operation
			setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		}
	}

	/**
	 * Performs task for the browse button.
	 * 
	 */
	private void browse() {

		// create file chooser
		JFileChooser chooser = new JFileChooser();

		// add custom file filter
		chooser.addChoosableFileFilter(new FFilter1());

		// disable the default (Accept All) file filter.
		chooser.setAcceptAllFileFilterUsed(false);

		// add custom icons for file types.
		chooser.setFileView(new FView1());

		// show file chooser
		int val = chooser.showDialog(this, "Open");

		// open approved
		if (val == JFileChooser.APPROVE_OPTION) {

			// get selected file's path
			String path = chooser.getSelectedFile().getAbsolutePath();

			// append extension if necessary
			String extension = ".smt";
			if (path.length() >= extension.length() + 1)
				if (extension.equalsIgnoreCase(path
						.substring(path.length() - 4)) == false)
					path += extension;

			// set path to textfield
			textfield1_.setText(path);
		}
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
