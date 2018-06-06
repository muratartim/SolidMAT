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
package main;

import java.awt.Container;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

/**
 * Class for creating toolbars for the main frame.
 * 
 * @author Murat Artim
 * 
 */
public class Toolbars {

	/** Static variable for the index of toolbar. */
	public static final int view1_ = 0, view2_ = 1, view3_ = 2, view4_ = 3,
			file1_ = 4, file2_ = 5, library1_ = 6, library2_ = 7,
			library3_ = 8, library4_ = 9, model1_ = 10, model2_ = 11,
			model3_ = 12, model4_ = 13, model5_ = 14, assign1_ = 15,
			assign2_ = 16, analysis1_ = 17, display1_ = 18, display2_ = 19,
			display3_ = 20, help1_ = 21;

	/** Owner frame of toolbars. */
	private SolidMAT owner_;

	/** Toolbars created for main frame. */
	private JToolBar[] toolbars_ = new JToolBar[22];

	/**
	 * Creates toolbars for the main frame.
	 * 
	 * @param owner
	 *            The owner frame of toolbars.
	 */
	public Toolbars(SolidMAT owner) {

		// set main frame
		owner_ = owner;

		// initialize
		initialize();

		// create toolbars
		createViewToolbars();
		createFileToolbars();
		createLibraryToolbars();
		createModelToolbars();
		createAssignToolbars();
		createAnalysisToolbars();
		createDisplayToolbars();
		createHelpToolbars();

		// set background color for toolbars
		Color c = new Color(240, 240, 240);
		for (int i = 0; i < toolbars_.length; i++)
			for (int j = 0; j < toolbars_[i].getComponentCount(); j++)
				toolbars_[i].getComponent(j).setBackground(c);
	}

	/**
	 * Returns the demanded toolbar.
	 * 
	 * @param index
	 *            The index of demanded toolbar.
	 * @return The demanded toolbar.
	 */
	public JToolBar getToolbar(int index) {
		return toolbars_[index];
	}

	/**
	 * Returns array containing all toolbars.
	 * 
	 * @return Array containing all toolbars.
	 */
	public JToolBar[] getToolbars() {
		return toolbars_;
	}

	/**
	 * Initializes this object by getting view toolbars.
	 * 
	 */
	private void initialize() {

		// get main toolbar
		Container container = owner_.viewer_.getContentPane();
		JToolBar toolBar = (JToolBar) container.getComponent(2);
		container.remove(2);

		// get first sub-toolbar
		toolbars_[view1_] = (JToolBar) toolBar.getComponent(0);

		// get second sub-toolbar
		toolbars_[view2_] = (JToolBar) toolBar.getComponent(1);
	}

	/**
	 * Creates View toolbars.
	 * 
	 */
	private void createViewToolbars() {

		// build the first toolbar
		toolbars_[view1_].setName("View1");

		// set tooltip texts to buttons of first sub-toolbar
		JToggleButton button1 = (JToggleButton) toolbars_[0].getComponent(0);
		button1.setToolTipText("3D Orbit");
		button1 = (JToggleButton) toolbars_[0].getComponent(1);
		button1.setToolTipText("Zoom Window");
		JButton button2 = (JButton) toolbars_[0].getComponent(3);
		button2.setToolTipText("Zoom In");
		button2 = (JButton) toolbars_[0].getComponent(4);
		button2.setToolTipText("Zoom Out");
		button2 = (JButton) toolbars_[0].getComponent(5);
		button2.setToolTipText("Zoom Extents");

		// build the second toolbar
		toolbars_[view2_].setName("View2");

		// set tooltip texts to buttons of second sub-toolbar
		button2 = (JButton) toolbars_[1].getComponent(0);
		button2.setToolTipText("Top View");
		button2 = (JButton) toolbars_[1].getComponent(1);
		button2.setToolTipText("Bottom View");
		button2 = (JButton) toolbars_[1].getComponent(2);
		button2.setToolTipText("Left View");
		button2 = (JButton) toolbars_[1].getComponent(3);
		button2.setToolTipText("Right View");
		button2 = (JButton) toolbars_[1].getComponent(4);
		button2.setToolTipText("Front View");
		button2 = (JButton) toolbars_[1].getComponent(5);
		button2.setToolTipText("Back View");
		button2 = (JButton) toolbars_[1].getComponent(7);
		button2.setToolTipText("SW Isometric View");
		button2 = (JButton) toolbars_[1].getComponent(8);
		button2.setToolTipText("SE Isometric View");
		button2 = (JButton) toolbars_[1].getComponent(9);
		button2.setToolTipText("NE Isometric View");
		button2 = (JButton) toolbars_[1].getComponent(10);
		button2.setToolTipText("NW Isometric View");

		// build third toolbar
		toolbars_[view3_] = new JToolBar("View3");

		// build Redraw button for third toolbar
		button2 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("redraw1.jpg");
		button2.setIcon(image);
		button2.setActionCommand("viewMenu" + "Redraw");
		button2.setToolTipText("Redraw");
		toolbars_[view3_].add(button2);

		// add seperator
		toolbars_[view3_].addSeparator();

		// build Background button for third toolbar
		button2 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("background.jpg");
		button2.setIcon(image);
		button2.setActionCommand("viewMenu" + "Background");
		button2.setToolTipText("Set Background");
		toolbars_[view3_].add(button2);

		// build Show Axes button for third toolbar
		button1 = new JToggleButton(owner_.action_);
		image = ImageHandler.createImageIcon("axesVisible1.jpg");
		button1.setIcon(image);
		button1.setActionCommand("viewMenu" + "Axes");
		button1.setSelected(true);
		button1.setToolTipText("Axes Visible");
		toolbars_[view3_].add(button1);

		// build fourth toolbar
		toolbars_[view4_] = new JToolBar("View4");

		// build Customize Toolbars button for fourth toolbar
		button2 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("toolbars1.jpg");
		button2.setIcon(image);
		button2.setActionCommand("viewMenu" + "Toolbars");
		button2.setToolTipText("Customize Toolbars");
		toolbars_[view4_].add(button2);

		// build Show Axes button for third toolbar
		button1 = new JToggleButton(owner_.action_);
		image = ImageHandler.createImageIcon("format1.jpg");
		button1.setIcon(image);
		button1.setActionCommand("viewMenu" + "Format");
		button1.setSelected(true);
		button1.setToolTipText("Format Numbers");
		toolbars_[view4_].add(button1);
	}

	/**
	 * Creates File toolbars.
	 * 
	 */
	private void createFileToolbars() {

		// set key for action command
		String key = "fileMenu";

		// build first toolbar
		toolbars_[file1_] = new JToolBar("File1");

		// build New button for third toolbar
		JButton button1 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("new1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "New");
		button1.setToolTipText("New");
		toolbars_[file1_].add(button1);

		// build Open button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("open1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Open");
		button1.setToolTipText("Open");
		toolbars_[file1_].add(button1);

		// build Save button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("save1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Save");
		button1.setToolTipText("Save");
		toolbars_[file1_].add(button1);

		// build second toolbar
		toolbars_[file2_] = new JToolBar("File2");

		// build Write Output Tables button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("writeOutput1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Write");
		button1.setToolTipText("Write Output Tables");
		toolbars_[file2_].add(button1);
	}

	/**
	 * Creates Library toolbars.
	 * 
	 */
	private void createLibraryToolbars() {

		// set key for action command
		String key = "libraryMenu";

		// build first toolbar
		toolbars_[library1_] = new JToolBar("Library1", JToolBar.VERTICAL);

		// build Elements button for third toolbar
		JButton button1 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("elementLib1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Elements");
		button1.setToolTipText("Elements");
		toolbars_[library1_].add(button1);

		// build Materials button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("materialLib1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Materials");
		button1.setToolTipText("Materials");
		toolbars_[library1_].add(button1);

		// build Sections button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("sectionLib1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Sections");
		button1.setToolTipText("Sections");
		toolbars_[library1_].add(button1);

		// build Local Axes button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("localAxesLib1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "LocalAxes");
		button1.setToolTipText("Local Axes");
		toolbars_[library1_].add(button1);

		// add seperator
		toolbars_[library1_].addSeparator();

		// build Solvers button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("solver1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Solvers");
		button1.setToolTipText("Solvers");
		toolbars_[library1_].add(button1);

		// build Functions button for third toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("function1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Functions");
		button1.setToolTipText("Functions");
		toolbars_[library1_].add(button1);

		// build second toolbar
		toolbars_[library2_] = new JToolBar("Library2", JToolBar.VERTICAL);

		// build Boundary Cases button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("boundaryCase1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "BoundaryCases");
		button1.setToolTipText("Boundary Cases");
		toolbars_[library2_].add(button1);

		// build Analysis Cases button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("analysisCase1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "AnalysisCases");
		button1.setToolTipText("Analysis Cases");
		toolbars_[library2_].add(button1);

		// build third toolbar
		toolbars_[library3_] = new JToolBar("Library3", JToolBar.VERTICAL);

		// build Constraints button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("constraint1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Constraints");
		button1.setToolTipText("Constraints");
		toolbars_[library3_].add(button1);

		// build Nodal Disp button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("dispLoad1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "NodalDispLoads");
		button1.setToolTipText("Nodal Displacements");
		toolbars_[library3_].add(button1);

		// build Nodal Mech Load button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("nodalMechLoad1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "NodalMechLoads");
		button1.setToolTipText("Nodal Mechanical Loads");
		toolbars_[library3_].add(button1);

		// add seperator
		toolbars_[library3_].addSeparator();

		// build Element Mech Load button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("elementMechLoad1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ElementMechLoads");
		button1.setToolTipText("Element Mechanical Loads");
		toolbars_[library3_].add(button1);

		// build Element Temp Load button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("elementTempLoad1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ElementTempLoads");
		button1.setToolTipText("Element Temperature Loads");
		toolbars_[library3_].add(button1);

		// build fourth toolbar
		toolbars_[library4_] = new JToolBar("Library4", JToolBar.VERTICAL);

		// build Nodal Masses button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("nodalMass1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "NodalMasses");
		button1.setToolTipText("Nodal Masses");
		toolbars_[library4_].add(button1);

		// build Element Masses button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("elementMass1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ElementMasses");
		button1.setToolTipText("Element Masses");
		toolbars_[library4_].add(button1);

		// add seperator
		toolbars_[library4_].addSeparator();

		// build Nodal Stiffness button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("nodalStiffness1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "NodalSprings");
		button1.setToolTipText("Nodal Stiffness");
		toolbars_[library4_].add(button1);

		// build Element Stiffness button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("elementStiffness1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ElementSprings");
		button1.setToolTipText("Element Stiffness");
		toolbars_[library4_].add(button1);
	}

	/**
	 * Creates Model toolbars.
	 * 
	 */
	private void createModelToolbars() {

		// set key for action command
		String key = "modelMenu";

		// build first toolbar
		toolbars_[model1_] = new JToolBar("Model1", JToolBar.VERTICAL);

		// build Add Node button for first toolbar
		JButton button1 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("addNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "AddNode");
		button1.setToolTipText("Add Node");
		toolbars_[model1_].add(button1);

		// build Add Element button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("addElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "AddElement");
		button1.setToolTipText("Add Element");
		toolbars_[model1_].add(button1);

		// add seperator
		toolbars_[model1_].addSeparator();

		// build Remove Nodes button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("removeNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "RemoveNodes");
		button1.setToolTipText("Remove Nodes");
		toolbars_[model1_].add(button1);

		// build Remove Elements button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("removeElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "RemoveElements");
		button1.setToolTipText("Remove Elements");
		toolbars_[model1_].add(button1);

		// add seperator
		toolbars_[model1_].addSeparator();

		// build Edit Node button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("editNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "EditNode");
		button1.setToolTipText("Edit Node");
		toolbars_[model1_].add(button1);

		// build Edit Element button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("editElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "EditElement");
		button1.setToolTipText("Edit Element");
		toolbars_[model1_].add(button1);

		// add seperator
		toolbars_[model1_].addSeparator();

		// build Show Node button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("showNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ShowNode");
		button1.setToolTipText("Show Node");
		toolbars_[model1_].add(button1);

		// build Show Element button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("showElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ShowElement");
		button1.setToolTipText("Show Element");
		toolbars_[model1_].add(button1);

		// build second toolbar
		toolbars_[model2_] = new JToolBar("Model2", JToolBar.VERTICAL);

		// build Divide Lines button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("divideLines1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Divide");
		button1.setToolTipText("Divide Lines");
		toolbars_[model2_].add(button1);

		// build Mesh Areas button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("meshAreas1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "MeshAreas");
		button1.setToolTipText("Mesh Areas");
		toolbars_[model2_].add(button1);

		// build Mesh Solids button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("meshSolids1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "MeshSolids");
		button1.setToolTipText("Mesh Solids");
		toolbars_[model2_].add(button1);

		// build second toolbar
		toolbars_[model3_] = new JToolBar("Model3", JToolBar.VERTICAL);

		// build Move Nodes button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("moveNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "MoveNodes");
		button1.setToolTipText("Move Nodes");
		toolbars_[model3_].add(button1);

		// build Move Elements button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("moveElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "MoveElements");
		button1.setToolTipText("Move Elements");
		toolbars_[model3_].add(button1);

		// add seperator
		toolbars_[model3_].addSeparator();

		// build Replicate Nodes button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("replicateNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ReplicateNodes");
		button1.setToolTipText("Replicate Nodes");
		toolbars_[model3_].add(button1);

		// build Replicate Elements button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("replicateElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ReplicateElements");
		button1.setToolTipText("Replicate Elements");
		toolbars_[model3_].add(button1);

		// add seperator
		toolbars_[model3_].addSeparator();

		// build Mirror Nodes button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("mirrorNode1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "MirrorNodes");
		button1.setToolTipText("Mirror Nodes");
		toolbars_[model3_].add(button1);

		// build Mirror Elements button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("mirrorElement1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "MirrorElements");
		button1.setToolTipText("Mirror Elements");
		toolbars_[model3_].add(button1);

		// build fourth toolbar
		toolbars_[model4_] = new JToolBar("Model4", JToolBar.VERTICAL);

		// build Define Groups button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("group1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Groups");
		button1.setToolTipText("Define Groups");
		toolbars_[model4_].add(button1);

		// add seperator
		toolbars_[model4_].addSeparator();

		// build Sweep button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("sweep1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Sweep");
		button1.setToolTipText("Sweep");
		toolbars_[model4_].add(button1);

		// build Check button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("check1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Check");
		button1.setToolTipText("Check");
		toolbars_[model4_].add(button1);

		// build fifth toolbar
		toolbars_[model5_] = new JToolBar("Model5", JToolBar.VERTICAL);

		// build Import Sub-Model button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("import1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Import");
		button1.setToolTipText("Import Sub-Model");
		toolbars_[model5_].add(button1);
	}

	/**
	 * Creates Assign toolbars.
	 * 
	 */
	private void createAssignToolbars() {

		// set key for action command
		String key = "assignMenu";

		// build first toolbar
		toolbars_[assign1_] = new JToolBar("Assign1");

		// build menu bar for assign menus
		JMenuBar menuBar = new JMenuBar();
		menuBar.setAlignmentY(JMenuBar.CENTER_ALIGNMENT);
		toolbars_[assign1_].add(menuBar);

		// build Node Assigns menu for first toolbar
		JMenu menu = new JMenu();
		ImageIcon image = ImageHandler.createImageIcon("nodeAssign1.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Node Assigns");
		menuBar.add(menu);

		// build the Constraints menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Constraints");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "NodalSprings");
		menu.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "NodalMasses");
		menu.add(menuItem);

		// build Local Axes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Local Axes");
		menuItem.setActionCommand(key + "NodalAxes");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical Loads");
		menuItem.setActionCommand(key + "NodalMechLoads");
		menu.add(menuItem);

		// build the Displacements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Displacements");
		menuItem.setActionCommand(key + "Displacements");
		menu.add(menuItem);

		// build the Initial Displacements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Initial Displacements");
		menuItem.setActionCommand(key + "InitialDisplacements");
		menu.add(menuItem);

		// build the Initial Velocities menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Initial Velocities");
		menuItem.setActionCommand(key + "InitialVelocities");
		menu.add(menuItem);

		// build Line Assigns menu for first toolbar
		menu = new JMenu();
		image = ImageHandler.createImageIcon("lineAssign1.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Line Assigns");
		menuBar.add(menu);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + "LineMaterials");
		menu.add(menuItem);

		// build the Sections menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Sections");
		menuItem.setActionCommand(key + "LineSections");
		menu.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "LineSprings");
		menu.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "LineMasses");
		menu.add(menuItem);

		// build the Local Axes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Local Axes");
		menuItem.setActionCommand(key + "LineLocalAxes");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical Loads");
		menuItem.setActionCommand(key + "LineMechLoads");
		menu.add(menuItem);

		// build the Temperature menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Temperature Loads");
		menuItem.setActionCommand(key + "LineTempLoads");
		menu.add(menuItem);

		// build Area Assigns menu for first toolbar
		menu = new JMenu();
		image = ImageHandler.createImageIcon("areaAssign1.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Area Assigns");
		menuBar.add(menu);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + "AreaMaterials");
		menu.add(menuItem);

		// build the Sections menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Sections");
		menuItem.setActionCommand(key + "AreaSections");
		menu.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "AreaSprings");
		menu.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "AreaMasses");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical Loads");
		menuItem.setActionCommand(key + "AreaMechLoads");
		menu.add(menuItem);

		// build the Temperature menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Temperature Loads");
		menuItem.setActionCommand(key + "AreaTempLoads");
		menu.add(menuItem);

		// build Solid Assigns menu for first toolbar
		menu = new JMenu();
		image = ImageHandler.createImageIcon("solidAssign1.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Solid Assigns");
		menuBar.add(menu);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + "SolidMaterials");
		menu.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "SolidSprings");
		menu.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "SolidMasses");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical Loads");
		menuItem.setActionCommand(key + "SolidMechLoads");
		menu.add(menuItem);

		// build the Temperature menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Temperature Loads");
		menuItem.setActionCommand(key + "SolidTempLoads");
		menu.add(menuItem);

		// build second toolbar
		toolbars_[assign2_] = new JToolBar("Assign2");

		// build Node Info button for second toolbar
		JButton button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("nodeInfo1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "NodeInfo");
		button1.setToolTipText("Node Info");
		toolbars_[assign2_].add(button1);

		// build Element Info button for second toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("elementInfo1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "ElementInfo");
		button1.setToolTipText("Element Info");
		toolbars_[assign2_].add(button1);
	}

	/**
	 * Creates Analysis toolbars.
	 * 
	 */
	private void createAnalysisToolbars() {

		// set key for action command
		String key = "analysisMenu";

		// build first toolbar
		toolbars_[analysis1_] = new JToolBar("Analysis1");

		// build Run button for first toolbar
		JButton button1 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("run1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Run");
		button1.setToolTipText("Run");
		toolbars_[analysis1_].add(button1);

		// add seperator
		toolbars_[analysis1_].addSeparator();

		// build Options button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("options.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Options");
		button1.setToolTipText("Options");
		toolbars_[analysis1_].add(button1);

		// build Check Model button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("checkModel1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Check");
		button1.setToolTipText("Check Model");
		toolbars_[analysis1_].add(button1);
	}

	/**
	 * Creates Display toolbars.
	 * 
	 */
	private void createDisplayToolbars() {

		// set key for action command
		String key = "displayMenu";

		// build first toolbar
		toolbars_[display1_] = new JToolBar("Display1");

		// build Undeformed Shape button for first toolbar
		JButton button1 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("undeformed.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Undeformed");
		button1.setToolTipText("Undeformed Shape");
		toolbars_[display1_].add(button1);

		// build Deformed Shape button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("deformed1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Deformed");
		button1.setToolTipText("Deformed Shape");
		toolbars_[display1_].add(button1);

		// build first toolbar
		toolbars_[display2_] = new JToolBar("Display2");

		// build menu bar for display menus
		JMenuBar menuBar = new JMenuBar();
		menuBar.setAlignmentY(JMenuBar.CENTER_ALIGNMENT);
		toolbars_[display2_].add(menuBar);

		// build Assigns menu for second toolbar
		JMenu menu = new JMenu();
		image = ImageHandler.createImageIcon("assigns.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Assigns");
		menuBar.add(menu);

		// build the Nodes menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Node Load Assigns");
		menuItem.setActionCommand(key + "LoadAssignsNodes");
		menu.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Element Load Assigns");
		menuItem.setActionCommand(key + "LoadAssignsElements");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Node Misc Assigns menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Node Misc Assigns");
		menuItem.setActionCommand(key + "MiscAssignsNodes");
		menu.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Element Misc Assigns");
		menuItem.setActionCommand(key + "MiscAssignsElements");
		menu.add(menuItem);

		// build Visual Results menu for first toolbar
		menu = new JMenu();
		image = ImageHandler.createImageIcon("visual.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Visual Results");
		menuBar.add(menu);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "VResultsNodes");
		menu.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand(key + "VResultsLines");
		menu.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand(key + "VResultsAreas");
		menu.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand(key + "VResultsSolids");
		menu.add(menuItem);

		// build Table Results menu for first toolbar
		menu = new JMenu();
		image = ImageHandler.createImageIcon("table1.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("Table Results");
		menuBar.add(menu);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "TResultsNodes");
		menu.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand(key + "TResultsLines");
		menu.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand(key + "TResultsAreas");
		menu.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand(key + "TResultsSolids");
		menu.add(menuItem);

		// build History Plot menu for first toolbar
		menu = new JMenu();
		image = ImageHandler.createImageIcon("historyPlot1.jpg");
		menu.setIcon(image);
		menu.setBackground(new Color(240, 240, 240));
		menu.setToolTipText("History Plot");
		menuBar.add(menu);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "PlotNodes");
		menu.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand(key + "PlotLines");
		menu.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand(key + "PlotAreas");
		menu.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand(key + "PlotSolids");
		menu.add(menuItem);

		// build first toolbar
		toolbars_[display3_] = new JToolBar("Display3");

		// build Options button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("vizOptions1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Options");
		button1.setToolTipText("Visualization Options");
		toolbars_[display3_].add(button1);

		// build Preferences button for first toolbar
		button1 = new JButton(owner_.action_);
		image = ImageHandler.createImageIcon("preferences1.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "Preferences");
		button1.setToolTipText("Display Preferences");
		toolbars_[display3_].add(button1);
	}

	/**
	 * Creates Help toolbars.
	 * 
	 */
	private void createHelpToolbars() {

		// set key for action command
		String key = "helpMenu";

		// build first toolbar
		toolbars_[help1_] = new JToolBar("Help1");

		// build Help button for first toolbar
		JButton button1 = new JButton(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("help.jpg");
		button1.setIcon(image);
		button1.setActionCommand(key + "SolidMATHelp");
		button1.setToolTipText("Help");
		toolbars_[help1_].add(button1);
	}
}
