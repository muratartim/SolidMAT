package main;


import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.KeyStroke;

/**
 * Class for menu bar of the main frame.
 * 
 * @author Murat
 * 
 */
public class MenuBar extends JMenuBar {

	private static final long serialVersionUID = 1L;

	/** Owner frame of the menu bar. */
	protected SolidMAT owner_;

	/**
	 * Creates menu bar.
	 * 
	 * @param owner
	 *            The owner frame of this menu bar.
	 */
	public MenuBar(SolidMAT owner) {

		// build menu bar
		super();

		// set main frame
		owner_ = owner;

		// build menus
		createFileMenu();
		createViewMenu();
		createLibraryMenu();
		createModelMenu();
		createAssignMenu();
		createAnalysisMenu();
		createDisplayMenu();
		createHelpMenu();

		// set background color
		setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates file menu and its items.
	 * 
	 */
	private void createFileMenu() {

		// build File menu
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		add(menu);

		// set key for action command
		String key = "fileMenu";

		// build the New menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("new.jpg");
		menuItem.setIcon(image);
		menuItem.setText("New");
		menuItem.setActionCommand(key + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		// build the Open menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("open.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Open");
		menuItem.setActionCommand(key + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Save menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("save.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Save");
		menuItem.setActionCommand(key + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		// build the Save As menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Save As...");
		menuItem.setActionCommand(key + "SaveAs");
		menu.add(menuItem);

		// build the Save Image menu item
		menuItem = owner_.viewer_.getJMenuBar().getMenu(0).getItem(0);
		menuItem.setText("Save Image...");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Write Output Tables menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("writeOutput.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Write Output Tables...");
		menuItem.setActionCommand(key + "Write");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Exit menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("exit.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Exit");
		menuItem.setActionCommand(key + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates view menu and its items.
	 * 
	 */
	private void createViewMenu() {

		// build the View menu
		JMenu menu = new JMenu("View");
		menu.setMnemonic(KeyEvent.VK_V);
		add(menu);

		// set key for action command
		String key = "viewMenu";

		// build the Redraw menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("redraw.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Redraw");
		menuItem.setActionCommand(key + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the 3D Orbit menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("orbit.jpg");
		menuItem.setIcon(image);
		menuItem.setText("3D Orbit");
		menuItem.setActionCommand(key + "3DOrbit");
		menu.add(menuItem);

		// build the Zoom Window menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("zoomWindow.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom Window");
		menuItem.setActionCommand(key + "ZoomWindow");
		menu.add(menuItem);

		// build the Zoom sub menu
		JMenu subMenu1 = new JMenu("Zoom...");
		menu.add(subMenu1);

		// build the Zoom In menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("zoomIn.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom In");
		menuItem.setActionCommand(key + "ZoomIn");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Zoom Out menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("zoomOut.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom Out");
		menuItem.setActionCommand(key + "ZoomOut");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Zoom Extends menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("zoomExtends.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Zoom Extents");
		menuItem.setActionCommand(key + "ZoomExtents");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the 2D Views sub menu
		subMenu1 = new JMenu("2D Views");
		menu.add(subMenu1);

		// build the Top View menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("top.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Top View");
		menuItem.setActionCommand(key + "TopView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Bottom View menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("bottom.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Bottom View");
		menuItem.setActionCommand(key + "BottomView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Left View menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("left.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Left View");
		menuItem.setActionCommand(key + "LeftView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Right View menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("right.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Right View");
		menuItem.setActionCommand(key + "RightView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Front View menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("front.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Front View");
		menuItem.setActionCommand(key + "FrontView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Back View menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("back.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Back View");
		menuItem.setActionCommand(key + "BackView");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Isometric Views sub menu
		subMenu1 = new JMenu("Isometric Views");
		menu.add(subMenu1);

		// build the SW Isometric menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("sw.jpg");
		menuItem.setIcon(image);
		menuItem.setText("SW Isometric View");
		menuItem.setActionCommand(key + "SW");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the SE Isometric menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("se.jpg");
		menuItem.setIcon(image);
		menuItem.setText("SE Isometric View");
		menuItem.setActionCommand(key + "SE");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the NE Isometric menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("ne.jpg");
		menuItem.setIcon(image);
		menuItem.setText("NE Isometric View");
		menuItem.setActionCommand(key + "NE");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the NW Isometric menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("nw.jpg");
		menuItem.setIcon(image);
		menuItem.setText("NW Isometric View");
		menuItem.setActionCommand(key + "NW");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F10,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Set Background Color menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("background.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Set Background");
		menuItem.setActionCommand(key + "Background");
		menu.add(menuItem);

		// build the Show Axes menu item
		JCheckBoxMenuItem menuItem3 = new JCheckBoxMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("axesVisible.jpg");
		menuItem3.setIcon(image);
		menuItem3.setText("Axes Visible");
		menuItem3.setActionCommand(key + "Axes");
		menuItem3.setSelected(true);
		menu.add(menuItem3);

		// add seperator
		menu.addSeparator();

		// build the Toolbars menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("toolbars.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Toolbars...");
		menuItem.setActionCommand(key + "Toolbars");
		menu.add(menuItem);

		// build the Format menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("format1.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Format Numbers");
		menuItem.setActionCommand(key + "Format");
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates library menu and its items.
	 * 
	 */
	private void createLibraryMenu() {

		// build the Library menu
		JMenu menu = new JMenu("Library");
		menu.setMnemonic(KeyEvent.VK_L);
		add(menu);

		// set key for action command
		String key = "libraryMenu";

		// build the Elements menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("elementLib.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("materialLib.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// build the Sections menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("sectionLib.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Sections");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// build the Local Axes menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("localAxesLib.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Local Axes");
		menuItem.setActionCommand(key + "LocalAxes");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Boundary Cases menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("boundaryCase.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Boundary Cases");
		menuItem.setActionCommand(key + "BoundaryCases");
		menu.add(menuItem);

		// build the Analysis Cases menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("analysisCase.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Analysis Cases");
		menuItem.setActionCommand(key + "AnalysisCases");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Boundary Conditions sub menu
		JMenu subMenu1 = new JMenu("Boundary Conditions");
		menu.add(subMenu1);

		// build the Constraints menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("constraint.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Constraints");
		menuItem.setActionCommand(key + menuItem.getText());
		subMenu1.add(menuItem);

		// build the Nodal Displacements menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("dispLoad.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodal Displacements");
		menuItem.setActionCommand(key + "NodalDispLoads");
		subMenu1.add(menuItem);

		// build the Nodal Mechanical Loads menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("nodalMechLoad.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodal Mechanical Loads");
		menuItem.setActionCommand(key + "NodalMechLoads");
		subMenu1.add(menuItem);

		// add seperator
		subMenu1.addSeparator();

		// build the Element Mechanical Loads menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("elementMechLoad.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element Mechanical Loads");
		menuItem.setActionCommand(key + "ElementMechLoads");
		subMenu1.add(menuItem);

		// build the Element Temperature Loads menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("elementTempLoad.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element Temperature Loads");
		menuItem.setActionCommand(key + "ElementTempLoads");
		subMenu1.add(menuItem);

		// build the Initial Conditions sub menu
		subMenu1 = new JMenu("Initial Conditions");
		menu.add(subMenu1);

		// build the Initial Displacements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Initial Displacements");
		menuItem.setActionCommand(key + "InitialDisp");
		subMenu1.add(menuItem);

		// build the Initial Velocities menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Initial Velocities");
		menuItem.setActionCommand(key + "InitialVelo");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Masses sub menu
		subMenu1 = new JMenu("Additional Masses");
		menu.add(subMenu1);

		// build the Nodal Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("nodalMass.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodal Masses");
		menuItem.setActionCommand(key + "NodalMasses");
		subMenu1.add(menuItem);

		// build the Element Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("elementMass.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element Masses");
		menuItem.setActionCommand(key + "ElementMasses");
		subMenu1.add(menuItem);

		// build the Springs sub menu
		subMenu1 = new JMenu("Additional Stiffness");
		menu.add(subMenu1);

		// build the Nodal Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("nodalStiffness.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodal Stiffness");
		menuItem.setActionCommand(key + "NodalSprings");
		subMenu1.add(menuItem);

		// build the Element Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("elementStiffness.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element Stiffness");
		menuItem.setActionCommand(key + "ElementSprings");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Solvers menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("solver.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Solvers");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// build the Functions menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("function.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Functions");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates model menu and its items.
	 * 
	 */
	private void createModelMenu() {

		// build the Model menu
		JMenu menu = new JMenu("Model");
		menu.setMnemonic(KeyEvent.VK_M);
		add(menu);

		// set key for action command
		String key = "modelMenu";

		// build the Define Groups menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("group.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Define Groups");
		menuItem.setActionCommand(key + "Groups");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Add sub menu
		JMenu subMenu1 = new JMenu("Add...");
		menu.add(subMenu1);

		// build the Node menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("addNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Node");
		menuItem.setActionCommand(key + "AddNode");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Element menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("addElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element");
		menuItem.setActionCommand(key + "AddElement");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
				ActionEvent.SHIFT_MASK));
		subMenu1.add(menuItem);

		// build the Remove sub menu
		subMenu1 = new JMenu("Remove...");
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("removeNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "RemoveNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("removeElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + "RemoveElements");
		subMenu1.add(menuItem);

		// build the Edit sub menu
		subMenu1 = new JMenu("Edit...");
		menu.add(subMenu1);

		// build the Node menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("editNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Node");
		menuItem.setActionCommand(key + "EditNode");
		subMenu1.add(menuItem);

		// build the Element menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("editElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element");
		menuItem.setActionCommand(key + "EditElement");
		subMenu1.add(menuItem);

		// build the Show sub menu
		subMenu1 = new JMenu("Show...");
		menu.add(subMenu1);

		// build the Node menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("showNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Node");
		menuItem.setActionCommand(key + "ShowNode");
		subMenu1.add(menuItem);

		// build the Element menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("showElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element");
		menuItem.setActionCommand(key + "ShowElement");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Divide Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("divideLines.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Divide Lines");
		menuItem.setActionCommand(key + "Divide");
		menu.add(menuItem);

		// build the Mesh Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("meshAreas.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Mesh Areas");
		menuItem.setActionCommand(key + "MeshAreas");
		menu.add(menuItem);

		// build the Mesh Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("meshSolids.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Mesh Solids");
		menuItem.setActionCommand(key + "MeshSolids");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Move sub menu
		subMenu1 = new JMenu("Move...");
		menu.add(subMenu1);

		// build the Move Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("moveNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "MoveNodes");
		subMenu1.add(menuItem);

		// build the Move Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("moveElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + "MoveElements");
		subMenu1.add(menuItem);

		// build the Replicate sub menu
		subMenu1 = new JMenu("Replicate...");
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("replicateNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "ReplicateNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("replicateElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + "ReplicateElements");
		subMenu1.add(menuItem);

		// build the Mirror sub menu
		subMenu1 = new JMenu("Mirror...");
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("mirrorNode.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "MirrorNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("mirrorElement.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + "MirrorElements");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Sweep menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("sweep.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Sweep...");
		menuItem.setActionCommand(key + "Sweep");
		menu.add(menuItem);

		// build the Check menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("check.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Check...");
		menuItem.setActionCommand(key + "Check");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Import Sub-Model menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("import.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Import Sub-Model");
		menuItem.setActionCommand(key + "Import");
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates assign menu and its items.
	 * 
	 */
	private void createAssignMenu() {

		// build the Assign menu
		JMenu menu = new JMenu("Assign");
		menu.setMnemonic(KeyEvent.VK_S);
		add(menu);

		// set key for action command
		String key = "assignMenu";

		// build the Node sub menu
		JMenu subMenu1 = new JMenu("Node...");
		ImageIcon image = ImageHandler.createImageIcon("nodeAssign.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Constraints menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Constraints");
		menuItem.setActionCommand(key + menuItem.getText());
		subMenu1.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "NodalSprings");
		subMenu1.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "NodalMasses");
		subMenu1.add(menuItem);

		// build Local Axes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Local Axes");
		menuItem.setActionCommand(key + "NodalAxes");
		subMenu1.add(menuItem);

		// build the Line sub menu
		subMenu1 = new JMenu("Line...");
		image = ImageHandler.createImageIcon("lineAssign.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + "LineMaterials");
		subMenu1.add(menuItem);

		// build the Sections menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Sections");
		menuItem.setActionCommand(key + "LineSections");
		subMenu1.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "LineSprings");
		subMenu1.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "LineMasses");
		subMenu1.add(menuItem);

		// build the Local Axes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Local Axes");
		menuItem.setActionCommand(key + "LineLocalAxes");
		subMenu1.add(menuItem);

		// build the Area sub menu
		subMenu1 = new JMenu("Area...");
		image = ImageHandler.createImageIcon("areaAssign.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + "AreaMaterials");
		subMenu1.add(menuItem);

		// build the Sections menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Sections");
		menuItem.setActionCommand(key + "AreaSections");
		subMenu1.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "AreaSprings");
		subMenu1.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "AreaMasses");
		subMenu1.add(menuItem);

		// build the Solid sub menu
		subMenu1 = new JMenu("Solid...");
		image = ImageHandler.createImageIcon("solidAssign.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Materials menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Materials");
		menuItem.setActionCommand(key + "SolidMaterials");
		subMenu1.add(menuItem);

		// build the Springs menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Springs");
		menuItem.setActionCommand(key + "SolidSprings");
		subMenu1.add(menuItem);

		// build the Masses menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Masses");
		menuItem.setActionCommand(key + "SolidMasses");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Nodal Loads sub menu
		subMenu1 = new JMenu("Nodal Loads");
		menu.add(subMenu1);

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical");
		menuItem.setActionCommand(key + "NodalMechLoads");
		subMenu1.add(menuItem);

		// build the Displacements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Displacements");
		menuItem.setActionCommand(key + "Displacements");
		subMenu1.add(menuItem);

		// build the Initial Displacements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Initial Displacements");
		menuItem.setActionCommand(key + "InitialDisplacements");
		subMenu1.add(menuItem);

		// build the Initial Velocities menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Initial Velocities");
		menuItem.setActionCommand(key + "InitialVelocities");
		subMenu1.add(menuItem);

		// build the Line Loads sub menu
		subMenu1 = new JMenu("Line Loads");
		menu.add(subMenu1);

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical");
		menuItem.setActionCommand(key + "LineMechLoads");
		subMenu1.add(menuItem);

		// build the Temperature menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Temperature");
		menuItem.setActionCommand(key + "LineTempLoads");
		subMenu1.add(menuItem);

		// build the Area Loads sub menu
		subMenu1 = new JMenu("Area Loads");
		menu.add(subMenu1);

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical");
		menuItem.setActionCommand(key + "AreaMechLoads");
		subMenu1.add(menuItem);

		// build the Temperature menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Temperature");
		menuItem.setActionCommand(key + "AreaTempLoads");
		subMenu1.add(menuItem);

		// build the Solid Loads sub menu
		subMenu1 = new JMenu("Solid Loads");
		menu.add(subMenu1);

		// build the Mechanical menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Mechanical");
		menuItem.setActionCommand(key + "SolidMechLoads");
		subMenu1.add(menuItem);

		// build the Temperature menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Temperature");
		menuItem.setActionCommand(key + "SolidTempLoads");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Node Info menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("nodeInfo.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Node Info...");
		menuItem.setActionCommand(key + "NodeInfo");
		menu.add(menuItem);

		// build the Element Info menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("elementInfo.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Element Info...");
		menuItem.setActionCommand(key + "ElementInfo");
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates analysis menu and its items.
	 * 
	 */
	private void createAnalysisMenu() {

		// build the Analysis menu
		JMenu menu = new JMenu("Analysis");
		menu.setMnemonic(KeyEvent.VK_A);
		add(menu);

		// set key for action command
		String key = "analysisMenu";

		// build the Options menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("options.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Options...");
		menuItem.setActionCommand(key + "Options");
		menu.add(menuItem);

		// build the Check Model menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("checkModel.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Check Model");
		menuItem.setActionCommand(key + "Check");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F11,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Run menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("run.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Run");
		menuItem.setActionCommand(key + menuItem.getText());
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates display menu and its items.
	 * 
	 */
	private void createDisplayMenu() {

		// build the Display menu
		JMenu menu = new JMenu("Display");
		menu.setMnemonic(KeyEvent.VK_D);
		add(menu);

		// set key for action command
		String key = "displayMenu";

		// build the Undeformed Shape menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("undeformed.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Undeformed Shape");
		menuItem.setActionCommand(key + "Undeformed");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// build the Load Assigns sub menu
		JMenu subMenu1 = new JMenu("Load Assigns");
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "LoadAssignsNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + "LoadAssignsElements");
		subMenu1.add(menuItem);

		// build the Misc Assigns sub menu
		subMenu1 = new JMenu("Misc Assigns");
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "MiscAssignsNodes");
		subMenu1.add(menuItem);

		// build the Elements menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Elements");
		menuItem.setActionCommand(key + "MiscAssignsElements");
		subMenu1.add(menuItem);

		// build the Function Plot menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Function Plot");
		menuItem.setActionCommand(key + "FunctionPlot");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Deformed Shape menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("deformed.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Deformed Shape...");
		menuItem.setActionCommand(key + "Deformed");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// build the Visual Results sub menu
		subMenu1 = new JMenu("Visual Results");
		image = ImageHandler.createImageIcon("visual.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "VResultsNodes");
		subMenu1.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand(key + "VResultsLines");
		subMenu1.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand(key + "VResultsAreas");
		subMenu1.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand(key + "VResultsSolids");
		subMenu1.add(menuItem);

		// build the Table Results sub menu
		subMenu1 = new JMenu("Table Results");
		image = ImageHandler.createImageIcon("table.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "TResultsNodes");
		subMenu1.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand(key + "TResultsLines");
		subMenu1.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand(key + "TResultsAreas");
		subMenu1.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand(key + "TResultsSolids");
		subMenu1.add(menuItem);

		// build the History Plot sub menu
		subMenu1 = new JMenu("History Plot");
		image = ImageHandler.createImageIcon("historyPlot.jpg");
		subMenu1.setIcon(image);
		menu.add(subMenu1);

		// build the Nodes menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Nodes");
		menuItem.setActionCommand(key + "PlotNodes");
		subMenu1.add(menuItem);

		// build the Lines menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Lines");
		menuItem.setActionCommand(key + "PlotLines");
		subMenu1.add(menuItem);

		// build the Areas menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Areas");
		menuItem.setActionCommand(key + "PlotAreas");
		subMenu1.add(menuItem);

		// build the Solids menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Solids");
		menuItem.setActionCommand(key + "PlotSolids");
		subMenu1.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Options menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("vizOptions.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Options...");
		menuItem.setActionCommand(key + "Options");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// build the Preferences menu item
		menuItem = new JMenuItem(owner_.action_);
		image = ImageHandler.createImageIcon("preferences.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Preferences...");
		menuItem.setActionCommand(key + "Preferences");
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}

	/**
	 * Creates help menu and its items.
	 * 
	 */
	private void createHelpMenu() {

		// build the Help menu
		JMenu menu = new JMenu("Help");
		menu.setMnemonic(KeyEvent.VK_H);
		add(menu);

		// set key for action command
		String key = "helpMenu";

		// build the SolidMAT Help menu item
		JMenuItem menuItem = new JMenuItem(owner_.action_);
		ImageIcon image = ImageHandler.createImageIcon("help.jpg");
		menuItem.setIcon(image);
		menuItem.setText("Help");
		menuItem.setActionCommand(key + "SolidMATHelp");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				ActionEvent.CTRL_MASK));
		menu.add(menuItem);

		// build the Key Assist menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Key Assist...");
		menuItem.setActionCommand(key + "KeyAssist");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				ActionEvent.SHIFT_MASK));
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the Introduction & Overview menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Introduction & Overview");
		menuItem.setActionCommand(key + "Introduction");
		menu.add(menuItem);

		// build the Volume A: Theory & Analysis Reference menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Volume A: Theory & Analysis Reference");
		menuItem.setActionCommand(key + "VolA");
		menu.add(menuItem);

		// build the Volume B: Element Library menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Volume B: Element Library");
		menuItem.setActionCommand(key + "VolB");
		menu.add(menuItem);

		// build the Volume C: Command Reference menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("Volume C: Command Reference");
		menuItem.setActionCommand(key + "VolC");
		menu.add(menuItem);

		// add seperator
		menu.addSeparator();

		// build the About SolidMAT menu item
		menuItem = new JMenuItem(owner_.action_);
		menuItem.setText("About");
		menuItem.setActionCommand(key + menuItem.getText());
		menu.add(menuItem);

		// set background color
		menu.setBackground(new Color(240, 240, 240));
	}
}
