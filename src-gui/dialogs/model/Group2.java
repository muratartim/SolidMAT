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
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import main.Commons;
// import main.ImageHandler;
import node.Node;

import java.util.Vector;

/**
 * Class for Add/Modify Groups menu.
 * 
 * @author Murat
 * 
 */
public class Group2 extends JDialog implements ActionListener, FocusListener {

	private static final long serialVersionUID = 1L;

	private JTextField textfield1_;

	private JTextArea textArea1_, textArea2_;

	/** Vector for storing nodes of group. */
	private Vector<Node> nodes_ = new Vector<Node>();

	/** Vector for storing elements of group. */
	private Vector<Element> elements_ = new Vector<Element>();

	/** Used for determining if add or modify button clicked from mother dialog. */
	private boolean add_;

	/** Mother dialog of this dialog. */
	private Group1 owner_;

	/**
	 * Builds dialog, builds components, calls addComponent, sets layout and
	 * sets up listeners.
	 * 
	 * @param owner
	 *            Dialog to be the owner of this dialog.
	 * @param add
	 *            Used for determining if add or modify button clicked from
	 *            mother dialog.
	 */
	public Group2(Group1 owner, boolean add) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner, "Add/Modify Groups", true);
		owner_ = owner;
		add_ = add;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = Commons.getPanel(null, Commons.gridbag_);
		JPanel panel2 = Commons.getPanel(null, Commons.flow_);

		// build sub-panels
		JPanel panel3 = Commons.getPanel("Name", Commons.gridbag_);
		JPanel panel4 = Commons.getPanel("Nodes", Commons.gridbag_);
		JPanel panel5 = Commons.getPanel("Elements", Commons.gridbag_);

		// build labels
		JLabel label1 = new JLabel("Name :");

		// build text fields and set font
		textfield1_ = new JTextField();
		textfield1_.setPreferredSize(new Dimension(299, 20));

		// build text areas
		textArea1_ = new JTextArea(10, 40);
		textArea2_ = new JTextArea(10, 40);
		textArea1_.setLineWrap(true);
		textArea1_.setWrapStyleWord(true);
		textArea2_.setLineWrap(true);
		textArea2_.setWrapStyleWord(true);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(textArea1_);
		JScrollPane scrollpane2 = new JScrollPane(textArea2_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);
		scrollpane2.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane2.setHorizontalScrollBarPolicy(horizontalConstant);

		// build buttons and set font
		JButton button1 = new JButton("  OK  ");
		JButton button2 = new JButton("Cancel");

		// add components to sub-panels
		Commons.addComponent(panel3, label1, 0, 0, 1, 1);
		Commons.addComponent(panel3, textfield1_, 0, 1, 1, 1);
		Commons.addComponent(panel4, scrollpane1, 0, 0, 1, 1);
		Commons.addComponent(panel5, scrollpane2, 0, 0, 1, 1);

		// add sub-panels to main panels
		Commons.addComponent(panel1, panel3, 0, 0, 1, 1);
		Commons.addComponent(panel1, panel4, 1, 0, 1, 1);
		Commons.addComponent(panel1, panel5, 2, 0, 1, 1);
		panel2.add(button1);
		panel2.add(button2);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1.addActionListener(this);
		button2.addActionListener(this);
		textfield1_.addFocusListener(this);

		// If add is clicked set default, if not initialize
		if (add_)
			setDefaultText();
		else
			initialize();

		// call visualize
		Commons.visualize(this);
	}

	/**
	 * Initializes the components if modify button has been clicked from the
	 * mother dialog.
	 */
	private void initialize() {

		// get index of selected item in list of mother dialog
		int index = owner_.list1_.getSelectedIndex();

		// get the selected object
		Group selected = owner_.temporary_.get(index);

		// get name
		String name = selected.getName();

		// get ids of nodes
		Vector<Node> nodes = selected.getNodes();
		Vector<Integer> nodeIds = new Vector<Integer>();
		for (int i = 0; i < nodes.size(); i++) {

			// get the node
			Node node = nodes.get(i);

			// check if structure contains the node
			if (owner_.owner_.structure_.containsNode(node)) {

				// add ids to nodeIds vector
				nodeIds.add(owner_.owner_.structure_.indexOfNode(node));
			}
		}

		// get ids of elements
		Vector<Element> elements = selected.getElements();
		Vector<Integer> elementIds = new Vector<Integer>();
		for (int i = 0; i < elements.size(); i++) {

			// get the element
			Element e = elements.get(i);

			// check if structure contains the element
			if (owner_.owner_.structure_.containsElement(e)) {

				// add ids to elementIds vector
				elementIds.add(owner_.owner_.structure_.indexOfElement(e));
			}
		}

		// create string for storing node and element ids
		String nIds = "";
		String eIds = "";
		for (int i = 0; i < nodeIds.size(); i++) {
			if (i == 0)
				nIds += nodeIds.get(i).toString();
			else
				nIds += "," + nodeIds.get(i).toString();
		}
		for (int i = 0; i < elementIds.size(); i++) {
			if (i == 0)
				eIds += elementIds.get(i).toString();
			else
				eIds += "," + elementIds.get(i).toString();
		}

		// set textfields
		textfield1_.setText(name);
		textArea1_.append(nIds);
		textArea2_.append(eIds);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// ok button clicked
		if (e.getActionCommand() == "  OK  ") {

			// call actionOk
			actionOk();
		}

		// cancel button clicked
		else if (e.getActionCommand() == "Cancel") {

			// set dialog unvisible
			setVisible(false);
		}
	}

	/**
	 * Calls either actionOkAdd or actionOkModify according to the button that
	 * has been clicked in mother dialog, then sets dialog unvisible.
	 */
	private void actionOk() {

		// add button clicked from the mother dialog
		if (add_) {

			// check name, nodes and elements
			if (checkName(1) && checkNode() && checkElement()) {
				actionOkAddModify();
				setVisible(false);
			}
		}

		// modify button is clicked from mother dialog
		else if (add_ == false) {

			// get selected item of list
			String selected = owner_.list1_.getSelectedValue().toString();

			// check if name textfield is equal to selected item of list
			if (textfield1_.getText().equals(selected)) {

				// check nodes and elements
				if (checkNode() && checkElement()) {
					actionOkAddModify();
					setVisible(false);
				}
			} else {

				// check name, nodes and elements
				if (checkName(1) && checkNode() && checkElement()) {
					actionOkAddModify();
					setVisible(false);
				}
			}
		}
	}

	/**
	 * Creates object and adds/sets it to temporary vector.
	 * 
	 */
	private void actionOkAddModify() {

		// get name
		String name = textfield1_.getText();

		// create object
		Group object = new Group(name);
		object.setNodes(nodes_);
		object.setElements(elements_);

		// add button clicked
		if (add_) {

			// add object to temporary vector and names to list
			owner_.temporary_.addElement(object);
			owner_.listModel1_.addElement(name);
		}

		// modify button clicked
		else if (add_ == false) {

			// set object to temporary vector and names to list
			int index = owner_.list1_.getSelectedIndex();
			owner_.temporary_.setElementAt(object, index);
			owner_.listModel1_.setElementAt(name, index);
		}
	}

	/**
	 * Checks for false data entries in textfields.
	 */
	public void focusLost(FocusEvent e) {

		try {

			// check if focuslost is triggered from other applications
			if (e.getOppositeComponent().equals(null) == false) {

				// check if no name given
				if (checkName(0) == false)
					setDefaultText();
			}
		} catch (Exception excep) {
		}
	}

	/**
	 * Checks name taxtfield for no name and name exists errors.
	 * 
	 * @param messageType
	 *            0 -> no name given, 1 -> name exists.
	 * @return True if name textfield is correct, False vice versa.
	 */
	private boolean checkName(int messageType) {

		// get the entered text
		String text = textfield1_.getText();

		// No name given
		if (messageType == 0) {

			// check if no name given
			if (text.equals("")) {

				// display message
				JOptionPane.showMessageDialog(Group2.this, "No name given!",
						"False data entry", 2);
				return false;
			}
		}

		// Name exists
		else if (messageType == 1) {

			// check if name exists in list of mother dialog
			if (owner_.listModel1_.contains(text)) {

				// display message
				JOptionPane.showMessageDialog(Group2.this,
						"Name already exists!", "False data entry", 2);
				return false;
			}
		}

		// the data is correct
		return true;
	}

	/**
	 * Checks whether any node of the structure has the given id or not. And
	 * also checks for other invalid values entered.
	 * 
	 * @return True if the given node ids are valid for addition to group, False
	 *         vice versa.
	 */
	private boolean checkNode() {

		// get the entered text
		String text = textArea1_.getText();

		// check if any node given
		if (text.equalsIgnoreCase(""))
			return true;

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
			Vector<Integer> nodeIds = new Vector<Integer>();
			for (int i = 0; i < comp.length; i++) {
				nodeIds.add(Integer.parseInt(comp[i]));

				// check if negative
				if (nodeIds.get(i) < 0) {
					JOptionPane.showMessageDialog(Group2.this,
							"Illegal values!", "False data entry", 2);
					return false;
				}
			}

			// eliminate duplicate values
			for (int i = 0; i < nodeIds.size() - 1; i++) {
				for (int j = i + 1; j < nodeIds.size(); j++) {
					int k = nodeIds.get(i);
					int l = nodeIds.get(j);
					if (k == l)
						nodeIds.remove(j);
				}
			}

			// check if given nodes exist
			nodes_.clear();
			for (int i = 0; i < nodeIds.size(); i++)
				nodes_.add(owner_.owner_.structure_.getNode(nodeIds.get(i)));
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(Group2.this,
					"Given nodes do not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Checks whether any element of the structure has the given coordinates or
	 * not.
	 * 
	 * @return True if the given element ids are valid for addition to group,
	 *         False vice versa.
	 */
	private boolean checkElement() {

		// get the entered text
		String text = textArea2_.getText();

		// check if any element given
		if (text.equalsIgnoreCase(""))
			return true;

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
			Vector<Integer> elementIds = new Vector<Integer>();
			for (int i = 0; i < comp.length; i++) {
				elementIds.add(Integer.parseInt(comp[i]));

				// check if negative
				if (elementIds.get(i) < 0) {
					JOptionPane.showMessageDialog(Group2.this,
							"Illegal values!", "False data entry", 2);
					return false;
				}
			}

			// eliminate duplicate values
			for (int i = 0; i < elementIds.size() - 1; i++) {
				for (int j = i + 1; j < elementIds.size(); j++) {
					int k = elementIds.get(i);
					int l = elementIds.get(j);
					if (k == l)
						elementIds.remove(j);
				}
			}

			// check if given elements exist
			elements_.clear();
			for (int i = 0; i < elementIds.size(); i++)
				elements_.add(owner_.owner_.structure_.getElement(elementIds
						.get(i)));
		} catch (Exception excep) {

			// display message
			JOptionPane.showMessageDialog(Group2.this,
					"Given elements do not exist!", "False data entry", 2);
			return false;
		}

		// entered values are correct
		return true;
	}

	/**
	 * Sets default values for textfields.
	 * 
	 */
	private void setDefaultText() {

		// The default values for textfields
		String defaultName = "Group1";

		// set to textfield1
		textfield1_.setText(defaultName);
	}

	/**
	 * Unused method.
	 */
	public void focusGained(FocusEvent e) {
	}
}
