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
package dialogs.view;


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
import javax.swing.DefaultListModel;
// import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

// import main.ImageHandler;
import main.SolidMAT;
import main.Commons;

/**
 * Class for Customize Toolbars View menu.
 * 
 * @author Murat
 * 
 */
public class CustomizeToolbars1 extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private GridBagLayout layout1_;

	private GridBagConstraints constraints1_;

	private JButton button1_, button2_, button3_, button4_;

	private JLabel label1_, label2_;

	private JList list1_;

	private DefaultListModel listModel1_;

	private JTree tree1_;

	/** Array storing the names of toolbars. */
	private String[] toolbars_;

	/** The owner frame of this dialog. */
	private SolidMAT owner_;

	/**
	 * Builds dialog, builds child dialog, builds components, calls
	 * addComponent, sets layout and sets up listeners.
	 * 
	 * @param owner
	 *            Frame to be the owner of this dialog.
	 */
	public CustomizeToolbars1(SolidMAT owner) {

		// build dialog, determine owner dialog, give caption, make it modal
		super(owner.viewer_, "Customize Toolbars", true);
		owner_ = owner;

		// set icon
		// ImageIcon image = ImageHandler.createImageIcon("SolidMAT2.jpg");
		// super.setIconImage(image.getImage());

		// build main panels
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();

		// build sub-panels
		JPanel panel3 = new JPanel();

		// build layout managers and set layout managers to panels
		layout1_ = new GridBagLayout();
		FlowLayout layout2 = new FlowLayout();
		panel1.setLayout(layout1_);
		panel2.setLayout(layout2);
		panel3.setLayout(layout1_);

		// set border to panels
		panel3.setBorder(BorderFactory.createTitledBorder("Toolbars"));

		// build gridbag constraints, make components extend in both directions
		// and determine gaps between components
		constraints1_ = new GridBagConstraints();
		constraints1_.insets = new Insets(5, 5, 5, 5);
		layout2.setAlignment(FlowLayout.CENTER);
		layout2.setHgap(5);
		layout2.setVgap(5);

		// build labels
		label1_ = new JLabel("All");
		label2_ = new JLabel("Selected");

		// build buttons
		button1_ = new JButton("    Add    ");
		button2_ = new JButton("Remove");
		button3_ = new JButton("  OK  ");
		button4_ = new JButton("Cancel");

		// build list
		listModel1_ = new DefaultListModel();
		list1_ = new JList(listModel1_);
		list1_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// build scroll pane and add list to it
		JScrollPane scrollpane1 = new JScrollPane(list1_);

		// set scrollpane constants
		int verticalConstant = ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;
		int horizontalConstant = ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
		scrollpane1.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane1.setHorizontalScrollBarPolicy(horizontalConstant);

		// create root node and build tree
		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Toolbars");
		createNodes(top);
		tree1_ = new JTree(top);
		tree1_.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);

		// build scroll panes and add lists to them
		JScrollPane scrollpane2 = new JScrollPane(tree1_);

		// set scrollpane constants
		scrollpane2.setVerticalScrollBarPolicy(verticalConstant);
		scrollpane2.setHorizontalScrollBarPolicy(horizontalConstant);

		// Create a split pane with the two scroll panes in it
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				scrollpane2, scrollpane1);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);
		splitPane.setPreferredSize(new Dimension(300, 200));

		// Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 200);
		scrollpane2.setMinimumSize(minimumSize);
		scrollpane1.setMinimumSize(minimumSize);

		// add components to sub-panels
		addComponent(panel3, label1_, 0, 0, 1, 1);
		addComponent(panel3, label2_, 0, 1, 1, 1);
		addComponent(panel3, splitPane, 1, 0, 2, 1);
		addComponent(panel3, button1_, 2, 0, 1, 1);
		addComponent(panel3, button2_, 2, 1, 1, 1);

		// add sub-panels to main panels
		addComponent(panel1, panel3, 0, 0, 1, 1);
		panel2.add(button3_);
		panel2.add(button4_);

		// set layout for dialog and add panels
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", panel1);
		getContentPane().add("South", panel2);

		// set up listeners for components
		button1_.addActionListener(this);
		button2_.addActionListener(this);
		button3_.addActionListener(this);
		button4_.addActionListener(this);

		// call initialize
		initialize();

		// call visualize
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

		if (component.equals(label1_) || component.equals(button1_)) {
			constraints1_.fill = GridBagConstraints.NONE;
			constraints1_.anchor = GridBagConstraints.WEST;
		}

		else if (component.equals(label2_) || component.equals(button2_)) {
			constraints1_.fill = GridBagConstraints.NONE;
			constraints1_.anchor = GridBagConstraints.EAST;
		}

		else
			constraints1_.fill = GridBagConstraints.BOTH;

		// set constraints and add component to panel1
		layout1_.setConstraints(component, constraints1_);
		panel.add(component);
	}

	/**
	 * Sets the input data vector to temporary vector. Copies names to list.
	 */
	private void initialize() {

		// set the toolbars to list
		JToolBar[] toolbars = owner_.toolbars_.getToolbars();
		for (int i = 0; i < toolbars.length; i++)
			if (toolbars[i].isVisible())
				listModel1_.addElement(toolbars[i].getName());
	}

	/**
	 * Creates the nodes under the root node.
	 * 
	 * @param top
	 *            Root node.
	 */
	private void createNodes(DefaultMutableTreeNode top) {

		// initialize nodes
		DefaultMutableTreeNode category = null;
		DefaultMutableTreeNode item1 = null;
		DefaultMutableTreeNode item2 = null;
		toolbars_ = new String[22];

		// create File category
		category = new DefaultMutableTreeNode("File");
		top.add(category);

		// add items of File category
		item1 = new DefaultMutableTreeNode("File1");
		category.add(item1);
		toolbars_[0] = "File1";
		item2 = new DefaultMutableTreeNode("New");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Open");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Save");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("File2");
		category.add(item1);
		toolbars_[1] = "File2";
		item2 = new DefaultMutableTreeNode("Write Output Tables");
		item1.add(item2);

		// create View category
		category = new DefaultMutableTreeNode("View");
		top.add(category);

		// add items of View category
		item1 = new DefaultMutableTreeNode("View1");
		category.add(item1);
		toolbars_[2] = "View1";
		item2 = new DefaultMutableTreeNode("3D Orbit");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Zoom Window");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Zoom In");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Zoom Out");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Zoom Extents");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("View2");
		category.add(item1);
		toolbars_[3] = "View2";
		item2 = new DefaultMutableTreeNode("Top View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Bottom View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Left View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Right View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Front View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Back View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("SW Isometric View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("SE Isometric View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("NE Isometric View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("NW Isometric View");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Menu");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("View3");
		category.add(item1);
		toolbars_[4] = "View3";
		item2 = new DefaultMutableTreeNode("Redraw");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Set Background");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Axes Visible");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("View4");
		category.add(item1);
		toolbars_[5] = "View4";
		item2 = new DefaultMutableTreeNode("Customize Toolbars");
		item1.add(item2);

		// create Library category
		category = new DefaultMutableTreeNode("Library");
		top.add(category);

		// add items of Library category
		item1 = new DefaultMutableTreeNode("Library1");
		category.add(item1);
		toolbars_[6] = "Library1";
		item2 = new DefaultMutableTreeNode("Elements");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Materials");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Sections");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Local Axes");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Solvers");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Functions");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Library2");
		category.add(item1);
		toolbars_[7] = "Library2";
		item2 = new DefaultMutableTreeNode("Boundary Cases");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Analysis Cases");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Library3");
		category.add(item1);
		toolbars_[8] = "Library3";
		item2 = new DefaultMutableTreeNode("Constraints");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Nodal Displacements");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Nodal Mechanical Loads");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Element Mechanical Loads");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Element Temperature Loads");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Library4");
		category.add(item1);
		toolbars_[9] = "Library4";
		item2 = new DefaultMutableTreeNode("Nodal Masses");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Element Masses");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Nodal Stiffness");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Element Stiffness");
		item1.add(item2);

		// create Model category
		category = new DefaultMutableTreeNode("Model");
		top.add(category);

		// add items of Model category
		item1 = new DefaultMutableTreeNode("Model1");
		category.add(item1);
		toolbars_[10] = "Model1";
		item2 = new DefaultMutableTreeNode("Add Node");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Add Element");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Remove Node");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Remove Element");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Edit Node");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Edit Element");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Show Node");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Show Element");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Model2");
		category.add(item1);
		toolbars_[11] = "Model2";
		item2 = new DefaultMutableTreeNode("Divide Lines");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Mesh Areas");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Mesh Solids");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Model3");
		category.add(item1);
		toolbars_[12] = "Model3";
		item2 = new DefaultMutableTreeNode("Move Nodes");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Move Elements");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Replicate Nodes");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Replicate Elements");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Mirror Nodes");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Mirror Elements");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Model4");
		category.add(item1);
		toolbars_[13] = "Model4";
		item2 = new DefaultMutableTreeNode("Sweep");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Check");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Model5");
		category.add(item1);
		toolbars_[14] = "Model5";
		item2 = new DefaultMutableTreeNode("Import Sub-Model");
		item1.add(item2);

		// create Assign category
		category = new DefaultMutableTreeNode("Assign");
		top.add(category);

		// add items of Assign category
		item1 = new DefaultMutableTreeNode("Assign1");
		category.add(item1);
		toolbars_[15] = "Assign1";
		item2 = new DefaultMutableTreeNode("Node Assigns");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Line Assigns");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Area Assigns");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Solid Assigns");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Assign2");
		category.add(item1);
		toolbars_[16] = "Assign2";
		item2 = new DefaultMutableTreeNode("Node Info");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Element Info");
		item1.add(item2);

		// create Analysis category
		category = new DefaultMutableTreeNode("Analysis");
		top.add(category);

		// add items of Analysis category
		item1 = new DefaultMutableTreeNode("Analysis1");
		category.add(item1);
		toolbars_[17] = "Analysis1";
		item2 = new DefaultMutableTreeNode("Run");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Options");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Check Model");
		item1.add(item2);

		// create Display category
		category = new DefaultMutableTreeNode("Display");
		top.add(category);

		// add items of Display category
		item1 = new DefaultMutableTreeNode("Display1");
		category.add(item1);
		toolbars_[18] = "Display1";
		item2 = new DefaultMutableTreeNode("Undeformed Shape");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Deformed Shape");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Display2");
		category.add(item1);
		toolbars_[19] = "Display2";
		item2 = new DefaultMutableTreeNode("Assigns");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Visual Results");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Table Results");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("History Plot");
		item1.add(item2);
		item1 = new DefaultMutableTreeNode("Display3");
		category.add(item1);
		toolbars_[20] = "Display3";
		item2 = new DefaultMutableTreeNode("Visualization Options");
		item1.add(item2);
		item2 = new DefaultMutableTreeNode("Display Preferences");
		item1.add(item2);

		// create Help category
		category = new DefaultMutableTreeNode("Help");
		top.add(category);

		// add items of Help category
		item1 = new DefaultMutableTreeNode("Help1");
		category.add(item1);
		toolbars_[21] = "Help1";
		item2 = new DefaultMutableTreeNode("Help");
		item1.add(item2);
	}

	/**
	 * Calls actionOk or sets dialog unvisible depending on button clicked.
	 */
	public void actionPerformed(ActionEvent e) {

		// add button clicked
		if (e.getSource().equals(button1_))
			add();

		// remove button clicked
		else if (e.getSource().equals(button2_))
			remove();

		// ok button clicked
		else if (e.getSource().equals(button3_))
			actionOk();

		// cancel button clicked
		else if (e.getSource().equals(button4_))
			setVisible(false);
	}

	/**
	 * Sets selected toolbars visible others unvisible.
	 * 
	 */
	private void actionOk() {

		// get toolbars
		Vector<String> names = new Vector<String>();
		JToolBar[] toolbars = owner_.toolbars_.getToolbars();
		for (int i = 0; i < toolbars.length; i++) {
			names.add(toolbars[i].getName());
			toolbars[i].setVisible(false);
		}

		// loop over list items
		for (int i = 0; i < listModel1_.size(); i++) {

			// get item
			String item = (String) listModel1_.get(i);

			// set selected item visible
			toolbars[names.indexOf(item)].setVisible(true);
		}

		// close dialog
		setVisible(false);
	}

	/**
	 * Removes selected item from list.
	 * 
	 */
	private void remove() {

		// check if any item selected in list
		if (list1_.isSelectionEmpty() == false) {

			// get the selection index from list
			int selected = list1_.getSelectedIndex();

			// remove it from list
			listModel1_.remove(selected);
		}
	}

	/**
	 * Adds selected item from tree to list.
	 * 
	 */
	private void add() {

		// check if any item selected
		if (tree1_.isSelectionEmpty() == false) {

			// get selected node
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree1_
					.getLastSelectedPathComponent();

			// check if selectable
			if (checkNode(node)) {

				// get name of node
				String name = node.getUserObject().toString();

				// check if list contains
				if (listModel1_.contains(name) == false) {

					// add to list
					listModel1_.addElement(name);
				}
			}
		}
	}

	/**
	 * Returns true if the given node is selectable.
	 * 
	 * @param node
	 *            The node to be checked.
	 * @return True if the given node is selectable.
	 */
	private boolean checkNode(DefaultMutableTreeNode node) {
		for (int i = 0; i < toolbars_.length; i++)
			if (toolbars_[i].equals(node.getUserObject().toString()))
				return true;
		return false;
	}
}
